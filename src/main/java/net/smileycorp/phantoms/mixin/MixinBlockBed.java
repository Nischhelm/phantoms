package net.smileycorp.phantoms.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockBed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockBed.class)
public abstract class MixinBlockBed {
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;trySleep(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/entity/player/EntityPlayer$SleepResult;"), method = "onBlockActivated")
    private EntityPlayer.SleepResult phantoms$resetSleepTimer(EntityPlayer.SleepResult result, @Local(argsOnly = true) EntityPlayer player) {
        if(result == EntityPlayer.SleepResult.OK) {
            NBTTagCompound nbt = player.getEntityData();
            nbt.setLong("ticksSinceLastSleep", 0);
        }
        return result;
    }
}
