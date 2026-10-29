package net.sashakyotoz.mixin.waterlogging;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.common.blocks.custom.fluids.DarkWaterlogging;
import net.sashakyotoz.common.blocks.custom.fluids.DualWaterloggedBlock;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BucketItem.class)
public abstract class BucketItemMixin {
    @WrapOperation(method = "use", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/BucketPickup;pickupBlock(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack unseen_world$pickupDarkWater(BucketPickup pickup, @Nullable Player player, LevelAccessor level, BlockPos pos, BlockState state, Operation<ItemStack> original) {
        if (!(pickup instanceof DualWaterloggedBlock) && DarkWaterlogging.isDarkLogged(state)) {
            return DarkWaterlogging.drain(level, pos, state);
        }
        return original.call(pickup, player, level, pos, state);
    }
}