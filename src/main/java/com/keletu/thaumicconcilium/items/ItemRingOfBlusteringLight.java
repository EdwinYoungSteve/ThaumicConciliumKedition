package com.keletu.thaumicconcilium.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import thaumcraft.api.aura.AuraHelper;

public class ItemRingOfBlusteringLight extends Item implements IBauble {

    public ItemRingOfBlusteringLight() {
        this.setMaxStackSize(1);
    }

    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.EPIC;
    }

    @Override
    public boolean canEquip(ItemStack arg0, EntityLivingBase arg1) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean canUnequip(ItemStack arg0, EntityLivingBase arg1) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public BaubleType getBaubleType(ItemStack arg0) {
        // TODO Auto-generated method stub
        return BaubleType.RING;
    }

    @Override
    public void onEquipped(ItemStack arg0, EntityLivingBase arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUnequipped(ItemStack arg0, EntityLivingBase arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!stack.hasTagCompound() || (stack.getTagCompound() != null && !stack.getTagCompound().hasKey("TC.RUNIC"))) {
            stack.setTagInfo("TC.RUNIC", new NBTTagInt(50));
        }else if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("TC.RUNIC") && stack.getTagCompound().getInteger("TC.RUNIC") < 50){
            stack.setTagInfo("TC.RUNIC", new NBTTagInt(50));
        }
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            ItemStack stack = new ItemStack(this);
            stack.setTagInfo("TC.RUNIC", new NBTTagInt(50));
            items.add(stack);
        }
    }

    @Override
    public void onWornTick(ItemStack arg0, EntityLivingBase arg1) {
        if (arg1.ticksExisted % 3 == 0) {
            if (arg1 instanceof EntityPlayer) {
                EntityPlayer par1EntityPlayer = (EntityPlayer) arg1;
                if (!par1EntityPlayer.world.isRemote) {
                    if (AuraHelper.getVis(par1EntityPlayer.world, par1EntityPlayer.getPosition()) < AuraHelper.getAuraBase(arg1.world, par1EntityPlayer.getPosition()))
                        AuraHelper.addVis(par1EntityPlayer.world, par1EntityPlayer.getPosition(), Math.min(20.0F, AuraHelper.getAuraBase(arg1.world, par1EntityPlayer.getPosition()) - AuraHelper.getVis(par1EntityPlayer.world, par1EntityPlayer.getPosition())));
                }
            }
        }
    }
}