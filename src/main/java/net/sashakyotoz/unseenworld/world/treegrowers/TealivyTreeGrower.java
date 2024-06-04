package net.sashakyotoz.unseenworld.world.treegrowers;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModFeatures;
import org.jetbrains.annotations.Nullable;

public class TealivyTreeGrower extends AbstractTreeGrower {
    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource p_222910_, boolean p_222911_) {
        return UnseenWorldModFeatures.TEALIVE_TREE;
    }
}
