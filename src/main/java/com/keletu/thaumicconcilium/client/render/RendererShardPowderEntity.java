package com.keletu.thaumicconcilium.client.render;

import com.keletu.thaumicconcilium.entity.EntityShardPowder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.fx.ParticleEngine;

import java.awt.*;
import java.util.HashMap;

public class RendererShardPowderEntity extends Render {

    public static HashMap<Integer, Integer> colors = new HashMap<>();

    public RendererShardPowderEntity() {
        super(Minecraft.getMinecraft().getRenderManager());
        this.shadowSize = 0.1F;
        this.shadowOpaque = 0.5F;
    }

    public void renderOrb(EntityShardPowder orb, double par2, double par4, double par6, float par8, float par9) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 1);

        Minecraft.getMinecraft().renderEngine.bindTexture(ParticleEngine.particleTexture);
        int i = (int) (System.nanoTime() / 25000000L % 16L);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        float f2 = (float) i / 64f;
        float f3 = (float) (i + 1) / 64f;
        float f4 = 0.5F / 4F;
        float f5 = 0.5625F / 4F;
        float f6 = 1.0F;
        float f7 = 0.5F;
        float f8 = 0.25F;
        int j = orb.getBrightnessForRender();
        int k = j % 65536;
        int l = j / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k, (float) l);
        GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        float f11 = 0.1F + 0.3F * ((float) (orb.orbMaxAge - orb.orbAge) / (float) orb.orbMaxAge);
        GL11.glScalef(f11, f11, f11);
        builder.begin(7, DefaultVertexFormats.POSITION_TEX);
        Color color = new Color(colors.get(orb.type));
        builder.color(color.getRed(), color.getGreen(), color.getBlue(), 128);
        builder.normal(0.0F, 1.0F, 0.0F);
        builder.pos(0.0F - f7, 0.0F - f8, 0.0).tex(f2, f5).endVertex();
        builder.pos(f6 - f7, 0.0F - f8, 0.0).tex(f3, f5).endVertex();
        builder.pos(f6 - f7, 1.0F - f8, 0.0).tex(f3, f4).endVertex();
        builder.pos(0.0F - f7, 1.0F - f8, 0.0).tex(f2, f4).endVertex();
        tessellator.draw();
        GL11.glDisable(3042);
        GL11.glDisable(32826);
        GlStateManager.resetColor();
        GL11.glPopMatrix();
    }

    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
        this.renderOrb((EntityShardPowder) par1Entity, par2, par4, par6, par8, par9);
    }

    protected ResourceLocation getEntityTexture(Entity entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

    static {
        colors.put(0, Aspect.AIR.getColor());
        colors.put(1, Aspect.FIRE.getColor());
        colors.put(2, Aspect.WATER.getColor());
        colors.put(3, Aspect.EARTH.getColor());
        colors.put(4, Aspect.ORDER.getColor());
        colors.put(5, Aspect.ENTROPY.getColor());
        colors.put(6, 0xFFFFFF);
        colors.put(7, Aspect.getAspect("ira").getColor());
        colors.put(8, Aspect.getAspect("invidia").getColor());
        colors.put(9, Aspect.FLUX.getColor());
        colors.put(10, Aspect.getAspect("superbia").getColor());
        colors.put(11, Aspect.getAspect("luxuria").getColor());
        colors.put(12, Aspect.getAspect("desidia").getColor());
        colors.put(13, Aspect.DESIRE.getColor());
        colors.put(14, Aspect.getAspect("gula").getColor());
    }
}