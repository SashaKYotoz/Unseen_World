package net.sashakyotoz.common.world.features.custom.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record SurfaceCoatingFeatureConfig(BlockStateProvider coating, IntProvider radius, float density) implements FeatureConfiguration {
    public static final Codec<SurfaceCoatingFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockStateProvider.CODEC.fieldOf("coating").forGetter(SurfaceCoatingFeatureConfig::coating),
            IntProvider.codec(1, 8).fieldOf("radius").forGetter(SurfaceCoatingFeatureConfig::radius),
            Codec.floatRange(0.0F, 1.0F).fieldOf("density").forGetter(SurfaceCoatingFeatureConfig::density)
    ).apply(instance, SurfaceCoatingFeatureConfig::new));
}
