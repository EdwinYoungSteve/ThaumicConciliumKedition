package com.keletu.thaumicconcilium.entity;

import com.keletu.thaumicconcilium.ThaumicConcilium;
import com.keletu.thaumicconcilium.items.TCItems;
import com.keletu.thaumicconcilium.packet.PacketMakeHole;
import fr.wind_blade.isorropia.common.entities.projectile.EntityEmber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.entities.monster.EntityWisp;
import thaumcraft.common.entities.projectile.EntityGolemOrb;
import thaumcraft.common.items.casters.foci.FocusEffectRift;
import thaumcraft.common.items.resources.ItemCrystalEssence;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXWispZap;
import thaumcraft.common.lib.utils.EntityUtils;

import java.awt.*;
import java.util.List;

public class EntityBrightestOne extends EntityFlying implements IMob {
    protected final BossInfoServer bossInfo;
    private static final DataParameter<String> NAME = EntityDataManager.createKey(EntityBrightestOne.class, DataSerializers.STRING);
    private Entity targetedEntity = null;
    private int aggroCooldown = 0;
    public int prevAttackCounter = 0;
    public int attackCounter = 0;
    public boolean continuousAttack = false;
    private BlockPos currentFlightTarget;
    static final String[] tags = {"ignis", "aer", "vacuos", "tenebrae"};

    public EntityBrightestOne(World world) {
        super(world);
        this.setSize(3.0F, 3.0F);
        this.experienceValue = 5;
        this.isImmuneToFire = true;
        this.bossInfo = new BossInfoServer(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(25.0);
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public int decreaseAirSupply(int par1) {
        return par1;
    }

    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float i) {
        if (damagesource.getImmediateSource() instanceof EntityBrightestOne) return false;
        if (damagesource.getTrueSource() instanceof EntityBrightestOne) return false;

        if (damagesource.getImmediateSource() instanceof EntityLivingBase) {
            this.targetedEntity = damagesource.getImmediateSource();
            this.aggroCooldown = 200;
        }

        if (damagesource.getTrueSource() instanceof EntityLivingBase) {
            this.targetedEntity = damagesource.getTrueSource();
            this.aggroCooldown = 200;
        }
        return false;
        //return super.attackEntityFrom(damagesource, i);
    }

    @Override
    public void setHealth(float p_70606_1_) {
        super.setHealth(this.getMaxHealth());
    }

    public void shortCircuit() {
        super.setHealth(MathHelper.clamp(this.getHealth() - 1.0F, 0.0F, this.getMaxHealth()));
        if (this.getHealth() == 0) {
            dropFewItems(true, 1);
        }
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(NAME, "");
    }

    @Override
    public void onDeath(DamageSource par1DamageSource) {
        super.onDeath(par1DamageSource);
        if (this.world.isRemote) {
            FXDispatcher.INSTANCE.burst(this.posX, this.posY + 0.44999998807907104, this.posZ, 4.0F);
        }
        if (!this.world.isRemote) {
            dropFewItems(true, 0);
        }
    }

    @Override
    public void handleStatusUpdate(byte p_70103_1_) {
        super.handleStatusUpdate(p_70103_1_);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote && this.ticksExisted <= 1) {
            FXDispatcher.INSTANCE.burst(this.posX, this.posY + 0.44999998807907104, this.posZ, 1.0F);
        }

        if (this.world.isRemote && this.world.rand.nextBoolean() && Aspect.getAspect(this.getType()) != null) {
            Color color = new Color(Aspect.getAspect(this.getType()).getColor());
            FXDispatcher.INSTANCE.drawWispyMotesEntity(this.posX + (double) ((this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.7F), this.posY + 0.44999998807907104 + (double) ((this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.7F), this.posZ + (double) ((this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.7F), this, (float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F);
        }

        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    public String getType() {
        return this.dataManager.get(NAME);
    }

    public void setType(String t) {
        this.dataManager.set(NAME, String.valueOf(t));
    }

    public void changeType() {
        this.dataManager.set(NAME, String.valueOf(tags[this.world.rand.nextInt(tags.length)]));
    }

    @Override
    public void setFire(int p_70015_1_) {
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (isServerWorld()) {
            if (!world.isRemote && Aspect.getAspect(this.getType()) == null) {
                this.setType(Aspect.FIRE.getTag());
                //this.dataWatcher.updateObject(6, Float.valueOf(this.getMaxHealth()));
            }
            if (!world.isRemote && world.getDifficulty() == EnumDifficulty.PEACEFUL) {
                setDead();
            }

            prevAttackCounter = attackCounter;
            double attackrange = 16.0;
            if (getAttackTarget() == null || !canEntityBeSeen(getAttackTarget())) {
                if (currentFlightTarget != null && (!world.isAirBlock(currentFlightTarget) || currentFlightTarget.getY() < 1 || currentFlightTarget.getY() > world.getPrecipitationHeight(currentFlightTarget).up(8).getY())) {
                    currentFlightTarget = null;
                }
                if (currentFlightTarget == null || rand.nextInt(30) == 0 || getDistanceSqToCenter(currentFlightTarget) < 4.0) {
                    currentFlightTarget = new BlockPos((int) posX + rand.nextInt(7) - rand.nextInt(7), (int) posY + rand.nextInt(6) - 2, (int) posZ + rand.nextInt(7) - rand.nextInt(7));
                }
                double var1 = currentFlightTarget.getX() + 0.5 - posX;
                double var2 = currentFlightTarget.getY() + 0.1 - posY;
                double var3 = currentFlightTarget.getZ() + 0.5 - posZ;
                motionX += (Math.signum(var1) * 0.5 - motionX) * 0.10000000149011612;
                motionY += (Math.signum(var2) * 0.699999988079071 - motionY) * 0.10000000149011612;
                motionZ += (Math.signum(var3) * 0.5 - motionZ) * 0.10000000149011612;
                float var4 = (float) (Math.atan2(motionZ, motionX) * 180.0 / 3.141592653589793) - 90.0f;
                float var5 = MathHelper.wrapDegrees(var4 - rotationYaw);
                moveForward = 0.15f;
                rotationYaw += var5;
            } else if (getDistanceSq(getAttackTarget()) > attackrange * attackrange / 2.0 && canEntityBeSeen(getAttackTarget())) {
                double var1 = getAttackTarget().posX - posX;
                double var2 = getAttackTarget().posY + getAttackTarget().getEyeHeight() * 0.66f - posY;
                double var3 = getAttackTarget().posZ - posZ;
                motionX += (Math.signum(var1) * 0.5 - motionX) * 0.10000000149011612;
                motionY += (Math.signum(var2) * 0.699999988079071 - motionY) * 0.10000000149011612;
                motionZ += (Math.signum(var3) * 0.5 - motionZ) * 0.10000000149011612;
                float var4 = (float) (Math.atan2(motionZ, motionX) * 180.0 / 3.141592653589793) - 90.0f;
                float var5 = MathHelper.wrapDegrees(var4 - rotationYaw);
                moveForward = 0.5f;
                rotationYaw += var5;
            }

            if (this.targetedEntity != null && this.targetedEntity.isDead) {
                this.targetedEntity = null;
            }

            --this.aggroCooldown;
            if ((this.targetedEntity == null || this.aggroCooldown-- <= 0)) {
                this.targetedEntity = this.world.getClosestPlayerToEntity(this, 64.0);
                if (this.targetedEntity != null) {
                    this.aggroCooldown = 50;
                }
            }

            if (this.targetedEntity != null && this.targetedEntity.getDistanceSq(this) < attackrange * attackrange) {
                double d5 = this.targetedEntity.posX - this.posX;
                double d6 = this.targetedEntity.getEntityBoundingBox().minY + (double) (this.targetedEntity.height / 2.0F) - (this.posY + (double) (this.height / 2.0F));
                double d7 = this.targetedEntity.posZ - this.posZ;
                this.renderYawOffset = this.rotationYaw = -((float) Math.atan2(d5, d7)) * 180.0F / 3.141593F;
                if (this.canEntityBeSeen(this.targetedEntity)) {
                    ++this.attackCounter;
                    if (continuousAttack && attackCounter < 10) {
                        switch (getType()) {
                            case "aer": {
                                Vec3d v = this.getLook(1.0F);
                                Entity e = targetedEntity;
                                if (!world.isRemote) {
                                    EntityGolemOrb blast = new EntityGolemOrb(this.world, this, (EntityLivingBase) targetedEntity, false);
                                    blast.posX += v.x + (-0.5F + world.rand.nextFloat()) * 5.0F;
                                    blast.posY += 0.5f;
                                    blast.posZ += v.z + (-0.5F + world.rand.nextFloat()) * 5.0F;
                                    blast.setPosition(blast.posX, blast.posY, blast.posZ);
                                    double tx = e.posX + e.motionX - this.posX;
                                    double ty = e.posY - this.posY - (double) (e.height / 2.0F);
                                    double tz = e.posZ + e.motionZ - this.posZ;
                                    blast.shoot(tx, ty, tz, 0.66F, 5.0F);
                                    this.playSound(SoundsTC.egattack, 1.0F, 1.0F + this.world.rand.nextFloat() * 0.1F);
                                    this.world.spawnEntity(blast);
                                }
                                break;
                            }
                            case "ignis": {
                                float scatter = 15.0F;
                                if (!this.world.isRemote) {
                                    for (int i = 0; i < 30; i++) {
                                        EntityEmber orb = new EntityEmber(this.world, this, scatter);
                                        orb.damage = (float) 25;
                                        orb.motionX *= -0.5f + Math.random();
                                        orb.motionY *= Math.random();
                                        orb.motionZ *= -0.5f + Math.random();
                                        orb.firey = 0;
                                        orb.posX += orb.motionX * (8.0f + Math.random());
                                        orb.posY += orb.motionY * (8.0f + Math.random());
                                        orb.posZ += orb.motionZ * (8.0f + Math.random());
                                        this.world.spawnEntity(orb);
                                    }
                                }
                                break;
                            }
                            case "tenebrae": {
                                if (!world.isRemote) {
                                    this.faceEntity(targetedEntity, 100.0F, 100.0F);
                                    EntityShadowbeam beam = new EntityShadowbeam(world, this, 20);
                                    beam.updateUntilDead();
                                }
                                break;
                            }
                        }
                    } else if (attackCounter > 10) {
                        continuousAttack = false;
                    }
                    if (this.attackCounter == 20) {
                        switch (getType()) {
                            case "aer": {
                                if ((world.rand.nextInt(10) % 2) == 0) {
                                    this.world.playSound(null, this.getPosition(), SoundsTC.zap, SoundCategory.HOSTILE, 1.0F, 1.1F);
                                    PacketHandler.INSTANCE.sendToAllAround(new PacketFXWispZap(this.getEntityId(), this.targetedEntity.getEntityId()), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), this.posX, this.posY, this.posZ, 32.0));
                                    float damage = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
                                    this.targetedEntity.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
                                } else {
                                    continuousAttack = true;
                                }
                                break;
                            }
                            case "ignis": {
                                int rand = world.rand.nextInt(6);
                                Vec3d look = this.getLook(1.0F);
                                if (rand % 2 == 0) {
                                    if (!world.isRemote) {
                                        for (int i = 0; i < 10; i++) {
                                            EntityShardPowder orb = new EntityShardPowder(this, this.posX + look.x, this.posY + this.getEyeHeight(), this.posZ + look.z, 1);
                                            orb.posX -= MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.32F;
                                            orb.posY -= 0.3;
                                            orb.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.32F;
                                            orb.setPosition(orb.posX, orb.posY, orb.posZ);
                                            orb.motionX = look.x * Math.random();
                                            orb.motionY = look.y;
                                            orb.motionZ = look.z * Math.random();
                                            this.world.spawnEntity(orb);
                                        }
                                    }
                                } else {
                                    continuousAttack = true;
                                }
                                break;
                            }
                            case "vacuos": {
                                int rand = world.rand.nextInt(10);
                                if (rand % 2 == 0) {
                                    FocusEffectRift.createHole(targetedEntity.world, new BlockPos(MathHelper.floor(targetedEntity.posX), MathHelper.floor(targetedEntity.posY) - 1, MathHelper.floor(targetedEntity.posZ)), EnumFacing.UP, (byte) 33, 120);
                                    ThaumicConcilium.packetInstance.sendToAllAround(new PacketMakeHole(targetedEntity.posX, targetedEntity.posY, targetedEntity.posZ), new NetworkRegistry.TargetPoint(targetedEntity.world.provider.getDimension(), targetedEntity.posX, targetedEntity.posY, targetedEntity.posZ, 32.0F));
                                } else {
                                    this.world.playSound(null, this.getPosition(), SoundsTC.zap, SoundCategory.HOSTILE, 1.0F, 1.1F);
                                    PacketHandler.INSTANCE.sendToAllAround(new PacketFXWispZap(this.getEntityId(), this.targetedEntity.getEntityId()), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), this.posX, this.posY, this.posZ, 32.0));
                                    float damage = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
                                    this.targetedEntity.attackEntityFrom(DamageSource.OUT_OF_WORLD, damage / 4);
                                }
                                break;
                            }
                            case "tenebrae": {
                                int rand = world.rand.nextInt(10);
                                if (rand % 2 == 0) {
                                    continuousAttack = true;
                                } else {
                                    List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, getEntityBoundingBox().grow(32.0F, 32.0F, 32.0F));
                                    if (!players.isEmpty()) {
                                        for (EntityPlayer p : players) {
                                            PotionEffect effect = new PotionEffect(MobEffects.BLINDNESS, 600, 30);
                                            effect.getCurativeItems().clear();
                                            p.addPotionEffect(effect);
                                        }
                                    }
                                }
                            }
                        }
                        List<EntityWisp> list = world.getEntitiesWithinAABB(EntityWisp.class, this.getEntityBoundingBox().grow(32.0, 32.0, 32.0));
                        if (list.isEmpty() || list.size() < 8) {
                            int wisps = world.rand.nextInt(3) + 3;
                            if (!world.isRemote && world.rand.nextInt(3) == 2) {
                                for (int i = 0; i < wisps; i++) {
                                    EntityWisp wisp = new EntityWisp(world);
                                    wisp.setPositionAndRotation(this.posX, this.posY, this.posZ, this.world.rand.nextFloat(), this.world.rand.nextFloat());
                                    wisp.setType(tags[this.world.rand.nextInt(tags.length)]);
                                    this.world.spawnEntity(wisp);
                                }
                            }
                        }
                        this.attackCounter = -20 + this.world.rand.nextInt(20);
                    }
                } else if (this.attackCounter > 0) {
                    --this.attackCounter;
                }
            } else {
                this.renderYawOffset = this.rotationYaw = -((float) Math.atan2(this.motionX, this.motionZ)) * 180.0F / 3.141593F;
                if (this.attackCounter > 0) {
                    --this.attackCounter;
                }
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsTC.wisplive;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.BLOCK_LAVA_EXTINGUISH;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsTC.wispdead;
    }

    @Override
    protected Item getDropItem() {
        return Item.getItemById(0);
    }

    @Override
    protected void dropFewItems(boolean flag, int i) {
        if (Aspect.getAspect(this.getType()) != null) {
            ItemStack ess = new ItemStack(ItemsTC.crystalEssence);
            new AspectList();
            ((ItemCrystalEssence) ess.getItem()).setAspects(ess, (new AspectList()).add(Aspect.getAspect(this.getType()), 2));
            this.entityDropItem(ess, 0.0F);
        }
        EntityUtils.entityDropSpecialItem(this, new ItemStack(TCItems.primordial_life), height / 2.0f);

    }

    public void setInWeb() {
    }

    public boolean canPickUpLoot() {
        return false;
    }

    protected void addRandomArmor() {
    }

    protected void enchantEquipment() {
    }

    protected float getSoundVolume() {
        return 0.25F;
    }

    protected boolean canDespawn() {
        return false;
    }

    @Override
    public void setCustomNameTag(String name) {
        super.setCustomNameTag(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setString("Type", this.getType());
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.setType(nbttagcompound.getString("Type"));
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }


}