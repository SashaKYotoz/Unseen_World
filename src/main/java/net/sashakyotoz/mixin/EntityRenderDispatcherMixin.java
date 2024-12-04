package net.sashakyotoz.mixin;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.sashakyotoz.common.entities.bosses.EclipseSentinelEntity;
import net.sashakyotoz.common.entities.bosses.parts.EclipseSentinelPartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(method = "renderHitbox",at = @At("HEAD"))
    private static void renderHitbox(MatrixStack matrices, VertexConsumer vertices, Entity entity, float tickDelta, CallbackInfo ci){
        if (entity instanceof EclipseSentinelEntity entity1) {
            double d = -MathHelper.lerp(tickDelta, entity.lastRenderX, entity.getX());
            double e = -MathHelper.lerp(tickDelta, entity.lastRenderY, entity.getY());
            double f = -MathHelper.lerp(tickDelta, entity.lastRenderZ, entity.getZ());

            for (EclipseSentinelPartEntity partEntity : entity1.getBodyParts()) {
                matrices.push();
                double g = d + MathHelper.lerp(tickDelta, partEntity.lastRenderX, partEntity.getX());
                double h = e + MathHelper.lerp(tickDelta, partEntity.lastRenderY, partEntity.getY());
                double i = f + MathHelper.lerp(tickDelta, partEntity.lastRenderZ, partEntity.getZ());
                matrices.translate(g, h, i);
                WorldRenderer.drawBox(
                        matrices,
                        vertices,
                        partEntity.getBoundingBox().offset(-partEntity.getX(), -partEntity.getY(), -partEntity.getZ()),
                        0.25F,
                        1.0F,
                        0.0F,
                        1.0F
                );
                matrices.pop();
            }
        }
    }
}