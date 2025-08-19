package com.keletu.thaumicconcilium.client.render;

import com.keletu.thaumicconcilium.entity.EntityPontifexPortal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;

public class RenderCultistPortalLesserPontifex extends Render<EntityPontifexPortal> {

    public static final ResourceLocation portaltex = new ResourceLocation("thaumcraft", "textures/misc/cultist_portal.png");

    public RenderCultistPortalLesserPontifex(RenderManager renderManager) {
        super(renderManager);
        this.shadowSize = 0.0F;
        this.shadowOpaque = 0.0F;
    }

    public void renderPortal(EntityPontifexPortal portal, double px, double py, double pz, float par8, float f) {
        //if (portal.isActive()) {
        long nt = System.nanoTime();
        long time = nt / 50000000L;
        float scaley = 1.4F;
        int e = (int) Math.min(50.0F, (float) portal.ticksExisted + f);
        double d;
        if (portal.hurtTime > 0) {
            d = Math.sin((double) (portal.hurtTime * 72) * Math.PI / 180.0);
            scaley = (float) ((double) scaley - d / 4.0);
            e = (int) ((double) e + 6.0 * d);
        }

        if (portal.pulse > 0) {
            d = Math.sin((double) (portal.pulse * 36) * Math.PI / 180.0);
            scaley = (float) ((double) scaley + d / 4.0);
            e = (int) ((double) e + 12.0 * d);
        }

        float scale = (float) e / 50.0F * 1.25F;
        py += portal.height / 2.0F;
        float m = (1.0F - portal.getHealth() / portal.getMaxHealth()) / 3.0F;
        float bob = MathHelper.sin((float) portal.ticksExisted / (5.0F - 12.0F * m)) * m + m;
        float bob2 = MathHelper.sin((float) portal.ticksExisted / (6.0F - 15.0F * m)) * m + m;
        float alpha = 1.0F - bob;
        scaley -= bob / 4.0F;
        scale -= bob2 / 3.0F;
        this.bindTexture(portaltex);
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
        if (Minecraft.getMinecraft().getRenderViewEntity() instanceof EntityPlayer) {
            GL11.glDepthMask(false);
            Tessellator tessellator = Tessellator.getInstance();
            float arX = ActiveRenderInfo.getRotationX();
            float arZ = ActiveRenderInfo.getRotationZ();
            float arYZ = ActiveRenderInfo.getRotationYZ();
            float arXY = ActiveRenderInfo.getRotationXY();
            float arXZ = ActiveRenderInfo.getRotationXZ();
            tessellator.getBuffer().begin(7, UtilsFX.VERTEXFORMAT_POS_TEX_CO_LM_NO);
            Vec3d v1 = new Vec3d(-arX - arYZ, -arXZ, -arZ - arXY);
            Vec3d v2 = new Vec3d(-arX + arYZ, arXZ, -arZ + arXY);
            Vec3d v3 = new Vec3d(arX + arYZ, arXZ, arZ + arXY);
            Vec3d v4 = new Vec3d(arX - arYZ, -arXZ, arZ - arXY);
            int frame = 15 - (int) time % 16;
            float f2 = (float) frame / 16.0F;
            float f3 = f2 + 0.0625F;
            float f4 = 0.0F;
            float f5 = 1.0F;
            int i = 220;
            int j = i >> 16 & '\uffff';
            int k = i & '\uffff';
            tessellator.getBuffer().pos(px + v1.x * (double) scale, py + v1.y * (double) scaley, pz + v1.z * (double) scale).tex(f3, f4).color(1.0F, 1.0F, 1.0F, alpha).lightmap(j, k).normal(0.0F, 0.0F, -1.0F).endVertex();
            tessellator.getBuffer().pos(px + v2.x * (double) scale, py + v2.y * (double) scaley, pz + v2.z * (double) scale).tex(f3, f5).color(1.0F, 1.0F, 1.0F, alpha).lightmap(j, k).normal(0.0F, 0.0F, -1.0F).endVertex();
            tessellator.getBuffer().pos(px + v3.x * (double) scale, py + v3.y * (double) scaley, pz + v3.z * (double) scale).tex(f2, f5).color(1.0F, 1.0F, 1.0F, alpha).lightmap(j, k).normal(0.0F, 0.0F, -1.0F).endVertex();
            tessellator.getBuffer().pos(px + v4.x * (double) scale, py + v4.y * (double) scaley, pz + v4.z * (double) scale).tex(f2, f4).color(1.0F, 1.0F, 1.0F, alpha).lightmap(j, k).normal(0.0F, 0.0F, -1.0F).endVertex();
            tessellator.draw();
            GL11.glDepthMask(true);
        }

        GL11.glDisable(32826);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        // }

    }

    public void doRender(EntityPontifexPortal par1Entity, double par2, double par4, double par6, float par8, float par9) {
        this.renderPortal(par1Entity, par2, par4, par6, par8, par9);
    }

    protected ResourceLocation getEntityTexture(EntityPontifexPortal entity) {
        return portaltex;
    }

}