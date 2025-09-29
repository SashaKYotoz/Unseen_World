package net.sashakyotoz.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.animations.EclipseSentinelAnimations;
import net.sashakyotoz.common.entities.bosses.EclipseSentinelEntity;

public class EclipseSentinelModel extends SinglePartEntityModel<EclipseSentinelEntity> implements ModelWithArms {
    public static final EntityModelLayer ECLIPSE_SENTINEL = new EntityModelLayer(UnseenWorld.makeID("eclipse_sentinel"), "main");
    private final ModelPart root;
    private final ModelPart upper_part;
    private final ModelPart head;
    private final ModelPart right_arm;
    private final ModelPart right_arm1;
    private final ModelPart right_arm2;
    private final ModelPart left_arm;
    private final ModelPart left_arm1;
    private final ModelPart below_part;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public EclipseSentinelModel(ModelPart root) {
        super(RenderLayer::getEntityTranslucent);
        this.root = root.getChild("root");
        this.upper_part = this.root.getChild("upper_part");
        this.head = upper_part.getChild("head");
        this.right_arm = upper_part.getChild("right_arm");
        this.right_arm1 = right_arm.getChild("right_arm1");
        this.right_arm2 = right_arm1.getChild("right_arm2");
        this.left_arm = upper_part.getChild("left_arm");
        this.left_arm1 = left_arm.getChild("left_arm1");
        this.below_part = this.root.getChild("below_part");
        this.rightLeg = below_part.getChild("rightLeg");
        this.leftLeg = below_part.getChild("leftLeg");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 2.0F, 0.0F));

        ModelPartData upper_part = root.addChild("upper_part", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -4.0F, 0.0F));

        ModelPartData cube_r1 = upper_part.addChild("cube_r1", ModelPartBuilder.create().uv(72, 51).cuboid(-0.5F, -2.0F, -5.475F, 4.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.5F, 2.0F, -0.525F, -0.1309F, 0.0F, 0.0F));

        ModelPartData cube_r2 = upper_part.addChild("cube_r2", ModelPartBuilder.create().uv(68, 19).cuboid(-5.5F, -1.0F, 0.525F, 11.0F, 6.0F, 5.0F, new Dilation(0.0F))
                .uv(68, 19).cuboid(-1.5F, -1.0F, 0.525F, 11.0F, 6.0F, 5.0F, new Dilation(0.0F))
                .uv(35, 19).cuboid(-3.5F, -2.0F, -5.475F, 11.0F, 7.0F, 11.0F, new Dilation(0.001F)), ModelTransform.of(-2.0F, -6.0F, 2.25F, 0.1745F, 0.0F, 0.0F));

        ModelPartData cube_r3 = upper_part.addChild("cube_r3", ModelPartBuilder.create().uv(0, 0).cuboid(-5.5F, -2.0F, -5.475F, 11.0F, 7.0F, 11.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -5.0F, 0.5F, 0.1745F, 0.7854F, 0.1309F));

        ModelPartData head = upper_part.addChild("head", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -7.0F, 0.0F));

        ModelPartData cube_r4 = head.addChild("cube_r4", ModelPartBuilder.create().uv(0, 62).cuboid(-3.5F, -9.0F, -3.5F, 7.0F, 10.0F, 7.0F, new Dilation(0.25F))
                .uv(70, 60).cuboid(-3.5F, -6.0F, -3.5F, 7.0F, 9.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_arm = upper_part.addChild("right_arm", ModelPartBuilder.create().uv(98, 2).cuboid(-8.0F, -3.0F, -3.5F, 8.0F, 5.0F, 7.0F, new Dilation(0.01F))
                .uv(108, 67).cuboid(-6.5F, 1.0F, -1.5F, 5.0F, 8.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-6.0F, -2.5F, 0.5F, 0.0F, 0.0F, 0.1309F));

        ModelPartData cube_r5 = right_arm.addChild("cube_r5", ModelPartBuilder.create().uv(58, 77).mirrored().cuboid(-4.0F, -5.0F, -4.0F, 5.0F, 5.0F, 9.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, -1.0F, -0.5F, 0.0F, 0.0F, -0.7854F));

        ModelPartData cube_r6 = right_arm.addChild("cube_r6", ModelPartBuilder.create().uv(0, 48).mirrored().cuboid(-9.0F, -6.0F, -3.0F, 10.0F, 6.0F, 7.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.of(0.0F, -1.0F, -0.5F, 0.0F, 0.0F, -0.3927F));

        ModelPartData right_arm1 = right_arm.addChild("right_arm1", ModelPartBuilder.create().uv(108, 115).cuboid(-3.3695F, -0.0086F, -2.5F, 5.0F, 8.0F, 5.0F, new Dilation(0.25F)), ModelTransform.pivot(-4.0F, 7.0F, 1.0F));

        ModelPartData right_arm2 = right_arm1.addChild("right_arm2", ModelPartBuilder.create().uv(103, 30).cuboid(-3.5F, 1.0F, -1.5F, 5.0F, 10.0F, 5.0F, new Dilation(0.1F)), ModelTransform.pivot(1.0F, 1.0F, -1.0F));

        ModelPartData sword = right_arm2.addChild("sword", ModelPartBuilder.create(), ModelTransform.of(-2.0F, 8.5F, -27.5F, 1.5708F, -0.8727F, -1.5708F));

        ModelPartData blade = sword.addChild("blade", ModelPartBuilder.create().uv(222, 110).cuboid(1.0F, 0.0F, -7.0F, 15.0F, 16.0F, 2.0F, new Dilation(0.0F))
                .uv(248, 84).cuboid(16.0F, 0.0F, -7.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(246, 92).cuboid(12.0F, -3.0F, -7.0F, 3.0F, 3.0F, 2.0F, new Dilation(0.0F))
                .uv(250, 97).cuboid(11.0F, -2.0F, -7.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(250, 101).cuboid(10.0F, -3.0F, -7.0F, 1.0F, 3.0F, 2.0F, new Dilation(0.0F))
                .uv(242, 97).cuboid(8.0F, -4.0F, -7.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F))
                .uv(232, 102).cuboid(5.0F, -5.0F, -7.0F, 3.0F, 5.0F, 2.0F, new Dilation(0.0F))
                .uv(232, 94).cuboid(4.0F, -4.0F, -7.0F, 1.0F, 4.0F, 2.0F, new Dilation(0.0F))
                .uv(128, 119).cuboid(2.0F, -7.0F, -7.0F, 2.0F, 7.0F, 2.0F, new Dilation(0.0F))
                .uv(136, 118).cuboid(1.0F, -8.0F, -7.0F, 1.0F, 8.0F, 2.0F, new Dilation(0.0F))
                .uv(142, 120).cuboid(0.0F, -8.0F, -7.0F, 1.0F, 6.0F, 2.0F, new Dilation(0.0F))
                .uv(148, 119).cuboid(-1.0F, -9.0F, -7.0F, 1.0F, 7.0F, 2.0F, new Dilation(0.0F))
                .uv(154, 116).cuboid(-2.0F, -12.0F, -7.0F, 1.0F, 10.0F, 2.0F, new Dilation(0.0F))
                .uv(160, 114).cuboid(-4.0F, -14.0F, -7.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F))
                .uv(168, 113).cuboid(-6.0F, -15.0F, -7.0F, 2.0F, 13.0F, 2.0F, new Dilation(0.0F))
                .uv(176, 112).cuboid(-8.0F, -16.0F, -7.0F, 2.0F, 14.0F, 2.0F, new Dilation(0.0F))
                .uv(184, 113).cuboid(-10.0F, -15.0F, -7.0F, 2.0F, 13.0F, 2.0F, new Dilation(0.0F))
                .uv(192, 109).cuboid(-11.0F, -19.0F, -7.0F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
                .uv(198, 109).cuboid(-13.0F, -20.0F, -7.0F, 2.0F, 17.0F, 2.0F, new Dilation(0.0F))
                .uv(206, 109).cuboid(-14.0F, -21.0F, -7.0F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
                .uv(212, 114).cuboid(-15.0F, -21.0F, -7.0F, 1.0F, 12.0F, 2.0F, new Dilation(0.0F))
                .uv(128, 101).cuboid(-16.0F, -21.0F, -7.0F, 1.0F, 10.0F, 2.0F, new Dilation(0.0F))
                .uv(134, 102).cuboid(-18.001F, -22.001F, -6.999F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F))
                .uv(142, 102).cuboid(-19.0F, -23.0F, -7.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F))
                .uv(150, 103).cuboid(-20.0F, -23.0F, -7.0F, 1.0F, 8.0F, 2.0F, new Dilation(0.0F))
                .uv(162, 104).cuboid(-22.0F, -24.0F, -7.0F, 1.0F, 7.0F, 2.0F, new Dilation(0.0F))
                .uv(168, 103).cuboid(-23.0F, -25.0F, -7.0F, 1.0F, 8.0F, 2.0F, new Dilation(0.0F))
                .uv(179, 94).cuboid(-26.0F, -26.0F, -7.0F, 3.0F, 5.0F, 2.0F, new Dilation(0.0F))
                .uv(134, 113).cuboid(-24.0F, -21.0F, -7.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(128, 88).cuboid(-25.0F, -21.0F, -7.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(156, 103).cuboid(-21.0F, -24.0F, -7.0F, 1.0F, 8.0F, 2.0F, new Dilation(0.0F))
                .uv(250, 88).cuboid(-15.0F, -8.0F, -7.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(128, 113).cuboid(-10.0F, -19.0F, -7.0F, 1.0F, 4.0F, 2.0F, new Dilation(0.0F))
                .uv(134, 87).cuboid(5.0F, 16.0F, -7.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(237, 80).cuboid(-2.0F, 12.0F, -7.0F, 3.0F, 3.0F, 2.0F, new Dilation(0.0F))
                .uv(238, 74).cuboid(7.0F, 16.0F, -7.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(240, 85).cuboid(-2.0F, 10.0F, -7.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(240, 88).cuboid(-4.0F, 3.0F, -7.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(240, 91).cuboid(-9.0F, -2.0F, -7.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(218, 73).cuboid(-7.0F, 1.0F, -7.0F, 8.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(215, 77).cuboid(-8.0F, -2.0F, -7.0F, 9.0F, 3.0F, 2.0F, new Dilation(0.0F))
                .uv(237, 77).cuboid(1.0F, 16.0F, -7.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(242, 103).cuboid(-1.0F, 8.0F, -7.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F))
                .uv(142, 87).cuboid(-3.0F, 8.0F, -7.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(226, 69).cuboid(-3.0F, 3.0F, -7.0F, 4.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(224, 64).cuboid(-4.0F, 5.0F, -7.0F, 5.0F, 3.0F, 2.0F, new Dilation(0.0F))
                .uv(248, 79).cuboid(16.0F, 5.0F, -7.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 2.0F, 6.0F));

        ModelPartData handle = sword.addChild("handle", ModelPartBuilder.create().uv(246, 64).cuboid(21.0F, 21.0F, -1.0F, 3.0F, 3.0F, 2.0F, new Dilation(0.0F))
                .uv(246, 69).cuboid(19.999F, 19.999F, -0.999F, 3.0F, 3.0F, 2.0F, new Dilation(0.0F))
                .uv(238, 68).cuboid(18.0F, 16.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(238, 71).cuboid(16.0F, 18.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(246, 74).cuboid(16.999F, 16.999F, -0.999F, 3.0F, 3.0F, 2.0F, new Dilation(0.0F))
                .uv(238, 64).cuboid(19.0F, 19.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData left_arm = upper_part.addChild("left_arm", ModelPartBuilder.create().uv(98, 2).mirrored().cuboid(0.0F, -3.0F, -3.5F, 8.0F, 5.0F, 7.0F, new Dilation(0.01F)).mirrored(false)
                .uv(108, 67).mirrored().cuboid(1.5F, 1.0F, -1.5F, 5.0F, 8.0F, 5.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(6.0F, -2.5F, 0.5F, 0.0F, 0.0F, -0.1309F));

        ModelPartData cube_r7 = left_arm.addChild("cube_r7", ModelPartBuilder.create().uv(29, 74).cuboid(-1.0F, -5.0F, -4.0F, 5.0F, 5.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F));

        ModelPartData cube_r8 = left_arm.addChild("cube_r8", ModelPartBuilder.create().uv(0, 48).cuboid(-1.0F, -6.0F, -3.0F, 10.0F, 6.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0F, -1.0F, -0.5F, 0.0F, 0.0F, 0.3927F));

        ModelPartData left_arm1 = left_arm.addChild("left_arm1", ModelPartBuilder.create().uv(108, 115).mirrored().cuboid(-1.6305F, -0.0086F, -2.5F, 5.0F, 8.0F, 5.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(4.0F, 7.0F, 1.0F));

        ModelPartData left_arm2 = left_arm1.addChild("left_arm2", ModelPartBuilder.create().uv(103, 30).mirrored().cuboid(-1.5F, 1.0F, -1.5F, 5.0F, 10.0F, 5.0F, new Dilation(0.1F)).mirrored(false), ModelTransform.pivot(-1.0F, 1.0F, -1.0F));

        ModelPartData cape = upper_part.addChild("cape", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -7.0F, 7.0F));

        ModelPartData cube_r9 = cape.addChild("cube_r9", ModelPartBuilder.create().uv(99, 0).cuboid(-6.0F, -0.0684F, -2.0442F, 12.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 25.75F, 5.75F, 0.1309F, 0.0F, 0.0F));

        ModelPartData cube_r10 = cape.addChild("cube_r10", ModelPartBuilder.create().uv(3, 19).cuboid(-7.0F, -8.0F, -1.0F, 14.0F, 27.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 7.0F, 2.25F, 0.1309F, 0.0F, 0.0F));

        ModelPartData below_part = root.addChild("below_part", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -2.0F, 0.0F));

        ModelPartData cube_r11 = below_part.addChild("cube_r11", ModelPartBuilder.create().uv(72, 51).cuboid(-2.0F, -2.6526F, -0.5178F, 4.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 8.0F, -4.525F, -0.0873F, -0.6981F, 0.0F));

        ModelPartData cube_r12 = below_part.addChild("cube_r12", ModelPartBuilder.create().uv(72, 51).cuboid(-2.0F, -2.6526F, -0.5178F, 4.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, 8.0F, -4.525F, -0.0873F, 0.6981F, 0.0F));

        ModelPartData cube_r13 = below_part.addChild("cube_r13", ModelPartBuilder.create().uv(35, 38).cuboid(-4.0F, -6.0F, -5.0F, 9.0F, 12.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 3.0F, 1.5F, 0.0F, 0.7854F, 0.0F));

        ModelPartData rightLeg = below_part.addChild("rightLeg", ModelPartBuilder.create(), ModelTransform.pivot(-3.0F, 6.0F, 2.0F));

        ModelPartData cube_r14 = rightLeg.addChild("cube_r14", ModelPartBuilder.create().uv(0, 115).cuboid(-3.0F, -7.0F, -4.0F, 4.0F, 7.0F, 6.0F, new Dilation(0.11F)), ModelTransform.of(-4.0941F, 6.5F, -0.359F, 0.0F, 0.0F, 0.5236F));

        ModelPartData cube_r15 = rightLeg.addChild("cube_r15", ModelPartBuilder.create().uv(20, 115).cuboid(-3.0F, -7.0F, -4.0F, 4.0F, 7.0F, 6.0F, new Dilation(0.12F)), ModelTransform.of(-4.0941F, 4.5F, -0.359F, 0.0F, 0.0F, 0.5236F));

        ModelPartData cube_r16 = rightLeg.addChild("cube_r16", ModelPartBuilder.create().uv(99, 51).cuboid(-3.0F, -9.0F, -4.0F, 4.0F, 9.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(-2.0941F, 9.5F, -0.359F, 0.0F, 0.0F, 0.2182F));

        ModelPartData cube_r17 = rightLeg.addChild("cube_r17", ModelPartBuilder.create().uv(40, 114).cuboid(-2.0F, -9.0F, -3.0F, 4.0F, 9.0F, 5.0F, new Dilation(0.1F)), ModelTransform.of(-2.0941F, 9.0F, -0.859F, 0.0F, 0.0F, 0.2182F));

        ModelPartData rightFoot = rightLeg.addChild("rightFoot", ModelPartBuilder.create().uv(87, 90).cuboid(-3.6F, 3.0F, -2.85F, 5.0F, 5.0F, 7.0F, new Dilation(0.0F))
                .uv(54, 92).cuboid(-3.6F, 3.0F, -2.85F, 5.0F, 5.0F, 7.0F, new Dilation(0.25F)), ModelTransform.pivot(-1.0F, 10.0F, -2.0F));

        ModelPartData cube_r18 = rightFoot.addChild("cube_r18", ModelPartBuilder.create().uv(0, 80).cuboid(-4.0941F, -3.0F, -2.0F, 6.0F, 5.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.5F, -1.359F, 0.0873F, 0.0F, 0.0F));

        ModelPartData leftLeg = below_part.addChild("leftLeg", ModelPartBuilder.create(), ModelTransform.pivot(3.0F, 6.0F, 0.0F));

        ModelPartData cube_r19 = leftLeg.addChild("cube_r19", ModelPartBuilder.create().uv(0, 115).cuboid(-1.0F, -7.0F, -4.0F, 4.0F, 7.0F, 6.0F, new Dilation(0.11F)), ModelTransform.of(4.0941F, 6.5F, 1.641F, 0.0F, 0.0F, -0.5236F));

        ModelPartData cube_r20 = leftLeg.addChild("cube_r20", ModelPartBuilder.create().uv(20, 115).cuboid(-1.0F, -7.0F, -4.0F, 4.0F, 7.0F, 6.0F, new Dilation(0.12F)), ModelTransform.of(4.0941F, 4.5F, 1.641F, 0.0F, 0.0F, -0.5236F));

        ModelPartData cube_r21 = leftLeg.addChild("cube_r21", ModelPartBuilder.create().uv(0, 94).cuboid(-1.0F, -9.0F, -4.0F, 4.0F, 9.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(2.0941F, 9.5F, 1.641F, 0.0F, 0.0F, -0.2182F));

        ModelPartData cube_r22 = leftLeg.addChild("cube_r22", ModelPartBuilder.create().uv(40, 114).mirrored().cuboid(-2.0F, -9.0F, -3.0F, 4.0F, 9.0F, 5.0F, new Dilation(0.1F)).mirrored(false), ModelTransform.of(2.0941F, 9.0F, 1.141F, 0.0F, 0.0F, -0.2182F));

        ModelPartData leftFoot = leftLeg.addChild("leftFoot", ModelPartBuilder.create().uv(87, 77).cuboid(-1.4F, 3.0F, -2.85F, 5.0F, 5.0F, 7.0F, new Dilation(0.0F))
                .uv(54, 92).mirrored().cuboid(-1.4F, 3.0F, -2.85F, 5.0F, 5.0F, 7.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.pivot(1.0F, 10.0F, 0.0F));

        ModelPartData cube_r23 = leftFoot.addChild("cube_r23", ModelPartBuilder.create().uv(0, 80).mirrored().cuboid(-1.9059F, -3.0F, -2.0F, 6.0F, 5.0F, 8.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 1.5F, -1.359F, 0.0873F, 0.0F, 0.0F));

        ModelPartData spine = below_part.addChild("spine", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 2.0F, 0.5F));

        ModelPartData cube_r24 = spine.addChild("cube_r24", ModelPartBuilder.create().uv(44, 0).cuboid(-5.5F, -2.0F, -3.475F, 9.0F, 7.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, 0.0F, -0.1745F, 0.7854F, -0.1309F));

        ModelPartData eclipse_circle = root.addChild("eclipse_circle", ModelPartBuilder.create().uv(168, 0).cuboid(-22.0F, -23.0F, -1.0F, 44.0F, 44.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 16.0F));
        return TexturedModelData.of(modelData, 256, 128);
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
    public void setAngles(EclipseSentinelEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
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

    @Override
    public void setArmAngle(Arm arm, MatrixStack matrices) {
        matrices.translate(arm == Arm.LEFT ? 0.2 : -0.7, arm == Arm.LEFT ? 0.25 : 0, 0);
        this.getArm(arm).rotate(matrices);
    }

    protected ModelPart getArm(Arm arm) {
        return arm == Arm.LEFT ? this.left_arm1 : this.right_arm2;
    }
}