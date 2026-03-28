package net.smileycorp.phantoms.common;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.registries.GameData;
import net.smileycorp.atlas.api.config.EntityAttributesEntry;
import net.smileycorp.phantoms.common.entities.EntityPhantom;

import java.util.List;

public class ConfigHandler {

    //general
    public static boolean leatherRepairsElytra;
    public static boolean membraneRepairsElytra;

    //phantoms
    public static EntityAttributesEntry phantomAttributes;
    public static boolean phantomsBurn;
    private static String[] phantomRepellentEntitiesStr;
    private static List<Class<? extends Entity>> phantomRepellentEntities;

    //spawning
    public static boolean phantomsSpawn;
    public static int spawnDelay;
    public static int spawnDelayVariation;
    public static int sleeplessTicks;
    public static boolean invertSleeplessTicks;
    public static int spawnLight;
    public static int[] spawnDimensions;
    public static int spawnMax;

    public static void syncConfig(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        try {
            config.load();
            //general
            leatherRepairsElytra = config.getBoolean("leatherRepairsElytra", "General", false, "Can Leather be used to repair elytras? (Default 1.12 behaviour)");
            membraneRepairsElytra = config.getBoolean("membraneRepairsElytra", "General", true, "Can Phantom Membrane be used to repair elytras? (Default 1.13+ behaviour)");

            //phantoms
            phantomAttributes = new EntityAttributesEntry(config, "Phantom", 0.7, 16, 2, 20, 0, 0, 0, 0.4);
            phantomsBurn = config.getBoolean("phantomsBurn", "Phantom", true, "Do phantoms burn in sunlight?");
            phantomRepellentEntitiesStr = config.get("Phantom", "phantomRepellentEntities", new String[] {"minecraft:ocelot"}, "Which entities repel phantoms?").getStringList();

            //spawning
            phantomsSpawn = config.getBoolean("phantomsSpawn", "Spawning", true, "Do Phantoms Spawn?");
            spawnDelay = config.getInt("spawnDelay", "Spawning", 1200, 0, Integer.MAX_VALUE, "How often in ticks do Phantoms attempt to spawn? (Default is 1200 or once a minute");
            spawnDelayVariation = config.getInt("spawnDelayVariation", "Spawning", 1200, 0, Integer.MAX_VALUE, "How much random variation in ticks can be added to spawnDelay? (Default is 1200 or up to a minute");
            sleeplessTicks = config.getInt("sleeplessTicks", "Spawning", 72000, 0, Integer.MAX_VALUE, "How many ticks since a players last sleep before Phantoms start attempting to spawn? (Default is 72000 or 3 minecraft days");
            invertSleeplessTicks = config.getBoolean("invertSleeplessTicks", "Spawning", false, "Change the sleeplessTicks option to instead only attempt spawn Phantoms before the specified number of ticks. (For example you could use them as a punishment if people sleep multiple days in a row by setting sleeplessTicks to 24000 (A full day).)");
            spawnLight = config.getInt("spawnLight", "Spawning", 7, 0, Integer.MAX_VALUE, "What is the maximum skylight Phantoms can spawn in? (Default is 7)");
            spawnDimensions = config.get("Spawning", "spawnDimensions", new int[] {0}, "Dimensions Phantoms can spawn in.").getIntList();
            spawnMax = config.getInt("spawnMax", "Spawning", 5, 0, Integer.MAX_VALUE, "How many Phantoms can be spawned in the world at once?");
        } catch (Exception e) {
        } finally {
            config.save();
        }
    }

    public static boolean repelsPhantoms(Entity entity) {
        if (phantomRepellentEntities == null) {
            phantomRepellentEntities = Lists.newArrayList();
            for (String str : phantomRepellentEntitiesStr) {
                try {
                    Class<?> clazz = null;
                    //check if it matches the syntax for a registry name
                    if (str.contains(":")) {
                        ResourceLocation loc = new ResourceLocation(str);
                        if (GameData.getEntityRegistry().containsKey(loc)) {
                            clazz = GameData.getEntityRegistry().getValue(loc).getEntityClass();
                        } else continue;
                    }
                    if (clazz == null) throw new Exception("Entry " + str + " is not in the correct format");
                    phantomRepellentEntities.add((Class<? extends Entity>) clazz);
                    System.out.println("[Phantoms] Loaded hero of the village discount entity " + clazz + " as " + clazz.getName());
                } catch (Exception e) {
                    System.out.println("[Phantoms] Error adding hero of the village discount entity " + str + " " + e.getMessage());
                }
            }
        }
        if (entity instanceof EntityPhantom) return false;
        for (Class<? extends Entity> clazz : phantomRepellentEntities) if (clazz.isAssignableFrom(entity.getClass())) return true;
        return false;
    }

}
