package net.sashakyotoz.client.particles;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.particles.custom.effects.LeafParticleEffect;
import net.sashakyotoz.client.particles.custom.effects.LightVibrationParticleEffect;
import net.sashakyotoz.client.particles.custom.effects.WindVibrationParticleEffect;

import java.util.function.Function;

public class ModParticleTypes {
    public static void register() {
        UnseenWorld.log().debug("Registering Particles for modid : " + UnseenWorld.MOD_ID);
    }

    public static final SimpleParticleType GRIPPING_CRYSTAL = Registry.register(BuiltInRegistries.PARTICLE_TYPE, UnseenWorld.makeID("gripping_crystal"),
            FabricParticleTypes.simple());

    public static final ParticleType<WindVibrationParticleEffect> WIND_VIBRATION = register(
            "unseen_world:wind_vibration", true, WindVibrationParticleEffect.PARAMETERS_FACTORY, particleType -> WindVibrationParticleEffect.CODEC
    );
    public static final ParticleType<LightVibrationParticleEffect> LIGHT_VIBRATION = register(
            "unseen_world:light_vibration", true, LightVibrationParticleEffect.PARAMETERS_FACTORY, particleType -> LightVibrationParticleEffect.CODEC
    );
    public static final ParticleType<LeafParticleEffect> LEAF = register(
            "unseen_world:leaf", true, LeafParticleEffect.PARAMETERS_FACTORY, particleType -> LeafParticleEffect.CODEC
    );

    private static <T extends ParticleOptions> ParticleType<T> register(
            String name, boolean alwaysShow, ParticleOptions.Deserializer<T> factory, Function<ParticleType<T>, Codec<T>> codecGetter
    ) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, name, new ParticleType<T>(alwaysShow, factory) {
            @Override
            public Codec<T> codec() {
                return codecGetter.apply(this);
            }
        });
    }
}