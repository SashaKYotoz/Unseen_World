
package net.sashakyotoz.unseenworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModBlocks;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModItems;
import org.checkerframework.checker.units.qual.A;

public class MisteryFlowerBlock extends CropBlock {
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{Block.box(3.0D, 0.0D, 3.0D, 13.0D, 6.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 6.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 12.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 15.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D), Block.box(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D)};

	public MisteryFlowerBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.GRASS)
				.instabreak().lightLevel(s -> 1).noLootTable().noCollission());
		this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
	}

	@Override
	public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 40;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 60;
	}
	public VoxelShape getShape(BlockState pState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {
		return SHAPE_BY_AGE[this.getAge(pState)];
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (state.getValue(AGE) > 3){
			player.spawnAtLocation(UnseenWorldModItems.PURPLE_BERRIES.get());
			level.setBlock(pos,this.getStateForAge(getAge(state) -1),2);
		}
		return state.getValue(AGE) > 0 ? InteractionResult.SUCCESS : super.use(state, level, pos, player, hand, hitResult);
	}
	public BlockState getStateForAge(int i) {
		return this.defaultBlockState().setValue(this.getAgeProperty(), i);
	}
	@Override
	protected ItemLike getBaseSeedId() {
		return new ItemStack(UnseenWorldModBlocks.MISTERY_CROP_FLOWER.get()).getItem();
	}
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}

	@Override
	public boolean mayPlaceOn(BlockState groundState, BlockGetter worldIn, BlockPos pos) {
		return groundState.is(Blocks.FARMLAND);
	}

	@Override
	public boolean canSurvive(BlockState blockstate, LevelReader worldIn, BlockPos pos) {
		BlockPos blockpos = pos.below();
		BlockState groundState = worldIn.getBlockState(blockpos);
		return this.mayPlaceOn(groundState, worldIn, blockpos);
	}
}
