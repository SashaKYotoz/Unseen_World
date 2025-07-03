package net.sashakyotoz.unseenworld.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ModelRed_Armored_Blaze<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("unseen_world", "model_red_armored_blaze"), "main");
	public final ModelPart head;
	public final ModelPart upperBodyParts;
	public final ModelPart middleBodyParts;
	public final ModelPart underBodyParts;

	public ModelRed_Armored_Blaze(ModelPart root) {
		this.head = root.getChild("head");
		this.upperBodyParts = root.getChild("upperBodyParts");
		this.middleBodyParts = root.getChild("middleBodyParts");
		this.underBodyParts = root.getChild("underBodyParts");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition head = partdefinition.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(32, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition upperBodyParts = partdefinition.addOrReplaceChild("upperBodyParts",
				CubeListBuilder.create().texOffs(0, 16).addBox(8.0F, -6.0F, -3.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 16).addBox(-3.0F, -6.0F, -10.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(16, 16)
						.addBox(-5.0F, -7.0F, -11.0F, 6.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(50, 16).addBox(-4.0F, 5.5F, -11.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.5F)).texOffs(50, 16)
						.addBox(-6.5F, 1.5F, -11.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.5F)).texOffs(50, 16).mirror().addBox(0.5F, 1.5F, -11.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.5F)).mirror(false).texOffs(50, 16)
						.addBox(-6.5F, -7.5F, -11.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.5F)).texOffs(58, 20).addBox(-6.25F, -4.5F, -11.0F, 2.0F, 6.0F, 1.0F, new CubeDeformation(0.25F)).texOffs(58, 20).mirror()
						.addBox(0.25F, -4.5F, -11.0F, 2.0F, 6.0F, 1.0F, new CubeDeformation(0.25F)).mirror(false).texOffs(50, 16).mirror().addBox(0.5F, -7.5F, -11.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.5F)).mirror(false).texOffs(0, 16)
						.addBox(-10.0F, -6.0F, 1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 16).addBox(1.0F, -6.0F, 8.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 8.0F, 0.0F));
		PartDefinition upperBodyParts3_r1 = upperBodyParts.addOrReplaceChild("upperBodyParts3_r1",
				CubeListBuilder.create().texOffs(16, 16).addBox(-5.0F, -7.0F, -11.0F, 6.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(50, 16).addBox(-4.0F, 5.5F, -11.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.5F)).texOffs(50, 16)
						.addBox(-6.5F, 1.5F, -11.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.5F)).texOffs(50, 16).mirror().addBox(0.5F, 1.5F, -11.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.5F)).mirror(false).texOffs(50, 16)
						.addBox(-6.5F, -7.5F, -11.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.5F)).texOffs(50, 16).mirror().addBox(0.5F, -7.5F, -11.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.5F)).mirror(false).texOffs(58, 20)
						.addBox(-6.25F, -4.5F, -11.0F, 2.0F, 6.0F, 1.0F, new CubeDeformation(0.25F)).texOffs(58, 20).mirror().addBox(0.25F, -4.5F, -11.0F, 2.0F, 6.0F, 1.0F, new CubeDeformation(0.25F)).mirror(false),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));
		PartDefinition upperBodyParts3_r2 = upperBodyParts.addOrReplaceChild("upperBodyParts3_r2",
				CubeListBuilder.create().texOffs(58, 20).mirror().addBox(0.25F, -4.5F, -11.0F, 2.0F, 6.0F, 1.0F, new CubeDeformation(0.25F)).mirror(false).texOffs(58, 20).addBox(-6.25F, -4.5F, -11.0F, 2.0F, 6.0F, 1.0F, new CubeDeformation(0.25F))
						.texOffs(50, 16).mirror().addBox(0.5F, -7.5F, -11.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.5F)).mirror(false).texOffs(50, 16).addBox(-6.5F, -7.5F, -11.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.5F)).texOffs(50, 16)
						.mirror().addBox(0.5F, 1.5F, -11.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.5F)).mirror(false).texOffs(50, 16).addBox(-6.5F, 1.5F, -11.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.5F)).texOffs(50, 16)
						.addBox(-4.0F, 5.5F, -11.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.5F)).texOffs(16, 16).addBox(-5.0F, -7.0F, -11.0F, 6.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
		PartDefinition upperBodyParts3_r3 = upperBodyParts.addOrReplaceChild("upperBodyParts3_r3",
				CubeListBuilder.create().texOffs(58, 20).mirror().addBox(0.25F, -4.5F, -11.0F, 2.0F, 6.0F, 1.0F, new CubeDeformation(0.25F)).mirror(false).texOffs(58, 20).addBox(-6.25F, -4.5F, -11.0F, 2.0F, 6.0F, 1.0F, new CubeDeformation(0.25F))
						.texOffs(50, 16).mirror().addBox(0.5F, -7.5F, -11.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.5F)).mirror(false).texOffs(50, 16).addBox(-6.5F, -7.5F, -11.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.5F)).texOffs(50, 16)
						.mirror().addBox(0.5F, 1.5F, -11.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.5F)).mirror(false).texOffs(50, 16).addBox(-6.5F, 1.5F, -11.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.5F)).texOffs(50, 16)
						.addBox(-4.0F, 5.5F, -11.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.5F)).texOffs(16, 16).addBox(-5.0F, -7.0F, -11.0F, 6.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
		PartDefinition middleBodyParts = partdefinition.addOrReplaceChild("middleBodyParts",
				CubeListBuilder.create().texOffs(0, 16).addBox(5.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 16).addBox(-7.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 16)
						.addBox(-1.0F, 0.0F, 5.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 16).addBox(-1.0F, 0.0F, -7.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(8, 16)
						.addBox(-1.0F, 0.0F, -7.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.25F)).texOffs(8, 16).mirror().addBox(-1.0F, 0.0F, 5.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.25F)).mirror(false).texOffs(8, 16).mirror()
						.addBox(5.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.25F)).mirror(false).texOffs(8, 16).addBox(-7.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.25F)),
				PartPose.offset(0.0F, 6.0F, 0.0F));
		PartDefinition underBodyParts = partdefinition.addOrReplaceChild("underBodyParts",
				CubeListBuilder.create().texOffs(0, 16).addBox(3.0F, 0.0F, 2.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 16).addBox(-5.0F, 0.0F, -4.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 16)
						.addBox(-4.0F, 0.0F, 3.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 16).addBox(2.0F, 0.0F, -5.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 16.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		upperBodyParts.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		middleBodyParts.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		underBodyParts.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.head.xRot = headPitch / (180F / (float) Math.PI);
		this.upperBodyParts.yRot = ageInTicks / 20.f;
		this.underBodyParts.yRot = ageInTicks / 10.f;
		this.middleBodyParts.yRot = ageInTicks / 5.f;
	}
}
