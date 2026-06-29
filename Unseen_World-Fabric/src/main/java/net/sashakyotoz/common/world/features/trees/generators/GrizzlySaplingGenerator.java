package net.sashakyotoz.common.world.features.trees.generators;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.sashakyotoz.common.world.features.ModConfiguredFeatures;
import org.jetbrains.annotations.Nullable;

public class GrizzlySaplingGenerator extends AbstractTreeGrower {
    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean bees) {
        return ModConfiguredFeatures.GRIZZLY_TREE;
    }
}