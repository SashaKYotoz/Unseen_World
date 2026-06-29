package net.sashakyotoz.common.world.features.placements;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.world.features.ModConfiguredFeatures;

import java.util.List;

public class ModPlacements {
    public static final ResourceKey<PlacedFeature> AMETHYST_TREE = create("amethyst_tree");
    public static final ResourceKey<PlacedFeature> SMALL_AMETHYST_TREE = create("small_amethyst_tree");
    public static final ResourceKey<PlacedFeature> AMETHYST_BUSH = create("amethyst_bush");
    public static final ResourceKey<PlacedFeature> GRIZZLY_TREE = create("grizzly_tree");
    public static final ResourceKey<PlacedFeature> TALL_GRIZZLY_TREE = create("tall_grizzly_tree");
    public static final ResourceKey<PlacedFeature> TEALIVE_TREE = create("tealive_tree");
    public static final ResourceKey<PlacedFeature> CRIMSONVEIL_TREE = create("crimsonveil_tree");
    public static final ResourceKey<PlacedFeature> TALL_CRIMSONVEIL_TREE = create("tall_crimsonveil_tree");
    public static final ResourceKey<PlacedFeature> BURLYWOOD_TREE = create("burlywood_tree");
    public static final ResourceKey<PlacedFeature> SMALL_BURLYWOOD_TREE = create("small_burlywood_tree");
    public static final ResourceKey<PlacedFeature> BUSH_BURLYWOOD_TREE = create("bush_burlywood_tree");

    public static final ResourceKey<PlacedFeature> UNSEENIUM_ORE = create("unseenium_ore");
    public static final ResourceKey<PlacedFeature> RED_TITANIUM_ORE = create("red_titanium_ore");
    public static final ResourceKey<PlacedFeature> ABYSSAL_ORE = create("abyssal_ore");
    public static final ResourceKey<PlacedFeature> IRON_ORE = create("iron_ore");
    public static final ResourceKey<PlacedFeature> ANCIENT_DEBRIS = create("ancient_debris");

    public static final ResourceKey<PlacedFeature> TANZANITE_SPHERE = create("tanzanite_sphere");

    public static final ResourceKey<PlacedFeature> AMETHYST_FLOWERS = create("amethyst_flowers");

    public static final ResourceKey<PlacedFeature> GLACIEMITE_BOULDER = create("glaciemite_boulder");
    public static final ResourceKey<PlacedFeature> DARK_CURRANTSLATE_BOULDER = create("dark_currantslate_boulder");
    public static final ResourceKey<PlacedFeature> AMETHYST_BOULDER = create("amethyst_boulder");

    public static final ResourceKey<PlacedFeature> AMETHYST_CEILING_BOULDER = create("amethyst_ceiling_boulder");
    public static final ResourceKey<PlacedFeature> DARK_CURRANTSLATE_CEILING_BOULDER = create("dark_currantslate_ceiling_boulder");

    public static final ResourceKey<PlacedFeature> DARKNESS_SPIRAL_SPIKE = create("darkness_spiral_spike");
    public static final ResourceKey<PlacedFeature> GRIPTONITE_CLUSTER_SPIKE = create("griptonite_cluster_spike");
    public static final ResourceKey<PlacedFeature> AMETHYST_CLUSTER_SPIKE = create("amethyst_cluster_spike");

    public static final ResourceKey<PlacedFeature> GRIPCRYSTAL_WART_PATCH = create("gripcrystal_wart_patch");

    public static final ResourceKey<PlacedFeature> WATER_LAKE = create("water_lake");
    public static final ResourceKey<PlacedFeature> DARK_WATER_LAKE = create("dark_water_lake");
    public static final ResourceKey<PlacedFeature> CRIMSONVEIL_DARK_WATER_LAKE = create("crimsonveil_dark_water_lake");

    public static final ResourceKey<PlacedFeature> BEARFRUIT_BRAMBLE_PATCH = create("bearfruit_bramble_patch");
    public static final ResourceKey<PlacedFeature> MIDNIGHT_LILY_PATCH = create("midnight_lily_patch");
    public static final ResourceKey<PlacedFeature> BUSH_LIKE_TREE_PATCH = create("bush_like_tree_patch");
    public static final ResourceKey<PlacedFeature> SMALL_BUSH_LIKE_TREE_PATCH = create("small_bush_like_tree_patch");
    public static final ResourceKey<PlacedFeature> BURLYWOOD_VIOLET_PATCH = create("burlywood_violet_patch");

    public static final ResourceKey<PlacedFeature> UMBRAL_KELP = create("umbral_kelp");
    public static final ResourceKey<PlacedFeature> RARE_GLOOMWEED_PATCH = create("rare_gloomweed_patch");
    public static final ResourceKey<PlacedFeature> GLOOMWEED_PATCH = create("gloomweed_patch");
    public static final ResourceKey<PlacedFeature> TALL_GLOOMWEED_PATCH = create("tall_gloomweed_patch");

    public static final ResourceKey<PlacedFeature> GRIPPING_GLACIEMITE_PATCH_CEILING = create("gripping_glaciemite_patch_ceiling");
    public static final ResourceKey<PlacedFeature> GRIPPING_DARK_CURRANTSLATE_PATCH_FLOOR = create("gripping_dark_currantslate_patch_floor");

    public static final ResourceKey<PlacedFeature> UNSEENIUM_CURRANTSLATE_DISK = create("unseenium_currantslate_disk");
    public static final ResourceKey<PlacedFeature> TITANIUM_CURRANTSLATE_DISK = create("titanium_currantslate_disk");

    public static void boostrap(BootstapContext<PlacedFeature> context) {
        var configLookup = context.lookup(Registries.CONFIGURED_FEATURE);
        register(context, AMETHYST_TREE, configLookup.getOrThrow(ModConfiguredFeatures.AMETHYST_TREE),
                VegetationPlacements.treePlacement(
                        PlacementUtils.countExtra(1, 0.5f, 2),
                        ModBlocks.AMETHYST_SAPLING));
        register(context, SMALL_AMETHYST_TREE, configLookup.getOrThrow(ModConfiguredFeatures.SMALL_AMETHYST_TREE),
                VegetationPlacements.treePlacement(
                        PlacementUtils.countExtra(1, 0.5f, 4),
                        ModBlocks.AMETHYST_SAPLING));
        register(context, AMETHYST_BUSH, configLookup.getOrThrow(ModConfiguredFeatures.AMETHYST_BUSH),
                VegetationPlacements.treePlacement(
                        RarityFilter.onAverageOnceEvery(8),
                        ModBlocks.AMETHYST_SAPLING));
        register(context, GRIZZLY_TREE, configLookup.getOrThrow(ModConfiguredFeatures.GRIZZLY_TREE),
                VegetationPlacements.treePlacement(
                        PlacementUtils.countExtra(1, 0.5f, 2),
                        ModBlocks.GRIZZLY_SAPLING));
        register(context, TALL_GRIZZLY_TREE, configLookup.getOrThrow(ModConfiguredFeatures.TALL_GRIZZLY_TREE),
                VegetationPlacements.treePlacement(
                        PlacementUtils.countExtra(1, 0.5f, 1),
                        ModBlocks.GRIZZLY_SAPLING));
        register(context, TEALIVE_TREE, configLookup.getOrThrow(ModConfiguredFeatures.TEALIVE_TREE),
                VegetationPlacements.treePlacement(
                        PlacementUtils.countExtra(1, 0.5f, 2),
                        ModBlocks.TEALIVE_SAPLING));
        register(context, CRIMSONVEIL_TREE, configLookup.getOrThrow(ModConfiguredFeatures.CRIMSONVEIL_TREE),
                VegetationPlacements.treePlacement(
                        PlacementUtils.countExtra(1, 0.5f, 2),
                        ModBlocks.CRIMSONVEIL_SAPLING));
        register(context, TALL_CRIMSONVEIL_TREE, configLookup.getOrThrow(ModConfiguredFeatures.TALL_CRIMSONVEIL_TREE),
                VegetationPlacements.treePlacement(
                        PlacementUtils.countExtra(1, 0.5f, 3),
                        ModBlocks.CRIMSONVEIL_SAPLING));
        register(context, BURLYWOOD_TREE, configLookup.getOrThrow(ModConfiguredFeatures.BURLYWOOD_TREE),
                VegetationPlacements.treePlacement(
                        PlacementUtils.countExtra(1, 0.5f, 3),
                        ModBlocks.BURLYWOOD_SAPLING));
        register(context, SMALL_BURLYWOOD_TREE, configLookup.getOrThrow(ModConfiguredFeatures.SMALL_BURLYWOOD_TREE),
                VegetationPlacements.treePlacement(
                        PlacementUtils.countExtra(2, 0.5f, 3),
                        ModBlocks.BURLYWOOD_SAPLING));
        register(context, BUSH_BURLYWOOD_TREE, configLookup.getOrThrow(ModConfiguredFeatures.BUSH_BURLYWOOD_TREE),
                VegetationPlacements.treePlacement(
                        PlacementUtils.countExtra(2, 0.5f, 4),
                        ModBlocks.BURLYWOOD_SAPLING));

        register(context, UNSEENIUM_ORE, configLookup.getOrThrow(ModConfiguredFeatures.UNSEENIUM_ORE),
                ModOrePlacement.modifiersWithCount(4,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(48))));
        register(context, RED_TITANIUM_ORE, configLookup.getOrThrow(ModConfiguredFeatures.RED_TITANIUM_ORE),
                ModOrePlacement.modifiersWithCount(2,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32))));
        register(context, ABYSSAL_ORE, configLookup.getOrThrow(ModConfiguredFeatures.ABYSSAL_ORE),
                ModOrePlacement.modifiersWithCount(3,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32))));
        register(context, IRON_ORE, configLookup.getOrThrow(ModConfiguredFeatures.IRON_ORE),
                ModOrePlacement.modifiersWithCount(4,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-32), VerticalAnchor.absolute(48))));
        register(context, ANCIENT_DEBRIS, configLookup.getOrThrow(ModConfiguredFeatures.ANCIENT_DEBRIS),
                ModOrePlacement.modifiersWithRarity(20,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(0))));

        register(context, TANZANITE_SPHERE, configLookup.getOrThrow(ModConfiguredFeatures.TANZANITE_SPHERE),
                ModOrePlacement.modifiersWithCount(3,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(48))));

        register(context, GLACIEMITE_BOULDER, configLookup.getOrThrow(ModConfiguredFeatures.GLACIEMITE_BOULDER),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                BiomeFilter.biome(),
                RarityFilter.onAverageOnceEvery(3));
        register(context, DARK_CURRANTSLATE_BOULDER, configLookup.getOrThrow(ModConfiguredFeatures.DARK_CURRANTSLATE_BOULDER),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                BiomeFilter.biome(),
                RarityFilter.onAverageOnceEvery(4));
        register(context, AMETHYST_BOULDER, configLookup.getOrThrow(ModConfiguredFeatures.AMETHYST_BOULDER),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome(),
                RarityFilter.onAverageOnceEvery(4));
        register(context, AMETHYST_CEILING_BOULDER, configLookup.getOrThrow(ModConfiguredFeatures.AMETHYST_CEILING_BOULDER),
                CountPlacement.of(32),
                InSquarePlacement.spread(),
                BiomeFilter.biome(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 6),
                RandomOffsetPlacement.vertical(ConstantInt.of(1))
        );
        register(context, DARK_CURRANTSLATE_CEILING_BOULDER, configLookup.getOrThrow(ModConfiguredFeatures.DARK_CURRANTSLATE_CEILING_BOULDER),
                CountPlacement.of(32),
                InSquarePlacement.spread(),
                BiomeFilter.biome(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 5),
                RandomOffsetPlacement.vertical(ConstantInt.of(1))
        );

        register(
                context,
                DARKNESS_SPIRAL_SPIKE,
                configLookup.getOrThrow(ModConfiguredFeatures.DARKNESS_SPIRAL_SPIKE),
                RarityFilter.onAverageOnceEvery(6),
                CountPlacement.of(2),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome()
        );
        register(
                context,
                GRIPTONITE_CLUSTER_SPIKE,
                configLookup.getOrThrow(ModConfiguredFeatures.GRIPTONITE_CLUSTER_SPIKE),
                RarityFilter.onAverageOnceEvery(8),
                CountPlacement.of(2),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome()
        );
        register(
                context,
                AMETHYST_CLUSTER_SPIKE,
                configLookup.getOrThrow(ModConfiguredFeatures.AMETHYST_CLUSTER_SPIKE),
                RarityFilter.onAverageOnceEvery(8),
                CountPlacement.of(2),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome()
        );
        register(
                context,
                GRIPCRYSTAL_WART_PATCH,
                configLookup.getOrThrow(ModConfiguredFeatures.GRIPCRYSTAL_WART_PATCH),
                CountPlacement.of(1), RarityFilter.onAverageOnceEvery(4), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()
        );

        register(context, WATER_LAKE, configLookup.getOrThrow(ModConfiguredFeatures.WATER_LAKE),
                RarityFilter.onAverageOnceEvery(6),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome());
        register(context, DARK_WATER_LAKE, configLookup.getOrThrow(ModConfiguredFeatures.DARK_WATER_LAKE),
                RarityFilter.onAverageOnceEvery(24),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome());
        register(context, CRIMSONVEIL_DARK_WATER_LAKE, configLookup.getOrThrow(ModConfiguredFeatures.CRIMSONVEIL_DARK_WATER_LAKE),
                CountPlacement.of(2),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome());

        register(context, BEARFRUIT_BRAMBLE_PATCH, configLookup.getOrThrow(ModConfiguredFeatures.BEARFRUIT_BRAMBLE_PATCH),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome(),
                RarityFilter.onAverageOnceEvery(4));
        register(context, MIDNIGHT_LILY_PATCH, configLookup.getOrThrow(ModConfiguredFeatures.MIDNIGHT_LILY_PATCH),
                RarityFilter.onAverageOnceEvery(4),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(56), VerticalAnchor.absolute(58)),
                InSquarePlacement.spread(),
                BiomeFilter.biome());
        register(context, BUSH_LIKE_TREE_PATCH, configLookup.getOrThrow(ModConfiguredFeatures.BUSH_LIKE_TREE_PATCH),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome(),
                BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.GLOW_APPLE_BUSH.defaultBlockState(), BlockPos.ZERO)),
                RarityFilter.onAverageOnceEvery(2));
        register(context, SMALL_BUSH_LIKE_TREE_PATCH, configLookup.getOrThrow(ModConfiguredFeatures.SMALL_BUSH_LIKE_TREE_PATCH),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome(),
                BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(ModBlocks.GLOW_APPLE_BUSH.defaultBlockState(), BlockPos.ZERO)),
                RarityFilter.onAverageOnceEvery(2));
        register(context, BURLYWOOD_VIOLET_PATCH, configLookup.getOrThrow(ModConfiguredFeatures.BURLYWOOD_VIOLET_PATCH),
                RarityFilter.onAverageOnceEvery(3),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome());

        register(
                context,
                UMBRAL_KELP,
                configLookup.getOrThrow(ModConfiguredFeatures.UMBRAL_KELP),
                NoiseBasedCountPlacement.of(80, 72.0, 0.0),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,
                BiomeFilter.biome()
        );

        register(
                context,
                AMETHYST_FLOWERS,
                configLookup.getOrThrow(ModConfiguredFeatures.AMETHYST_FLOWERS),
                NoiseThresholdCountPlacement.of(-0.85, 3, 6),
                InSquarePlacement.spread(),
                RarityFilter.onAverageOnceEvery(4),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome()
        );
        register(
                context,
                RARE_GLOOMWEED_PATCH,
                configLookup.getOrThrow(ModConfiguredFeatures.RARE_GLOOMWEED_PATCH),
                CountPlacement.of(1), RarityFilter.onAverageOnceEvery(8), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()
        );
        register(
                context,
                GLOOMWEED_PATCH,
                configLookup.getOrThrow(ModConfiguredFeatures.GLOOMWEED_PATCH),
                CountPlacement.of(4), RarityFilter.onAverageOnceEvery(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()
        );
        register(
                context,
                TALL_GLOOMWEED_PATCH,
                configLookup.getOrThrow(ModConfiguredFeatures.TALL_GLOOMWEED_PATCH),
                CountPlacement.of(2), RarityFilter.onAverageOnceEvery(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()
        );
        register(
                context,
                GRIPPING_GLACIEMITE_PATCH_CEILING,
                configLookup.getOrThrow(ModConfiguredFeatures.GRIPPING_GLACIEMITE_PATCH_CEILING),
                CountPlacement.of(64),
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(56)),
                EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 4),
                RandomOffsetPlacement.vertical(ConstantInt.of(-1)),
                BiomeFilter.biome()
        );
        register(
                context,
                GRIPPING_DARK_CURRANTSLATE_PATCH_FLOOR,
                configLookup.getOrThrow(ModConfiguredFeatures.GRIPPING_DARK_CURRANTSLATE_PATCH_FLOOR),
                CountPlacement.of(64),
                InSquarePlacement.spread(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 4),
                RandomOffsetPlacement.vertical(ConstantInt.of(1)),
                BiomeFilter.biome()
        );
        register(
                context,
                UNSEENIUM_CURRANTSLATE_DISK,
                configLookup.getOrThrow(ModConfiguredFeatures.UNSEENIUM_CURRANTSLATE_DISK),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(-48), VerticalAnchor.absolute(32)),
                InSquarePlacement.spread(),
                BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(List.of(ModBlocks.TANZANITE_BLOCK, Blocks.CALCITE))),
                BiomeFilter.biome()
        );
        register(
                context,
                TITANIUM_CURRANTSLATE_DISK,
                configLookup.getOrThrow(ModConfiguredFeatures.TITANIUM_CURRANTSLATE_DISK),
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(-48), VerticalAnchor.absolute(16)),
                BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(List.of(ModBlocks.TANZANITE_BLOCK, Blocks.CALCITE))),
                BiomeFilter.biome()
        );
    }

    public static ResourceKey<PlacedFeature> create(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, UnseenWorld.makeID(name));
    }

    private static void register(BootstapContext<PlacedFeature> context,
                                 ResourceKey<PlacedFeature> key,
                                 Holder<ConfiguredFeature<?, ?>> config,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(config, List.copyOf(modifiers)));
    }

    private static void register(BootstapContext<PlacedFeature> context,
                                 ResourceKey<PlacedFeature> key,
                                 Holder<ConfiguredFeature<?, ?>> config,
                                 PlacementModifier... modifiers) {
        register(context, key, config, List.of(modifiers));
    }
}