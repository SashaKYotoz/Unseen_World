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

public class ModelMoonFish<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("unseen_world", "modelmoon_fish"), "main");
	public final ModelPart body;
	public final ModelPart tailfin;
	public final ModelPart leftFin;
	public final ModelPart rightFin;

	public ModelMoonFish(ModelPart root) {
		this.body = root.getChild("body");
		this.tailfin = root.getChild("tailfin");
		this.leftFin = root.getChild("leftFin");
		this.rightFin = root.getChild("rightFin");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(17, 0).mirror().addBox(-1.5F, -3.0F, -3.0F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(0, 1).mirror()
				.addBox(-0.5F, -6.0F, -2.0F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 24.0F, -1.0F));
		PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(45, 1).addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, -1.0F, 0.0F, 0.0F, 3.1416F));
		PartDefinition tailfin = partdefinition.addOrReplaceChild("tailfin", CubeListBuilder.create().texOffs(36, 2).mirror().addBox(0.0F, -3.0F, 0.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 24.0F, 2.0F));
		PartDefinition leftFin = partdefinition.addOrReplaceChild("leftFin", CubeListBuilder.create().texOffs(2, 12).mirror().addBox(-2.4F, -1.0F, -0.29F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(-0.5F, 23.0F, -1.0F, 0.0F, 0.6109F, 0.0F));
		PartDefinition rightFin = partdefinition.addOrReplaceChild("rightFin", CubeListBuilder.create().texOffs(2, 16).mirror().addBox(0.4F, -1.0F, -0.29F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.5F, 23.0F, -1.0F, 0.0F, -0.6109F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		tailfin.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leftFin.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rightFin.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 1.0F;
		this.tailfin.yRot = -f * 0.45F * Mth.sin(0.6F * ageInTicks);
		this.leftFin.xRot = Mth.cos(limbSwing * 0.75F) * limbSwingAmount;
		this.rightFin.xRot = Mth.cos(limbSwing * 0.75F + (float) Math.PI) * limbSwingAmount;
	}
}
