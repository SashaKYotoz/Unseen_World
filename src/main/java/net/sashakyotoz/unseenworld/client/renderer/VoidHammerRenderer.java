package net.sashakyotoz.unseenworld.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.sashakyotoz.unseenworld.client.model.ModelVoidHammer;
import net.sashakyotoz.unseenworld.entity.VoidHammerEntity;

public class VoidHammerRenderer extends EntityRenderer<VoidHammerEntity> {
    private static final ResourceLocation texture = new ResourceLocation("unseen_world:textures/entities/void_hammer_entity.png");
    private final ModelVoidHammer model;

    public VoidHammerRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new ModelVoidHammer(context.bakeLayer(ModelVoidHammer.LAYER_LOCATION));
    }

    @Override
    public void render(VoidHammerEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        VertexConsumer vb = bufferIn.getBuffer(RenderType.entityCutout(this.getTextureLocation(entityIn)));
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot()) - 90));
        poseStack.mulPose(Axis.ZP.rotationDegrees(90 + Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot())));
        model.renderToBuffer(poseStack, vb, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 0.0625f);
        poseStack.popPose();
        super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(VoidHammerEntity entity) {
        return texture;
    }
}
