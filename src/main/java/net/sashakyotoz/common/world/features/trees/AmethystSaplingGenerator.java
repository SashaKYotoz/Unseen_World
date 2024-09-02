package net.sashakyotoz.common.world.features.trees;

import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.sashakyotoz.common.world.features.ModConfiguredFeatures;
import org.jetbrains.annotations.Nullable;

public class AmethystSaplingGenerator extends SaplingGenerator {
    @Nullable
    @Override
    protected RegistryKey<ConfiguredFeature<?, ?>> getTreeFeature(Random random, boolean bees) {
        return ModConfiguredFeatures.AMETHYST_TREE;
    }
}