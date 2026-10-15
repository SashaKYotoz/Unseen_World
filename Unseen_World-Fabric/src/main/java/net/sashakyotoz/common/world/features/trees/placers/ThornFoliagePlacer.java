package net.sashakyotoz.common.world.features.trees.placers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.sashakyotoz.common.world.features.trees.ModTreePlacerTypes;

public class ThornFoliagePlacer extends FoliagePlacer {
    public static final Codec<ThornFoliagePlacer> CODEC = RecordCodecBuilder.create(instance -> foliagePlacerParts(instance)
            .and(instance.group(
                    Codec.intRange(0, 4).fieldOf("max_depth").forGetter(placer -> placer.maxDepth),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("edge_hole_chance").forGetter(placer -> placer.edgeHoleChance)))
            .apply(instance, ThornFoliagePlacer::new));

    private final int maxDepth;
    private final float edgeHoleChance;

    public ThornFoliagePlacer(IntProvider radius, IntProvider offset, int maxDepth, float edgeHoleChance) {
        super(radius, offset);
        this.maxDepth = maxDepth;
        this.edgeHoleChance = edgeHoleChance;
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModTreePlacerTypes.THORN_FOLIAGE_PLACER;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader level, FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int maxFreeTreeHeight, FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset) {
        int radius = Math.max(foliageRadius + attachment.radiusOffset(), 1);
        BlockPos center = attachment.pos().above(offset);
        for (int localY = -Math.min(this.maxDepth, radius); localY <= radius; localY++) {
            this.placeLeavesRow(level, foliageSetter, random, config, center, radius, localY, attachment.doubleTrunk());
        }
    }

    @Override
    public int foliageHeight(RandomSource random, int height, TreeConfiguration config) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
        int distanceSq = localX * localX + localY * localY + localZ * localZ;
        float outer = range + 0.5F;
        if (distanceSq > outer * outer) {
            return true;
        }
        float inner = range - 0.5F;
        return distanceSq > inner * inner && random.nextFloat() < this.edgeHoleChance;
    }
}