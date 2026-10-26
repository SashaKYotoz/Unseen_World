package net.sashakyotoz.common.world.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BushFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.rootplacers.AboveRootPlacement;
import net.minecraft.world.level.levelgen.feature.rootplacers.MangroveRootPlacement;
import net.minecraft.world.level.levelgen.feature.rootplacers.MangroveRootPlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.*;
import net.minecraft.world.level.levelgen.feature.treedecorators.AttachedToLeavesDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.CherryTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.UpwardsBranchingTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.custom.BurlywoodHeartBlock;
import net.sashakyotoz.common.blocks.custom.plants.DarknessFernBlock;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.tags.ModTags;
import net.sashakyotoz.common.world.features.custom.*;
import net.sashakyotoz.common.world.features.custom.configs.*;
import net.sashakyotoz.common.world.features.trees.placers.*;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> AMETHYST_TREE = create("amethyst_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_AMETHYST_TREE = create("small_amethyst_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> AMETHYST_BUSH = create("amethyst_bush");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CRIMSONVEIL_TREE = create("crimsonveil_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TALL_CRIMSONVEIL_TREE = create("tall_crimsonveil_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TEALIVE_TREE = create("tealive_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GRIZZLY_TREE = create("grizzly_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TALL_GRIZZLY_TREE = create("tall_grizzly_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BURLYWOOD_TREE = create("burlywood_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_BURLYWOOD_TREE = create("small_burlywood_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BUSH_BURLYWOOD_TREE = create("bush_burlywood_tree");

    public static final ResourceKey<ConfiguredFeature<?, ?>> UNSEENIUM_ORE = create("unseenium_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> RED_TITANIUM_ORE = create("red_titanium_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ABYSSAL_ORE = create("abyssal_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> IRON_ORE = create("iron_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ANCIENT_DEBRIS = create("ancient_debris");

    public static final ResourceKey<ConfiguredFeature<?, ?>> TANZANITE_SPHERE = create("tanzanite_sphere");

    public static final ResourceKey<ConfiguredFeature<?, ?>> AMETHYST_FLOWERS = create("amethyst_flowers");

    public static final ResourceKey<ConfiguredFeature<?, ?>> GLACIEMITE_BOULDER = create("glaciemite_boulder");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_CURRANTSLATE_BOULDER = create("dark_currantslate_boulder");
    public static final ResourceKey<ConfiguredFeature<?, ?>> AMETHYST_BOULDER = create("amethyst_boulder");

    public static final ResourceKey<ConfiguredFeature<?, ?>> DARKNESS_SPIRAL_SPIKE = create("darkness_spiral_spike");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GRIPTONITE_CLUSTER_SPIKE = create("griptonite_cluster_spike");
    public static final ResourceKey<ConfiguredFeature<?, ?>> AMETHYST_CLUSTER_SPIKE = create("amethyst_cluster_spike");

    public static final ResourceKey<ConfiguredFeature<?, ?>> GRIPCRYSTAL_WART_PATCH = create("gripcrystal_wart_patch");

    public static final ResourceKey<ConfiguredFeature<?, ?>> BEARFRUIT_BRAMBLE_PATCH = create("bearfruit_bramble_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MIDNIGHT_LILY_PATCH = create("midnight_lily_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BUSH_LIKE_TREE_PATCH = create("bush_like_tree_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_BUSH_LIKE_TREE_PATCH = create("small_bush_like_tree_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BURLYWOOD_VIOLET_PATCH = create("burlywood_violet_patch");

    public static final ResourceKey<ConfiguredFeature<?, ?>> UMBRAL_KELP = create("umbral_kelp");
    public static final ResourceKey<ConfiguredFeature<?, ?>> RARE_GLOOMWEED_PATCH = create("rare_gloomweed_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GLOOMWEED_PATCH = create("gloomweed_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TALL_GLOOMWEED_PATCH = create("tall_gloomweed_patch");

    public static final ResourceKey<ConfiguredFeature<?, ?>> GRIPPING_GLACIEMITE_PATCH_CEILING = create("gripping_glaciemite_patch_ceiling");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GRIPPING_DARK_CURRANTSLATE_PATCH_FLOOR = create("gripping_dark_currantslate_patch_floor");

    public static final ResourceKey<ConfiguredFeature<?, ?>> AMETHYST_CEILING_BOULDER = create("amethyst_ceiling_boulder");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_CURRANTSLATE_CEILING_BOULDER = create("dark_currantslate_ceiling_boulder");

    public static final ResourceKey<ConfiguredFeature<?, ?>> UNSEENIUM_CURRANTSLATE_DISK = create("unseenium_currantslate_disk");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TITANIUM_CURRANTSLATE_DISK = create("titanium_currantslate_disk");

    public static final ResourceKey<ConfiguredFeature<?, ?>> CORONA_STONE_BOULDER = create("corona_stone_boulder");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GILDED_CURRANTSLATE_BOULDER = create("gilded_currantslate_boulder");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CORONA_STONE_RING = create("corona_stone_ring");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CORONA_BLOOM_PATCH = create("corona_bloom_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FROSTBLOOM_PATCH = create("frostbloom_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ASHREED_PATCH = create("ashreed_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GLOWROOT_VINES = create("glowroot_vines");
    public static final ResourceKey<ConfiguredFeature<?, ?>> UMBRAL_CORAL_REEF = create("umbral_coral_reef");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DEAD_UMBRAL_CORAL_REEF = create("dead_umbral_coral_reef");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CHIMERIC_FREEZE = create("chimeric_freeze");

    public static final ResourceKey<ConfiguredFeature<?, ?>> HOLLOWS_FLOOR_VEGETATION = create("hollows_floor_vegetation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HOLLOWS_FLOOR_MOSS = create("hollows_floor_moss");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HOLLOWS_CEILING_MOSS = create("hollows_ceiling_moss");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HOLLOWS_GLOW_LICHEN = create("hollows_glow_lichen");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SHINY_ICE_CEILING_BOULDER = create("shiny_ice_ceiling_boulder");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FROZEN_CAVE_POOL = create("frozen_cave_pool");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TAPROOT_TREE = create("taproot_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HOARFROST_PATCH = create("hoarfrost_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HOLLOWS_GLEAMCARVER_NEST = create("hollows_gleamcarver_nest");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TANZANITE_GLEAMCARVER_NEST = create("tanzanite_gleamcarver_nest");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SHINY_GLEAMCARVER_NEST = create("shiny_gleamcarver_nest");


    public static void boostrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<Block> registryEntryLookup = context.lookup(Registries.BLOCK);
        register(context, AMETHYST_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.AMETHYST_LOG),
                new ThornTrunkPlacer(10, 2, 1, UniformInt.of(3, 5), 1, 1.8F, UniformInt.of(3, 5)),
                BlockStateProvider.simple(ModBlocks.AMETHYST_LEAVES),
                new ThornFoliagePlacer(UniformInt.of(2, 3), ConstantInt.of(0), 1, 0.2F),
                new TwoLayersFeatureSize(1, 1, 2))
                .decorators(
                        List.of(
                                new VinesToLeavesTreeDecorator(
                                        0.125F,
                                        BlockStateProvider.simple(ModBlocks.HANGING_AMETHYST_LEAVES),
                                        UniformInt.of(2, 4)
                                )
                        ))
                .dirt(BlockStateProvider.simple(ModBlocks.AMETHYST_GRASS_BLOCK)).forceDirt().build());
        register(context, SMALL_AMETHYST_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.AMETHYST_LOG),
                new ThornTrunkPlacer(5, 2, 1, UniformInt.of(1, 2), 1, 1.6F, UniformInt.of(3, 5)),
                BlockStateProvider.simple(ModBlocks.AMETHYST_LEAVES),
                new ThornFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 1, 0.25F),
                new TwoLayersFeatureSize(1, 0, 1))
                .dirt(BlockStateProvider.simple(ModBlocks.AMETHYST_GRASS_BLOCK)).forceDirt().build());
        register(context, AMETHYST_BUSH, Feature.TREE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ModBlocks.AMETHYST_LOG),
                        new StraightTrunkPlacer(1, 0, 0),
                        BlockStateProvider.simple(ModBlocks.AMETHYST_LEAVES),
                        new BushFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), 2),
                        new TwoLayersFeatureSize(0, 0, 0)
                ).build());
        register(context, CRIMSONVEIL_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.CRIMSONVEIL_LOG),
                new UpwardsBranchingTrunkPlacer(6, 4, 2, UniformInt.of(1, 3), 0.5F, UniformInt.of(1, 3), registryEntryLookup.getOrThrow(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH)),
                BlockStateProvider.simple(ModBlocks.CRIMSONVEIL_LEAVES),
                new RandomSpreadFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), ConstantInt.of(2), 64),
                Optional.of(
                        new MangroveRootPlacer(
                                UniformInt.of(1, 3),
                                BlockStateProvider.simple(ModBlocks.CRIMSONVEIL_LOG),
                                Optional.of(new AboveRootPlacement(BlockStateProvider.simple(ModBlocks.CRIMSONVEIL_LOG), 0.25F)),
                                new MangroveRootPlacement(
                                        registryEntryLookup.getOrThrow(BlockTags.MANGROVE_ROOTS_CAN_GROW_THROUGH),
                                        HolderSet.direct(Block::builtInRegistryHolder, ModBlocks.NIGHTDARK_GRASS_BLOCK, ModBlocks.NIGHTDARK_DIRT, ModBlocks.CRIMSONVEIL_LOG),
                                        BlockStateProvider.simple(ModBlocks.CRIMSONVEIL_LOG), 5, 12, 0.2F))),
                new TwoLayersFeatureSize(2, 0, 2))
                .decorators(
                        List.of(
                                new AttachedToLeavesDecorator(
                                        0.3F,
                                        1,
                                        0,
                                        BlockStateProvider.simple(ModBlocks.CRIMSONVEIL_VINE),
                                        2,
                                        List.of(Direction.DOWN)
                                )
                        )
                ).ignoreVines().build());
        register(context, TALL_CRIMSONVEIL_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.CRIMSONVEIL_LOG),
                new UpwardsBranchingTrunkPlacer(8, 6, 4, UniformInt.of(2, 4), 0.75F, UniformInt.of(1, 3), registryEntryLookup.getOrThrow(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH)),
                BlockStateProvider.simple(ModBlocks.CRIMSONVEIL_LEAVES),
                new HangingBlobFoliagePlacer(UniformInt.of(2, 3), ConstantInt.of(0), 2, 0.5f, 0.25f),
                Optional.of(
                        new MangroveRootPlacer(
                                UniformInt.of(1, 4),
                                BlockStateProvider.simple(ModBlocks.CRIMSONVEIL_LOG),
                                Optional.of(new AboveRootPlacement(BlockStateProvider.simple(ModBlocks.CRIMSONVEIL_LOG), 0F)),
                                new MangroveRootPlacement(
                                        registryEntryLookup.getOrThrow(BlockTags.MANGROVE_ROOTS_CAN_GROW_THROUGH),
                                        HolderSet.direct(Block::builtInRegistryHolder, ModBlocks.NIGHTDARK_GRASS_BLOCK, ModBlocks.CRIMSONVEIL_LOG),
                                        BlockStateProvider.simple(ModBlocks.CRIMSONVEIL_LOG), 6, 13, 0.2F))),
                new TwoLayersFeatureSize(1, 0, 2))
                .decorators(
                        List.of(
                                new VinesToLeavesTreeDecorator(
                                        0.125F,
                                        BlockStateProvider.simple(ModBlocks.CRIMSONVEIL_VINE_PLANT),
                                        UniformInt.of(1, 3)
                                )
                        )
                ).build());
        register(context, TEALIVE_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.TEALIVE_LOG),
                new CherryTrunkPlacer(7, 1, 0,
                        new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder().add(ConstantInt.of(1), 1).add(ConstantInt.of(2), 1).add(ConstantInt.of(3), 1).build()),
                        UniformInt.of(2, 4),
                        UniformInt.of(-4, -3),
                        UniformInt.of(-1, 0)
                ),
                BlockStateProvider.simple(ModBlocks.TEALIVE_LEAVES),
                new HangingBlobFoliagePlacer(ConstantInt.of(2), UniformInt.of(0, 1), 2, 0.35f, 0.5f),
                new TwoLayersFeatureSize(1, 0, 2)
        ).ignoreVines().build());
        register(context, GRIZZLY_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.GRIZZLY_LOG),
                new GrizzlyTrunkPlacer(8, 2, 1, 0.4F, UniformInt.of(2, 3), UniformInt.of(2, 4), 0.15F),
                BlockStateProvider.simple(ModBlocks.GRIZZLY_LEAVES),
                new GrizzlyFoliagePlacer(UniformInt.of(2, 3), ConstantInt.of(0), 2, 0.3F),
                new TwoLayersFeatureSize(1, 0, 2)
        ).dirt(BlockStateProvider.simple(ModBlocks.GRIMWOOD_GRASS_BLOCK)).build());
        register(context, TALL_GRIZZLY_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.GRIZZLY_LOG),
                new GrizzlyTrunkPlacer(12, 3, 2, 0.45F, UniformInt.of(2, 3), UniformInt.of(3, 4), 0.2F),
                BlockStateProvider.simple(ModBlocks.GRIZZLY_LEAVES),
                new GrizzlyFoliagePlacer(UniformInt.of(2, 4), UniformInt.of(0, 1), 2, 0.2F),
                new TwoLayersFeatureSize(1, 1, 2)
        ).dirt(BlockStateProvider.simple(ModBlocks.GRIMWOOD_GRASS_BLOCK)).build());
        register(context, BURLYWOOD_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.BURLYWOOD_LOG),
                new BurlywoodTrunkPlacer(12, 2, 2, 0.45F,
                        new BurlywoodTrunkPlacer.BranchSettings(UniformInt.of(4, 6), UniformInt.of(3, 5), 0.5F), 0.6F,
                        Optional.of(new BurlywoodTrunkPlacer.BridgeSettings(0.6F, 12,
                                HolderSet.direct(Block::builtInRegistryHolder, ModBlocks.BURLYWOOD_LOG)))),
                BlockStateProvider.simple(ModBlocks.BURLYWOOD_LEAVES),
                new BurlywoodFoliagePlacer(UniformInt.of(3, 4), ConstantInt.of(0), 0.45F),
                new ThreeLayersFeatureSize(1, 1, 1, 1, 2, OptionalInt.empty())
        ).decorators(
                List.of(
                        new VinesToLeavesTreeDecorator(
                                0.1F,
                                BlockStateProvider.simple(ModBlocks.HANGING_BURLYWOOD_LEAVES),
                                UniformInt.of(1, 4)
                        )
                )
        ).ignoreVines().dirt(BlockStateProvider.simple(Blocks.DIRT)).forceDirt().build());
        register(context, SMALL_BURLYWOOD_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.BURLYWOOD_LOG),
                new BurlywoodTrunkPlacer(8, 1, 2, 0.5F,
                        new BurlywoodTrunkPlacer.BranchSettings(UniformInt.of(3, 4), UniformInt.of(2, 3), 0.3F), 0.4F, Optional.empty()),
                BlockStateProvider.simple(ModBlocks.BURLYWOOD_LEAVES),
                new BurlywoodFoliagePlacer(UniformInt.of(2, 3), ConstantInt.of(0), 0.35F),
                new ThreeLayersFeatureSize(1, 1, 1, 1, 2, OptionalInt.empty())
        ).decorators(
                List.of(
                        new VinesToLeavesTreeDecorator(
                                0.05F,
                                BlockStateProvider.simple(ModBlocks.HANGING_BURLYWOOD_LEAVES),
                                UniformInt.of(1, 2)
                        )
                )
        ).ignoreVines().dirt(BlockStateProvider.simple(Blocks.DIRT)).build());
        register(context, BUSH_BURLYWOOD_TREE, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.BURLYWOOD_LOG),
                new StraightTrunkPlacer(1, 0, 0),
                BlockStateProvider.simple(ModBlocks.BURLYWOOD_LEAVES),
                new AcaciaFoliagePlacer(UniformInt.of(1, 2), UniformInt.of(0, 1)),
                new TwoLayersFeatureSize(1, 0, 1)
        ).build());

        RuleTest glaciemiteReplacables = new BlockMatchTest(ModBlocks.GLACIEMITE);
        RuleTest darkCurrantslateReplacables = new BlockMatchTest(ModBlocks.DARK_CURRANTSLATE);
        RuleTest coronaStoneReplacables = new BlockMatchTest(ModBlocks.CORONA_STONE);
        List<OreConfiguration.TargetBlockState> unseeniumOres =
                List.of(OreConfiguration.target(glaciemiteReplacables, ModBlocks.UNSEENIUM_ORE_IN_GLACIEMITE.defaultBlockState()),
                        OreConfiguration.target(darkCurrantslateReplacables, ModBlocks.UNSEENIUM_ORE_IN_DARK_CURRANTSLATE.defaultBlockState()),
                        OreConfiguration.target(coronaStoneReplacables, ModBlocks.UNSEENIUM_ORE_IN_CORONA_STONE.defaultBlockState()));
        List<OreConfiguration.TargetBlockState> titaniumOres =
                List.of(OreConfiguration.target(glaciemiteReplacables, ModBlocks.RED_TITANIUM_ORE_IN_GLACIEMITE.defaultBlockState()),
                        OreConfiguration.target(darkCurrantslateReplacables, ModBlocks.RED_TITANIUM_ORE_IN_DARK_CURRANTSLATE.defaultBlockState()),
                        OreConfiguration.target(coronaStoneReplacables, ModBlocks.RED_TITANIUM_ORE_IN_CORONA_STONE.defaultBlockState()));
        List<OreConfiguration.TargetBlockState> abyssalOres =
                List.of(OreConfiguration.target(glaciemiteReplacables, ModBlocks.ABYSSAL_ORE_IN_GLACIEMITE.defaultBlockState()),
                        OreConfiguration.target(darkCurrantslateReplacables, ModBlocks.ABYSSAL_ORE_IN_DARK_CURRANTSLATE.defaultBlockState()),
                        OreConfiguration.target(coronaStoneReplacables, ModBlocks.ABYSSAL_ORE_IN_CORONA_STONE.defaultBlockState()));
        List<OreConfiguration.TargetBlockState> ironOres =
                List.of(OreConfiguration.target(glaciemiteReplacables, ModBlocks.IRON_ORE_IN_GLACIEMITE.defaultBlockState()),
                        OreConfiguration.target(darkCurrantslateReplacables, ModBlocks.IRON_ORE_IN_DARK_CURRANTSLATE.defaultBlockState()),
                        OreConfiguration.target(coronaStoneReplacables, ModBlocks.IRON_ORE_IN_CORONA_STONE.defaultBlockState()));
        List<OreConfiguration.TargetBlockState> ancientDebrisOres =
                List.of(OreConfiguration.target(glaciemiteReplacables, ModBlocks.ANCIENT_DEBRIS_IN_GLACIEMITE.defaultBlockState()),
                        OreConfiguration.target(darkCurrantslateReplacables, ModBlocks.ANCIENT_DEBRIS_IN_DARK_CURRANTSLATE.defaultBlockState()),
                        OreConfiguration.target(coronaStoneReplacables, ModBlocks.ANCIENT_DEBRIS_IN_CORONA_STONE.defaultBlockState()));
        register(context, UNSEENIUM_ORE, Feature.ORE, new OreConfiguration(unseeniumOres, 4, 0.25f));
        register(context, RED_TITANIUM_ORE, Feature.ORE, new OreConfiguration(titaniumOres, 2, 0.1f));
        register(context, ABYSSAL_ORE, Feature.ORE, new OreConfiguration(abyssalOres, 4, 0.25f));
        register(context, IRON_ORE, Feature.ORE, new OreConfiguration(ironOres, 6, 0.5f));
        register(context, ANCIENT_DEBRIS, Feature.ORE, new OreConfiguration(ancientDebrisOres, 1));

        List<OreConfiguration.TargetBlockState> amethystSpheres =
                List.of(OreConfiguration.target(darkCurrantslateReplacables, ModBlocks.TANZANITE_BLOCK.defaultBlockState()));
        register(context, TANZANITE_SPHERE, Feature.ORE, new OreConfiguration(amethystSpheres, 32));

        register(context, GLACIEMITE_BOULDER, BoulderFeature.INSTANCE, new BoulderFeatureConfig(UniformInt.of(4, 9), BlockStateProvider.simple(ModBlocks.GLACIEMITE)));
        register(context, DARK_CURRANTSLATE_BOULDER, BoulderFeature.INSTANCE, new BoulderFeatureConfig(UniformInt.of(3, 8), BlockStateProvider.simple(ModBlocks.DARK_CURRANTSLATE)));
        register(context, AMETHYST_BOULDER, BoulderFeature.INSTANCE, new BoulderFeatureConfig(UniformInt.of(4, 7), BlockStateProvider.simple(Blocks.AMETHYST_BLOCK)));
        register(context, AMETHYST_CEILING_BOULDER, CeilingBoulderFeature.INSTANCE, new BoulderFeatureConfig(UniformInt.of(4, 9), BlockStateProvider.simple(Blocks.AMETHYST_BLOCK)));
        register(context, DARK_CURRANTSLATE_CEILING_BOULDER, CeilingBoulderFeature.INSTANCE, new BoulderFeatureConfig(UniformInt.of(4, 8), BlockStateProvider.simple(ModBlocks.DARK_CURRANTSLATE)));

        register(context, UNSEENIUM_CURRANTSLATE_DISK, DiskOreFeature.INSTANCE, new DiskOreFeatureConfig(
                RuleBasedBlockStateProvider.simple(ModBlocks.DARK_CURRANTSLATE), RuleBasedBlockStateProvider.simple(ModBlocks.UNSEENIUM_ORE_IN_DARK_CURRANTSLATE),
                BlockPredicate.matchesBlocks(List.of(ModBlocks.TANZANITE_BLOCK, Blocks.CALCITE, ModBlocks.CORONA_STONE)), UniformInt.of(2, 5), 2));
        register(context, TITANIUM_CURRANTSLATE_DISK, DiskOreFeature.INSTANCE, new DiskOreFeatureConfig(
                RuleBasedBlockStateProvider.simple(ModBlocks.GLACIEMITE), RuleBasedBlockStateProvider.simple(ModBlocks.RED_TITANIUM_ORE_IN_GLACIEMITE),
                BlockPredicate.matchesBlocks(List.of(ModBlocks.TANZANITE_BLOCK, Blocks.CALCITE, ModBlocks.CORONA_STONE)), UniformInt.of(2, 4), 2));

        register(context, DARKNESS_SPIRAL_SPIKE, SpiralSpikeFeature.INSTANCE, new SpiralSpikeFeatureConfig(BlockStateProvider.simple(ModBlocks.GLACIEMITE), BlockStateProvider.simple(ModBlocks.GRIPPING_GLACIEMITE), BlockStateProvider.simple(ModBlocks.GRIPCRYSTAL_WART), UniformInt.of(4, 12)));
        register(context, GRIPTONITE_CLUSTER_SPIKE, ClusterSpikeFeature.INSTANCE, new ClusterSpikeFeatureConfig(BlockStateProvider.simple(ModBlocks.DARK_CURRANTSLATE), BlockStateProvider.simple(ModBlocks.GRIPTONITE_CLUSTER), BlockStateProvider.simple(ModBlocks.GRIPTONITED_DARK_CURRANTSLATE), UniformInt.of(11, 15), UniformInt.of(13, 27)));

        SimpleWeightedRandomList.Builder<BlockState> amethystSpike = SimpleWeightedRandomList.builder();
        amethystSpike.add(Blocks.MEDIUM_AMETHYST_BUD.defaultBlockState(),3);
        amethystSpike.add(Blocks.LARGE_AMETHYST_BUD.defaultBlockState(),1);
        register(context, AMETHYST_CLUSTER_SPIKE, ClusterSpikeFeature.INSTANCE, new ClusterSpikeFeatureConfig(BlockStateProvider.simple(Blocks.AMETHYST_BLOCK), new WeightedStateProvider(amethystSpike), BlockStateProvider.simple(Blocks.AMETHYST_BLOCK), UniformInt.of(7, 11), UniformInt.of(7, 17)));

        register(context, BEARFRUIT_BRAMBLE_PATCH, StandingFlowerBunchFeature.INSTANCE, new StandingFlowerBunchFeatureConfig(UniformInt.of(3, 7), BlockStateProvider.simple(ModBlocks.BEARFRUIT_BRAMBLE)));
        register(context, MIDNIGHT_LILY_PATCH, MidnightLilyPadFeature.INSTANCE, NoneFeatureConfiguration.INSTANCE);
        register(context, BUSH_LIKE_TREE_PATCH, TreeBushLikeFeature.INSTANCE, new TreeBushLikeConfig(UniformInt.of(0, 2), UniformInt.of(1, 5)));
        register(context, SMALL_BUSH_LIKE_TREE_PATCH, TreeBushLikeFeature.INSTANCE, new TreeBushLikeConfig(UniformInt.of(0, 1), UniformInt.of(1, 4)));
        register(context, BURLYWOOD_VIOLET_PATCH, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.BURLYWOOD_VIOLET))));
        register(context, GRIPCRYSTAL_WART_PATCH, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        32,
                        5,
                        2,
                        PlacementUtils.onlyWhenEmpty(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(
                                        new NoiseProvider(
                                                12345L,
                                                new NormalNoise.NoiseParameters(0, 1.0),
                                                0.020833334F,
                                                List.of(
                                                        ModBlocks.GRIPCRYSTAL_WART.defaultBlockState(),
                                                        ModBlocks.GRIPPING_SPIGELIA.defaultBlockState().trySetValue(BlockStateProperties.AGE_2, 1)
                                                )
                                        )
                                )
                        )
                ));

        register(context, UMBRAL_KELP, UmbralKelpFeature.INSTANCE, new NoneFeatureConfiguration());
        SimpleWeightedRandomList.Builder<BlockState> petals = SimpleWeightedRandomList.builder();

        for (int i = 1; i <= 4; i++) {
            for (Direction direction : Direction.Plane.HORIZONTAL)
                petals.add(ModBlocks.AMETHYST_PETALS.defaultBlockState().setValue(PinkPetalsBlock.AMOUNT, i).setValue(PinkPetalsBlock.FACING, direction), 1);
        }
        register(
                context,
                AMETHYST_FLOWERS,
                Feature.FLOWER,
                new RandomPatchConfiguration(
                        32, 3, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new WeightedStateProvider(petals)))
                )
        );
        register(context, RARE_GLOOMWEED_PATCH, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        32,
                        4,
                        2,
                        PlacementUtils.onlyWhenEmpty(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(
                                        new NoiseProvider(
                                                54321L,
                                                new NormalNoise.NoiseParameters(0, 1.0),
                                                0.020833334F,
                                                List.of(
                                                        ModBlocks.GLOOMWEED.defaultBlockState().setValue(DarknessFernBlock.LIT, true),
                                                        ModBlocks.MURKTUFT.defaultBlockState().setValue(DarknessFernBlock.LIT, true)
                                                )
                                        )
                                )
                        )
                ));
        register(context, GLOOMWEED_PATCH, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        96,
                        5,
                        2,
                        PlacementUtils.onlyWhenEmpty(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(
                                        new NoiseProvider(
                                                2345L,
                                                new NormalNoise.NoiseParameters(0, 1.0),
                                                0.020833334F,
                                                List.of(
                                                        ModBlocks.GLOOMWEED.defaultBlockState(),
                                                        ModBlocks.MURKTUFT.defaultBlockState().setValue(DarknessFernBlock.LIT, true)
                                                )
                                        )
                                )
                        )
                ));
        register(context, TALL_GLOOMWEED_PATCH, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        48,
                        5,
                        2,
                        PlacementUtils.onlyWhenEmpty(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(
                                        new NoiseProvider(
                                                2345L,
                                                new NormalNoise.NoiseParameters(0, 1.0),
                                                0.020833334F,
                                                List.of(
                                                        ModBlocks.TALL_GLOOMWEED.defaultBlockState()
                                                )
                                        )
                                )
                        )
                )
        );
        register(
                context,
                GRIPPING_GLACIEMITE_PATCH_CEILING,
                GrippingVegetationFeature.INSTANCE,
                new GrippingVegetationFeatureConfig(
                        CaveSurface.CEILING,
                        SimpleStateProvider.simple(ModBlocks.GRIPPING_GLACIEMITE.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.DOWN)),
                        0.2f,
                        4,
                        0.4f,
                        UniformInt.of(4, 7),
                        0.1f
                )
        );
        register(
                context,
                GRIPPING_DARK_CURRANTSLATE_PATCH_FLOOR,
                GrippingVegetationFeature.INSTANCE,
                new GrippingVegetationFeatureConfig(
                        CaveSurface.FLOOR,
                        SimpleStateProvider.simple(ModBlocks.GRIPPING_DARK_CURRANTSLATE.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.UP)),
                        0.25f,
                        3,
                        0.35f,
                        UniformInt.of(3, 7),
                        0.25f
                )
        );

        register(context, CORONA_STONE_BOULDER, BoulderFeature.INSTANCE, new BoulderFeatureConfig(UniformInt.of(3, 6), BlockStateProvider.simple(ModBlocks.CORONA_STONE)));
        register(context, GILDED_CURRANTSLATE_BOULDER, BoulderFeature.INSTANCE, new BoulderFeatureConfig(UniformInt.of(3, 7),
                new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                        .add(ModBlocks.GILDED_CURRANTSLATE.defaultBlockState(), 3)
                        .add(ModBlocks.DARK_CURRANTSLATE.defaultBlockState(), 1))));

        register(context, CORONA_STONE_RING, StoneRingFeature.INSTANCE, new StoneRingFeatureConfig(
                new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                        .add(ModBlocks.CORONA_STONE_BRICKS.defaultBlockState(), 4)
                        .add(ModBlocks.CRACKED_CORONA_STONE_BRICKS.defaultBlockState(), 2)
                        .add(ModBlocks.CORONA_STONE.defaultBlockState(), 1)),
                BlockStateProvider.simple(ModBlocks.CORONA_STONE_BRICKS_SLAB),
                BlockStateProvider.simple(ModBlocks.CORONA_BLOOM),
                UniformInt.of(4, 7),
                UniformInt.of(7, 11),
                UniformInt.of(2, 5),
                0.3F));

        register(context, CORONA_BLOOM_PATCH, Feature.FLOWER, new RandomPatchConfiguration(
                48, 6, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.CORONA_BLOOM)))));
        register(context, FROSTBLOOM_PATCH, Feature.FLOWER, new RandomPatchConfiguration(
                32, 5, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.FROSTBLOOM)))));

        register(context, ASHREED_PATCH, Feature.RANDOM_PATCH, new RandomPatchConfiguration(
                20, 4, 0, PlacementUtils.filtered(
                Feature.BLOCK_COLUMN,
                BlockColumnConfiguration.simple(BiasedToBottomInt.of(2, 4), BlockStateProvider.simple(ModBlocks.ASHREED)),
                BlockPredicate.allOf(
                        BlockPredicate.ONLY_IN_AIR_PREDICATE,
                        BlockPredicate.wouldSurvive(ModBlocks.ASHREED.defaultBlockState(), BlockPos.ZERO)))));

        BlockState glowrootBody = ModBlocks.GLOWROOT_VINE_PLANT.defaultBlockState();
        BlockState glowrootHead = ModBlocks.GLOWROOT_VINE.defaultBlockState();
        register(context, GLOWROOT_VINES, Feature.BLOCK_COLUMN, new BlockColumnConfiguration(
                List.of(
                        BlockColumnConfiguration.layer(
                                new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder()
                                        .add(UniformInt.of(0, 14), 2)
                                        .add(UniformInt.of(0, 2), 3)
                                        .add(UniformInt.of(0, 6), 10)
                                        .build()),
                                new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                                        .add(glowrootBody, 4)
                                        .add(glowrootBody.trySetValue(BlockStateProperties.BERRIES, true), 1))),
                        BlockColumnConfiguration.layer(
                                ConstantInt.of(1),
                                new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                                        .add(glowrootHead, 4)
                                        .add(glowrootHead.trySetValue(BlockStateProperties.BERRIES, true), 1)))),
                Direction.DOWN,
                BlockPredicate.ONLY_IN_AIR_PREDICATE,
                true));

        register(context, UMBRAL_CORAL_REEF, CoralReefFeature.INSTANCE, new CoralReefFeatureConfig(
                ModBlocks.UMBRAL_CORAL_BLOCK.defaultBlockState(),
                ModBlocks.UMBRAL_CORAL.defaultBlockState(),
                ModBlocks.UMBRAL_CORAL_FAN.defaultBlockState(),
                ModBlocks.UMBRAL_CORAL_WALL_FAN.defaultBlockState(),
                UniformInt.of(2, 5),
                UniformInt.of(1, 3),
                0.2F));
        register(context, DEAD_UMBRAL_CORAL_REEF, CoralReefFeature.INSTANCE, new CoralReefFeatureConfig(
                ModBlocks.DEAD_UMBRAL_CORAL_BLOCK.defaultBlockState(),
                ModBlocks.DEAD_UMBRAL_CORAL.defaultBlockState(),
                ModBlocks.DEAD_UMBRAL_CORAL_FAN.defaultBlockState(),
                ModBlocks.DEAD_UMBRAL_CORAL_WALL_FAN.defaultBlockState(),
                UniformInt.of(1, 3),
                UniformInt.of(0, 2),
                0.1F));

        register(context, CHIMERIC_FREEZE, ChimericFreezeFeature.INSTANCE, NoneFeatureConfiguration.INSTANCE);

        registerCaveContent(context);
    }

    private static void registerCaveContent(BootstapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configured = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, HOLLOWS_FLOOR_VEGETATION, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
                new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                        .add(Blocks.GRASS.defaultBlockState(), 50)
                        .add(Blocks.MOSS_CARPET.defaultBlockState(), 25)
                        .add(ModBlocks.BURLYWOOD_VIOLET.defaultBlockState(), 8)
                        .add(Blocks.AZALEA.defaultBlockState(), 4)
                        .add(Blocks.FLOWERING_AZALEA.defaultBlockState(), 4))));
        register(context, HOLLOWS_FLOOR_MOSS, Feature.VEGETATION_PATCH, new VegetationPatchConfiguration(
                ModTags.Blocks.CHIMERIC_DARKNESS_STONES,
                BlockStateProvider.simple(Blocks.MOSS_BLOCK),
                PlacementUtils.inlinePlaced(configured.getOrThrow(HOLLOWS_FLOOR_VEGETATION)),
                CaveSurface.FLOOR,
                ConstantInt.of(1),
                0.0F,
                5,
                0.8F,
                UniformInt.of(4, 7),
                0.3F));
        register(context, HOLLOWS_CEILING_MOSS, Feature.VEGETATION_PATCH, new VegetationPatchConfiguration(
                ModTags.Blocks.CHIMERIC_DARKNESS_STONES,
                BlockStateProvider.simple(Blocks.MOSS_BLOCK),
                PlacementUtils.inlinePlaced(configured.getOrThrow(GLOWROOT_VINES)),
                CaveSurface.CEILING,
                UniformInt.of(1, 2),
                0.0F,
                5,
                0.08F,
                UniformInt.of(4, 7),
                0.3F));
        register(context, HOLLOWS_GLOW_LICHEN, Feature.MULTIFACE_GROWTH, new MultifaceGrowthConfiguration(
                (MultifaceBlock) Blocks.GLOW_LICHEN,
                20,
                false,
                true,
                true,
                0.5F,
                HolderSet.direct(Block::builtInRegistryHolder, ModBlocks.DARK_CURRANTSLATE, Blocks.MOSS_BLOCK, Blocks.ROOTED_DIRT)));

        register(context, SHINY_ICE_CEILING_BOULDER, CeilingBoulderFeature.INSTANCE, new BoulderFeatureConfig(UniformInt.of(3, 6),
                new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                        .add(Blocks.PACKED_ICE.defaultBlockState(), 3)
                        .add(ModBlocks.RIME_ICE.defaultBlockState(), 2)
                        .add(Blocks.BLUE_ICE.defaultBlockState(), 1))));
        register(context, FROZEN_CAVE_POOL, Feature.DISK, new DiskConfiguration(
                RuleBasedBlockStateProvider.simple(ModBlocks.RIME_ICE),
                BlockPredicate.allOf(
                        BlockPredicate.matchesBlocks(ModBlocks.DARK_WATER),
                        BlockPredicate.matchesBlocks(new BlockPos(0, 1, 0), Blocks.AIR, Blocks.CAVE_AIR)),
                UniformInt.of(2, 4),
                0));
        register(context, TAPROOT_TREE, TaprootTreeFeature.INSTANCE, new TaprootTreeFeatureConfig(
                BlockStateProvider.simple(ModBlocks.BURLYWOOD_LOG),
                BlockStateProvider.simple(ModBlocks.BURLYWOOD_LEAVES),
                BlockStateProvider.simple(ModBlocks.HANGING_BURLYWOOD_LEAVES),
                BlockStateProvider.simple(Blocks.HANGING_ROOTS),
                BlockStateProvider.simple(Blocks.ROOTED_DIRT),
                ModBlocks.BURLYWOOD_HEART.defaultBlockState().trySetValue(BurlywoodHeartBlock.PERSISTENT, false),
                ModTags.Blocks.CHIMERIC_DARKNESS_STONES,
                UniformInt.of(4, 7),
                UniformInt.of(2, 3),
                0.1F));
        register(context, HOARFROST_PATCH, SurfaceCoatingFeature.INSTANCE, new SurfaceCoatingFeatureConfig(
                BlockStateProvider.simple(ModBlocks.HOARFROST),
                UniformInt.of(3, 6),
                0.85F));

        register(context, HOLLOWS_GLEAMCARVER_NEST, NestFeature.INSTANCE, new NestFeatureConfig(
                new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                        .add(Blocks.MOSS_BLOCK.defaultBlockState(), 3)
                        .add(Blocks.ROOTED_DIRT.defaultBlockState(), 1)),
                BlockStateProvider.simple(Blocks.MOSS_BLOCK),
                new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                        .add(ModBlocks.BURLYWOOD_VIOLET.defaultBlockState(), 2)
                        .add(Blocks.MOSS_CARPET.defaultBlockState(), 3)),
                BlockStateProvider.simple(ModBlocks.BURLYWOOD_VIOLET),
                UniformInt.of(2, 3),
                0.5F,
                ModEntities.GLEAMCARVER,
                UniformInt.of(1, 3)));
        register(context, TANZANITE_GLEAMCARVER_NEST, NestFeature.INSTANCE, new NestFeatureConfig(
                new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                        .add(ModBlocks.CORONA_STONE.defaultBlockState(), 3)
                        .add(Blocks.CALCITE.defaultBlockState(), 1)),
                BlockStateProvider.simple(Blocks.CALCITE),
                new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                        .add(Blocks.SMALL_AMETHYST_BUD.defaultBlockState(), 3)
                        .add(Blocks.MEDIUM_AMETHYST_BUD.defaultBlockState(), 1)),
                BlockStateProvider.simple(Blocks.AMETHYST_CLUSTER),
                UniformInt.of(2, 3),
                0.4F,
                ModEntities.GLEAMCARVER,
                UniformInt.of(1, 3)));
        register(context, SHINY_GLEAMCARVER_NEST, NestFeature.INSTANCE, new NestFeatureConfig(
                new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                        .add(ModBlocks.CORONA_STONE.defaultBlockState(), 2)
                        .add(Blocks.PACKED_ICE.defaultBlockState(), 2)
                        .add(ModBlocks.GLACIEMITE.defaultBlockState(), 1)),
                BlockStateProvider.simple(Blocks.SNOW_BLOCK),
                BlockStateProvider.simple(Blocks.SNOW),
                BlockStateProvider.simple(ModBlocks.FROSTBLOOM),
                UniformInt.of(2, 3),
                0.5F,
                ModEntities.GLEAMCARVER,
                UniformInt.of(1, 3)));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> create(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, UnseenWorld.MOD.makeID(name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
        context.register(key, new ConfiguredFeature<>(feature, config));
    }
}