package net.sashakyotoz.common.world.features.trees.placers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.sashakyotoz.common.world.features.trees.ModTreePlacerTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class CrimsonveilTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<CrimsonveilTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(instance -> trunkPlacerParts(instance)
            .and(instance.group(
                    Codec.floatRange(0.2F, 0.8F).fieldOf("fork_height").forGetter(placer -> placer.forkHeight),
                    IntProvider.codec(2, 6).fieldOf("branch_count").forGetter(placer -> placer.branchCount),
                    IntProvider.codec(1, 6).fieldOf("branch_reach").forGetter(placer -> placer.branchReach),
                    IntProvider.codec(1, 6).fieldOf("branch_rise").forGetter(placer -> placer.branchRise),
                    Codec.BOOL.fieldOf("central_leader").forGetter(placer -> placer.centralLeader)))
            .apply(instance, CrimsonveilTrunkPlacer::new));

    private static final float FULL_TURN = (float) (Math.PI * 2.0D);
    private static final float ANGLE_JITTER = 0.6F;

    private final float forkHeight;
    private final IntProvider branchCount;
    private final IntProvider branchReach;
    private final IntProvider branchRise;
    private final boolean centralLeader;

    public CrimsonveilTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, float forkHeight, IntProvider branchCount, IntProvider branchReach, IntProvider branchRise, boolean centralLeader) {
        super(baseHeight, heightRandA, heightRandB);
        this.forkHeight = forkHeight;
        this.branchCount = branchCount;
        this.branchReach = branchReach;
        this.branchRise = branchRise;
        this.centralLeader = centralLeader;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTreePlacerTypes.CRIMSONVEIL_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, int freeTreeHeight, BlockPos pos, TreeConfiguration config) {
        setDirtAt(level, blockSetter, random, pos.below(), config);
        int top = Math.max(freeTreeHeight - 1, 4);
        int forkY = Mth.clamp(Math.round(top * this.forkHeight), 2, top - 2);
        for (int y = 0; y <= forkY; y++) {
            this.placeLog(level, blockSetter, random, pos.above(y), config);
        }
        BlockPos fork = pos.above(forkY);
        List<FoliagePlacer.FoliageAttachment> attachments = new ArrayList<>();
        if (this.centralLeader) {
            for (int y = forkY + 1; y <= top; y++) {
                this.placeLog(level, blockSetter, random, pos.above(y), config);
            }
            attachments.add(new FoliagePlacer.FoliageAttachment(pos.above(top), 0, false));
        }
        int count = this.branchCount.sample(random);
        float baseAngle = random.nextFloat() * FULL_TURN;
        for (int i = 0; i < count; i++) {
            float angle = baseAngle + i * FULL_TURN / count + (random.nextFloat() - 0.5F) * ANGLE_JITTER;
            this.growBranch(level, blockSetter, random, config, fork, angle, this.branchReach.sample(random), this.branchRise.sample(random), attachments);
        }
        return attachments;
    }

    private void growBranch(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, TreeConfiguration config,
                            BlockPos fork, float angle, int reach, int rise, List<FoliagePlacer.FoliageAttachment> attachments) {
        float reachX = Mth.cos(angle) * reach;
        float reachZ = Mth.sin(angle) * reach;
        int steps = Math.max(reach, rise);
        int x = 0;
        int y = 0;
        int z = 0;
        for (int step = 1; step <= steps; step++) {
            float progress = (float) step / steps;
            float bend = progress * progress;
            int targetX = Math.round(reachX * progress);
            int targetZ = Math.round(reachZ * progress);
            int targetY = Math.round(rise * (0.35F * progress + 0.65F * bend));
            while (x != targetX) {
                x += Integer.signum(targetX - x);
                if (!this.placeLog(level, blockSetter, random, fork.offset(x, y, z), config, state -> state.trySetValue(RotatedPillarBlock.AXIS, Direction.Axis.X))) {
                    return;
                }
            }
            while (z != targetZ) {
                z += Integer.signum(targetZ - z);
                if (!this.placeLog(level, blockSetter, random, fork.offset(x, y, z), config, state -> state.trySetValue(RotatedPillarBlock.AXIS, Direction.Axis.Z))) {
                    return;
                }
            }
            while (y < targetY) {
                y++;
                if (!this.placeLog(level, blockSetter, random, fork.offset(x, y, z), config)) {
                    return;
                }
            }
        }
        if (y == 0) {
            y++;
            this.placeLog(level, blockSetter, random, fork.offset(x, y, z), config);
        }
        attachments.add(new FoliagePlacer.FoliageAttachment(fork.offset(x, y, z), 0, false));
    }
}
