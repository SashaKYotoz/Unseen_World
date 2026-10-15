package net.sashakyotoz.common.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sashakyotoz.common.blocks.ModBlocks;

public class EffigyPartBlock extends Block {
    public static final IntegerProperty OFFSET_X = IntegerProperty.create("offset_x", 0, 2);
    public static final IntegerProperty OFFSET_Y = IntegerProperty.create("offset_y", 0, EffigyBlock.STATUE_LAYERS);
    public static final IntegerProperty OFFSET_Z = IntegerProperty.create("offset_z", 0, 2);

    public EffigyPartBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(OFFSET_X, 0).setValue(OFFSET_Y, 0).setValue(OFFSET_Z, 0));
    }

    public static BlockState forOffset(int dx, int dy, int dz) {
        return ModBlocks.EFFIGY_PART.defaultBlockState()
                .setValue(OFFSET_X, dx + 1)
                .setValue(OFFSET_Y, dy)
                .setValue(OFFSET_Z, dz + 1);
    }

    public static BlockPos corePos(BlockState state, BlockPos pos) {
        return pos.offset(1 - state.getValue(OFFSET_X), -state.getValue(OFFSET_Y), 1 - state.getValue(OFFSET_Z));
    }

    public static boolean belongsTo(BlockState state, BlockPos pos, BlockPos core) {
        return state.getBlock() instanceof EffigyPartBlock && corePos(state, pos).equals(core);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        BlockState core = level.getBlockState(corePos(state, pos));
        if (!(core.getBlock() instanceof EffigyBlock)) {
            return Shapes.empty();
        }
        return EffigyBlock.slice(core, state.getValue(OFFSET_X) - 1, state.getValue(OFFSET_Y), state.getValue(OFFSET_Z) - 1);
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return level.getBlockState(corePos(state, pos)).getBlock() instanceof EffigyBlock
                ? state
                : Blocks.AIR.defaultBlockState();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockPos core = corePos(state, pos);
        BlockState coreState = level.getBlockState(core);
        if (!(coreState.getBlock() instanceof EffigyBlock)) {
            return InteractionResult.PASS;
        }
        return coreState.use(level, player, hand, hit.withPosition(core));
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockPos core = corePos(state, pos);
        if (!level.isClientSide && level.getBlockState(core).getBlock() instanceof EffigyBlock) {
            level.destroyBlock(core, false, player);
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(ModBlocks.EFFIGY);
    }
}
