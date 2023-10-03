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

// Made with Blockbench 4.2.4
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports
public class ModelCavern_Scarecrow<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("unseen_world", "model_cavern_scarecrow"), "main");
	public final ModelPart head;
	public final ModelPart jaw;
	public final ModelPart body;
	public final ModelPart forwardLegs;
	public final ModelPart backLegs;

	public ModelCavern_Scarecrow(ModelPart root) {
		this.head = root.getChild("head");
		this.jaw = root.getChild("jaw");
		this.body = root.getChild("body");
		this.forwardLegs = root.getChild("forwardLegs");
		this.backLegs = root.getChild("backLegs");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, -6.0F, 7.0F, 4.0F, 6.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 17.5F, -3.0F));
		PartDefinition jaw = partdefinition.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(38, 0).addBox(-4.0F, 0.0F, -6.25F, 7.0F, 3.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offset(0.5F, 17.5F, -3.0F));
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 18.0F, 0.0F));
		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1",
				CubeListBuilder.create().texOffs(0, 16).addBox(-0.5F, -5.0F, -2.5F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(0, 32).addBox(-3.5F, -2.0F, -3.5F, 7.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0F));
		PartDefinition forwardLegs = partdefinition.addOrReplaceChild("forwardLegs",
				CubeListBuilder.create().texOffs(52, 16).addBox(-5.0F, 0.0F, -1.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(52, 16).mirror().addBox(2.0F, 0.0F, -1.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(0.0F, 18.0F, -3.0F));
		PartDefinition backLegs = partdefinition.addOrReplaceChild("backLegs",
				CubeListBuilder.create().texOffs(52, 32).addBox(-5.0F, 0.0F, -2.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(52, 32).mirror().addBox(2.0F, 0.0F, -2.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(0.0F, 19.0F, 3.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		jaw.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		forwardLegs.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		backLegs.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);
		this.jaw.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.jaw.xRot = headPitch / (180F / (float) Math.PI);
		this.backLegs.xRot = Mth.cos(limbSwing * 1.0F) * -1.0F * limbSwingAmount;
		this.forwardLegs.xRot = Mth.cos(limbSwing * 1.0F) * 1.0F * limbSwingAmount;
	}
}
