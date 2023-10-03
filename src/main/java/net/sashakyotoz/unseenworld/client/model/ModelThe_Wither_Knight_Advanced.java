package net.sashakyotoz.unseenworld.client.model;

import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.ArmedModel;

import net.sashakyotoz.unseenworld.entity.TheWitherKnightEntity;
import net.sashakyotoz.unseenworld.entity.animations.AdvancedWitherKnightAnimations;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class ModelThe_Wither_Knight_Advanced<T extends TheWitherKnightEntity> extends HierarchicalModel<T> implements ArmedModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("unseen_world", "model_the_wither_knight_advanced"), "main");
	public final ModelPart Head;
	public final ModelPart Body;
	public final ModelPart rods;
	public final ModelPart right_arm;
	public final ModelPart left_arm;
	public final ModelPart right_leg;
	public final ModelPart left_leg;
	public final ModelPart right_boot;
	public final ModelPart left_boot;
	public final ModelPart root;
	public final ModelPart right_wing;
	public final ModelPart left_wing;

	public ModelThe_Wither_Knight_Advanced(ModelPart root) {
		this.Head = root.getChild("Head");
		this.Body = root.getChild("Body");
		this.rods = root.getChild("rods");
		this.right_arm = root.getChild("right_arm");
		this.left_arm = root.getChild("left_arm");
		this.right_leg = root.getChild("right_leg");
		this.left_leg = root.getChild("left_leg");
		this.right_boot = root.getChild("right_boot");
		this.left_boot = root.getChild("left_boot");
		this.root = root;
		this.right_wing = rods.getChild("right_wing");
		this.left_wing = rods.getChild("left_wing");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition Head = partdefinition.addOrReplaceChild("Head",
				CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.0F)).texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.5F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(1.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition belt = Body.addOrReplaceChild("belt", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition rods = partdefinition.addOrReplaceChild("rods", CubeListBuilder.create().texOffs(2, 36).addBox(-1.0F, 0.0F, -10.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 13.0F));
		PartDefinition right_wing = rods.addOrReplaceChild("right_wing",
				CubeListBuilder.create().texOffs(2, 36).addBox(-9.5F, -3.0F, 0.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(102, 0).addBox(-22.0F, -6.0F, 1.0F, 13.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, -10.0F, 0.0F, 0.0873F, 0.0F));
		PartDefinition cube_r1 = right_wing.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(102, 0).addBox(-16.0F, -14.0F, 1.0F, 13.0F, 11.0F, 0.0F, new CubeDeformation(0.002F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.3963F));
		PartDefinition cube_r2 = right_wing.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(2, 36).addBox(0.0F, -8.0F, -10.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(-0.0002F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 10.0F, 0.0F, 0.0F, -1.5708F));
		PartDefinition left_wing = rods.addOrReplaceChild("left_wing",
				CubeListBuilder.create().texOffs(2, 36).addBox(7.5F, -3.0F, 0.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(102, 0).mirror().addBox(9.0F, -6.0F, 1.0F, 13.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, 0.0F, -10.0F, 0.0F, -0.0873F, 0.0F));
		PartDefinition cube_r3 = left_wing.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(102, 0).mirror().addBox(3.0F, -14.0F, 1.0F, 13.0F, 11.0F, 0.0F, new CubeDeformation(0.002F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.3963F));
		PartDefinition cube_r4 = left_wing.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(2, 36).addBox(0.0F, -0.5F, -10.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 10.0F, 0.0F, 0.0F, -1.5708F));
		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm",
				CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)).texOffs(64, 16).mirror().addBox(-3.5F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)).mirror(false),
				PartPose.offset(-5.0F, 2.0F, 0.0F));
		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm",
				CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)).mirror(false).texOffs(64, 16).addBox(-0.5F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)),
				PartPose.offsetAndRotation(5.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5101F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 32).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.51F)).mirror(false),
				PartPose.offset(1.9F, 12.0F, 0.0F));
		PartDefinition right_boot = partdefinition.addOrReplaceChild("right_boot", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.01F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
		PartDefinition left_boot = partdefinition.addOrReplaceChild("left_boot", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.01F)).mirror(false),
				PartPose.offset(1.9F, 12.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rods.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_boot.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_boot.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.Head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.Head.xRot = headPitch / (180F / (float) Math.PI);
		this.animate(entity.idle1AnimationState, AdvancedWitherKnightAnimations.IDLE, ageInTicks);
		this.animate(entity.flyAnimationState, AdvancedWitherKnightAnimations.FLY, ageInTicks);
		this.animate(entity.flyingAttackAnimationState, AdvancedWitherKnightAnimations.ATTACK, ageInTicks);
	}

	@Override
	public void translateToHand(HumanoidArm p_103392_, PoseStack p_103393_) {
		ModelPart modelpart = this.left_arm;
		float f = 0.5F * (float) (p_103392_ == HumanoidArm.LEFT ? 1 : -1);
		modelpart.x += f;
		modelpart.translateAndRotate(p_103393_);
		modelpart.x -= f;
	}
}
