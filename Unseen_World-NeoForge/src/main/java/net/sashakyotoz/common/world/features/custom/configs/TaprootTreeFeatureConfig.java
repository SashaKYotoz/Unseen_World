package net.sashakyotoz.common.world.features.custom.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record TaprootTreeFeatureConfig(BlockStateProvider trunk, BlockStateProvider leaves, BlockStateProvider hangingLeaves,
                                       BlockStateProvider roots, BlockStateProvider anchor, BlockState heart,
                                       TagKey<Block> anchorReplaceable, IntProvider trunkLength, IntProvider crownRadius,
                                       float heartChance) implements FeatureConfiguration {
    public static final Codec<TaprootTreeFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockStateProvider.CODEC.fieldOf("trunk").forGetter(TaprootTreeFeatureConfig::trunk),
            BlockStateProvider.CODEC.fieldOf("leaves").forGetter(TaprootTreeFeatureConfig::leaves),
            BlockStateProvider.CODEC.fieldOf("hanging_leaves").forGetter(TaprootTreeFeatureConfig::hangingLeaves),
            BlockStateProvider.CODEC.fieldOf("roots").forGetter(TaprootTreeFeatureConfig::roots),
            BlockStateProvider.CODEC.fieldOf("anchor").forGetter(TaprootTreeFeatureConfig::anchor),
            BlockState.CODEC.fieldOf("heart").forGetter(TaprootTreeFeatureConfig::heart),
            TagKey.codec(Registries.BLOCK).fieldOf("anchor_replaceable").forGetter(TaprootTreeFeatureConfig::anchorReplaceable),
            IntProvider.codec(2, 12).fieldOf("trunk_length").forGetter(TaprootTreeFeatureConfig::trunkLength),
            IntProvider.codec(1, 4).fieldOf("crown_radius").forGetter(TaprootTreeFeatureConfig::crownRadius),
            Codec.floatRange(0.0F, 1.0F).fieldOf("heart_chance").forGetter(TaprootTreeFeatureConfig::heartChance)
    ).apply(instance, TaprootTreeFeatureConfig::new));
}
