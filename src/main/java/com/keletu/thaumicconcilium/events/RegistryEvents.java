package com.keletu.thaumicconcilium.events;

import com.keletu.thaumicconcilium.ThaumicConcilium;
import com.keletu.thaumicconcilium.blocks.*;
import com.keletu.thaumicconcilium.items.ItemVisConductor;
import com.keletu.thaumicconcilium.items.TCItems;
import com.keletu.thaumicconcilium.packet.PacketFXLightning;
import com.keletu.thaumicconcilium.util.PolishRecipe;
import fr.wind_blade.isorropia.common.IsorropiaAPI;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectEventProxy;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.AspectRegistryEvent;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.tiles.crafting.TileInfusionMatrix;
import thaumcraft.common.tiles.crafting.TilePedestal;

import java.util.Random;

@Mod.EventBusSubscriber
public class RegistryEvents {

    @SubscribeEvent
    public static void regBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(TCBlocks.quicksilver_crucible);
        event.getRegistry().registerAll(TCBlocks.destabilized_crystal);
        event.getRegistry().registerAll(TCBlocks.vis_condenser);
        event.getRegistry().registerAll(TCBlocks.hex_of_predictability);

        GameRegistry.registerTileEntity(TileQuicksilverCrucible.class, new ResourceLocation(ThaumicConcilium.MODID, "quicksilver_crucible"));
        GameRegistry.registerTileEntity(TileDestabilizedCrystal.class, new ResourceLocation(ThaumicConcilium.MODID, "destabilized_crystal"));
        GameRegistry.registerTileEntity(TileVisCondenser.class, new ResourceLocation(ThaumicConcilium.MODID, "vis_condenser"));
        GameRegistry.registerTileEntity(TileHexOfPredictability.class, new ResourceLocation(ThaumicConcilium.MODID, "hex_of_predictability"));
    }

    @SubscribeEvent
    public static void regItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(TCItems.pontifex_hood);
        event.getRegistry().registerAll(TCItems.pontifex_robe);
        event.getRegistry().registerAll(TCItems.pontifex_legs);
        event.getRegistry().registerAll(TCItems.pontifex_boots);
        event.getRegistry().registerAll(TCItems.molot);
        event.getRegistry().registerAll(TCItems.crimson_annales);
        event.getRegistry().registerAll(TCItems.research_notes_crimson);
        event.getRegistry().registerAll(TCItems.runic_chestplate);
        event.getRegistry().registerAll(TCItems.runic_leggings);
        event.getRegistry().registerAll(TCItems.bottle_of_thick_taint);
        event.getRegistry().registerAll(TCItems.research_page);
        event.getRegistry().registerAll(TCItems.vis_conductor);
        event.getRegistry().registerAll(TCItems.dump_jackboots);
        event.getRegistry().registerAll(TCItems.tight_belt);
        event.getRegistry().registerAll(TCItems.burdening_amulet);
        event.getRegistry().registerAll(TCItems.void_slag);
        event.getRegistry().registerAll(TCItems.primordial_life);
        event.getRegistry().registerAll(TCItems.blustering_ring);

        event.getRegistry().registerAll(TCItems.item_icon);

        event.getRegistry().registerAll(TCItems.quicksilverCrucible);
        event.getRegistry().registerAll(TCItems.destabilizedCrystal);
        event.getRegistry().registerAll(TCItems.visCondenser);
        event.getRegistry().registerAll(TCItems.hexOfPredictor);
    }

    @SubscribeEvent
    public static void registerAspects(AspectRegistryEvent event) {
        AspectEventProxy proxy = event.register;
        proxy.registerComplexObjectTag(new ItemStack(TCItems.crimson_annales, 1, 0), new AspectList().add(Aspect.MIND, 30).add(IsorropiaAPI.HUNGER, 30).add(Aspect.ELDRITCH, 30).add(Aspect.MAN, 30));
        proxy.registerComplexObjectTag(new ItemStack(TCItems.research_notes_crimson, 1, 0), new AspectList().add(Aspect.MIND, 66).add(Aspect.ELDRITCH, 66).add(IsorropiaAPI.WRATH, 66));
        proxy.registerComplexObjectTag(new ItemStack(TCItems.quicksilverCrucible, 1, 0), new AspectList().add(Aspect.METAL, 100).add(Aspect.MAGIC, 100).add(Aspect.FIRE, 30).add(IsorropiaAPI.FLESH, 10));
        proxy.registerComplexObjectTag(new ItemStack(TCItems.research_page, 1, 0), new AspectList().add(Aspect.MIND, 30));
        proxy.registerComplexObjectTag(new ItemStack(TCItems.research_page, 1, 1), new AspectList().add(Aspect.MIND, 30));
        proxy.registerComplexObjectTag(new ItemStack(TCItems.research_page, 1, 2), new AspectList().add(Aspect.MIND, 30));
        proxy.registerComplexObjectTag(new ItemStack(TCItems.research_page, 1, 3), new AspectList().add(Aspect.MIND, 30));
        proxy.registerComplexObjectTag(new ItemStack(TCItems.research_page, 1, 4), new AspectList().add(Aspect.MIND, 30));

    }

    @SubscribeEvent
    public static void onInfusion(final PlayerEvent.ItemCraftedEvent event) {
        /*if (event.crafting.getItem() instanceof ICaster && !(event.player instanceof FakePlayer)) {
            NBTTagCompound tag = event.crafting.getTagCompound();
            if (tag != null) {
                String xyl = tag.getString("Xylography");
                if (xyl != null && xyl.equals(" ")) {
                    tag.setString("Xylography", event.player.getName());
                    event.crafting.setTagCompound(tag);
                }
            }
        }*/

        AspectList aspectList = PolishRecipe.getPolishmentRecipe(event.crafting);
        if (aspectList != null) {
            EntityPlayer player = event.player;
            double iX = event.player.posX;
            double iY = event.player.posY + 1;
            double iZ = event.player.posZ;
            boolean found = false;
            for (int yy = -16; yy <= 16; yy++)
                for (int zz = -16; zz <= 16; zz++)
                    for (int xx = -16; xx <= 16; xx++)
                        if (event.player.world.getTileEntity(new BlockPos((int) event.player.posX + xx, (int) event.player.posY + yy, (int) event.player.posZ + zz)) instanceof TileInfusionMatrix) {
                            iX = event.player.posX + xx;
                            iY = event.player.posY + yy;
                            iZ = event.player.posZ + zz;
                            found = true;
                        }
            if (player.getHeldItem(player.getActiveHand()) != null && found) {
                if (player.getHeldItem(player.getActiveHand()).getItem() instanceof ItemVisConductor) {
                    ItemStack wand = player.getHeldItem(player.getActiveHand());
                    NBTTagCompound fociTag = wand.getTagCompound();
                    if (fociTag != null) {
                        if (fociTag.hasKey("blockX")) {
                            int x = fociTag.getInteger("blockX");
                            int y = fociTag.getInteger("blockY");
                            int z = fociTag.getInteger("blockZ");
                            BlockPos pos = new BlockPos(x, y, z);

                            if (player.world.getTileEntity(pos) instanceof TileDestabilizedCrystal) {
                                TileDestabilizedCrystal crystal = (TileDestabilizedCrystal) player.world.getTileEntity(pos);
                                int amount = MathHelper.clamp(fociTag.getInteger("amount") - crystal.capacity, 0, Integer.MAX_VALUE);
                                if (amount >= aspectList.getAmount(aspectList.getAspects()[0]) && crystal.aspect.equalsIgnoreCase(aspectList.getAspects()[0].getTag())) {
                                    if (!player.world.isRemote) {
                                        player.world.setBlockToAir(pos);
                                        player.world.removeTileEntity(pos);
                                        player.world.playSound(null, player.getPosition(), SoundsTC.shock, SoundCategory.BLOCKS, 0.8F, player.world.rand.nextFloat() * 0.1F + 0.9F);
                                        player.stopActiveHand();
                                        int rgb = Aspect.aspects.get(crystal.aspect).getColor();
                                        ThaumicConcilium.packetInstance.sendToAllAround(new PacketFXLightning((float) player.posX, (float) (player.posY + 1F), (float) player.posZ, (float) iX, (float) iY, (float) iZ, rgb, 1.0F), new NetworkRegistry.TargetPoint(player.world.provider.getDimension(), player.posX, player.posY, player.posZ, 32.0));
                                        return;
                                    } else {
                                        FXDispatcher.INSTANCE.burst(x, y, z, 2.0F);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!event.player.world.isRemote) {
                event.crafting.shrink(1);

                TileEntity te = event.player.world.getTileEntity(new BlockPos(iX, iY - 2, iZ));
                if (te instanceof TilePedestal) {
                    ((TilePedestal) te).setInventorySlotContents(0, ItemStack.EMPTY);
                    player.stopActiveHand();
                }
            }
        }
    }

    public static void addDrop(LivingDropsEvent event, ItemStack drop) {
        EntityItem entityitem = new EntityItem(event.getEntityLiving().world, event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, drop);
        event.getDrops().add(entityitem);
    }

    public static void addDropWithChance(LivingDropsEvent event, ItemStack drop, int chance) {
        if (new Random().nextInt(100) < chance) {
            addDrop(event, drop);
        }
    }
}
