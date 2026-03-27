package net.smileycorp.phantoms.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.phantoms.common.Constants;
import net.smileycorp.phantoms.common.entities.EntityPhantom;

public class LayerPhantomEyes implements LayerRenderer<EntityPhantom> {

    private static final ResourceLocation TEXTURE = Constants.loc("textures/entity/phantom_eyes.png");
    private final RenderPhantom renderer;

    public LayerPhantomEyes(RenderPhantom renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntityPhantom entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        renderer.bindTexture(TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(!entity.isInvisible());
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 61680, 0);
        GlStateManager.enableLighting();
        EntityRenderer render = Minecraft.getMinecraft().entityRenderer;
        render.setupFogColor(true);
        renderer.getMainModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        render.setupFogColor(false);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

}
