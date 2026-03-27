package net.smileycorp.phantoms.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPhantom extends ModelBase {

    private final ModelRenderer body;
    private final ModelRenderer head;
    private final ModelRenderer leftWing;
    private final ModelRenderer leftWingTip;
    private final ModelRenderer rightWing;
    private final ModelRenderer rightWingTip;
    private final ModelRenderer tail;
    private final ModelRenderer tailTip;

    public ModelPhantom() {
        textureHeight = 64;
        textureWidth = 64;
        body = new ModelRenderer(this, 0, 8);
        body.rotateAngleX = -0.1f;
        body.addBox(-3, -2, -8, 5, 3, 9);
        tail = new ModelRenderer(this,3, 20);
        body.addChild(tail);
        tail.setRotationPoint(0, -2, 1);
        tail.addBox(-2, 0, 0, 3, 2, 6);
        tailTip = new ModelRenderer(this, 4, 29);
        tail.addChild(tailTip);
        tailTip.setRotationPoint(0, 0.5f, 6);
        tailTip.addBox(-1, 0, 0, 1, 1, 6);
        leftWing = new ModelRenderer(this, 23, 12);
        body.addChild(leftWing);
        leftWing.setRotationPoint(2, -2, -8);
        leftWing.rotateAngleZ = 0.1f;
        leftWing.addBox(0, 0, 0, 6, 2, 9);
        leftWingTip = new ModelRenderer(this, 16, 24);
        leftWing.addChild(leftWingTip);
        leftWingTip.setRotationPoint(6, 0, 0);
        leftWingTip.rotateAngleZ = 0.1f;
        leftWingTip.addBox(0, 0, 0, 13, 1, 9);
        rightWing = new ModelRenderer(this, 23, 12);
        body.addChild(rightWing);
        rightWing.mirror = true;
        rightWing.setRotationPoint(-3, -2, -8);
        rightWing.rotateAngleZ = -0.1f;
        rightWing.addBox(-6, 0, 0, 6, 2, 9);
        rightWingTip = new ModelRenderer(this, 16, 24);
        rightWing.addChild(rightWingTip);
        rightWingTip.mirror = true;
        rightWingTip.setRotationPoint(-6, 0, 0);
        rightWingTip.rotateAngleZ = -0.1f;
        rightWingTip.addBox(-13, 0, 0, 13, 1, 9);
        head = new ModelRenderer(this,0, 0);
        body.addChild(head);
        head.setRotationPoint(0, 1, -7);
        head.rotateAngleX = 0.2f;
        head.addBox(-4, -2, -5, 7, 3, 5);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        body.render(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
        float t = entity.getEntityId() * 3f + ageInTicks * 7.448451f * (float) Math.PI / 180f;
        float flap = (float) Math.cos(t) * 16f * (float) Math.PI / 180f;
        leftWing.rotateAngleZ = flap;
        leftWingTip.rotateAngleZ = flap;
        rightWing.rotateAngleZ = -flap;
        rightWingTip.rotateAngleZ = -flap;
        float tailSwing = -(5 + (float) Math.cos(t * 2f) * 5f) * (float) Math.PI / 180f;
        tail.rotateAngleX = tailSwing;
        tailTip.rotateAngleX = tailSwing;
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
    }

}
