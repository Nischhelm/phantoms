package net.smileycorp.phantoms.client;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.phantoms.common.Constants;
import net.smileycorp.phantoms.common.entities.EntityPhantom;

public class RenderPhantom extends RenderLiving<EntityPhantom> {
    
    private static final ResourceLocation TEXTURE = Constants.loc("textures/entity/phantom.png");

    public RenderPhantom(RenderManager rm) {
        super(rm, new ModelPhantom(), 0.75f);
        addLayer(new LayerPhantomEyes(this));
    }
    
    @Override
    protected ResourceLocation getEntityTexture(EntityPhantom phantom) {
        return TEXTURE;
    }

    @Override
	protected void preRenderCallback(EntityPhantom phantom, float partialTicks) {
        float scale = 1 + 0.15f * phantom.getSize();
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.translate(0.0F, 1.3125F, 0.1875F);
    }

    @Override
    protected void applyRotations(EntityPhantom phantom, float ticks, float rotationYaw, float partialTicks) {
        super.applyRotations(phantom, ticks, rotationYaw, partialTicks);
        GlStateManager.rotate(phantom.rotationPitch, 1, 0, 0);
    }
}
