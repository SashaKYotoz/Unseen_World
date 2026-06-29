package net.sashakyotoz.common.items.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.api.entity_data.data.GripcrystalManaData;
import net.sashakyotoz.utils.ActionsUtils;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class GrippingGauntletItem extends Item {
    public GrippingGauntletItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        user.startUsingItem(hand);
        return InteractionResultHolder.consume(user.getItemInHand(hand));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof Player player && !world.isClientSide()) {
            ActionsUtils.rayCastAlong(world, player, 24, (world1, pos) ->
                    world1.getEntitiesOfClass(LivingEntity.class, new AABB(pos.getCenter(), pos.getCenter()).inflate(0.75),
                            LivingEntity::isPickable).stream().filter(this::getNotEmptyEntity).forEach(entity -> {
                        if (entity != player && world1 instanceof ServerLevel serverWorld) {
                            entity.playSound(SoundEvents.CONDUIT_ACTIVATE);
                            serverWorld.sendParticles(ParticleTypes.ASH, pos.getX() + (Math.cos(remainingUseTicks * Math.PI / 10)), pos.getY() + (Math.tan(remainingUseTicks * Math.PI / 10)), pos.getZ() + (Math.sin(remainingUseTicks * Math.PI / 10)), 9, 0.0D, 0.0D, 0.0D, 1);
                            if (player instanceof ServerPlayer && GripcrystalManaData.getMana((IEntityDataSaver) player) >= 12) {
                                GripcrystalManaData.removeMana((IEntityDataSaver) player, 6);
                                if (entity instanceof InventoryCarrier owner && !owner.getInventory().isEmpty()) {
                                    ItemStack stack1 = owner.getInventory().items.stream().filter(itemStack -> !itemStack.isEmpty()).findFirst().orElse(ItemStack.EMPTY);
                                    if (!stack1.isEmpty()) {
                                        ActionsUtils.dropItem(entity, stack1);
                                        owner.getInventory().removeItemType(stack1.getItem(), stack1.getCount());
                                    }
                                }
                                for (ItemStack stack1 : entity.getAllSlots()) {
                                    if (!stack1.isEmpty()) {
                                        ActionsUtils.dropItem(entity, stack1);
                                        Arrays.stream(EquipmentSlot.values()).filter(slot -> ItemStack.matches(entity.getItemBySlot(slot), stack1)).forEach(slot -> {
                                            entity.setItemSlot(slot, ItemStack.EMPTY);
                                        });
                                    }
                                }
                            }
                        }
                    }));
        }
    }

    private boolean getNotEmptyEntity(LivingEntity entity) {
        if (entity instanceof InventoryCarrier owner) {
            return !owner.getInventory().isEmpty();
        }
        AtomicInteger i = new AtomicInteger();
        Arrays.stream(EquipmentSlot.values()).forEach(slot -> {
            if (!entity.getItemBySlot(slot).isEmpty())
                i.incrementAndGet();
        });
        return i.get() > 0;
    }
}