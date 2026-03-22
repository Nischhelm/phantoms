package net.smileycorp.phantoms.common;

import com.google.common.collect.Sets;
import net.minecraft.util.SoundEvent;

import java.util.Set;

public class PhantomsSoundEvents {
    
    public static final Set<SoundEvent> SOUNDS = Sets.newHashSet();
    
    public static final SoundEvent PHANTOM_AMBIENT = register("entity.phantom.ambient");
    public static final SoundEvent PHANTOM_BITE = register("entity.phantom.bite");
    public static final SoundEvent PHANTOM_DEATH = register("entity.phantom.death");
    public static final SoundEvent PHANTOM_FLAP = register("entity.phantom.flap");
    public static final SoundEvent PHANTOM_HURT = register("entity.phantom.hurt");
    public static final SoundEvent PHANTOM_SWOOP = register("entity.phantom.swoop");

    public static SoundEvent register(String name) {
        SoundEvent sound = new SoundEvent(Constants.loc(name));
        sound.setRegistryName(name);
        SOUNDS.add(sound);
        return sound;
    }

}
