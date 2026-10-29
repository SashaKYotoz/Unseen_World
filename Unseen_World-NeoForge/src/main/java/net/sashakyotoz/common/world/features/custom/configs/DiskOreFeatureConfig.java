package net.sashakyotoz.common.world.features.custom.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;

public record DiskOreFeatureConfig(RuleBasedBlockStateProvider stateProvider, RuleBasedBlockStateProvider oreStateProvider,
                                   BlockPredicate target, IntProvider radius, int halfHeight) implements FeatureConfiguration {
    public static final Codec<DiskOreFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            RuleBasedBlockStateProvider.CODEC.fieldOf("state_provider").forGetter(DiskOreFeatureConfig::stateProvider),
                            RuleBasedBlockStateProvider.CODEC.fieldOf("ore_state_provider").forGetter(DiskOreFeatureConfig::oreStateProvider),
                            BlockPredicate.CODEC.fieldOf("target").forGetter(DiskOreFeatureConfig::target),
                            IntProvider.codec(0, 8).fieldOf("radius").forGetter(DiskOreFeatureConfig::radius),
                            Codec.intRange(0, 4).fieldOf("half_height").forGetter(DiskOreFeatureConfig::halfHeight)
                    )
                    .apply(instance, DiskOreFeatureConfig::new)
    );
}