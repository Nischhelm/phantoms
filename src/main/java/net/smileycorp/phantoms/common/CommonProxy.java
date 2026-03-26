package net.smileycorp.phantoms.common;

import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.smileycorp.phantoms.integration.TinkersConstructIntegration;

@Mod.EventBusSubscriber
public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event) {
		ConfigHandler.syncConfig(event);
	}

	public void init(FMLInitializationEvent event) {
		if (Loader.isModLoaded("tconstruct")) TinkersConstructIntegration.registerRecipes();
		LootTableList.register(Constants.PHANTOM_DROPS);
	}
	
	public void postInit(FMLPostInitializationEvent event) {}
	
	public void serverStart(FMLServerStartingEvent event) {}
	
	@SubscribeEvent
	public static void worldTick(TickEvent.WorldTickEvent event) {

	}
	
}
