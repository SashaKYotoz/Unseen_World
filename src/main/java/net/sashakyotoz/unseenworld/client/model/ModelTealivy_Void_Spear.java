package net.sashakyotoz.unseenworld.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.EntityModel;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

// Made with Blockbench 4.3.1
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports
public class ModelTealivy_Void_Spear<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("unseen_world", "model_tealivy_void_spear"), "main");
	public final ModelPart handle;

	public ModelTealivy_Void_Spear(ModelPart root) {
		this.handle = root.getChild("handle");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition handle = partdefinition.addOrReplaceChild("handle", CubeListBuilder.create(), PartPose.offset(-0.5F, 24.0F, 0.0F));
		PartDefinition cube_r1 = handle.addOrReplaceChild("cube_r1",
				CubeListBuilder.create().texOffs(7, 3).addBox(0.0F, -4.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(7, 9).addBox(0.0F, -3.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(14, 4)
						.addBox(0.0F, -4.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(7, 0).addBox(0.0F, -5.0F, 2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(5, 12)
						.addBox(0.0F, -4.0F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 17).addBox(0.0F, -2.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(12, 19)
						.addBox(0.0F, -4.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(13, 0).addBox(0.0F, -3.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(19, 0)
						.addBox(0.0F, -1.0F, 1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(16, 17).addBox(0.0F, -3.0F, 1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(14, 0)
						.addBox(0.0F, -15.0F, 11.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(0.0F, -15.0F, 14.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 6)
						.addBox(0.0F, -13.0F, 13.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(0.0F, -14.0F, 9.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(0, 6)
						.addBox(0.0F, -13.0F, 7.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(8, 15).addBox(0.0F, -13.0F, 12.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 3)
						.addBox(0.0F, -12.0F, 9.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(9, 9).addBox(0.0F, -9.0F, 6.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(15, 7)
						.addBox(0.0F, -9.0F, 10.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(-0.01F)).texOffs(0, 12).addBox(0.0F, -12.0F, 6.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(-0.01F)).texOffs(12, 15)
						.addBox(0.0F, -10.0F, 8.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F)).texOffs(16, 13).addBox(0.0F, -7.0F, 5.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F)).texOffs(0, 17)
						.addBox(0.0F, -6.0F, 4.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(19, 4).addBox(0.0F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.7854F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		handle.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
