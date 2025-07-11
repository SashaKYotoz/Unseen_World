
package net.sashakyotoz.unseenworld.block;

import com.google.common.annotations.VisibleForTesting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sashakyotoz.unseenworld.registries.UnseenWorldBlocks;
import net.sashakyotoz.unseenworld.registries.UnseenWorldFluids;
import net.sashakyotoz.unseenworld.registries.UnseenWorldTags;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class DripstoneOfAmethystOvergrowthBlock extends Block implements Fallable, SimpleWaterloggedBlock {
    public static final DirectionProperty TIP_DIRECTION = BlockStateProperties.VERTICAL_DIRECTION;
    public static final EnumProperty<DripstoneThickness> THICKNESS = BlockStateProperties.DRIPSTONE_THICKNESS;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape TIP_MERGE_SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 16.0D, 11.0D);
    private static final VoxelShape TIP_SHAPE_UP = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 11.0D, 11.0D);
    private static final VoxelShape TIP_SHAPE_DOWN = Block.box(5.0D, 5.0D, 5.0D, 11.0D, 16.0D, 11.0D);
    private static final VoxelShape FRUSTUM_SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    private static final VoxelShape MIDDLE_SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D);
    private static final VoxelShape BASE_SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    private static final VoxelShape REQUIRED_SPACE_TO_DRIP_THROUGH_NON_SOLID_BLOCK = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);

    public DripstoneOfAmethystOvergrowthBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.DRIPSTONE_BLOCK).sound(SoundType.LARGE_AMETHYST_BUD).strength(1.5f, 7.5f).lightLevel(s -> 8).requiresCorrectToolForDrops().noOcclusion().randomTicks()
                .isRedstoneConductor((bs, br, bp) -> false));
        this.registerDefaultState(this.stateDefinition.any().setValue(TIP_DIRECTION, Direction.UP).setValue(THICKNESS, DripstoneThickness.TIP).setValue(WATERLOGGED, Boolean.FALSE));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_154157_) {
        p_154157_.add(TIP_DIRECTION, THICKNESS, WATERLOGGED);
    }

    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos p_154139_) {
        return isValidPointedDripstonePlacement(reader, p_154139_, state.getValue(TIP_DIRECTION));
    }

    public BlockState updateShape(BlockState state, Direction direction1, BlockState state1, LevelAccessor accessor, BlockPos pos, BlockPos p_154152_) {
        if (state.getValue(WATERLOGGED)) {
            if (accessor.getFluidState(pos).is(Fluids.WATER)){
                accessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(accessor));
            }else{
                accessor.scheduleTick(pos, UnseenWorldFluids.DARK_WATER.get(), UnseenWorldFluids.DARK_WATER.get().getTickDelay(accessor));
            }
        }

        if (direction1 != Direction.UP && direction1 != Direction.DOWN) {
            return state;
        } else {
            Direction direction = state.getValue(TIP_DIRECTION);
            if (direction == Direction.DOWN && accessor.getBlockTicks().hasScheduledTick(pos, this)) {
                return state;
            } else if (direction1 == direction.getOpposite() && !this.canSurvive(state, accessor, pos)) {
                if (direction == Direction.DOWN) {
                    accessor.scheduleTick(pos, this, 2);
                } else {
                    accessor.scheduleTick(pos, this, 1);
                }

                return state;
            } else {
                boolean flag = state.getValue(THICKNESS) == DripstoneThickness.TIP_MERGE;
                DripstoneThickness dripstonethickness = calculateDripstoneThickness(accessor, pos, direction, flag);
                return state.setValue(THICKNESS, dripstonethickness);
            }
        }
    }

    public void onProjectileHit(Level level, BlockState state, BlockHitResult p_154044_, Projectile interact) {
        BlockPos blockpos = p_154044_.getBlockPos();
        if (!level.isClientSide && interact.mayInteract(level, blockpos) && interact instanceof ThrownTrident && interact.getDeltaMovement().length() > 0.6D) {
            level.destroyBlock(blockpos, true);
        }

    }

    public void fallOn(Level level, BlockState p_154048_, BlockPos p_154049_, Entity p_154050_, float p_154051_) {
        if (p_154048_.getValue(TIP_DIRECTION) == Direction.UP && p_154048_.getValue(THICKNESS) == DripstoneThickness.TIP) {
            p_154050_.causeFallDamage(p_154051_ + 2.0F, 2.0F, level.damageSources().stalagmite());
        } else {
            super.fallOn(level, p_154048_, p_154049_, p_154050_, p_154051_);
        }

    }

    public void animateTick(BlockState p_221870_, Level p_221871_, BlockPos p_221872_, RandomSource p_221873_) {
        if (canDrip(p_221870_)) {
            float f = p_221873_.nextFloat();
            if (!(f > 0.12F)) {
                getFluidAboveStalactite(p_221871_, p_221872_, p_221870_).filter((p_221848_) -> f < 0.02F || canFillCauldron(p_221848_.fluid)).ifPresent((p_221881_) -> spawnDripParticle(p_221871_, p_221872_, p_221870_, p_221881_.fluid));
            }
        }
    }

    public void tick(BlockState p_221865_, ServerLevel p_221866_, BlockPos p_221867_, RandomSource p_221868_) {
        if (isStalagmite(p_221865_) && !this.canSurvive(p_221865_, p_221866_, p_221867_)) {
            p_221866_.destroyBlock(p_221867_, true);
        } else {
            spawnFallingStalactite(p_221865_, p_221866_, p_221867_);
        }

    }

    public void randomTick(BlockState p_221883_, ServerLevel p_221884_, BlockPos p_221885_, RandomSource p_221886_) {
        maybeTransferFluid(p_221883_, p_221884_, p_221885_, p_221886_.nextFloat());
        if (p_221886_.nextFloat() < 0.011377778F && isStalactiteStartPos(p_221883_, p_221884_, p_221885_)) {
            growStalactiteOrStalagmiteIfPossible(p_221883_, p_221884_, p_221885_, p_221886_);
        }

    }

    @VisibleForTesting
    public static void maybeTransferFluid(BlockState p_221860_, ServerLevel p_221861_, BlockPos p_221862_, float p_221863_) {
        if (!(p_221863_ > 0.17578125F)) {
            if (isStalactiteStartPos(p_221860_, p_221861_, p_221862_)) {
                Optional<DripstoneOfAmethystOvergrowthBlock.FluidInfo> optional = getFluidAboveStalactite(p_221861_, p_221862_, p_221860_);
                if (optional.isPresent()) {
                    Fluid fluid = (optional.get()).fluid;
                    float f;
                    if (fluid == Fluids.WATER) {
                        f = 0.17578125F;
                    } else {
                        if (fluid != Fluids.LAVA) {
                            return;
                        }

                        f = 0.05859375F;
                    }

                    if (!(p_221863_ >= f)) {
                        BlockPos blockpos = findTip(p_221860_, p_221861_, p_221862_, 11, false);
                        if (blockpos != null) {
                            if ((optional.get()).sourceState.is(Blocks.MUD) && fluid == Fluids.WATER) {
                                BlockState blockstate1 = Blocks.CLAY.defaultBlockState();
                                p_221861_.setBlockAndUpdate((optional.get()).pos, blockstate1);
                                Block.pushEntitiesUp((optional.get()).sourceState, blockstate1, p_221861_, (optional.get()).pos);
                                p_221861_.gameEvent(GameEvent.BLOCK_CHANGE, (optional.get()).pos, GameEvent.Context.of(blockstate1));
                                p_221861_.levelEvent(1504, blockpos, 0);
                            } else {
                                BlockPos blockpos1 = findFillableCauldronBelowStalactiteTip(p_221861_, blockpos, fluid);
                                if (blockpos1 != null) {
                                    p_221861_.levelEvent(1504, blockpos, 0);
                                    int i = blockpos.getY() - blockpos1.getY();
                                    int j = 50 + i;
                                    BlockState blockstate = p_221861_.getBlockState(blockpos1);
                                    p_221861_.scheduleTick(blockpos1, blockstate.getBlock(), j);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public PushReaction getPistonPushReaction(BlockState p_154237_) {
        return PushReaction.DESTROY;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_154040_) {
        LevelAccessor levelaccessor = p_154040_.getLevel();
        BlockPos blockpos = p_154040_.getClickedPos();
        Direction direction = p_154040_.getNearestLookingVerticalDirection().getOpposite();
        Direction direction1 = calculateTipDirection(levelaccessor, blockpos, direction);
        if (direction1 == null) {
            return null;
        } else {
            boolean flag = !p_154040_.isSecondaryUseActive();
            DripstoneThickness dripstonethickness = calculateDripstoneThickness(levelaccessor, blockpos, direction1, flag);
            return dripstonethickness == null ? null : this.defaultBlockState().setValue(TIP_DIRECTION, direction1).setValue(THICKNESS, dripstonethickness).setValue(WATERLOGGED, (levelaccessor.getFluidState(blockpos).getType().isSame(Fluids.WATER) || levelaccessor.getFluidState(blockpos).getType().isSame(UnseenWorldFluids.DARK_WATER.get())));
        }
    }

    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    public VoxelShape getOcclusionShape(BlockState state, BlockGetter p_154171_, BlockPos p_154172_) {
        return Shapes.empty();
    }

    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext p_154120_) {
        DripstoneThickness dripstonethickness = state.getValue(THICKNESS);
        VoxelShape voxelshape;
        if (dripstonethickness == DripstoneThickness.TIP_MERGE) {
            voxelshape = TIP_MERGE_SHAPE;
        } else if (dripstonethickness == DripstoneThickness.TIP) {
            if (state.getValue(TIP_DIRECTION) == Direction.DOWN) {
                voxelshape = TIP_SHAPE_DOWN;
            } else {
                voxelshape = TIP_SHAPE_UP;
            }
        } else if (dripstonethickness == DripstoneThickness.FRUSTUM) {
            voxelshape = FRUSTUM_SHAPE;
        } else if (dripstonethickness == DripstoneThickness.MIDDLE) {
            voxelshape = MIDDLE_SHAPE;
        } else {
            voxelshape = BASE_SHAPE;
        }

        Vec3 vec3 = state.getOffset(getter, pos);
        return voxelshape.move(vec3.x, 0.0D, vec3.z);
    }

    public boolean isCollisionShapeFullBlock(BlockState state, BlockGetter p_181236_, BlockPos p_181237_) {
        return false;
    }

    public float getMaxHorizontalOffset() {
        return 0.125F;
    }

    public void onBrokenAfterFall(Level level, BlockPos p_154060_, FallingBlockEntity p_154061_) {
        if (!p_154061_.isSilent()) {
            level.levelEvent(1045, p_154060_, 0);
        }

    }

    public DamageSource getFallDamageSource(Entity p_254432_) {
        return p_254432_.damageSources().fallingStalactite(p_254432_);
    }

    private static void spawnFallingStalactite(BlockState p_154098_, ServerLevel p_154099_, BlockPos p_154100_) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = p_154100_.mutable();

        for(BlockState blockstate = p_154098_; isStalactite(blockstate); blockstate = p_154099_.getBlockState(blockpos$mutableblockpos)) {
            FallingBlockEntity fallingblockentity = FallingBlockEntity.fall(p_154099_, blockpos$mutableblockpos, blockstate);
            if (isTip(blockstate, true)) {
                int i = Math.max(1 + p_154100_.getY() - blockpos$mutableblockpos.getY(), 6);
                float f = (float) i;
                fallingblockentity.setHurtsEntities(f, 40);
                break;
            }

            blockpos$mutableblockpos.move(Direction.DOWN);
        }

    }

    @VisibleForTesting
    public static void growStalactiteOrStalagmiteIfPossible(BlockState p_221888_, ServerLevel p_221889_, BlockPos p_221890_, RandomSource p_221891_) {
        BlockState blockstate = p_221889_.getBlockState(p_221890_.above(1));
        BlockState blockstate1 = p_221889_.getBlockState(p_221890_.above(2));
        if (canGrow(blockstate, blockstate1)) {
            BlockPos blockpos = findTip(p_221888_, p_221889_, p_221890_, 7, false);
            if (blockpos != null) {
                BlockState blockstate2 = p_221889_.getBlockState(blockpos);
                if (canDrip(blockstate2) && canTipGrow(blockstate2, p_221889_, blockpos)) {
                    if (p_221891_.nextBoolean()) {
                        grow(p_221889_, blockpos, Direction.DOWN);
                    } else {
                        growStalagmiteBelow(p_221889_, blockpos);
                    }

                }
            }
        }
    }

    private static void growStalagmiteBelow(ServerLevel p_154033_, BlockPos p_154034_) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = p_154034_.mutable();

        for(int i = 0; i < 10; ++i) {
            blockpos$mutableblockpos.move(Direction.DOWN);
            BlockState blockstate = p_154033_.getBlockState(blockpos$mutableblockpos);
            if (!blockstate.getFluidState().isEmpty()) {
                return;
            }

            if (isUnmergedTipWithDirection(blockstate, Direction.UP) && canTipGrow(blockstate, p_154033_, blockpos$mutableblockpos)) {
                grow(p_154033_, blockpos$mutableblockpos, Direction.UP);
                return;
            }

            if (isValidPointedDripstonePlacement(p_154033_, blockpos$mutableblockpos, Direction.UP) && !p_154033_.isWaterAt(blockpos$mutableblockpos.below())) {
                grow(p_154033_, blockpos$mutableblockpos.below(), Direction.UP);
                return;
            }

            if (!canDripThrough(p_154033_, blockpos$mutableblockpos, blockstate)) {
                return;
            }
        }

    }

    private static void grow(ServerLevel level, BlockPos pos, Direction direction) {
        BlockPos blockpos = pos.relative(direction);
        BlockState blockstate = level.getBlockState(blockpos);
        if (isUnmergedTipWithDirection(blockstate, direction.getOpposite())) {
            createMergedTips(blockstate, level, blockpos);
        } else if (blockstate.isAir() || blockstate.is(Blocks.WATER)) {
            createDripstone(level, blockpos, direction, DripstoneThickness.TIP);
        }

    }

    private static void createDripstone(LevelAccessor state, BlockPos pos, Direction direction, DripstoneThickness thickness) {
        BlockState blockstate = UnseenWorldBlocks.DRIPSTONE_OF_AMETHYST_OVERGROWTH.get().defaultBlockState().setValue(TIP_DIRECTION, direction).setValue(THICKNESS, thickness).setValue(WATERLOGGED, (state.getFluidState(pos).getType().isSame(Fluids.WATER) || (state.getFluidState(pos).getType().isSame(UnseenWorldFluids.DARK_WATER.get()))));
        state.setBlock(pos, blockstate, 3);
    }

    private static void createMergedTips(BlockState state, LevelAccessor p_154232_, BlockPos p_154233_) {
        BlockPos blockpos;
        BlockPos blockpos1;
        if (state.getValue(TIP_DIRECTION) == Direction.UP) {
            blockpos1 = p_154233_;
            blockpos = p_154233_.above();
        } else {
            blockpos = p_154233_;
            blockpos1 = p_154233_.below();
        }

        createDripstone(p_154232_, blockpos, Direction.DOWN, DripstoneThickness.TIP_MERGE);
        createDripstone(p_154232_, blockpos1, Direction.UP, DripstoneThickness.TIP_MERGE);
    }

    private static void spawnDripParticle(Level p_154072_, BlockPos p_154073_, BlockState p_154074_, Fluid p_154075_) {
        Vec3 vec3 = p_154074_.getOffset(p_154072_, p_154073_);
        double d1 = (double)p_154073_.getX() + 0.5D + vec3.x;
        double d2 = (double)((float)(p_154073_.getY() + 1) - 0.6875F) - 0.0625D;
        double d3 = (double)p_154073_.getZ() + 0.5D + vec3.z;
        Fluid fluid = getDripFluid(p_154072_, p_154075_);
        ParticleOptions particleoptions = fluid.is(FluidTags.LAVA) ? ParticleTypes.DRIPPING_DRIPSTONE_LAVA : ParticleTypes.DRIPPING_DRIPSTONE_WATER;
        p_154072_.addParticle(particleoptions, d1, d2, d3, 0.0D, 0.0D, 0.0D);
    }

    @Nullable
    private static BlockPos findTip(BlockState p_154131_, LevelAccessor p_154132_, BlockPos p_154133_, int p_154134_, boolean p_154135_) {
        if (isTip(p_154131_, p_154135_)) {
            return p_154133_;
        } else {
            Direction direction = p_154131_.getValue(TIP_DIRECTION);
            BiPredicate<BlockPos, BlockState> bipredicate = (p_202023_, p_202024_) -> p_202024_.is(UnseenWorldBlocks.DRIPSTONE_OF_AMETHYST_OVERGROWTH.get()) && p_202024_.getValue(TIP_DIRECTION) == direction;
            return findBlockVertical(p_154132_, p_154133_, direction.getAxisDirection(), bipredicate, (p_154168_) -> isTip(p_154168_, p_154135_), p_154134_).orElse(null);
        }
    }

    @Nullable
    private static Direction calculateTipDirection(LevelReader p_154191_, BlockPos p_154192_, Direction p_154193_) {
        Direction direction;
        if (isValidPointedDripstonePlacement(p_154191_, p_154192_, p_154193_)) {
            direction = p_154193_;
        } else {
            if (!isValidPointedDripstonePlacement(p_154191_, p_154192_, p_154193_.getOpposite())) {
                return null;
            }

            direction = p_154193_.getOpposite();
        }

        return direction;
    }

    private static DripstoneThickness calculateDripstoneThickness(LevelReader p_154093_, BlockPos p_154094_, Direction p_154095_, boolean p_154096_) {
        Direction direction = p_154095_.getOpposite();
        BlockState blockstate = p_154093_.getBlockState(p_154094_.relative(p_154095_));
        if (isPointedDripstoneWithDirection(blockstate, direction)) {
            return !p_154096_ && blockstate.getValue(THICKNESS) != DripstoneThickness.TIP_MERGE ? DripstoneThickness.TIP : DripstoneThickness.TIP_MERGE;
        } else if (!isPointedDripstoneWithDirection(blockstate, p_154095_)) {
            return DripstoneThickness.TIP;
        } else {
            DripstoneThickness dripstonethickness = blockstate.getValue(THICKNESS);
            if (dripstonethickness != DripstoneThickness.TIP && dripstonethickness != DripstoneThickness.TIP_MERGE) {
                BlockState blockstate1 = p_154093_.getBlockState(p_154094_.relative(direction));
                return !isPointedDripstoneWithDirection(blockstate1, p_154095_) ? DripstoneThickness.BASE : DripstoneThickness.MIDDLE;
            } else {
                return DripstoneThickness.FRUSTUM;
            }
        }
    }

    public static boolean canDrip(BlockState p_154239_) {
        return isStalactite(p_154239_) && p_154239_.getValue(THICKNESS) == DripstoneThickness.TIP && !p_154239_.getValue(WATERLOGGED);
    }

    private static boolean canTipGrow(BlockState state, ServerLevel level, BlockPos p_154197_) {
        Direction direction = state.getValue(TIP_DIRECTION);
        BlockPos blockpos = p_154197_.relative(direction);
        BlockState blockstate = level.getBlockState(blockpos);
        if (!blockstate.getFluidState().isEmpty()) {
            return false;
        } else {
            return blockstate.isAir() || isUnmergedTipWithDirection(blockstate, direction.getOpposite());
        }
    }

    private static Optional<BlockPos> findRootBlock(Level level, BlockPos pos, BlockState p_154069_) {
        Direction direction = p_154069_.getValue(TIP_DIRECTION);
        BiPredicate<BlockPos, BlockState> bipredicate = (p_202015_, p_202016_) -> p_202016_.is(Blocks.POINTED_DRIPSTONE) && p_202016_.getValue(TIP_DIRECTION) == direction;
        return findBlockVertical(level, pos, direction.getOpposite().getAxisDirection(), bipredicate, (p_154245_) -> !p_154245_.is(Blocks.POINTED_DRIPSTONE), 11);
    }

    private static boolean isValidPointedDripstonePlacement(LevelReader reader, BlockPos p_154223_, Direction p_154224_) {
        BlockPos blockpos = p_154223_.relative(p_154224_.getOpposite());
        BlockState blockstate = reader.getBlockState(blockpos);
        return blockstate.isFaceSturdy(reader, blockpos, p_154224_) || isPointedDripstoneWithDirection(blockstate, p_154224_);
    }

    private static boolean isTip(BlockState p_154154_, boolean p_154155_) {
        if (!p_154154_.is(UnseenWorldBlocks.DRIPSTONE_OF_AMETHYST_OVERGROWTH.get())) {
            return false;
        } else {
            DripstoneThickness dripstonethickness = p_154154_.getValue(THICKNESS);
            return dripstonethickness == DripstoneThickness.TIP || p_154155_ && dripstonethickness == DripstoneThickness.TIP_MERGE;
        }
    }

    private static boolean isUnmergedTipWithDirection(BlockState state, Direction direction) {
        return isTip(state, false) && state.getValue(TIP_DIRECTION) == direction;
    }

    private static boolean isStalactite(BlockState p_154241_) {
        return isPointedDripstoneWithDirection(p_154241_, Direction.DOWN);
    }

    private static boolean isStalagmite(BlockState p_154243_) {
        return isPointedDripstoneWithDirection(p_154243_, Direction.UP);
    }

    private static boolean isStalactiteStartPos(BlockState p_154204_, LevelReader p_154205_, BlockPos p_154206_) {
        return isStalactite(p_154204_) && !p_154205_.getBlockState(p_154206_.above()).is(UnseenWorldBlocks.DRIPSTONE_OF_AMETHYST_OVERGROWTH.get());
    }

    public boolean isPathfindable(BlockState p_154112_, BlockGetter p_154113_, BlockPos p_154114_, PathComputationType p_154115_) {
        return false;
    }

    private static boolean isPointedDripstoneWithDirection(BlockState p_154208_, Direction p_154209_) {
        return p_154208_.is(UnseenWorldBlocks.DRIPSTONE_OF_AMETHYST_OVERGROWTH.get()) && p_154208_.getValue(TIP_DIRECTION) == p_154209_;
    }

    @Nullable
    private static BlockPos findFillableCauldronBelowStalactiteTip(Level p_154077_, BlockPos p_154078_, Fluid p_154079_) {
        Predicate<BlockState> predicate = (p_154162_) -> p_154162_.getBlock() == Blocks.WATER || p_154162_.getBlock() == Blocks.LAVA;
        BiPredicate<BlockPos, BlockState> bipredicate = (p_202034_, p_202035_) -> canDripThrough(p_154077_, p_202034_, p_202035_);
        return findBlockVertical(p_154077_, p_154078_, Direction.DOWN.getAxisDirection(), bipredicate, predicate, 11).orElse(null);
    }

    private static Optional<DripstoneOfAmethystOvergrowthBlock.FluidInfo> getFluidAboveStalactite(Level level, BlockPos p_154183_, BlockState p_154184_) {
        return !isStalactite(p_154184_) ? Optional.empty() : findRootBlock(level, p_154183_, p_154184_).map((p_221876_) -> {
            BlockPos blockpos = p_221876_.above();
            BlockState blockstate = level.getBlockState(blockpos);
            Fluid fluid;
            if (blockstate.is(Blocks.MUD) && !level.dimensionType().ultraWarm()) {
                fluid = Fluids.WATER;
            } else {
                fluid = level.getFluidState(blockpos).getType();
            }

            return new DripstoneOfAmethystOvergrowthBlock.FluidInfo(blockpos, fluid, blockstate);
        });
    }

    private static boolean canFillCauldron(Fluid fluid) {
        return fluid == Fluids.LAVA || fluid == Fluids.WATER;
    }

    private static boolean canGrow(BlockState state, BlockState blockState) {
        return state.is(UnseenWorldTags.Blocks.DRIPSTONE_OF_AMETHYST_CAN_GROW_ON) && (blockState.is(Blocks.WATER) || blockState.is(UnseenWorldBlocks.DARK_WATER.get())) && blockState.getFluidState().isSource();
    }

    private static Fluid getDripFluid(Level level, Fluid fluid) {
        if (fluid.isSame(Fluids.EMPTY)) {
            return level.dimensionType().ultraWarm() ? Fluids.LAVA : Fluids.WATER;
        } else {
            return fluid;
        }
    }

    private static Optional<BlockPos> findBlockVertical(LevelAccessor p_202007_, BlockPos p_202008_, Direction.AxisDirection p_202009_, BiPredicate<BlockPos, BlockState> p_202010_, Predicate<BlockState> p_202011_, int p_202012_) {
        Direction direction = Direction.get(p_202009_, Direction.Axis.Y);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = p_202008_.mutable();

        for(int i = 1; i < p_202012_; ++i) {
            blockpos$mutableblockpos.move(direction);
            BlockState blockstate = p_202007_.getBlockState(blockpos$mutableblockpos);
            if (p_202011_.test(blockstate)) {
                return Optional.of(blockpos$mutableblockpos.immutable());
            }

            if (p_202007_.isOutsideBuildHeight(blockpos$mutableblockpos.getY()) || !p_202010_.test(blockpos$mutableblockpos, blockstate)) {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }

    private static boolean canDripThrough(BlockGetter getter, BlockPos pos, BlockState state) {
        if (state.isAir()) {
            return true;
        } else if (state.isSolidRender(getter, pos)) {
            return false;
        } else if (!state.getFluidState().isEmpty()) {
            return false;
        } else {
            VoxelShape voxelshape = state.getCollisionShape(getter, pos);
            return !Shapes.joinIsNotEmpty(REQUIRED_SPACE_TO_DRIP_THROUGH_NON_SOLID_BLOCK, voxelshape, BooleanOp.AND);
        }
    }

    record FluidInfo(BlockPos pos, Fluid fluid, BlockState sourceState) {
    }
}