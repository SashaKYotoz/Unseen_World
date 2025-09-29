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

public class ModelTanzanite_Guardian<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("unseen_world", "model_tanzanite_guardian"), "main");
	public final ModelPart head;
	public final ModelPart body;
	public final ModelPart rightArm;
	public final ModelPart leftArm;
	public final ModelPart rightLeg;
	public final ModelPart leftLeg;

	public ModelTanzanite_Guardian(ModelPart root) {
		this.head = root.getChild("head");
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
				CubeListBuilder.create().texOffs(0, 0).addBox(-5.5F, -10.0F, -5.0F, 10.0F, 11.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(0, 24).addBox(-5.5F, -10.0F, -5.0F, 10.0F, 11.0F, 10.0F, new CubeDeformation(0.25F)).texOffs(22, 36)
						.addBox(-4.5F, -10.0F, -5.75F, 8.0F, 3.0F, 1.0F, new CubeDeformation(0.2F)).texOffs(10, 41).addBox(-4.75F, -2.0F, -5.75F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.2F)).texOffs(10, 41).mirror()
						.addBox(1.75F, -2.0F, -5.75F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.2F)).mirror(false).texOffs(47, 0).addBox(-7.5F, -9.0F, -4.0F, 2.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(47, 0).mirror()
						.addBox(4.5F, -9.0F, -4.0F, 2.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(0.0F, -6.0F, 0.0F));
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(92, 40).addBox(-6.0F, -3.0F, -3.0F, 12.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(92, 24)
				.addBox(-6.0F, -3.0F, -3.0F, 12.0F, 9.0F, 6.0F, new CubeDeformation(0.25F)).texOffs(80, 0).addBox(-8.25F, -13.0F, -4.0F, 16.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.0F));
		PartDefinition rightArm = partdefinition.addOrReplaceChild("rightArm", CubeListBuilder.create(), PartPose.offset(-8.0F, 0.0F, 0.0F));
		PartDefinition cube_r1 = rightArm.addOrReplaceChild("cube_r1",
				CubeListBuilder.create().texOffs(40, 16).addBox(-7.0F, 11.0F, -3.5F, 7.0F, 10.0F, 7.0F, new CubeDeformation(0.5F)).texOffs(40, 0).addBox(-7.0F, -5.0F, -3.5F, 7.0F, 26.0F, 7.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0436F));
		PartDefinition leftArm = partdefinition.addOrReplaceChild("leftArm", CubeListBuilder.create(), PartPose.offset(8.0F, 0.0F, 0.0F));
		PartDefinition cube_r2 = leftArm.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-0.25F, 11.0F, -3.5F, 7.0F, 10.0F, 7.0F, new CubeDeformation(0.5F)).mirror(false).texOffs(40, 0).mirror()
				.addBox(-0.25F, -5.0F, -3.5F, 7.0F, 26.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.0436F));
		PartDefinition rightLeg = partdefinition.addOrReplaceChild("rightLeg",
				CubeListBuilder.create().texOffs(64, 48).mirror().addBox(-3.5F, -2.0F, -3.5F, 5.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(92, 24).addBox(-3.5F, -1.0F, -3.5F, 5.0F, 9.0F, 6.0F, new CubeDeformation(0.1F)),
				PartPose.offset(-2.5F, 16.0F, 0.5F));
		PartDefinition leftLeg = partdefinition.addOrReplaceChild("leftLeg",
				CubeListBuilder.create().texOffs(64, 48).addBox(-1.5F, -2.0F, -3.5F, 5.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(92, 24).mirror().addBox(-1.5F, -1.0F, -3.5F, 5.0F, 9.0F, 6.0F, new CubeDeformation(0.1F)).mirror(false),
				PartPose.offset(2.5F, 16.0F, 0.5F));
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
		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);
		this.rightLeg.xRot = Mth.cos(limbSwing * 1.0F) * 1.0F * limbSwingAmount;
		this.rightArm.xRot = Mth.cos(limbSwing * 0.9F + (float) Math.PI) * limbSwingAmount;
		this.leftArm.xRot = Mth.cos(limbSwing * 0.9F) * limbSwingAmount;
		this.leftLeg.xRot = Mth.cos(limbSwing * 1.0F) * -1.0F * limbSwingAmount;
	}
}
