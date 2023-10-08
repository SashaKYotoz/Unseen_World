package net.sashakyotoz.unseenworld.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class ModelThe_Wither_Knight_Armor_Rods<T extends LivingEntity> extends AgeableListModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("unseen_world", "model_the_wither_knight_armor_rods"), "main");
	public final ModelPart rods;

	public ModelThe_Wither_Knight_Armor_Rods(ModelPart root) {
		this.rods = root.getChild("rods");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition rods = partdefinition.addOrReplaceChild("rods",
				CubeListBuilder.create().texOffs(2, 36).addBox(-9.5F, -3.0F, -11.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(2, 36).addBox(-14.5F, -3.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(2, 36)
						.addBox(12.5F, -3.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(2, 36).addBox(7.5F, -3.0F, -11.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(2, 36)
						.addBox(-9.5F, -3.0F, 9.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(2, 36).addBox(7.5F, -3.0F, 9.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 6.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		rods.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(this.rods);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.rods.yRot = (ageInTicks % 360f)*0.125f;
		this.rods.xScale = 1+Mth.sin(ageInTicks % 360)*0.125f;
		this.rods.yScale = 1+Mth.sin(ageInTicks % 360)*0.125f;
		this.rods.zScale = 1+Mth.sin(ageInTicks % 360)*0.125f;
	}
}
