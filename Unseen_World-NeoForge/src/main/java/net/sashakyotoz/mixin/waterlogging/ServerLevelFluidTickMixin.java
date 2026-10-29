package net.sashakyotoz.mixin.waterlogging;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.sashakyotoz.common.blocks.custom.fluids.DarkWaterlogging;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerLevelFluidTickMixin {
    @Inject(method = "tickFluid", at = @At("HEAD"), cancellable = true)
    private void unseen_world$tickDarkWaterInstead(BlockPos pos, Fluid fluid, CallbackInfo ci) {
        if (!fluid.isSame(Fluids.WATER)) {
            return;
        }
        ServerLevel level = (ServerLevel) (Object) this;
        FluidState state = level.getFluidState(pos);
        if (DarkWaterlogging.isDarkWater(state)) {
            state.tick(level, pos);
            ci.cancel();
        }
    }
}