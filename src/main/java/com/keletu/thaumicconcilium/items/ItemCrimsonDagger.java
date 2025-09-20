package com.keletu.thaumicconcilium.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.NonNullList;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;

public class ItemCrimsonDagger extends ItemSword {
    public ItemCrimsonDagger() {
        super(ToolMaterial.IRON);
    }

    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.RARE;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) {
            return;
        }

        ItemStack stack = new ItemStack(TCItems.crimson_dagger);
        EnumInfusionEnchantment.addInfusionEnchantment(stack, EnumInfusionEnchantment.ESSENCE, 1);
        items.add(stack);
    }
}
