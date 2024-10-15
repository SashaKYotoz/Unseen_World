package net.sashakyotoz.common.items.custom;

import net.minecraft.block.DispenserBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.sashakyotoz.utils.ActionsManager;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShieldOfChimericWarriorItem extends Item implements Equipment {

    public ShieldOfChimericWarriorItem(Item.Settings settings) {
        super(settings);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return BlockItem.getBlockEntityNbt(stack) != null ? this.getTranslationKey() + "." + getColor(stack).getName() : super.getTranslationKey(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        BannerItem.appendBannerTooltip(stack, tooltip);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (user.isSneaking()) {
            user.setVelocity(ActionsManager.getXVector(2, user.getYaw()),
                    ActionsManager.getYVector(0.25, user.getPitch()),
                    ActionsManager.getZVector(2, user.getYaw()));
            BlockPos center = world.raycast(new RaycastContext(user.getEyePos(), user.getEyePos().add(user.getRotationVec(1f).multiply(3)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, user)).getBlockPos();
            List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, new Box(center.toCenterPos(), center.toCenterPos()).expand(3), LivingEntity::canHit);
            for (LivingEntity entity : entities) {
                if (entity != user)
                    entity.damage(user.getDamageSources().mobAttack(user), 8);
            }
            itemStack.damage(1, user, p -> p.sendToolBreakStatus(user.getActiveHand()));
            user.getItemCooldownManager().set(itemStack.getItem(), 30);
        }
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isIn(ItemTags.STONE_BRICKS) || super.canRepair(stack, ingredient);
    }

    public static DyeColor getColor(ItemStack stack) {
        NbtCompound nbtCompound = BlockItem.getBlockEntityNbt(stack);
        return nbtCompound != null ? DyeColor.byId(nbtCompound.getInt("Base")) : DyeColor.WHITE;
    }

    @Override
    public EquipmentSlot getSlotType() {
        return EquipmentSlot.OFFHAND;
    }
}
