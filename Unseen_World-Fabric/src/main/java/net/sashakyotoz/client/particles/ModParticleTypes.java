package net.sashakyotoz.client.particles;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.particles.custom.effects.LeafParticleEffect;
import net.sashakyotoz.client.particles.custom.effects.LightVibrationParticleEffect;
import net.sashakyotoz.client.particles.custom.effects.WindVibrationParticleEffect;

public class ModParticleTypes {
    public static void register() {
        UnseenWorld.log().debug("Registering Particles for modid : " + UnseenWorld.MOD_ID);
    }

    public static final SimpleParticleType GRIPPING_CRYSTAL = Registry.register(BuiltInRegistries.PARTICLE_TYPE, UnseenWorld.MOD.makeID("gripping_crystal"),
            FabricParticleTypes.simple());
    public static final SimpleParticleType EXPLOSION_CIRCLE = Registry.register(BuiltInRegistries.PARTICLE_TYPE, UnseenWorld.MOD.makeID("explosion_circle"),
            FabricParticleTypes.simple());

    public static final ParticleType<WindVibrationParticleEffect> WIND_VIBRATION = Registry.register(BuiltInRegistries.PARTICLE_TYPE, UnseenWorld.MOD.makeID("wind_vibration"),
            FabricParticleTypes.complex(true, WindVibrationParticleEffect.CODEC, WindVibrationParticleEffect.STREAM_CODEC));
    public static final ParticleType<LightVibrationParticleEffect> LIGHT_VIBRATION = Registry.register(BuiltInRegistries.PARTICLE_TYPE, UnseenWorld.MOD.makeID("light_vibration"),
            FabricParticleTypes.complex(true, LightVibrationParticleEffect.CODEC, LightVibrationParticleEffect.STREAM_CODEC));
    public static final ParticleType<LeafParticleEffect> LEAF = Registry.register(BuiltInRegistries.PARTICLE_TYPE, UnseenWorld.MOD.makeID("leaf"),
            FabricParticleTypes.complex(true, LeafParticleEffect.CODEC, LeafParticleEffect.STREAM_CODEC));
}
