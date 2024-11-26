package net.sashakyotoz.common.world.features.custom.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public record AdaptiveLakeFeatureConfig(BlockStateProvider state, Integer deepness, IntProvider xRadius, IntProvider zRadius) implements FeatureConfig {
    public static Codec<AdaptiveLakeFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    BlockStateProvider.TYPE_CODEC.fieldOf("block").forGetter(AdaptiveLakeFeatureConfig::state),
                    Codecs.POSITIVE_INT.fieldOf("deepness").forGetter(AdaptiveLakeFeatureConfig::deepness),
                    IntProvider.VALUE_CODEC.fieldOf("xRadius").forGetter(AdaptiveLakeFeatureConfig::xRadius),
                    IntProvider.VALUE_CODEC.fieldOf("zRadius").forGetter(AdaptiveLakeFeatureConfig::zRadius)
            ).apply(instance, AdaptiveLakeFeatureConfig::new));
}