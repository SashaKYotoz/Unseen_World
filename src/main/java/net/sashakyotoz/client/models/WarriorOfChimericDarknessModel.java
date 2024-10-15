package net.sashakyotoz.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.animations.WarriorOfChimericDarknessAnimations;
import net.sashakyotoz.common.entities.bosses.WarriorOfChimericDarkness;

public class WarriorOfChimericDarknessModel extends SinglePartEntityModel<WarriorOfChimericDarkness> {
    public static final EntityModelLayer WARRIOR_OF_CHIMERIC_DARKNESS = new EntityModelLayer(UnseenWorld.makeID("warrior_of_chimeric_darkness"), "main");
    private final ModelPart root;

    public WarriorOfChimericDarknessModel(ModelPart root) {
        super(RenderLayer::getEntityTranslucent);
        this.root = root.getChild("root");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 8.0F, 0.0F));

        ModelPartData head = root.addChild("head", ModelPartBuilder.create().uv(13, 0).cuboid(-1.9F, -12.0F, -5.6F, 4.0F, 5.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 36).cuboid(-2.9F, -14.0F, -4.6F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F))
                .uv(0, 36).mirrored().cuboid(0.1F, -14.0F, -4.6F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)).mirrored(false)
                .uv(8, 7).cuboid(-4.0F, -4.0F, -3.0F, 1.0F, 3.0F, 3.0F, new Dilation(0.25F))
                .uv(0, 7).mirrored().cuboid(-4.0F, -9.0F, -0.75F, 1.0F, 3.0F, 3.0F, new Dilation(0.25F)).mirrored(false)
                .uv(8, 7).cuboid(3.0F, -4.0F, -3.0F, 1.0F, 3.0F, 3.0F, new Dilation(0.25F))
                .uv(0, 7).cuboid(3.0F, -9.0F, -0.75F, 1.0F, 3.0F, 3.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, -13.0F, 0.0F));

        ModelPartData cube_r1 = head.addChild("cube_r1", ModelPartBuilder.create().uv(0, 111).cuboid(-4.0941F, -10.0F, -4.0F, 7.0F, 10.0F, 7.0F, new Dilation(0.325F))
                .uv(27, 0).cuboid(-4.0941F, -10.0F, -4.0F, 7.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0941F, 0.0F, 0.641F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body = root.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -8.0F, 0.0F));

        ModelPartData cube_r2 = body.addChild("cube_r2", ModelPartBuilder.create().uv(80, 102).cuboid(-6.0F, -7.0F, -6.0F, 12.0F, 14.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(0.0923F, 2.0F, 0.2637F, 0.1745F, -0.7854F, -0.1222F));

        ModelPartData belt = body.addChild("belt", ModelPartBuilder.create().uv(100, 74).cuboid(-4.9059F, 3.0F, 2.5236F, 10.0F, 10.0F, 4.0F, new Dilation(0.0F))
                .uv(100, 74).cuboid(-4.9059F, -2.0F, 3.5236F, 10.0F, 10.0F, 4.0F, new Dilation(-0.01F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r3 = belt.addChild("cube_r3", ModelPartBuilder.create().uv(0, 26).cuboid(-1.5F, -2.75F, -1.75F, 6.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.5F, 13.25F, -4.5F, 0.2182F, 0.0F, 0.0F));

        ModelPartData cube_r4 = belt.addChild("cube_r4", ModelPartBuilder.create().uv(0, 23).mirrored().cuboid(-1.5F, -2.75F, -1.75F, 6.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-1.5F, 12.0F, -5.25F, -0.2182F, 0.0F, 0.0F));

        ModelPartData cube_r5 = belt.addChild("cube_r5", ModelPartBuilder.create().uv(0, 26).cuboid(-1.5F, -2.75F, -1.75F, 6.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.5F, 10.0F, -5.75F, -0.2182F, 0.0F, 0.0F));

        ModelPartData cube_r6 = belt.addChild("cube_r6", ModelPartBuilder.create().uv(0, 26).mirrored().cuboid(-1.5F, -2.75F, -1.75F, 6.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-1.5F, 8.0F, -6.25F, -0.2182F, 0.0F, 0.0F));

        ModelPartData cube_r7 = belt.addChild("cube_r7", ModelPartBuilder.create().uv(18, 73).cuboid(-6.0F, -2.0F, -6.0F, 5.0F, 5.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0923F, 2.0F, -0.7363F, 0.1745F, -0.7854F, -0.1222F));

        ModelPartData cube_r8 = belt.addChild("cube_r8", ModelPartBuilder.create().uv(36, 116).mirrored().cuboid(-6.0F, -7.0F, -4.0F, 12.0F, 2.0F, 10.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-3.2559F, 2.0F, 0.5337F, 0.1719F, -0.8306F, 0.1222F));

        ModelPartData cube_r9 = belt.addChild("cube_r9", ModelPartBuilder.create().uv(84, 88).mirrored().cuboid(-4.0F, -7.0F, -6.0F, 10.0F, 2.0F, 12.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(3.7441F, 2.0F, 0.7837F, 0.1621F, -0.741F, -0.3561F));

        ModelPartData cube_r10 = belt.addChild("cube_r10", ModelPartBuilder.create().uv(94, 71).cuboid(-5.0F, -6.0F, -5.0F, 10.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0941F, 2.0F, 0.5236F, 0.3927F, 0.0F, 0.0F));

        ModelPartData rightLeg = root.addChild("rightLeg", ModelPartBuilder.create(), ModelTransform.pivot(-3.0F, 0.0F, 1.0F));

        ModelPartData cube_r11 = rightLeg.addChild("cube_r11", ModelPartBuilder.create().uv(0, 83).mirrored().cuboid(-2.0F, -8.0F, -4.0F, 4.0F, 8.0F, 6.0F, new Dilation(0.1F)).mirrored(false), ModelTransform.of(-2.9059F, 7.0F, 0.641F, 0.0F, 0.0F, 0.2618F));

        ModelPartData cube_r12 = rightLeg.addChild("cube_r12", ModelPartBuilder.create().uv(0, 70).cuboid(-2.0F, -8.0F, -3.0F, 4.0F, 8.0F, 5.0F, new Dilation(0.1F)), ModelTransform.of(-1.9059F, 7.0F, 0.141F, 0.0F, 0.0F, 0.2618F));

        ModelPartData rightFoot = rightLeg.addChild("rightFoot", ModelPartBuilder.create().uv(104, 44).cuboid(-3.4059F, 3.0F, -0.859F, 5.0F, 5.0F, 7.0F, new Dilation(0.0F))
                .uv(107, 56).cuboid(-3.4059F, 1.0F, -1.859F, 5.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.0F, 8.0F, -3.0F));

        ModelPartData cube_r13 = rightFoot.addChild("cube_r13", ModelPartBuilder.create().uv(0, 97).cuboid(-4.0F, -3.0F, -2.0F, 6.0F, 5.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0941F, 1.5F, 0.641F, 0.0873F, 0.0F, 0.0F));

        ModelPartData leftLeg = root.addChild("leftLeg", ModelPartBuilder.create(), ModelTransform.pivot(3.0F, 0.0F, 1.0F));

        ModelPartData cube_r14 = leftLeg.addChild("cube_r14", ModelPartBuilder.create().uv(0, 83).cuboid(-1.0F, -8.0F, -4.0F, 4.0F, 8.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(2.0941F, 7.5F, 0.641F, 0.0F, 0.0F, -0.2618F));

        ModelPartData cube_r15 = leftLeg.addChild("cube_r15", ModelPartBuilder.create().uv(0, 70).cuboid(-2.0F, -8.0F, -3.0F, 4.0F, 8.0F, 5.0F, new Dilation(0.1F)), ModelTransform.of(2.0941F, 7.0F, 0.141F, 0.0F, 0.0F, -0.2618F));

        ModelPartData leftFoot = leftLeg.addChild("leftFoot", ModelPartBuilder.create().uv(104, 44).cuboid(-1.4059F, 3.0F, -0.859F, 5.0F, 5.0F, 7.0F, new Dilation(0.0F))
                .uv(107, 56).mirrored().cuboid(-1.4059F, 1.0F, -1.859F, 5.0F, 6.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(1.0F, 8.0F, -3.0F));

        ModelPartData cube_r16 = leftFoot.addChild("cube_r16", ModelPartBuilder.create().uv(0, 97).cuboid(-2.0F, -3.0F, -2.0F, 6.0F, 5.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0941F, 1.5F, 0.641F, 0.0873F, 0.0F, 0.0F));

        ModelPartData rightArm = root.addChild("rightArm", ModelPartBuilder.create(), ModelTransform.of(-9.0F, -16.0F, -2.0F, 0.0F, 0.0F, 0.0F));

        ModelPartData arm0_r1 = rightArm.addChild("arm0_r1", ModelPartBuilder.create().uv(106, 26).cuboid(-13.7385F, -30.0114F, -3.0F, 5.0F, 12.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(6.9725F, 30.8896F, 2.0F, 0.0F, 0.0F, 0.0873F));

        ModelPartData arm0_r2 = rightArm.addChild("arm0_r2", ModelPartBuilder.create().uv(16, 57).cuboid(-1.7447F, -5.0114F, -0.0566F, 5.0F, 10.0F, 3.0F, new Dilation(0.15F)), ModelTransform.of(-3.1516F, 8.9237F, 2.0F, 0.0F, -0.2182F, 0.0873F));

        ModelPartData arm0_r3 = rightArm.addChild("arm0_r3", ModelPartBuilder.create().uv(40, 101).cuboid(-5.7765F, -3.1022F, -5.0F, 8.0F, 5.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.2728F, 4.2042F, 2.0F, 0.0F, 0.0F, -0.2618F));

        ModelPartData rightArm1 = rightArm.addChild("rightArm1", ModelPartBuilder.create().uv(106, 12).mirrored().cuboid(-1.7385F, 2.9886F, -3.0F, 5.0F, 8.0F, 6.0F, new Dilation(0.1F)).mirrored(false)
                .uv(106, 12).cuboid(-2.7385F, 0.9886F, -3.0F, 5.0F, 8.0F, 6.0F, new Dilation(0.25F)), ModelTransform.of(-3.1516F, 8.9237F, 2.0F, 0.0F, 0.0F, 0.0873F));

        ModelPartData right_fingers = rightArm1.addChild("right_fingers", ModelPartBuilder.create(), ModelTransform.pivot(-0.8484F, 8.0763F, -1.0F));

        ModelPartData arm0_r4 = right_fingers.addChild("arm0_r4", ModelPartBuilder.create().uv(98, 56).cuboid(-7.9886F, -4.0F, -6.2615F, 8.0F, 8.0F, 7.0F, new Dilation(0.1F)), ModelTransform.of(-1.1516F, 1.9237F, 1.0F, 1.5708F, 0.0F, -1.5708F));

        ModelPartData hammer = right_fingers.addChild("hammer", ModelPartBuilder.create(), ModelTransform.pivot(2.0F, 6.0F, -1.0F));

        ModelPartData pick = hammer.addChild("pick", ModelPartBuilder.create().uv(32, 34).cuboid(-3.0F, -10.0F, -4.0F, 6.0F, 6.0F, 7.0F, new Dilation(0.0F))
                .uv(65, 17).cuboid(-4.0F, -18.0F, -5.0F, 8.0F, 8.0F, 9.0F, new Dilation(0.0F))
                .uv(31, 17).cuboid(-4.0F, -4.0F, -5.0F, 8.0F, 8.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 7.0F, -18.0F));

        ModelPartData stick = hammer.addChild("stick", ModelPartBuilder.create().uv(58, 34).cuboid(0.0F, 0.0F, -23.0F, 0.0F, 5.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 11.0F));

        ModelPartData cube_r17 = stick.addChild("cube_r17", ModelPartBuilder.create().uv(31, 17).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r18 = stick.addChild("cube_r18", ModelPartBuilder.create().uv(31, 21).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -2.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r19 = stick.addChild("cube_r19", ModelPartBuilder.create().uv(31, 17).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.001F)), ModelTransform.of(0.0F, 0.0F, -4.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r20 = stick.addChild("cube_r20", ModelPartBuilder.create().uv(31, 21).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -6.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r21 = stick.addChild("cube_r21", ModelPartBuilder.create().uv(31, 17).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.001F)), ModelTransform.of(0.0F, 0.0F, -8.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r22 = stick.addChild("cube_r22", ModelPartBuilder.create().uv(31, 21).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -10.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r23 = stick.addChild("cube_r23", ModelPartBuilder.create().uv(31, 17).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.001F)), ModelTransform.of(0.0F, 0.0F, -12.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r24 = stick.addChild("cube_r24", ModelPartBuilder.create().uv(31, 21).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -14.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r25 = stick.addChild("cube_r25", ModelPartBuilder.create().uv(31, 17).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.001F)), ModelTransform.of(0.0F, 0.0F, -16.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r26 = stick.addChild("cube_r26", ModelPartBuilder.create().uv(31, 21).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -26.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r27 = stick.addChild("cube_r27", ModelPartBuilder.create().uv(31, 21).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -18.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r28 = stick.addChild("cube_r28", ModelPartBuilder.create().uv(31, 17).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.001F)), ModelTransform.of(0.0F, 0.0F, -20.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r29 = stick.addChild("cube_r29", ModelPartBuilder.create().uv(31, 21).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -22.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r30 = stick.addChild("cube_r30", ModelPartBuilder.create().uv(31, 17).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.001F)), ModelTransform.of(0.0F, 0.0F, -24.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData shoulder = rightArm.addChild("shoulder", ModelPartBuilder.create(), ModelTransform.of(-4.2724F, -1.2126F, 2.0F, 0.0F, 0.0F, 0.0873F));

        ModelPartData cube_r31 = shoulder.addChild("cube_r31", ModelPartBuilder.create().uv(8, 30).cuboid(-5.4575F, -0.3201F, -4.3724F, 5.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, -1.0472F));

        ModelPartData cube_r32 = shoulder.addChild("cube_r32", ModelPartBuilder.create().uv(0, 19).cuboid(-5.4575F, -0.2793F, -0.5F, 5.0F, 3.0F, 1.0F, new Dilation(-0.01F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

        ModelPartData cube_r33 = shoulder.addChild("cube_r33", ModelPartBuilder.create().uv(8, 37).cuboid(-5.4575F, -0.3201F, 0.3724F, 5.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, -1.0472F));

        ModelPartData shoulder4 = shoulder.addChild("shoulder4", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        ModelPartData cube_r34 = shoulder4.addChild("cube_r34", ModelPartBuilder.create().uv(8, 30).cuboid(-4.7207F, -4.3992F, -3.4681F, 5.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-6.0F, 1.0F, 0.0F, 0.2182F, 0.0F, -1.0472F));

        ModelPartData cube_r35 = shoulder4.addChild("cube_r35", ModelPartBuilder.create().uv(0, 19).cuboid(-4.7207F, -4.4575F, -0.5F, 5.0F, 3.0F, 1.0F, new Dilation(-0.01F)), ModelTransform.of(-6.0F, 1.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

        ModelPartData cube_r36 = shoulder4.addChild("cube_r36", ModelPartBuilder.create().uv(8, 37).cuboid(-4.7207F, -4.3992F, -0.5319F, 5.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-6.0F, 1.0F, 0.0F, -0.2182F, 0.0F, -1.0472F));

        ModelPartData cube_r37 = shoulder4.addChild("cube_r37", ModelPartBuilder.create().uv(0, 19).cuboid(-5.8978F, -2.7765F, -0.5F, 5.0F, 3.0F, 1.0F, new Dilation(-0.01F)), ModelTransform.of(-3.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        ModelPartData cube_r38 = shoulder4.addChild("cube_r38", ModelPartBuilder.create().uv(8, 37).cuboid(-5.8978F, -2.7581F, -3.8319F, 5.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, -1.0F, 0.0F, 0.2182F, 0.0F, -0.3491F));

        ModelPartData cube_r39 = shoulder4.addChild("cube_r39", ModelPartBuilder.create().uv(8, 37).cuboid(-5.8978F, -2.7581F, -0.1681F, 5.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, -1.0F, 0.0F, -0.2182F, 0.0F, -0.3491F));

        ModelPartData leftArm = root.addChild("leftArm", ModelPartBuilder.create(), ModelTransform.of(9.0F, -16.0F, -2.0F, 0.0F, 0.0F, 0.0F));

        ModelPartData arm1_r1 = leftArm.addChild("arm1_r1", ModelPartBuilder.create().uv(106, 26).cuboid(8.7385F, -30.0114F, -3.0F, 5.0F, 12.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(-6.9725F, 30.8896F, 2.0F, 0.0F, 0.0F, -0.0873F));

        ModelPartData arm1_r2 = leftArm.addChild("arm1_r2", ModelPartBuilder.create().uv(0, 57).cuboid(-3.2553F, -5.0114F, -0.0566F, 5.0F, 10.0F, 3.0F, new Dilation(0.15F)), ModelTransform.of(3.1516F, 8.9237F, 2.0F, 0.0F, 0.2182F, -0.0873F));

        ModelPartData arm1_r3 = leftArm.addChild("arm1_r3", ModelPartBuilder.create().uv(40, 101).cuboid(-1.2235F, -1.1022F, -5.0F, 8.0F, 5.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(-0.4471F, 2.2118F, 2.0F, 0.0F, 0.0F, 0.2618F));

        ModelPartData spikes2 = leftArm.addChild("spikes2", ModelPartBuilder.create().uv(0, 15).cuboid(-0.5F, -1.0114F, -1.2385F, 1.0F, 3.0F, 1.0F, new Dilation(0.51F))
                .uv(0, 15).cuboid(-0.5F, -4.0114F, -1.2385F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 36).cuboid(-1.5F, -6.0114F, -0.7385F, 3.0F, 8.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(6.352F, -0.3907F, 2.0F, 0.0F, -1.5708F, -0.0873F));

        ModelPartData head_r1 = spikes2.addChild("head_r1", ModelPartBuilder.create().uv(6, 36).cuboid(-4.0052F, -6.2914F, -1.2968F, 1.0F, 8.0F, 0.0F, new Dilation(0.0F))
                .uv(0, 30).cuboid(-5.0052F, -3.2914F, -1.7968F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 30).cuboid(-3.0052F, -6.2914F, -1.7968F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 15).cuboid(-2.5052F, -1.2914F, -1.7968F, 1.0F, 3.0F, 1.0F, new Dilation(0.51F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.4363F, -0.4363F));

        ModelPartData head_r2 = spikes2.addChild("head_r2", ModelPartBuilder.create().uv(6, 36).cuboid(3.0052F, -6.2914F, -1.2968F, 1.0F, 8.0F, 0.0F, new Dilation(0.0F))
                .uv(0, 30).cuboid(4.0052F, -3.2914F, -1.7968F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 30).cuboid(2.0052F, -6.2914F, -1.7968F, 1.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 15).cuboid(1.5052F, -1.2914F, -1.7968F, 1.0F, 3.0F, 1.0F, new Dilation(0.51F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.4363F, 0.4363F));

        ModelPartData leftArm1 = leftArm.addChild("leftArm1", ModelPartBuilder.create().uv(106, 12).cuboid(-3.2615F, 2.9886F, -3.0F, 5.0F, 8.0F, 6.0F, new Dilation(0.1F))
                .uv(106, 12).mirrored().cuboid(-2.2615F, 0.9886F, -3.0F, 5.0F, 8.0F, 6.0F, new Dilation(0.25F)).mirrored(false), ModelTransform.of(3.1516F, 8.9237F, 2.0F, 0.0F, 0.0F, -0.0873F));

        ModelPartData left_fingers = leftArm1.addChild("left_fingers", ModelPartBuilder.create(), ModelTransform.pivot(-0.1516F, 8.0763F, 0.0F));

        ModelPartData arm0_r5 = left_fingers.addChild("arm0_r5", ModelPartBuilder.create().uv(98, 56).cuboid(-9.9886F, -4.0F, -4.2615F, 8.0F, 8.0F, 7.0F, new Dilation(0.1F)), ModelTransform.of(0.1516F, -0.0763F, 0.0F, -1.5708F, 0.0F, -1.5708F));

        ModelPartData shield = left_fingers.addChild("shield", ModelPartBuilder.create(), ModelTransform.of(4.0F, 8.0F, -2.0F, -1.5708F, 0.0F, 1.5708F));

        ModelPartData base = shield.addChild("base", ModelPartBuilder.create().uv(44, 48).cuboid(-6.0F, -12.5F, -2.0F, 12.0F, 22.0F, 2.0F, new Dilation(0.0F))
                .uv(72, 48).cuboid(-7.0F, 5.5F, -2.5F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F))
                .uv(72, 56).mirrored().cuboid(-4.0F, 7.5F, -2.5F, 2.0F, 3.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
                .uv(72, 56).cuboid(2.0F, 7.5F, -2.5F, 2.0F, 3.0F, 3.0F, new Dilation(0.0F))
                .uv(72, 56).mirrored().cuboid(-4.0F, -13.5F, -2.5F, 2.0F, 3.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
                .uv(72, 56).cuboid(2.0F, -13.5F, -2.5F, 2.0F, 3.0F, 3.0F, new Dilation(0.0F))
                .uv(84, 48).mirrored().cuboid(4.0F, -13.5F, -2.5F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
                .uv(84, 48).cuboid(-7.0F, -13.5F, -2.5F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F))
                .uv(72, 48).mirrored().cuboid(4.0F, 5.5F, -2.5F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData handle = shield.addChild("handle", ModelPartBuilder.create().uv(72, 62).cuboid(-1.0F, -5.0F, 0.0F, 2.0F, 7.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData shoulder2 = leftArm.addChild("shoulder2", ModelPartBuilder.create(), ModelTransform.of(1.1095F, -2.9435F, 2.0F, 0.0F, 0.0F, -0.0873F));

        ModelPartData cube_r40 = shoulder2.addChild("cube_r40", ModelPartBuilder.create().uv(8, 37).cuboid(0.4575F, -0.3201F, 0.3724F, 5.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, 2.0F, 0.0F, -0.2182F, 0.0F, 1.0472F));

        ModelPartData cube_r41 = shoulder2.addChild("cube_r41", ModelPartBuilder.create().uv(0, 19).cuboid(0.4575F, -0.2793F, -0.5F, 5.0F, 3.0F, 1.0F, new Dilation(-0.01F)), ModelTransform.of(3.0F, 2.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

        ModelPartData cube_r42 = shoulder2.addChild("cube_r42", ModelPartBuilder.create().uv(8, 30).cuboid(0.4575F, -0.3201F, -4.3724F, 5.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, 2.0F, 0.0F, 0.2182F, 0.0F, 1.0472F));

        ModelPartData shoulder3 = shoulder2.addChild("shoulder3", ModelPartBuilder.create(), ModelTransform.of(0.0F, 5.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        ModelPartData cube_r43 = shoulder3.addChild("cube_r43", ModelPartBuilder.create().uv(0, 19).cuboid(0.8978F, -2.7765F, -0.5F, 5.0F, 3.0F, 1.0F, new Dilation(-0.01F)), ModelTransform.of(0.0F, -4.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

        ModelPartData cube_r44 = shoulder3.addChild("cube_r44", ModelPartBuilder.create().uv(8, 37).cuboid(-0.2793F, -4.3992F, -0.5319F, 5.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, -2.0F, 0.0F, -0.2182F, 0.0F, 1.0472F));

        ModelPartData cube_r45 = shoulder3.addChild("cube_r45", ModelPartBuilder.create().uv(0, 19).cuboid(-0.2793F, -4.4575F, -0.5F, 5.0F, 3.0F, 1.0F, new Dilation(-0.01F)), ModelTransform.of(3.0F, -2.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

        ModelPartData cube_r46 = shoulder3.addChild("cube_r46", ModelPartBuilder.create().uv(8, 30).cuboid(-0.2793F, -4.3992F, -3.4681F, 5.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, -2.0F, 0.0F, 0.2182F, 0.0F, 1.0472F));

        ModelPartData cube_r47 = shoulder3.addChild("cube_r47", ModelPartBuilder.create().uv(8, 37).cuboid(0.8978F, -2.7581F, -0.1681F, 5.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, 0.0F, -0.2182F, 0.0F, 0.3491F));

        ModelPartData cube_r48 = shoulder3.addChild("cube_r48", ModelPartBuilder.create().uv(8, 37).cuboid(0.8978F, -2.7581F, -3.8319F, 5.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, 0.0F, 0.2182F, 0.0F, 0.3491F));
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
    public void setAngles(WarriorOfChimericDarkness entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.root.getChild("head").yaw = headYaw / (180F / (float) Math.PI);
        this.root.getChild("head").pitch = headPitch / (180F / (float) Math.PI);
        switch (entity.getWarriorPose()) {
            default -> this.animateMovement(WarriorOfChimericDarknessAnimations.WALK, limbAngle, limbDistance, 3.0f, 3);
            case EROFLAMING ->
                    this.animateMovement(WarriorOfChimericDarknessAnimations.EROFLAMING, limbAngle, limbDistance, 6.0f, 6);
            case SHIELDED_WALK ->
                    this.animateMovement(WarriorOfChimericDarknessAnimations.SHIELDED_WALK, limbAngle, limbDistance, 4.0f, 4);
            case SPINNING ->
                    this.animateMovement(WarriorOfChimericDarknessAnimations.SPIN_ATTACK_WALK, limbAngle, limbDistance, 3.0f, 3);
        }
        this.updateAnimation(entity.spawn, WarriorOfChimericDarknessAnimations.SPAWN, animationProgress, 0.5f);
        this.updateAnimation(entity.death, WarriorOfChimericDarknessAnimations.DEATH, animationProgress, 0.5f);
        this.updateAnimation(entity.dash, WarriorOfChimericDarknessAnimations.DASH, animationProgress, 0.5f);
        this.updateAnimation(entity.hammerAttack, WarriorOfChimericDarknessAnimations.HAMMER_ATTACK, animationProgress);
        this.updateAnimation(entity.shieldStriking, WarriorOfChimericDarknessAnimations.SHIELD_STRIKING, animationProgress);
        this.updateAnimation(entity.heavyHammerAttack, WarriorOfChimericDarknessAnimations.HEAVY_HAMMER_ATTACK, animationProgress);
        if (entity.isInWarriorPose(WarriorOfChimericDarkness.WarriorPose.BLASTING)){
            this.root.getChild("head").yaw += (float) Math.sin(entity.age)/5f;
            this.root.getChild("head").pitch += (float) Math.sin(entity.age)/4f;
            ModelPart rightArm = this.getPart().getChild("rightArm");
            ModelPart leftArm = this.getPart().getChild("leftArm");
            rightArm.pitch = -0.75f - (float) (entity.age % 360)/3f;
            rightArm.roll = 0.25f - (float) (entity.age % 360)/2f;
            leftArm.pitch = -0.05f - (float) Math.sin(entity.age)/4f;
            leftArm.roll = -0.1f;
        }
    }
}