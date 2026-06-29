package net.sashakyotoz.common;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.sashakyotoz.common.datagen.*;
import net.sashakyotoz.common.datagen.tags.*;
import net.sashakyotoz.common.world.ModDimensions;
import net.sashakyotoz.common.world.biomes.ModBiomes;
import net.sashakyotoz.common.world.carvers.ModCarvers;
import net.sashakyotoz.common.world.features.ModConfiguredFeatures;
import net.sashakyotoz.common.world.features.placements.ModPlacements;

import java.util.Map;

public class UnseenWorldDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(ModAdvancementProvider::new);
		pack.addProvider(ModBlockTagProvider::new);
		pack.addProvider(ModFluidTagProvider::new);
		pack.addProvider(ModBiomeTagProvider::new);
		pack.addProvider(ModItemTagProvider::new);
		pack.addProvider(ModGameEventTagProvider::new);
		pack.addProvider(ModEntityTagProvider::new);
		pack.addProvider(ModBlockLootTableProvider::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModWorldGenerator::new);
		pack.addProvider(ModLanguageProvider::new);
		pack.addProvider(ModRecipeProvider::new);
	}
	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::boostrap);
		registryBuilder.add(Registries.PLACED_FEATURE, ModPlacements::boostrap);
		registryBuilder.add(Registries.BIOME, ModBiomes::boostrap);
		registryBuilder.add(Registries.CONFIGURED_CARVER, ModCarvers::boostrap);
		registryBuilder.add(Registries.DIMENSION_TYPE, ModDimensions::bootstrapType);
	}
	public static void registerFuels() {
		for (Map.Entry<ItemLike, Integer> entry : ModRegistry.ITEM_BURNABLE.entrySet())
			FuelRegistry.INSTANCE.add(entry.getKey(), entry.getValue());
	}

	public static void registerBurnable() {
		FlammableBlockRegistry registry = FlammableBlockRegistry.getDefaultInstance();
		for (Map.Entry<Block, Tuple<Integer, Integer>> entry : ModRegistry.BLOCK_FLAMMABLE.entrySet()) {
			registry.add(entry.getKey(), entry.getValue().getA(), entry.getValue().getB());
		}
	}

	public static void registerStripped() {
		for (Map.Entry<Block, Block> entry : ModRegistry.BLOCK_STRIPPED.entrySet())
			StrippableBlockRegistry.register(entry.getKey(), entry.getValue());
	}
}