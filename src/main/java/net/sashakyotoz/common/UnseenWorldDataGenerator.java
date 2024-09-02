package net.sashakyotoz.common;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Pair;
import net.sashakyotoz.common.datagen.*;
import net.sashakyotoz.common.world.biomes.ModBiomes;
import net.sashakyotoz.common.world.features.ModConfiguredFeatures;
import net.sashakyotoz.common.world.ModDimensions;
import net.sashakyotoz.common.world.features.ModPlacements;

import java.util.Map;

public class UnseenWorldDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(ModBlockTagProvider::new);
		pack.addProvider(ModBiomeTagProvider::new);
		pack.addProvider(ModItemTagProvider::new);
		pack.addProvider(ModLootTableProvider::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModWorldGenerator::new);
		pack.addProvider(ModLanguageProvider::new);
		pack.addProvider(ModRecipeProvider::new);
	}
	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ModConfiguredFeatures::boostrap);
		registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, ModPlacements::boostrap);
		registryBuilder.addRegistry(RegistryKeys.BIOME, ModBiomes::boostrap);
		registryBuilder.addRegistry(RegistryKeys.DIMENSION_TYPE, ModDimensions::bootstrapType);
	}
	public static void registerFuels() {
		for (Map.Entry<ItemConvertible, Integer> entry : ModRegistry.ITEM_BURNABLE.entrySet())
			FuelRegistry.INSTANCE.add(entry.getKey(), entry.getValue());
	}

	public static void registerBurnable() {
		FlammableBlockRegistry registry = FlammableBlockRegistry.getDefaultInstance();
		for (Map.Entry<Block, Pair<Integer, Integer>> entry : ModRegistry.BLOCK_FLAMMABLE.entrySet()) {
			registry.add(entry.getKey(), entry.getValue().getLeft(), entry.getValue().getRight());
		}
	}

	public static void registerStripped() {
		for (Map.Entry<Block, Block> entry : ModRegistry.BLOCK_STRIPPED.entrySet())
			StrippableBlockRegistry.register(entry.getKey(), entry.getValue());
	}
}