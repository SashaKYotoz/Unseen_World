package net.sashakyotoz.common.items.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.sashakyotoz.common.items.ModTiers;

import java.util.List;

public class ChimericRockbreakerHammerItem extends PickaxeItem {
    private int enemiesDamaged = 0;

    public ChimericRockbreakerHammerItem(int attackDamage, float attackSpeed, Settings settings) {
        super(ModTiers.ROCKBREAKER_HAMMER, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        player.setCurrentHand(hand);
        return super.use(world, player, hand);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        BlockPos center = world.raycast(new RaycastContext(user.getEyePos(), user.getEyePos().add(user.getRotationVec(1f).multiply(remainingUseTicks % 2 == 0 ? 2 : 4)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, user)).getBlockPos();
        List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, new Box(center.toCenterPos(), center.toCenterPos()).expand(3), LivingEntity::canHit);
        for (LivingEntity entity : entities) {
            if (entity != user) {
                enemiesDamaged++;
                entity.damage(user.getDamageSources().mobAttack(user), 8);
                stack.damage(1, user, p -> p.sendToolBreakStatus(user.getActiveHand()));
                if (enemiesDamaged > 11 && user instanceof PlayerEntity player) {
                    player.getItemCooldownManager().set(stack.getItem(), 20 * enemiesDamaged);
                    enemiesDamaged = 0;
                }
            }
        }
    }
}