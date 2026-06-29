package net.sashakyotoz.common;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.sashakyotoz.UnseenWorld;

public class ModSoundEvents {
    public static final SoundEvent THE_DARKNESS_AMBIENT = registerSoundEvent("the_darkness.ambient");

    public static final SoundEvent GLEAMCARVER_AMBIENT_SOUND = registerSoundEvent("gleamcarver_ambient_sound");

    public static final SoundEvent ORDEAL_SPAWNER_REWARDING = registerSoundEvent("ordeal_spawner.rewarding");

//    public static final BlockSoundGroup SOUND_BLOCK_SOUNDS = new BlockSoundGroup(1f, 1f,
//            ModSounds.SOUND_BLOCK_BREAK, ModSounds.SOUND_BLOCK_STEP, ModSounds.SOUND_BLOCK_PLACE,
//            ModSounds.SOUND_BLOCK_HIT, ModSounds.SOUND_BLOCK_FALL);


    private static SoundEvent registerSoundEvent(String name) {
        ResourceLocation id = UnseenWorld.makeID(name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void registerSounds() {
        UnseenWorld.log().debug("Registering Sounds for " + UnseenWorld.MOD_ID);
    }
}