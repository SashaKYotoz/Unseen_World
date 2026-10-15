package net.sashakyotoz.unseenworld.managers;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.sashakyotoz.unseenworld.UnseenWorld;
import net.sashakyotoz.unseenworld.registries.UnseenWorldBlocks;
import net.sashakyotoz.unseenworld.registries.UnseenWorldEntities;
import net.sashakyotoz.unseenworld.registries.UnseenWorldItems;

public class DarkGolemHeartRightClickOnBlockProcedure {
    public static void execute(LevelAccessor level, double x, double y, double z, Player entity) {
        if (entity == null)
            return;
        if ((level.getBlockState(BlockPos.containing(x, y, z))).getBlock() == UnseenWorldBlocks.UNDEAD_WARRIOR_OF_THE_CHIMERIC_DARKNESS.get()) {
            level.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
            UnseenWorld.queueServerWork(10, () -> {
                if (level instanceof ServerLevel _level) {
                    Entity entityToSpawn = UnseenWorldEntities.DARK_GOLEM.get().spawn(_level, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED);
                    if (entityToSpawn != null) {
                        entityToSpawn.setYRot(level.getRandom().nextFloat() * 360F);
                    }
                }
            });
            ItemStack itemStack = new ItemStack(UnseenWorldItems.DARK_GOLEM_HEART.get());
            entity.getInventory().clearOrCountMatchingItems(p -> itemStack.getItem() == p.getItem(), 1, entity.inventoryMenu.getCraftSlots());
            AncientTransientBlockCloseOnBlockRightClickedProcedure.execute(level, x, y, z, entity);
        }
    }
}
