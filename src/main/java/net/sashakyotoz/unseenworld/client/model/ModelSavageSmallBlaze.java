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

public class ModelSavageSmallBlaze<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("unseen_world", "model_savage_small_blaze"), "main");
	public final ModelPart head;
	public final ModelPart tail;
	public final ModelPart rightArm;
	public final ModelPart leftArm;

	public ModelSavageSmallBlaze(ModelPart root) {
		this.head = root.getChild("head");
		this.tail = root.getChild("tail");
		this.rightArm = root.getChild("rightArm");
		this.leftArm = root.getChild("leftArm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition head = partdefinition.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(0, 48).addBox(-4.0F, -8.0F, -4.5F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(32, 48).addBox(-4.0F, -8.0F, -4.5F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F)),
				PartPose.offset(0.0F, 8.0F, 0.0F));
		PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, 11.0F, 0.0F));
		PartDefinition cube_r1 = tail.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(20, 0).addBox(-4.5F, 0.25F, -2.5F, 9.0F, 5.0F, 5.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0436F, 0.0F, 0.0F));
		PartDefinition cube_r2 = tail.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -0.3067F, -2.5794F, 5.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.2618F, 0.0F, 0.0F));
		PartDefinition rightArm = partdefinition.addOrReplaceChild("rightArm", CubeListBuilder.create(), PartPose.offset(-5.0F, 10.5F, -1.0F));
		PartDefinition cube_r3 = rightArm.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, -3.5F, -3.0F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(5.0F, 2.0F, 0.0F, -1.0472F, 0.0F, 0.0873F));
		PartDefinition cube_r4 = rightArm.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-10.25F, 0.25F, -2.75F, 5.0F, 9.0F, 5.0F, new CubeDeformation(-0.5F)),
				PartPose.offsetAndRotation(5.0F, 2.0F, 0.0F, -1.0472F, -0.0873F, 0.0873F));
		PartDefinition leftArm = partdefinition.addOrReplaceChild("leftArm", CubeListBuilder.create(), PartPose.offset(5.0F, 11.0F, 0.0F));
		PartDefinition cube_r5 = leftArm.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(5.0F, -3.5F, -3.0F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-5.0F, 1.5F, -1.0F, -1.0472F, 0.0F, -0.0873F));
		PartDefinition cube_r6 = leftArm.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 0).addBox(5.25F, 0.25F, -2.75F, 5.0F, 9.0F, 5.0F, new CubeDeformation(-0.5F)),
				PartPose.offsetAndRotation(-5.0F, 1.5F, -1.0F, -1.0472F, 0.0873F, -0.0873F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);
		this.tail.zRot = Mth.cos(limbSwing * 0.75F) * 0.5F * limbSwingAmount;
		this.tail.xRot = Mth.cos(limbSwing * 0.75F) * 1.0F * limbSwingAmount;
		this.rightArm.xRot = Mth.cos(limbSwing * 0.75F + (float) Math.PI) * limbSwingAmount + 0.5f;
		this.leftArm.xRot = Mth.cos(limbSwing * 0.75F) * limbSwingAmount + 0.5f;
	}
}
