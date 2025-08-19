package com.keletu.thaumicconcilium.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.api.potions.PotionFluxTaint;
import thaumcraft.api.potions.PotionVisExhaust;
import thaumcraft.codechicken.lib.vec.Vector3;
import thaumcraft.common.entities.monster.tainted.EntityTaintacleSmall;
import thaumcraft.common.lib.potions.PotionInfectiousVisExhaust;

import java.util.Iterator;
import java.util.List;

public class EntityShardPowder extends Entity implements IEntityAdditionalSpawnData {
    public int orbAge = 0;
    public int orbMaxAge = 40;
    public int orbCooldown;
    private int orbHealth = 5;
    public int type;
    public int iteration;
    private EntityPlayer closestPlayer;
    private EntityLivingBase caster;

    public boolean isInRangeToRenderDist(double par1) {
        double d1 = 0.5;
        d1 *= 64.0 * renderDistanceWeight;
        return par1 < d1 * d1;
    }

    public EntityShardPowder(World par1World) {
        super(par1World);
        this.setSize(0.125F, 0.125F);
    }

    public EntityShardPowder(EntityLivingBase caster, double x, double y, double z, int type) {
        super(caster.world);
        this.setSize(0.125F, 0.125F);
        this.setPosition(x, y, z);
        this.caster = caster;
        this.rotationYaw = (float) (Math.random() * 360.0);
        this.type = type;
        this.iteration = 0;
    }

    public EntityShardPowder(EntityLivingBase caster, int iteration, double x, double y, double z) {
        super(caster.world);
        this.setSize(0.125F, 0.125F);
        this.setPosition(x, y, z);
        this.caster = caster;
        this.rotationYaw = (float) (Math.random() * 360.0);
        this.type = 8;
        this.iteration = iteration;
    }

    public double getYOffset() {
        return this.height / 2.0F;
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    protected void entityInit() {
    }


    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        float f1 = 0.5F;
        if (f1 < 0.0F) {
            f1 = 0.0F;
        }

        if (f1 > 1.0F) {
            f1 = 1.0F;
        }

        int i = super.getBrightnessForRender();
        int j = i & 255;
        int k = i >> 16 & 255;
        j += (int) (f1 * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
    }

    public void onUpdate() {
        super.onUpdate();
        if (this.orbCooldown > 0) {
            --this.orbCooldown;
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= 0.029999999329447746;
        if (this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ))).getMaterial() == Material.LAVA) {
            this.motionY = 0.20000000298023224;
            this.motionX = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
            this.motionZ = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
            this.playSound(SoundEvents.BLOCK_LAVA_EXTINGUISH, 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
        }

        this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0, this.posZ);
        double d0 = 8.0;
        if (type == 9) {
            if (this.ticksExisted % 5 == 0 && this.closestPlayer == null) {
                List<Entity> targets = this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(this.posX, this.posY, this.posZ, this.posX, this.posY, this.posZ).grow(d0, d0, d0));
                if (targets.size() > 0) {
                    targets.remove(caster);
                    double distance = Double.MAX_VALUE;
                    Iterator i$ = targets.iterator();

                    while (i$.hasNext()) {
                        Entity t = (Entity) i$.next();
                        if (!t.equals(caster)) {
                            double d = t.getDistanceSq(this);
                            if (d < distance) {
                                distance = d;
                                this.closestPlayer = (EntityPlayer) t;
                            }
                        }
                    }
                }
            }

            if (this.closestPlayer != null) {
                double d1 = (this.closestPlayer.posX - this.posX) / d0;
                double d2 = (this.closestPlayer.posY + (double) this.closestPlayer.getEyeHeight() - this.posY) / d0;
                double d3 = (this.closestPlayer.posZ - this.posZ) / d0;
                double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
                double d5 = 1.0 - d4;
                if (d5 > 0.0) {
                    d5 *= d5;
                    this.motionX += d1 / d4 * d5 * 0.1;
                    this.motionY += d2 / d4 * d5 * 0.1;
                    this.motionZ += d3 / d4 * d5 * 0.1;
                }
            }
        }
        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        float f = 0.98F;
        if (this.onGround) {
            f = 0.58800006F;
            IBlockState i = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ)));
            if (!i.getBlock().isAir(i, this.world, new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ)))) {
                f = i.getBlock().slipperiness * 0.98F;
            }
        }

        this.motionX *= f;
        this.motionY *= 0.9800000190734863;
        this.motionZ *= f;
        if (this.onGround) {
            this.motionY *= -0.8999999761581421;
        }

        ++this.orbAge;
        if (this.orbAge >= this.orbMaxAge) {
            performEffect();
            this.setDead();
        }

    }

    public void performEffect() {
        switch (type) {
            case 0: {
                if (!world.isRemote) {
                    if (this.world.rand.nextInt(10) > 8) {
                        world.addWeatherEffect(new EntityLightningBolt(world, posX, posY, posZ, false));
                    }
                }
                break;
            }
            case 1: {
                if (!world.isRemote) {
                    if (this.world.rand.nextInt(10) > 8) {
                        this.world.newExplosion(null, this.posX, this.posY, this.posZ, 1.5F, true, false);
                    }
                }
                break;
            }
            case 2: {
                if (!world.isRemote) {
                    int x = MathHelper.floor(posX);
                    int y = MathHelper.floor(posY) - 1;
                    int z = MathHelper.floor(posZ);
                    ItemDye.applyBonemeal(new ItemStack(Items.DYE, 1, 15), world, new BlockPos(x, y, z));
                    ItemDye.applyBonemeal(new ItemStack(Items.DYE, 1, 15), world, new BlockPos(x, y + 1, z));
                }
                break;
            }
            case 3: {
                if (!world.isRemote) {
                    int x = MathHelper.floor(posX);
                    int y = MathHelper.floor(posY);
                    int z = MathHelper.floor(posZ);
                    Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block.isReplaceable(world, new BlockPos(x, y, z))) {
                        world.setBlockState(new BlockPos(x, y, z), Blocks.DIRT.getDefaultState());
                    }
                }
                break;
            }
            case 4: {
                if (!world.isRemote) {
                    List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(1.5F, 1.5F, 1.5F));
                    if (!list.isEmpty()) {
                        if (list.get(0).getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE) != null) {
                            float f = (float) list.get(0).getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
                            list.get(0).attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) caster).setMagicDamage(), f);
                        }
                    }
                }
                break;
            }
            case 5: {
                if (!world.isRemote) {
                    List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(1.5F, 1.5F, 1.5F));
                    if (!list.isEmpty()) {
                        list.get(0).addPotionEffect(new PotionEffect(MobEffects.WITHER, 90, 50));
                    }
                }
                break;
            }
            case 7: {
                if (!world.isRemote) {
                    List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(1.5F, 1.5F, 1.5F));
                    if (!list.isEmpty()) {
                        EntityLivingBase e = list.get(0);
                        e.setFire(50000);
                        e.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 200, 5));
                        e.addPotionEffect(new PotionEffect(PotionVisExhaust.instance, 200, 10));
                    }
                }
                break;
            }
            case 8: {
                List<Entity> projs = world.getEntitiesWithinAABB(Entity.class, this.getEntityBoundingBox().grow(10.0, 10.0, 10.0));
                if (!world.isRemote) {
                    for (Entity e : projs) {
                        if (e instanceof IProjectile) {
                            if (caster != null && iteration < 5) {
                                EntityShardPowder powder = new EntityShardPowder(caster, iteration + 1, e.posX, e.posY, e.posZ);
                                world.spawnEntity(powder);
                            }
                            e.setDead();
                        }
                    }
                }
                break;
            }
            case 9: {
                if (!world.isRemote) {
                    List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(1.5F, 1.5F, 1.5F));
                    if (!list.isEmpty()) {
                        EntityLivingBase e = list.get(0);
                        if (e instanceof EntityPlayer) {
                            ThaumcraftApi.internalMethods.addWarpToPlayer((EntityPlayer) e, 20, IPlayerWarp.EnumWarpType.TEMPORARY);
                        } else {
                            e.addPotionEffect(new PotionEffect(PotionFluxTaint.instance, 80, 50));
                        }
                    }
                }
                break;
            }
            case 10: {
                if (!world.isRemote) {

                    List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(1.5F, 1.5F, 1.5F));
                    if (!list.isEmpty()) {
                        EntityLivingBase e = list.get(0);
                        Vector3 target = new Vector3(e.posX - this.posX, e.posY, e.posZ - this.posZ).multiply(2.0, 1.0, 2.0);
                        EntityThaumGib.setEntityMotionFromVector(e, target, 3.0F);
                    }
                }
                break;
            }
            case 11: {
                if (!world.isRemote) {
                    if (world.rand.nextInt(10) > 8) {
                        EntityTaintacleSmall taintacle = new EntityTaintacleSmall(world);
                        taintacle.setPosition(posX, posY, posZ);
                        world.spawnEntity(taintacle);
                        List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, taintacle.getEntityBoundingBox().grow(20.5F, 2.5F, 20.5F));
                        list.remove(taintacle);
                        if (!list.isEmpty()) {
                            int i = -1;
                            for (EntityLivingBase e : list) {
                                if (!(e instanceof ITaintedMob)) {
                                    i = list.indexOf(e);
                                    break;
                                }
                            }
                            if (i != -1) {
                                taintacle.setAttackTarget(list.get(i));
                            }
                        }
                    }
                }
                break;
            }
            case 12: {
                if (!world.isRemote) {

                    List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(1.5F, 1.5F, 1.5F));
                    if (!list.isEmpty()) {
                        EntityLivingBase e = list.get(0);

                        e.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 80, 15));
                        e.addPotionEffect(new PotionEffect(PotionInfectiousVisExhaust.instance, 60, 20));
                    }
                }
                break;
            }
            case 13: {
                if (!world.isRemote) {

                    List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(1.5F, 1.5F, 1.5F));
                    if (!list.isEmpty()) {
                        EntityLivingBase e = list.get(0);
                        EntityThaumGib.setEntityMotionFromVector(e, new Vector3(this.posX, this.posY, this.posZ), 3.0F);
                    }
                }
                break;
            }
        }
    }


    public boolean handleWaterMovement() {
        return this.world.handleMaterialAcceleration(this.getEntityBoundingBox(), Material.WATER, this);
    }

    protected void dealFireDamage(int par1) {
        this.attackEntityFrom(DamageSource.IN_FIRE, (float) par1);
    }

    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
        if (this.world.isRemote || this.isDead) return false; //Forge: Fixes MC-53850
        if (this.isEntityInvulnerable(par1DamageSource)) {
            return false;
        } else {
            this.markVelocityChanged();
            this.orbHealth = (int) ((float) this.orbHealth - par2);
            if (this.orbHealth <= 0) {
                this.setDead();
            }

            return false;
        }
    }


    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
        par1NBTTagCompound.setShort("Health", (byte) this.orbHealth);
        par1NBTTagCompound.setShort("Age", (short) this.orbAge);
        par1NBTTagCompound.setInteger("Type", this.type);

    }

    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
        this.orbHealth = par1NBTTagCompound.getShort("Health") & 255;
        this.orbAge = par1NBTTagCompound.getShort("Age");
        this.type = par1NBTTagCompound.getInteger("Type");
    }

    public void onCollideWithPlayer(EntityPlayer player) {
        if (!this.world.isRemote) {
            //if (!par1EntityPlayer.equals(caster)) {
            if (this.orbCooldown == 0 && player.xpCooldown == 0 && !player.getIsInvulnerable()) {
                switch (type) {
                    case 9: {
                        AuraHelper.drainVis(player.world, player.getPosition(), Math.min(AuraHelper.getVis(world, player.getPosition()), 10000), false);
                        break;
                    }
                    case 14: {
                        player.getFoodStats().addStats(1, 1.0F);
                        break;
                    }
                }
                player.xpCooldown = 2;
                //this.playSound("random.orb", 0.1F, 0.5F * ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.8F));
                this.setDead();
            }
            // }
        }

    }

    public boolean canBeAttackedWithItem() {
        return false;
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeInt(this.type);
        buffer.writeInt(this.iteration);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        try {
            this.type = additionalData.readInt();
            this.iteration = additionalData.readInt();
        } catch (Exception e) {
        }
    }
}