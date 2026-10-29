package net.sashakyotoz.common.entities.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.SwimNodeEvaluator;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class WhaleSwimNavigation extends WaterBoundPathNavigation {
    private static final int PROGRESS_TIMEOUT = 60;
    private static final int SKIP_LOOKAHEAD = 4;
    private static final double SKIP_RANGE_SQR = 64.0D;

    private int lastNodeIndex = -1;
    private int lastProgressTick = 0;

    public WhaleSwimNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    protected PathFinder createPathFinder(int maxVisitedNodes) {
        this.nodeEvaluator = new SwimNodeEvaluator(true);
        return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
    }

    @Override
    protected boolean canUpdatePath() {
        return true;
    }

    @Override
    protected double getGroundY(Vec3 vec) {
        return vec.y;
    }

    @Override
    public boolean isStableDestination(BlockPos pos) {
        return this.level.getFluidState(pos).is(FluidTags.WATER) && this.level.getFluidState(pos.above()).is(FluidTags.WATER);
    }

    @Override
    public boolean moveTo(@Nullable Path path, double speed) {
        boolean started = super.moveTo(path, speed);
        this.lastNodeIndex = -1;
        this.lastProgressTick = this.tick;
        return started;
    }

    @Override
    protected void followThePath() {
        if (this.path == null) {
            return;
        }
        Vec3 position = this.mob.position();
        double horizontalTolerance = Math.max(1.5D, this.mob.getBbWidth() * 0.6D);
        double verticalTolerance = Math.max(1.5D, this.mob.getBbHeight() * 0.75D);

        Vec3 next = this.path.getNextEntityPos(this.mob);
        double dx = position.x - next.x;
        double dz = position.z - next.z;
        if (dx * dx + dz * dz < horizontalTolerance * horizontalTolerance && Math.abs(position.y - next.y) < verticalTolerance) {
            this.path.advance();
        }

        if (!this.path.isDone()) {
            Vec3 center = position.add(0.0D, this.mob.getBbHeight() * 0.5D, 0.0D);
            int last = Math.min(this.path.getNextNodeIndex() + SKIP_LOOKAHEAD, this.path.getNodeCount() - 1);
            for (int i = last; i > this.path.getNextNodeIndex(); i--) {
                Vec3 candidate = this.path.getEntityPosAtNode(this.mob, i);
                if (candidate.distanceToSqr(position) <= SKIP_RANGE_SQR && this.canMoveDirectly(center, candidate.add(0.0D, this.mob.getBbHeight() * 0.5D, 0.0D))) {
                    this.path.setNextNodeIndex(i);
                    break;
                }
            }
        }

        if (this.path.getNextNodeIndex() != this.lastNodeIndex) {
            this.lastNodeIndex = this.path.getNextNodeIndex();
            this.lastProgressTick = this.tick;
        } else if (this.tick - this.lastProgressTick > PROGRESS_TIMEOUT) {
            if (!this.path.isDone() && this.path.getNextNodeIndex() < this.path.getNodeCount() - 1) {
                this.path.advance();
                this.lastProgressTick = this.tick;
            } else {
                this.stop();
                return;
            }
        }

        this.doStuckDetection(position);
    }
}
