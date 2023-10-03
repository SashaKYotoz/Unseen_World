package net.sashakyotoz.unseenworld.client.renderer;

import net.sashakyotoz.unseenworld.client.model.ModelTealivy_Void_Spear;
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import net.sashakyotoz.unseenworld.entity.TealivyVoidSpearEntity;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class TealivyVoidSpearRenderer extends EntityRenderer<TealivyVoidSpearEntity> {
	private static final ResourceLocation texture = new ResourceLocation("unseen_world:textures/entities/tealivy_void_spear.png");
	private final ModelTealivy_Void_Spear model;

	public TealivyVoidSpearRenderer(EntityRendererProvider.Context context) {
		super(context);
		model = new ModelTealivy_Void_Spear(context.bakeLayer(ModelTealivy_Void_Spear.LAYER_LOCATION));
	}

	@Override
	public void render(TealivyVoidSpearEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
		VertexConsumer vb = bufferIn.getBuffer(RenderType.entityCutout(this.getTextureLocation(entityIn)));
		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot()) - 90));
		poseStack.mulPose(Axis.ZP.rotationDegrees(90 + Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot())));
		model.renderToBuffer(poseStack, vb, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 0.0625f);
		poseStack.popPose();
		super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getTextureLocation(TealivyVoidSpearEntity entity) {
		return texture;
	}
}
