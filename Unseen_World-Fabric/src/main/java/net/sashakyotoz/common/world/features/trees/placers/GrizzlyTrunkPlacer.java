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
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.sashakyotoz.common.world.features.trees.ModTreePlacerTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class GrizzlyTrunkPlacer extends TrunkPlacer {
    public static final Codec<GrizzlyTrunkPlacer> CODEC = RecordCodecBuilder.create(instance -> trunkPlacerParts(instance)
            .and(instance.group(
                    Codec.floatRange(0.1F, 0.9F).fieldOf("fork_height").forGetter(placer -> placer.forkHeight),
                    IntProvider.codec(1, 4).fieldOf("branch_count").forGetter(placer -> placer.branchCount),
                    IntProvider.codec(0, 6).fieldOf("branch_spread").forGetter(placer -> placer.branchSpread),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("twig_chance").forGetter(placer -> placer.twigChance)))
            .apply(instance, GrizzlyTrunkPlacer::new));

    private static final float FULL_TURN = (float) (Math.PI * 2.0D);
    private static final float ANGLE_JITTER = 0.8F;
    private static final int MAX_TWIG_LENGTH = 3;

    private final float forkHeight;
    private final IntProvider branchCount;
    private final IntProvider branchSpread;
    private final float twigChance;

    public GrizzlyTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, float forkHeight, IntProvider branchCount, IntProvider branchSpread, float twigChance) {
        super(baseHeight, heightRandA, heightRandB);
        this.forkHeight = forkHeight;
        this.branchCount = branchCount;
        this.branchSpread = branchSpread;
        this.twigChance = twigChance;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTreePlacerTypes.GRIZZLY_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, int freeTreeHeight, BlockPos pos, TreeConfiguration config) {
        setDirtAt(level, blockSetter, random, pos.below(), config);
        int top = Math.max(freeTreeHeight - 1, 3);
        int forkY = Mth.clamp(Math.round(top * this.forkHeight), 1, top - 2);
        for (int y = 0; y <= forkY; y++) {
            this.placeLog(level, blockSetter, random, pos.above(y), config);
        }
        BlockPos fork = pos.above(forkY);
        List<FoliagePlacer.FoliageAttachment> attachments = new ArrayList<>();
        int count = this.branchCount.sample(random);
        float baseAngle = random.nextFloat() * FULL_TURN;
        for (int i = 0; i < count; i++) {
            float angle = baseAngle + i * FULL_TURN / count + (random.nextFloat() - 0.5F) * ANGLE_JITTER;
            int spread = this.branchSpread.sample(random);
            int length = top - random.nextInt(2) - forkY;
            this.growBranch(level, blockSetter, random, config, fork, angle, spread, length, attachments);
        }
        return attachments;
    }

    private void growBranch(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, TreeConfiguration config, BlockPos fork, float angle, int spread, int length, List<FoliagePlacer.FoliageAttachment> attachments) {
        float reachX = Mth.cos(angle) * spread;
        float reachZ = Mth.sin(angle) * spread;
        int x = 0;
        int z = 0;
        for (int step = 1; step <= length; step++) {
            float progress = (float) step / length;
            int targetX = Math.round(reachX * progress);
            int targetZ = Math.round(reachZ * progress);
            while (x != targetX) {
                x += Integer.signum(targetX - x);
                this.placeLog(level, blockSetter, random, fork.offset(x, step - 1, z), config, state -> state.trySetValue(RotatedPillarBlock.AXIS, Direction.Axis.X));
            }
            while (z != targetZ) {
                z += Integer.signum(targetZ - z);
                this.placeLog(level, blockSetter, random, fork.offset(x, step - 1, z), config, state -> state.trySetValue(RotatedPillarBlock.AXIS, Direction.Axis.Z));
            }
            BlockPos node = fork.offset(x, step, z);
            this.placeLog(level, blockSetter, random, node, config);
            if (step < length && random.nextFloat() < this.twigChance) {
                this.growTwig(level, blockSetter, random, config, node, attachments);
            }
        }
        attachments.add(new FoliagePlacer.FoliageAttachment(fork.offset(x, length, z), 0, false));
    }

    private void growTwig(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, TreeConfiguration config, BlockPos node, List<FoliagePlacer.FoliageAttachment> attachments) {
        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        int length = 1 + random.nextInt(MAX_TWIG_LENGTH);
        BlockPos tip = node;
        for (int i = 1; i <= length; i++) {
            BlockPos next = node.relative(direction, i);
            if (!this.placeLog(level, blockSetter, random, next, config, state -> state.trySetValue(RotatedPillarBlock.AXIS, direction.getAxis()))) {
                return;
            }
            tip = next;
        }
        if (length >= 2) {
            attachments.add(new FoliagePlacer.FoliageAttachment(tip, -1, false));
        }
    }
}