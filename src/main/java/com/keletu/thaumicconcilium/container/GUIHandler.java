package com.keletu.thaumicconcilium.container;

import com.keletu.thaumicconcilium.client.gui.GuiThaumaturge;
import com.keletu.thaumicconcilium.entity.EntityThaumaturge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;


public class GUIHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 1:
                return new ContainerThaumaturge(player.inventory, world, (EntityThaumaturge) world.getEntityByID(x));
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 1:
                return new GuiThaumaturge(player.inventory, world, (EntityThaumaturge) world.getEntityByID(x));
        }
        return null;
    }
}