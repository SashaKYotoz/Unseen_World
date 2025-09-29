package net.sashakyotoz.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.sashakyotoz.UnseenWorld;

public class ShieldOfWarriorModel extends Model {
	public static final EntityModelLayer SHIELD_OF_WARRIOR = new EntityModelLayer(UnseenWorld.makeID("shield_of_warrior"), "main");

	private final ModelPart base;
	private final ModelPart handle;
	public ShieldOfWarriorModel(ModelPart root) {
        super(RenderLayer::getEntityTranslucent);
		this.base = root.getChild("base");
		this.handle = root.getChild("handle");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create().uv(0, 0).cuboid(-6.0F, -11.0F, -2.0F, 12.0F, 22.0F, 2.0F, new Dilation(0.0F))
				.uv(12, 24).cuboid(4.75F, -9.0F, -2.5F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F))
				.uv(0, 24).mirrored().cuboid(-7.75F, 7.0F, -2.5F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
				.uv(0, 29).cuboid(-7.75F, 9.0F, -2.5F, 5.0F, 3.0F, 3.0F, new Dilation(0.0F))
				.uv(0, 29).mirrored().cuboid(2.75F, 9.0F, -2.5F, 5.0F, 3.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
				.uv(0, 24).cuboid(4.75F, 7.0F, -2.5F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F))
				.uv(16, 29).cuboid(2.75F, -12.0F, -2.5F, 5.0F, 3.0F, 3.0F, new Dilation(0.0F))
				.uv(12, 24).mirrored().cuboid(-7.75F, -9.0F, -2.5F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
				.uv(16, 29).mirrored().cuboid(-7.75F, -12.0F, -2.5F, 5.0F, 3.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.NONE);
		ModelPartData handle = modelPartData.addChild("handle", ModelPartBuilder.create().uv(28, 0).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 7.0F, 7.0F, new Dilation(0.0F)), ModelTransform.NONE);
		return TexturedModelData.of(modelData, 128, 64);
	}
	public ModelPart getPlate() {
		return this.base;
	}

	public ModelPart getHandle() {
		return this.handle;
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		base.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		handle.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}