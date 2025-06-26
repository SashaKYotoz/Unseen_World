package net.sashakyotoz.common.world.features;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerbedBlock;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.property.Properties;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.VerticalSurfaceType;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.intprovider.WeightedListIntProvider;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.ThreeLayersFeatureSize;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BushFoliagePlacer;
import net.minecraft.world.gen.foliage.RandomSpreadFoliagePlacer;
import net.minecraft.world.gen.root.AboveRootPlacement;
import net.minecraft.world.gen.root.MangroveRootPlacement;
import net.minecraft.world.gen.root.MangroveRootPlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.NoiseBlockStateProvider;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.treedecorator.AttachedToLeavesTreeDecorator;
import net.minecraft.world.gen.trunk.CherryTrunkPlacer;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;
import net.minecraft.world.gen.trunk.UpwardsBranchingTrunkPlacer;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.custom.plants.HangingFruitBlock;
import net.sashakyotoz.common.world.features.custom.*;
import net.sashakyotoz.common.world.features.custom.configs.*;
import net.sashakyotoz.common.world.features.trees.placers.*;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class ModConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> AMETHYST_TREE = create("amethyst_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SMALL_AMETHYST_TREE = create("small_amethyst_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> AMETHYST_BUSH = create("amethyst_bush");
    public static final RegistryKey<ConfiguredFeature<?, ?>> CRIMSONVEIL_TREE = create("crimsonveil_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TALL_CRIMSONVEIL_TREE = create("tall_crimsonveil_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TEALIVE_TREE = create("tealive_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> GRIZZLY_TREE = create("grizzly_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TALL_GRIZZLY_TREE = create("tall_grizzly_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BURLYWOOD_TREE = create("burlywood_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SMALL_BURLYWOOD_TREE = create("small_burlywood_tree");

    public static final RegistryKey<ConfiguredFeature<?, ?>> UNSEENIUM_ORE = create("unseenium_ore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> RED_TITANIUM_ORE = create("red_titanium_ore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> ABYSSAL_ORE = create("abyssal_ore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> IRON_ORE = create("iron_ore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> ANCIENT_DEBRIS = create("ancient_debris");

    public static final RegistryKey<ConfiguredFeature<?, ?>> TANZANITE_SPHERE = create("tanzanite_sphere");

    public static final RegistryKey<ConfiguredFeature<?, ?>> AMETHYST_FLOWERS = create("amethyst_flowers");

    public static final RegistryKey<ConfiguredFeature<?, ?>> GLACIEMITE_BOULDER = create("glaciemite_boulder");
    public static final RegistryKey<ConfiguredFeature<?, ?>> DARK_CURRANTSLATE_BOULDER = create("dark_currantslate_boulder");
    public static final RegistryKey<ConfiguredFeature<?, ?>> AMETHYST_BOULDER = create("amethyst_boulder");

    public static final RegistryKey<ConfiguredFeature<?, ?>> DARKNESS_SPIRAL_SPIKE = create("darkness_spiral_spike");

    public static final RegistryKey<ConfiguredFeature<?, ?>> WATER_LAKE = create("water_lake");
    public static final RegistryKey<ConfiguredFeature<?, ?>> DARK_WATER_LAKE = create("dark_water_lake");
    public static final RegistryKey<ConfiguredFeature<?, ?>> CRIMSONVEIL_DARK_WATER_LAKE = create("crimsonveil_dark_water_lake");

    public static final RegistryKey<ConfiguredFeature<?, ?>> BEARFRUIT_BRAMBLE_PATCH = create("bearfruit_bramble_patch");
    public static final RegistryKey<ConfiguredFeature<?, ?>> MIDNIGHT_LILY_PATCH = create("midnight_lily_patch");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BUSH_LIKE_TREE_PATCH = create("bush_like_tree_patch");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SMALL_BUSH_LIKE_TREE_PATCH = create("small_bush_like_tree_patch");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BURLYWOOD_VIOLET_PATCH = create("burlywood_violet_patch");

    public static final RegistryKey<ConfiguredFeature<?, ?>> UMBRAL_KELP = create("umbral_kelp");
    public static final RegistryKey<ConfiguredFeature<?, ?>> GLOOMWEED_PATCH = create("gloomweed_patch");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TALL_GLOOMWEED_PATCH = create("tall_gloomweed_patch");

    public static final RegistryKey<ConfiguredFeature<?, ?>> GRIPPING_GLACIEMITE_PATCH_CEILING = create("gripping_glaciemite_patch_ceiling");
    public static final RegistryKey<ConfiguredFeature<?, ?>> GRIPPING_DARK_CURRANTSLATE_PATCH_FLOOR = create("gripping_dark_currantslate_patch_floor");

    public static final RegistryKey<ConfiguredFeature<?, ?>> AMETHYST_CEILING_BOULDER = create("amethyst_ceiling_boulder");
    public static final RegistryKey<ConfiguredFeature<?, ?>> DARK_CURRANTSLATE_CEILING_BOULDER = create("dark_currantslate_ceiling_boulder");


    public static void boostrap(Registerable<ConfiguredFeature<?, ?>> context) {
        RegistryEntryLookup<Block> registryEntryLookup = context.getRegistryLookup(RegistryKeys.BLOCK);
        register(context, AMETHYST_TREE, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.AMETHYST_LOG),
                new GrizzlyTrunkPlacer(6, 2, 2, 2),
                BlockStateProvider.of(ModBlocks.AMETHYST_LEAVES),
                new HangingBlobFoliagePlacer(
                        UniformIntProvider.create(2, 3),
                        ConstantIntProvider.create(1),
                        2,
                        0.5F,
                        1F),
                new TwoLayersFeatureSize(1, 2, 1))
                .decorators(List.of(
                        new AttachedToLeavesTreeDecorator(
                                0.2f,
                                1,
                                0,
                                new WeightedBlockStateProvider(DataPool.of(ModBlocks.HANGING_AMETHYST_LEAVES.getDefaultState().with(HangingFruitBlock.HAS_FRUIT, Random.create().nextBoolean()))),
                                1,
                                List.of(Direction.DOWN))))
                .dirtProvider(BlockStateProvider.of(ModBlocks.AMETHYST_GRASS_BLOCK)).forceDirt().build());
        register(context, SMALL_AMETHYST_TREE, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.AMETHYST_LOG),
                new GrizzlyTrunkPlacer(4, 2, 2, 1),
                BlockStateProvider.of(ModBlocks.AMETHYST_LEAVES),
                new HangingBlobFoliagePlacer(
                        UniformIntProvider.create(2, 3),
                        UniformIntProvider.create(1, 2),
                        2,
                        0.5F,
                        0.5F),
                new TwoLayersFeatureSize(1, 1, 1))
                .dirtProvider(BlockStateProvider.of(ModBlocks.AMETHYST_GRASS_BLOCK)).forceDirt().build());
        register(context, AMETHYST_BUSH, Feature.TREE,
                new TreeFeatureConfig.Builder(
                        BlockStateProvider.of(ModBlocks.AMETHYST_LOG),
                        new StraightTrunkPlacer(1, 0, 0),
                        BlockStateProvider.of(ModBlocks.AMETHYST_LEAVES),
                        new BushFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(1), 2),
                        new TwoLayersFeatureSize(0, 0, 0)
                ).build());
        register(context, CRIMSONVEIL_TREE, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.CRIMSONVEIL_LOG),
                new UpwardsBranchingTrunkPlacer(6, 4, 4, UniformIntProvider.create(2, 4), 0.5F, UniformIntProvider.create(1, 3), registryEntryLookup.getOrThrow(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH)),
                BlockStateProvider.of(ModBlocks.CRIMSONVEIL_LEAVES),
                new RandomSpreadFoliagePlacer(ConstantIntProvider.create(3), UniformIntProvider.create(0, 1), ConstantIntProvider.create(1), 70),
                Optional.of(
                        new MangroveRootPlacer(
                                UniformIntProvider.create(1, 3),
                                BlockStateProvider.of(ModBlocks.CRIMSONVEIL_LOG),
                                Optional.of(new AboveRootPlacement(BlockStateProvider.of(ModBlocks.CRIMSONVEIL_LOG), 0.25F)),
                                new MangroveRootPlacement(
                                        registryEntryLookup.getOrThrow(BlockTags.MANGROVE_ROOTS_CAN_GROW_THROUGH),
                                        RegistryEntryList.of(Block::getRegistryEntry, ModBlocks.NIGHTDARK_GRASS_BLOCK, ModBlocks.CRIMSONVEIL_LOG),
                                        BlockStateProvider.of(ModBlocks.CRIMSONVEIL_LOG), 5, 12, 0.2F))),
                new TwoLayersFeatureSize(2, 0, 2))
                .decorators(
                        List.of(
                                new AttachedToLeavesTreeDecorator(
                                        0.3F,
                                        1,
                                        0,
                                        BlockStateProvider.of(ModBlocks.CRIMSONVEIL_VINE),
                                        2,
                                        List.of(Direction.DOWN)
                                )
                        )
                ).ignoreVines().build());
        register(context, TALL_CRIMSONVEIL_TREE, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.CRIMSONVEIL_LOG),
                new UpwardsBranchingTrunkPlacer(7, 5, 4, UniformIntProvider.create(2, 5), 0.75F, UniformIntProvider.create(1, 3), registryEntryLookup.getOrThrow(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH)),
                BlockStateProvider.of(ModBlocks.CRIMSONVEIL_LEAVES),
                new HangingBlobFoliagePlacer(UniformIntProvider.create(2, 3), ConstantIntProvider.create(0), 2, 0.5f, 0.25f),
                Optional.of(
                        new MangroveRootPlacer(
                                UniformIntProvider.create(1, 4),
                                BlockStateProvider.of(ModBlocks.CRIMSONVEIL_LOG),
                                Optional.of(new AboveRootPlacement(BlockStateProvider.of(ModBlocks.CRIMSONVEIL_LOG), 0F)),
                                new MangroveRootPlacement(
                                        registryEntryLookup.getOrThrow(BlockTags.MANGROVE_ROOTS_CAN_GROW_THROUGH),
                                        RegistryEntryList.of(Block::getRegistryEntry, ModBlocks.NIGHTDARK_GRASS_BLOCK, ModBlocks.CRIMSONVEIL_LOG),
                                        BlockStateProvider.of(ModBlocks.CRIMSONVEIL_LOG), 6, 13, 0.2F))),
                new TwoLayersFeatureSize(1, 0, 2))
                .decorators(
                        List.of(
                                new VinesToLeavesTreeDecorator(
                                        0.125F,
                                        BlockStateProvider.of(ModBlocks.CRIMSONVEIL_VINE_PLANT),
                                        UniformIntProvider.create(2, 4)
                                )
                        )
                ).build());
        register(context, TEALIVE_TREE, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.TEALIVE_LOG),
                new CherryTrunkPlacer(7, 1, 0,
                        new WeightedListIntProvider(DataPool.<IntProvider>builder().add(ConstantIntProvider.create(1), 1).add(ConstantIntProvider.create(2), 1).add(ConstantIntProvider.create(3), 1).build()),
                        UniformIntProvider.create(2, 4),
                        UniformIntProvider.create(-4, -3),
                        UniformIntProvider.create(-1, 0)
                ),
                BlockStateProvider.of(ModBlocks.TEALIVE_LEAVES),
                new HangingBlobFoliagePlacer(ConstantIntProvider.create(2), UniformIntProvider.create(0, 1), 2, 0.35f, 0.5f),
                new TwoLayersFeatureSize(1, 0, 2)
        ).ignoreVines().build());
        register(context, GRIZZLY_TREE, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.GRIZZLY_LOG),
                new GrizzlyTrunkPlacer(4, 3, 5, 0),
                BlockStateProvider.of(ModBlocks.GRIZZLY_LEAVES),
                new GrizzlyFoliagePlacer(UniformIntProvider.create(3, 5), ConstantIntProvider.create(0)),
                new TwoLayersFeatureSize(1, 0, 2)
        ).dirtProvider(BlockStateProvider.of(ModBlocks.GRIMWOOD_GRASS_BLOCK)).build());
        register(context, TALL_GRIZZLY_TREE, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.GRIZZLY_LOG),
                new GrizzlyTrunkPlacer(9, 4, 5, 1),
                BlockStateProvider.of(ModBlocks.GRIZZLY_LEAVES),
                new GrizzlyFoliagePlacer(ConstantIntProvider.create(2), UniformIntProvider.create(0, 1)),
                new TwoLayersFeatureSize(1, 1, 2)
        ).dirtProvider(BlockStateProvider.of(ModBlocks.GRIMWOOD_GRASS_BLOCK)).build());
        register(context, BURLYWOOD_TREE, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.BURLYWOOD_LOG),
                new BurlywoodTrunkPlacer(9, 2, 3),
                BlockStateProvider.of(ModBlocks.BURLYWOOD_LEAVES),
                new BurlywoodFoliagePlacer(UniformIntProvider.create(1, 2), UniformIntProvider.create(0, 1)),
                new ThreeLayersFeatureSize(1, 1, 1, 1, 2, OptionalInt.empty())
        ).ignoreVines().dirtProvider(BlockStateProvider.of(Blocks.DIRT)).build());
        register(context, SMALL_BURLYWOOD_TREE, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.BURLYWOOD_LOG),
                new BurlywoodTrunkPlacer(5, 1, 2),
                BlockStateProvider.of(ModBlocks.BURLYWOOD_LEAVES),
                new BurlywoodFoliagePlacer(UniformIntProvider.create(1, 2), UniformIntProvider.create(0, 1)),
                new ThreeLayersFeatureSize(1, 1, 1, 1, 2, OptionalInt.empty())
        ).ignoreVines().dirtProvider(BlockStateProvider.of(Blocks.DIRT)).build());

        RuleTest glaciemiteReplacables = new BlockMatchRuleTest(ModBlocks.GLACIEMITE);
        RuleTest darkCurrantslateReplacables = new BlockMatchRuleTest(ModBlocks.DARK_CURRANTSLATE);
        List<OreFeatureConfig.Target> unseeniumOres =
                List.of(OreFeatureConfig.createTarget(glaciemiteReplacables, ModBlocks.UNSEENIUM_ORE_IN_GLACIEMITE.getDefaultState()),
                        OreFeatureConfig.createTarget(darkCurrantslateReplacables, ModBlocks.UNSEENIUM_ORE_IN_DARK_CURRANTSLATE.getDefaultState()));
        List<OreFeatureConfig.Target> titaniumOres =
                List.of(OreFeatureConfig.createTarget(glaciemiteReplacables, ModBlocks.RED_TITANIUM_IN_GLACIEMITE.getDefaultState()),
                        OreFeatureConfig.createTarget(darkCurrantslateReplacables, ModBlocks.RED_TITANIUM_ORE_IN_DARK_CURRANTSLATE.getDefaultState()));
        List<OreFeatureConfig.Target> abyssalOres =
                List.of(OreFeatureConfig.createTarget(glaciemiteReplacables, ModBlocks.ABYSSAL_ORE_IN_GLACIEMITE.getDefaultState()),
                        OreFeatureConfig.createTarget(darkCurrantslateReplacables, ModBlocks.ABYSSAL_ORE_IN_DARK_CURRANTSLATE.getDefaultState()));
        List<OreFeatureConfig.Target> ironOres =
                List.of(OreFeatureConfig.createTarget(glaciemiteReplacables, ModBlocks.IRON_ORE_IN_GLACIEMITE.getDefaultState()),
                        OreFeatureConfig.createTarget(darkCurrantslateReplacables, ModBlocks.IRON_ORE_IN_DARK_CURRANTSLATE.getDefaultState()));
        List<OreFeatureConfig.Target> ancientDebrisOres =
                List.of(OreFeatureConfig.createTarget(glaciemiteReplacables, ModBlocks.ANCIENT_DEBRIS_IN_GLACIEMITE.getDefaultState()),
                        OreFeatureConfig.createTarget(darkCurrantslateReplacables, ModBlocks.ANCIENT_DEBRIS_IN_DARK_CURRANTSLATE.getDefaultState()));
        register(context, UNSEENIUM_ORE, Feature.ORE, new OreFeatureConfig(unseeniumOres, 4, 0.125f));
        register(context, RED_TITANIUM_ORE, Feature.ORE, new OreFeatureConfig(titaniumOres, 2));
        register(context, ABYSSAL_ORE, Feature.ORE, new OreFeatureConfig(abyssalOres, 3));
        register(context, IRON_ORE, Feature.ORE, new OreFeatureConfig(ironOres, 4, 0.25f));
        register(context, ANCIENT_DEBRIS, Feature.ORE, new OreFeatureConfig(ancientDebrisOres, 1));

        List<OreFeatureConfig.Target> amethystSpheres =
                List.of(OreFeatureConfig.createTarget(darkCurrantslateReplacables, ModBlocks.TANZANITE_BLOCK.getDefaultState()));
        register(context, TANZANITE_SPHERE, Feature.ORE, new OreFeatureConfig(amethystSpheres, 32));

        register(context, GLACIEMITE_BOULDER, BoulderFeature.INSTANCE, new BoulderFeatureConfig(UniformIntProvider.create(4, 9), BlockStateProvider.of(ModBlocks.GLACIEMITE)));
        register(context, DARK_CURRANTSLATE_BOULDER, BoulderFeature.INSTANCE, new BoulderFeatureConfig(UniformIntProvider.create(3, 8), BlockStateProvider.of(ModBlocks.DARK_CURRANTSLATE)));
        register(context, AMETHYST_BOULDER, BoulderFeature.INSTANCE, new BoulderFeatureConfig(UniformIntProvider.create(4, 7), BlockStateProvider.of(Blocks.AMETHYST_BLOCK)));
        register(context, AMETHYST_CEILING_BOULDER, CeilingBoulderFeature.INSTANCE, new BoulderFeatureConfig(UniformIntProvider.create(4, 9), BlockStateProvider.of(Blocks.AMETHYST_BLOCK)));
        register(context, DARK_CURRANTSLATE_CEILING_BOULDER, CeilingBoulderFeature.INSTANCE, new BoulderFeatureConfig(UniformIntProvider.create(4, 8), BlockStateProvider.of(ModBlocks.DARK_CURRANTSLATE)));

        register(context, DARKNESS_SPIRAL_SPIKE, SpiralSpikesFeature.INSTANCE, new SpiralSpikesFeatureConfig(BlockStateProvider.of(ModBlocks.GLACIEMITE), BlockStateProvider.of(Blocks.DEEPSLATE), UniformIntProvider.create(3, 9)));

        register(context, WATER_LAKE, Feature.LAKE, new LakeFeature.Config(BlockStateProvider.of(Blocks.WATER.getDefaultState()), BlockStateProvider.of(Blocks.STONE.getDefaultState())));
        register(context, DARK_WATER_LAKE, Feature.LAKE, new LakeFeature.Config(BlockStateProvider.of(ModBlocks.DARK_WATER.getDefaultState()), BlockStateProvider.of(ModBlocks.DARK_CURRANTSLATE.getDefaultState())));

        register(context, BEARFRUIT_BRAMBLE_PATCH, StandingFlowerBunchFeature.INSTANCE, new StandingFlowerBunchFeatureConfig(UniformIntProvider.create(3, 7), BlockStateProvider.of(ModBlocks.BEARFRUIT_BRAMBLE)));
        register(context, MIDNIGHT_LILY_PATCH, MidnightLilyPadFeature.INSTANCE, DefaultFeatureConfig.INSTANCE);
        register(context, BUSH_LIKE_TREE_PATCH, TreeBushLikeFeature.INSTANCE, new TreeBushLikeConfig(UniformIntProvider.create(0, 2), UniformIntProvider.create(1, 5)));
        register(context, SMALL_BUSH_LIKE_TREE_PATCH, TreeBushLikeFeature.INSTANCE, new TreeBushLikeConfig(UniformIntProvider.create(0, 1), UniformIntProvider.create(1, 4)));
        register(context, BURLYWOOD_VIOLET_PATCH, Feature.RANDOM_PATCH, ConfiguredFeatures.createRandomPatchFeatureConfig(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(BlockStateProvider.of(ModBlocks.BURLYWOOD_VIOLET))));

        register(context, CRIMSONVEIL_DARK_WATER_LAKE, AdaptiveLakeFeature.INSTANCE, new AdaptiveLakeFeatureConfig(
                BlockStateProvider.of(ModBlocks.DARK_WATER), 1, UniformIntProvider.create(2, 7), UniformIntProvider.create(2, 7)));

        register(context, UMBRAL_KELP, UmbralKelpFeature.INSTANCE, new DefaultFeatureConfig());
        DataPool.Builder<BlockState> builder = DataPool.builder();

        for (int i = 1; i <= 4; i++) {
            for (Direction direction : Direction.Type.HORIZONTAL)
                builder.add(ModBlocks.AMETHYST_PETALS.getDefaultState().with(FlowerbedBlock.FLOWER_AMOUNT, i).with(FlowerbedBlock.FACING, direction), 1);
        }
        register(
                context,
                AMETHYST_FLOWERS,
                Feature.FLOWER,
                new RandomPatchFeatureConfig(
                        32, 3, 2, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(builder)))
                )
        );
        register(context, GLOOMWEED_PATCH, Feature.RANDOM_PATCH,
                new RandomPatchFeatureConfig(
                        96,
                        6,
                        2,
                        PlacedFeatures.createEntry(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockFeatureConfig(
                                        new NoiseBlockStateProvider(
                                                2345L,
                                                new DoublePerlinNoiseSampler.NoiseParameters(0, 1.0),
                                                0.020833334F,
                                                List.of(
                                                        ModBlocks.GLOOMWEED.getDefaultState()
                                                )
                                        )
                                )
                        )
                ));
        register(context, TALL_GLOOMWEED_PATCH, Feature.RANDOM_PATCH,
                new RandomPatchFeatureConfig(
                        48,
                        5,
                        2,
                        PlacedFeatures.createEntry(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockFeatureConfig(
                                        new NoiseBlockStateProvider(
                                                2345L,
                                                new DoublePerlinNoiseSampler.NoiseParameters(0, 1.0),
                                                0.020833334F,
                                                List.of(
                                                        ModBlocks.TALL_GLOOMWEED.getDefaultState()
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
                        VerticalSurfaceType.CEILING,
                        SimpleBlockStateProvider.of(ModBlocks.GRIPPING_GLACIEMITE.getDefaultState().with(Properties.FACING, Direction.DOWN)),
                        0.2f,
                        4,
                        0.4f,
                        UniformIntProvider.create(4, 7),
                        0.1f
                )
        );
        register(
                context,
                GRIPPING_DARK_CURRANTSLATE_PATCH_FLOOR,
                GrippingVegetationFeature.INSTANCE,
                new GrippingVegetationFeatureConfig(
                        VerticalSurfaceType.FLOOR,
                        SimpleBlockStateProvider.of(ModBlocks.GRIPPING_DARK_CURRANTSLATE.getDefaultState().with(Properties.FACING, Direction.UP)),
                        0.25f,
                        3,
                        0.35f,
                        UniformIntProvider.create(3, 7),
                        0.25f
                )
        );
    }

    public static RegistryKey<ConfiguredFeature<?, ?>> create(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, UnseenWorld.makeID(name));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context,
                                                                                   RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
        context.register(key, new ConfiguredFeature<>(feature, config));
    }
}
