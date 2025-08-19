package com.keletu.thaumicconcilium.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;

import java.util.List;

public class ItemPrimordialLife extends Item {

    public ItemPrimordialLife() {
        this.setMaxStackSize(1);
    }

    @Override
    public EnumRarity getRarity(ItemStack p_77613_1_) {
        return EnumRarity.EPIC;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof EntityPlayer) {
            IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge((EntityPlayer) entity);
            if (!knowledge.isResearchKnown("c_defeatbrightest")) {
                knowledge.addResearch("c_defeatbrightest");
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World player, List<String> list, ITooltipFlag par4) {
        super.addInformation(stack, player, list, par4);
        list.add(TextFormatting.DARK_PURPLE + I18n.translateToLocal("tc.tooltip.life.0"));
        list.add(TextFormatting.DARK_PURPLE + I18n.translateToLocal("tc.tooltip.life.1"));
    }
}
