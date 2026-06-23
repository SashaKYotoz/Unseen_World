package net.sashakyotoz.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.custom.ShimmerEntity;

public class ShimmerModel extends SinglePartEntityModel<ShimmerEntity> {
    public static final EntityModelLayer SHIMMER = new EntityModelLayer(UnseenWorld.makeID("shimmer"), "main");

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart right_tentacle;
    private final ModelPart left_tentacle;
    private final ModelPart tentacle_0;
    private final ModelPart tentacle_1;
    private final ModelPart tentacle_2;
    private final ModelPart tentacle_3;
    private final ModelPart tentacle_4;
    private final ModelPart tentacle_5;
    private final ModelPart tentacle_6;
    private final ModelPart tentacle_7;

    public ShimmerModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.right_tentacle = this.head.getChild("right_tentacle");
        this.left_tentacle = this.head.getChild("left_tentacle");
        ModelPart directional = this.head.getChild("directional");
        this.tentacle_0 = directional.getChild("tentacle_0");
        this.tentacle_1 = directional.getChild("tentacle_1");
        this.tentacle_2 = directional.getChild("tentacle_2");
        this.tentacle_3 = directional.getChild("tentacle_3");
        ModelPart diagonal = this.head.getChild("diagonal");
        this.tentacle_4 = diagonal.getChild("tentacle_4");
        this.tentacle_5 = diagonal.getChild("tentacle_5");
        this.tentacle_6 = diagonal.getChild("tentacle_6");
        this.tentacle_7 = diagonal.getChild("tentacle_7");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(84, 0).cuboid(-5.5F, -16.0F, -5.5F, 11.0F, 16.0F, 11.0F, new Dilation(0.0F))
                .uv(63, 13).cuboid(-2.5F, -16.0F, -6.5F, 5.0F, 7.0F, 1.0F, new Dilation(0.0F))
                .uv(63, 13).cuboid(-2.5F, -16.0F, 5.5F, 5.0F, 7.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 38).cuboid(-7.5F, -37.0F, -7.5F, 15.0F, 21.0F, 15.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-10.5F, -35.0F, -10.5F, 21.0F, 17.0F, 21.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 2.0F, 0.0F));

        ModelPartData cube_r1 = head.addChild("cube_r1", ModelPartBuilder.create().uv(63, 13).cuboid(-2.5F, -3.5F, -1.0F, 5.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-6.5F, -12.5F, 0.0F, 0.0F, -1.5708F, 0.0F));

        ModelPartData cube_r2 = head.addChild("cube_r2", ModelPartBuilder.create().uv(63, 13).cuboid(-2.5F, -3.5F, -1.0F, 5.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(5.5F, -12.5F, 0.0F, 0.0F, -1.5708F, 0.0F));

        ModelPartData cube_r3 = head.addChild("cube_r3", ModelPartBuilder.create().uv(78, 38).cuboid(-12.0F, -17.0F, 1.0F, 25.0F, 18.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, -1.0F, -1.0F, 0.0F, -2.3562F, 0.0F));

        ModelPartData cube_r4 = head.addChild("cube_r4", ModelPartBuilder.create().uv(78, 38).cuboid(-12.0F, -17.0F, 1.0F, 25.0F, 18.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, 1.0F, -0.5F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_tentacle = head.addChild("right_tentacle", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, 18.0F, -2.0F, 2.0F, 6.0F, 4.0F, new Dilation(0.0F))
                .uv(12, 0).cuboid(-2.0F, 2.0F, -1.0F, 2.0F, 16.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.0F, -2.0F, 0.0F));

        ModelPartData left_tentacle = head.addChild("left_tentacle", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(0.0F, 18.0F, -2.0F, 2.0F, 6.0F, 4.0F, new Dilation(0.0F)).mirrored(false)
                .uv(12, 0).mirrored().cuboid(0.0F, 2.0F, -1.0F, 2.0F, 16.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(1.0F, -2.0F, 0.0F));

        ModelPartData directional = head.addChild("directional", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData tentacle_0 = directional.addChild("tentacle_0", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 4.0F));

        ModelPartData cube_r5 = tentacle_0.addChild("cube_r5", ModelPartBuilder.create().uv(0, 74).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 19.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        ModelPartData tentacle_1 = directional.addChild("tentacle_1", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, -4.0F));

        ModelPartData cube_r6 = tentacle_1.addChild("cube_r6", ModelPartBuilder.create().uv(0, 74).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 19.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

        ModelPartData tentacle_2 = directional.addChild("tentacle_2", ModelPartBuilder.create(), ModelTransform.pivot(-4.0F, 0.0F, 0.0F));

        ModelPartData cube_r7 = tentacle_2.addChild("cube_r7", ModelPartBuilder.create().uv(0, 74).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 19.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

        ModelPartData tentacle_3 = directional.addChild("tentacle_3", ModelPartBuilder.create(), ModelTransform.pivot(4.0F, 0.0F, 0.0F));

        ModelPartData cube_r8 = tentacle_3.addChild("cube_r8", ModelPartBuilder.create().uv(0, 74).mirrored().cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 19.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

        ModelPartData diagonal = head.addChild("diagonal", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData tentacle_4 = diagonal.addChild("tentacle_4", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 4.0F));

        ModelPartData cube_r9 = tentacle_4.addChild("cube_r9", ModelPartBuilder.create().uv(0, 74).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 19.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        ModelPartData tentacle_5 = diagonal.addChild("tentacle_5", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, -4.0F));

        ModelPartData cube_r10 = tentacle_5.addChild("cube_r10", ModelPartBuilder.create().uv(0, 74).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 19.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

        ModelPartData tentacle_6 = diagonal.addChild("tentacle_6", ModelPartBuilder.create(), ModelTransform.pivot(-4.0F, 0.0F, 0.0F));

        ModelPartData cube_r11 = tentacle_6.addChild("cube_r11", ModelPartBuilder.create().uv(0, 74).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 19.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

        ModelPartData tentacle_7 = diagonal.addChild("tentacle_7", ModelPartBuilder.create(), ModelTransform.pivot(4.0F, 0.0F, 0.0F));

        ModelPartData cube_r12 = tentacle_7.addChild("cube_r12", ModelPartBuilder.create().uv(0, 74).mirrored().cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 19.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, -0.1745F));
        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public void setAngles(ShimmerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float speed = 0.5f;
        float intensity = 0.8f;
        float wave = MathHelper.sin(ageInTicks * speed) * intensity;

        this.right_tentacle.roll = wave * 0.5f;
        this.left_tentacle.roll = -wave * 0.5f;

        this.tentacle_0.pitch = wave;
        this.tentacle_1.pitch = -wave;
        this.tentacle_2.roll = wave;
        this.tentacle_3.roll = -wave;
        this.tentacle_4.pitch = -wave;
        this.tentacle_5.pitch = wave;
        this.tentacle_6.roll = wave;
        this.tentacle_7.roll = -wave;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }
}