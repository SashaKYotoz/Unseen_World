package net.sashakyotoz.common.world.features.custom.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.PredicatedStateProvider;

public record DiskOreFeatureConfig(PredicatedStateProvider stateProvider, PredicatedStateProvider oreStateProvider,
                                   BlockPredicate target, IntProvider radius, int halfHeight) implements FeatureConfig {
    public static final Codec<DiskOreFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            PredicatedStateProvider.CODEC.fieldOf("state_provider").forGetter(DiskOreFeatureConfig::stateProvider),
                            PredicatedStateProvider.CODEC.fieldOf("ore_state_provider").forGetter(DiskOreFeatureConfig::oreStateProvider),
                            BlockPredicate.BASE_CODEC.fieldOf("target").forGetter(DiskOreFeatureConfig::target),
                            IntProvider.createValidatingCodec(0, 8).fieldOf("radius").forGetter(DiskOreFeatureConfig::radius),
                            Codec.intRange(0, 4).fieldOf("half_height").forGetter(DiskOreFeatureConfig::halfHeight)
                    )
                    .apply(instance, DiskOreFeatureConfig::new)
    );
}