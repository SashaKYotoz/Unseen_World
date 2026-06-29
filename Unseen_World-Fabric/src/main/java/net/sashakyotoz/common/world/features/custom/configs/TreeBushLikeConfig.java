package net.sashakyotoz.common.world.features.custom.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record TreeBushLikeConfig(IntProvider extraBranches, IntProvider height) implements FeatureConfiguration {
    public static Codec<TreeBushLikeConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    IntProvider.codec(0, 8).fieldOf("extraBranches").forGetter(TreeBushLikeConfig::extraBranches),
                    IntProvider.POSITIVE_CODEC.fieldOf("height").forGetter(TreeBushLikeConfig::height)
            ).apply(instance, TreeBushLikeConfig::new));
}