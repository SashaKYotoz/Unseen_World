package net.sashakyotoz.unseenworld.managers;

import net.sashakyotoz.unseenworld.util.UnseenWorldModEntities;
import net.sashakyotoz.unseenworld.util.UnseenWorldModItems;
import net.minecraftforge.items.ItemHandlerHelper;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import java.util.Map;

public class DustyPinkMaxorFishBucketRightclickedOnBlockProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (!world.getBlockState(BlockPos.containing(x, y + 1, z)).canOcclude()) {
			{
				BlockPos _bp = BlockPos.containing(x, y + 1, z);
				BlockState _bs = Blocks.WATER.defaultBlockState();
				BlockState _bso = world.getBlockState(_bp);
				for (Map.Entry<Property<?>, Comparable<?>> entry : _bso.getValues().entrySet()) {
					Property _property = _bs.getBlock().getStateDefinition().getProperty(entry.getKey().getName());
					if (_property != null && _bs.getValue(_property) != null)
						try {
							_bs = _bs.setValue(_property, (Comparable) entry.getValue());
						} catch (Exception e) {
						}
				}
				world.setBlock(_bp, _bs, 3);
			}
			if (world instanceof ServerLevel _level) {
				Entity entityToSpawn = UnseenWorldModEntities.DUSTY_PINK_MAXOR_FISH.get().spawn(_level, BlockPos.containing(x, y + 1, z), MobSpawnType.MOB_SUMMONED);
				if (entityToSpawn != null) {
					entityToSpawn.setYRot(world.getRandom().nextFloat() * 360F);
				}
			}
			if (entity instanceof Player _player) {
				ItemStack _stktoremove = new ItemStack(UnseenWorldModItems.DUSTY_PINK_MAXOR_FISH_BUCKET.get());
				_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
			}
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(Items.BUCKET);
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (!world.getBlockState(BlockPos.containing(x - 1, y, z)).canOcclude()) {
			{
				BlockPos _bp = BlockPos.containing(x - 1, y, z);
				BlockState _bs = Blocks.WATER.defaultBlockState();
				world.setBlock(_bp, _bs, 3);
			}
			if (world instanceof ServerLevel _level) {
				Entity entityToSpawn = UnseenWorldModEntities.DUSTY_PINK_MAXOR_FISH.get().spawn(_level, BlockPos.containing(x - 1, y, z), MobSpawnType.MOB_SUMMONED);
				if (entityToSpawn != null) {
					entityToSpawn.setYRot(world.getRandom().nextFloat() * 360F);
				}
			}
			if (entity instanceof Player player) {
				ItemStack _stktoremove = new ItemStack(UnseenWorldModItems.DUSTY_PINK_MAXOR_FISH_BUCKET.get());
				player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
			}
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(Items.BUCKET);
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		} else if (!world.getBlockState(BlockPos.containing(x + 1, y, z)).canOcclude()) {
			{
				BlockPos _bp = BlockPos.containing(x + 1, y, z);
				BlockState _bs = Blocks.WATER.defaultBlockState();
				world.setBlock(_bp, _bs, 3);
			}
			if (world instanceof ServerLevel _level) {
				Entity entityToSpawn = UnseenWorldModEntities.DUSTY_PINK_MAXOR_FISH.get().spawn(_level, BlockPos.containing(x + 1, y, z), MobSpawnType.MOB_SUMMONED);
				if (entityToSpawn != null) {
					entityToSpawn.setYRot(world.getRandom().nextFloat() * 360F);
				}
			}
			if (entity instanceof Player _player) {
				ItemStack _stktoremove = new ItemStack(UnseenWorldModItems.DUSTY_PINK_MAXOR_FISH_BUCKET.get());
				_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
			}
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(Items.BUCKET);
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
		}
	}
}
