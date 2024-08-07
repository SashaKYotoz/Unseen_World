package net.sashakyotoz.unseenworld.managers;

import net.sashakyotoz.unseenworld.entity.DarkPearlEntity;
import net.sashakyotoz.unseenworld.registries.UnseenWorldEntities;
import net.sashakyotoz.unseenworld.registries.UnseenWorldItems;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

public class DarkPearlRightClickedProcedure {
    public static void execute(Player player) {
        if (player == null)
            return;
        Level projectileLevel = player.level();
        if (!projectileLevel.isClientSide()) {
            Projectile arrow = new Object() {
                public Projectile getArrow(Level level, Entity shooter, float damage, int knockback) {
                    AbstractArrow entityToSpawn = new DarkPearlEntity(UnseenWorldEntities.DARK_PEARL.get(), level);
                    entityToSpawn.setOwner(shooter);
                    entityToSpawn.setBaseDamage(damage);
                    entityToSpawn.setKnockback(knockback);
                    entityToSpawn.setSilent(true);
                    return entityToSpawn;
                }
            }.getArrow(projectileLevel, player, 0, 0);
            arrow.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
            arrow.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, (float) 1.5, 0);
            projectileLevel.addFreshEntity(arrow);
        }
        ItemStack stack = new ItemStack(UnseenWorldItems.DARK_PEARL.get());
        player.getInventory().clearOrCountMatchingItems(p -> stack.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
    }
}
