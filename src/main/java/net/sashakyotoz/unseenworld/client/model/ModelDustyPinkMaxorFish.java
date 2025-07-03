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

public class ModelDustyPinkMaxorFish<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("unseen_world", "model_dusty_pink_maxor_fish"), "main");
	public final ModelPart body;
	public final ModelPart rightfin;
	public final ModelPart leftfin;
	public final ModelPart tailfin;

	public ModelDustyPinkMaxorFish(ModelPart root) {
		this.body = root.getChild("body");
		this.rightfin = root.getChild("rightfin");
		this.leftfin = root.getChild("leftfin");
		this.tailfin = root.getChild("tailfin");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -2.5F, -2.0F, 3.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(12, 1)
				.addBox(0.0F, -4.0F, 2.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(12, 1).addBox(0.0F, 1.0F, -1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.5F, -2.0F));
		PartDefinition rightfin = partdefinition.addOrReplaceChild("rightfin", CubeListBuilder.create(), PartPose.offset(-1.5F, 22.0F, -2.0F));
		PartDefinition cube_r1 = rightfin.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(12, 0).addBox(-2.0F, 0.0F, -1.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));
		PartDefinition leftfin = partdefinition.addOrReplaceChild("leftfin", CubeListBuilder.create(), PartPose.offset(1.5F, 22.0F, -2.0F));
		PartDefinition cube_r2 = leftfin.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(12, 0).mirror().addBox(0.0F, 0.0F, -1.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));
		PartDefinition tailfin = partdefinition.addOrReplaceChild("tailfin", CubeListBuilder.create().texOffs(3, 0).addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 0)
				.addBox(0.0F, -1.5F, 3.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(16, 1).addBox(0.0F, -1.5F, 1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.0F, 1.0F));
		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rightfin.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leftfin.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		tailfin.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float f = 1.0F;
		this.tailfin.yRot = -f * 0.45F * Mth.sin(0.6F * ageInTicks);
		this.leftfin.xRot = Mth.cos(limbSwing * 0.75F) * limbSwingAmount;
		this.rightfin.xRot = Mth.cos(limbSwing * 0.75F + (float) Math.PI) * limbSwingAmount;
	}
}
