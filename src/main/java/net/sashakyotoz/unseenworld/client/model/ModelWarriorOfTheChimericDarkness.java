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

import net.sashakyotoz.unseenworld.entity.DarkGolemEntity;
import net.sashakyotoz.unseenworld.entity.animations.DarkGolemAnimations;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class ModelWarriorOfTheChimericDarkness<T extends DarkGolemEntity> extends HierarchicalModel<T> implements ArmedModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("unseen_world", "model_warrior_of_the_chimeric_darkness"), "main");
	public final ModelPart root;
	public final ModelPart head;
	public final ModelPart head1;
	public final ModelPart body;
	public final ModelPart rightArm;
	public final ModelPart leftArm;
	public final ModelPart rightLeg;
	public final ModelPart leftLeg;

	public ModelWarriorOfTheChimericDarkness(ModelPart root) {
		this.root = root;
		this.head = root.getChild("head");
		this.head1 = head.getChild("head1");
		this.body = root.getChild("body");
		this.rightArm = root.getChild("rightArm");
		this.leftArm = root.getChild("leftArm");
		this.rightLeg = root.getChild("rightLeg");
		this.leftLeg = root.getChild("leftLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition head = partdefinition.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -7.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F)).texOffs(59, 22).addBox(4.0F, -4.0F, -6.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(59, 22).mirror()
						.addBox(-5.0F, -4.0F, -6.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(63, 1).mirror().addBox(-1.5F, -7.0F, -8.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(15, 29)
						.addBox(4.0F, -8.0F, -3.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(15, 29).mirror().addBox(-5.0F, -8.0F, -3.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(0.0F, 0.0F, -5.0F));
		PartDefinition head1 = head.addOrReplaceChild("head1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition cube_r1 = head1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 24).addBox(-4.0F, 1.5F, -7.0F, 8.0F, 3.0F, 8.0F, new CubeDeformation(0.15F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.1745F, 0.0F, 0.0F));
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(1, 48).addBox(-7.0F, -7.0F, -3.0F, 14.0F, 9.0F, 7.0F, new CubeDeformation(1.0F)).texOffs(10, 55)
				.addBox(-6.0F, -7.0F, -5.0F, 12.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 24).addBox(-6.0F, -17.0F, -3.0F, 12.0F, 9.0F, 7.0F, new CubeDeformation(0.75F)), PartPose.offset(0.0F, 12.0F, 0.0F));
		PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2",
				CubeListBuilder.create().texOffs(63, 1).mirror().addBox(-4.5F, 1.0F, -10.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(63, 1).addBox(1.5F, -2.0F, -10.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -13.0F, -5.0F, 0.0F, 3.1416F, 0.0F));
		PartDefinition rightArm = partdefinition.addOrReplaceChild("rightArm", CubeListBuilder.create(), PartPose.offset(-6.0F, 1.0F, 1.0F));
		PartDefinition cube_r3 = rightArm.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(104, 52).mirror().addBox(-7.0F, 11.5F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.3F)).mirror(false),
				PartPose.offsetAndRotation(-1.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.0873F));
		PartDefinition cube_r4 = rightArm.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(108, 28).addBox(-7.0F, 1.5F, -2.0F, 6.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, -0.0873F, 0.1309F, 0.0873F));
		PartDefinition cube_r5 = rightArm.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(104, 0).addBox(-6.0F, -1.5F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(1.0F)),
				PartPose.offsetAndRotation(-1.0F, -3.0F, 0.0F, 0.0F, 0.0F, 0.1309F));
		PartDefinition leftArm = partdefinition.addOrReplaceChild("leftArm", CubeListBuilder.create(), PartPose.offset(6.0F, 1.0F, 1.0F));
		PartDefinition cube_r6 = leftArm.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(104, 52).addBox(1.0F, 11.5F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.3F)),
				PartPose.offsetAndRotation(1.0F, -2.0F, 0.0F, 0.0F, 0.0F, -0.0873F));
		PartDefinition cube_r7 = leftArm.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(108, 28).mirror().addBox(1.0F, 1.5F, -1.0F, 6.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(1.0F, 0.0F, -1.0F, -0.0873F, -0.1309F, -0.0873F));
		PartDefinition cube_r8 = leftArm.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(104, 0).mirror().addBox(0.0F, -1.5F, -2.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(1.0F)).mirror(false),
				PartPose.offsetAndRotation(1.0F, -3.0F, -1.0F, 0.0F, 0.0F, -0.1309F));
		PartDefinition rightLeg = partdefinition.addOrReplaceChild("rightLeg", CubeListBuilder.create(), PartPose.offset(-3.0F, 15.0F, 0.0F));
		PartDefinition cube_r9 = rightLeg.addOrReplaceChild("cube_r9",
				CubeListBuilder.create().texOffs(50, 30).mirror().addBox(-4.0F, -8.0F, -1.0F, 4.0F, 5.0F, 5.0F, new CubeDeformation(1.0F)).mirror(false).texOffs(28, 30).addBox(-4.0F, -1.0F, -3.0F, 4.0F, 1.0F, 7.0F, new CubeDeformation(1.0F)),
				PartPose.offsetAndRotation(0.0F, 8.0F, -1.0F, 0.0F, 0.0873F, 0.0F));
		PartDefinition leftLeg = partdefinition.addOrReplaceChild("leftLeg", CubeListBuilder.create(), PartPose.offset(3.0F, 15.0F, 0.0F));
		PartDefinition cube_r10 = leftLeg.addOrReplaceChild("cube_r10",
				CubeListBuilder.create().texOffs(50, 30).addBox(0.0F, -8.0F, -1.0F, 4.0F, 5.0F, 5.0F, new CubeDeformation(1.0F)).texOffs(28, 30).mirror().addBox(0.0F, -1.0F, -3.0F, 4.0F, 1.0F, 7.0F, new CubeDeformation(1.0F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, 8.0F, -1.0F, 0.0F, -0.0873F, 0.0F));
		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		float f = Math.min((float) entity.getDeltaMovement().lengthSqr() * 100.0F, 12.0F);
		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);
		this.animate(entity.walkAnimationState, DarkGolemAnimations.WALK, ageInTicks, f);
		this.animate(entity.attackAnimationState, DarkGolemAnimations.ATTACK, ageInTicks,0.75f);
		this.animate(entity.attackBlockingAnimationState, DarkGolemAnimations.ATTACK_BLOCKING, ageInTicks);
		this.animate(entity.attackWithHammerAnimationState, DarkGolemAnimations.ATTACK_HAMMER, ageInTicks);
		this.animate(entity.throwingHammerState, DarkGolemAnimations.HAMMER_THROWING, ageInTicks);
		this.animate(entity.deathAnimationState, DarkGolemAnimations.DEATH, ageInTicks,0.5f);
		this.animate(entity.spawnAnimationState, DarkGolemAnimations.SPAWN, ageInTicks,0.5f);
	}

	public ModelPart root() {
		return this.root;
	}

	@Override
	public void translateToHand(HumanoidArm humanoidArm, PoseStack poseStack) {
		this.getArm(humanoidArm).translateAndRotate(poseStack);
		poseStack.translate(0.5F, 0.25F, 0.0F);
	}

	protected ModelPart getArm(HumanoidArm arm) {
		return arm == HumanoidArm.LEFT ? this.rightArm : this.leftArm;
	}
}
