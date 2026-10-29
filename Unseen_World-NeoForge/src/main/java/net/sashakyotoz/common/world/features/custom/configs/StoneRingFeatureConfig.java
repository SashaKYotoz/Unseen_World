package net.sashakyotoz.common.world.features.custom.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record StoneRingFeatureConfig(BlockStateProvider pillar, BlockStateProvider cap, BlockStateProvider flower,
                                     IntProvider radius, IntProvider pillarCount, IntProvider pillarHeight,
                                     float brokenChance) implements FeatureConfiguration {
    public static final Codec<StoneRingFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockStateProvider.CODEC.fieldOf("pillar").forGetter(StoneRingFeatureConfig::pillar),
            BlockStateProvider.CODEC.fieldOf("cap").forGetter(StoneRingFeatureConfig::cap),
            BlockStateProvider.CODEC.fieldOf("flower").forGetter(StoneRingFeatureConfig::flower),
            IntProvider.codec(2, 8).fieldOf("radius").forGetter(StoneRingFeatureConfig::radius),
            IntProvider.codec(3, 16).fieldOf("pillar_count").forGetter(StoneRingFeatureConfig::pillarCount),
            IntProvider.codec(1, 8).fieldOf("pillar_height").forGetter(StoneRingFeatureConfig::pillarHeight),
            Codec.floatRange(0.0F, 1.0F).fieldOf("broken_chance").forGetter(StoneRingFeatureConfig::brokenChance)
    ).apply(instance, StoneRingFeatureConfig::new));
}
