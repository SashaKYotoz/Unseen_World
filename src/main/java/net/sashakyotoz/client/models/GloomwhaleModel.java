package net.sashakyotoz.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.custom.GloomwhaleEntity;
import net.sashakyotoz.common.entities.animations.GloomwhaleAnimations;

public class GloomwhaleModel extends SinglePartEntityModel<GloomwhaleEntity> {
    public static final EntityModelLayer GLOOMWHALE = new EntityModelLayer(UnseenWorld.makeID("gloomwhale"), "main");
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart jaw;

    public GloomwhaleModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
        this.jaw = this.head.getChild("jaw");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.25F, 11.0F, -11.0F));

        ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(0, 180).cuboid(-12.75F, -11.0F, -37.0F, 24.0F, 14.0F, 36.0F, new Dilation(0.0F))
                .uv(404, 0).cuboid(-10.75F, 3.0F, -35.0F, 20.0F, 1.0F, 34.0F, new Dilation(0.0F))
                .uv(232, 224).cuboid(-16.5F, -15.0F, -2.0F, 32.0F, 30.0F, 6.0F, new Dilation(0.0F))
                .uv(128, 296).cuboid(-14.25F, -15.0F, -21.0F, 8.0F, 8.0F, 12.0F, new Dilation(0.0F))
                .uv(40, 292).mirrored().cuboid(5.25F, -6.0F, -15.0F, 8.0F, 10.0F, 10.0F, new Dilation(0.0F)).mirrored(false)
                .uv(40, 292).cuboid(-14.75F, -6.0F, -15.0F, 8.0F, 10.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -3.0F, -19.0F));

        ModelPartData jaw = head.addChild("jaw", ModelPartBuilder.create().uv(120, 180).cuboid(-12.75F, -1.0F, -36.0F, 24.0F, 8.0F, 36.0F, new Dilation(0.0F))
                .uv(404, 0).cuboid(-10.75F, -2.0F, -34.0F, 20.0F, 1.0F, 34.0F, new Dilation(0.0F))
                .uv(120, 224).cuboid(-12.65F, 3.1F, -32.1F, 24.0F, 8.0F, 32.0F, new Dilation(0.11F)), ModelTransform.pivot(0.0F, 4.0F, -1.0F));

        ModelPartData forward_body = body.addChild("forward_body", ModelPartBuilder.create().uv(0, 122).cuboid(-14.5F, -12.7225F, -1.7196F, 28.0F, 22.0F, 36.0F, new Dilation(0.0F))
                .uv(148, 0).cuboid(-14.4F, 5.3775F, -1.8196F, 28.0F, 8.0F, 36.0F, new Dilation(0.11F))
                .uv(168, 296).cuboid(4.0F, -14.7225F, 5.2804F, 8.0F, 4.0F, 12.0F, new Dilation(0.0F))
                .uv(76, 298).mirrored().cuboid(-16.0F, -8.7225F, 13.2804F, 8.0F, 4.0F, 12.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, -3.0F, -15.0F, -0.1309F, 0.0F, 0.0F));

        ModelPartData cube_r1 = forward_body.addChild("cube_r1", ModelPartBuilder.create().uv(240, 180).cuboid(-16.5F, -30.0F, -2.0F, 32.0F, 30.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 13.2775F, 34.2804F, 0.1309F, 0.0F, 0.0F));

        ModelPartData right_fin = forward_body.addChild("right_fin", ModelPartBuilder.create().uv(276, 64).cuboid(0.8689F, 2.5031F, -3.7185F, 2.0F, 0.0F, 22.0F, new Dilation(0.0F))
                .uv(104, 230).cuboid(-1.1311F, 0.5031F, 14.2815F, 4.0F, 2.0F, 4.0F, new Dilation(0.0F))
                .uv(40, 312).cuboid(-1.1311F, 0.5031F, 2.2815F, 4.0F, 4.0F, 12.0F, new Dilation(0.0F))
                .uv(316, 86).cuboid(-1.1311F, 4.5031F, 0.2815F, 4.0F, 2.0F, 12.0F, new Dilation(0.0F))
                .uv(128, 264).cuboid(-1.1311F, -9.4969F, -3.7185F, 4.0F, 10.0F, 22.0F, new Dilation(0.0F))
                .uv(316, 116).cuboid(-3.1311F, 4.5031F, 0.2815F, 2.0F, 2.0F, 12.0F, new Dilation(0.0F))
                .uv(104, 236).cuboid(-3.1311F, 0.5031F, 14.2815F, 2.0F, 2.0F, 4.0F, new Dilation(0.0F))
                .uv(316, 100).cuboid(-3.1311F, 0.5031F, 2.2815F, 2.0F, 4.0F, 12.0F, new Dilation(0.0F))
                .uv(276, 0).cuboid(-3.1311F, -9.4969F, -3.7185F, 2.0F, 10.0F, 22.0F, new Dilation(0.0F)), ModelTransform.of(-10.5F, 0.2775F, 15.2804F, 0.9599F, 0.0F, -1.8675F));

        ModelPartData left_fin = forward_body.addChild("left_fin", ModelPartBuilder.create().uv(220, 114).cuboid(-2.8689F, 0.5031F, 14.2815F, 4.0F, 2.0F, 4.0F, new Dilation(0.0F))
                .uv(40, 312).mirrored().cuboid(-2.8689F, 0.5031F, 2.2815F, 4.0F, 4.0F, 12.0F, new Dilation(0.0F)).mirrored(false)
                .uv(316, 130).cuboid(-2.8689F, 4.5031F, 2.2815F, 4.0F, 2.0F, 10.0F, new Dilation(0.0F))
                .uv(128, 264).mirrored().cuboid(-2.8689F, -9.4969F, -3.7185F, 4.0F, 10.0F, 22.0F, new Dilation(0.0F)).mirrored(false)
                .uv(208, 296).cuboid(1.1311F, 4.5031F, 2.2815F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F))
                .uv(104, 316).cuboid(1.1311F, 0.5031F, 2.2815F, 2.0F, 4.0F, 12.0F, new Dilation(0.0F))
                .uv(236, 114).cuboid(1.1311F, 0.5031F, 14.2815F, 2.0F, 2.0F, 4.0F, new Dilation(0.0F))
                .uv(276, 0).mirrored().cuboid(1.1311F, -9.4969F, -3.7185F, 2.0F, 10.0F, 22.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(9.5F, 0.2775F, 15.2804F, 0.9599F, 0.0F, 1.8675F));

        ModelPartData back_fin2 = forward_body.addChild("back_fin2", ModelPartBuilder.create().uv(232, 294).cuboid(-1.0F, -1.5F, -5.0F, 2.0F, 10.0F, 18.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -14.7225F, 11.2804F, 1.0472F, 0.0F, 0.0F));

        ModelPartData tail = forward_body.addChild("tail", ModelPartBuilder.create().uv(128, 122).cuboid(-14.5F, -9.8765F, -1.477F, 28.0F, 22.0F, 36.0F, new Dilation(0.0F))
                .uv(148, 44).cuboid(-14.4F, 8.2235F, -1.577F, 28.0F, 8.0F, 36.0F, new Dilation(0.11F)), ModelTransform.of(0.0F, -3.7225F, 34.7804F, 0.2618F, 0.0F, 0.0F));

        ModelPartData cube_r2 = tail.addChild("cube_r2", ModelPartBuilder.create().uv(0, 256).cuboid(-16.5F, -30.0F, -2.0F, 32.0F, 30.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 16.6235F, 33.023F, -0.1309F, 0.0F, 0.0F));

        ModelPartData tail2 = tail.addChild("tail2", ModelPartBuilder.create().uv(0, 0).cuboid(-14.5F, -13.097F, -0.0064F, 28.0F, 22.0F, 46.0F, new Dilation(0.0F))
                .uv(0, 68).cuboid(-14.4F, 3.003F, -0.1064F, 28.0F, 8.0F, 46.0F, new Dilation(0.11F)), ModelTransform.of(0.0F, 3.6235F, 36.523F, -0.1309F, 0.0F, 0.0F));

        ModelPartData tail3 = tail2.addChild("tail3", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -1.097F, 39.9936F));

        ModelPartData body_r1 = tail3.addChild("body_r1", ModelPartBuilder.create().uv(148, 88).cuboid(-0.4981F, -2.0F, 0.0436F, 30.0F, 4.0F, 22.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -2.0F, 0.0F, -0.0873F, 0.0F));

        ModelPartData body_r2 = tail3.addChild("body_r2", ModelPartBuilder.create().uv(0, 230).cuboid(-30.4971F, -1.999F, -0.0446F, 30.0F, 4.0F, 22.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -2.0F, 0.0F, 0.0873F, 0.0F));

        ModelPartData back_fin = tail.addChild("back_fin", ModelPartBuilder.create().uv(0, 292).cuboid(-1.0F, -1.5F, -5.0F, 2.0F, 10.0F, 18.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -12.3765F, 6.523F, 1.0472F, 0.0F, 0.0F));

        ModelPartData right_fin2 = tail.addChild("right_fin2", ModelPartBuilder.create().uv(284, 260).cuboid(-2.0F, 2.0F, -3.0F, 2.0F, 0.0F, 22.0F, new Dilation(0.0F))
                .uv(150, 116).cuboid(-4.0F, 0.0F, 17.0F, 4.0F, 2.0F, 4.0F, new Dilation(0.0F))
                .uv(256, 160).cuboid(-4.0F, 0.0F, 1.0F, 4.0F, 4.0F, 16.0F, new Dilation(0.0F))
                .uv(272, 304).cuboid(-4.0F, 4.0F, 1.0F, 4.0F, 2.0F, 14.0F, new Dilation(0.0F))
                .uv(256, 88).cuboid(-4.0F, -10.0F, -5.0F, 4.0F, 10.0F, 26.0F, new Dilation(0.0F))
                .uv(200, 312).cuboid(-6.0F, 4.0F, 1.0F, 2.0F, 2.0F, 14.0F, new Dilation(0.0F))
                .uv(190, 116).cuboid(-6.0F, 0.0F, 17.0F, 2.0F, 2.0F, 4.0F, new Dilation(0.0F))
                .uv(308, 232).cuboid(-6.0F, 0.0F, 3.0F, 2.0F, 4.0F, 14.0F, new Dilation(0.0F))
                .uv(232, 260).cuboid(-6.0F, -10.0F, -3.0F, 2.0F, 10.0F, 24.0F, new Dilation(0.0F)), ModelTransform.of(-12.5F, -2.3765F, 14.023F, 0.9599F, 0.0F, -1.8675F));

        ModelPartData left_fin2 = tail.addChild("left_fin2", ModelPartBuilder.create().uv(284, 282).cuboid(0.0F, 2.0F, -3.0F, 2.0F, 0.0F, 22.0F, new Dilation(0.0F))
                .uv(170, 116).cuboid(0.0F, 0.0F, 17.0F, 4.0F, 2.0F, 4.0F, new Dilation(0.0F))
                .uv(296, 160).cuboid(0.0F, 0.0F, 1.0F, 4.0F, 4.0F, 16.0F, new Dilation(0.0F))
                .uv(308, 216).cuboid(0.0F, 4.0F, 1.0F, 4.0F, 2.0F, 14.0F, new Dilation(0.0F))
                .uv(256, 124).cuboid(0.0F, -10.0F, -5.0F, 4.0F, 10.0F, 26.0F, new Dilation(0.0F))
                .uv(72, 314).cuboid(4.0F, 4.0F, 1.0F, 2.0F, 2.0F, 14.0F, new Dilation(0.0F))
                .uv(206, 116).cuboid(4.0F, 0.0F, 17.0F, 2.0F, 2.0F, 4.0F, new Dilation(0.0F))
                .uv(308, 304).cuboid(4.0F, 0.0F, 3.0F, 2.0F, 4.0F, 14.0F, new Dilation(0.0F))
                .uv(76, 264).cuboid(4.0F, -10.0F, -3.0F, 2.0F, 10.0F, 24.0F, new Dilation(0.0F)), ModelTransform.of(11.5F, -2.3765F, 14.023F, 0.9599F, 0.0F, 1.8675F));
        return TexturedModelData.of(modelData, 512, 512);
    }

    @Override
    public void setAngles(GloomwhaleEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        if (entity.getVelocity().horizontalLengthSquared() > 1.0E-7)
            this.body.pitch = this.body.pitch + (-0.05F - 0.05F * MathHelper.cos(ageInTicks * 0.3F));
        this.head.yaw = netHeadYaw / (270F / (float) Math.PI);
        this.head.pitch = headPitch / (270F / (float) Math.PI);
        if (entity.getTarget() != null && entity.squaredAttackRange(entity.getTarget()) < 9)
            this.jaw.pitch = 0.1f + MathHelper.sin(entity.age) / 10f;
        this.animateMovement(GloomwhaleAnimations.SWIM, limbSwing, limbSwingAmount, 1, 1);
        this.updateAnimation(entity.death, GloomwhaleAnimations.DEATH, ageInTicks, 0.5f);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }
}