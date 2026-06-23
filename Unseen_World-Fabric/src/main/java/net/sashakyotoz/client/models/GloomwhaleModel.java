package net.sashakyotoz.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.custom.basic.WhaleEntity;

public class GloomwhaleModel<T extends WhaleEntity> extends SinglePartEntityModel<T> {
    public static final EntityModelLayer GLOOMWHALE = new EntityModelLayer(UnseenWorld.makeID("gloomwhale"), "main");
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart jaw;
    private final ModelPart tail;
    private final ModelPart left_fin;
    private final ModelPart right_fin;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart tail4;

    public GloomwhaleModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.jaw = this.head.getChild("jaw");
        this.tail = this.head.getChild("tail");
        this.left_fin = this.tail.getChild("left_fin");
        this.right_fin = this.tail.getChild("right_fin");
        this.tail1 = this.tail.getChild("tail1");
        this.tail2 = this.tail1.getChild("tail2");
        this.tail3 = this.tail2.getChild("tail3");
        this.tail4 = this.tail3.getChild("tail4");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 160).cuboid(-21.0F, -18.0F, -60.0F, 42.0F, 32.0F, 64.0F, new Dilation(0.0F))
                .uv(0, 7).cuboid(18.0F, -13.0F, -61.0F, 0.0F, 12.0F, 1.0F, new Dilation(0.0F))
                .uv(2, 6).cuboid(-20.0F, -18.0F, -62.0F, 0.0F, 12.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 4.0F, 16.0F));

        ModelPartData cube_r1 = head.addChild("cube_r1", ModelPartBuilder.create().uv(-6, 2).mirrored().cuboid(0.0F, 0.0F, -5.0F, 5.0F, 0.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(24.0F, -11.0F, -3.0F, -2.3562F, 0.0F, 3.1416F));

        ModelPartData cube_r2 = head.addChild("cube_r2", ModelPartBuilder.create().uv(-8, 0).mirrored().cuboid(-6.0F, 0.0F, -4.0F, 6.0F, 0.0F, 8.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-20.0F, -4.0F, -24.0F, -1.5708F, 0.3927F, 0.0F));

        ModelPartData cube_r3 = head.addChild("cube_r3", ModelPartBuilder.create().uv(-8, 0).mirrored().cuboid(-6.0F, 0.0F, -4.0F, 6.0F, 0.0F, 8.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(21.0F, -4.0F, -22.0F, -1.5708F, 0.3927F, 3.1416F));

        ModelPartData cube_r4 = head.addChild("cube_r4", ModelPartBuilder.create().uv(-8, 0).cuboid(0.0F, 0.0F, -5.0F, 6.0F, 0.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(21.0F, 8.0F, -3.0F, 0.0F, 0.0F, -0.3927F));

        ModelPartData cube_r5 = head.addChild("cube_r5", ModelPartBuilder.create().uv(88, 0).mirrored().cuboid(-6.0F, -12.0F, 0.0F, 10.0F, 12.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-15.0F, -17.0F, -55.0F, 0.3927F, 0.7854F, 0.0F));

        ModelPartData cube_r6 = head.addChild("cube_r6", ModelPartBuilder.create().uv(88, 0).mirrored().cuboid(-6.0F, -12.0F, 0.0F, 12.0F, 12.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(15.0F, -15.0F, -19.0F, -0.3927F, 0.0F, 0.0F));

        ModelPartData cube_r7 = head.addChild("cube_r7", ModelPartBuilder.create().uv(88, 0).cuboid(-6.0F, -12.0F, 0.0F, 12.0F, 12.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-15.0F, -16.0F, -33.0F, -0.3927F, 0.3927F, 0.0F));

        ModelPartData cube_r8 = head.addChild("cube_r8", ModelPartBuilder.create().uv(88, 0).cuboid(-6.0F, -12.0F, 0.0F, 12.0F, 12.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, -18.0F, -60.0F, 0.3927F, 0.0F, 0.0F));

        ModelPartData crystal_g = head.addChild("crystal_g", ModelPartBuilder.create(), ModelTransform.pivot(5.0F, -16.0F, -51.5F));

        ModelPartData crystal = crystal_g.addChild("crystal", ModelPartBuilder.create().uv(8, 58).cuboid(-2.0F, -8.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2618F, 0.0873F, -0.2182F));

        ModelPartData cube_r9 = crystal.addChild("cube_r9", ModelPartBuilder.create().uv(0, 61).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData cube_r10 = crystal.addChild("cube_r10", ModelPartBuilder.create().uv(0, 58).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData crystal1 = crystal_g.addChild("crystal1", ModelPartBuilder.create().uv(8, 58).cuboid(-2.0F, -8.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.0F, 0.0F, -0.1309F, 0.0F, 0.2618F));

        ModelPartData cube_r11 = crystal1.addChild("cube_r11", ModelPartBuilder.create().uv(0, 61).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData cube_r12 = crystal1.addChild("cube_r12", ModelPartBuilder.create().uv(0, 58).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData crystal2 = crystal_g.addChild("crystal2", ModelPartBuilder.create().uv(8, 58).mirrored().cuboid(-2.0F, -8.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(11.0F, 0.0F, 6.0F, 0.1309F, 0.0F, -0.2618F));

        ModelPartData cube_r13 = crystal2.addChild("cube_r13", ModelPartBuilder.create().uv(0, 61).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData cube_r14 = crystal2.addChild("cube_r14", ModelPartBuilder.create().uv(0, 58).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData crystal3 = crystal_g.addChild("crystal3", ModelPartBuilder.create().uv(8, 58).cuboid(-2.0F, -8.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(10.0F, 2.0F, -6.0F, -0.2618F, -0.0873F, 0.2182F));

        ModelPartData cube_r15 = crystal3.addChild("cube_r15", ModelPartBuilder.create().uv(0, 61).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData cube_r16 = crystal3.addChild("cube_r16", ModelPartBuilder.create().uv(0, 58).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData crystal_g1 = head.addChild("crystal_g1", ModelPartBuilder.create(), ModelTransform.of(12.0F, -15.0F, -51.5F, 0.0F, -1.5708F, 0.0F));

        ModelPartData crystal4 = crystal_g1.addChild("crystal4", ModelPartBuilder.create().uv(8, 58).cuboid(-2.0F, -8.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2618F, 0.0873F, -0.2182F));

        ModelPartData cube_r17 = crystal4.addChild("cube_r17", ModelPartBuilder.create().uv(0, 61).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData cube_r18 = crystal4.addChild("cube_r18", ModelPartBuilder.create().uv(0, 58).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData crystal5 = crystal_g1.addChild("crystal5", ModelPartBuilder.create().uv(8, 58).cuboid(-2.0F, -8.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.0F, 0.0F, -0.1309F, 0.0F, 0.2618F));

        ModelPartData cube_r19 = crystal5.addChild("cube_r19", ModelPartBuilder.create().uv(0, 61).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData cube_r20 = crystal5.addChild("cube_r20", ModelPartBuilder.create().uv(0, 58).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData crystal6 = crystal_g1.addChild("crystal6", ModelPartBuilder.create().uv(8, 58).mirrored().cuboid(-2.0F, -8.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(11.0F, 0.0F, 6.0F, 0.1309F, 0.0F, -0.2618F));

        ModelPartData cube_r21 = crystal6.addChild("cube_r21", ModelPartBuilder.create().uv(0, 61).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData cube_r22 = crystal6.addChild("cube_r22", ModelPartBuilder.create().uv(0, 58).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData crystal7 = crystal_g1.addChild("crystal7", ModelPartBuilder.create().uv(8, 58).cuboid(-2.0F, -8.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(10.0F, 2.0F, -6.0F, -0.2618F, -0.0873F, 0.2182F));

        ModelPartData cube_r23 = crystal7.addChild("cube_r23", ModelPartBuilder.create().uv(0, 61).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData cube_r24 = crystal7.addChild("cube_r24", ModelPartBuilder.create().uv(0, 58).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData crystal_g2 = head.addChild("crystal_g2", ModelPartBuilder.create(), ModelTransform.pivot(-23.0F, -16.0F, -55.5F));

        ModelPartData crystal8 = crystal_g2.addChild("crystal8", ModelPartBuilder.create().uv(8, 58).cuboid(-2.0F, -8.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, 2.0F, -2.0F, 0.4878F, 0.2482F, -0.5186F));

        ModelPartData cube_r25 = crystal8.addChild("cube_r25", ModelPartBuilder.create().uv(0, 61).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData cube_r26 = crystal8.addChild("cube_r26", ModelPartBuilder.create().uv(0, 58).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData crystal9 = crystal_g2.addChild("crystal9", ModelPartBuilder.create().uv(8, 58).cuboid(-2.0F, -8.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(7.0F, 1.0F, 7.0F, -0.1309F, 0.0F, 0.2618F));

        ModelPartData cube_r27 = crystal9.addChild("cube_r27", ModelPartBuilder.create().uv(0, 61).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData cube_r28 = crystal9.addChild("cube_r28", ModelPartBuilder.create().uv(0, 58).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData crystal10 = crystal_g2.addChild("crystal10", ModelPartBuilder.create().uv(8, 58).cuboid(-2.0F, -8.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(8.0F, 2.0F, 0.0F, 1.0663F, -0.253F, 0.3624F));

        ModelPartData cube_r29 = crystal10.addChild("cube_r29", ModelPartBuilder.create().uv(0, 61).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData cube_r30 = crystal10.addChild("cube_r30", ModelPartBuilder.create().uv(0, 58).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData jaw = head.addChild("jaw", ModelPartBuilder.create().uv(0, 95).cuboid(-15.0F, 0.0F, -58.0F, 30.0F, 6.0F, 58.0F, new Dilation(0.02F))
                .uv(176, 100).cuboid(-14.0F, -1.0F, -58.0F, 28.0F, 1.0F, 58.0F, new Dilation(0.0F))
                .uv(212, 192).cuboid(-15.0F, 6.0F, -58.0F, 30.0F, 6.0F, 58.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 14.0F, 4.0F));

        ModelPartData tail = head.addChild("tail", ModelPartBuilder.create().uv(0, 0).cuboid(-16.0F, -12.0F, 4.0F, 32.0F, 34.0F, 24.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -4.0F, 0.0F));

        ModelPartData cube_r31 = tail.addChild("cube_r31", ModelPartBuilder.create().uv(88, 0).cuboid(-7.0F, -11.0F, 0.0F, 12.0F, 12.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(11.0F, -12.0F, 14.0F, -0.3927F, 0.0F, 0.0F));

        ModelPartData left_fin = tail.addChild("left_fin", ModelPartBuilder.create().uv(364, 232).cuboid(0.0F, 0.0F, -8.0F, 32.0F, 0.0F, 24.0F, new Dilation(0.0F)), ModelTransform.of(16.0F, 8.0F, 12.0F, 0.0F, 0.0F, 0.48F));

        ModelPartData right_fin = tail.addChild("right_fin", ModelPartBuilder.create().uv(364, 232).mirrored().cuboid(-32.0F, 0.0F, -8.0F, 32.0F, 0.0F, 24.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-16.0F, 8.0F, 12.0F, 0.0F, 0.0F, -0.7418F));

        ModelPartData tail1 = tail.addChild("tail1", ModelPartBuilder.create().uv(112, 0).cuboid(-13.0F, -10.0F, 6.0F, 26.0F, 31.0F, 22.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 22.0F));

        ModelPartData cube_r32 = tail1.addChild("cube_r32", ModelPartBuilder.create().uv(94, 3).cuboid(-7.0F, -5.0F, 0.0F, 6.0F, 8.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-14.0F, -3.0F, 19.0F, -0.3927F, 0.0F, -1.5708F));

        ModelPartData tail2 = tail1.addChild("tail2", ModelPartBuilder.create().uv(208, 0).cuboid(-11.0F, -3.0F, 4.0F, 22.0F, 25.0F, 25.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.0F, 24.0F));

        ModelPartData tail3 = tail2.addChild("tail3", ModelPartBuilder.create().uv(302, 0).cuboid(-7.0F, -8.0F, 4.0F, 14.0F, 17.0F, 20.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 13.0F, 25.0F));

        ModelPartData tail4 = tail3.addChild("tail4", ModelPartBuilder.create().uv(356, 200).cuboid(-21.0F, 0.0F, -5.0F, 42.0F, 0.0F, 32.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 1.0F, 18.0F));
        return TexturedModelData.of(modelData, 512, 256);
    }

    @Override
    public void setAngles(WhaleEntity entity, float limbSwing, float limbSwingAmount, float animationProgress, float headYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);

        this.head.pitch = headPitch * ((float) Math.PI / 180F);
        this.head.yaw = headYaw * ((float) Math.PI / 180F);

        float waveFreq = 0.3f;
        float waveAmp = 0.35f * limbSwingAmount;

        this.tail.pitch = MathHelper.cos(limbSwing * waveFreq) * waveAmp * 0.3f;
        this.tail1.pitch = MathHelper.cos(limbSwing * waveFreq - 0.6f) * waveAmp * 0.5f;
        this.tail2.pitch = MathHelper.cos(limbSwing * waveFreq - 1.2f) * waveAmp * 0.7f;
        this.tail3.pitch = MathHelper.cos(limbSwing * waveFreq - 1.8f) * waveAmp * 0.9f;
        this.tail4.pitch = MathHelper.cos(limbSwing * waveFreq - 2.4f) * waveAmp * 1.1f;

        float finSwing = MathHelper.cos(limbSwing * waveFreq * 0.5f) * limbSwingAmount;
        this.right_fin.roll = 0.25f + finSwing * 0.4f;
        this.left_fin.roll = -0.25f - finSwing * 0.4f;
        this.right_fin.yaw = 0.15f + finSwing * 0.2f;
        this.left_fin.yaw = -0.15f - finSwing * 0.2f;

        if (entity.getTarget() != null && entity.squaredDistanceTo(entity.getTarget()) < 9.0F)
            this.jaw.pitch = 0.55f;
        else
            this.jaw.pitch = entity.isOnGround() ? 0 : MathHelper.cos(animationProgress * 0.1f) * 0.025f;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        root.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }
}