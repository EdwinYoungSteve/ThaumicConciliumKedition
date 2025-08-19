package com.keletu.thaumicconcilium.client.render;

import com.keletu.thaumicconcilium.entity.EntityChargedWisp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.lib.UtilsFX;

public class RendererChargedWisp extends Render<EntityChargedWisp> {

    int size1 = 0;
    int size2 = 0;

    public RendererChargedWisp() {
        super(Minecraft.getMinecraft().getRenderManager());
        this.shadowSize = 0.0F;
    }

    public void renderEntityAt(EntityChargedWisp entity, double x, double y, double z, float fq, float pticks) {
        double xx = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) pticks;
        double yy = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) pticks;
        double zz = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) pticks;
        int color = 0;
        if (Aspect.getAspect(entity.getType()) != null) {
            color = Aspect.getAspect(entity.getType()).getColor();
        }

        GL11.glPushMatrix();
        GL11.glAlphaFunc(516, 0.003921569F);
        GL11.glDepthMask(false);
        this.bindTexture(ParticleEngine.particleTexture);
        UtilsFX.renderFacingQuad(xx, yy, zz, 64, 64, 512 + entity.ticksExisted % 16, 0.4F, 16777215, 1.0F, 1, pticks);
        UtilsFX.renderFacingQuad(xx, yy, zz, 64, 64, 320 + entity.ticksExisted % 16, 0.75F, 16777215, 0.25F, 1, pticks);
        this.bindTexture(UtilsFX.nodeTexture);
        UtilsFX.renderFacingQuad(xx, yy, zz, 32, 32, 800 + entity.ticksExisted % 16, 0.75F, color, 0.5F, 1, pticks);
        GL11.glDepthMask(true);
        GL11.glAlphaFunc(516, 0.1F);
        GL11.glPopMatrix();
    }

    public void doRender(EntityChargedWisp entity, double d, double d1, double d2, float f, float f1) {
        this.renderEntityAt(entity, d, d1, d2, f, f1);
    }

    protected ResourceLocation getEntityTexture(EntityChargedWisp entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}