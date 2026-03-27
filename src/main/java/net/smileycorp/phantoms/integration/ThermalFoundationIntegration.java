package net.smileycorp.phantoms.integration;

import cofh.thermalfoundation.init.TFFluids;
import net.minecraftforge.fluids.FluidStack;
import net.smileycorp.phantoms.common.PhantomsContent;

public class ThermalFoundationIntegration {

    public static FluidStack getSlowFallingPotion() {
        return TFFluids.getPotion(250, PhantomsContent.SLOW_FALLING_POTION);
    }

}
