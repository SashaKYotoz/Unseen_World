package net.sashakyotoz.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.textures.FluidSpriteCache;
import net.sashakyotoz.client.environment.DarkWaterRendering;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = FluidSpriteCache.class, remap = false)
public abstract class DarkWaterSpriteMixin {
    @WrapOperation(method = "getFluidSprites", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/extensions/common/IClientFluidTypeExtensions;of(Lnet/minecraft/world/level/material/FluidState;)Lnet/neoforged/neoforge/client/extensions/common/IClientFluidTypeExtensions;"))
    private static IClientFluidTypeExtensions unseen_world$darkWaterSprites(FluidState state, Operation<IClientFluidTypeExtensions> original) {
        return DarkWaterRendering.extensionsFor(state, original.call(state));
    }
}
