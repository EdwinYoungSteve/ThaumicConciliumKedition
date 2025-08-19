package com.keletu.thaumicconcilium.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.entities.monster.cult.EntityCultist;
import thaumcraft.common.entities.monster.cult.EntityCultistCleric;
import thaumcraft.common.entities.monster.cult.EntityCultistKnight;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.utils.EntityUtils;

import java.util.List;

public class EntityPontifexPortal extends EntityMob {
    int stage = 0;
    int stagecounter = 200;
    public int pulse = 0;

    public EntityPontifexPortal(World par1World) {
        super(par1World);
        this.isImmuneToFire = true;
        this.experienceValue = 30;
        this.setSize(1.5F, 3.0F);
    }

    @Override
    public int getTotalArmorValue() {
        return 5;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("stage", this.stage);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.stage = nbt.getInteger("stage");
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(500.0);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(0.0);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean canPickUpLoot() {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public void move(MoverType type, double par1, double par3, double par5) {
    }

    @Override
    protected void updateEntityActionState() {
    }

    @Override
    public boolean isInRangeToRenderDist(double par1) {
        return par1 < 4096.0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        return 15728880;
    }

    @Override
    public float getBrightness() {
        return 1.0F;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.world.isRemote) {
            int a;
            if (this.stagecounter <= 0) {
                if (this.world.getClosestPlayerToEntity(this, 48.0) != null) {
                    this.world.setEntityState(this, (byte) 16);
                    switch (this.stage) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                            this.stagecounter = 15 + this.rand.nextInt(10 - this.stage) - this.stage;
                            this.spawnMinions();
                            break;
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                        default:
                            a = this.getTiming();
                            this.stagecounter = a + this.rand.nextInt(5 + a / 3);
                            this.spawnMinions();
                            break;
                        case 12:
                            this.stagecounter = 50 + this.getTiming() * 2 + this.rand.nextInt(50);
                            this.spawnMinions();
                            break;
                        case 13:
                            this.spawnBoss();
                            break;
                    }

                    ++this.stage;
                } else {
                    this.stagecounter = 30 + this.rand.nextInt(30);
                }
            } else {
                --this.stagecounter;
            }

            if (this.stage < 12) {
                this.heal(1.0F);
            }
        }

        if (this.pulse > 0) {
            --this.pulse;
        }

    }

    int getTiming() {
        List<Entity> l = EntityUtils.getEntitiesInRange(this.world, this.posX, this.posY, this.posZ, this, EntityCultist.class, 32.0);
        return l.size() * 20;
    }

    void spawnBoss() {
        EntityCrimsonPontifex boss = new EntityCrimsonPontifex(world);
        boss.setPosition(this.posX + (double) this.rand.nextFloat() - (double) this.rand.nextFloat(), this.posY + 0.25, this.posZ + (double) this.rand.nextFloat() - (double) this.rand.nextFloat());
        boss.onInitialSpawn(world.getDifficultyForLocation(this.getPosition()), null);
        this.world.spawnEntity(boss);
        boss.playSound(SoundsTC.wandfail, 1.0F, 1.0F);
        this.attackEntityFrom(DamageSource.OUT_OF_WORLD, getHealth());
    }

    void spawnMinions() {
        EntityCultist cultist = null;
        int rand = this.rand.nextInt(3);
        switch (rand) {
            case 0: {
                cultist = new EntityCultistKnight(this.world);
                break;
            }
            case 1: {
                cultist = new EntityCultistCleric(this.world);
                break;
            }
            case 2: {
                cultist = new EntityCrimsonPaladin(this.world);
                break;
            }
        }

        cultist.setPosition(this.posX + (double) this.rand.nextFloat() - (double) this.rand.nextFloat(), this.posY + 0.25, this.posZ + (double) this.rand.nextFloat() - (double) this.rand.nextFloat());
        cultist.onInitialSpawn(world.getDifficultyForLocation(this.getPosition()), null);
        cultist.spawnExplosionParticle();
        cultist.setHomePosAndDistance(this.getPosition(), 32);
        this.world.spawnEntity(cultist);
        cultist.playSound(SoundsTC.wandfail, 1.0F, 1.0F);
        if (this.stage > 12) {
            this.attackEntityFrom(DamageSource.OUT_OF_WORLD, (float) (5 + this.rand.nextInt(5)));
        }

    }


    public void onCollideWithPlayer(EntityPlayer p) {
        if (this.getDistanceSq(p) < 3.0 && p.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, this), 8.0F)) {
            this.playSound(SoundsTC.zap, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F + 1.0F);
        }

    }

    @Override
    protected float getSoundVolume() {
        return 0.75F;
    }

    @Override
    public int getTalkInterval() {
        return 540;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsTC.monolith;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundsTC.zap;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsTC.shock;
    }

    @Override
    protected Item getDropItem() {
        return Item.getItemById(0);
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte msg) {
        if (msg == 16) {
            this.pulse = 10;
            this.spawnExplosionParticle();
        } else {
            super.handleStatusUpdate(msg);
        }

    }

    @Override
    public void addPotionEffect(PotionEffect p_70690_1_) {
    }

    @Override
    public void fall(float a, float b) {
    }

    @Override
    public void onDeath(DamageSource p_70645_1_) {
        if (!this.world.isRemote) {
            this.world.newExplosion(this, this.posX, this.posY, this.posZ, 2.0F, false, false);
        }

        super.onDeath(p_70645_1_);
    }
}