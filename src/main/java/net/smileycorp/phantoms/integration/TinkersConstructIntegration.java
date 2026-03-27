package net.smileycorp.phantoms.integration;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.smileycorp.phantoms.common.entities.EntityPhantom;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.shared.TinkerFluids;

public class TinkersConstructIntegration {
    
    public static void registerRecipes() {
        FluidStack fluid = new FluidStack(TinkerFluids.glass, 50);
        if (Loader.isModLoaded("thermalfoundation")) fluid = ThermalFoundationIntegration.getSlowFallingPotion();
        TinkerRegistry.registerEntityMelting(EntityPhantom.class, fluid);
    }

}
