package net.sashakyotoz.common.world.features.custom.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record AdaptiveLakeFeatureConfig(BlockStateProvider state, BlockStateProvider barrier) implements FeatureConfiguration {
    public static Codec<AdaptiveLakeFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    BlockStateProvider.CODEC.fieldOf("block").forGetter(AdaptiveLakeFeatureConfig::state),
                    BlockStateProvider.CODEC.fieldOf("barrier").forGetter(AdaptiveLakeFeatureConfig::barrier)
            ).apply(instance, AdaptiveLakeFeatureConfig::new));
}