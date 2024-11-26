package net.sashakyotoz.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.custom.GleamcarverEntity;
import net.sashakyotoz.common.entities.animations.GleamcarverAnimations;

public class GleamcarverModel extends SinglePartEntityModel<GleamcarverEntity> {
	public static final EntityModelLayer GLEAMCARVER = new EntityModelLayer(UnseenWorld.makeID("gleamcarver"), "main");

	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart forwardRightPaw;
	private final ModelPart forwardLeftPaw;
	private final ModelPart middleRightPaw;
	private final ModelPart middleLeftPaw;
	private final ModelPart backRightPaw;
	private final ModelPart backLeftPaw;
	public GleamcarverModel(ModelPart root) {
		this.root = root;
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.forwardRightPaw = root.getChild("forwardRightPaw");
		this.forwardLeftPaw = root.getChild("forwardLeftPaw");
		this.middleRightPaw = root.getChild("middleRightPaw");
		this.middleLeftPaw = root.getChild("middleLeftPaw");
		this.backRightPaw = root.getChild("backRightPaw");
		this.backLeftPaw = root.getChild("backLeftPaw");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(50, 0).cuboid(-2.5F, -1.0F, -1.5F, 5.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 21.0F, -5.0F));

		ModelPartData cube_r1 = head.addChild("cube_r1", ModelPartBuilder.create().uv(30, -2).cuboid(0.0F, -2.0F, -1.0F, 0.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, -1.0F, -0.5F, 0.0F, -0.1745F, 0.0F));

		ModelPartData cube_r2 = head.addChild("cube_r2", ModelPartBuilder.create().uv(30, -2).cuboid(0.0F, -2.0F, -1.0F, 0.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -1.0F, -0.5F, 0.0F, 0.1745F, 0.0F));

		ModelPartData cube_r3 = head.addChild("cube_r3", ModelPartBuilder.create().uv(32, 0).cuboid(-1.5F, 0.1F, -1.0F, 3.0F, 0.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, -1.75F, 0.1745F, 0.0F, 0.0F));

		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -5.0F, -4.5F, 8.0F, 5.0F, 9.0F, new Dilation(0.0F))
		.uv(40, -3).cuboid(0.0F, -4.0F, 4.5F, 0.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 23.0F, 0.0F));

		ModelPartData cube_r4 = body.addChild("cube_r4", ModelPartBuilder.create().uv(32, 16).cuboid(-8.0F, -16.0F, 0.0F, 16.0F, 16.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, 0.0F, 0.0F, -2.3562F, 0.0F));

		ModelPartData cube_r5 = body.addChild("cube_r5", ModelPartBuilder.create().uv(0, 24).cuboid(-8.0F, -8.0F, 0.0F, 16.0F, 8.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.0F, 0.0F, 0.0F, 0.0436F, 0.0F));

		ModelPartData cube_r6 = body.addChild("cube_r6", ModelPartBuilder.create().uv(32, 16).mirrored().cuboid(-8.0F, -16.0F, 0.0F, 16.0F, 16.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, -2.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		ModelPartData forwardRightPaw = modelPartData.addChild("forwardRightPaw", ModelPartBuilder.create(), ModelTransform.of(0.0F, 24.5F, -3.0F, 0.0F, -0.2618F, 0.0F));

		ModelPartData cube_r7 = forwardRightPaw.addChild("cube_r7", ModelPartBuilder.create().uv(52, 8).cuboid(-4.0F, 1.0F, -1.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, -3.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

		ModelPartData forwardLeftPaw = modelPartData.addChild("forwardLeftPaw", ModelPartBuilder.create(), ModelTransform.of(0.0F, 23.5F, -3.0F, 0.0F, 0.2618F, 0.0F));

		ModelPartData cube_r8 = forwardLeftPaw.addChild("cube_r8", ModelPartBuilder.create().uv(52, 5).cuboid(0.0F, 1.0F, -1.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		ModelPartData middleRightPaw = modelPartData.addChild("middleRightPaw", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.5F, 0.0F));

		ModelPartData cube_r9 = middleRightPaw.addChild("cube_r9", ModelPartBuilder.create().uv(52, 8).cuboid(-4.0F, 1.0F, -1.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, -3.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

		ModelPartData middleLeftPaw = modelPartData.addChild("middleLeftPaw", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 23.5F, 0.0F));

		ModelPartData cube_r10 = middleLeftPaw.addChild("cube_r10", ModelPartBuilder.create().uv(52, 5).cuboid(0.0F, 1.0F, -1.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		ModelPartData backRightPaw = modelPartData.addChild("backRightPaw", ModelPartBuilder.create(), ModelTransform.of(0.0F, 24.5F, 3.0F, 0.0F, 0.2618F, 0.0F));

		ModelPartData cube_r11 = backRightPaw.addChild("cube_r11", ModelPartBuilder.create().uv(52, 8).cuboid(-4.0F, 1.0F, -1.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, -3.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

		ModelPartData backLeftPaw = modelPartData.addChild("backLeftPaw", ModelPartBuilder.create(), ModelTransform.of(0.0F, 23.5F, 3.0F, 0.0F, -0.2618F, 0.0F));

		ModelPartData cube_r12 = backLeftPaw.addChild("cube_r12", ModelPartBuilder.create().uv(52, 5).cuboid(0.0F, 1.0F, -1.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.2618F));
		return TexturedModelData.of(modelData, 64, 32);
	}
	@Override
	public void setAngles(GleamcarverEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.getPart().traverse().forEach(ModelPart::resetTransform);
		this.animateMovement(GleamcarverAnimations.WALK,limbSwing,limbSwingAmount,4.0F, 2.5F);
		this.updateAnimation(entity.death, GleamcarverAnimations.DEATH,ageInTicks);
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		forwardRightPaw.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		forwardLeftPaw.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		middleRightPaw.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		middleLeftPaw.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		backRightPaw.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		backLeftPaw.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart getPart() {
		return this.root;
	}
}