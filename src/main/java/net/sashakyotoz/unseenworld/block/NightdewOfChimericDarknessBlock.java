
package net.sashakyotoz.unseenworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModBlocks;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModItems;

import java.util.Collections;
import java.util.List;

public class NightdewOfChimericDarknessBlock extends Block implements SimpleWaterloggedBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public NightdewOfChimericDarknessBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.CACTUS).sound(SoundType.SWEET_BERRY_BUSH).strength(1f, 1f).noOcclusion().randomTicks().isRedstoneConductor((bs, br, bp) -> false));
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false).setValue(AGE, 0));
    }

    @Override
    public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidstate) {
        return true;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return state.getFluidState().isEmpty();
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 2;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return box(2, 0, 2, 14, 16, 14);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
        builder.add(AGE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean flag = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(WATERLOGGED, flag);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
        if (player.getInventory().getSelected().getItem() instanceof HoeItem tieredItem)
            return tieredItem.getTier().getLevel() >= 1;
        return false;
    }

    @Override
    public void randomTick(BlockState blockstate, ServerLevel world, BlockPos pos, RandomSource random) {
        super.tick(blockstate, world, pos, random);
        if (world.getBlockState(pos.below()).getBlock() == Blocks.AIR) {
            Block.dropResources(world.getBlockState(pos), world, pos, null);
            world.destroyBlock(pos, false);
        }
        if (blockstate.getBlock().getStateDefinition().getProperty("age") instanceof IntegerProperty property) {
            if (blockstate.getValue(property) == 0){
                if (Math.random() < 0.05) {
                    if (property.getPossibleValues().contains(1))
                        world.setBlock(pos, blockstate.setValue(property, 1), 3);
                }
            }else{
                if (Math.random() < 0.05 && (world.getBlockState(pos.above()).getBlock() == Blocks.AIR
                        && !((world.getBlockState(pos.below()).getBlock() == UnseenWorldModBlocks.NIGHTDEW_OF_CHIMERIC_DARKNESS.get()
                        && (world.getBlockState(pos.below(2)).getBlock() == UnseenWorldModBlocks.NIGHTDEW_OF_CHIMERIC_DARKNESS.get()))))) {
                    world.setBlock(pos.above(), UnseenWorldModBlocks.NIGHTDEW_OF_CHIMERIC_DARKNESS.get().defaultBlockState(), 3);
                }
            }
        }
    }

    @Override
    public InteractionResult use(BlockState blockstate, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        super.use(blockstate, world, pos, player, hand, hit);
        if (blockstate.getBlock().getStateDefinition().getProperty("age") instanceof IntegerProperty property && blockstate.getValue(property) == 1
                && player.getMainHandItem().is(Items.GLASS_BOTTLE)) {
            ItemStack stack = new ItemStack(Items.GLASS_BOTTLE);
            player.getInventory().clearOrCountMatchingItems(p -> stack.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
            player.spawnAtLocation(new ItemStack(UnseenWorldModItems.NIGHTDEW_NECTAR_BOTTLE.get()));
            BlockState blockState = world.getBlockState(pos);
            if (property.getPossibleValues().contains(0))
                world.setBlock(pos, blockState.setValue(property, 0), 3);
        }
        return InteractionResult.SUCCESS;
    }
}
