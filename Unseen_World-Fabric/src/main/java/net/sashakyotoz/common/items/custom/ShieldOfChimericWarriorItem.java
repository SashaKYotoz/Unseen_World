package net.sashakyotoz.common.items.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.utils.ActionsUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShieldOfChimericWarriorItem extends Item implements Equipable {

    public ShieldOfChimericWarriorItem(Item.Properties settings) {
        super(settings);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        BannerItem.appendHoverTextFromBannerBlockEntityTag(stack, tooltip);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        if (user.isShiftKeyDown()) {
            user.setDeltaMovement(ActionsUtils.getXVector(2, user.getYRot()),
                    ActionsUtils.getYVector(0.25, user.getXRot()),
                    ActionsUtils.getZVector(2, user.getYRot()));
            BlockPos center = world.clip(new ClipContext(user.getEyePosition(), user.getEyePosition().add(user.getViewVector(1f).scale(3)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, user)).getBlockPos();
            List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, new AABB(center.getCenter(), center.getCenter()).inflate(3), LivingEntity::isPickable);
            for (LivingEntity entity : entities) {
                if (entity != user)
                    entity.hurt(user.damageSources().mobAttack(user), 8);
            }
            itemStack.hurtAndBreak(1, user, p -> p.broadcastBreakEvent(user.getUsedItemHand()));
            user.getCooldowns().addCooldown(itemStack.getItem(), 30);
        }
        user.startUsingItem(hand);
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack ingredient) {
        return ingredient.is(ModItems.GRIPTONITE) || super.isValidRepairItem(stack, ingredient);
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.OFFHAND;
    }
}
