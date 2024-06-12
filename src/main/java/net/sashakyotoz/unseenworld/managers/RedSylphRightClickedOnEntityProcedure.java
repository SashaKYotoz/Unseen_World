package net.sashakyotoz.unseenworld.managers;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.sashakyotoz.unseenworld.entity.RedSlylfEntity;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModBlocks;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModItems;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModParticleTypes;

public class RedSylphRightClickedOnEntityProcedure {
    public static void execute(LevelAccessor level, double x, double y, double z, Player player, RedSlylfEntity slylfEntity) {
        if (player == null || slylfEntity == null)
            return;
        if (player.getMainHandItem().is(UnseenWorldModItems.OUTGROWTH_APPLE.get()) && slylfEntity.getMainHandItem().isEmpty()) {
            ItemStack stack = new ItemStack(UnseenWorldModItems.OUTGROWTH_APPLE.get());
            player.getInventory().clearOrCountMatchingItems(p -> stack.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
            slylfEntity.setItemInHand(InteractionHand.MAIN_HAND, stack);
            if (Math.random() < 0.5) {
                slylfEntity.spawnAtLocation(new ItemStack(UnseenWorldModItems.CRIMSERRY_SOUL_BERRY.get()));
                level.addParticle(UnseenWorldModParticleTypes.GOLDEN.get(), x, y, z, 0, 1, 0);
            } else if (Math.random() < 0.35) {
                slylfEntity.spawnAtLocation(new ItemStack(UnseenWorldModItems.BERRIES_OF_BLOOMING_VINE.get()));
                level.addParticle(UnseenWorldModParticleTypes.BLUE_VOID_PARTICLE.get(), x, y, z, 0, 1, 0);
            }
        } else if (player.getMainHandItem().is(UnseenWorldModBlocks.GRIZZLY_LIGHT_BLOCK.get().asItem())) {
            ItemStack stack = new ItemStack(UnseenWorldModBlocks.GRIZZLY_LIGHT_BLOCK.get());
            player.getInventory().clearOrCountMatchingItems(p -> stack.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
            slylfEntity.setItemInHand(InteractionHand.MAIN_HAND,stack);
            if (Math.random() < 0.65) {
                slylfEntity.spawnAtLocation(new ItemStack(UnseenWorldModBlocks.TANZASHROOM_LIGHT.get()));
                level.addParticle(UnseenWorldModParticleTypes.GOLDEN.get(), x, y, z, 0, 1, 0);
            }
        } else {
            if (!slylfEntity.getMainHandItem().isEmpty()){
                slylfEntity.spawnAtLocation(slylfEntity.getMainHandItem());
                slylfEntity.setItemInHand(InteractionHand.MAIN_HAND,ItemStack.EMPTY);
            }
        }
    }
}
