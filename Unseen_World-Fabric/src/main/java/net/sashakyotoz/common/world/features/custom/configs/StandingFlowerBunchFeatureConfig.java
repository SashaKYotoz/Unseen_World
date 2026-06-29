package net.sashakyotoz.common.world.features.custom.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record StandingFlowerBunchFeatureConfig(IntProvider radius, BlockStateProvider block) implements FeatureConfiguration {
    public static Codec<StandingFlowerBunchFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    IntProvider.POSITIVE_CODEC.fieldOf("radius").forGetter(StandingFlowerBunchFeatureConfig::radius),
                    BlockStateProvider.CODEC.fieldOf("block").forGetter(StandingFlowerBunchFeatureConfig::block)
            ).apply(instance, StandingFlowerBunchFeatureConfig::new));
}