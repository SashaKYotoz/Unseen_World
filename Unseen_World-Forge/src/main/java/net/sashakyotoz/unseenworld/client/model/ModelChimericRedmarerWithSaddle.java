package net.sashakyotoz.unseenworld.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class ModelChimericRedmarerWithSaddle<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("unseen_world", "model_chimeric_redmarer_with_saddle"), "main");
	public final ModelPart head;
	public final ModelPart body;
	public final ModelPart tail;
	public final ModelPart forwardlegs;
	public final ModelPart backrightLeg;
	public final ModelPart backleftLeg;

	public ModelChimericRedmarerWithSaddle(ModelPart root) {
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.tail = root.getChild("tail");
		this.forwardlegs = root.getChild("forwardlegs");
		this.backrightLeg = root.getChild("backrightLeg");
		this.backleftLeg = root.getChild("backleftLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 4.0F, -13.5F));
		PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1",
				CubeListBuilder.create().texOffs(104, 35).mirror().addBox(6.75F, 0.0F, -9.0F, 2.0F, 4.0F, 10.0F, new CubeDeformation(0.25F)).mirror(false).texOffs(104, 35).addBox(-8.75F, 0.0F, -9.0F, 2.0F, 4.0F, 10.0F, new CubeDeformation(0.25F))
						.texOffs(0, 58).mirror().addBox(7.0F, -6.0F, -10.0F, 2.0F, 4.0F, 10.0F, new CubeDeformation(0.5F)).mirror(false).texOffs(0, 58).addBox(-9.0F, -6.0F, -10.0F, 2.0F, 4.0F, 10.0F, new CubeDeformation(0.5F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, -0.1745F, 0.0F, 0.0F));
		PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(84, 114).addBox(-6.0F, 5.0F, -7.0F, 12.0F, 4.0F, 10.0F, new CubeDeformation(0.25F)).texOffs(0, 0)
				.addBox(-6.0F, 5.0F, -7.0F, 12.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(0, 34).addBox(-6.5F, -7.0F, -8.0F, 13.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, -0.0873F, 0.0F, 0.0F));
		PartDefinition body = partdefinition.addOrReplaceChild("body",
				CubeListBuilder.create().texOffs(68, 55).addBox(-7.0F, -14.0F, -8.0F, 14.0F, 11.0F, 16.0F, new CubeDeformation(0.5F)).texOffs(2, 78).addBox(-7.0F, -14.5F, -8.0F, 14.0F, 1.0F, 16.0F, new CubeDeformation(0.75F)),
				PartPose.offset(0.0F, 17.0F, 0.0F));
		PartDefinition cube_r3 = body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(90, 0).addBox(-7.5F, -7.0F, -2.0F, 15.0F, 15.0F, 4.0F, new CubeDeformation(0.75F)),
				PartPose.offsetAndRotation(0.0F, -12.0F, -8.0F, -0.2618F, 0.0F, 0.0F));
		PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, 8.0F));
		PartDefinition cube_r4 = tail.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 101).addBox(-5.0F, -4.0F, 0.0F, 10.0F, 8.0F, 12.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));
		PartDefinition forwardlegs = partdefinition.addOrReplaceChild(
				"forwardlegs", CubeListBuilder.create().texOffs(48, 0).addBox(3.0F, 8.0F, -5.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(48, 0).mirror().addBox(-7.0F, 8.0F, -5.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
						.mirror(false).texOffs(48, 16).addBox(3.0F, 3.0F, -5.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)).texOffs(48, 16).addBox(-7.0F, 3.0F, -5.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)),
				PartPose.offset(0.0F, 12.0F, -3.0F));
		PartDefinition backrightLeg = partdefinition.addOrReplaceChild("backrightLeg",
				CubeListBuilder.create().texOffs(48, 0).mirror().addBox(-1.0F, 4.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(48, 16).addBox(-1.0F, -1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)),
				PartPose.offset(-6.0F, 16.0F, 6.0F));
		PartDefinition backleftLeg = partdefinition.addOrReplaceChild("backleftLeg",
				CubeListBuilder.create().texOffs(48, 0).addBox(-3.0F, 4.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(48, 16).addBox(-3.0F, -1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.5F)),
				PartPose.offset(6.0F, 16.0F, 6.0F));
		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		forwardlegs.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		backrightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		backleftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);
		this.backleftLeg.xRot = Mth.cos(limbSwing) * -1.0F * limbSwingAmount;
		this.tail.zRot = netHeadYaw / (180F / (float) Math.PI);
		this.backrightLeg.xRot = Mth.cos(limbSwing) * 1.0F * limbSwingAmount;
		this.forwardlegs.xRot = Mth.cos(limbSwing) * 1.0F * limbSwingAmount;
	}
}
