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
import net.sashakyotoz.unseenworld.entity.VoidEndermanEntity;

public class ModelVoidEndermen<T extends VoidEndermanEntity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("unseen_world", "modelvoid_endermen"), "main");
	public final ModelPart head;
	public final ModelPart chin;
	public final ModelPart Body;
	public final ModelPart RightArm;
	public final ModelPart LeftArm;
	public final ModelPart RightLeg;
	public final ModelPart LeftLeg;

	public ModelVoidEndermen(ModelPart root) {
		this.head = root.getChild("head");
		this.chin = head.getChild("chin");
		this.Body = root.getChild("Body");
		this.RightArm = root.getChild("RightArm");
		this.LeftArm = root.getChild("LeftArm");
		this.RightLeg = root.getChild("RightLeg");
		this.LeftLeg = root.getChild("LeftLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F)).texOffs(0, 0)
				.addBox(3.5F, -6.0F, 0.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.25F)).texOffs(0, 0).mirror().addBox(-6.5F, -6.0F, 0.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offset(0.0F, -16.0F, 0.0F));
		PartDefinition chin = head.addOrReplaceChild("chin", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.4F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition Body = partdefinition.addOrReplaceChild("Body",
				CubeListBuilder.create().texOffs(32, 16).addBox(-4.0F, -2.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(32, 0).addBox(-4.0F, -2.0F, -2.0F, 8.0F, 11.0F, 4.0F, new CubeDeformation(0.5F)),
				PartPose.offset(0.0F, -14.0F, 0.0F));
		PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm",
				CubeListBuilder.create().texOffs(56, 0).addBox(-3.0F, -4.0F, -1.0F, 2.0F, 30.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(56, 10).addBox(-5.0F, 8.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-3.0F, -12.0F, 0.0F));
		PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm",
				CubeListBuilder.create().texOffs(56, 0).mirror().addBox(-1.0F, -4.0F, -1.0F, 2.0F, 30.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(56, 10).addBox(1.0F, 8.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(5.0F, -12.0F, 0.0F));
		PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(56, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 30.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -2.0F, 0.0F));
		PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(56, 0).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 30.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(2.0F, -2.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, buffer, packedLight, packedOverlay);
		Body.render(poseStack, buffer, packedLight, packedOverlay);
		RightArm.render(poseStack, buffer, packedLight, packedOverlay);
		LeftArm.render(poseStack, buffer, packedLight, packedOverlay);
		RightLeg.render(poseStack, buffer, packedLight, packedOverlay);
		LeftLeg.render(poseStack, buffer, packedLight, packedOverlay);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);
		this.RightLeg.xRot = Mth.cos(limbSwing) * 1.0F * limbSwingAmount;
		this.RightArm.xRot = Mth.cos(limbSwing * 0.5F + (float) Math.PI) * limbSwingAmount;
		this.LeftArm.xRot = Mth.cos(limbSwing * 0.5F) * limbSwingAmount;
		this.LeftLeg.xRot = Mth.cos(limbSwing) * -1.0F * limbSwingAmount;
		int i = entity.getAttackAnimationRemainingTicks();
		if (i > 0) {
			this.head.y = -19.0F;
			this.chin.y = +4.0F;
			this.RightArm.xRot = Mth.cos(limbSwing * 0.75F + (float) Math.PI) * limbSwingAmount - 0.25F;
			this.LeftArm.xRot = Mth.cos(limbSwing * 0.75F) * limbSwingAmount - 0.25F;
		} else {
			this.head.y = -16.0F;
			this.chin.y = 2.0F;
		}
	}
}
