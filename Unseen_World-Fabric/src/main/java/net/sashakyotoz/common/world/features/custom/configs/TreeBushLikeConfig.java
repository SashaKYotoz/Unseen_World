package net.sashakyotoz.common.world.features.custom.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;

public record TreeBushLikeConfig(IntProvider extraBranches, IntProvider height) implements FeatureConfig {
    public static Codec<TreeBushLikeConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    IntProvider.createValidatingCodec(0, 8).fieldOf("extraBranches").forGetter(TreeBushLikeConfig::extraBranches),
                    IntProvider.POSITIVE_CODEC.fieldOf("height").forGetter(TreeBushLikeConfig::height)
            ).apply(instance, TreeBushLikeConfig::new));
}