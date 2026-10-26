package net.sashakyotoz.mixin.waterlogging;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.common.blocks.custom.fluids.DarkWaterlogging;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {
    @Inject(method = "getPlacementState", at = @At("RETURN"), cancellable = true)
    private void unseen_world$logDarkWater(BlockPlaceContext context, CallbackInfoReturnable<BlockState> cir) {
        BlockState state = cir.getReturnValue();
        if (state != null && DarkWaterlogging.supports(state)) {
            cir.setReturnValue(DarkWaterlogging.placementState(state, context.getLevel(), context.getClickedPos()));
        }
    }
}