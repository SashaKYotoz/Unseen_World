package net.sashakyotoz.unseenworld.managers;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;
import net.sashakyotoz.unseenworld.util.UnseenWorldModEntities;
import net.sashakyotoz.unseenworld.util.UnseenWorldModItems;

public class MoonFishInBucketRightClickedOnBlockProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity == null)
            return;
        if (!world.getBlockState(BlockPos.containing(x, y + 1, z)).canOcclude()) {
            BlockPos blockPos = BlockPos.containing(x, y + 1, z);
            BlockState blockState = Blocks.WATER.defaultBlockState();
            world.setBlock(blockPos, blockState, 3);
            if (world instanceof ServerLevel _level) {
                Entity entityToSpawn = UnseenWorldModEntities.MOONFISH.get().spawn(_level, BlockPos.containing(x, y + 1, z), MobSpawnType.MOB_SUMMONED);
                if (entityToSpawn != null) {
                    entityToSpawn.setYRot(world.getRandom().nextFloat() * 360F);
                }
            }
            if (entity instanceof Player _player) {
                ItemStack itemStack = new ItemStack(UnseenWorldModItems.MOON_FISHIN_BUCKET.get());
                _player.getInventory().clearOrCountMatchingItems(p -> itemStack.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
				ItemStack stack = new ItemStack(Items.BUCKET,1);
				ItemHandlerHelper.giveItemToPlayer(_player, stack);
            }
        } else if (!world.getBlockState(BlockPos.containing(x - 1, y, z)).canOcclude()) {
            {
                BlockPos blockPos = BlockPos.containing(x - 1, y, z);
                BlockState blockState = Blocks.WATER.defaultBlockState();
                world.setBlock(blockPos, blockState, 3);
            }
            if (world instanceof ServerLevel level) {
                Entity entityToSpawn = UnseenWorldModEntities.MOONFISH.get().spawn(level, BlockPos.containing(x - 1, y, z), MobSpawnType.MOB_SUMMONED);
                if (entityToSpawn != null) {
                    entityToSpawn.setYRot(world.getRandom().nextFloat() * 360F);
                }
            }
            if (entity instanceof Player player) {
                ItemStack itemStack = new ItemStack(UnseenWorldModItems.MOON_FISHIN_BUCKET.get());
                player.getInventory().clearOrCountMatchingItems(p -> itemStack.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
				ItemStack stack = new ItemStack(Items.BUCKET,1);
				ItemHandlerHelper.giveItemToPlayer(player, stack);
            }
        } else if (!world.getBlockState(BlockPos.containing(x + 1, y, z)).canOcclude()) {
            {
                BlockPos blockPos = BlockPos.containing(x + 1, y, z);
                BlockState state = Blocks.WATER.defaultBlockState();
                world.setBlock(blockPos, state, 3);
            }
            if (world instanceof ServerLevel _level) {
                Entity entityToSpawn = UnseenWorldModEntities.MOONFISH.get().spawn(_level, BlockPos.containing(x + 1, y, z), MobSpawnType.MOB_SUMMONED);
                if (entityToSpawn != null) {
                    entityToSpawn.setYRot(world.getRandom().nextFloat() * 360F);
                }
            }
            if (entity instanceof Player player) {
                ItemStack itemStack = new ItemStack(UnseenWorldModItems.MOON_FISHIN_BUCKET.get());
                player.getInventory().clearOrCountMatchingItems(p -> itemStack.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
				ItemStack stack = new ItemStack(Items.BUCKET,1);
				ItemHandlerHelper.giveItemToPlayer(player, stack);
            }
        }
    }
}
