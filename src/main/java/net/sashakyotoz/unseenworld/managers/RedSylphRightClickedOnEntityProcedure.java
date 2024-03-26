package net.sashakyotoz.unseenworld.managers;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.sashakyotoz.unseenworld.entity.RedSlylfEntity;
import net.sashakyotoz.unseenworld.util.UnseenWorldModBlocks;
import net.sashakyotoz.unseenworld.util.UnseenWorldModItems;
import net.sashakyotoz.unseenworld.util.UnseenWorldModParticleTypes;

public class RedSylphRightClickedOnEntityProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, Player player, RedSlylfEntity slylfEntity) {
        if (player == null || slylfEntity == null)
            return;
        if (player.getMainHandItem().is(UnseenWorldModItems.OUTGROWTHAPPLE.get()) && slylfEntity.getMainHandItem().isEmpty()) {
            ItemStack stack = new ItemStack(UnseenWorldModItems.OUTGROWTHAPPLE.get());
            player.getInventory().clearOrCountMatchingItems(p -> stack.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
            slylfEntity.setItemInHand(InteractionHand.MAIN_HAND, stack);
            if (Math.random() < 0.5) {
                slylfEntity.spawnAtLocation(new ItemStack(UnseenWorldModItems.CRIMSERRY_SOUL_BERRY_FOOD.get()));
                world.addParticle(UnseenWorldModParticleTypes.GOLDEN.get(), x, y, z, 0, 1, 0);
            } else if (Math.random() < 0.35) {
                slylfEntity.spawnAtLocation(new ItemStack(UnseenWorldModItems.BERRIESFROM_BLOOMING_VINE.get()));
                world.addParticle(UnseenWorldModParticleTypes.BLUEVOIDPARTICLE.get(), x, y, z, 0, 1, 0);
            }
        } else if (player.getMainHandItem().is(UnseenWorldModItems.GRIZZLY_LIGHT_BLOCK.get())) {
            ItemStack stack = new ItemStack(UnseenWorldModBlocks.GRIZZLY_LIGHT_BLOCK.get());
            player.getInventory().clearOrCountMatchingItems(p -> stack.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
            slylfEntity.setItemInHand(InteractionHand.MAIN_HAND,stack);
            if (Math.random() < 0.65) {
                slylfEntity.spawnAtLocation(new ItemStack(UnseenWorldModBlocks.TANZASHROOM_LIGHT.get()));
                world.addParticle(UnseenWorldModParticleTypes.GOLDEN.get(), x, y, z, 0, 1, 0);
            }
        } else {
            if (!slylfEntity.getMainHandItem().isEmpty()){
                slylfEntity.spawnAtLocation(slylfEntity.getMainHandItem());
                slylfEntity.setItemInHand(InteractionHand.MAIN_HAND,ItemStack.EMPTY);
            }
        }
    }
}
