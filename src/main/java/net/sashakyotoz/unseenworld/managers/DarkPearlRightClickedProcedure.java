package net.sashakyotoz.unseenworld.managers;

import net.sashakyotoz.unseenworld.entity.DarkPearlRangedItemEntity;
import net.sashakyotoz.unseenworld.util.UnseenWorldModEntities;
import net.sashakyotoz.unseenworld.util.UnseenWorldModItems;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

public class DarkPearlRightClickedProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        Level projectileLevel = entity.level();
        if (!projectileLevel.isClientSide()) {
            Projectile arrow = new Object() {
                public Projectile getArrow(Level level, Entity shooter, float damage, int knockback) {
                    AbstractArrow entityToSpawn = new DarkPearlRangedItemEntity(UnseenWorldModEntities.DARK_PEARL_RANGED_ITEM.get(), level);
                    entityToSpawn.setOwner(shooter);
                    entityToSpawn.setBaseDamage(damage);
                    entityToSpawn.setKnockback(knockback);
                    entityToSpawn.setSilent(true);
                    return entityToSpawn;
                }
            }.getArrow(projectileLevel, entity, 0, 0);
            arrow.setPos(entity.getX(), entity.getEyeY() - 0.1, entity.getZ());
            arrow.shoot(entity.getLookAngle().x, entity.getLookAngle().y, entity.getLookAngle().z, (float) 1.5, 0);
            projectileLevel.addFreshEntity(arrow);
        }
        if (entity instanceof Player _player) {
            ItemStack _stktoremove = new ItemStack(UnseenWorldModItems.DARKPEARL.get());
            _player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
        }
    }
}
