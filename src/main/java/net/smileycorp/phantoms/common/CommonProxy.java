package net.smileycorp.phantoms.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntitySelectors;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.smileycorp.phantoms.common.entities.EntityPhantom;
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
	public static void leaveWorld(PlayerEvent.PlayerLoggedOutEvent event) {
		World world = event.player.world;
		if (world.isRemote) return;
		PhantomSpawner.clear();
	}

	@SubscribeEvent
	public static void worldTick(TickEvent.WorldTickEvent event) {
		World world = event.world;
		GameRules gameRules = world.getGameRules();
		if (!gameRules.hasRule("doInsomnia")) gameRules.addGameRule("doInsomnia", "true",
				GameRules.ValueType.BOOLEAN_VALUE);
		if (world.isRemote) return;
		if (!ConfigHandler.phantomsSpawn) return;
		if (!world.getGameRules().getBoolean("doInsomnia")) return;
		if (world.getEntities(EntityPhantom.class, EntityPhantom::isEntityAlive).size() > ConfigHandler.spawnMax) return;
		PhantomSpawner spawner = PhantomSpawner.get(world);
		if (spawner != null) spawner.trySpawn();
	}

	@SubscribeEvent
	public static void playerTick(TickEvent.PlayerTickEvent event) {
		EntityPlayer player = event.player;
		if (player.world.isRemote) return;
		NBTTagCompound nbt = player.getEntityData();
		nbt.setLong("ticksSinceLastSleep", nbt.hasKey("ticksSinceLastSleep") ? nbt.getInteger("ticksSinceLastSleep") + 1 : 1);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void playerSleep(PlayerSleepInBedEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		if (player.world.isRemote || event.getResultStatus() != EntityPlayer.SleepResult.OK) return;
		NBTTagCompound nbt = player.getEntityData();
		nbt.setLong("ticksSinceLastSleep", 0);
	}
	
}
