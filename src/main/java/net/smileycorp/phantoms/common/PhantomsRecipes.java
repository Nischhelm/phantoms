package net.smileycorp.phantoms.common;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.smileycorp.phantoms.integration.TinkersConstructIntegration;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class PhantomsRecipes {

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        if (Loader.isModLoaded("tconstruct")) TinkersConstructIntegration.registerRecipes();
        PotionHelper.addMix(PotionTypes.AWKWARD, PhantomsContent.PHANTOM_MEMBRANE, PhantomsContent.SLOW_FALLING_POTION);
        PotionHelper.addMix(PhantomsContent.SLOW_FALLING_POTION, Items.REDSTONE, PhantomsContent.EXTENDED_SLOW_FALLING_POTION);
    }
    
}
