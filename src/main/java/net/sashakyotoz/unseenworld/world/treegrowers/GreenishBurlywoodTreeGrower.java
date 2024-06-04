package net.sashakyotoz.unseenworld.world.treegrowers;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModFeatures;
import org.jetbrains.annotations.Nullable;

public class GreenishBurlywoodTreeGrower extends AbstractMegaTreeGrower {
    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource p_222910_, boolean p_222911_) {
        return UnseenWorldModFeatures.GREENISH_BURLYWOOD_TREE;
    }

    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredMegaFeature(RandomSource p_222904_) {
        return UnseenWorldModFeatures.MEGA_GREENISH_BURLYWOOD_TREE;
    }
}
