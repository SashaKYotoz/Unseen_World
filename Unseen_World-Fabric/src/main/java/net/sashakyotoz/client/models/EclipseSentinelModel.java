package net.sashakyotoz.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.animations.EclipseSentinelAnimations;
import net.sashakyotoz.common.entities.bosses.EclipseSentinel;

public class EclipseSentinelModel extends SinglePartEntityModel<EclipseSentinel> {
    public static final EntityModelLayer ECLIPSE_SENTINEL = new EntityModelLayer(UnseenWorld.makeID("eclipse_sentinel"), "main");
    private final ModelPart root;
    private final ModelPart head;

    public EclipseSentinelModel(ModelPart root) {
        super(RenderLayer::getEntityTranslucent);
        this.root = root;
        ModelPart body = this.root.getChild("body");
        ModelPart chest = body.getChild("chest");
        this.head = chest.getChild("head");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 16).cuboid(-7.5F, -10.0F, -2.5F, 15.0F, 10.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 12.0F, 0.0F));

        ModelPartData cube_r1 = body.addChild("cube_r1", ModelPartBuilder.create().uv(35, 0).mirrored().cuboid(-1.0F, -10.0F, -4.0F, 5.0F, 10.0F, 7.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(7.0F, 2.0F, 0.5F, 0.0F, 0.0F, -0.3054F));

        ModelPartData cube_r2 = body.addChild("cube_r2", ModelPartBuilder.create().uv(35, 0).cuboid(-4.0F, -10.0F, -4.0F, 5.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(-7.0F, 2.0F, 0.5F, 0.0F, 0.0F, 0.3054F));

        ModelPartData chest = body.addChild("chest", ModelPartBuilder.create().uv(0, 32).cuboid(-10.0F, -17.0F, -4.0F, 19.0F, 17.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.5F, -10.0F, 0.0F));

        ModelPartData head = chest.addChild("head", ModelPartBuilder.create(), ModelTransform.pivot(-0.5F, -17.0F, 0.0F));

        ModelPartData cube_r3 = head.addChild("cube_r3", ModelPartBuilder.create().uv(76, 19).cuboid(-3.5F, -9.0F, -3.5F, 7.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, -2.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData cube_r4 = head.addChild("cube_r4", ModelPartBuilder.create().uv(104, 19).cuboid(-3.0F, -7.0F, -3.0F, 6.0F, 7.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -0.5F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_arm = chest.addChild("right_arm", ModelPartBuilder.create().uv(59, 0).cuboid(-5.5F, 5.0F, -2.0F, 4.0F, 16.0F, 4.0F, new Dilation(0.5F)), ModelTransform.pivot(-10.0F, -16.0F, 0.0F));

        ModelPartData cube_r5 = right_arm.addChild("cube_r5", ModelPartBuilder.create().uv(24, 7).cuboid(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, -2.0F, 4.0F, -0.2618F, 0.0F, -0.0873F));

        ModelPartData cube_r6 = right_arm.addChild("cube_r6", ModelPartBuilder.create().uv(24, 7).cuboid(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-4.0F, -2.0F, -4.0F, 0.2618F, 0.0F, -0.0873F));

        ModelPartData cube_r7 = right_arm.addChild("cube_r7", ModelPartBuilder.create().uv(24, 0).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-6.25F, -2.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

        ModelPartData cube_r8 = right_arm.addChild("cube_r8", ModelPartBuilder.create().uv(24, 7).cuboid(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, -2.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

        ModelPartData cube_r9 = right_arm.addChild("cube_r9", ModelPartBuilder.create().uv(32, 0).cuboid(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, -2.0F, -3.0F, 0.0F, 0.0F, 0.2618F));

        ModelPartData cube_r10 = right_arm.addChild("cube_r10", ModelPartBuilder.create().uv(32, 0).cuboid(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-4.0F, -1.5F, 3.0F, 0.0F, 0.0F, -0.2618F));

        ModelPartData cube_r11 = right_arm.addChild("cube_r11", ModelPartBuilder.create().uv(88, 0).cuboid(-8.0F, -4.0F, -5.0F, 10.0F, 9.0F, 10.0F, new Dilation(0.5F)), ModelTransform.of(1.0F, 1.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

        ModelPartData sword = right_arm.addChild("sword", ModelPartBuilder.create().uv(0, 94).cuboid(5.0F, 5.0F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(8, 94).cuboid(4.0F, 4.0F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(6, 98).cuboid(3.0F, 3.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 98).cuboid(2.0F, 2.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(16, 94).cuboid(-7.0F, -7.0F, -0.5F, 10.0F, 10.0F, 1.0F, new Dilation(0.0F))
                .uv(16, 108).cuboid(-7.0F, 3.0F, -0.5F, 5.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(28, 108).cuboid(-4.0F, 6.0F, -0.5F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(28, 110).cuboid(-3.0F, 7.0F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(17, 105).cuboid(-11.0F, 3.0F, -0.5F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(27, 105).cuboid(-9.0F, 1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 101).cuboid(3.0F, -7.0F, -0.5F, 3.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 107).cuboid(6.0F, -4.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(4, 107).cuboid(7.0F, -3.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(8, 101).cuboid(3.0F, -11.0F, -0.5F, 2.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(8, 106).cuboid(1.0F, -9.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 111).cuboid(-12.0F, -12.0F, -0.5F, 5.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(38, 94).cuboid(-24.0F, -24.0F, -0.5F, 10.0F, 10.0F, 1.0F, new Dilation(0.0F))
                .uv(8, 117).cuboid(-10.0F, -7.0F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(12, 114).cuboid(-8.0F, -4.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(16, 114).cuboid(-9.0F, -4.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(16, 114).cuboid(-11.0F, -6.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(26, 112).cuboid(-14.0F, -23.0F, -0.5F, 2.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(22, 114).cuboid(-12.0F, -22.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(24, 120).cuboid(-11.0F, -21.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(36, 118).cuboid(-12.0F, -17.0F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(16, 118).cuboid(-11.0F, -16.0F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(28, 120).cuboid(-10.0F, -15.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(24, 122).cuboid(-9.0F, -14.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(16, 114).cuboid(-8.0F, -13.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(16, 114).cuboid(-7.0F, -12.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(10, 121).cuboid(-7.0F, -11.0F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(4, 121).cuboid(-12.0F, -7.0F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(32, 112).cuboid(-13.0F, -18.0F, -0.5F, 1.0F, 11.0F, 1.0F, new Dilation(0.0F))
                .uv(40, 112).cuboid(-14.0F, -19.0F, -0.5F, 1.0F, 11.0F, 1.0F, new Dilation(0.0F))
                .uv(36, 112).cuboid(-15.0F, -14.0F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(16, 123).cuboid(-16.0F, -14.0F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(20, 120).cuboid(-17.0F, -14.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(20, 124).cuboid(-18.0F, -14.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(16, 114).cuboid(-19.0F, -14.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(22, 117).cuboid(-23.0F, -14.0F, -0.5F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(20, 112).cuboid(-22.0F, -12.0F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(18, 116).cuboid(-21.0F, -11.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(16, 114).cuboid(-4.0F, -9.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(12, 112).cuboid(-4.0F, -8.0F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 117).cuboid(-7.0F, -10.0F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-3.5F, 21.0F, -8.0F, 1.5708F, -0.7854F, -1.5708F));

        ModelPartData trail = sword.addChild("trail", ModelPartBuilder.create().uv(100, 112).cuboid(-16.0F, -3.0F, 0.0F, 14.0F, 16.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -20.0F, 0.0F));

        ModelPartData left_arm = chest.addChild("left_arm", ModelPartBuilder.create().uv(59, 0).mirrored().cuboid(1.5F, 5.0F, -2.0F, 4.0F, 16.0F, 4.0F, new Dilation(0.5F)).mirrored(false), ModelTransform.pivot(9.0F, -16.0F, 0.0F));

        ModelPartData cube_r12 = left_arm.addChild("cube_r12", ModelPartBuilder.create().uv(24, 7).mirrored().cuboid(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(1.0F, -2.0F, 4.0F, -0.2618F, 0.0F, 0.0873F));

        ModelPartData cube_r13 = left_arm.addChild("cube_r13", ModelPartBuilder.create().uv(24, 7).mirrored().cuboid(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(4.0F, -2.0F, -4.0F, 0.2618F, 0.0F, 0.0873F));

        ModelPartData cube_r14 = left_arm.addChild("cube_r14", ModelPartBuilder.create().uv(24, 0).mirrored().cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(6.25F, -2.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        ModelPartData cube_r15 = left_arm.addChild("cube_r15", ModelPartBuilder.create().uv(24, 7).mirrored().cuboid(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(1.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

        ModelPartData cube_r16 = left_arm.addChild("cube_r16", ModelPartBuilder.create().uv(32, 0).mirrored().cuboid(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(1.0F, -2.0F, -3.0F, 0.0F, 0.0F, -0.2618F));

        ModelPartData cube_r17 = left_arm.addChild("cube_r17", ModelPartBuilder.create().uv(32, 0).mirrored().cuboid(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(4.0F, -1.5F, 3.0F, 0.0F, 0.0F, 0.2618F));

        ModelPartData cube_r18 = left_arm.addChild("cube_r18", ModelPartBuilder.create().uv(88, 0).mirrored().cuboid(-2.0F, -4.0F, -5.0F, 10.0F, 9.0F, 10.0F, new Dilation(0.5F)).mirrored(false), ModelTransform.of(-1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

        ModelPartData eclipse_circle = chest.addChild("eclipse_circle", ModelPartBuilder.create().uv(54, 50).cuboid(-18.5F, -18.5F, 0.0F, 37.0F, 37.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.5F, -6.5F, 16.0F));

        ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(104, 32).cuboid(-3.0F, -1.0F, -4.0F, 6.0F, 12.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.0F, 13.0F, 1.0F));

        ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(104, 32).mirrored().cuboid(-3.0F, -1.0F, -4.0F, 6.0F, 12.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(5.0F, 13.0F, 1.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        root.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void setAngles(EclipseSentinel entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.head.yaw = headYaw / (180F / (float) Math.PI);
        this.head.pitch = headPitch / (180F / (float) Math.PI);
        switch (entity.getSentinelPose()) {
            case HARD_RUSH ->
                    this.animateMovement(EclipseSentinelAnimations.HARD_RUSH, limbAngle, limbDistance, 2f, 2f);
            default -> this.animateMovement(EclipseSentinelAnimations.WALK, limbAngle, limbDistance, 2.5f, 2.5f);
        }
        this.updateAnimation(entity.death, EclipseSentinelAnimations.DEATH, animationProgress, 0.5f);
        this.updateAnimation(entity.backflip, EclipseSentinelAnimations.BACKFLIP, animationProgress);
        this.updateAnimation(entity.darkness, EclipseSentinelAnimations.DARKNESS, animationProgress);
        this.updateAnimation(entity.idling, EclipseSentinelAnimations.IDLING, animationProgress);
        this.updateAnimation(entity.sword_swing, EclipseSentinelAnimations.SWORD_SWING, animationProgress);
        this.updateAnimation(entity.sky_jumping, EclipseSentinelAnimations.SKY_JUMPING, animationProgress);
        this.updateAnimation(entity.beaming, EclipseSentinelAnimations.BEAMING, animationProgress);
        this.updateAnimation(entity.rush_and_swing, EclipseSentinelAnimations.RUSH_AND_SWING, animationProgress);
        this.updateAnimation(entity.blasting_eroflame, EclipseSentinelAnimations.BLASTING_EROFRAME, animationProgress);
        this.updateAnimation(entity.exaltation, EclipseSentinelAnimations.EXALTATION, animationProgress, 0.5f);
        this.updateAnimation(entity.exalting, EclipseSentinelAnimations.EXALTING, animationProgress);
        this.updateAnimation(entity.heavy_swing, EclipseSentinelAnimations.HEAVY_SWING, animationProgress);
    }
}