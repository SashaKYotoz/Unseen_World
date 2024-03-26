package net.sashakyotoz.unseenworld.managers;

import net.sashakyotoz.unseenworld.util.UnseenWorldModBlocks;
import net.sashakyotoz.unseenworld.util.UnseenWorldModItems;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;

public class AncientTransientBlockCloseOnBlockRightClickedProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity == null)
            return;
        double sx, sy, sz;
        if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == UnseenWorldModItems.DARK_GOLEM_HEART.get()
                || (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == UnseenWorldModItems.DARK_GOLEM_HEART.get()) {
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
        }
    }
}
