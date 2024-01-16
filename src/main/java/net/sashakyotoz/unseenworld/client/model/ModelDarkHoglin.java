package net.sashakyotoz.unseenworld.client.model;

import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.EntityModel;

import net.sashakyotoz.unseenworld.entity.DarkHoglinEntity;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class ModelDarkHoglin<T extends DarkHoglinEntity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("unseen_world", "model_dark_hoglin"), "main");
	public final ModelPart head;
	public final ModelPart body;
	public final ModelPart rightEar;
	public final ModelPart leftEar;
	public final ModelPart rightFrontLeg;
	public final ModelPart leftFrontLeg;
	public final ModelPart rightHindLeg;
	public final ModelPart leftHindLeg;

	public ModelDarkHoglin(ModelPart root) {
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.rightEar = this.head.getChild("right_ear");
		this.leftEar = this.head.getChild("left_ear");
		this.rightFrontLeg = root.getChild("rightFrontLeg");
		this.leftFrontLeg = root.getChild("leftFrontLeg");
		this.rightHindLeg = root.getChild("rightHindLeg");
		this.leftHindLeg = root.getChild("leftHindLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(61, 1).addBox(-7.0F, -4.8767F, -17.5912F, 14.0F, 6.0F, 19.0F, new CubeDeformation(0.0F)).texOffs(1, 13)
				.addBox(-8.0F, -10.8767F, -12.5912F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(1, 13).mirror().addBox(6.0F, -10.8767F, -12.5912F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, 3.0F, -13.0F, 0.8727F, 0.0F, 0.0F));
		PartDefinition right_ear = head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(1, 1).addBox(-5.8572F, 0.266F, -2.0F, 6.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-7.0F, -4.5F, -2.0F, 0.0F, 0.0F, -0.8727F));
		PartDefinition left_ear = head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(1, 6).addBox(-0.25F, 0.25F, -2.0F, 6.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(7.0F, -4.5F, -2.0F, 0.0F, 0.0F, 0.8727F));
		PartDefinition body = partdefinition.addOrReplaceChild("body",
				CubeListBuilder.create().texOffs(1, 1).addBox(-8.0F, -6.0F, -11.0F, 16.0F, 14.0F, 26.0F, new CubeDeformation(0.02F)).texOffs(90, 33).addBox(0.0F, -13.0F, -14.0F, 0.0F, 10.0F, 19.0F, new CubeDeformation(0.02F)),
				PartPose.offset(0.0F, 5.0F, -3.0F));
		PartDefinition rightFrontLeg = partdefinition.addOrReplaceChild("rightFrontLeg", CubeListBuilder.create().texOffs(66, 42).addBox(-2.0F, -2.0F, -3.0F, 6.0F, 14.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 12.0F, -10.0F));
		PartDefinition leftFrontLeg = partdefinition.addOrReplaceChild("leftFrontLeg", CubeListBuilder.create().texOffs(41, 42).mirror().addBox(-4.0F, -2.0F, -3.0F, 6.0F, 14.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(6.0F, 12.0F, -10.0F));
		PartDefinition rightHindLeg = partdefinition.addOrReplaceChild("rightHindLeg", CubeListBuilder.create().texOffs(21, 45).mirror().addBox(-3.0F, -1.0F, -2.0F, 5.0F, 12.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(-5.0F, 13.0F, 8.0F));
		PartDefinition leftHindLeg = partdefinition.addOrReplaceChild("leftHindLeg", CubeListBuilder.create().texOffs(0, 45).addBox(-2.0F, -1.0F, -2.0F, 5.0F, 12.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 13.0F, 8.0F));
		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, buffer, packedLight, packedOverlay);
		body.render(poseStack, buffer, packedLight, packedOverlay);
		rightHindLeg.render(poseStack, buffer, packedLight, packedOverlay);
		leftHindLeg.render(poseStack, buffer, packedLight, packedOverlay);
		rightFrontLeg.render(poseStack, buffer, packedLight, packedOverlay);
		leftFrontLeg.render(poseStack, buffer, packedLight, packedOverlay);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.rightEar.zRot = -0.7F - limbSwingAmount * Mth.sin(limbSwing);
		this.leftEar.zRot = 0.7F + limbSwingAmount * Mth.sin(limbSwing);
		this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
		int i = entity.getAttackAnimationRemainingTicks();
		float f = 1.0F - (float) Mth.abs(10 - 2 * i) / 10.0F;
		this.head.xRot = Mth.lerp(f, 0.875F, -0.35F);
		float f1 = 1.3F;
		this.rightFrontLeg.xRot = Mth.cos(limbSwing) * 1.3F * limbSwingAmount;
		this.leftFrontLeg.xRot = Mth.cos(limbSwing + (float) Math.PI) * 1.3F * limbSwingAmount;
		this.rightHindLeg.xRot = this.leftFrontLeg.xRot;
		this.leftHindLeg.xRot = this.rightFrontLeg.xRot;
	}
}
