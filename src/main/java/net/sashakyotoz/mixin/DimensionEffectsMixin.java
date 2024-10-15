package net.sashakyotoz.mixin;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.sashakyotoz.UnseenWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DimensionEffects.class)
public class DimensionEffectsMixin {
    @Shadow @Mutable
    @Final public static Object2ObjectMap<Identifier, DimensionEffects> BY_IDENTIFIER;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void addCustomDimensionEffect(CallbackInfo ci) {
        BY_IDENTIFIER.put(UnseenWorld.makeID("the_chimeric_darkness"), new DimensionEffects(Float.NaN,false, DimensionEffects.SkyType.NONE,false,true) {
            @Override
            public Vec3d adjustFogColor(Vec3d color, float sunHeight) {
                return color.multiply(0.1);
            }

            @Override
            public boolean useThickFog(int camX, int camY) {
                return true;
            }
        });
//        Object2ObjectMap<Identifier, DimensionEffects> map = ReflectionHelper.getMap();
//        if (map != null) {
//            map.put(UnseenWorld.makeID("the_chimeric_darkness"), new DimensionEffects(Float.NaN,false, DimensionEffects.SkyType.NONE,false,true) {
//                @Override
//                public Vec3d adjustFogColor(Vec3d color, float sunHeight) {
//                    return color.multiply(0.1);
//                }
//
//                @Override
//                public boolean useThickFog(int camX, int camY) {
//                    return true;
//                }
//            });
//        }
    }
}