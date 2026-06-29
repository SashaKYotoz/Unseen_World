package net.sashakyotoz.common.items.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.api.entity_data.data.GrippingData;

public class GriptoniteItem extends Item {
    public GriptoniteItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        if (user instanceof IGrippingEntity player && player.getGrippingData() > 0) {
            GrippingData.removeGripping(player);
            user.playNotifySound(SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, 1.5f, 1);
            itemStack.hurtAndBreak(1, user, p -> p.broadcastBreakEvent(hand));
            return InteractionResultHolder.consume(itemStack);
        }
        return super.use(world, user, hand);
    }
}