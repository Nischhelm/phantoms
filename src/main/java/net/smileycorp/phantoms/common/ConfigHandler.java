package net.smileycorp.phantoms.common;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
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
    public static double attackDamage;
    public static double maxHealth;
    public static boolean phantomsBurn;
    private static String[] phantomRepellentEntitiesStr;
    private static List<Class<? extends Entity>> phantomRepellentEntities;
    public static int minSize;
    public static int maxSize;
    public static float sizeIncreaseChance;
    public static double attackDamageSizeIncrease;
    public static double maxHealthSizeIncrease;
    public static int phantomCircleHeight;

    //spawning
    public static boolean phantomsSpawn;
    public static int spawnDelay;
    public static int spawnDelayVariation;
    public static int sleeplessTicks;
    public static boolean invertSleeplessTicks;
    public static int spawnLight;
    public static int[] spawnDimensions;
    public static int minSpawns;
    public static int spawnMax;
    public static float extraSpawnsPerDifficulty;
    private static String[] spawnBiomesBlacklist;
    private static List<Biome> spawnBiomes;
    private static boolean spawnBiomesWhitelist;

    public static void syncConfig(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        try {
            config.load();
            //general
            leatherRepairsElytra = config.getBoolean("leatherRepairsElytra", "General", false, "Can Leather be used to repair elytras? (Default 1.12 behaviour)");
            membraneRepairsElytra = config.getBoolean("membraneRepairsElytra", "General", true, "Can Phantom Membrane be used to repair elytras? (Default 1.13+ behaviour)");

            //phantoms
            phantomAttributes = new EntityAttributesEntry(config, "Phantom", 0.7, 16, 2, 20, 0, 0, 0, 0.4);
            attackDamage = config.get("Phantom", "attackDamage", 2, "Attack Damage").getDouble();
            maxHealth = config.get("Phantom", "maxHealth", 20, "Max Health").getDouble();
            phantomsBurn = config.getBoolean("phantomsBurn", "Phantom", true, "Do phantoms burn in sunlight?");
            phantomRepellentEntitiesStr = config.get("Phantom", "phantomRepellentEntities", new String[] {"minecraft:ocelot"}, "Which entities repel phantoms?").getStringList();
            minSize = config.getInt("minSize", "Phantom", 0, 0, 64, "Minimum size of Phantoms");
            maxSize = config.getInt("maxSize", "Phantom", 7, 0, 64, "Maximum size of Phantoms");
            sizeIncreaseChance = config.getFloat("sizeIncreaseChance", "Phantom", 0.5f, 0, 1, "Chance for Phantoms to spawn increased a size.");
            attackDamageSizeIncrease = config.get("Phantom", "attackDamageSizeIncrease", 1, "How much extra damage do Phantoms do per size?").getDouble();
            maxHealthSizeIncrease = config.get("Phantom", "maxHealthSizeIncrease", 2, "How much extra health do Phantoms have per size?").getDouble();
            phantomCircleHeight = config.getInt("phantomCircleHeight", "Phantom", 30, Integer.MIN_VALUE, Integer.MAX_VALUE, "The average height above the ground or target Phantoms spawn and perform their circling ai at.");

            //spawning
            phantomsSpawn = config.getBoolean("phantomsSpawn", "Spawning", true, "Do Phantoms Spawn?");
            spawnDelay = config.getInt("spawnDelay", "Spawning", 1200, 0, Integer.MAX_VALUE, "How often in ticks do Phantoms attempt to spawn? (Default is 1200 or once a minute");
            spawnDelayVariation = config.getInt("spawnDelayVariation", "Spawning", 1200, 0, Integer.MAX_VALUE, "How much random variation in ticks can be added to spawnDelay? (Default is 1200 or up to a minute");
            sleeplessTicks = config.getInt("sleeplessTicks", "Spawning", 72000, 0, Integer.MAX_VALUE, "How many ticks since a players last sleep before Phantoms start attempting to spawn? (Default is 72000 or 3 minecraft days");
            invertSleeplessTicks = config.getBoolean("invertSleeplessTicks", "Spawning", false, "Change the sleeplessTicks option to instead only attempt spawn Phantoms before the specified number of ticks. (For example you could use them as a punishment if people sleep multiple days in a row by setting sleeplessTicks to 24000 (A full day).)");
            spawnLight = config.getInt("spawnLight", "Spawning", 7, 0, Integer.MAX_VALUE, "What is the maximum skylight Phantoms can spawn in? (Default is 7)");
            spawnDimensions = config.get("Spawning", "spawnDimensions", new int[] {0}, "Dimensions Phantoms can spawn in.").getIntList();
            spawnMax = config.getInt("spawnMax", "Spawning", 5, 0, Integer.MAX_VALUE, "How many Phantoms can be spawned in the world at once?");
            minSpawns = config.getInt("minSpawns", "Spawning", 1, 0, 255, "Minimum number of Phantoms to spawn per group.");
            extraSpawnsPerDifficulty = config.getFloat("extraSpawnsPerDifficulty", "Spawning", 1, 0, 255, "Maximum number of random Phantoms to be added to each group per game difficulty level (Rounded Down).");
            spawnBiomesBlacklist = config.get("Spawning", "spawnBiomesBlacklist", new String[] {}, "Biomes phantoms can't spawn in (Can specify either biomes names or Biome Dictionaries e.g. minecraft:plains, FOREST)?").getStringList();
            spawnBiomesWhitelist = config.getBoolean("spawnBiomesWhitelist", "Spawning", false, "Invert spawnBiomesBlacklist so that Phantoms can only spawn in the specified biome.");
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
                    System.out.println("[Phantoms] Loaded Phantom repellent entity " + clazz + " as " + clazz.getName());
                } catch (Exception e) {
                    System.out.println("[Phantoms] Error adding Phantom repellent entity " + str + " " + e.getMessage());
                }
            }
        }
        if (entity instanceof EntityPhantom) return false;
        for (Class<? extends Entity> clazz : phantomRepellentEntities) if (clazz.isAssignableFrom(entity.getClass())) return true;
        return false;
    }

    public static boolean canSpawn(Biome biome) {
        if (spawnBiomes == null) {
            spawnBiomes = Lists.newArrayList();
            for (String str : spawnBiomesBlacklist) {
                if (str.contains(":")) {
                    try {
                        Biome biome1 = Biome.REGISTRY.getObject(new ResourceLocation(str));
                        if (biome1 != null) spawnBiomes.add(biome1);
                        else System.out.println("[Phantoms] Biome " + str + " is not registered");
                    } catch (Exception e) {
                        System.out.println("[Phantoms] " + str + " is not a valid registry name");
                    }
                }
                else {
                    try {
                        BiomeDictionary.Type type = BiomeDictionary.Type.getType(str);
                        for (Biome biome1 : BiomeDictionary.getBiomes(type)) spawnBiomes.add(biome1);
                    } catch (Exception e) {
                        System.out.println("[Phantoms] " + str + " is not a valid registry name");
                    }
                }
            }
        }
        return spawnBiomesWhitelist == spawnBiomes.contains(biome);
    }

}
