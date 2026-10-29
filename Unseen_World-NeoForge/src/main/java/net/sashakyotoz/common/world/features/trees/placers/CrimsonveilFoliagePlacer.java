package net.sashakyotoz.common.world.features.trees.placers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.sashakyotoz.common.world.features.trees.ModTreePlacerTypes;

public class CrimsonveilFoliagePlacer extends FoliagePlacer {
    public static final MapCodec<CrimsonveilFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(instance -> foliagePlacerParts(instance)
            .and(instance.group(
                    Codec.floatRange(0.0F, 1.0F).fieldOf("hanging_leaves_chance").forGetter(placer -> placer.hangingLeavesChance),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("hanging_leaves_extension_chance").forGetter(placer -> placer.hangingLeavesExtensionChance),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("ragged_edge_chance").forGetter(placer -> placer.raggedEdgeChance)))
            .apply(instance, CrimsonveilFoliagePlacer::new));

    private static final int MAX_LEAF_REACH = 4;

    private final float hangingLeavesChance;
    private final float hangingLeavesExtensionChance;
    private final float raggedEdgeChance;

    public CrimsonveilFoliagePlacer(IntProvider radius, IntProvider offset, float hangingLeavesChance, float hangingLeavesExtensionChance, float raggedEdgeChance) {
        super(radius, offset);
        this.hangingLeavesChance = hangingLeavesChance;
        this.hangingLeavesExtensionChance = hangingLeavesExtensionChance;
        this.raggedEdgeChance = raggedEdgeChance;
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModTreePlacerTypes.CRIMSONVEIL_FOLIAGE_PLACER;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader level, FoliageSetter setter, RandomSource random, TreeConfiguration config, int maxFreeTreeHeight,
                                 FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset) {
        int radius = foliageRadius + attachment.radiusOffset();
        this.placeLeavesRow(level, setter, random, config, attachment.pos(), Math.max(radius - 1, 1), offset + 1, false);
        this.placeLeavesRow(level, setter, random, config, attachment.pos(), radius, offset, false);
        this.placeLeavesRowWithHangingLeavesBelow(level, setter, random, config, attachment.pos(), radius, offset - 1, false,
                this.hangingLeavesChance, this.hangingLeavesExtensionChance);
    }

    @Override
    public int foliageHeight(RandomSource random, int trunkHeight, TreeConfiguration config) {
        return 3;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        int reach = dx + dz + Math.abs(y);
        if (reach > Math.min(radius + 1, MAX_LEAF_REACH)) {
            return true;
        }
        return reach == Math.min(radius + 1, MAX_LEAF_REACH) && random.nextFloat() < this.raggedEdgeChance;
    }
}
