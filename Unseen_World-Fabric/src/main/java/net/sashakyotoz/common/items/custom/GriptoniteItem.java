package net.sashakyotoz.common.items.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.api.entity_data.data.GrippingData;

public class GriptoniteItem extends Item {
    public GriptoniteItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (user instanceof IGrippingEntity player && player.getGrippingData() > 0) {
            GrippingData.removeGripping(player);
            user.playSound(SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.PLAYERS, 1.5f, 1);
            itemStack.damage(1, user, p -> p.sendToolBreakStatus(hand));
            return TypedActionResult.consume(itemStack);
        }
        return super.use(world, user, hand);
    }
}