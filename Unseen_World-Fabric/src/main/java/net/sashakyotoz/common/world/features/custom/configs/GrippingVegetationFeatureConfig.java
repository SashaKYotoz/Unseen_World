package net.sashakyotoz.common.world.features.custom.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.CaveSurface;

public record GrippingVegetationFeatureConfig(CaveSurface surface,
                                              BlockStateProvider stoneState,
                                              float extraBottomBlockChance,
                                              int verticalRange,
                                              float vegetationChance,
                                              IntProvider horizontalRadius,
                                              float extraEdgeColumnChance) implements FeatureConfiguration {
    public static Codec<GrippingVegetationFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    CaveSurface.CODEC.fieldOf("surface").forGetter(GrippingVegetationFeatureConfig::surface),
                    BlockStateProvider.CODEC.fieldOf("stone_state").forGetter(GrippingVegetationFeatureConfig::stoneState),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("extra_bottom_block_chance").forGetter(GrippingVegetationFeatureConfig::extraBottomBlockChance),
                    Codec.intRange(1, 256).fieldOf("vertical_range").forGetter(GrippingVegetationFeatureConfig::verticalRange),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("vegetation_chance").forGetter(GrippingVegetationFeatureConfig::vegetationChance),
                    IntProvider.CODEC.fieldOf("xz_radius").forGetter(GrippingVegetationFeatureConfig::horizontalRadius),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("extra_edge_column_chance").forGetter(GrippingVegetationFeatureConfig::extraEdgeColumnChance)
            ).apply(instance, GrippingVegetationFeatureConfig::new));
}