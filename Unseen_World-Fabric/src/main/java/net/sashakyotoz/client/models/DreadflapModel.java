package net.sashakyotoz.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.animations.DreadflapAnimations;
import net.sashakyotoz.common.entities.custom.DreadflapEntity;

public class DreadflapModel extends SinglePartEntityModel<DreadflapEntity> {
    public static final EntityModelLayer DREADFLAP = new EntityModelLayer(UnseenWorld.makeID("dreadflap"), "main");

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;

    public DreadflapModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 11.0F, 0.0F));

        ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(26, 0).cuboid(-2.5F, -4.0F, -5.0F, 5.0F, 4.0F, 5.0F, new Dilation(0.0F))
                .uv(24, 23).cuboid(2.5F, -10.0F, 0.0F, 3.0F, 9.0F, 0.0F, new Dilation(0.0F))
                .uv(24, 23).mirrored().cuboid(-5.5F, -10.0F, 0.0F, 3.0F, 9.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, -1.0F, 1.0F));

        ModelPartData main_body = body.addChild("main_body", ModelPartBuilder.create().uv(28, 9).cuboid(-2.5F, 0.0F, -2.0F, 5.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(31, 19).cuboid(-1.5F, 6.0F, -1.0F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F))
                .uv(16, 19).cuboid(0.0F, 6.0F, 0.0F, 0.0F, 9.0F, 4.0F, new Dilation(0.0F))
                .uv(16, 7).cuboid(0.0F, -2.0F, 0.0F, 0.0F, 10.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -1.0F, 1.0F));

        ModelPartData cube_r1 = main_body.addChild("cube_r1", ModelPartBuilder.create().uv(16, 19).mirrored().cuboid(-1.5F, 4.0F, 0.5F, 0.0F, 9.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 3.0F, 2.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData cube_r2 = main_body.addChild("cube_r2", ModelPartBuilder.create().uv(16, 19).cuboid(1.5F, 4.0F, 0.5F, 0.0F, 9.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 3.0F, 2.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_s_b = main_body.addChild("right_s_b", ModelPartBuilder.create().uv(16, 0).mirrored().cuboid(-4.0F, -10.0F, 0.0F, 5.0F, 13.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-3.5F, 1.0F, 1.0F));

        ModelPartData right_wing = right_s_b.addChild("right_wing", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-8.0F, -11.0F, 0.0F, 8.0F, 28.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-4.0F, 0.0F, 0.0F));

        ModelPartData left_s_b = main_body.addChild("left_s_b", ModelPartBuilder.create().uv(16, 0).cuboid(-1.0F, -10.0F, 0.0F, 5.0F, 13.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(3.5F, 1.0F, 1.0F));

        ModelPartData left_wing = left_s_b.addChild("left_wing", ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, -11.0F, 0.0F, 8.0F, 28.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(4.0F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(DreadflapEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        if (entity.isRoosting())
            this.setRoostingHeadAngles(netHeadYaw);

        float f = Math.min((float) entity.getVelocity().lengthSquared() * 50.0F, 1.5F);

        this.updateAnimation(entity.flying, DreadflapAnimations.FLYING, ageInTicks, f);
        this.updateAnimation(entity.roosting, DreadflapAnimations.ROOSTING, ageInTicks, f);
    }

    private void setRoostingHeadAngles(float yaw) {
        this.head.yaw = yaw * (float) (Math.PI / 180.0);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }
}