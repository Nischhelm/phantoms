package net.smileycorp.phantoms.mixin;

import net.minecraft.init.Items;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.smileycorp.phantoms.common.ConfigHandler;
import net.smileycorp.phantoms.common.PhantomsContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemElytra.class)
public abstract class MixinItemElytra {

    @Inject(at = @At("HEAD"), method = "getIsRepairable", cancellable = true)
    public void phantoms$isRepairable(ItemStack toRepair, ItemStack repair, CallbackInfoReturnable<Boolean> callback) {
        if (!ConfigHandler.leatherRepairsElytra && repair.getItem() == Items.LEATHER) {
            callback.setReturnValue(false);
            return;
        }
        if (ConfigHandler.membraneRepairsElytra && repair.getItem() == PhantomsContent.PHANTOM_MEMBRANE) callback.setReturnValue(true);
    }


}
