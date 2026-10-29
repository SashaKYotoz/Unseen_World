package net.sashakyotoz.common.world.features.custom.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record NestFeatureConfig(BlockStateProvider shell, BlockStateProvider floor, BlockStateProvider decoration,
                                BlockStateProvider core, IntProvider radius, float decorationChance,
                                EntityType<?> dweller, IntProvider dwellerCount) implements FeatureConfiguration {
    public static final Codec<NestFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockStateProvider.CODEC.fieldOf("shell").forGetter(NestFeatureConfig::shell),
            BlockStateProvider.CODEC.fieldOf("floor").forGetter(NestFeatureConfig::floor),
            BlockStateProvider.CODEC.fieldOf("decoration").forGetter(NestFeatureConfig::decoration),
            BlockStateProvider.CODEC.fieldOf("core").forGetter(NestFeatureConfig::core),
            IntProvider.codec(2, 4).fieldOf("radius").forGetter(NestFeatureConfig::radius),
            Codec.floatRange(0.0F, 1.0F).fieldOf("decoration_chance").forGetter(NestFeatureConfig::decorationChance),
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("dweller").forGetter(NestFeatureConfig::dweller),
            IntProvider.codec(0, 4).fieldOf("dweller_count").forGetter(NestFeatureConfig::dwellerCount)
    ).apply(instance, NestFeatureConfig::new));
}
