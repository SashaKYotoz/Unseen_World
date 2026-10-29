package net.sashakyotoz.common.world.features.trees.placers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.world.features.trees.ModTreePlacerTypes;

import java.util.*;
import java.util.function.BiConsumer;

public class BurlywoodTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<BurlywoodTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(instance -> trunkPlacerParts(instance)
            .and(instance.group(
                    Codec.floatRange(0.2F, 0.8F).fieldOf("fork_height").forGetter(placer -> placer.forkHeight),
                    BranchSettings.CODEC.fieldOf("branches").forGetter(placer -> placer.branches),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("root_chance").forGetter(placer -> placer.rootChance),
                    BridgeSettings.CODEC.optionalFieldOf("bridge").forGetter(placer -> placer.bridge)))
            .apply(instance, BurlywoodTrunkPlacer::new));

    private static final float FULL_TURN = (float) (Math.PI * 2.0D);
    private static final float ANGLE_JITTER = 0.6F;
    private static final float CLIMB_CHANCE = 0.55F;
    private static final float DRIFT_CHANCE = 0.5F;
    private static final int MAX_TIP_RISE = 3;
    private static final int MAX_ROOT_DROP = 2;
    private static final int MAX_ROOT_CRAWL = 2;
    private static final int MAX_BUTTRESS = 2;
    private static final int BRIDGE_MIN_GAP = 4;
    private static final int BRIDGE_SCAN_SPAN = 2;
    private static final float BRIDGE_HEART_CHANCE = 0.1F;

    private final float forkHeight;
    private final BranchSettings branches;
    private final float rootChance;
    private final Optional<BridgeSettings> bridge;

    public BurlywoodTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, float forkHeight, BranchSettings branches, float rootChance, Optional<BridgeSettings> bridge) {
        super(baseHeight, heightRandA, heightRandB);
        this.forkHeight = forkHeight;
        this.branches = branches;
        this.rootChance = rootChance;
        this.bridge = bridge;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTreePlacerTypes.BURLYWOOD_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, int freeTreeHeight, BlockPos pos, TreeConfiguration config) {
        int top = Math.max(freeTreeHeight - 1, 6);
        int coreTop = top - 2;
        int forkY = Mth.clamp(Math.round(top * this.forkHeight), 2, coreTop - 1);
        Optional<BridgeSettings> activeBridge = this.bridge.filter(settings -> random.nextFloat() < settings.chance());
        Optional<BlockPos> bridgeTarget = activeBridge.flatMap(settings -> this.findBridgeTarget(level, pos, forkY, settings));

        Set<BlockPos> own = new HashSet<>();
        BiConsumer<BlockPos, BlockState> setter = (target, state) -> {
            own.add(target.immutable());
            blockSetter.accept(target, state);
        };

        for (int x = 0; x <= 1; x++) {
            for (int z = 0; z <= 1; z++) {
                setDirtAt(level, blockSetter, random, pos.offset(x, -1, z), config);
            }
        }

        BlockPos[] cores = this.placeCore(level, setter, random, pos, coreTop, forkY, config);
        this.placeRoots(level, setter, random, pos, config);

        List<FoliagePlacer.FoliageAttachment> attachments = new ArrayList<>();
        int count = this.branches.count().sample(random);
        float baseAngle = random.nextFloat() * FULL_TURN;
        for (int i = 0; i < count; i++) {
            float angle = baseAngle + i * FULL_TURN / count + (random.nextFloat() - 0.5F) * ANGLE_JITTER;
            BlockPos core = cores[forkY + random.nextInt(coreTop - forkY + 1)];
            float startX = core.getX() + 1.0F;
            float startZ = core.getZ() + 1.0F;
            BlockPos start = new BlockPos(Mth.floor(startX - 0.5F + 0.5F * Mth.cos(angle)), core.getY(), Mth.floor(startZ - 0.5F + 0.5F * Mth.sin(angle)));
            this.growLimb(level, setter, random, config, own, start, startX, startZ, angle, this.branches.length().sample(random), true, attachments);
        }
        attachments.add(new FoliagePlacer.FoliageAttachment(cores[coreTop].above(), 0, true));

        activeBridge.ifPresent(settings -> bridgeTarget.ifPresent(target -> this.placeBridge(level, setter, random, config, own, cores, coreTop, target, settings)));
        return attachments;
    }

    private BlockPos[] placeCore(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> setter, RandomSource random, BlockPos pos, int coreTop, int forkY, TreeConfiguration config) {
        BlockPos[] cores = new BlockPos[coreTop + 1];
        Direction drift = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        int driftAt = random.nextFloat() < DRIFT_CHANCE ? 2 + random.nextInt(Math.max(forkY - 1, 1)) : -1;
        int offsetX = 0;
        int offsetZ = 0;
        for (int y = 0; y <= coreTop; y++) {
            if (y == driftAt) {
                offsetX += drift.getStepX();
                offsetZ += drift.getStepZ();
            }
            BlockPos core = pos.offset(offsetX, y, offsetZ);
            cores[y] = core;
            for (int x = 0; x <= 1; x++) {
                for (int z = 0; z <= 1; z++) {
                    this.placeLog(level, setter, random, core.offset(x, 0, z), config);
                }
            }
        }
        return cores;
    }

    private void placeRoots(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> setter, RandomSource random, BlockPos pos, TreeConfiguration config) {
        for (int dx = -1; dx <= 2; dx++) {
            for (int dz = -1; dz <= 2; dz++) {
                boolean inner = dx >= 0 && dx <= 1 && dz >= 0 && dz <= 1;
                if (inner || random.nextFloat() >= this.rootChance) {
                    continue;
                }
                Direction outward = this.outward(dx, dz, random);
                BlockPos foot = this.placeRootBlock(level, setter, random, config, pos.offset(dx, 0, dz), Direction.Axis.Y);
                if (foot == null) {
                    continue;
                }
                int buttress = random.nextInt(MAX_BUTTRESS + 1);
                for (int y = 1; y <= buttress; y++) {
                    this.placeLog(level, setter, random, foot.above(y), config);
                }
                BlockPos crawl = foot;
                int length = random.nextInt(MAX_ROOT_CRAWL + 1);
                for (int i = 0; i < length && crawl != null; i++) {
                    crawl = this.placeRootBlock(level, setter, random, config, crawl.relative(outward), outward.getAxis());
                }
            }
        }
    }

    private Direction outward(int dx, int dz, RandomSource random) {
        Direction alongX = dx < 0 ? Direction.WEST : dx > 1 ? Direction.EAST : null;
        Direction alongZ = dz < 0 ? Direction.NORTH : dz > 1 ? Direction.SOUTH : null;
        if (alongX != null && alongZ != null) {
            return random.nextBoolean() ? alongX : alongZ;
        }
        return alongX != null ? alongX : alongZ;
    }

    private BlockPos placeRootBlock(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> setter, RandomSource random, TreeConfiguration config, BlockPos pos, Direction.Axis axis) {
        BlockPos.MutableBlockPos cursor = pos.mutable();
        for (int i = 0; i < MAX_ROOT_DROP && this.validTreePos(level, cursor) && this.validTreePos(level, cursor.below()); i++) {
            cursor.move(Direction.DOWN);
        }
        if (this.validTreePos(level, cursor)) {
            if (this.validTreePos(level, cursor.below())) {
                return null;
            }
            this.placeLog(level, setter, random, cursor, config, state -> state.trySetValue(RotatedPillarBlock.AXIS, axis));
            return cursor.immutable();
        }
        if (Feature.isGrassOrDirt(level, cursor)) {
            setter.accept(cursor.immutable(), config.trunkProvider.getState(random, cursor).trySetValue(RotatedPillarBlock.AXIS, axis));
            return cursor.immutable();
        }
        return null;
    }

    private void growLimb(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> setter, RandomSource random, TreeConfiguration config, Set<BlockPos> own, BlockPos start, float startX, float startZ, float angle, int length, boolean canFork, List<FoliagePlacer.FoliageAttachment> attachments) {
        float stepX = Mth.cos(angle);
        float stepZ = Mth.sin(angle);
        float floatX = startX;
        float floatZ = startZ;
        BlockPos.MutableBlockPos cursor = start.mutable();
        int forkStep = Math.max(length / 2, 2);
        for (int step = 1; step <= length; step++) {
            floatX += stepX;
            floatZ += stepZ;
            int targetX = Mth.floor(floatX);
            int targetZ = Mth.floor(floatZ);
            while (cursor.getX() != targetX) {
                Direction move = targetX > cursor.getX() ? Direction.EAST : Direction.WEST;
                cursor.move(move);
                if (!this.placeWood(level, setter, random, config, own, cursor, Direction.Axis.X)) {
                    this.finishLimb(cursor.move(move.getOpposite()), attachments, canFork);
                    return;
                }
            }
            while (cursor.getZ() != targetZ) {
                Direction move = targetZ > cursor.getZ() ? Direction.SOUTH : Direction.NORTH;
                cursor.move(move);
                if (!this.placeWood(level, setter, random, config, own, cursor, Direction.Axis.Z)) {
                    this.finishLimb(cursor.move(move.getOpposite()), attachments, canFork);
                    return;
                }
            }
            if (step > 1 && random.nextFloat() < CLIMB_CHANCE) {
                cursor.move(Direction.UP);
                if (!this.placeWood(level, setter, random, config, own, cursor, Direction.Axis.Y)) {
                    this.finishLimb(cursor.move(Direction.DOWN), attachments, canFork);
                    return;
                }
            }
            if (canFork && step == forkStep && step < length && random.nextFloat() < this.branches.subBranchChance()) {
                float side = random.nextBoolean() ? 1.0F : -1.0F;
                float subAngle = angle + side * (0.8F + random.nextFloat() * 0.5F);
                this.growLimb(level, setter, random, config, own, cursor.immutable(), cursor.getX() + 0.5F, cursor.getZ() + 0.5F, subAngle, Math.max(length / 2, 1) + 1, false, attachments);
            }
        }
        int rise = 1 + random.nextInt(MAX_TIP_RISE);
        for (int i = 0; i < rise; i++) {
            cursor.move(Direction.UP);
            if (!this.placeWood(level, setter, random, config, own, cursor, Direction.Axis.Y)) {
                cursor.move(Direction.DOWN);
                break;
            }
        }
        this.finishLimb(cursor, attachments, canFork);
    }

    private void finishLimb(BlockPos tip, List<FoliagePlacer.FoliageAttachment> attachments, boolean primary) {
        attachments.add(new FoliagePlacer.FoliageAttachment(tip.above(), primary ? 0 : -1, false));
    }

    private boolean placeWood(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> setter, RandomSource random, TreeConfiguration config, Set<BlockPos> own, BlockPos pos, Direction.Axis axis) {
        if (own.contains(pos)) {
            return true;
        }
        return this.placeLog(level, setter, random, pos, config, state -> state.trySetValue(RotatedPillarBlock.AXIS, axis));
    }

    private Optional<BlockPos> findBridgeTarget(LevelSimulatedReader level, BlockPos origin, int forkY, BridgeSettings settings) {
        int range = settings.range();
        int rangeSq = range * range;
        BlockPos.MutableBlockPos probe = new BlockPos.MutableBlockPos();
        BlockPos best = null;
        int bestScore = Integer.MAX_VALUE;
        for (int dy = -BRIDGE_SCAN_SPAN; dy <= BRIDGE_SCAN_SPAN; dy += 2) {
            for (int dx = -range; dx <= range; dx += 2) {
                for (int dz = -range; dz <= range; dz += 2) {
                    int horizontalSq = dx * dx + dz * dz;
                    if (horizontalSq > rangeSq || (Math.abs(dx) <= BRIDGE_MIN_GAP && Math.abs(dz) <= BRIDGE_MIN_GAP)) {
                        continue;
                    }
                    int score = horizontalSq + dy * dy * 4;
                    if (score >= bestScore) {
                        continue;
                    }
                    probe.setWithOffset(origin, dx, forkY - 1 + dy, dz);
                    if (level.isStateAtPosition(probe, state -> state.is(settings.targets()))) {
                        best = probe.immutable();
                        bestScore = score;
                    }
                }
            }
        }
        return Optional.ofNullable(best);
    }

    private void placeBridge(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> setter, RandomSource random, TreeConfiguration config, Set<BlockPos> own, BlockPos[] cores, int coreTop, BlockPos target, BridgeSettings settings) {
        BlockPos core = cores[Mth.clamp(target.getY() - cores[0].getY(), 1, coreTop)];
        int startX = target.getX() > core.getX() ? core.getX() + 1 : core.getX();
        int startZ = target.getZ() > core.getZ() ? core.getZ() + 1 : core.getZ();
        int startY = core.getY();
        double deltaX = target.getX() - startX;
        double deltaY = target.getY() - startY;
        double deltaZ = target.getZ() - startZ;
        double horizontal = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        if (horizontal < 1.0D) {
            return;
        }
        double sag = horizontal * (0.08D + random.nextDouble() * 0.12D);
        double swing = horizontal * (random.nextDouble() - 0.5D) * 0.4D;
        double normalX = -deltaZ / horizontal;
        double normalZ = deltaX / horizontal;
        int samples = Mth.ceil((horizontal + Math.abs(deltaY) + sag * 2.0D) * 2.0D) + 2;

        List<BlockPos> path = new ArrayList<>();
        List<Direction.Axis> axes = new ArrayList<>();
        int x = startX;
        int y = startY;
        int z = startZ;
        for (int i = 1; i <= samples; i++) {
            double t = (double) i / samples;
            double bow = Math.sin(Math.PI * t);
            int targetX = (int) Math.round(startX + deltaX * t + normalX * swing * bow);
            int targetY = (int) Math.round(startY + deltaY * t - sag * bow);
            int targetZ = (int) Math.round(startZ + deltaZ * t + normalZ * swing * bow);
            while (x != targetX || y != targetY || z != targetZ) {
                Direction.Axis axis;
                if (x != targetX) {
                    x += Integer.signum(targetX - x);
                    axis = Direction.Axis.X;
                } else if (z != targetZ) {
                    z += Integer.signum(targetZ - z);
                    axis = Direction.Axis.Z;
                } else {
                    y += Integer.signum(targetY - y);
                    axis = Direction.Axis.Y;
                }
                BlockPos cell = new BlockPos(x, y, z);
                if (own.contains(cell)) {
                    continue;
                }
                if (level.isStateAtPosition(cell, state -> state.is(settings.targets()))) {
                    this.commitBridge(level, setter, random, config, path, axes);
                    return;
                }
                if (!this.validTreePos(level, cell)) {
                    return;
                }
                path.add(cell);
                axes.add(axis);
            }
        }
    }

    private void commitBridge(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> setter, RandomSource random, TreeConfiguration config, List<BlockPos> path, List<Direction.Axis> axes) {
        for (int i = 0; i < path.size(); i++) {
            BlockPos cell = path.get(i);
            Direction.Axis axis = axes.get(i);
            boolean inner = i > 0 && i < path.size() - 1;
            if (inner && random.nextFloat() < BRIDGE_HEART_CHANCE && this.validTreePos(level, cell)) {
                setter.accept(cell, ModBlocks.BURLYWOOD_HEART.defaultBlockState().trySetValue(RotatedPillarBlock.AXIS, axis));
            } else {
                this.placeLog(level, setter, random, cell, config, state -> state.trySetValue(RotatedPillarBlock.AXIS, axis));
            }
        }
    }

    public record BranchSettings(IntProvider count, IntProvider length, float subBranchChance) {
        public static final Codec<BranchSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                IntProvider.codec(1, 8).fieldOf("count").forGetter(BranchSettings::count),
                IntProvider.codec(1, 8).fieldOf("length").forGetter(BranchSettings::length),
                Codec.floatRange(0.0F, 1.0F).fieldOf("sub_branch_chance").forGetter(BranchSettings::subBranchChance)
        ).apply(instance, BranchSettings::new));
    }

    public record BridgeSettings(float chance, int range, HolderSet<Block> targets) {
        public static final Codec<BridgeSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.floatRange(0.0F, 1.0F).fieldOf("chance").forGetter(BridgeSettings::chance),
                Codec.intRange(BRIDGE_MIN_GAP + 1, 12).fieldOf("range").forGetter(BridgeSettings::range),
                RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("targets").forGetter(BridgeSettings::targets)
        ).apply(instance, BridgeSettings::new));
    }
}