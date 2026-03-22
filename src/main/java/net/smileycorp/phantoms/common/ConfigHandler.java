package net.smileycorp.phantoms.common;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.smileycorp.atlas.api.config.EntityAttributesEntry;

public class ConfigHandler {

    public static EntityAttributesEntry phantomAttributes;
    public static boolean phantomsBurn;

    public static void syncConfig(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        try {
            config.load();
            phantomAttributes = new EntityAttributesEntry(config, "Phantom", 0.7, 16, 2, 20, 0, 0, 0, 0.4);
            phantomsBurn = config.getBoolean("phantomsBurn", "Phantom", true, "Do phantoms burn in sunlight?");
        } catch (Exception e) {
        } finally {
            config.save();
        }
    }

}
