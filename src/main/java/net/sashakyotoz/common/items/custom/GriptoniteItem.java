package net.sashakyotoz.common.items.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.sashakyotoz.common.networking.data.GrippingData;
import net.sashakyotoz.utils.IEntityDataSaver;

public class GriptoniteItem extends Item {
    public GriptoniteItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (user instanceof ServerPlayerEntity player && GrippingData.getGrippingTime((IEntityDataSaver) player) > 0) {
            GrippingData.removeGripping((IEntityDataSaver) player);
            itemStack.damage(1, player, p -> p.sendToolBreakStatus(hand));
            return TypedActionResult.consume(itemStack);
        }
        return super.use(world, user, hand);
    }
}