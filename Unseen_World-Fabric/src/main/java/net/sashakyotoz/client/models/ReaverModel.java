package net.sashakyotoz.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.custom.ReaverEntity;

public class ReaverModel extends SinglePartEntityModel<ReaverEntity> {
    public static final EntityModelLayer REAVER = new EntityModelLayer(UnseenWorld.makeID("reaver"), "main");

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart left_arm;
    private final ModelPart right_arm;
    private final ModelPart left_leg;
    private final ModelPart right_leg;

    public ReaverModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
        this.left_arm = this.body.getChild("left_arm");
        this.right_arm = this.body.getChild("right_arm");
        this.left_leg = root.getChild("left_leg");
        this.right_leg = root.getChild("right_leg");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 52).cuboid(0.0F, -1.0F, -1.0F, 6.0F, 6.0F, 6.0F, new Dilation(0.0F))
                .uv(0, 16).cuboid(-6.0F, -8.0F, -1.0F, 12.0F, 7.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 3.0F, -2.0F));

        ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -7.0F, -2.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
                .uv(24, 0).cuboid(-7.0F, -7.0F, -2.01F, 14.0F, 6.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, -9.0F, 0.0F));

        ModelPartData left_arm = body.addChild("left_arm", ModelPartBuilder.create().uv(42, 42).cuboid(0.0F, -3.0F, -1.0F, 2.0F, 20.0F, 2.0F, new Dilation(0.0F))
                .uv(50, 40).cuboid(0.0F, -3.0F, -2.0F, 3.0F, 20.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(6.0F, -7.0F, 2.0F));

        ModelPartData right_arm = body.addChild("right_arm", ModelPartBuilder.create().uv(42, 42).mirrored().cuboid(-2.0F, -3.0F, -1.0F, 2.0F, 20.0F, 2.0F, new Dilation(0.0F)).mirrored(false)
                .uv(50, 40).mirrored().cuboid(-3.0F, -3.0F, -2.0F, 3.0F, 20.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-6.0F, -3.0F, 2.0F));

        ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(34, 43).cuboid(1.0F, 3.0F, -1.0F, 2.0F, 15.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 7.0F, 0.0F));

        ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(34, 43).mirrored().cuboid(-3.0F, -1.0F, -1.0F, 2.0F, 19.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 7.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(ReaverEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        left_leg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        right_leg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }
}