package net.sashakyotoz.common.items.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.api.entity_data.data.GripcrystalManaData;
import net.sashakyotoz.utils.ActionsUtils;

public class EclipsebaneItem extends SwordItem {
    public EclipsebaneItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (IGrippingWeapons.getPhase(user.getStackInHand(hand)).equals("absorption")
                && GripcrystalManaData.getMana((IEntityDataSaver) user) < 48) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(user.getStackInHand(hand));
        }
        return super.use(world, user, hand);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return IGrippingWeapons.getPhase(stack).equals("absorption") ? 72000 : 0;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.TOOT_HORN;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (IGrippingWeapons.getPhase(stack).equals("absorption") && GripcrystalManaData.getMana((IEntityDataSaver) user) < 48) {
            ActionsUtils.raycastAlong(world, user, 16, (world1, pos) ->
                    world1.getEntitiesByClass(LivingEntity.class, new Box(pos.toCenterPos(), pos.toCenterPos()).expand(0.675),
                            LivingEntity::canHit).forEach(entity ->  {
                        if (entity != user) {
                            entity.playSound(SoundEvents.BLOCK_CONDUIT_DEACTIVATE, 1.5f, 2.0f);
                            world1.addParticle(ParticleTypes.END_ROD, pos.getX() + (Math.cos(remainingUseTicks * Math.PI / 10)), pos.getY() + (Math.tan(remainingUseTicks * Math.PI / 10)), pos.getZ() + (Math.sin(remainingUseTicks * Math.PI / 10)), 0.0D, 0.0D, 0.0D);
                        }
                    }));
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (IGrippingWeapons.getPhase(stack).equals("blade_shield")
                && entity.age % 10 == 0
                && entity instanceof ServerPlayerEntity player) {
            player.getWorld().getEntitiesByClass(LivingEntity.class, player.getBoundingBox().expand(2), LivingEntity::canTakeDamage).forEach(entity1 -> {
                if (entity1 != player && GripcrystalManaData.getMana((IEntityDataSaver) player) >= 2) {
                    GripcrystalManaData.removeMana((IEntityDataSaver) player, 2);
                    entity1.damage(player.getDamageSources().playerAttack(player), 6);
                    entity1.setVelocity(entity1.getVelocity().rotateX((entity.age / 2f) % 360).rotateZ((entity.age / 2f) % 360));
                }
            });
        }
    }
}