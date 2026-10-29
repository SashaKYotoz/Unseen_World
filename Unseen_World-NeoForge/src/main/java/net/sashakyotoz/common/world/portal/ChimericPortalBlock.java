package net.sashakyotoz.common.world.portal;

import com.mojang.serialization.MapCodec;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.world.ModDimensions;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ChimericPortalBlock extends Block implements Portal {
    public static final MapCodec<ChimericPortalBlock> CODEC = simpleCodec(ChimericPortalBlock::new);
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    private static final VoxelShape X_AXIS_AABB = Block.box(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
    private static final VoxelShape Z_AXIS_AABB = Block.box(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);

    public ChimericPortalBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(AXIS) == Direction.Axis.Z ? Z_AXIS_AABB : X_AXIS_AABB;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        Direction.Axis updateAxis = direction.getAxis();
        Direction.Axis axis = state.getValue(AXIS);
        boolean sideways = axis != updateAxis && updateAxis.isHorizontal();
        if (!sideways && !neighborState.is(this) && !new ChimericPortalShape(level, pos, axis).isComplete()) {
            return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity.canUsePortal(false)) {
            entity.setAsInsidePortal(this, pos);
        }
    }

    @Override
    public int getPortalTransitionTime(ServerLevel level, Entity entity) {
        if (entity instanceof Player player) {
            return Math.max(1, level.getGameRules().getInt(player.getAbilities().invulnerable
                    ? GameRules.RULE_PLAYERS_NETHER_PORTAL_CREATIVE_DELAY
                    : GameRules.RULE_PLAYERS_NETHER_PORTAL_DEFAULT_DELAY));
        }
        return 0;
    }

    @Nullable
    @Override
    public DimensionTransition getPortalDestination(ServerLevel level, Entity entity, BlockPos pos) {
        ResourceKey<Level> destinationKey = level.dimension() == ModDimensions.CHIMERIC_DARKNESS_LEVEL_KEY ? Level.OVERWORLD : ModDimensions.CHIMERIC_DARKNESS_LEVEL_KEY;
        ServerLevel destination = level.getServer().getLevel(destinationKey);
        if (destination == null) {
            return null;
        }
        WorldBorder border = destination.getWorldBorder();
        double scale = DimensionType.getTeleportationScale(level.dimensionType(), destination.dimensionType());
        boolean compressed = destination.dimensionType().coordinateScale() > level.dimensionType().coordinateScale();
        BlockPos target = border.clampToBounds(entity.getX() * scale, entity.getY(), entity.getZ() * scale);
        return this.getExitPortal(destination, entity, pos, target, compressed, border);
    }

    @Nullable
    private DimensionTransition getExitPortal(ServerLevel destination, Entity entity, BlockPos entryPos, BlockPos target, boolean compressed, WorldBorder border) {
        Optional<BlockPos> existing = ChimericPortalForcer.findClosestPortalPosition(destination, target, compressed, border);
        BlockUtil.FoundRectangle rectangle;
        DimensionTransition.PostDimensionTransition postTransition;
        if (existing.isPresent()) {
            BlockPos found = existing.get();
            BlockState foundState = destination.getBlockState(found);
            rectangle = BlockUtil.getLargestRectangleAround(found, foundState.getValue(BlockStateProperties.HORIZONTAL_AXIS), 21, Direction.Axis.Y, 21,
                    candidate -> destination.getBlockState(candidate) == foundState);
            postTransition = DimensionTransition.PLAY_PORTAL_SOUND.then(transitioned -> transitioned.placePortalTicket(found));
        } else {
            Direction.Axis axis = entity.level().getBlockState(entryPos).getOptionalValue(AXIS).orElse(Direction.Axis.X);
            Optional<BlockUtil.FoundRectangle> created = ChimericPortalForcer.createPortal(destination, target, axis);
            if (created.isEmpty()) {
                UnseenWorld.log().error("Unable to create a Chimeric Darkness portal, likely target out of worldborder");
                return null;
            }
            rectangle = created.get();
            postTransition = DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET);
        }
        return transitionFromExit(entity, entryPos, rectangle, destination, postTransition);
    }

    private static DimensionTransition transitionFromExit(Entity entity, BlockPos entryPos, BlockUtil.FoundRectangle exit, ServerLevel destination, DimensionTransition.PostDimensionTransition postTransition) {
        BlockState entryState = entity.level().getBlockState(entryPos);
        Direction.Axis axis;
        Vec3 relative;
        if (entryState.hasProperty(BlockStateProperties.HORIZONTAL_AXIS)) {
            axis = entryState.getValue(BlockStateProperties.HORIZONTAL_AXIS);
            BlockUtil.FoundRectangle entry = BlockUtil.getLargestRectangleAround(entryPos, axis, 21, Direction.Axis.Y, 21,
                    candidate -> entity.level().getBlockState(candidate) == entryState);
            relative = entity.getRelativePortalPosition(axis, entry);
        } else {
            axis = Direction.Axis.X;
            relative = new Vec3(0.5D, 0.0D, 0.0D);
        }
        return createTransition(destination, exit, axis, relative, entity, entity.getDeltaMovement(), entity.getYRot(), entity.getXRot(), postTransition);
    }

    private static DimensionTransition createTransition(ServerLevel destination, BlockUtil.FoundRectangle exit, Direction.Axis entryAxis, Vec3 relative, Entity entity,
                                                        Vec3 motion, float yRot, float xRot, DimensionTransition.PostDimensionTransition postTransition) {
        BlockPos corner = exit.minCorner;
        Direction.Axis exitAxis = destination.getBlockState(corner).getOptionalValue(BlockStateProperties.HORIZONTAL_AXIS).orElse(Direction.Axis.X);
        double width = exit.axis1Size;
        double height = exit.axis2Size;
        EntityDimensions dimensions = entity.getDimensions(entity.getPose());
        int rotation = entryAxis == exitAxis ? 0 : 90;
        Vec3 rotatedMotion = entryAxis == exitAxis ? motion : new Vec3(motion.z, motion.y, -motion.x);
        double along = dimensions.width() / 2.0D + (width - dimensions.width()) * relative.x();
        double up = (height - dimensions.height()) * relative.y();
        double across = 0.5D + relative.z();
        boolean xAxis = exitAxis == Direction.Axis.X;
        Vec3 position = new Vec3(corner.getX() + (xAxis ? along : across), corner.getY() + up, corner.getZ() + (xAxis ? across : along));
        Vec3 safe = PortalShape.findCollisionFreePosition(position, destination, entity, dimensions);
        return new DimensionTransition(destination, safe, rotatedMotion, yRot + rotation, xRot, postTransition);
    }

    @Override
    public Portal.Transition getLocalTransition() {
        return Portal.Transition.CONFUSION;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(100) == 0) {
            level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
        }
        for (int i = 0; i < 4; i++) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + random.nextDouble();
            double z = pos.getZ() + random.nextDouble();
            double dx = (random.nextFloat() - 0.5D) * 0.5D;
            double dy = (random.nextFloat() - 0.5D) * 0.5D;
            double dz = (random.nextFloat() - 0.5D) * 0.5D;
            int side = random.nextInt(2) * 2 - 1;
            if (!level.getBlockState(pos.west()).is(this) && !level.getBlockState(pos.east()).is(this)) {
                x = pos.getX() + 0.5D + 0.25D * side;
                dx = random.nextFloat() * 2.0F * side;
            } else {
                z = pos.getZ() + 0.5D + 0.25D * side;
                dz = random.nextFloat() * 2.0F * side;
            }
            level.addParticle(ParticleTypes.REVERSE_PORTAL, x, y, z, dx * 0.1D, dy * 0.1D, dz * 0.1D);
        }
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return ItemStack.EMPTY;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        if (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90) {
            return state.setValue(AXIS, state.getValue(AXIS) == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X);
        }
        return state;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }
}
