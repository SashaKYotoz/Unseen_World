
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.sashakyotoz.unseenworld.init;

import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.effect.MobEffect;

import net.sashakyotoz.unseenworld.potion.UnseeniumMobEffect;
import net.sashakyotoz.unseenworld.potion.SoulOvergrowthMobEffect;
import net.sashakyotoz.unseenworld.potion.ReducedOfGravityMobEffect;
import net.sashakyotoz.unseenworld.potion.MeteoritestrophyMobEffect;
import net.sashakyotoz.unseenworld.potion.DarkVoidMobEffect;
import net.sashakyotoz.unseenworld.potion.DarkImmuniteMobEffect;

public class UnseenWorldModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, UnseenWorldMod.MODID);
	public static final RegistryObject<MobEffect> DARK_VOID = REGISTRY.register("dark_void", () -> new DarkVoidMobEffect());
	public static final RegistryObject<MobEffect> UNSEENIUM = REGISTRY.register("unseenium", () -> new UnseeniumMobEffect());
	public static final RegistryObject<MobEffect> SOUL_OVERGROWTH = REGISTRY.register("soul_overgrowth", () -> new SoulOvergrowthMobEffect());
	public static final RegistryObject<MobEffect> DARK_IMMUNITE = REGISTRY.register("dark_immunite", () -> new DarkImmuniteMobEffect());
	public static final RegistryObject<MobEffect> METEORITESTROPHY = REGISTRY.register("meteoritestrophy", () -> new MeteoritestrophyMobEffect());
	public static final RegistryObject<MobEffect> REDUCED_OF_GRAVITY = REGISTRY.register("reduced_of_gravity", () -> new ReducedOfGravityMobEffect());
}
