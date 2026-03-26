package net.smileycorp.phantoms.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.*;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.phantoms.common.entities.EntityPhantom;
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
