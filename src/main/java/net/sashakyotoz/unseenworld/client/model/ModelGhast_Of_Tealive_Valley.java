package net.sashakyotoz.unseenworld.client.model;

import net.minecraft.world.entity.Entity;
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

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class ModelGhast_Of_Tealive_Valley<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("unseen_world", "model_ghast_of_tealive_valley"), "main");
	public final ModelPart body;
	public final ModelPart tail;
	public final ModelPart tail2;
	public final ModelPart tail3;
	public final ModelPart rightwing;
	public final ModelPart leftwing;

	public ModelGhast_Of_Tealive_Valley(ModelPart root) {
		this.body = root.getChild("body");
		this.tail = root.getChild("tail");
		this.tail2 = root.getChild("tail2");
		this.tail3 = root.getChild("tail3");
		this.rightwing = root.getChild("rightwing");
		this.leftwing = root.getChild("leftwing");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition body = partdefinition.addOrReplaceChild("body",
				CubeListBuilder.create().texOffs(0, 40).addBox(-6.0F, -10.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(0, 15).addBox(-6.0F, -10.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.1F)),
				PartPose.offset(0.0F, 16.0F, 0.0F));
		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(32, 0).addBox(-8.0F, -7.0F, 0.0F, 16.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, 0.2618F, 0.0F, 0.0F));
		PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, 17.5F, -4.0F));
		PartDefinition cube_r2 = tail.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(5.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(-0.1F)).texOffs(0, 0)
				.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(-0.1F)).texOffs(0, 0).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(-0.1F)).mirror(false),
				PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));
		PartDefinition tail2 = partdefinition.addOrReplaceChild("tail2", CubeListBuilder.create(), PartPose.offset(0.0F, 17.5F, 0.0F));
		PartDefinition cube_r3 = tail2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(-0.1F)).mirror(false),
				PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, 0.1745F, 0.0F, -0.0436F));
		PartDefinition cube_r4 = tail2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(2.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));
		PartDefinition cube_r5 = tail2.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(-0.1F)),
				PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0436F));
		PartDefinition tail3 = partdefinition.addOrReplaceChild("tail3", CubeListBuilder.create(), PartPose.offset(0.0F, 17.5F, 4.0F));
		PartDefinition cube_r6 = tail3.addOrReplaceChild("cube_r6",
				CubeListBuilder.create().texOffs(0, 0).addBox(5.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(-0.1F)).texOffs(0, 0).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(-0.1F)).mirror(false),
				PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));
		PartDefinition rightwing = partdefinition.addOrReplaceChild("rightwing", CubeListBuilder.create(), PartPose.offset(-6.0F, 12.0F, 0.0F));
		PartDefinition cube_r7 = rightwing.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(32, 7).addBox(-5.5F, -1.0F, 0.0F, 6.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.4363F, 0.4363F, 0.0F));
		PartDefinition leftwing = partdefinition.addOrReplaceChild("leftwing", CubeListBuilder.create(), PartPose.offset(6.0F, 12.0F, 0.0F));
		PartDefinition cube_r8 = leftwing.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(32, 7).mirror().addBox(-0.5F, -1.0F, 0.0F, 6.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.4363F, -0.4363F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		tail2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		tail3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rightwing.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leftwing.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.tail2.xRot = Mth.cos(limbSwing * 0.3F) * limbSwingAmount;
		this.tail3.xRot = Mth.cos(limbSwing * 0.4F + (float) Math.PI) * limbSwingAmount;
		this.tail.xRot = Mth.cos(limbSwing * 0.3F + (float) Math.PI) * limbSwingAmount;
		this.rightwing.yRot = Mth.cos(limbSwing * 0.95F + (float) Math.PI) * limbSwingAmount;
		this.leftwing.yRot = Mth.cos(limbSwing * 0.95F) * limbSwingAmount;
	}
}
