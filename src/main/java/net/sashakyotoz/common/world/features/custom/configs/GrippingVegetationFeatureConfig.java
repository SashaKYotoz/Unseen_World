package net.sashakyotoz.common.world.features.custom.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.VerticalSurfaceType;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public record GrippingVegetationFeatureConfig(VerticalSurfaceType surface,
                                              BlockStateProvider stoneState,
                                              float extraBottomBlockChance,
                                              int verticalRange,
                                              float vegetationChance,
                                              IntProvider horizontalRadius,
                                              float extraEdgeColumnChance) implements FeatureConfig {
    public static Codec<GrippingVegetationFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    VerticalSurfaceType.CODEC.fieldOf("surface").forGetter(GrippingVegetationFeatureConfig::surface),
                    BlockStateProvider.TYPE_CODEC.fieldOf("stone_state").forGetter(GrippingVegetationFeatureConfig::stoneState),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("extra_bottom_block_chance").forGetter(GrippingVegetationFeatureConfig::extraBottomBlockChance),
                    Codec.intRange(1, 256).fieldOf("vertical_range").forGetter(GrippingVegetationFeatureConfig::verticalRange),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("vegetation_chance").forGetter(GrippingVegetationFeatureConfig::vegetationChance),
                    IntProvider.VALUE_CODEC.fieldOf("xz_radius").forGetter(GrippingVegetationFeatureConfig::horizontalRadius),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("extra_edge_column_chance").forGetter(GrippingVegetationFeatureConfig::extraEdgeColumnChance)
            ).apply(instance, GrippingVegetationFeatureConfig::new));
}