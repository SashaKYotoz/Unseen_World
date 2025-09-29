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
import net.sashakyotoz.unseenworld.UnseenWorldMod;

public class ModelFireParticle<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(UnseenWorldMod.MODID, "model_fire_particle"), "main");
	private final ModelPart fireball;

	public ModelFireParticle(ModelPart root) {
		this.fireball = root.getChild("fireball");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition fireball = partdefinition.addOrReplaceChild("fireball", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.5F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 8).addBox(-2.0F, -2.5F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 24.5F, 0.0F));
		PartDefinition cube_r1 = fireball.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(16, -8).addBox(0.0F, -5.0F, -4.0F, 0.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, -2.0F, 0.7854F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 32, 16);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}
	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		fireball.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}