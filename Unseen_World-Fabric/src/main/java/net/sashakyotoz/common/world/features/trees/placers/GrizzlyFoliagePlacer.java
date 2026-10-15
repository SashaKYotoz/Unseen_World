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

public class GrizzlyFoliagePlacer extends FoliagePlacer {
    public static final Codec<GrizzlyFoliagePlacer> CODEC = RecordCodecBuilder.create(instance -> foliagePlacerParts(instance)
            .and(instance.group(
                    Codec.intRange(1, 4).fieldOf("height").forGetter(placer -> placer.height),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("edge_hole_chance").forGetter(placer -> placer.edgeHoleChance)))
            .apply(instance, GrizzlyFoliagePlacer::new));

    private static final float SHELL = 0.6F;

    private final int height;
    private final float edgeHoleChance;

    public GrizzlyFoliagePlacer(IntProvider radius, IntProvider offset, int height, float edgeHoleChance) {
        super(radius, offset);
        this.height = height;
        this.edgeHoleChance = edgeHoleChance;
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModTreePlacerTypes.GRIZZLY_FOLIAGE_PLACER;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader level, FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, int maxFreeTreeHeight, FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset) {
        int radius = Math.max(foliageRadius + attachment.radiusOffset(), 1);
        BlockPos center = attachment.pos().above(offset);
        this.placeLeavesRow(level, foliageSetter, random, config, center, radius - 1, -1, attachment.doubleTrunk());
        for (int localY = 0; localY <= this.height; localY++) {
            this.placeLeavesRow(level, foliageSetter, random, config, center, radius, localY, attachment.doubleTrunk());
        }
    }

    @Override
    public int foliageHeight(RandomSource random, int height, TreeConfiguration config) {
        return this.height;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
        float horizontal = range + 0.5F;
        float vertical = this.height + 0.5F;
        float value = (localX * localX + localZ * localZ) / (horizontal * horizontal) + (localY * localY) / (vertical * vertical);
        if (value > 1.0F) {
            return true;
        }
        return value > SHELL && random.nextFloat() < this.edgeHoleChance;
    }
}