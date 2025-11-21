package net.sashakyotoz.common.world.features.custom.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public record AdaptiveLakeFeatureConfig(BlockStateProvider state, BlockStateProvider barrier) implements FeatureConfig {
    public static Codec<AdaptiveLakeFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    BlockStateProvider.TYPE_CODEC.fieldOf("block").forGetter(AdaptiveLakeFeatureConfig::state),
                    BlockStateProvider.TYPE_CODEC.fieldOf("barrier").forGetter(AdaptiveLakeFeatureConfig::barrier)
            ).apply(instance, AdaptiveLakeFeatureConfig::new));
}