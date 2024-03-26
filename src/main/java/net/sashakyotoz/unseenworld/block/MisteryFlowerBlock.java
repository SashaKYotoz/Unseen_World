
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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.sashakyotoz.unseenworld.util.UnseenWorldModItems;

public class MisteryFlowerBlock extends FlowerBlock {
	public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 2);

	public MisteryFlowerBlock() {
		super(() -> MobEffects.MOVEMENT_SPEED, 100, BlockBehaviour.Properties.copy(Blocks.PEONY).sound(SoundType.GRASS)
				.instabreak().lightLevel(s -> 1).noLootTable().noCollission());
		this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
	}

	@Override
	public int getEffectDuration() {
		return 100;
	}

	@Override
	public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 40;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 60;
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

	@Override
	public void tick(BlockState blockstate, ServerLevel world, BlockPos pos, RandomSource random) {
		super.tick(blockstate, world, pos, random);
		if (Math.random() < 0.025) {
			if (blockstate.getBlock().getStateDefinition().getProperty("age") instanceof IntegerProperty property)
				world.setBlock(pos,blockstate.setValue(property,blockstate.getValue(property) == 0 ? 1 : 2),3);
		}
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState blockstate, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
		boolean onDestroyedByPlayer = super.onDestroyedByPlayer(blockstate, world, pos, player, willHarvest, fluid);
		if (blockstate.getBlock().getStateDefinition().getProperty("age") instanceof IntegerProperty property && blockstate.getValue(property) > 0){
			int randomCountOfBerries = player.getRandom().nextIntBetweenInclusive(1,3) + blockstate.getValue(property);
			player.spawnAtLocation(new ItemStack(UnseenWorldModItems.PURPLE_BERRIES.get(),randomCountOfBerries));
		}else
			player.spawnAtLocation(new ItemStack(UnseenWorldModItems.PURPLE_BERRIES.get()));
		return onDestroyedByPlayer;
	}

	@Override
	public InteractionResult use(BlockState blockstate, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		super.use(blockstate, world, pos, player, hand, hit);
		onClick(world, pos, blockstate, player);
		return InteractionResult.SUCCESS;
	}
	private void onClick(LevelAccessor world, BlockPos pos, BlockState blockstate, Player player) {
		if (player == null)
			return;
		if (player.getMainHandItem().is(Items.BONE_MEAL)) {
			ItemStack stack = new ItemStack(Items.BONE_MEAL);
			player.getInventory().clearOrCountMatchingItems(p -> stack.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
			if (Math.random() < 0.25) {
				if (blockstate.getBlock().getStateDefinition().getProperty("age") instanceof IntegerProperty property) {
					switch (blockstate.getValue(property)){
						case 0 -> {
							BlockState state = world.getBlockState(pos);
							if (property.getPossibleValues().contains(1))
								world.setBlock(pos, state.setValue(property, 1), 3);
						}
						case 1 ->{
							BlockState blockState = world.getBlockState(pos);
							if (property.getPossibleValues().contains(2))
								world.setBlock(pos, blockState.setValue(property, 2), 3);
						}
					}
				}
			}
		}
	}
}