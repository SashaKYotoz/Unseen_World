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

public class ModelDark_Phantom<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("unseen_world", "model_dark_phantom"), "main");
	public final ModelPart head;
	public final ModelPart body;
	public final ModelPart wing0;
	public final ModelPart wing1;
	public final ModelPart tail;
	public final ModelPart tail1;
	public final ModelPart tail2;

	public ModelDark_Phantom(ModelPart root) {
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.wing0 = root.getChild("wing0");
		this.wing1 = root.getChild("wing1");
		this.tail = root.getChild("tail");
		this.tail1 = root.getChild("tail1");
		this.tail2 = root.getChild("tail2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(-0.5F, 10.5F, -7.0F));
		PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1",
				CubeListBuilder.create().texOffs(32, 0).addBox(-4.5F, -3.0F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(32, 0).mirror().addBox(2.5F, -3.0F, 0.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.5F, 2.0F, -3.0F, -0.4363F, 0.0F, 0.0F));
		PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2",
				CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-4.5F, -3.0F, 0.4F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(32, 0).addBox(2.5F, -3.0F, 0.4F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.5F, -1.0F, -2.0F, -0.9163F, 0.0F, 0.0F));
		PartDefinition head_r1 = head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(69, 0).addBox(-0.8536F, -1.5F, -11.3536F, 6.0F, 3.0F, 5.0F, new CubeDeformation(0.001F)),
				PartPose.offsetAndRotation(1.0F, 3.5F, 7.0F, 0.0F, 0.7854F, 0.0F));
		PartDefinition head_r2 = head.addOrReplaceChild("head_r2", CubeListBuilder.create().texOffs(94, 0).addBox(-11.3536F, -1.5F, -10.6464F, 11.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(1.0F, 3.5F, 7.0F, 0.0F, -0.7854F, 0.0F));
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 8).addBox(-2.5F, 2.0F, -8.0F, 5.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, 0.0F));
		PartDefinition cube_r3 = body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(9, 56).mirror().addBox(-9.0F, -6.0F, 0.5F, 12.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.5F, 4.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
		PartDefinition wing0 = partdefinition.addOrReplaceChild("wing0", CubeListBuilder.create().texOffs(23, 12).mirror().addBox(0.5F, 1.0F, -5.0F, 6.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 11.0F, -3.0F));
		PartDefinition wingtip0 = wing0.addOrReplaceChild("wingtip0", CubeListBuilder.create(), PartPose.offset(6.0F, -9.0F, -5.0F));
		PartDefinition wingtip0_r1 = wingtip0.addOrReplaceChild("wingtip0_r1", CubeListBuilder.create().texOffs(81, 27).addBox(0.0F, -2.0F, -8.0F, 13.0F, 1.0F, 9.0F, new CubeDeformation(0.01F)),
				PartPose.offsetAndRotation(0.5F, 12.0F, 8.0F, 0.0F, 0.0F, -0.1745F));
		PartDefinition wing1 = partdefinition.addOrReplaceChild("wing1", CubeListBuilder.create().texOffs(23, 12).addBox(-5.5F, 1.0F, -5.0F, 6.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 11.0F, -3.0F));
		PartDefinition wingtip1 = wing1.addOrReplaceChild("wingtip1", CubeListBuilder.create(), PartPose.offset(-6.0F, -9.0F, -5.0F));
		PartDefinition wingtip1_r1 = wingtip1.addOrReplaceChild("wingtip1_r1", CubeListBuilder.create().texOffs(81, 27).mirror().addBox(-13.0F, -2.0F, -8.0F, 13.0F, 1.0F, 9.0F, new CubeDeformation(0.01F)).mirror(false),
				PartPose.offsetAndRotation(0.5F, 12.0F, 8.0F, 0.0F, 0.0F, 0.1745F));
		PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(108, 48).addBox(-2.0F, -0.5F, 0.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(85, 49)
				.addBox(-2.0F, 0.0F, 4.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.1F)).texOffs(118, 44).addBox(-1.5F, 0.0F, 2.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, 1.0F));
		PartDefinition tail1 = partdefinition.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(96, 62).addBox(-1.0F, 0.0F, 6.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(96, 62)
				.addBox(-1.0F, 0.0F, 8.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(100, 48).addBox(-0.5F, 0.0F, 7.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, 1.0F));
		PartDefinition tail2 = partdefinition.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(110, 60).addBox(-3.0F, 0.0F, 9.0F, 6.0F, 1.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 13.0F, 1.0F));
		PartDefinition cube_r4 = tail2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(10, 56).mirror().addBox(7.0F, -4.5F, 0.5F, 9.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.5F, 1.0F, -1.0F, 0.0F, -1.5708F, 0.0F));
		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		wing0.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		wing1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		tail1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		tail2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);
		this.tail1.xRot = headPitch / (180F / (float) Math.PI) + 0.1f;
		this.tail2.xRot = headPitch / (180F / (float) Math.PI) + 0.2f;
		this.tail.xRot = headPitch / (180F / (float) Math.PI);
		this.wing1.zRot = Mth.cos(limbSwing * 0.9F) * limbSwingAmount;
		this.wing0.zRot = Mth.cos(limbSwing * 0.9F + (float) Math.PI) * limbSwingAmount;
	}
}
