package net.sashakyotoz.common.world.features.custom.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public record SpiralSpikesFeatureConfig(BlockStateProvider state, BlockStateProvider state1,
                                        IntProvider size) implements FeatureConfig {
    public static Codec<SpiralSpikesFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    BlockStateProvider.TYPE_CODEC.fieldOf("state").forGetter(SpiralSpikesFeatureConfig::state),
                    BlockStateProvider.TYPE_CODEC.fieldOf("state1").forGetter(SpiralSpikesFeatureConfig::state1),
                    IntProvider.POSITIVE_CODEC.fieldOf("size").forGetter(SpiralSpikesFeatureConfig::size)
            ).apply(instance, SpiralSpikesFeatureConfig::new));
}