package net.sashakyotoz.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.registry.ModRegistrar;

public class ModSoundEvents {
    public static final SoundEvent THE_DARKNESS_AMBIENT = registerSoundEvent("the_darkness.ambient");
    public static final SoundEvent PENUMBRAL_STEPPE_AMBIENT = registerSoundEvent("penumbral_steppe.ambient");
    public static final SoundEvent AURIC_HEIGHTS_AMBIENT = registerSoundEvent("auric_heights.ambient");

    public static final SoundEvent ORDEAL_SPAWNER_REWARDING = registerSoundEvent("ordeal_spawner.rewarding");
    public static final SoundEvent WIND_SLIDE = registerSoundEvent("wind_slide");

    public static final SoundEvent GLEAMCARVER_AMBIENT = registerSoundEvent("entity.gleamcarver.ambient");

    public static final SoundEvent REAVER_AMBIENT = registerSoundEvent("entity.reaver.ambient");
    public static final SoundEvent REAVER_SCREAM = registerSoundEvent("entity.reaver.scream");
    public static final SoundEvent REAVER_GRIND = registerSoundEvent("entity.reaver.grind");
    public static final SoundEvent REAVER_HURT = registerSoundEvent("entity.reaver.hurt");
    public static final SoundEvent REAVER_CRACK = registerSoundEvent("entity.reaver.crack");
    public static final SoundEvent REAVER_DEATH = registerSoundEvent("entity.reaver.death");
    public static final SoundEvent REAVER_CRUMBLE = registerSoundEvent("entity.reaver.crumble");
    public static final SoundEvent REAVER_TELEPORT = registerSoundEvent("entity.reaver.teleport");
    public static final SoundEvent REAVER_RESONATE = registerSoundEvent("entity.reaver.resonate");
    public static final SoundEvent REAVER_ATTACK = registerSoundEvent("entity.reaver.attack");
    public static final SoundEvent REAVER_STEP = registerSoundEvent("entity.reaver.step");

    public static final SoundEvent VEXAL_BEETLE_AMBIENT = registerSoundEvent("entity.vexal_beetle.ambient");
    public static final SoundEvent VEXAL_BEETLE_ANGRY = registerSoundEvent("entity.vexal_beetle.angry");
    public static final SoundEvent VEXAL_BEETLE_DEATH = registerSoundEvent("entity.vexal_beetle.death");
    public static final SoundEvent VEXAL_BEETLE_ATTACK = registerSoundEvent("entity.vexal_beetle.attack");
    public static final SoundEvent VEXAL_BEETLE_EXHAUSTED = registerSoundEvent("entity.vexal_beetle.exhausted");

    public static final SoundEvent SHIMMER_AMBIENT = registerSoundEvent("entity.shimmer.ambient");

    private static SoundEvent registerSoundEvent(String name) {
        ResourceLocation id = UnseenWorld.MOD.makeID(name);
        return ModRegistrar.register(Registries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void register() {
        UnseenWorld.log().debug("Registering Sounds for " + UnseenWorld.MOD_ID);
    }
}