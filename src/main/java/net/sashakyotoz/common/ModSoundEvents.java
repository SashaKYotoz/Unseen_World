package net.sashakyotoz.common;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;

public class ModSoundEvents {
    public static final SoundEvent GLEAMCARVER_AMBIENT_SOUND = registerSoundEvent("gleamcarver_ambient_sound");

//    public static final BlockSoundGroup SOUND_BLOCK_SOUNDS = new BlockSoundGroup(1f, 1f,
//            ModSounds.SOUND_BLOCK_BREAK, ModSounds.SOUND_BLOCK_STEP, ModSounds.SOUND_BLOCK_PLACE,
//            ModSounds.SOUND_BLOCK_HIT, ModSounds.SOUND_BLOCK_FALL);


    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = UnseenWorld.makeID(name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        UnseenWorld.log("Registering Sounds for " + UnseenWorld.MOD_ID);
    }
}