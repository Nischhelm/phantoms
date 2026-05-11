package net.smileycorp.phantoms.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerMP.class)
public abstract class MixinEntityPlayerMP extends EntityPlayer {
    public MixinEntityPlayerMP(World world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayerMP;addStat(Lnet/minecraft/stats/StatBase;)V"), method = "trySleep")
    private void phantoms$resetSleepTimer(BlockPos p_180469_1_, CallbackInfoReturnable<SleepResult> cir) {
        NBTTagCompound nbt = this.getEntityData();
        nbt.setLong("ticksSinceLastSleep", 0);
    }
}
