package net.smileycorp.phantoms.common;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.smileycorp.atlas.api.config.EntityAttributesEntry;

public class ConfigHandler {

    public static EntityAttributesEntry phantomAttributes;
    public static boolean phantomsBurn;
    public static boolean leatherRepairsElytra;
    public static boolean membraneRepairsElytra;

    public static void syncConfig(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        try {
            config.load();
            phantomAttributes = new EntityAttributesEntry(config, "Phantom", 0.7, 16, 2, 20, 0, 0, 0, 0.4);
            phantomsBurn = config.getBoolean("phantomsBurn", "Phantom", true, "Do phantoms burn in sunlight?");
            leatherRepairsElytra = config.getBoolean("leatherRepairsElytra", "General", false, "Can Leather be used to repair elytras? (Default 1.12 behaviour)");
            membraneRepairsElytra = config.getBoolean("membraneRepairsElytra", "General", true, "Can Phantom Membrane be used to repair elytras? (Default 1.13+ behaviour)");
        } catch (Exception e) {
        } finally {
            config.save();
        }
    }

}
