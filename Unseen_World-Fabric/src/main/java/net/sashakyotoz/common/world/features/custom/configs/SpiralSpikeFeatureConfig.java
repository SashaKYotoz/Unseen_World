package net.sashakyotoz.common.world.features.custom.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record SpiralSpikeFeatureConfig(BlockStateProvider state,
                                       BlockStateProvider facing_state,
                                       BlockStateProvider decorating_state,
                                       IntProvider size) implements FeatureConfiguration {
    public static Codec<SpiralSpikeFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    BlockStateProvider.CODEC.fieldOf("state").forGetter(SpiralSpikeFeatureConfig::state),
                    BlockStateProvider.CODEC.fieldOf("facing_state").forGetter(SpiralSpikeFeatureConfig::facing_state),
                    BlockStateProvider.CODEC.fieldOf("decorating_state").forGetter(SpiralSpikeFeatureConfig::decorating_state),
                    IntProvider.POSITIVE_CODEC.fieldOf("height").forGetter(SpiralSpikeFeatureConfig::size)
            ).apply(instance, SpiralSpikeFeatureConfig::new));
}