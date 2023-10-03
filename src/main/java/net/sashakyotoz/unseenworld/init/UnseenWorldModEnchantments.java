
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.sashakyotoz.unseenworld.init;

import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.enchantment.GravitySpikeEnchantment;
import net.sashakyotoz.unseenworld.enchantment.LifeSteelEnchantment;
import net.sashakyotoz.unseenworld.enchantment.MiningbootsEnchantment;
import net.sashakyotoz.unseenworld.enchantment.ShiningBladeEnchantment;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.enchantment.Enchantment;

public class UnseenWorldModEnchantments {
	public static final DeferredRegister<Enchantment> REGISTRY = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, UnseenWorldMod.MODID);
	public static final RegistryObject<Enchantment> GRAVITY_SPIKE = REGISTRY.register("gravity_spike", () -> new GravitySpikeEnchantment());
	public static final RegistryObject<Enchantment> LIFE_STEEL = REGISTRY.register("life_steel", () -> new LifeSteelEnchantment());
	public static final RegistryObject<Enchantment> MININGBOOTS = REGISTRY.register("miningboots", () -> new MiningbootsEnchantment());
	public static final RegistryObject<Enchantment> SHINING_BLADE = REGISTRY.register("shining_blade", () -> new ShiningBladeEnchantment());
}
