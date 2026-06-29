package net.sashakyotoz.common.items.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.api.entity_data.data.GripcrystalManaData;
import net.sashakyotoz.utils.ActionsUtils;

public class EclipsebaneItem extends SwordItem {
    public EclipsebaneItem(Tier toolMaterial, int attackDamage, float attackSpeed, Properties settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (IGrippingWeapons.getPhase(user.getItemInHand(hand)).equals("absorption")
                && GripcrystalManaData.getMana((IEntityDataSaver) user) < 48) {
            user.startUsingItem(hand);
            return InteractionResultHolder.consume(user.getItemInHand(hand));
        }
        return super.use(world, user, hand);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return IGrippingWeapons.getPhase(stack).equals("absorption") ? 72000 : 0;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public void onUseTick(Level world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (IGrippingWeapons.getPhase(stack).equals("absorption") && GripcrystalManaData.getMana((IEntityDataSaver) user) < 48) {
            ActionsUtils.rayCastAlong(world, user, 16, (world1, pos) ->
                    world1.getEntitiesOfClass(LivingEntity.class, new AABB(pos.getCenter(), pos.getCenter()).inflate(0.675),
                            LivingEntity::isPickable).stream().findFirst().ifPresent(entity -> {
                        if (entity != user) {
                            world1.playSound(null, pos, SoundEvents.CONDUIT_DEACTIVATE, SoundSource.PLAYERS, 1.5f, 2.0f);
                            world1.addParticle(ParticleTypes.END_ROD, pos.getX() + (Math.cos(remainingUseTicks * Math.PI / 10)), pos.getY() + (Math.tan(remainingUseTicks * Math.PI / 10)), pos.getZ() + (Math.sin(remainingUseTicks * Math.PI / 10)), 0.0D, 0.0D, 0.0D);
                            entity.setDeltaMovement(
                                    user.getDeltaMovement().x - entity.getDeltaMovement().x,
                                    user.getDeltaMovement().y - entity.getDeltaMovement().y,
                                    user.getDeltaMovement().z - entity.getDeltaMovement().z);
                            entity.hurtMarked = true;
                            if (user instanceof ServerPlayer player) {
                                GripcrystalManaData.addMana((IEntityDataSaver) player, 2);
                                player.getFoodData().addExhaustion(1);
                                player.getCooldowns().addCooldown(stack.getItem(), 10);
                            }
                        }
                    }));
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (IGrippingWeapons.getPhase(stack).equals("blade_shield")
                && entity.tickCount % 10 == 0
                && entity instanceof ServerPlayer player) {
            player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(2), LivingEntity::canBeSeenAsEnemy).forEach(entity1 -> {
                if (entity1 != player && GripcrystalManaData.getMana((IEntityDataSaver) player) >= 2) {
                    GripcrystalManaData.removeMana((IEntityDataSaver) player, 2);
                    entity1.hurt(player.damageSources().playerAttack(player), 6);
                    entity1.setDeltaMovement(entity1.getDeltaMovement().xRot((entity.tickCount / 2f) % 360).zRot((entity.tickCount / 2f) % 360));
                }
            });
        }
    }
}