package net.sashakyotoz.mixin.client.compat;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.irisshaders.iris.pipeline.WorldRenderingPipeline;
import net.irisshaders.iris.shaderpack.materialmap.NamespacedId;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.compat.IrisCompat;
import net.sashakyotoz.client.compat.ShaderCompatMode;
import net.sashakyotoz.common.world.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "net.irisshaders.iris.Iris", remap = false)
public abstract class IrisDimensionMixin {
    @Unique
    private static final NamespacedId UNSEEN_WORLD$NETHER_PROFILE = new NamespacedId("minecraft", "the_nether");
    @Unique
    private static boolean unseen_world$aliasLogged;
    @Unique
    private static boolean unseen_world$vanillaLogged;

    @ModifyReturnValue(method = "getCurrentDimension", at = @At("RETURN"), require = 0)
    private static NamespacedId unseen_world$aliasDimension(NamespacedId dimension) {
        if (ShaderCompatMode.get() != ShaderCompatMode.NETHER_PROFILE || !unseen_world$isChimericDarkness(dimension)) {
            return dimension;
        }
        if (!unseen_world$aliasLogged) {
            unseen_world$aliasLogged = true;
            UnseenWorld.log().info("Shaderpack profile for " + dimension + " linked to " + UNSEEN_WORLD$NETHER_PROFILE);
        }
        return UNSEEN_WORLD$NETHER_PROFILE;
    }

    @Inject(method = "createPipeline", at = @At("HEAD"), cancellable = true, require = 0)
    private static void unseen_world$useVanillaPipeline(NamespacedId dimensionId, CallbackInfoReturnable<WorldRenderingPipeline> cir) {
        if (ShaderCompatMode.get() != ShaderCompatMode.VANILLA || !unseen_world$isChimericDarkness(dimensionId)) {
            return;
        }
        if (IrisCompat.createVanillaPipeline() instanceof WorldRenderingPipeline pipeline) {
            if (!unseen_world$vanillaLogged) {
                unseen_world$vanillaLogged = true;
                UnseenWorld.log().info("Shaderpack disabled in " + dimensionId + ", using vanilla rendering pipeline");
            }
            cir.setReturnValue(pipeline);
        }
    }

    @Unique
    private static boolean unseen_world$isChimericDarkness(NamespacedId dimension) {
        ResourceLocation location = ModDimensions.CHIMERIC_DARKNESS_LEVEL_KEY.location();
        return dimension != null && location.getNamespace().equals(dimension.getNamespace()) && location.getPath().equals(dimension.getName());
    }
}