package net.sashakyotoz.common.world.portal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.common.blocks.ModBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

public class ChimericPortalShape {
    public static final int MIN_WIDTH = 2;
    public static final int MAX_WIDTH = 21;
    public static final int MIN_HEIGHT = 3;
    public static final int MAX_HEIGHT = 21;

    private final LevelAccessor level;
    private final Direction.Axis axis;
    private final Direction rightDir;
    private int numPortalBlocks;
    @Nullable
    private BlockPos bottomLeft;
    private int height;
    private final int width;

    public static Optional<ChimericPortalShape> findEmptyPortalShape(LevelAccessor level, BlockPos pos, Direction.Axis axis) {
        return findPortalShape(level, pos, shape -> shape.isValid() && shape.numPortalBlocks == 0, axis);
    }

    public static Optional<ChimericPortalShape> findPortalShape(LevelAccessor level, BlockPos pos, Predicate<ChimericPortalShape> predicate, Direction.Axis axis) {
        Optional<ChimericPortalShape> shape = Optional.of(new ChimericPortalShape(level, pos, axis)).filter(predicate);
        if (shape.isPresent()) {
            return shape;
        }
        Direction.Axis other = axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
        return Optional.of(new ChimericPortalShape(level, pos, other)).filter(predicate);
    }

    public ChimericPortalShape(LevelAccessor level, BlockPos pos, Direction.Axis axis) {
        this.level = level;
        this.axis = axis;
        this.rightDir = axis == Direction.Axis.X ? Direction.WEST : Direction.SOUTH;
        this.bottomLeft = this.calculateBottomLeft(pos);
        if (this.bottomLeft == null) {
            this.bottomLeft = pos;
            this.width = 1;
            this.height = 1;
        } else {
            this.width = this.calculateWidth();
            if (this.width > 0) {
                this.height = this.calculateHeight();
            }
        }
    }

    public static boolean isFrame(BlockState state) {
        return state.is(ModBlocks.DARK_FROST_BRICKS);
    }

    private static boolean isEmpty(BlockState state) {
        return state.isAir() || state.is(BlockTags.FIRE) || state.is(ChimericPortals.PORTAL);
    }

    @Nullable
    private BlockPos calculateBottomLeft(BlockPos pos) {
        int minY = Math.max(this.level.getMinBuildHeight(), pos.getY() - MAX_HEIGHT);
        while (pos.getY() > minY && isEmpty(this.level.getBlockState(pos.below()))) {
            pos = pos.below();
        }
        Direction left = this.rightDir.getOpposite();
        int distance = this.getDistanceUntilEdgeAboveFrame(pos, left) - 1;
        return distance < 0 ? null : pos.relative(left, distance);
    }

    private int calculateWidth() {
        int distance = this.getDistanceUntilEdgeAboveFrame(this.bottomLeft, this.rightDir);
        return distance >= MIN_WIDTH && distance <= MAX_WIDTH ? distance : 0;
    }

    private int getDistanceUntilEdgeAboveFrame(BlockPos pos, Direction direction) {
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int i = 0; i <= MAX_WIDTH; i++) {
            cursor.set(pos).move(direction, i);
            BlockState state = this.level.getBlockState(cursor);
            if (!isEmpty(state)) {
                if (isFrame(state)) {
                    return i;
                }
                break;
            }
            BlockState below = this.level.getBlockState(cursor.move(Direction.DOWN));
            if (!isFrame(below)) {
                break;
            }
        }
        return 0;
    }

    private int calculateHeight() {
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        int distance = this.getDistanceUntilTop(cursor);
        return distance >= MIN_HEIGHT && distance <= MAX_HEIGHT && this.hasTopFrame(cursor, distance) ? distance : 0;
    }

    private boolean hasTopFrame(BlockPos.MutableBlockPos cursor, int distance) {
        for (int i = 0; i < this.width; i++) {
            cursor.set(this.bottomLeft).move(Direction.UP, distance).move(this.rightDir, i);
            if (!isFrame(this.level.getBlockState(cursor))) {
                return false;
            }
        }
        return true;
    }

    private int getDistanceUntilTop(BlockPos.MutableBlockPos cursor) {
        for (int i = 0; i < MAX_HEIGHT; i++) {
            cursor.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, -1);
            if (!isFrame(this.level.getBlockState(cursor))) {
                return i;
            }
            cursor.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, this.width);
            if (!isFrame(this.level.getBlockState(cursor))) {
                return i;
            }
            for (int j = 0; j < this.width; j++) {
                cursor.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, j);
                BlockState state = this.level.getBlockState(cursor);
                if (!isEmpty(state)) {
                    return i;
                }
                if (state.is(ChimericPortals.PORTAL)) {
                    this.numPortalBlocks++;
                }
            }
        }
        return MAX_HEIGHT;
    }

    public boolean isValid() {
        return this.bottomLeft != null && this.width >= MIN_WIDTH && this.width <= MAX_WIDTH && this.height >= MIN_HEIGHT && this.height <= MAX_HEIGHT;
    }

    public boolean isComplete() {
        return this.isValid() && this.numPortalBlocks == this.width * this.height;
    }

    public void createPortalBlocks() {
        BlockState portal = ChimericPortals.PORTAL.defaultBlockState().setValue(ChimericPortalBlock.AXIS, this.axis);
        BlockPos.betweenClosed(this.bottomLeft, this.bottomLeft.relative(Direction.UP, this.height - 1).relative(this.rightDir, this.width - 1))
                .forEach(pos -> this.level.setBlock(pos, portal, 18));
    }
}
