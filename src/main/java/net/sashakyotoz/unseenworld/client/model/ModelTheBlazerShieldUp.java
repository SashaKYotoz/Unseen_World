package net.sashakyotoz.unseenworld.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.unseenworld.entity.TheBlazerEntity;
import net.sashakyotoz.unseenworld.entity.animations.BlazerAnimations;

public class ModelTheBlazerShieldUp<T extends TheBlazerEntity> extends HierarchicalModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("unseen_world", "modelthe_blazer_shield_up"), "main");
	public final ModelPart head;
	public final ModelPart Body;
	private final ModelPart root;

	public ModelTheBlazerShieldUp(ModelPart root) {
		this.root = root;
		this.head = root.getChild("head");
		this.Body = root.getChild("Body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -4.5F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-5.0F, -6.0F, -5.0F, 10.0F, 12.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));

		PartDefinition head_r1 = head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(41, 0).addBox(-4.0F, -5.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition right_forward = Body.addOrReplaceChild("right_forward", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -6.0F));

		PartDefinition cube_r2 = right_forward.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(12, 32).mirror().addBox(-5.5F, -7.75F, -1.0F, 11.0F, 18.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 7.5F, -2.0F, 0.0873F, 0.0F, 0.0F));

		PartDefinition right_back = Body.addOrReplaceChild("right_back", CubeListBuilder.create(), PartPose.offset(-6.0F, 0.0F, 0.0F));

		PartDefinition cube_r3 = right_back.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(12, 32).mirror().addBox(-5.5F, -7.5F, -0.5F, 11.0F, 18.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.5F, 7.0F, 0.0F, 0.0F, 1.5708F, -0.0873F));

		PartDefinition left_forward = Body.addOrReplaceChild("left_forward", CubeListBuilder.create(), PartPose.offset(6.0F, 0.0F, 0.0F));

		PartDefinition cube_r4 = left_forward.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 32).mirror().addBox(-5.5F, -8.0F, -0.8F, 11.0F, 18.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.0F, 7.5F, 0.0F, 0.0F, 1.5708F, 0.0873F));

		PartDefinition left_back = Body.addOrReplaceChild("left_back", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 6.0F));

		PartDefinition cube_r5 = left_back.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 32).mirror().addBox(-5.5F, -8.0F, 0.0F, 11.0F, 18.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 8.0F, 2.0F, -0.0873F, 0.0F, 0.0F));

		PartDefinition rods = Body.addOrReplaceChild("rods", CubeListBuilder.create().texOffs(52, 18).mirror().addBox(-1.2F, -11.0F, -1.7F, 3.0F, 20.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(52, 41).mirror().addBox(-1.1824F, -10.5F, -1.7F, 3.0F, 20.0F, 3.0F, new CubeDeformation(1.0F)).mirror(false)
				.texOffs(47, 18).mirror().addBox(-2.9324F, 0.0F, 2.3176F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(47, 18).mirror().addBox(2.5676F, 0.0F, -3.1824F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(47, 18).mirror().addBox(-2.9324F, 0.0F, -3.1824F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(47, 18).mirror().addBox(2.5676F, 0.0F, 2.3176F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 0.0F, -2.3562F, 0.0F));

		PartDefinition shields = rods.addOrReplaceChild("shields", CubeListBuilder.create(), PartPose.offsetAndRotation(0.5676F, -9.0F, 0.3176F, 0.0F, 0.7854F, 0.0F));

		PartDefinition cube_r6 = shields.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 31).mirror().addBox(-5.5F, -8.048F, -3.7862F, 11.0F, 18.0F, 1.0F, new CubeDeformation(-1.0F)).mirror(false), PartPose.offsetAndRotation(9.0F, 5.5F, 0.0F, 0.0F, 1.5708F, -0.0436F));

		PartDefinition cube_r7 = shields.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(12, 32).mirror().addBox(-5.3176F, -7.75F, 1.6824F, 11.0F, 18.0F, 1.0F, new CubeDeformation(-1.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 5.5F, -8.0F, -0.0436F, 0.0F, 0.0F));

		PartDefinition cube_r8 = shields.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(12, 32).mirror().addBox(-5.3176F, -7.5F, 2.6824F, 11.0F, 18.0F, 1.0F, new CubeDeformation(-1.0F)).mirror(false), PartPose.offsetAndRotation(-8.5F, 5.0F, 0.0F, 0.0F, 1.5708F, 0.0436F));

		PartDefinition cube_r9 = shields.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 32).mirror().addBox(-5.5F, -8.25F, -3.1F, 11.0F, 18.0F, 1.0F, new CubeDeformation(-1.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 6.0F, 8.0F, 0.0436F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);
		if(!entity.spawnAnimationState.isStarted())
			this.Body.yRot = ageInTicks / 10F;
		if(entity.isBlocked()) {
			this.Body.getChild("right_forward").xRot = 0.15f;
			this.Body.getChild("left_forward").zRot = -0.15f;
			this.Body.getChild("right_back").zRot = 0.15f;
			this.Body.getChild("left_back").xRot = -0.15f;
		}
		this.animate(entity.spawnAnimationState, BlazerAnimations.SPAWN,ageInTicks,0.75f);
		this.animate(entity.strikeAnimationState, BlazerAnimations.STRIKE,ageInTicks);
	}
}
