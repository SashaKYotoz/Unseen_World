package net.sashakyotoz.common.world.features.custom.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public record ClusterSpikeFeatureConfig(BlockStateProvider state,
                                        BlockStateProvider cluster_state,
                                        IntProvider integration_size,
                                        IntProvider height) implements FeatureConfig {
    public static Codec<ClusterSpikeFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    BlockStateProvider.TYPE_CODEC.fieldOf("state").forGetter(ClusterSpikeFeatureConfig::state),
                    BlockStateProvider.TYPE_CODEC.fieldOf("cluster_state").forGetter(ClusterSpikeFeatureConfig::cluster_state),
                    IntProvider.POSITIVE_CODEC.fieldOf("integration_size").forGetter(ClusterSpikeFeatureConfig::integration_size),
                    IntProvider.POSITIVE_CODEC.fieldOf("height").forGetter(ClusterSpikeFeatureConfig::height)
            ).apply(instance, ClusterSpikeFeatureConfig::new));
}