package net.sashakyotoz.unseenworld.registries;

import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.effect.MobEffectInstance;

public class UnseenWorldPotions {
	public static final DeferredRegister<Potion> REGISTRY = DeferredRegister.create(ForgeRegistries.POTIONS, UnseenWorldMod.MODID);
	public static final RegistryObject<Potion> DARK_VOID_POTION = REGISTRY.register("dark_void_potion", () -> new Potion(new MobEffectInstance(UnseenWorldMobEffects.DARK_VOID.get(), 200, 0, false, true), new MobEffectInstance(UnseenWorldMobEffects.DARK_VOID.get(), 400, 0, false, true)));
	public static final RegistryObject<Potion> DARK_IMMUNITE_POTION = REGISTRY.register("dark_immunite_potion", () -> new Potion(new MobEffectInstance(UnseenWorldMobEffects.DARK_IMMUNITE.get(), 9600, 0, true, true)));
}
