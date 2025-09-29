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

public class ModelStrederWithSaddle<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("unseen_world", "model_streder_with_saddle"), "main");
	public final ModelPart body;
	public final ModelPart upperSpikes;
	public final ModelPart rightSpikes;
	public final ModelPart leftSpikes;
	public final ModelPart rightLeg;
	public final ModelPart leftLeg;

	public ModelStrederWithSaddle(ModelPart root) {
		this.body = root.getChild("body");
		this.upperSpikes = root.getChild("upperSpikes");
		this.rightSpikes = root.getChild("rightSpikes");
		this.leftSpikes = root.getChild("leftSpikes");
		this.rightLeg = root.getChild("rightLeg");
		this.leftLeg = root.getChild("leftLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition body = partdefinition.addOrReplaceChild("body",
				CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -9.0F, -3.5F, 16.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(13, 22).addBox(-8.0F, -9.0F, -3.5F, 16.0F, 9.0F, 7.0F, new CubeDeformation(0.25F)),
				PartPose.offset(0.0F, 19.0F, 0.0F));
		PartDefinition saddle = body.addOrReplaceChild("saddle", CubeListBuilder.create(), PartPose.offset(0.0F, -9.0F, 0.0F));
		PartDefinition cube_r1 = saddle.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 19).addBox(-4.0F, -4.0F, -0.26F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));
		PartDefinition upperSpikes = partdefinition.addOrReplaceChild("upperSpikes", CubeListBuilder.create(), PartPose.offset(0.0F, 10.0F, -1.0F));
		PartDefinition cube_r2 = upperSpikes.addOrReplaceChild("cube_r2",
				CubeListBuilder.create().texOffs(56, 6).addBox(5.0F, -6.0F, 0.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(56, 16).mirror().addBox(4.0F, -6.0F, 1.0F, 4.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
						.texOffs(56, 16).addBox(-8.0F, -6.0F, 1.0F, 4.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(56, 6).addBox(-7.0F, -6.0F, 0.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));
		PartDefinition rightSpikes = partdefinition.addOrReplaceChild("rightSpikes", CubeListBuilder.create(), PartPose.offset(-8.0F, 15.0F, 0.0F));
		PartDefinition cube_r3 = rightSpikes.addOrReplaceChild("cube_r3",
				CubeListBuilder.create().texOffs(48, 0).mirror().addBox(-5.0F, 0.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(48, 0).addBox(-5.0F, -4.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.2618F, 0.0F));
		PartDefinition leftSpikes = partdefinition.addOrReplaceChild("leftSpikes", CubeListBuilder.create(), PartPose.offset(8.0F, 15.0F, 0.0F));
		PartDefinition cube_r4 = leftSpikes.addOrReplaceChild("cube_r4",
				CubeListBuilder.create().texOffs(48, 0).addBox(-1.0F, 0.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(48, 0).mirror().addBox(-1.0F, -4.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.2618F, 0.0F));
		PartDefinition rightLeg = partdefinition.addOrReplaceChild("rightLeg", CubeListBuilder.create(), PartPose.offset(-3.5F, 19.0F, 0.0F));
		PartDefinition cube_r5 = rightLeg.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 30).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0436F));
		PartDefinition leftLeg = partdefinition.addOrReplaceChild("leftLeg", CubeListBuilder.create(), PartPose.offset(3.5F, 19.0F, 0.0F));
		PartDefinition cube_r6 = leftLeg.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 30).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.0436F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		upperSpikes.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rightSpikes.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leftSpikes.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		rightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		leftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.leftSpikes.yRot = Mth.cos(limbSwing * 0.75F) * limbSwingAmount;
		this.rightSpikes.yRot = Mth.cos(limbSwing * 0.75F + (float) Math.PI) * limbSwingAmount;
		this.rightLeg.xRot = Mth.cos(limbSwing) * 1.2F * limbSwingAmount;
		this.leftLeg.xRot = Mth.cos(limbSwing) * -1.2F * limbSwingAmount;
		this.upperSpikes.zRot = headPitch / (180F / (float) Math.PI);
	}
}
