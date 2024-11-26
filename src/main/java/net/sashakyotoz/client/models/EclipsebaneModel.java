package net.sashakyotoz.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.sashakyotoz.UnseenWorld;

public class EclipsebaneModel extends Model {
	public static final EntityModelLayer ECLIPSEBANE = new EntityModelLayer(UnseenWorld.makeID("eclipsebane"), "main");
	private final ModelPart sword;
	private final ModelPart blade;
	private final ModelPart handle;
	public EclipsebaneModel(ModelPart root) {
		super(RenderLayer::getEntityTranslucent);
        this.sword = root.getChild("sword");
		this.blade = sword.getChild("blade");
		this.handle = sword.getChild("handle");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData sword = modelPartData.addChild("sword", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 16.0F, 0.0F));

		ModelPartData blade = sword.addChild("blade", ModelPartBuilder.create().uv(94, 46).cuboid(1.0F, 0.0F, -7.0F, 15.0F, 16.0F, 2.0F, new Dilation(0.0F))
		.uv(120, 20).cuboid(16.0F, 0.0F, -7.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(118, 28).cuboid(12.0F, -3.0F, -7.0F, 3.0F, 3.0F, 2.0F, new Dilation(0.0F))
		.uv(122, 33).cuboid(11.0F, -2.0F, -7.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(122, 37).cuboid(10.0F, -3.0F, -7.0F, 1.0F, 3.0F, 2.0F, new Dilation(0.0F))
		.uv(114, 33).cuboid(8.0F, -4.0F, -7.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F))
		.uv(104, 38).cuboid(5.0F, -5.0F, -7.0F, 3.0F, 5.0F, 2.0F, new Dilation(0.0F))
		.uv(104, 30).cuboid(4.0F, -4.0F, -7.0F, 1.0F, 4.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 55).cuboid(2.0F, -7.0F, -7.0F, 2.0F, 7.0F, 2.0F, new Dilation(0.0F))
		.uv(8, 54).cuboid(1.0F, -8.0F, -7.0F, 1.0F, 8.0F, 2.0F, new Dilation(0.0F))
		.uv(14, 56).cuboid(0.0F, -8.0F, -7.0F, 1.0F, 6.0F, 2.0F, new Dilation(0.0F))
		.uv(20, 55).cuboid(-1.0F, -9.0F, -7.0F, 1.0F, 7.0F, 2.0F, new Dilation(0.0F))
		.uv(26, 52).cuboid(-2.0F, -12.0F, -7.0F, 1.0F, 10.0F, 2.0F, new Dilation(0.0F))
		.uv(32, 50).cuboid(-4.0F, -14.0F, -7.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F))
		.uv(40, 49).cuboid(-6.0F, -15.0F, -7.0F, 2.0F, 13.0F, 2.0F, new Dilation(0.0F))
		.uv(48, 48).cuboid(-8.0F, -16.0F, -7.0F, 2.0F, 14.0F, 2.0F, new Dilation(0.0F))
		.uv(56, 49).cuboid(-10.0F, -15.0F, -7.0F, 2.0F, 13.0F, 2.0F, new Dilation(0.0F))
		.uv(64, 45).cuboid(-11.0F, -19.0F, -7.0F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
		.uv(70, 45).cuboid(-13.0F, -20.0F, -7.0F, 2.0F, 17.0F, 2.0F, new Dilation(0.0F))
		.uv(78, 45).cuboid(-14.0F, -21.0F, -7.0F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
		.uv(84, 50).cuboid(-15.0F, -21.0F, -7.0F, 1.0F, 12.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 37).cuboid(-16.0F, -21.0F, -7.0F, 1.0F, 10.0F, 2.0F, new Dilation(0.0F))
		.uv(6, 38).cuboid(-18.001F, -22.001F, -6.999F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F))
		.uv(14, 38).cuboid(-19.0F, -23.0F, -7.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F))
		.uv(22, 39).cuboid(-20.0F, -23.0F, -7.0F, 1.0F, 8.0F, 2.0F, new Dilation(0.0F))
		.uv(34, 40).cuboid(-22.0F, -24.0F, -7.0F, 1.0F, 7.0F, 2.0F, new Dilation(0.0F))
		.uv(40, 39).cuboid(-23.0F, -25.0F, -7.0F, 1.0F, 8.0F, 2.0F, new Dilation(0.0F))
		.uv(51, 30).cuboid(-26.0F, -26.0F, -7.0F, 3.0F, 5.0F, 2.0F, new Dilation(0.0F))
		.uv(6, 49).cuboid(-24.0F, -21.0F, -7.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 24).cuboid(-25.0F, -21.0F, -7.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(28, 39).cuboid(-21.0F, -24.0F, -7.0F, 1.0F, 8.0F, 2.0F, new Dilation(0.0F))
		.uv(122, 24).cuboid(-15.0F, -8.0F, -7.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 49).cuboid(-10.0F, -19.0F, -7.0F, 1.0F, 4.0F, 2.0F, new Dilation(0.0F))
		.uv(6, 23).cuboid(5.0F, 16.0F, -7.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(109, 16).cuboid(-2.0F, 12.0F, -7.0F, 3.0F, 3.0F, 2.0F, new Dilation(0.0F))
		.uv(110, 10).cuboid(7.0F, 16.0F, -7.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(112, 21).cuboid(-2.0F, 10.0F, -7.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(112, 24).cuboid(-4.0F, 3.0F, -7.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(112, 27).cuboid(-9.0F, -2.0F, -7.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(90, 9).cuboid(-7.0F, 1.0F, -7.0F, 8.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(87, 13).cuboid(-8.0F, -2.0F, -7.0F, 9.0F, 3.0F, 2.0F, new Dilation(0.0F))
		.uv(109, 13).cuboid(1.0F, 16.0F, -7.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(114, 39).cuboid(-1.0F, 8.0F, -7.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F))
		.uv(14, 23).cuboid(-3.0F, 8.0F, -7.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(98, 5).cuboid(-3.0F, 3.0F, -7.0F, 4.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(96, 0).cuboid(-4.0F, 5.0F, -7.0F, 5.0F, 3.0F, 2.0F, new Dilation(0.0F))
		.uv(120, 15).cuboid(16.0F, 5.0F, -7.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 2.0F, 6.0F));

		ModelPartData handle = sword.addChild("handle", ModelPartBuilder.create().uv(118, 0).cuboid(21.0F, 21.0F, -1.0F, 3.0F, 3.0F, 2.0F, new Dilation(0.0F))
		.uv(118, 5).cuboid(19.999F, 19.999F, -0.999F, 3.0F, 3.0F, 2.0F, new Dilation(0.0F))
		.uv(110, 4).cuboid(18.0F, 16.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(110, 7).cuboid(16.0F, 18.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(118, 10).cuboid(16.999F, 16.999F, -0.999F, 3.0F, 3.0F, 2.0F, new Dilation(0.0F))
		.uv(110, 0).cuboid(19.0F, 19.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 128, 64);
	}
	public ModelPart getHandle(){
		return this.handle;
	}
	public ModelPart getBlade(){
		return this.blade;
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		sword.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}