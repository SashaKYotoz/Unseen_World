
package net.sashakyotoz.unseenworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sashakyotoz.unseenworld.util.UnseenWorldModBlocks;
import net.sashakyotoz.unseenworld.util.UnseenWorldModItems;
import org.jetbrains.annotations.Nullable;

public class OutGrowtAppleBushBlock extends BushBlock implements BonemealableBlock {
	protected static final float AABB_OFFSET = 3.0F;
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
	public VoxelShape getShape(BlockState p_53517_, BlockGetter p_53518_, BlockPos p_53519_, CollisionContext p_53520_) {
		Vec3 vec3 = p_53517_.getOffset(p_53518_, p_53519_);
		return SHAPE.move(vec3.x, vec3.y, vec3.z);
	}
	public BlockState getStateForAge(int p_52290_) {
		return this.defaultBlockState().setValue(this.getAgeProperty(), p_52290_);
	}
	public final boolean isMaxAge(BlockState p_52308_) {
		return this.getAge(p_52308_) > 0;
	}

	public boolean isRandomlyTicking(BlockState p_52288_) {
		return !this.isMaxAge(p_52288_);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}
	@Override
	public boolean mayPlaceOn(BlockState groundState, BlockGetter worldIn, BlockPos pos) {
		return groundState.is(UnseenWorldModBlocks.DARK_GRASS.get()) || groundState.is(Blocks.GRASS_BLOCK) || groundState.is(Blocks.MYCELIUM) || groundState.is(Blocks.MOSS_BLOCK)
				|| groundState.is(UnseenWorldModBlocks.TEALIVE_LUMINOUS_GRASS_BLOCK.get()) || groundState.is(UnseenWorldModBlocks.GRASS_BLOCK_OF_SHINY_REDLIGHT.get()) || groundState.is(UnseenWorldModBlocks.RED_OOZE.get());
	}

	@Override
	public boolean canSurvive(BlockState blockstate, LevelReader worldIn, BlockPos pos) {
		BlockPos blockpos = pos.below();
		BlockState groundState = worldIn.getBlockState(blockpos);
		return this.mayPlaceOn(groundState, worldIn, blockpos);
	}

	@Override
	public void randomTick(BlockState p_221050_, ServerLevel p_221051_, BlockPos p_221052_, RandomSource p_221053_) {
		if (!p_221051_.isAreaLoaded(p_221052_, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
		if (p_221051_.getRawBrightness(p_221052_, 0) >= 3) {
			int i = this.getAge(p_221050_);
			if (i < 1) {
				if (Math.random() > 0.75) {
					p_221051_.setBlock(p_221052_, this.getStateForAge(i + 1), 2);
				}
			}
		}
	}

	public void playerDestroy(Level p_49827_, Player p_49828_, BlockPos p_49829_, BlockState p_49830_, @Nullable BlockEntity p_49831_, ItemStack p_49832_) {
		super.playerDestroy(p_49827_, p_49828_, p_49829_, p_49830_, p_49831_, p_49832_);
		execute(p_49827_,p_49829_.getX(),p_49829_.getY(),p_49829_.getZ(),p_49830_);
	}

	public void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate) {
		if (isMaxAge(blockstate)) {
			for (int index0 = 0; index0 < (int) Mth.nextDouble(RandomSource.create(), 1, 4); index0++) {
				if (world instanceof ServerLevel _level) {
					ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(UnseenWorldModItems.OUTGROWTHAPPLE.get()));
					entityToSpawn.setPickUpDelay(10);
					_level.addFreshEntity(entityToSpawn);
					world.setBlock(new BlockPos((int)x,(int)y,(int)z), this.getStateForAge(getAge(blockstate) - 1), 2);
				}
			}
		}
	}

	public boolean isValidBonemealTarget(LevelReader p_255715_, BlockPos p_52259_, BlockState p_52260_, boolean p_52261_) {
		return !this.isMaxAge(p_52260_);
	}

	public boolean isBonemealSuccess(Level p_221045_, RandomSource p_221046_, BlockPos p_221047_, BlockState p_221048_) {
		return true;
	}

	public void performBonemeal(ServerLevel p_221040_, RandomSource p_221041_, BlockPos p_221042_, BlockState p_221043_) {
		p_221040_.setBlock(p_221042_, this.getStateForAge(getAge(p_221043_) + 1), 2);
	}
}
