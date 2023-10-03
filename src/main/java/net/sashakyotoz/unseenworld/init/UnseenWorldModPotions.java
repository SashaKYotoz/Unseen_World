
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.sashakyotoz.unseenworld.init;

import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.effect.MobEffectInstance;

public class UnseenWorldModPotions {
	public static final DeferredRegister<Potion> REGISTRY = DeferredRegister.create(ForgeRegistries.POTIONS, UnseenWorldMod.MODID);
	public static final RegistryObject<Potion> DARK_VOID_POTION = REGISTRY.register("dark_void_potion",
			() -> new Potion(new MobEffectInstance(UnseenWorldModMobEffects.DARK_VOID.get(), 100, 0, false, true), new MobEffectInstance(UnseenWorldModMobEffects.DARK_VOID.get(), 60, 0, false, true)));
	public static final RegistryObject<Potion> DARK_IMMUNITE_POTION = REGISTRY.register("dark_immunite_potion", () -> new Potion(new MobEffectInstance(UnseenWorldModMobEffects.DARK_IMMUNITE.get(), 8000, 0, true, true)));
}
