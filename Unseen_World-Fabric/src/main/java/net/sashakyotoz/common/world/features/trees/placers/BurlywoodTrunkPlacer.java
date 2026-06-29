package net.sashakyotoz.common.world.features.trees.placers;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.world.features.trees.ModTreePlacerTypes;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class BurlywoodTrunkPlacer extends TrunkPlacer {
    public static final Codec<BurlywoodTrunkPlacer> CODEC = RecordCodecBuilder.create(instance ->
            trunkPlacerParts(instance)
                    .and(Codec.BOOL.fieldOf("should_search_wood").forGetter(trunk -> trunk.shouldSearchAnotherTrunk))
                    .apply(instance, BurlywoodTrunkPlacer::new)
    );
    private final boolean shouldSearchAnotherTrunk;

    public BurlywoodTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight, boolean shouldSearchWood) {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
        this.shouldSearchAnotherTrunk = shouldSearchWood;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTreePlacerTypes.BURLYWOOD_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(
            LevelSimulatedReader world,
            BiConsumer<BlockPos, BlockState> replacer,
            RandomSource random,
            int height,
            BlockPos startPos,
            TreeConfiguration config
    ) {
        List<FoliagePlacer.FoliageAttachment> list = Lists.newArrayList();
        if (config.forceDirt) {
            BlockPos base = startPos.below();
            setDirtAt(world, replacer, random, base, config);
            setDirtAt(world, replacer, random, base.east(), config);
            setDirtAt(world, replacer, random, base.south(), config);
            setDirtAt(world, replacer, random, base.south().east(), config);
        }
        BlockPos trunk = startPos;
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        int j = random.nextInt(3);
        for (int i = 0; i < height; i++) {
            Direction direction;
            if (i % 2 == 0 && random.nextBoolean()) {
                direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
                trunk = trunk.relative(direction);
                if (i >= height / 2) {
                    mutable.set(trunk);
                    placeBranch(startPos.relative(direction).above(i - 4), world, replacer, random, height - 2, list, config);
                }
            }
            if (TreeFeature.isAirOrLeaves(world, trunk)) {
                this.placeLog(world, replacer, random, trunk, config);
                this.placeLog(world, replacer, random, trunk.east(), config);
                this.placeLog(world, replacer, random, trunk.south(), config);
                this.placeLog(world, replacer, random, trunk.south().east(), config);
            }
            if ((i == height / 2 || i == (height + 2) / 2) && this.shouldSearchAnotherTrunk) {
                if (this.placeBranchToAnotherTree(config, world, random, Direction.Axis.X, getFreePos(startPos.above(i), world), replacer))
                    this.placeBranchToAnotherTree(config, world, random, Direction.Axis.Z, getFreePos(startPos.above(i), world), replacer);
            }
            trunk = trunk.above();
        }
        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        int i = height - random.nextInt(4);
        int k = startPos.getX();
        int l = startPos.getY();
        int m = startPos.getZ();

        int nx = k;
        int nz = m;
        int topY = l + height - 1;

        for (int q = 0; q < height; q++) {
            if (q >= i && j > 0) {
                nx += direction.getStepX();
                nz += direction.getStepZ();
                j--;
            }
        }
        list.add(new FoliagePlacer.FoliageAttachment(new BlockPos(nx, topY - 1, nz), 0, true));
        for (int q = -1; q <= 2; q++) {
            for (int r = -1; r <= 2; r++) {
                if ((q < 0 || q > 1 || r < 0 || r > 1) && random.nextInt(3) == 0) {
                    int s = random.nextInt(3) + 2;
                    for (int t = 0; t < s; t++) {
                        this.placeLog(world, replacer, random, new BlockPos(k + q, topY - t - 1, m + r), config);
                    }
                    list.add(new FoliagePlacer.FoliageAttachment(new BlockPos(nx + q, topY, nz + r), 0, false));
                }
            }
        }
        return list;
    }

    private BlockPos getFreePos(BlockPos pos, LevelSimulatedReader world) {
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                pos = pos.offset(x, 0, z);
                if (world.isStateAtPosition(pos, BlockBehaviour.BlockStateBase::isAir))
                    return pos;
            }
        }
        return pos;
    }

    private void placeBranch(BlockPos start, LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> replacer, RandomSource random,
                             int height, List<FoliagePlacer.FoliageAttachment> list, TreeConfiguration config) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        OptionalInt optionalInt = OptionalInt.empty();
        Direction ultDirection = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        int x = start.getX();
        int z = start.getZ();
        int dx, dz, DX = 0, DZ = 0, k = 0;
        for (int dy = 0; dy < height; dy++) {
            int y1 = start.getY() + dy;

            if (dy > height * 0.4) {
                for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
                    dx = direction.getStepX();
                    dz = direction.getStepZ();
                    if (this.placeLog(world, replacer, random, mutableBlockPos.set(x + dx * k + DX, y1, z + dz * k + DZ), config)) {
                        optionalInt = OptionalInt.of(y1 + 1);
                    }
                }
                k++;
            } else if (dy <= height * 0.4) {
                if (this.placeLog(world, replacer, random, mutableBlockPos.set(x + DX, y1, z + DZ), config)) {
                    optionalInt = OptionalInt.of(y1 + 1);
                }
                if ((dy + 1) % 3 == 0) {
                    DX += ultDirection.getStepX();
                    DZ += ultDirection.getStepZ();
                }
            }
        }
        int k1 = k - 1;
        for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
            if (optionalInt.isPresent()) {
                list.add(new FoliagePlacer.FoliageAttachment(new BlockPos(x + DX + direction.getStepX() * k1, optionalInt.getAsInt() - 1, z + DZ + direction.getStepZ() * k1), 1, false));
            }
        }
    }

    private boolean placeBranchToAnotherTree(TreeConfiguration config, LevelSimulatedReader world, RandomSource random, Direction.Axis axis, BlockPos pos, BiConsumer<BlockPos, BlockState> replacer) {
        Direction negative = (axis == Direction.Axis.X) ? Direction.WEST : Direction.NORTH;
        Direction positive = (axis == Direction.Axis.X) ? Direction.EAST : Direction.SOUTH;

        BlockPos wallNeg = findFirstNonEmpty(world, pos, negative, 21);
        BlockPos wallPos = findFirstNonEmpty(world, pos, positive, 21);

        if (wallNeg == null || wallPos == null) return false;

        int coordNeg = (axis == Direction.Axis.X) ? wallNeg.getX() : wallNeg.getZ();
        int coordPos = (axis == Direction.Axis.X) ? wallPos.getX() : wallPos.getZ();

        if (Math.abs(coordPos - coordNeg) <= 1) return false;
        int startCoord = Math.min(coordNeg, coordPos) + 1;
        int endCoord = Math.max(coordNeg, coordPos) - 1;
        int y = pos.getY();

        BlockPos start = (axis == Direction.Axis.X)
                ? new BlockPos(startCoord, y, pos.getZ())
                : new BlockPos(pos.getX(), y, startCoord);

        BlockPos end = (axis == Direction.Axis.X)
                ? new BlockPos(endCoord, y, pos.getZ())
                : new BlockPos(pos.getX(), y, endCoord);
        if (!rectangularGapIsEmpty(world, start, end)) return false;
        int samples = Math.max((int) Math.ceil(start.distSqr(end) * 0.5), 12);
        samples = Math.min(samples, 256);

        List<BlockPos> curvePositions;
        for (int attempt = 0; attempt < 3; attempt++) {
            double sideOffset = computeSideOffset(start, end, random);
            double sag = computeSag(start, end, random);
            curvePositions = generateCurvePositions(start, end, axis, sideOffset, sag, samples);
            int currSamples = samples;
            while ((!isFaceConnectedSequence(curvePositions)) && currSamples <= 1024) {
                currSamples *= 2;
                if (currSamples > 1024) break;
                curvePositions = generateCurvePositions(start, end, axis, sideOffset, sag, currSamples);
            }

            if (isFaceConnectedSequence(curvePositions)) {
                boolean allEmpty = true;
                for (BlockPos p : curvePositions) {
                    if (!world.isStateAtPosition(p, BlockBehaviour.BlockStateBase::isAir)) {
                        allEmpty = false;
                        break;
                    }
                }
                if (allEmpty) {
                    for (BlockPos p : curvePositions) {
                        if ((p.getX() % 3 == 0 || p.getZ() % 3 == 0) && random.nextDouble() > 0.725f)
                            this.getAndSetHeartState(world, replacer, p, Function.identity());
                        else
                            this.placeLog(world, replacer, random, p, config);
                    }
                    return true;
                }
            }
            samples = Math.min(samples * 2, 512);
        }
        return false;
    }

    protected void getAndSetHeartState(
            LevelSimulatedReader world,
            BiConsumer<BlockPos, BlockState> replacer,
            BlockPos pos,
            Function<BlockState, BlockState> function
    ) {
        if (this.validTreePos(world, pos))
            replacer.accept(pos, function.apply(ModBlocks.BURLYWOOD_HEART.defaultBlockState()));
    }

    private static double computeSideOffset(BlockPos a, BlockPos b, RandomSource random) {
        double dx = b.getX() - a.getX();
        double dz = b.getZ() - a.getZ();
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        double base = Math.max(1.0, horizontal) * (0.15 + random.nextDouble() * 0.45);
        return (random.nextBoolean() ? 1 : -1) * base;
    }

    private static double computeSag(BlockPos a, BlockPos b, RandomSource random) {
        double dx = b.getX() - a.getX();
        double dz = b.getZ() - a.getZ();
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        double sag = horizontal * (0.05 + random.nextDouble() * 0.20);
        return -sag;
    }

    private List<BlockPos> generateCurvePositions(BlockPos start, BlockPos end, Direction.Axis axis, double sideOffset, double sag, int samples) {
        double mx = (start.getX() + end.getX()) * 0.5;
        double my = (start.getY() + end.getY()) * 0.5;
        double mz = (start.getZ() + end.getZ()) * 0.5;
        double cx = mx;
        double cy = my + sag;
        double cz = mz;
        if (axis == Direction.Axis.X)
            cz += sideOffset;
        else
            cx += sideOffset;
        Set<BlockPos> ordered = new LinkedHashSet<>();
        for (int i = 0; i <= samples; i++) {
            double t = (double) i / (double) samples;
            double omt = 1.0 - t;
            double bx = omt * omt * start.getX() + 2 * omt * t * cx + t * t * end.getX();
            double by = omt * omt * start.getY() + 2 * omt * t * cy + t * t * end.getY();
            double bz = omt * omt * start.getZ() + 2 * omt * t * cz + t * t * end.getZ();
            int ix = (int) Math.round(bx);
            int iy = (int) Math.round(by);
            int iz = (int) Math.round(bz);
            ordered.add(new BlockPos(ix, iy, iz));
        }
        return new ArrayList<>(ordered);
    }

    private boolean rectangularGapIsEmpty(LevelSimulatedReader level, BlockPos a, BlockPos b) {
        int minX = Math.min(a.getX(), b.getX());
        int maxX = Math.max(a.getX(), b.getX());
        int minZ = Math.min(a.getZ(), b.getZ());
        int maxZ = Math.max(a.getZ(), b.getZ());
        int y = a.getY();
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                if (!level.isStateAtPosition(new BlockPos(x, y, z), BlockBehaviour.BlockStateBase::isAir)) return false;
            }
        }
        return true;
    }

    private boolean isFaceConnectedSequence(List<BlockPos> list) {
        if (list == null || list.isEmpty()) return false;
        for (int i = 1; i < list.size(); i++) {
            if (!areFaceAdjacent(list.get(i - 1), list.get(i))) return false;
        }
        return true;
    }

    private boolean areFaceAdjacent(BlockPos a, BlockPos b) {
        int dx = Math.abs(a.getX() - b.getX());
        int dy = Math.abs(a.getY() - b.getY());
        int dz = Math.abs(a.getZ() - b.getZ());
        return (dx + dy + dz) == 1;
    }

    private BlockPos findFirstNonEmpty(LevelSimulatedReader world, BlockPos start, Direction dir, int maxDist) {
        for (int i = 1; i <= maxDist; i++) {
            BlockPos p = start.relative(dir, i);
            if (!world.isStateAtPosition(p, BlockBehaviour.BlockStateBase::isAir)) {
                return p;
            }
        }
        return null;
    }
}