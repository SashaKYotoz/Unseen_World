package net.sashakyotoz.unseenworld.managers;

import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModBlocks;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModEntities;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModItems;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

public class DarkGolemHeartRightClickOnBlockProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, Player entity) {
        if (entity == null)
            return;
        if ((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == UnseenWorldModBlocks.UNDEAD_WARRIOR_OF_THE_CHIMERIC_DARKNESS.get()) {
            world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
            UnseenWorldMod.queueServerWork(10, () -> {
                if (world instanceof ServerLevel _level) {
                    Entity entityToSpawn = UnseenWorldModEntities.DARK_GOLEM.get().spawn(_level, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED);
                    if (entityToSpawn != null) {
                        entityToSpawn.setYRot(world.getRandom().nextFloat() * 360F);
                    }
                }
            });
            ItemStack itemStack = new ItemStack(UnseenWorldModItems.DARK_GOLEM_HEART.get());
            entity.getInventory().clearOrCountMatchingItems(p -> itemStack.getItem() == p.getItem(), 1, entity.inventoryMenu.getCraftSlots());
            AncientTransientBlockCloseOnBlockRightClickedProcedure.execute(world, x, y, z, entity);
        }
    }
}
