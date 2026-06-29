package net.sashakyotoz.common.items.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.api.entity_data.data.GripcrystalManaData;
import net.sashakyotoz.client.particles.ModParticleTypes;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.items.ModTiers;

import java.util.List;

public class ChimericRockbreakerHammerItem extends PickaxeItem {
    private int enemiesDamaged = 0;

    public ChimericRockbreakerHammerItem(int attackDamage, float attackSpeed, Properties settings) {
        super(ModTiers.ROCKBREAKER_HAMMER, attackDamage, attackSpeed, settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (IGrippingWeapons.getPhase(player.getMainHandItem()).equals("hammer_eroflaming"))
            player.startUsingItem(hand);
        if (player instanceof ServerPlayer entity && GripcrystalManaData.getMana((IEntityDataSaver) entity) < 48) {
            if (IGrippingWeapons.getPhase(player.getMainHandItem()).equals("hammer_smashing") && player.getInventory().hasAnyMatching(itemStack -> itemStack.is(ModItems.GRIPCRYSTAL))) {
                GripcrystalManaData.addMana((IEntityDataSaver) player, 6);
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack itemStack = player.getInventory().getItem(i);
                    if (itemStack.is(ModItems.GRIPCRYSTAL)) {
                        itemStack.shrink(1);
                        player.playSound(SoundEvents.SNIFFER_EGG_CRACK, 3, 2.5F);
                        player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), 60);
                        break;
                    }
                }
                entity.serverLevel().sendParticles(
                        ModParticleTypes.GRIPPING_CRYSTAL,
                        entity.getX(), entity.getY() + 1, entity.getZ(),
                        5, 0, 0, 0, 1);
            }
        }
        return super.use(world, player, hand);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public void onUseTick(Level world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (IGrippingWeapons.getPhase(stack).equals("hammer_eroflaming") && user instanceof ServerPlayer serverPlayer && GripcrystalManaData.getMana((IEntityDataSaver) serverPlayer) > 0) {
            BlockPos center = world.clip(new ClipContext(user.getEyePosition(), user.getEyePosition().add(user.getViewVector(1f).scale(remainingUseTicks % 2 == 0 ? 2 : 4)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, user)).getBlockPos();
            List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, new AABB(center.getCenter(), center.getCenter()).inflate(3), LivingEntity::isPickable);
            Vec3 eyePos = serverPlayer.getEyePosition();
            Vec3 lookVec = serverPlayer.getViewVector(1.0F);
            Vec3 endPos = eyePos.add(lookVec.scale(5));
            int particleCount = 20;
            Vec3 step = endPos.subtract(eyePos).scale(1.0 / particleCount);
            double radius = 0.5;
            for (int j = 0; j < particleCount; j++) {
                Vec3 basePos = eyePos.add(step.scale(j));
                double angle = (remainingUseTicks) * Math.PI / 20 + (j * Math.PI / 10);
                double offsetX = radius * Math.cos(angle);
                double offsetY = radius * Math.sin(angle);

                Vec3 perpendicular = lookVec.cross(new Vec3(0, 1, 0)).normalize();
                Vec3 particlePos = basePos.add(perpendicular.scale(offsetX)).add(0, offsetY, 0);

                serverPlayer.serverLevel().sendParticles(
                        ParticleTypes.SOUL_FIRE_FLAME,
                        particlePos.x,
                        particlePos.y,
                        particlePos.z,
                        1, 0, 0, 0, 1
                );
            }
            if (serverPlayer.tickCount % 10 == 0)
                GripcrystalManaData.removeMana((IEntityDataSaver) serverPlayer, 1);
            for (LivingEntity entity : entities) {
                if (entity != user) {
                    enemiesDamaged++;
                    entity.hurt(user.damageSources().mobAttack(user), 8);
                    entity.setRemainingFireTicks(60);
                    stack.hurtAndBreak(1, user, p -> p.broadcastBreakEvent(user.getUsedItemHand()));
                    if (enemiesDamaged > 11) {
                        serverPlayer.getCooldowns().addCooldown(stack.getItem(), 20 * enemiesDamaged);
                        enemiesDamaged = 0;
                        break;
                    }
                }
            }
        }
    }
}