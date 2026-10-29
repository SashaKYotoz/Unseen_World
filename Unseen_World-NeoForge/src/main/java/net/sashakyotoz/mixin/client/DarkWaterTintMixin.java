package net.sashakyotoz.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.sashakyotoz.client.environment.DarkWaterRendering;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LiquidBlockRenderer.class)
public abstract class DarkWaterTintMixin {
    @WrapOperation(method = "tesselate", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/extensions/common/IClientFluidTypeExtensions;of(Lnet/minecraft/world/level/material/FluidState;)Lnet/neoforged/neoforge/client/extensions/common/IClientFluidTypeExtensions;"))
    private IClientFluidTypeExtensions unseen_world$darkWaterTint(FluidState state, Operation<IClientFluidTypeExtensions> original) {
        return DarkWaterRendering.extensionsFor(state, original.call(state));
    }
}
