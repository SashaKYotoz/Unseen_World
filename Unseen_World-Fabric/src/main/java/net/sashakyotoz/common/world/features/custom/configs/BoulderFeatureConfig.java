package net.sashakyotoz.common.world.features.custom.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public record BoulderFeatureConfig(IntProvider size, BlockStateProvider block) implements FeatureConfig {
    public static Codec<BoulderFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    IntProvider.POSITIVE_CODEC.fieldOf("size").forGetter(BoulderFeatureConfig::size),
                    BlockStateProvider.TYPE_CODEC.fieldOf("block").forGetter(BoulderFeatureConfig::block)
            ).apply(instance, BoulderFeatureConfig::new));
}