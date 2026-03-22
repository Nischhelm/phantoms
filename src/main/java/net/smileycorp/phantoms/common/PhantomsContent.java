package net.smileycorp.phantoms.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.phantoms.common.entities.EntityPhantom;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class PhantomsContent {

    public static final EntityEntry PHANTOM = EntityEntryBuilder.create().entity(EntityPhantom.class).id(Constants.loc("phantom"), 167)
            .name(Constants.name("phantom")).egg(4411786, 8978176).tracker(8, 3, true).build();

    public static final Item PHANTOM_MEMBRANE = new Item().setCreativeTab(CreativeTabs.BREWING)
            .setRegistryName(Constants.loc("phantom_membrane")).setUnlocalizedName(Constants.name("phantom_membrane"));

    public static final PotionSlowFalling SLOW_FALLING = new PotionSlowFalling();

    public static final PotionType SLOW_FALLING_POTION = new PotionType(Constants.name("slow_falling"), new PotionEffect(SLOW_FALLING, 1800));
    public static final PotionType EXTENDED_SLOW_FALLING_POTION = new PotionType(Constants.name("extended_slow_falling"), new PotionEffect(SLOW_FALLING, 4800));

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().register(PHANTOM);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(PHANTOM_MEMBRANE);
    }

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(SLOW_FALLING);
    }

    @SubscribeEvent
    public static void registerPotionTypes(RegistryEvent.Register<PotionType> event) {
        IForgeRegistry<PotionType> registry = event.getRegistry();
        registry.register(SLOW_FALLING_POTION);
        registry.register(EXTENDED_SLOW_FALLING_POTION);
    }
    
    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        IForgeRegistry<SoundEvent> registry = event.getRegistry();
        PhantomsSoundEvents.SOUNDS.forEach(registry::register);
    }
    
}
