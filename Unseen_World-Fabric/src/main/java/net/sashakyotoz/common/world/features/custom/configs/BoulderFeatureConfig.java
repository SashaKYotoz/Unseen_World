package net.sashakyotoz.common.world.features.custom.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record BoulderFeatureConfig(IntProvider size, BlockStateProvider block) implements FeatureConfiguration {
    public static Codec<BoulderFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    IntProvider.POSITIVE_CODEC.fieldOf("height").forGetter(BoulderFeatureConfig::size),
                    BlockStateProvider.CODEC.fieldOf("block").forGetter(BoulderFeatureConfig::block)
            ).apply(instance, BoulderFeatureConfig::new));
}