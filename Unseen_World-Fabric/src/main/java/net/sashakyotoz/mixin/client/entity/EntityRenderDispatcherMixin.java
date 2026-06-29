package net.sashakyotoz.mixin.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.sashakyotoz.api.multipart_entity.EntityPart;
import net.sashakyotoz.api.multipart_entity.MultipartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

    @Inject(method = "renderHitbox", at = @At("HEAD"))
    private static void renderHitbox(PoseStack matrices, VertexConsumer vertices, Entity entity, float tickDelta, CallbackInfo ci) {
        if (entity instanceof MultipartEntity multipartEntity) {
            double x = -Mth.lerp(tickDelta, entity.xOld, entity.getX());
            double y = -Mth.lerp(tickDelta, entity.yOld, entity.getY());
            double z = -Mth.lerp(tickDelta, entity.zOld, entity.getZ());
            EntityPart[] parts = multipartEntity.getParts();

            for (EntityPart part : parts) {
                matrices.pushPose();
                double g = x + Mth.lerp(tickDelta, part.xOld, part.getX());
                double h = y + Mth.lerp(tickDelta, part.yOld, part.getY());
                double i = z + Mth.lerp(tickDelta, part.zOld, part.getZ());
                matrices.translate(g, h, i);
                LevelRenderer.renderLineBox(matrices, vertices,
                        part.getBoundingBox().move(-part.getX(), -part.getY(), -part.getZ()), 0.25F, 1.0F, 0.0F, 1.0F);
                matrices.popPose();
            }
        }
    }
}
