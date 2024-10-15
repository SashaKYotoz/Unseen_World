package net.sashakyotoz.common.world.features;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.PropaguleBlock;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.intprovider.WeightedListIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.ThreeLayersFeatureSize;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.AcaciaFoliagePlacer;
import net.minecraft.world.gen.foliage.RandomSpreadFoliagePlacer;
import net.minecraft.world.gen.root.AboveRootPlacement;
import net.minecraft.world.gen.root.MangroveRootPlacement;
import net.minecraft.world.gen.root.MangroveRootPlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.RandomizedIntBlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.treedecorator.AttachedToLeavesTreeDecorator;
import net.minecraft.world.gen.trunk.CherryTrunkPlacer;
import net.minecraft.world.gen.trunk.UpwardsBranchingTrunkPlacer;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.custom.HangingFruitBlock;
import net.sashakyotoz.common.world.features.custom.BoulderFeature;
import net.sashakyotoz.common.world.features.custom.MidnightLilyPadFeature;
import net.sashakyotoz.common.world.features.custom.StandingFlowerBunchFeature;
import net.sashakyotoz.common.world.features.custom.configs.BoulderFeatureConfig;
import net.sashakyotoz.common.world.features.custom.configs.StandingFlowerBunchFeatureConfig;
import net.sashakyotoz.common.world.features.trees.placers.BurlywoodFoliagePlacer;
import net.sashakyotoz.common.world.features.trees.placers.BurlywoodTrunkPlacer;
import net.sashakyotoz.common.world.features.trees.placers.GrizzlyFoliagePlacer;
import net.sashakyotoz.common.world.features.trees.placers.GrizzlyTrunkPlacer;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class ModConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> AMETHYST_TREE = create("amethyst_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SMALL_AMETHYST_TREE = create("small_amethyst_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> CRIMSONVEIL_TREE = create("crimsonveil_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TALL_CRIMSONVEIL_TREE = create("tall_crimsonveil_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TEALIVE_TREE = create("tealive_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> GRIZZLY_TREE = create("grizzly_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> TALL_GRIZZLY_TREE = create("tall_grizzly_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> BURLYWOOD_TREE = create("burlywood_tree");

    public static final RegistryKey<ConfiguredFeature<?, ?>> UNSEENIUM_ORE = create("unseenium_ore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> RED_TITANIUM_ORE = create("red_titanium_ore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> ABYSSAL_ORE = create("abyssal_ore");

    public static final RegistryKey<ConfiguredFeature<?, ?>> GLACIEMITE_BOULDER = create("glaciemite_boulder");
    public static final RegistryKey<ConfiguredFeature<?, ?>> DARK_CURRANTSLATE_BOULDER = create("dark_currantslate_boulder");

    public static final RegistryKey<ConfiguredFeature<?, ?>> WATER_LAKE = create("water_lake");
    public static final RegistryKey<ConfiguredFeature<?, ?>> DARK_WATER_LAKE = create("dark_water_lake");

    public static final RegistryKey<ConfiguredFeature<?, ?>> BEARFRUIT_BRAMBLE_PATCH = create("bearfruit_bramble_patch");
    public static final RegistryKey<ConfiguredFeature<?, ?>> MIDNIGHT_LILY_PATCH = create("midnight_lily_patch");

    public static void boostrap(Registerable<ConfiguredFeature<?, ?>> context) {
        RegistryEntryLookup<Block> registryEntryLookup = context.getRegistryLookup(RegistryKeys.BLOCK);
        register(context, AMETHYST_TREE, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.AMETHYST_LOG),
                new GrizzlyTrunkPlacer(5, 2, 2),
                BlockStateProvider.of(ModBlocks.AMETHYST_LEAVES),
                new AcaciaFoliagePlacer(UniformIntProvider.create(1, 2), UniformIntProvider.create(0, 1)),
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
                new GrizzlyTrunkPlacer(2, 1, 2),
                BlockStateProvider.of(ModBlocks.AMETHYST_LEAVES),
                new AcaciaFoliagePlacer(UniformIntProvider.create(0, 1), UniformIntProvider.create(0, 1)),
                new TwoLayersFeatureSize(1, 1, 1))
                .dirtProvider(BlockStateProvider.of(ModBlocks.AMETHYST_GRASS_BLOCK)).forceDirt().build());
        register(context, CRIMSONVEIL_TREE, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.CRIMSONVEIL_LOG),
                new UpwardsBranchingTrunkPlacer(6, 3, 4, UniformIntProvider.create(1, 5), 0.75F, UniformIntProvider.create(0, 2), registryEntryLookup.getOrThrow(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH)),
                BlockStateProvider.of(ModBlocks.CRIMSONVEIL_LEAVES),
                new RandomSpreadFoliagePlacer(ConstantIntProvider.create(3), ConstantIntProvider.create(0), ConstantIntProvider.create(2), 70),
                Optional.of(
                        new MangroveRootPlacer(
                                UniformIntProvider.create(1, 4),
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
                                        0.14F,
                                        1,
                                        0,
                                        new RandomizedIntBlockStateProvider(
                                                BlockStateProvider.of(Blocks.MANGROVE_PROPAGULE.getDefaultState().with(PropaguleBlock.HANGING, Boolean.TRUE)),
                                                PropaguleBlock.AGE,
                                                UniformIntProvider.create(0, 4)
                                        ),
                                        2,
                                        List.of(Direction.DOWN)
                                )
                        )
                ).ignoreVines().build());
        register(context, TALL_CRIMSONVEIL_TREE, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.CRIMSONVEIL_LOG),
                new UpwardsBranchingTrunkPlacer(8, 3, 5, UniformIntProvider.create(2, 6), 0.75F, UniformIntProvider.create(0, 2), registryEntryLookup.getOrThrow(BlockTags.MANGROVE_LOGS_CAN_GROW_THROUGH)),
                BlockStateProvider.of(ModBlocks.CRIMSONVEIL_LEAVES),
                new RandomSpreadFoliagePlacer(ConstantIntProvider.create(3), ConstantIntProvider.create(0), ConstantIntProvider.create(2), 70),
                Optional.of(
                        new MangroveRootPlacer(
                                UniformIntProvider.create(1, 4),
                                BlockStateProvider.of(ModBlocks.CRIMSONVEIL_LOG),
                                Optional.of(new AboveRootPlacement(BlockStateProvider.of(ModBlocks.CRIMSONVEIL_LOG), 0F)),
                                new MangroveRootPlacement(
                                        registryEntryLookup.getOrThrow(BlockTags.MANGROVE_ROOTS_CAN_GROW_THROUGH),
                                        RegistryEntryList.of(Block::getRegistryEntry, ModBlocks.NIGHTDARK_GRASS_BLOCK, ModBlocks.CRIMSONVEIL_LOG),
                                        BlockStateProvider.of(ModBlocks.CRIMSONVEIL_LOG), 6, 13, 0.2F))),
                new TwoLayersFeatureSize(2, 0, 2))
                .decorators(
                        List.of(
                                new AttachedToLeavesTreeDecorator(
                                        0.15F,
                                        1,
                                        0,
                                        new RandomizedIntBlockStateProvider(
                                                BlockStateProvider.of(Blocks.MANGROVE_PROPAGULE.getDefaultState().with(PropaguleBlock.HANGING, Boolean.TRUE)),
                                                PropaguleBlock.AGE,
                                                UniformIntProvider.create(0, 4)
                                        ),
                                        2,
                                        List.of(Direction.DOWN)
                                )
                        )
                ).ignoreVines().build());
        register(context, TEALIVE_TREE, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.TEALIVE_LOG),
                new CherryTrunkPlacer(7, 1, 0,
                        new WeightedListIntProvider(DataPool.<IntProvider>builder().add(ConstantIntProvider.create(1), 1).add(ConstantIntProvider.create(2), 1).add(ConstantIntProvider.create(3), 1).build()),
                        UniformIntProvider.create(2, 4),
                        UniformIntProvider.create(-4, -3),
                        UniformIntProvider.create(-1, 0)
                ),
                BlockStateProvider.of(ModBlocks.TEALIVE_LEAVES),
                new AcaciaFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0)),
                new TwoLayersFeatureSize(1, 0, 2)
        ).ignoreVines().build());
        register(context, GRIZZLY_TREE, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.GRIZZLY_LOG),
                new GrizzlyTrunkPlacer(4, 3, 5),
                BlockStateProvider.of(ModBlocks.GRIZZLY_LEAVES),
                new GrizzlyFoliagePlacer(UniformIntProvider.create(3, 5), ConstantIntProvider.create(0)),
                new TwoLayersFeatureSize(1, 0, 2)
        ).dirtProvider(BlockStateProvider.of(ModBlocks.GRIMWOOD_GRASS_BLOCK)).build());
        register(context, TALL_GRIZZLY_TREE, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.GRIZZLY_LOG),
                new GrizzlyTrunkPlacer(9, 4, 6),
                BlockStateProvider.of(ModBlocks.GRIZZLY_LEAVES),
                new GrizzlyFoliagePlacer(UniformIntProvider.create(2, 3), UniformIntProvider.create(0, 1)),
                new TwoLayersFeatureSize(1, 1, 2)
        ).dirtProvider(BlockStateProvider.of(ModBlocks.GRIMWOOD_GRASS_BLOCK)).build());
        register(context, BURLYWOOD_TREE, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.BURLYWOOD_LOG),
                new BurlywoodTrunkPlacer(9, 2, 3),
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
        register(context, UNSEENIUM_ORE, Feature.ORE, new OreFeatureConfig(unseeniumOres, 4));
        register(context, RED_TITANIUM_ORE, Feature.ORE, new OreFeatureConfig(titaniumOres, 2));
        register(context, ABYSSAL_ORE, Feature.ORE, new OreFeatureConfig(abyssalOres, 3));

        register(context, GLACIEMITE_BOULDER, BoulderFeature.INSTANCE, new BoulderFeatureConfig(UniformIntProvider.create(4, 9), BlockStateProvider.of(ModBlocks.GLACIEMITE)));
        register(context, DARK_CURRANTSLATE_BOULDER, BoulderFeature.INSTANCE, new BoulderFeatureConfig(UniformIntProvider.create(3, 8), BlockStateProvider.of(ModBlocks.DARK_CURRANTSLATE)));

        register(context, WATER_LAKE, Feature.LAKE, new LakeFeature.Config(BlockStateProvider.of(Blocks.WATER.getDefaultState()), BlockStateProvider.of(Blocks.STONE.getDefaultState())));
        register(context, DARK_WATER_LAKE, Feature.LAKE, new LakeFeature.Config(BlockStateProvider.of(ModBlocks.DARK_WATER_BLOCK.getDefaultState()), BlockStateProvider.of(ModBlocks.DARK_CURRANTSLATE.getDefaultState())));

        register(context, BEARFRUIT_BRAMBLE_PATCH, StandingFlowerBunchFeature.INSTANCE, new StandingFlowerBunchFeatureConfig(UniformIntProvider.create(3, 7), BlockStateProvider.of(ModBlocks.BEARFRUIT_BRAMBLE)));
        register(context, MIDNIGHT_LILY_PATCH, MidnightLilyPadFeature.INSTANCE,DefaultFeatureConfig.INSTANCE);
    }

    public static RegistryKey<ConfiguredFeature<?, ?>> create(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, UnseenWorld.makeID(name));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context,
                                                                                   RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
        context.register(key, new ConfiguredFeature<>(feature, config));
    }
}
