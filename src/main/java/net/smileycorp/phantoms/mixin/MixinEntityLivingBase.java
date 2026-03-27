package net.smileycorp.phantoms.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;
import net.smileycorp.phantoms.common.PhantomsContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity {

    @Shadow public abstract boolean isPotionActive(Potion potion);

    public MixinEntityLivingBase(World world) {
        super(world);
    }

    @Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/EntityLivingBase;hasNoGravity()Z"), method = "travel")
    public void phantoms$travel$hasNoGravity(float strafe, float vertical, float forward, CallbackInfo ci) {
        if (!isPotionActive(PhantomsContent.SLOW_FALLING)) return;
        if (motionY < 0)
        motionY = Math.min(motionY + (isInWater() || isInLava() ? 0.0175 : 0.07), 0);
    }

    @WrapOperation(at = @At(value = "FIELD", target = "Lnet/minecraft/entity/EntityLivingBase;motionY:D", ordinal = 1), method = "travel")
    public double phantoms$travel$isElytraFlying$min$post(EntityLivingBase instance, Operation<Double> original, @Local(ordinal = 4) float f4) {
        if (!isPotionActive(PhantomsContent.SLOW_FALLING)) return original.call(instance);
        return 0.01 * (-1 + f4 * 0.75);
    }

}
