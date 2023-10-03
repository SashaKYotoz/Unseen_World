package net.sashakyotoz.unseenworld.client.model;

import net.minecraft.world.entity.Entity;
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

// Made with Blockbench 4.6.0
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class Modelthe_blazer<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("unseen_world", "modelthe_blazer"), "main");
	public final ModelPart head;
	public final ModelPart Body;

	public Modelthe_blazer(ModelPart root) {
		this.head = root.getChild("head");
		this.Body = root.getChild("Body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -4.5F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(73, 0).mirror().addBox(-5.0F, -7.0F, -5.0F, 10.0F, 12.0F, 10.0F, new CubeDeformation(0.25F)).mirror(false).texOffs(0, 0).mirror()
				.addBox(-5.0F, -7.0F, -5.0F, 10.0F, 12.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));
		PartDefinition head_r1 = head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(41, 0).addBox(-4.0F, -6.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));
		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
		PartDefinition cube_r2 = Body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(12, 32).mirror().addBox(-5.5F, -7.4072F, -0.5126F, 11.0F, 18.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(-8.5F, 7.0F, 0.0F, -0.3491F, 1.5708F, 0.0F));
		PartDefinition cube_r3 = Body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 32).mirror().addBox(-5.5F, -8.048F, -0.7862F, 11.0F, 18.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(9.0F, 7.5F, 0.0F, 0.3491F, 1.5708F, 0.0F));
		PartDefinition cube_r4 = Body.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(12, 32).mirror().addBox(-5.5F, -7.7712F, -1.1058F, 11.0F, 18.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, 7.5F, -8.0F, -0.3491F, 0.0F, 0.0F));
		PartDefinition cube_r5 = Body.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 32).mirror().addBox(-5.5F, -8.1107F, -0.1278F, 11.0F, 18.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, 8.0F, 8.0F, 0.3491F, 0.0F, 0.0F));
		PartDefinition rods = Body.addOrReplaceChild("rods",
				CubeListBuilder.create().texOffs(52, 18).mirror().addBox(-1.5F, -11.0F, -1.5F, 3.0F, 20.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(52, 41).mirror()
						.addBox(-1.5F, -11.5F, -1.5F, 3.0F, 20.0F, 3.0F, new CubeDeformation(1.0F)).mirror(false).texOffs(47, 18).mirror().addBox(-3.5F, 0.0F, 2.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(47, 18).mirror()
						.addBox(2.0F, 0.0F, -3.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(47, 18).mirror().addBox(-3.5F, 0.0F, -3.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(47, 18).mirror()
						.addBox(2.0F, 0.0F, 2.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.5676F, 11.0F, -0.1824F, 0.0F, -2.3562F, 0.0F));
		PartDefinition shields = rods.addOrReplaceChild("shields", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -9.0F, 0.5F, 0.0F, 0.7854F, 0.0F));
		PartDefinition cube_r6 = shields.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 31).mirror().addBox(-5.5F, -8.048F, -3.7862F, 11.0F, 18.0F, 1.0F, new CubeDeformation(-1.0F)).mirror(false),
				PartPose.offsetAndRotation(9.0F, 5.5F, 0.0F, 0.0F, 1.5708F, -0.0436F));
		PartDefinition cube_r7 = shields.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(12, 32).mirror().addBox(-5.5F, -7.75F, 1.9F, 11.0F, 18.0F, 1.0F, new CubeDeformation(-1.0F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, 5.5F, -8.0F, -0.0436F, 0.0F, 0.0F));
		PartDefinition cube_r8 = shields.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(12, 32).mirror().addBox(-5.5F, -7.4072F, 2.4874F, 11.0F, 18.0F, 1.0F, new CubeDeformation(-1.0F)).mirror(false),
				PartPose.offsetAndRotation(-8.5F, 5.0F, 0.0F, 0.0F, 1.5708F, 0.0436F));
		PartDefinition cube_r9 = shields.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 32).mirror().addBox(-5.5F, -8.25F, -3.1F, 11.0F, 18.0F, 1.0F, new CubeDeformation(-1.0F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, 6.0F, 8.0F, 0.0436F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);
		this.Body.yRot = ageInTicks / 10F;
	}
}
