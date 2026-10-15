package net.sashakyotoz.common.world.features.custom.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record CoralReefFeatureConfig(BlockState block, BlockState plant, BlockState fan, BlockState wallFan,
                                     IntProvider height, IntProvider radius, float decorationChance) implements FeatureConfiguration {
    public static final Codec<CoralReefFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.fieldOf("block").forGetter(CoralReefFeatureConfig::block),
            BlockState.CODEC.fieldOf("plant").forGetter(CoralReefFeatureConfig::plant),
            BlockState.CODEC.fieldOf("fan").forGetter(CoralReefFeatureConfig::fan),
            BlockState.CODEC.fieldOf("wall_fan").forGetter(CoralReefFeatureConfig::wallFan),
            IntProvider.codec(1, 12).fieldOf("height").forGetter(CoralReefFeatureConfig::height),
            IntProvider.codec(0, 4).fieldOf("radius").forGetter(CoralReefFeatureConfig::radius),
            Codec.floatRange(0.0F, 1.0F).fieldOf("decoration_chance").forGetter(CoralReefFeatureConfig::decorationChance)
    ).apply(instance, CoralReefFeatureConfig::new));
}
