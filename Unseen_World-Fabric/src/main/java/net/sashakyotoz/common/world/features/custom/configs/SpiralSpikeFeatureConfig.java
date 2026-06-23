package net.sashakyotoz.common.world.features.custom.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public record SpiralSpikeFeatureConfig(BlockStateProvider state,
                                       BlockStateProvider facing_state,
                                       BlockStateProvider decorating_state,
                                       IntProvider size) implements FeatureConfig {
    public static Codec<SpiralSpikeFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    BlockStateProvider.TYPE_CODEC.fieldOf("state").forGetter(SpiralSpikeFeatureConfig::state),
                    BlockStateProvider.TYPE_CODEC.fieldOf("facing_state").forGetter(SpiralSpikeFeatureConfig::facing_state),
                    BlockStateProvider.TYPE_CODEC.fieldOf("decorating_state").forGetter(SpiralSpikeFeatureConfig::decorating_state),
                    IntProvider.POSITIVE_CODEC.fieldOf("height").forGetter(SpiralSpikeFeatureConfig::size)
            ).apply(instance, SpiralSpikeFeatureConfig::new));
}