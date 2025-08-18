package com.keletu.thaumicconcilium.client.particle;

import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.world.World;

public class ParticleFlamePublic extends ParticleFlame {
    public ParticleFlamePublic(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
    }
}
