
package net.sashakyotoz.unseenworld.item;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.unseenworld.util.UnseenWorldModBlocks;
import net.sashakyotoz.unseenworld.util.UnseenWorldModItems;
import net.sashakyotoz.unseenworld.util.UnseenWorldModTags;

public class PurpleBerriesItem extends Item {
	public PurpleBerriesItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.RARE).food((new FoodProperties.Builder()).nutrition(3).saturationMod(1f).build()));
	}

	@Override
	public int getUseDuration(ItemStack itemstack) {
		return 48;
	}

	@Override
	public float getDestroySpeed(ItemStack par1ItemStack, BlockState par2Block) {
		return 0F;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack itemstack, Level world, LivingEntity entity) {
		ItemStack stack = super.finishUsingItem(itemstack, world, entity);
		if (!entity.level().isClientSide())
			entity.addEffect(new MobEffectInstance(MobEffects.HEAL, 60, 1));
		return stack;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		super.useOn(context);
		onClick(context.getLevel(), context.getClickedPos(), context.getPlayer());
		return InteractionResult.SUCCESS;
	}
	private void onClick(LevelAccessor world, BlockPos pos, Player player) {
		if (player == null)
			return;
		if (world.getBlockState(pos).is(UnseenWorldModTags.Blocks.DIRT_THE_DARKNESS)
				&& world.getMaxLocalRawBrightness(pos) <= 8) {
			ItemStack stack = new ItemStack(UnseenWorldModItems.PURPLE_BERRIES.get());
			player.getInventory().clearOrCountMatchingItems(p -> stack.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
			world.setBlock(pos.above(), UnseenWorldModBlocks.MISTERYFLOWER_SAPLING.get().defaultBlockState(), 3);
		} else if (world.getBlockState(pos).is(BlockTags.DIRT) && world.getMaxLocalRawBrightness(pos) <= 12) {
			ItemStack stack = new ItemStack(UnseenWorldModItems.PURPLE_BERRIES.get());
			player.getInventory().clearOrCountMatchingItems(p -> stack.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
			if (Math.random() < 0.5) {
				world.setBlock(pos.above(), UnseenWorldModBlocks.MISTERYFLOWER_SAPLING.get().defaultBlockState(), 3);
			}
		}
	}
}
