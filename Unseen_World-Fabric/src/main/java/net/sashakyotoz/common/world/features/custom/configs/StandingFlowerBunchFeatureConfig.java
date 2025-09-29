package net.sashakyotoz.common.world.features.custom.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public record StandingFlowerBunchFeatureConfig(IntProvider radius, BlockStateProvider block) implements FeatureConfig {
    public static Codec<StandingFlowerBunchFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    IntProvider.POSITIVE_CODEC.fieldOf("radius").forGetter(StandingFlowerBunchFeatureConfig::radius),
                    BlockStateProvider.TYPE_CODEC.fieldOf("block").forGetter(StandingFlowerBunchFeatureConfig::block)
            ).apply(instance, StandingFlowerBunchFeatureConfig::new));
}