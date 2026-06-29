package net.sashakyotoz.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.sashakyotoz.UnseenWorld;

public class ShieldOfWarriorModel extends Model {
	public static final ModelLayerLocation SHIELD_OF_WARRIOR = new ModelLayerLocation(UnseenWorld.makeID("shield_of_warrior"), "main");

	private final ModelPart base;
	private final ModelPart handle;
	public ShieldOfWarriorModel(ModelPart root) {
        super(RenderType::entityTranslucent);
		this.base = root.getChild("base");
		this.handle = root.getChild("handle");
	}
	public static LayerDefinition getTextureLocationdModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();

		PartDefinition base = modelPartData.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -11.0F, -2.0F, 12.0F, 22.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(12, 24).addBox(4.75F, -9.0F, -2.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 24).mirror().addBox(-7.75F, 7.0F, -2.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 29).addBox(-7.75F, 9.0F, -2.5F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 29).mirror().addBox(2.75F, 9.0F, -2.5F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 24).addBox(4.75F, 7.0F, -2.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(16, 29).addBox(2.75F, -12.0F, -2.5F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(12, 24).mirror().addBox(-7.75F, -9.0F, -2.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(16, 29).mirror().addBox(-7.75F, -12.0F, -2.5F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.ZERO);
		PartDefinition handle = modelPartData.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(28, 0).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.ZERO);
		return LayerDefinition.create(modelData, 128, 64);
	}
	public ModelPart getPlate() {
		return this.base;
	}

	public ModelPart getHandle() {
		return this.handle;
	}
	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		base.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		handle.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}