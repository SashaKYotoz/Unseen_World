package net.sashakyotoz.common.items.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.api.entity_data.data.GripcrystalManaData;
import net.sashakyotoz.client.particles.ModParticleTypes;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.items.ModTiers;

import java.util.List;

public class ChimericRockbreakerHammerItem extends PickaxeItem {
    private int enemiesDamaged = 0;

    public ChimericRockbreakerHammerItem(int attackDamage, float attackSpeed, Settings settings) {
        super(ModTiers.ROCKBREAKER_HAMMER, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (IGrippingWeapons.getPhase(player.getMainHandStack()).equals("hammer_eroflaming"))
            player.setCurrentHand(hand);
        if (player instanceof ServerPlayerEntity entity && GripcrystalManaData.getMana((IEntityDataSaver) entity) < 48) {
            if (IGrippingWeapons.getPhase(player.getMainHandStack()).equals("hammer_smashing") && player.getInventory().containsAny(itemStack -> itemStack.isOf(ModItems.GRIPCRYSTAL))) {
                GripcrystalManaData.addMana((IEntityDataSaver) player, 6);
                for (int i = 0; i < player.getInventory().size(); i++) {
                    ItemStack itemStack = player.getInventory().getStack(i);
                    if (itemStack.isOf(ModItems.GRIPCRYSTAL)) {
                        itemStack.decrement(1);
                        player.playSound(SoundEvents.BLOCK_SNIFFER_EGG_CRACK, 3, 2.5F);
                        player.getItemCooldownManager().set(player.getMainHandStack().getItem(), 60);
                        break;
                    }
                }
                entity.getServerWorld().spawnParticles(
                        ModParticleTypes.GRIPPING_CRYSTAL,
                        entity.getX(), entity.getY() + 1, entity.getZ(),
                        5, 0, 0, 0, 1);
            }
        }
        return super.use(world, player, hand);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (IGrippingWeapons.getPhase(stack).equals("hammer_eroflaming") && user instanceof ServerPlayerEntity serverPlayer && GripcrystalManaData.getMana((IEntityDataSaver) serverPlayer) > 0) {
            BlockPos center = world.raycast(new RaycastContext(user.getEyePos(), user.getEyePos().add(user.getRotationVec(1f).multiply(remainingUseTicks % 2 == 0 ? 2 : 4)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, user)).getBlockPos();
            List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, new Box(center.toCenterPos(), center.toCenterPos()).expand(3), LivingEntity::canHit);
            Vec3d eyePos = serverPlayer.getEyePos();
            Vec3d lookVec = serverPlayer.getRotationVec(1.0F);
            Vec3d endPos = eyePos.add(lookVec.multiply(5));
            int particleCount = 20;
            Vec3d step = endPos.subtract(eyePos).multiply(1.0 / particleCount);
            double radius = 0.5;
            for (int j = 0; j < particleCount; j++) {
                Vec3d basePos = eyePos.add(step.multiply(j));
                double angle = (remainingUseTicks) * Math.PI / 20 + (j * Math.PI / 10);
                double offsetX = radius * Math.cos(angle);
                double offsetY = radius * Math.sin(angle);

                Vec3d perpendicular = lookVec.crossProduct(new Vec3d(0, 1, 0)).normalize();
                Vec3d particlePos = basePos.add(perpendicular.multiply(offsetX)).add(0, offsetY, 0);

                serverPlayer.getServerWorld().spawnParticles(
                        ParticleTypes.SOUL_FIRE_FLAME,
                        particlePos.x,
                        particlePos.y,
                        particlePos.z,
                        1, 0, 0, 0, 1
                );
            }
            if (serverPlayer.age % 10 == 0)
                GripcrystalManaData.removeMana((IEntityDataSaver) serverPlayer, 1);
            for (LivingEntity entity : entities) {
                if (entity != user) {
                    enemiesDamaged++;
                    entity.damage(user.getDamageSources().mobAttack(user), 8);
                    entity.setFireTicks(60);
                    stack.damage(1, user, p -> p.sendToolBreakStatus(user.getActiveHand()));
                    if (enemiesDamaged > 11) {
                        serverPlayer.getItemCooldownManager().set(stack.getItem(), 20 * enemiesDamaged);
                        enemiesDamaged = 0;
                        break;
                    }
                }
            }
        }
    }
}