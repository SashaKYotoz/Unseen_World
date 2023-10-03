
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.sashakyotoz.unseenworld.init;

import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;

public class UnseenWorldModParticleTypes {
	public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, UnseenWorldMod.MODID);
	public static final RegistryObject<SimpleParticleType> BLUEVOIDPARTICLE = REGISTRY.register("bluevoidparticle", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> REDNESS = REGISTRY.register("redness", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> GOLDEN = REGISTRY.register("golden", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> GREENISH_PARTICLE = REGISTRY.register("greenish_particle", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> FIRE_PARTICLE = REGISTRY.register("fire_particle", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> LIQUID_OF_CHIMERY_PARTICLE = REGISTRY.register("liquid_of_chimery_particle", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> GRIZZLY_PARTICLE = REGISTRY.register("grizzly_particle", () -> new SimpleParticleType(false));
}
