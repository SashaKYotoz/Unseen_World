package net.sashakyotoz.common.world.features.trees.placers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.sashakyotoz.common.world.features.trees.ModTreePlacerTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ThornTrunkPlacer extends TrunkPlacer {
    public static final Codec<ThornTrunkPlacer> CODEC = RecordCodecBuilder.create(instance -> trunkPlacerParts(instance)
            .and(instance.group(
                    IntProvider.codec(0, 8).fieldOf("lean").forGetter(placer -> placer.lean),
                    Codec.intRange(0, 3).fieldOf("base_radius").forGetter(placer -> placer.baseRadius),
                    Codec.floatRange(1.0F, 4.0F).fieldOf("curvature").forGetter(placer -> placer.curvature),
                    IntProvider.codec(1, 16).fieldOf("foliage_clearance").forGetter(placer -> placer.foliageClearance)))
            .apply(instance, ThornTrunkPlacer::new));

    private static final int MAX_ROOT_DEPTH = 3;
    private static final int FOLIAGE_SPACING = 2;

    private final IntProvider lean;
    private final int baseRadius;
    private final float curvature;
    private final IntProvider foliageClearance;

    public ThornTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, IntProvider lean, int baseRadius, float curvature, IntProvider foliageClearance) {
        super(baseHeight, heightRandA, heightRandB);
        this.lean = lean;
        this.baseRadius = baseRadius;
        this.curvature = curvature;
        this.foliageClearance = foliageClearance;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTreePlacerTypes.THORN_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, int freeTreeHeight, BlockPos pos, TreeConfiguration config) {
        setDirtAt(level, blockSetter, random, pos.below(), config);
        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        int leanLength = this.lean.sample(random);
        int top = Math.max(freeTreeHeight - 1, 1);
        int clearance = Math.min(this.foliageClearance.sample(random), top);
        BlockPos[] centers = new BlockPos[top + 1];
        int previousShift = 0;
        for (int y = 0; y <= top; y++) {
            float progress = (float) y / top;
            int shift = Math.round(leanLength * (float) Math.pow(progress, this.curvature));
            for (int step = previousShift + 1; step <= shift; step++) {
                BlockPos bridge = pos.relative(direction, step).above(y - 1);
                this.placeLog(level, blockSetter, random, bridge, config, state -> state.trySetValue(RotatedPillarBlock.AXIS, direction.getAxis()));
            }
            previousShift = shift;
            centers[y] = pos.relative(direction, shift).above(y);
            int radius = (int) (this.baseRadius * Mth.square(1.0F - progress) + 0.2F);
            this.placeLayer(level, blockSetter, random, centers[y], radius, y == 0, config);
        }
        List<FoliagePlacer.FoliageAttachment> attachments = new ArrayList<>();
        boolean taperedTip = top - clearance >= FOLIAGE_SPACING;
        attachments.add(new FoliagePlacer.FoliageAttachment(centers[top], taperedTip ? -1 : 0, false));
        for (int y = top - FOLIAGE_SPACING; y > clearance; y -= FOLIAGE_SPACING) {
            attachments.add(new FoliagePlacer.FoliageAttachment(centers[y], 0, false));
        }
        if (clearance < top) {
            attachments.add(new FoliagePlacer.FoliageAttachment(centers[clearance], 0, false));
        }
        return attachments;
    }

    private void placeLayer(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, BlockPos center, int radius, boolean ground, TreeConfiguration config) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (Math.abs(dx) + Math.abs(dz) > radius) {
                    continue;
                }
                BlockPos target = center.offset(dx, 0, dz);
                if (ground && (dx != 0 || dz != 0)) {
                    this.placeFlare(level, blockSetter, random, target, config);
                } else {
                    this.placeLog(level, blockSetter, random, target, config);
                }
            }
        }
    }

    private void placeFlare(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, BlockPos pos, TreeConfiguration config) {
        if (!this.validTreePos(level, pos)) {
            return;
        }
        BlockPos.MutableBlockPos probe = pos.mutable();
        for (int depth = 0; depth < MAX_ROOT_DEPTH; depth++) {
            probe.move(Direction.DOWN);
            if (this.validTreePos(level, probe)) {
                continue;
            }
            if (Feature.isGrassOrDirt(level, probe)) {
                setDirtAt(level, blockSetter, random, probe.immutable(), config);
            }
            for (int y = pos.getY(); y > probe.getY(); y--) {
                this.placeLog(level, blockSetter, random, new BlockPos(pos.getX(), y, pos.getZ()), config);
            }
            return;
        }
    }
}