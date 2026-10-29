package net.sashakyotoz.common.world.features.placements;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
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

    public static final ResourceKey<PlacedFeature> CORONA_STONE_BOULDER = create("corona_stone_boulder");
    public static final ResourceKey<PlacedFeature> GILDED_CURRANTSLATE_BOULDER = create("gilded_currantslate_boulder");
    public static final ResourceKey<PlacedFeature> CORONA_STONE_RING = create("corona_stone_ring");
    public static final ResourceKey<PlacedFeature> CORONA_BLOOM_PATCH = create("corona_bloom_patch");
    public static final ResourceKey<PlacedFeature> FROSTBLOOM_PATCH = create("frostbloom_patch");
    public static final ResourceKey<PlacedFeature> ASHREED_PATCH = create("ashreed_patch");
    public static final ResourceKey<PlacedFeature> GLOWROOT_VINES = create("glowroot_vines");
    public static final ResourceKey<PlacedFeature> UMBRAL_CORAL_REEF = create("umbral_coral_reef");
    public static final ResourceKey<PlacedFeature> DEAD_UMBRAL_CORAL_REEF = create("dead_umbral_coral_reef");
    public static final ResourceKey<PlacedFeature> CHIMERIC_FREEZE = create("chimeric_freeze");

    public static final ResourceKey<PlacedFeature> IRON_ORE_UPPER = create("iron_ore_upper");
    public static final ResourceKey<PlacedFeature> HOLLOWS_FLOOR_MOSS = create("hollows_floor_moss");
    public static final ResourceKey<PlacedFeature> HOLLOWS_CEILING_MOSS = create("hollows_ceiling_moss");
    public static final ResourceKey<PlacedFeature> HOLLOWS_GLOW_LICHEN = create("hollows_glow_lichen");
    public static final ResourceKey<PlacedFeature> HOLLOWS_BURLYWOOD_TREE = create("hollows_burlywood_tree");
    public static final ResourceKey<PlacedFeature> HOLLOWS_BURLYWOOD_BUSH = create("hollows_burlywood_bush");
    public static final ResourceKey<PlacedFeature> SHINY_ICE_CEILING_BOULDER = create("shiny_ice_ceiling_boulder");
    public static final ResourceKey<PlacedFeature> SHINY_FROSTBLOOM = create("shiny_frostbloom");
    public static final ResourceKey<PlacedFeature> FROZEN_CAVE_POOL = create("frozen_cave_pool");
    public static final ResourceKey<PlacedFeature> TAPROOT_TREE = create("taproot_tree");
    public static final ResourceKey<PlacedFeature> HOARFROST_PATCH = create("hoarfrost_patch");
    public static final ResourceKey<PlacedFeature> HOLLOWS_GLEAMCARVER_NEST = create("hollows_gleamcarver_nest");
    public static final ResourceKey<PlacedFeature> TANZANITE_GLEAMCARVER_NEST = create("tanzanite_gleamcarver_nest");
    public static final ResourceKey<PlacedFeature> SHINY_GLEAMCARVER_NEST = create("shiny_gleamcarver_nest");

    public static void boostrap(BootstrapContext<PlacedFeature> context) {
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
                        PlacementUtils.countExtra(1, 0.5f, 1),
                        ModBlocks.CRIMSONVEIL_SAPLING));
        register(context, TALL_CRIMSONVEIL_TREE, configLookup.getOrThrow(ModConfiguredFeatures.TALL_CRIMSONVEIL_TREE),
                VegetationPlacements.treePlacement(
                        PlacementUtils.countExtra(1, 0.5f, 2),
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
                BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(List.of(ModBlocks.TANZANITE_BLOCK, Blocks.CALCITE, ModBlocks.CORONA_STONE))),
                BiomeFilter.biome()
        );
        register(
                context,
                TITANIUM_CURRANTSLATE_DISK,
                configLookup.getOrThrow(ModConfiguredFeatures.TITANIUM_CURRANTSLATE_DISK),
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(-48), VerticalAnchor.absolute(16)),
                BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(List.of(ModBlocks.TANZANITE_BLOCK, Blocks.CALCITE, ModBlocks.CORONA_STONE))),
                BiomeFilter.biome()
        );

        register(context, CORONA_STONE_BOULDER, configLookup.getOrThrow(ModConfiguredFeatures.CORONA_STONE_BOULDER),
                RarityFilter.onAverageOnceEvery(3),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                BiomeFilter.biome());
        register(context, GILDED_CURRANTSLATE_BOULDER, configLookup.getOrThrow(ModConfiguredFeatures.GILDED_CURRANTSLATE_BOULDER),
                RarityFilter.onAverageOnceEvery(4),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                BiomeFilter.biome());
        register(context, CORONA_STONE_RING, configLookup.getOrThrow(ModConfiguredFeatures.CORONA_STONE_RING),
                RarityFilter.onAverageOnceEvery(12),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome());
        register(context, CORONA_BLOOM_PATCH, configLookup.getOrThrow(ModConfiguredFeatures.CORONA_BLOOM_PATCH),
                RarityFilter.onAverageOnceEvery(2),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome());
        register(context, FROSTBLOOM_PATCH, configLookup.getOrThrow(ModConfiguredFeatures.FROSTBLOOM_PATCH),
                RarityFilter.onAverageOnceEvery(3),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome());
        register(context, ASHREED_PATCH, configLookup.getOrThrow(ModConfiguredFeatures.ASHREED_PATCH),
                CountPlacement.of(4),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome());
        register(context, GLOWROOT_VINES, configLookup.getOrThrow(ModConfiguredFeatures.GLOWROOT_VINES),
                CountPlacement.of(96),
                InSquarePlacement.spread(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.hasSturdyFace(Direction.DOWN), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
                RandomOffsetPlacement.vertical(ConstantInt.of(-1)),
                BiomeFilter.biome());
        register(context, UMBRAL_CORAL_REEF, configLookup.getOrThrow(ModConfiguredFeatures.UMBRAL_CORAL_REEF),
                NoiseBasedCountPlacement.of(10, 400.0D, 0.0D),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,
                BiomeFilter.biome());
        register(context, DEAD_UMBRAL_CORAL_REEF, configLookup.getOrThrow(ModConfiguredFeatures.DEAD_UMBRAL_CORAL_REEF),
                RarityFilter.onAverageOnceEvery(2),
                CountPlacement.of(2),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,
                BiomeFilter.biome());
        register(context, CHIMERIC_FREEZE, configLookup.getOrThrow(ModConfiguredFeatures.CHIMERIC_FREEZE), BiomeFilter.biome());

        register(context, IRON_ORE_UPPER, configLookup.getOrThrow(ModConfiguredFeatures.IRON_ORE),
                ModOrePlacement.modifiersWithCount(6,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(32), VerticalAnchor.absolute(160))));
        register(context, HOLLOWS_FLOOR_MOSS, configLookup.getOrThrow(ModConfiguredFeatures.HOLLOWS_FLOOR_MOSS),
                CountPlacement.of(125),
                InSquarePlacement.spread(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
                RandomOffsetPlacement.vertical(ConstantInt.of(1)),
                BiomeFilter.biome());
        register(context, HOLLOWS_CEILING_MOSS, configLookup.getOrThrow(ModConfiguredFeatures.HOLLOWS_CEILING_MOSS),
                CountPlacement.of(125),
                InSquarePlacement.spread(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
                RandomOffsetPlacement.vertical(ConstantInt.of(-1)),
                BiomeFilter.biome());
        register(context, HOLLOWS_GLOW_LICHEN, configLookup.getOrThrow(ModConfiguredFeatures.HOLLOWS_GLOW_LICHEN),
                CountPlacement.of(UniformInt.of(60, 100)),
                InSquarePlacement.spread(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                BiomeFilter.biome());
        register(context, HOLLOWS_BURLYWOOD_TREE, configLookup.getOrThrow(ModConfiguredFeatures.SMALL_BURLYWOOD_TREE),
                caveFloorPlant(3, ModBlocks.BURLYWOOD_SAPLING));
        register(context, HOLLOWS_BURLYWOOD_BUSH, configLookup.getOrThrow(ModConfiguredFeatures.BUSH_BURLYWOOD_TREE),
                caveFloorPlant(8, ModBlocks.BURLYWOOD_SAPLING));
        register(context, SHINY_ICE_CEILING_BOULDER, configLookup.getOrThrow(ModConfiguredFeatures.SHINY_ICE_CEILING_BOULDER),
                CountPlacement.of(16),
                InSquarePlacement.spread(),
                BiomeFilter.biome(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 6),
                RandomOffsetPlacement.vertical(ConstantInt.of(1)));
        register(context, SHINY_FROSTBLOOM, configLookup.getOrThrow(ModConfiguredFeatures.FROSTBLOOM_PATCH),
                CountPlacement.of(6),
                InSquarePlacement.spread(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
                RandomOffsetPlacement.vertical(ConstantInt.of(1)),
                BiomeFilter.biome());
        register(context, FROZEN_CAVE_POOL, configLookup.getOrThrow(ModConfiguredFeatures.FROZEN_CAVE_POOL),
                CountPlacement.of(24),
                InSquarePlacement.spread(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.matchesBlocks(ModBlocks.DARK_WATER), BlockPredicate.ONLY_IN_AIR_PREDICATE, 16),
                BiomeFilter.biome());
        register(context, TAPROOT_TREE, configLookup.getOrThrow(ModConfiguredFeatures.TAPROOT_TREE),
                CountPlacement.of(2),
                InSquarePlacement.spread(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
                RandomOffsetPlacement.vertical(ConstantInt.of(-1)),
                BiomeFilter.biome());
        register(context, HOARFROST_PATCH, configLookup.getOrThrow(ModConfiguredFeatures.HOARFROST_PATCH),
                CountPlacement.of(10),
                InSquarePlacement.spread(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
                RandomOffsetPlacement.vertical(ConstantInt.of(1)),
                BiomeFilter.biome());
        register(context, HOLLOWS_GLEAMCARVER_NEST, configLookup.getOrThrow(ModConfiguredFeatures.HOLLOWS_GLEAMCARVER_NEST), caveNest());
        register(context, TANZANITE_GLEAMCARVER_NEST, configLookup.getOrThrow(ModConfiguredFeatures.TANZANITE_GLEAMCARVER_NEST), caveNest());
        register(context, SHINY_GLEAMCARVER_NEST, configLookup.getOrThrow(ModConfiguredFeatures.SHINY_GLEAMCARVER_NEST), caveNest());
    }

    private static List<PlacementModifier> caveNest() {
        return List.of(
                CountPlacement.of(6),
                InSquarePlacement.spread(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
                RandomOffsetPlacement.vertical(ConstantInt.of(1)),
                RarityFilter.onAverageOnceEvery(8),
                BiomeFilter.biome());
    }

    private static List<PlacementModifier> caveFloorPlant(int count, Block sapling) {
        return List.of(
                CountPlacement.of(count),
                InSquarePlacement.spread(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
                RandomOffsetPlacement.vertical(ConstantInt.of(1)),
                BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(sapling.defaultBlockState(), BlockPos.ZERO)),
                BiomeFilter.biome());
    }

    public static ResourceKey<PlacedFeature> create(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, UnseenWorld.MOD.makeID(name));
    }

    private static void register(BootstrapContext<PlacedFeature> context,
                                 ResourceKey<PlacedFeature> key,
                                 Holder<ConfiguredFeature<?, ?>> config,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(config, List.copyOf(modifiers)));
    }

    private static void register(BootstrapContext<PlacedFeature> context,
                                 ResourceKey<PlacedFeature> key,
                                 Holder<ConfiguredFeature<?, ?>> config,
                                 PlacementModifier... modifiers) {
        register(context, key, config, List.of(modifiers));
    }
}