
package net.sashakyotoz.unseenworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sashakyotoz.unseenworld.registries.UnseenWorldBlocks;
import net.sashakyotoz.unseenworld.registries.UnseenWorldItems;
import org.jetbrains.annotations.NotNull;

public class DarkCrimsonVineFlowerBlock extends GrowingPlantHeadBlock implements BonemealableBlock,DarkCrimsonVine {

	public DarkCrimsonVineFlowerBlock() {
		super(BlockBehaviour.Properties.of().mapColor(DyeColor.CYAN).sound(SoundType.SWEET_BERRY_BUSH).randomTicks().instabreak().lightLevel(s -> 3).noCollission().noOcclusion().randomTicks()
				.hasPostProcess((bs, br, bp) -> true).emissiveRendering((bs, br, bp) -> true), Direction.DOWN, SHAPE, false, 0.15D);
		this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(BERRIES, Boolean.FALSE));
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return true;
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 0;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	protected int getBlocksToGrowWhenBonemealed(RandomSource source) {
		return 1;
	}

	protected boolean canGrowInto(BlockState state) {
		return state.isAir();
	}

	protected @NotNull Block getBodyBlock() {
		return UnseenWorldBlocks.DARK_CRIMSON_BLOOMING_VINE.get();
	}

	protected @NotNull BlockState updateBodyAfterConvertedFromHead(BlockState state, BlockState p_152988_) {
		return p_152988_.setValue(BERRIES, state.getValue(BERRIES));
	}

	protected @NotNull BlockState getGrowIntoState(BlockState state, RandomSource source) {
		return super.getGrowIntoState(state, source).setValue(BERRIES, source.nextFloat() < 0.15F);
	}

	public @NotNull ItemStack getCloneItemStack(BlockGetter getter, BlockPos pos, BlockState state) {
		return new ItemStack(UnseenWorldItems.BERRIES_OF_BLOOMING_VINE.get());
	}

	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		return DarkCrimsonVine.use(player, state, level, pos);
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BERRIES);
	}

	public boolean isValidBonemealTarget(LevelReader reader, BlockPos pos, BlockState state, boolean p_152973_) {
		return !state.getValue(BERRIES);
	}

	public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
		return true;
	}

	public void performBonemeal(ServerLevel level, RandomSource source, BlockPos pos, BlockState state) {
		level.setBlock(pos, state.setValue(BERRIES, Boolean.TRUE), 2);
	}
}
