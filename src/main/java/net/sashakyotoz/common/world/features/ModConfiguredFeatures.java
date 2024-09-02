package net.sashakyotoz.common.world.features;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.RandomSpreadFoliagePlacer;
import net.minecraft.world.gen.root.AboveRootPlacement;
import net.minecraft.world.gen.root.MangroveRootPlacement;
import net.minecraft.world.gen.root.MangroveRootPlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.treedecorator.AttachedToLeavesTreeDecorator;
import net.minecraft.world.gen.trunk.BendingTrunkPlacer;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.ModBlocks;

import java.util.List;
import java.util.Optional;

public class ModConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> AMETHYST_TREE = create("amethyst_tree");

    public static void boostrap(Registerable<ConfiguredFeature<?, ?>> context) {
        RegistryEntryLookup<Block> registryEntryLookup = context.getRegistryLookup(RegistryKeys.BLOCK);
        register(context, AMETHYST_TREE, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.AMETHYST_LOG),
                new BendingTrunkPlacer(5, 2, 1, 4, UniformIntProvider.create(1, 2)),
                BlockStateProvider.of(ModBlocks.AMETHYST_LEAVES),
                new RandomSpreadFoliagePlacer(UniformIntProvider.create(2, 4), UniformIntProvider.create(0, 1), ConstantIntProvider.create(2), 52),
                Optional.of(
                        new MangroveRootPlacer(
                                UniformIntProvider.create(3, 7),
                                BlockStateProvider.of(ModBlocks.AMETHYST_WOOD),
                                Optional.of(new AboveRootPlacement(BlockStateProvider.of(ModBlocks.AMETHYST_LOG), 0.25F)),
                                new MangroveRootPlacement(
                                        registryEntryLookup.getOrThrow(BlockTags.MANGROVE_ROOTS_CAN_GROW_THROUGH),
                                        RegistryEntryList.of(Block::getRegistryEntry, Blocks.MUD, Blocks.MUDDY_MANGROVE_ROOTS),
                                        BlockStateProvider.of(Blocks.MUDDY_MANGROVE_ROOTS),
                                        2,
                                        3,
                                        0.2F
                                )
                        )
                ),
                new TwoLayersFeatureSize(1, 1, 1))
                .decorators(List.of(
                        new AttachedToLeavesTreeDecorator(
                                0.2f,
                                1,
                                0,
                                new WeightedBlockStateProvider(
                                        DataPool.of(ModBlocks.HANGING_AMETHYST_LEAVES.getDefaultState())
                                ),
                                1,
                                List.of(Direction.DOWN))))
                .dirtProvider(BlockStateProvider.of(ModBlocks.AMETHYST_GRASS_BLOCK)).forceDirt().build());
    }

    public static RegistryKey<ConfiguredFeature<?, ?>> create(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, UnseenWorld.makeID(name));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context,
                                                                                   RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
        context.register(key, new ConfiguredFeature<>(feature, config));
    }
}
