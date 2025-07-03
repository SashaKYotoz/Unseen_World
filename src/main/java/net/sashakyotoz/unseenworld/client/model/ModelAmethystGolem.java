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

public class ModelAmethystGolem<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("unseen_world", "modelamethyst_golem"), "main");
	public final ModelPart head;
	public final ModelPart body;
	public final ModelPart rightarm;
	public final ModelPart leftarm;
	public final ModelPart leftleg;
	public final ModelPart rightleg;

	public ModelAmethystGolem(ModelPart root) {
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.rightarm = root.getChild("rightarm");
		this.leftarm = root.getChild("leftarm");
		this.leftleg = root.getChild("leftleg");
		this.rightleg = root.getChild("rightleg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, -6.5F));
		PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(54, 55).addBox(0.0F, 0.0F, -2.5F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-1.0F, -5.0F, -1.0F, 0.0F, -0.7854F, -3.1416F));
		PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(54, 55).addBox(0.0F, 0.0F, -2.5F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-1.0F, -5.0F, -1.0F, 0.0F, 0.7854F, 3.1416F));
		PartDefinition cube_r3 = head.addOrReplaceChild("cube_r3",
				CubeListBuilder.create().texOffs(32, 41).addBox(-3.0F, -5.0F, 6.1F, 6.0F, 3.0F, 5.0F, new CubeDeformation(0.25F)).texOffs(32, 37).addBox(-3.0F, -9.0F, 6.0F, 6.0F, 7.0F, 5.0F, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, 4.0F, 7.5F, -3.1416F, 0.0F, 3.1416F));
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));
		PartDefinition cube_r4 = body.addOrReplaceChild("cube_r4",
				CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -2.0F, -5.0F, 14.0F, 7.0F, 11.0F, new CubeDeformation(0.0F)).texOffs(2, 31).addBox(-2.5F, -1.5F, 4.5F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 26)
						.addBox(-5.0F, -11.0F, -6.0F, 10.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 18).addBox(-6.0F, -12.0F, -4.0F, 12.0F, 10.0F, 9.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));
		PartDefinition crystals = body.addOrReplaceChild("crystals", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.0F, -10.0F, -3.5F, 3.1416F, 0.0F, 0.0F));
		PartDefinition cube_r5 = crystals.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(54, 55).mirror().addBox(0.0F, -2.0F, -2.5F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(2.0F, 2.0F, -6.0F, -3.1416F, -0.7854F, 3.1416F));
		PartDefinition cube_r6 = crystals.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(54, 55).mirror().addBox(0.0F, -2.0F, -2.5F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(2.0F, 2.0F, -6.0F, -3.1416F, 0.7854F, 3.1416F));
		PartDefinition cube_r7 = crystals.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(54, 55).mirror().addBox(0.0F, -2.0F, -2.5F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(8.0F, 2.0F, -4.0F, -3.1416F, -0.7854F, 3.1416F));
		PartDefinition cube_r8 = crystals.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(54, 55).mirror().addBox(0.0F, -2.0F, -2.5F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(8.0F, 2.0F, -4.0F, -3.1416F, 0.7854F, 3.1416F));
		PartDefinition cube_r9 = crystals.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(54, 55).mirror().addBox(8.5F, -2.0F, -2.5F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, 2.0F, 8.5F, 0.0F, 1.5708F, 0.0F));
		PartDefinition cube_r10 = crystals.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(54, 55).mirror().addBox(0.0F, -0.5F, -2.5F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(3.0F, -15.5F, -8.5F, -1.5708F, 0.0F, -2.3562F));
		PartDefinition cube_r11 = crystals.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(54, 55).mirror().addBox(0.0F, -0.5F, -2.5F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(3.0F, -15.5F, -8.5F, -1.5708F, 0.0F, -0.7854F));
		PartDefinition cube_r12 = crystals.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(54, 55).mirror().addBox(0.0F, -2.0F, 6.0F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(7.5F, -4.0F, -9.5F, -1.5708F, 0.0F, 3.1416F));
		PartDefinition cube_r13 = crystals.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(54, 55).mirror().addBox(8.5F, -2.0F, -2.5F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(7.5F, -4.0F, -9.5F, -1.5708F, 0.0F, -1.5708F));
		PartDefinition cube_r14 = crystals.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(54, 55).mirror().addBox(0.0F, -2.0F, 6.0F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, 2.0F, 8.5F, -3.1416F, 0.0F, 3.1416F));
		PartDefinition rightarm = partdefinition.addOrReplaceChild("rightarm", CubeListBuilder.create(), PartPose.offset(-1.0F, 6.0F, 0.0F));
		PartDefinition cube_r15 = rightarm.addOrReplaceChild("cube_r15",
				CubeListBuilder.create().texOffs(48, 49).addBox(6.5F, -9.3F, -1.6F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.5F)).texOffs(16, 37).addBox(6.5F, -9.3F, -1.6F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(1.0F, 8.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));
		PartDefinition leftarm = partdefinition.addOrReplaceChild("leftarm", CubeListBuilder.create(), PartPose.offset(1.0F, 6.0F, 0.0F));
		PartDefinition cube_r16 = leftarm.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(48, 49).mirror().addBox(-10.5F, -9.3F, -1.6F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false).texOffs(16, 37).mirror()
				.addBox(-10.5F, -9.3F, -1.6F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 8.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));
		PartDefinition leftleg = partdefinition.addOrReplaceChild("leftleg", CubeListBuilder.create(), PartPose.offset(3.0F, 19.0F, 0.0F));
		PartDefinition cube_r17 = leftleg.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(42, 18).addBox(-5.3F, 4.5F, -2.1F, 4.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-3.0F, -5.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));
		PartDefinition rightleg = partdefinition.addOrReplaceChild("rightleg", CubeListBuilder.create(), PartPose.offset(-3.0F, 19.0F, 0.0F));
		PartDefinition cube_r18 = rightleg.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(39, 0).addBox(1.3F, 4.0F, -2.1F, 4.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(3.0F, -5.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rightarm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leftarm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leftleg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rightleg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);
		this.head.zRot = Mth.cos(limbSwing * 0.5F) * 0.5F * limbSwingAmount;
		this.rightleg.xRot = Mth.cos(limbSwing * 1.0F) * 1.0F * limbSwingAmount;
		this.rightarm.xRot = Mth.cos(limbSwing * 0.95F + (float) Math.PI) * limbSwingAmount;
		this.leftleg.xRot = Mth.cos(limbSwing * 1.0F) * -1.0F * limbSwingAmount;
		this.leftarm.xRot = Mth.cos(limbSwing * 0.95F) * limbSwingAmount;
		this.body.zRot = Mth.cos(limbSwing * 0.5F) * 0.25F * limbSwingAmount;
	}
}
