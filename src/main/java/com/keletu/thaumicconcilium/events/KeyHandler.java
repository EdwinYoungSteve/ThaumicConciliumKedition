package com.keletu.thaumicconcilium.events;

import com.keletu.thaumicconcilium.ThaumicConcilium;
import com.keletu.thaumicconcilium.packet.PacketTogglePontifexRobe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber
public class KeyHandler {
    public static KeyBinding toggleRobe;
    private static boolean keyPressedToggleRobe = false;

    @SideOnly(Side.CLIENT)
    public static void registerKeybinds() {
        toggleRobe = new KeyBinding("key.toggle_pontifex", Keyboard.KEY_C, "key.categories.thaumicconcilium");

        ClientRegistry.registerKeyBinding(toggleRobe);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onKeyInput(TickEvent.ClientTickEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (event.phase != TickEvent.Phase.START || Minecraft.getMinecraft().isGamePaused() || player == null)
            return;

        if (toggleRobe.isPressed()) {
            if (FMLClientHandler.instance().getClient().inGameHasFocus) {
                if (!keyPressedToggleRobe) {
                    ThaumicConcilium.packetInstance.sendToServer(new PacketTogglePontifexRobe(player));
                }
                keyPressedToggleRobe = true;
            }
        } else {
            keyPressedToggleRobe = false;
        }
    }
}
