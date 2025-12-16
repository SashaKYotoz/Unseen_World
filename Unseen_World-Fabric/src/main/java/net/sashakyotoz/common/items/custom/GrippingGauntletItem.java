package net.sashakyotoz.common.items.custom;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.api.entity_data.data.GripcrystalManaData;
import net.sashakyotoz.utils.ActionsUtils;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class GrippingGauntletItem extends Item {
    public GrippingGauntletItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player && !world.isClient()) {
            ActionsUtils.raycastAlong(world, player, 24, (world1, pos) ->
                    world1.getEntitiesByClass(LivingEntity.class, new Box(pos.toCenterPos(), pos.toCenterPos()).expand(0.75),
                            LivingEntity::canHit).stream().filter(this::getNotEmptyEntity).forEach(entity -> {
                        if (entity != player && world1 instanceof ServerWorld serverWorld) {
                            entity.playSoundIfNotSilent(SoundEvents.BLOCK_CONDUIT_ACTIVATE);
                            serverWorld.spawnParticles(ParticleTypes.ASH, pos.getX() + (Math.cos(remainingUseTicks * Math.PI / 10)), pos.getY() + (Math.tan(remainingUseTicks * Math.PI / 10)), pos.getZ() + (Math.sin(remainingUseTicks * Math.PI / 10)), 9, 0.0D, 0.0D, 0.0D, 1);
                            if (player instanceof ServerPlayerEntity && GripcrystalManaData.getMana((IEntityDataSaver) player) >= 12) {
                                GripcrystalManaData.removeMana((IEntityDataSaver) player, 6);
                                if (entity instanceof InventoryOwner owner && !owner.getInventory().isEmpty()) {
                                    ItemStack stack1 = owner.getInventory().stacks.stream().filter(itemStack -> !itemStack.isEmpty()).findFirst().orElse(ItemStack.EMPTY);
                                    if (!stack1.isEmpty()) {
                                        ActionsUtils.dropItem(entity, stack1);
                                        owner.getInventory().removeItem(stack1.getItem(), stack1.getCount());
                                    }
                                }
                                for (ItemStack stack1 : entity.getItemsEquipped()) {
                                    if (!stack1.isEmpty()) {
                                        ActionsUtils.dropItem(entity, stack1);
                                        Arrays.stream(EquipmentSlot.values()).filter(slot -> ItemStack.areEqual(entity.getEquippedStack(slot), stack1)).forEach(slot -> {
                                            entity.equipStack(slot, ItemStack.EMPTY);
                                        });
                                    }
                                }
                            }
                        }
                    }));
        }
    }

    private boolean getNotEmptyEntity(LivingEntity entity) {
        if (entity instanceof InventoryOwner owner) {
            return !owner.getInventory().isEmpty();
        }
        AtomicInteger i = new AtomicInteger();
        Arrays.stream(EquipmentSlot.values()).forEach(slot -> {
            if (!entity.getEquippedStack(slot).isEmpty())
                i.incrementAndGet();
        });
        return i.get() > 0;
    }
}