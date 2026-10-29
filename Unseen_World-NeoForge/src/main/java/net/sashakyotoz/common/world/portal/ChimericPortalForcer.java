package net.sashakyotoz.common.world.portal;

import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.levelgen.Heightmap;
import net.sashakyotoz.common.blocks.ModBlocks;

import java.util.Comparator;
import java.util.Optional;

public final class ChimericPortalForcer {
    public static final int MIN_PORTAL_Y = 52;
    public static final int MAX_PORTAL_Y = 184;
    private static final int COMPRESSED_SEARCH_RADIUS = 16;
    private static final int WIDE_SEARCH_RADIUS = 128;

    private ChimericPortalForcer() {
    }

    public static Optional<BlockPos> findClosestPortalPosition(ServerLevel level, BlockPos pos, boolean compressed, WorldBorder border) {
        PoiManager poiManager = level.getPoiManager();
        int radius = compressed ? COMPRESSED_SEARCH_RADIUS : WIDE_SEARCH_RADIUS;
        poiManager.ensureLoadedAndValid(level, pos, radius);
        return poiManager.getInSquare(holder -> holder.is(ChimericPortals.PORTAL_POI_KEY), pos, radius, PoiManager.Occupancy.ANY)
                .map(PoiRecord::getPos)
                .filter(border::isWithinBounds)
                .filter(candidate -> level.getBlockState(candidate).hasProperty(BlockStateProperties.HORIZONTAL_AXIS))
                .min(Comparator.<BlockPos>comparingDouble(candidate -> candidate.distSqr(pos)).thenComparingInt(Vec3i::getY));
    }

    public static Optional<BlockUtil.FoundRectangle> createPortal(ServerLevel level, BlockPos origin, Direction.Axis axis) {
        Direction direction = Direction.get(Direction.AxisDirection.POSITIVE, axis);
        double bestDistance = -1.0D;
        BlockPos best = null;
        double fallbackDistance = -1.0D;
        BlockPos fallback = null;
        WorldBorder border = level.getWorldBorder();
        int logicalTop = Math.min(level.getMaxBuildHeight(), level.getMinBuildHeight() + level.getLogicalHeight()) - 1;
        int maxY = Math.min(logicalTop, MAX_PORTAL_Y);
        int minY = Math.max(level.getMinBuildHeight(), MIN_PORTAL_Y);
        BlockPos.MutableBlockPos cursor = origin.mutable();

        for (BlockPos.MutableBlockPos column : BlockPos.spiralAround(origin, 16, Direction.EAST, Direction.SOUTH)) {
            int top = Math.min(maxY, level.getHeight(Heightmap.Types.MOTION_BLOCKING, column.getX(), column.getZ()));
            if (!border.isWithinBounds(column) || !border.isWithinBounds(column.move(direction, 1))) {
                continue;
            }
            column.move(direction.getOpposite(), 1);
            for (int y = top; y >= minY; y--) {
                column.setY(y);
                if (!canPortalReplaceBlock(level, column)) {
                    continue;
                }
                int start = y;
                while (y > minY && canPortalReplaceBlock(level, column.move(Direction.DOWN))) {
                    y--;
                }
                if (y + 4 > maxY) {
                    continue;
                }
                int gap = start - y;
                if (gap > 0 && gap < 3) {
                    continue;
                }
                column.setY(y);
                if (!canHostFrame(level, column, cursor, direction, 0)) {
                    continue;
                }
                double distance = origin.distSqr(column);
                if (canHostFrame(level, column, cursor, direction, -1) && canHostFrame(level, column, cursor, direction, 1)
                        && (bestDistance == -1.0D || bestDistance > distance)) {
                    bestDistance = distance;
                    best = column.immutable();
                }
                if (bestDistance == -1.0D && (fallbackDistance == -1.0D || fallbackDistance > distance)) {
                    fallbackDistance = distance;
                    fallback = column.immutable();
                }
            }
        }

        if (bestDistance == -1.0D && fallbackDistance != -1.0D) {
            best = fallback;
            bestDistance = fallbackDistance;
        }

        BlockState frame = ModBlocks.DARK_FROST_BRICKS.defaultBlockState();
        if (bestDistance == -1.0D) {
            int low = Math.max(minY, level.getMinBuildHeight() + 1);
            int high = maxY - 9;
            if (high < low) {
                return Optional.empty();
            }
            best = border.clampToBounds(new BlockPos(origin.getX() - direction.getStepX(), Mth.clamp(origin.getY(), low, high), origin.getZ() - direction.getStepZ()).immutable());
            Direction side = direction.getClockWise();
            for (int i = -1; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    for (int k = -1; k < 3; k++) {
                        BlockState state = k < 0 ? frame : Blocks.AIR.defaultBlockState();
                        cursor.setWithOffset(best, j * direction.getStepX() + i * side.getStepX(), k, j * direction.getStepZ() + i * side.getStepZ());
                        level.setBlockAndUpdate(cursor, state);
                    }
                }
            }
        }

        for (int i = -1; i < 3; i++) {
            for (int j = -1; j < 4; j++) {
                if (i == -1 || i == 2 || j == -1 || j == 3) {
                    cursor.setWithOffset(best, i * direction.getStepX(), j, i * direction.getStepZ());
                    level.setBlock(cursor, frame, 3);
                }
            }
        }

        BlockState portal = ChimericPortals.PORTAL.defaultBlockState().setValue(ChimericPortalBlock.AXIS, axis);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                cursor.setWithOffset(best, i * direction.getStepX(), j, i * direction.getStepZ());
                level.setBlock(cursor, portal, 18);
            }
        }
        return Optional.of(new BlockUtil.FoundRectangle(best.immutable(), 2, 3));
    }

    private static boolean canPortalReplaceBlock(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.canBeReplaced() && state.getFluidState().isEmpty();
    }

    private static boolean canHostFrame(ServerLevel level, BlockPos origin, BlockPos.MutableBlockPos cursor, Direction direction, int offset) {
        Direction side = direction.getClockWise();
        for (int i = -1; i < 3; i++) {
            for (int j = -1; j < 4; j++) {
                cursor.setWithOffset(origin, direction.getStepX() * i + side.getStepX() * offset, j, direction.getStepZ() * i + side.getStepZ() * offset);
                if (j < 0 && !level.getBlockState(cursor).isSolid()) {
                    return false;
                }
                if (j >= 0 && !canPortalReplaceBlock(level, cursor)) {
                    return false;
                }
            }
        }
        return true;
    }
}
