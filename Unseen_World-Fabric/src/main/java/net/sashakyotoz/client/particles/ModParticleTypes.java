package net.sashakyotoz.client.particles;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.particles.custom.effects.LeafParticleEffect;
import net.sashakyotoz.client.particles.custom.effects.LightVibrationParticleEffect;
import net.sashakyotoz.client.particles.custom.effects.WindVibrationParticleEffect;

import java.util.function.Function;

public class ModParticleTypes {
    public static void register() {
        UnseenWorld.log("Registering Particles for modid : " + UnseenWorld.MOD_ID);
    }

    public static final DefaultParticleType GRIPPING_CRYSTAL = Registry.register(Registries.PARTICLE_TYPE, UnseenWorld.makeID("gripping_crystal"),
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