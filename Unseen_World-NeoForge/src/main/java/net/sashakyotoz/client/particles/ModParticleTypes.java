package net.sashakyotoz.client.particles;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.particles.custom.effects.LeafParticleEffect;
import net.sashakyotoz.client.particles.custom.effects.LightVibrationParticleEffect;
import net.sashakyotoz.client.particles.custom.effects.WindVibrationParticleEffect;
import net.sashakyotoz.common.registry.ModRegistrar;

public class ModParticleTypes {
    public static void register() {
        UnseenWorld.log().debug("Registering Particles for modid : " + UnseenWorld.MOD_ID);
    }

    public static final SimpleParticleType GRIPPING_CRYSTAL = ModRegistrar.register(Registries.PARTICLE_TYPE, UnseenWorld.MOD.makeID("gripping_crystal"),
            new SimpleParticleType(false));
    public static final SimpleParticleType EXPLOSION_CIRCLE = ModRegistrar.register(Registries.PARTICLE_TYPE, UnseenWorld.MOD.makeID("explosion_circle"),
            new SimpleParticleType(false));

    public static final ParticleType<WindVibrationParticleEffect> WIND_VIBRATION = ModRegistrar.register(Registries.PARTICLE_TYPE, UnseenWorld.MOD.makeID("wind_vibration"),
            complex(true, WindVibrationParticleEffect.CODEC, WindVibrationParticleEffect.STREAM_CODEC));
    public static final ParticleType<LightVibrationParticleEffect> LIGHT_VIBRATION = ModRegistrar.register(Registries.PARTICLE_TYPE, UnseenWorld.MOD.makeID("light_vibration"),
            complex(true, LightVibrationParticleEffect.CODEC, LightVibrationParticleEffect.STREAM_CODEC));
    public static final ParticleType<LeafParticleEffect> LEAF = ModRegistrar.register(Registries.PARTICLE_TYPE, UnseenWorld.MOD.makeID("leaf"),
            complex(true, LeafParticleEffect.CODEC, LeafParticleEffect.STREAM_CODEC));

    private static <T extends ParticleOptions> ParticleType<T> complex(boolean alwaysShow, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return new ParticleType<>(alwaysShow) {
            @Override
            public MapCodec<T> codec() {
                return codec;
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
                return streamCodec;
            }
        };
    }
}
