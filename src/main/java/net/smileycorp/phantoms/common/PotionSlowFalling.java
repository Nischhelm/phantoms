package net.smileycorp.phantoms.common;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionSlowFalling extends Potion {

    public static final ResourceLocation TEXTURE = Constants.loc("textures/mob_effect/slow_falling.png");

    public PotionSlowFalling() {
        super(false, 15978425);
        setPotionName("effect.phantoms.slow_falling");
        setRegistryName(Constants.loc("slow_falling"));
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        entity.fallDistance = entity.isElytraFlying() ? 1 : 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(PotionEffect effect, Gui gui, int x, int y, float z) {
        renderEffect(x + 6, y + 7, 1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderHUDEffect(PotionEffect effect, Gui gui, int x, int y, float z, float alpha) {
        renderEffect(x + 3, y + 3, alpha);
    }

    @SideOnly(Side.CLIENT)
    protected void renderEffect(int x, int y, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1, alpha);
        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
        Gui.drawScaledCustomSizeModalRect(x, y, 0, 0 , 18, 18, 18, 18, 18, 18);
        GlStateManager.popMatrix();
    }

}
