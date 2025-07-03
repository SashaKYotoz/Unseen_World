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

public class ModelRed_Ravager<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("unseen_world", "model_red_ravager"), "main");
	public final ModelPart head;
	public final ModelPart body;
	public final ModelPart rightLeg;
	public final ModelPart backrightLeg;
	public final ModelPart leftLeg;
	public final ModelPart backleftLeg;

	public ModelRed_Ravager(ModelPart root) {
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.rightLeg = root.getChild("rightLeg");
		this.backrightLeg = root.getChild("backrightLeg");
		this.leftLeg = root.getChild("leftLeg");
		this.backleftLeg = root.getChild("backleftLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -11.5F, -16.6759F, 16.0F, 17.0F, 16.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
				.addBox(-2.0F, 2.5F, -20.6759F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(0, 36).addBox(-8.0F, 5.5F, -16.6759F, 16.0F, 3.0F, 16.0F, new CubeDeformation(0.4F)), PartPose.offset(0.0F, 4.0F, -14.0F));
		PartDefinition horns_r1 = head.addOrReplaceChild("horns_r1", CubeListBuilder.create().texOffs(74, 55).addBox(-10.0F, -12.2595F, -1.5F, 2.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(74, 55).mirror()
				.addBox(8.0F, -12.2595F, -1.5F, 2.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -5.0F, -11.5F, 0.2182F, 0.0F, 0.0F));
		PartDefinition body = partdefinition
				.addOrReplaceChild("body",
						CubeListBuilder.create().texOffs(0, 55).addBox(-7.0F, -32.5F, -8.6759F, 14.0F, 16.0F, 20.0F, new CubeDeformation(0.1F)).texOffs(0, 91).addBox(-6.0F, -18.5F, -6.6759F, 12.0F, 10.0F, 18.0F, new CubeDeformation(0.0F))
								.texOffs(67, 73).addBox(-6.0F, -32.0F, -15.5F, 12.0F, 10.0F, 7.0F, new CubeDeformation(1.0F)).texOffs(66, 104).addBox(-7.0F, -32.5F, -6.5F, 14.0F, 1.0F, 16.0F, new CubeDeformation(0.75F)),
						PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition rightLeg = partdefinition.addOrReplaceChild("rightLeg", CubeListBuilder.create(), PartPose.offset(-5.0F, 0.0F, -11.0F));
		PartDefinition leg2_r1 = rightLeg.addOrReplaceChild("leg2_r1", CubeListBuilder.create().texOffs(64, 0).addBox(-10.0F, -0.2F, -4.75F, 8.0F, 37.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -10.5F, 1.25F, 0.0F, 0.0F, 0.0436F));
		PartDefinition backrightLeg = partdefinition.addOrReplaceChild("backrightLeg", CubeListBuilder.create(), PartPose.offset(-5.0F, 0.0F, 11.0F));
		PartDefinition leg0_r1 = backrightLeg.addOrReplaceChild("leg0_r1", CubeListBuilder.create().texOffs(64, 0).addBox(-10.0F, -2.5F, -4.0F, 8.0F, 37.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -8.0F, -0.5F, 0.0436F, 0.0F, 0.0436F));
		PartDefinition leftLeg = partdefinition.addOrReplaceChild("leftLeg", CubeListBuilder.create(), PartPose.offset(6.0F, 0.0F, -11.0F));
		PartDefinition leg3_r1 = leftLeg.addOrReplaceChild("leg3_r1", CubeListBuilder.create().texOffs(64, 0).mirror().addBox(1.0F, -0.2F, -4.75F, 8.0F, 37.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, -10.5F, 1.25F, 0.0F, 0.0F, -0.0436F));
		PartDefinition backleftLeg = partdefinition.addOrReplaceChild("backleftLeg", CubeListBuilder.create(), PartPose.offset(5.0F, 0.0F, 10.5F));
		PartDefinition leg1_r1 = backleftLeg.addOrReplaceChild("leg1_r1", CubeListBuilder.create().texOffs(64, 0).mirror().addBox(2.0F, -2.5F, -4.0F, 8.0F, 37.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0436F, 0.0F, -0.0436F));
		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);
		this.backleftLeg.xRot = Mth.cos(limbSwing * 0.6F) * -1.0F * limbSwingAmount;
		this.rightLeg.xRot = Mth.cos(limbSwing * 0.6F) * 1.0F * limbSwingAmount;
		this.backrightLeg.xRot = Mth.cos(limbSwing * 0.6F) * 1.0F * limbSwingAmount;
		this.leftLeg.xRot = Mth.cos(limbSwing * 0.6F) * -1.0F * limbSwingAmount;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		backrightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		backleftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
