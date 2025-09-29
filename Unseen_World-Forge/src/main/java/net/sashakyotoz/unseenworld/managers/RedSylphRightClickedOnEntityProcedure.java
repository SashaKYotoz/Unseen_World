package net.sashakyotoz.unseenworld.managers;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.sashakyotoz.unseenworld.entity.RedSlylfEntity;
import net.sashakyotoz.unseenworld.registries.UnseenWorldBlocks;
import net.sashakyotoz.unseenworld.registries.UnseenWorldItems;
import net.sashakyotoz.unseenworld.registries.UnseenWorldParticleTypes;

public class RedSylphRightClickedOnEntityProcedure {
    public static void execute(LevelAccessor level, double x, double y, double z, Player player, RedSlylfEntity slylfEntity) {
        if (player == null || slylfEntity == null)
            return;
        if (player.getMainHandItem().is(UnseenWorldItems.OUTGROWTH_APPLE.get()) && slylfEntity.getMainHandItem().isEmpty()) {
            ItemStack stack = new ItemStack(UnseenWorldItems.OUTGROWTH_APPLE.get());
            player.getInventory().clearOrCountMatchingItems(p -> stack.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
            slylfEntity.setItemInHand(InteractionHand.MAIN_HAND, stack);
            if (Math.random() < 0.5) {
                slylfEntity.spawnAtLocation(new ItemStack(UnseenWorldItems.CRIMSERRY_SOUL_BERRY.get()));
                level.addParticle(UnseenWorldParticleTypes.GOLDEN.get(), x, y, z, 0, 1, 0);
            } else if (Math.random() < 0.35) {
                slylfEntity.spawnAtLocation(new ItemStack(UnseenWorldItems.BERRIES_OF_BLOOMING_VINE.get()));
                level.addParticle(UnseenWorldParticleTypes.BLUE_VOID_PARTICLE.get(), x, y, z, 0, 1, 0);
            }
        } else if (player.getMainHandItem().is(UnseenWorldBlocks.GRIZZLY_LIGHT_BLOCK.get().asItem())) {
            ItemStack stack = new ItemStack(UnseenWorldBlocks.GRIZZLY_LIGHT_BLOCK.get());
            player.getInventory().clearOrCountMatchingItems(p -> stack.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
            slylfEntity.setItemInHand(InteractionHand.MAIN_HAND,stack);
            if (Math.random() < 0.65) {
                slylfEntity.spawnAtLocation(new ItemStack(UnseenWorldBlocks.TANZASHROOM_LIGHT.get()));
                level.addParticle(UnseenWorldParticleTypes.GOLDEN.get(), x, y, z, 0, 1, 0);
            }
        } else {
            if (!slylfEntity.getMainHandItem().isEmpty()){
                slylfEntity.spawnAtLocation(slylfEntity.getMainHandItem());
                slylfEntity.setItemInHand(InteractionHand.MAIN_HAND,ItemStack.EMPTY);
            }
        }
    }
}
