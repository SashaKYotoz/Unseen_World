package net.sashakyotoz.mixin.waterlogging;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.common.blocks.custom.fluids.DarkWaterlogging;
import net.sashakyotoz.common.blocks.custom.fluids.DualWaterloggedBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Block.class)
public abstract class BlockDefaultStateMixin {
    @ModifyVariable(method = "registerDefaultState", at = @At("HEAD"), argsOnly = true)
    private BlockState unseen_world$dryByDefault(BlockState state) {
        if (!(((Object) this) instanceof DualWaterloggedBlock) && DarkWaterlogging.supports(state)) {
            return state.setValue(DarkWaterlogging.DARK_WATER, false);
        }
        return state;
    }
}
