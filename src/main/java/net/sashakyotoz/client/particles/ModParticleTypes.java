package net.sashakyotoz.client.particles;

import com.mojang.serialization.Codec;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.particles.custom.effects.WindVibrationParticleEffect;

import java.util.function.Function;

public class ModParticleTypes {
    public static void register() {
        UnseenWorld.log("Registering Particles for modid : " + UnseenWorld.MOD_ID);
    }

    public static final ParticleType<WindVibrationParticleEffect> WIND_VIBRATION = register(
            "unseen_world:wind_vibration", true, WindVibrationParticleEffect.PARAMETERS_FACTORY, particleType -> WindVibrationParticleEffect.CODEC
    );
    private static <T extends ParticleEffect> ParticleType<T> register(
            String name, boolean alwaysShow, ParticleEffect.Factory<T> factory, Function<ParticleType<T>, Codec<T>> codecGetter
    ) {
        return Registry.register(Registries.PARTICLE_TYPE, name, new ParticleType<T>(alwaysShow, factory) {
            @Override
            public Codec<T> getCodec() {
                return codecGetter.apply(this);
            }
        });
    }
}