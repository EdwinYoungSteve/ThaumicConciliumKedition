package com.keletu.thaumicconcilium.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.potions.PotionInfectiousVisExhaust;

import java.awt.*;
import java.util.List;

public class EntityChargedWisp extends Entity {
    private static final DataParameter<String> NAME = EntityDataManager.createKey(EntityChargedWisp.class, DataSerializers.STRING);
    private int age;
    public int lifetime;
    public int power;
    public EntityPlayer caster;

    public EntityChargedWisp(World p_i1762_1_) {
        super(p_i1762_1_);
        this.setSize(0.25F, 0.25F);
    }

    protected void entityInit() {
        this.dataManager.register(NAME, "");
    }

    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double p_70112_1_) {
        return p_70112_1_ < 4096.0D;
    }

    public EntityChargedWisp(World p_i1763_1_, double p_i1763_2_, double p_i1763_4_, double p_i1763_6_, EntityPlayer p, ItemStack wand) {
        super(p_i1763_1_);
        this.age = 0;
        this.setSize(0.25F, 0.25F);
        this.setPosition(p_i1763_2_, p_i1763_4_, p_i1763_6_);

        this.caster = p;

        Vec3d look = p.getLookVec();
        this.motionX = look.x;
        this.motionY = look.y;
        this.motionZ = look.z;
        this.lifetime = 20 + this.rand.nextInt(20);
        this.power = 3; //TODO: add a config option to change that
    }

    public EntityChargedWisp(World p_i1763_1_, double p_i1763_2_, double p_i1763_4_, double p_i1763_6_, double tx, double ty, double tz) {
        super(p_i1763_1_);
        this.age = 0;
        this.setSize(0.25F, 0.25F);
        this.setPosition(p_i1763_2_, p_i1763_4_, p_i1763_6_);

        this.caster = null;

        this.motionX = tx;
        this.motionY = ty;
        this.motionZ = tz;
        this.lifetime = 40 + this.rand.nextInt(60);
        this.power = 2;
    }


    @SideOnly(Side.CLIENT)
    public void setVelocity(double p_70016_1_, double p_70016_3_, double p_70016_5_) {
        this.motionX = p_70016_1_;
        this.motionY = p_70016_3_;
        this.motionZ = p_70016_5_;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(p_70016_1_ * p_70016_1_ + p_70016_5_ * p_70016_5_);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(p_70016_1_, p_70016_5_) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(p_70016_3_, f) * 180.0D / Math.PI);
        }
    }

    @Override
    public void onUpdate() {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();

        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

        for (this.rotationPitch = (float) (Math.atan2(this.motionY, f) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;

        ++this.age;

        AxisAlignedBB boundingBox = this.getEntityBoundingBox().grow(4.0, 4.0, 4.0);
        List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, boundingBox);
        entities.remove(this.caster);
        int rgb = Aspect.getAspect(getType()) != null ? Aspect.getAspect(getType()).getColor() : Color.RED.getRGB();
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        for (EntityLivingBase entity : entities) {
            if (!this.world.isRemote) {

                if (entity instanceof EntityBrightestOne && entity.getEntityBoundingBox().intersects(entity.getEntityBoundingBox()) && entity.ticksExisted > 20) {
                    ((EntityBrightestOne) entity).changeType();
                    ((EntityBrightestOne) entity).shortCircuit();
                    //entity.setHealth(entity.getHealth() - 5.0F);
                    this.world.setEntityState(this, (byte) 17);
                    this.setDead();
                    break;
                }
                entity.attackEntityFrom(
                        EntityDamageSourceIndirect.causeIndirectMagicDamage(this, this.caster), 10 + this.power);
                PotionEffect effect = new PotionEffect(PotionInfectiousVisExhaust.instance, 1000 * this.power, 30);
                effect.getCurativeItems().clear();
                entity.addPotionEffect(effect);
                if (rand.nextInt(50) == 0) {
                    world.playSound(null, this.getPosition(), SoundsTC.jacobs, SoundCategory.HOSTILE, 0.5F, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.2F);
                }
            } else {
                FXDispatcher.INSTANCE.arcLightning((float) entity.posX + 0.5f + (-0.5f + rand.nextFloat()), (float) entity.posY + 0.5f + (-0.5f + rand.nextFloat()), (float) entity.posZ + 0.5f + (-0.5f + rand.nextFloat()), (float) this.posX, (float) this.posY + this.getEyeHeight() / 2.0f, (float) this.posZ, red / 255.0F, green / 255.0F, blue / 255.0F, 0.1f);
            }
        }

        //TCPacketHandler.INSTANCE.sendToAllAround(new PacketFXLightning((float) entity.posX + 0.5f + (-0.5f + rand.nextFloat()), (float) entity.posY + 0.5f + (-0.5f + rand.nextFloat()), (float) entity.posZ + 0.5f + (-0.5f + rand.nextFloat()), (float) this.posX, (float) this.posY + this.getEyeHeight() / 2.0f, (float) this.posZ, rgb, 0.1F), new NetworkRegistry.TargetPoint(world.provider.dimensionId, posX, posY, posZ, 32.0));

        if (!this.world.isRemote && this.age > this.lifetime) {
            this.world.setEntityState(this, (byte) 17);
            this.setDead();
        }
    }

    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte update) {
        if (update == 17) {
        }
        super.handleStatusUpdate(update);
    }

    public String getType() {
        return this.dataManager.get(NAME);
    }

    public void setType(String t) {
        this.dataManager.set(NAME, String.valueOf(t));
    }

    public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
        p_70014_1_.setInteger("Life", this.age);
        p_70014_1_.setInteger("LifeTime", this.lifetime);
        p_70014_1_.setInteger("power", this.power);
        p_70014_1_.setString("type", this.getType());
    }

    public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
        this.age = p_70037_1_.getInteger("Life");
        this.lifetime = p_70037_1_.getInteger("LifeTime");
        this.power = p_70037_1_.getInteger("power");
        this.setType(p_70037_1_.getString("type"));
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 0.0F;
    }

    public float getBrightness() {
        return super.getBrightness();
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        return super.getBrightnessForRender();
    }

    public boolean canBeAttackedWithItem() {
        return false;
    }
}