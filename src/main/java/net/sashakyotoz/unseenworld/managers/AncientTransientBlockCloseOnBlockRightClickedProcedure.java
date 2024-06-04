package net.sashakyotoz.unseenworld.managers;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModBlocks;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModItems;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;

public class AncientTransientBlockCloseOnBlockRightClickedProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, Player player) {
        if (player == null)
            return;
        double sx, sy, sz;
        if (player.getMainHandItem().is(UnseenWorldModItems.DARK_GOLEM_HEART.get()) || player.getOffhandItem().is(UnseenWorldModItems.DARK_GOLEM_HEART.get())) {
            sx = -3;
            for (int index0 = 0; index0 < 6; index0++) {
                sy = -3;
                for (int index1 = 0; index1 < 6; index1++) {
                    sz = -3;
                    for (int index2 = 0; index2 < 6; index2++) {
                        if ((world.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))).getBlock() == UnseenWorldModBlocks.ANCIENT_TRANSIENT_BLOCK_CLOSE.get()) {
                            BlockPos pos = BlockPos.containing(x + sx, y + sy, z + sz);
                            BlockState state = UnseenWorldModBlocks.ANCIENT_TRANSIENT_BLOCK_OPEN.get().defaultBlockState();
                            world.setBlock(pos, state, 3);
                        }
                        sz = sz + 1;
                    }
                    sy = sy + 1;
                }
                sx = sx + 1;
            }
            if (RandomSource.create().nextBoolean() && !player.getAbilities().instabuild)
                player.getInventory().clearOrCountMatchingItems(p -> UnseenWorldModItems.DARK_GOLEM_HEART.get() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
        }
    }
}
