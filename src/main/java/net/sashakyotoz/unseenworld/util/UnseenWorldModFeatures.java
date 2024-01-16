package net.sashakyotoz.unseenworld.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.world.features.*;
import net.sashakyotoz.unseenworld.world.features.underground.*;

public class UnseenWorldModFeatures {
	public static final DeferredRegister<Feature<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.FEATURES, UnseenWorldMod.MODID);
	public static final RegistryObject<Feature<?>> GRASS_BLOCK_OF_SHINY_REDLIGHT = REGISTRY.register("grass_block_of_shiny_redlight", GrassBlockOfShinyRedlightFeature::new);
	public static final RegistryObject<Feature<?>> GRIZZLY_LIGHT_BLOCK = REGISTRY.register("grizzly_light_block", GrizzlyLightBlockFeature::new);
	public static final RegistryObject<Feature<?>> NETHERFLYISLAND = REGISTRY.register("netherflyisland", NetherflyislandFeature::new);
	public static final RegistryObject<Feature<?>> GRIZZLY_SMALL_TREE = REGISTRY.register("grizzly_small_tree", GrizzlySmallTreeFeature::new);
	public static final RegistryObject<Feature<?>> TEALIVY_TREE = REGISTRY.register("tealivy_tree", TealivyTreeFeature::new);
	public static final RegistryObject<Feature<?>> TEALIVY_BIG_TREE = REGISTRY.register("tealivy_big_tree", TealivyBigTreeFeature::new);
	public static final RegistryObject<Feature<?>> LIQUID_OF_CHIMERY_FEATURE = REGISTRY.register("liquid_of_chimery_feature", LiquidOfChimeryFeature::new);
	public static final RegistryObject<Feature<?>> GRIZZLY_LIGHT_IN_CAVERNS = REGISTRY.register("grizzly_light_in_caverns", GrizzlyLightInCavernsFeature::new);
	public static final RegistryObject<Feature<?>> TANZANITE_DELTAS = REGISTRY.register("tanzanite_deltas", TanzaniteDeltasFeature::new);
	public static final RegistryObject<Feature<?>> TEALIVE_VALLEY_ROCKS = REGISTRY.register("tealive_valley_rocks", TealiveValleyRocksFeature::new);
	public static final RegistryObject<Feature<?>> DARK_BEACH_OOZE = REGISTRY.register("dark_beach_ooze", DarkBeachOozeFeature::new);
	public static final RegistryObject<Feature<?>> DRIPSTONE_OF_AMETHYST_OVERGROWTH_CLUSTER = REGISTRY.register("dripstone_of_amethyst_overgrowth_cluster", DripstoneOfAmethystOvergrowthClusterFeature::new);
	public static final RegistryObject<Feature<?>> DEAD_CORAL_TREES = REGISTRY.register("dead_coral_trees", DeadCoralTreesFeature::new);
	public static final RegistryObject<Feature<?>> SHINY_CAVERNS_LIGHT = REGISTRY.register("shiny_caverns_light", ShinyCavernsLightFeature::new);
	public static final RegistryObject<Feature<?>> SPIKE_FEATURE = REGISTRY.register("spike_feature",SpikesFeature::new);
	public static final ResourceKey<ConfiguredFeature<?,?>> AMETHYST_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE,new ResourceLocation("unseen_world","amethyst_forest_tree"));
	public static final ResourceKey<ConfiguredFeature<?,?>> TEALIVE_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE,new ResourceLocation("unseen_world","tealivy_tree_1"));
	public static final ResourceKey<ConfiguredFeature<?,?>> GREENISH_BURLYWOOD_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE,new ResourceLocation("unseen_world","greenish_tree"));
	public static final ResourceKey<ConfiguredFeature<?,?>> MEGA_GREENISH_BURLYWOOD_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE,new ResourceLocation("unseen_world","greenish_big_tree"));
	public static final ResourceKey<ConfiguredFeature<?,?>> DARK_CRIMSON_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE,new ResourceLocation("unseen_world","darkcrimsonforest_tree"));
	public static final ResourceKey<ConfiguredFeature<?,?>> GRIZZLY_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE,new ResourceLocation("unseen_world","grizzly_forest_trees"));
}