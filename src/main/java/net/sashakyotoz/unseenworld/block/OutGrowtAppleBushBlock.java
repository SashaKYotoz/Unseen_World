
package net.sashakyotoz.unseenworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModItems;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModTags;
import org.jetbrains.annotations.Nullable;

public class OutGrowtAppleBushBlock extends BushBlock implements BonemealableBlock {
	protected static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);
	public static final IntegerProperty AGE = BlockStateProperties.AGE_1;

	public OutGrowtAppleBushBlock() {
		super(BlockBehaviour.Properties.copy(Blocks.PEONY).randomTicks().sound(SoundType.NETHER_WART).instabreak().noCollission());
		this.registerDefaultState(this.stateDefinition.any().setValue(this.getAgeProperty(), 0));
	}
	protected IntegerProperty getAgeProperty() {
		return AGE;
	}
	public int getAge(BlockState p_52306_) {
		return p_52306_.getValue(this.getAgeProperty());
	}
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		Vec3 vec3 = state.getOffset(getter, pos);
		return SHAPE.move(vec3.x, vec3.y, vec3.z);
	}
	public BlockState getStateForAge(int i) {
		return this.defaultBlockState().setValue(this.getAgeProperty(), i);
	}
	public final boolean isMaxAge(BlockState state) {
		return this.getAge(state) > 0;
	}

	public boolean isRandomlyTicking(BlockState state) {
		return !this.isMaxAge(state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}
	@Override
	public boolean mayPlaceOn(BlockState groundState, BlockGetter worldIn, BlockPos pos) {
		return groundState.is(UnseenWorldModTags.Blocks.DIRT_THE_DARKNESS) || groundState.is(Blocks.GRASS_BLOCK) || groundState.is(Blocks.MYCELIUM) || groundState.is(Blocks.MOSS_BLOCK);
	}

	@Override
	public boolean canSurvive(BlockState blockstate, LevelReader worldIn, BlockPos pos) {
		BlockPos blockpos = pos.below();
		BlockState groundState = worldIn.getBlockState(blockpos);
		return this.mayPlaceOn(groundState, worldIn, blockpos);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource source) {
		if (!level.isAreaLoaded(pos, 1)) return;
		if (level.getRawBrightness(pos, 0) >= 3) {
			int i = this.getAge(state);
			if (i < 1) {
				if (Math.random() > 0.75) {
					level.setBlock(pos, this.getStateForAge(i + 1), 2);
				}
			}
		}
	}

	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity entity, ItemStack p_49832_) {
		super.playerDestroy(level, player, pos, state, entity, p_49832_);
		if (isMaxAge(state)) {
			for (int i = 0; i < (int) Mth.nextDouble(RandomSource.create(), 1, 4); i++) {
				player.spawnAtLocation(new ItemStack(UnseenWorldModItems.OUTGROWTH_APPLE.get()));
			}
		}
	}

	public boolean isValidBonemealTarget(LevelReader reader, BlockPos p_52259_, BlockState state, boolean p_52261_) {
		return !this.isMaxAge(state);
	}

	public boolean isBonemealSuccess(Level level, RandomSource p_221046_, BlockPos p_221047_, BlockState p_221048_) {
		return true;
	}

	public void performBonemeal(ServerLevel level, RandomSource source, BlockPos pos, BlockState state) {
		level.setBlock(pos, this.getStateForAge(getAge(state) + 1), 2);
	}
}
