package net.sashakyotoz.common.items.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.api.entity_data.data.GrippingData;
import net.sashakyotoz.utils.ActionsUtils;

import java.util.Set;

public class GripcrystalOrbItem extends Item {
    public GripcrystalOrbItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World startWorld, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        ActionsUtils.raycastAlong(player.getWorld(), player, 32, (world, pos) ->
                world.getOtherEntities(player, new Box(pos.toCenterPos(), pos.toCenterPos()).expand(0.75)).stream()
                        .filter(entity -> entity instanceof LivingEntity).findFirst().ifPresent(entity -> {
                            world.playSound(player, pos, SoundEvents.BLOCK_METAL_HIT, SoundCategory.BLOCKS, 2.5f, 2);
                            if (world instanceof ServerWorld world1) {
                                player.teleport(world1,
                                        entity.getX() - ActionsUtils.getXVector(2, entity.getYaw()),
                                        entity.getY() + 0.25f,
                                        entity.getZ() - ActionsUtils.getZVector(2, entity.getYaw()),
                                        Set.of(),
                                        entity.getYaw(),
                                        player.getPitch()
                                );
                                stack.decrement(1);
                                player.getItemCooldownManager().set(stack.getItem(), 20);
                                if (player instanceof IGrippingEntity grippingEntity)
                                    GrippingData.addGrippingSeconds(grippingEntity, 2);
                                if (entity instanceof IGrippingEntity grippingEntity)
                                    GrippingData.addGrippingSeconds(grippingEntity, 6);
                            }
                        })
        );
        return super.use(startWorld, player, hand);
    }
}