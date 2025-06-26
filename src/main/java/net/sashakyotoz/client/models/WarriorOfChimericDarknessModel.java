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

        ModelPartData head = root.addChild("head", ModelPartBuilder.create().uv(114, 114).cuboid(-1.9F, -12.0F, -5.6F, 4.0F, 5.0F, 3.0F, new Dilation(0.0F))
                .uv(22, 101).cuboid(-2.9F, -14.0F, -4.6F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F))
                .uv(22, 106).cuboid(0.1F, -14.0F, -4.6F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -17.35F, 0.0F));

        ModelPartData cube_r1 = head.addChild("cube_r1", ModelPartBuilder.create().uv(104, 26).cuboid(-4.0941F, -3.0F, -4.0F, 7.0F, 3.0F, 7.0F, new Dilation(0.325F))
                .uv(28, 94).cuboid(-4.0941F, 0.5F, -4.0F, 7.0F, 10.0F, 7.0F, new Dilation(0.325F))
                .uv(0, 84).cuboid(-4.0941F, 0.5F, -4.0F, 7.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0941F, -10.5F, 0.641F, 0.0F, -0.7854F, 0.0F));

        ModelPartData body = root.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-11.5F, -5.0F, -7.0F, 23.0F, 14.0F, 14.0F, new Dilation(0.0F))
                .uv(74, 0).cuboid(-6.5F, 9.0F, -3.5F, 13.0F, 6.0F, 7.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, -12.0F, 0.0F));

        ModelPartData crystal = body.addChild("crystal", ModelPartBuilder.create().uv(108, 78).cuboid(-2.0F, -8.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-6.0F, -1.0F, -4.5F, 0.2986F, 0.0651F, -0.2084F));

        ModelPartData cube_r2 = crystal.addChild("cube_r2", ModelPartBuilder.create().uv(28, 124).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData cube_r3 = crystal.addChild("cube_r3", ModelPartBuilder.create().uv(124, 9).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData crystal1 = body.addChild("crystal1", ModelPartBuilder.create().uv(108, 90).cuboid(-2.0F, -7.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-6.0F, -4.0F, 4.5F, -0.2618F, 0.0F, -0.0873F));

        ModelPartData cube_r4 = crystal1.addChild("cube_r4", ModelPartBuilder.create().uv(44, 124).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -7.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData cube_r5 = crystal1.addChild("cube_r5", ModelPartBuilder.create().uv(36, 124).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -7.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData rightLeg = root.addChild("rightLeg", ModelPartBuilder.create(), ModelTransform.pivot(-3.0F, 0.0F, 1.0F));

        ModelPartData rightFoot = rightLeg.addChild("rightFoot", ModelPartBuilder.create().uv(40, 53).cuboid(-5.0F, -8.0F, -2.0F, 8.0F, 16.0F, 8.0F, new Dilation(0.01F)), ModelTransform.pivot(-1.0F, 8.0F, -3.0F));

        ModelPartData leftLeg = root.addChild("leftLeg", ModelPartBuilder.create(), ModelTransform.pivot(3.0F, 0.0F, 1.0F));

        ModelPartData leftFoot = leftLeg.addChild("leftFoot", ModelPartBuilder.create().uv(72, 53).cuboid(-3.0F, -8.0F, -2.0F, 8.0F, 16.0F, 8.0F, new Dilation(0.01F)), ModelTransform.pivot(1.0F, 8.0F, -3.0F));

        ModelPartData rightArm = root.addChild("rightArm", ModelPartBuilder.create().uv(86, 94).cuboid(-5.016F, 0.8782F, -1.0F, 5.0F, 12.0F, 6.0F, new Dilation(1.0F))
                .uv(0, 28).cuboid(-7.0F, -4.0F, -5.0F, 12.0F, 11.0F, 14.0F, new Dilation(0.01F)), ModelTransform.pivot(-13.0F, -17.0F, -2.0F));

        ModelPartData rightArm1 = rightArm.addChild("rightArm1", ModelPartBuilder.create().uv(104, 36).cuboid(-1.7385F, 0.9886F, -3.0F, 5.0F, 8.0F, 6.0F, new Dilation(0.7F)), ModelTransform.pivot(-3.1516F, 8.9237F, 2.0F));

        ModelPartData right_fingers = rightArm1.addChild("right_fingers", ModelPartBuilder.create(), ModelTransform.pivot(-0.8484F, 8.0763F, -1.0F));

        ModelPartData arm0_r1 = right_fingers.addChild("arm0_r1", ModelPartBuilder.create().uv(74, 13).cuboid(-7.8484F, -3.9237F, -6.25F, 8.0F, 8.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(-1.1516F, 1.9237F, 1.0F, 1.5708F, 0.0F, -1.5708F));

        ModelPartData hammer = right_fingers.addChild("hammer", ModelPartBuilder.create().uv(74, 77).cuboid(-4.0F, 3.0F, -23.0F, 8.0F, 8.0F, 9.0F, new Dilation(0.0F))
                .uv(40, 77).cuboid(-4.0F, -11.0F, -23.0F, 8.0F, 8.0F, 9.0F, new Dilation(0.0F))
                .uv(104, 13).cuboid(-3.0F, -3.0F, -22.0F, 6.0F, 6.0F, 7.0F, new Dilation(0.0F))
                .uv(108, 102).cuboid(0.0F, 0.0F, -12.0F, 0.0F, 5.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 6.0F, -1.0F));

        ModelPartData cube_r6 = hammer.addChild("cube_r6", ModelPartBuilder.create().uv(8, 129).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.001F)), ModelTransform.of(0.0F, 0.0F, -13.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r7 = hammer.addChild("cube_r7", ModelPartBuilder.create().uv(0, 128).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -11.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r8 = hammer.addChild("cube_r8", ModelPartBuilder.create().uv(8, 129).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.001F)), ModelTransform.of(0.0F, 0.0F, -9.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r9 = hammer.addChild("cube_r9", ModelPartBuilder.create().uv(0, 128).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -7.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r10 = hammer.addChild("cube_r10", ModelPartBuilder.create().uv(0, 128).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -15.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r11 = hammer.addChild("cube_r11", ModelPartBuilder.create().uv(8, 129).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.001F)), ModelTransform.of(0.0F, 0.0F, -5.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r12 = hammer.addChild("cube_r12", ModelPartBuilder.create().uv(0, 128).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -3.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r13 = hammer.addChild("cube_r13", ModelPartBuilder.create().uv(0, 128).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.001F)), ModelTransform.of(0.0F, 0.0F, -1.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r14 = hammer.addChild("cube_r14", ModelPartBuilder.create().uv(8, 129).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 1.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r15 = hammer.addChild("cube_r15", ModelPartBuilder.create().uv(0, 128).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.001F)), ModelTransform.of(0.0F, 0.0F, 3.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r16 = hammer.addChild("cube_r16", ModelPartBuilder.create().uv(8, 129).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 5.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r17 = hammer.addChild("cube_r17", ModelPartBuilder.create().uv(0, 128).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.001F)), ModelTransform.of(0.0F, 0.0F, 7.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r18 = hammer.addChild("cube_r18", ModelPartBuilder.create().uv(8, 129).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 9.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r19 = hammer.addChild("cube_r19", ModelPartBuilder.create().uv(0, 132).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.001F)), ModelTransform.of(0.0F, 0.0F, 11.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData leftArm = root.addChild("leftArm", ModelPartBuilder.create().uv(0, 101).cuboid(0.016F, 0.8782F, -1.0F, 5.0F, 12.0F, 6.0F, new Dilation(1.0F))
                .uv(52, 28).cuboid(-5.0F, -4.0F, -5.0F, 12.0F, 11.0F, 14.0F, new Dilation(0.01F)), ModelTransform.pivot(13.0F, -17.0F, -2.0F));

        ModelPartData leftArm1 = leftArm.addChild("leftArm1", ModelPartBuilder.create().uv(104, 50).cuboid(-3.2615F, 0.9886F, -3.0F, 5.0F, 8.0F, 6.0F, new Dilation(0.7F)), ModelTransform.pivot(3.1516F, 8.9237F, 2.0F));

        ModelPartData left_fingers = leftArm1.addChild("left_fingers", ModelPartBuilder.create(), ModelTransform.pivot(-0.1516F, 8.0763F, 0.0F));
        ModelPartData arm0_r2 = left_fingers.addChild("arm0_r2", ModelPartBuilder.create().uv(56, 94).cuboid(-0.1516F, -3.9237F, -6.25F, 8.0F, 8.0F, 7.0F, new Dilation(0.125F)), ModelTransform.of(2.1516F, 1.9237F, 0.0F, 1.5708F, 0.0F, 1.5708F));
        ModelPartData shield = left_fingers.addChild("shield", ModelPartBuilder.create(), ModelTransform.of(4.0F, 8.0F, -2.0F, -1.5708F, 0.0F, 1.5708F));
        ModelPartData base = shield.addChild("base", ModelPartBuilder.create().uv(0, 53).cuboid(-11.0F, -15.5F, -3.0F, 17.0F, 28.0F, 3.0F, new Dilation(0.0F))
                .uv(72, 109).cuboid(-12.0F, 8.5F, -3.5F, 3.0F, 5.0F, 4.0F, new Dilation(0.0F))
                .uv(28, 84).cuboid(-9.0F, 10.5F, -3.5F, 2.0F, 3.0F, 4.0F, new Dilation(0.0F))
                .uv(54, 118).cuboid(2.0F, 10.5F, -3.5F, 2.0F, 3.0F, 4.0F, new Dilation(0.0F))
                .uv(66, 118).cuboid(-9.0F, -16.5F, -3.5F, 2.0F, 3.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 119).cuboid(2.0F, -16.5F, -3.5F, 2.0F, 3.0F, 4.0F, new Dilation(0.0F))
                .uv(86, 112).cuboid(4.0F, -16.5F, -3.5F, 3.0F, 5.0F, 4.0F, new Dilation(0.0F))
                .uv(114, 0).cuboid(-12.0F, -16.5F, -3.5F, 3.0F, 5.0F, 4.0F, new Dilation(0.0F))
                .uv(100, 114).cuboid(4.0F, 8.5F, -3.5F, 3.0F, 5.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        ModelPartData handle = shield.addChild("handle", ModelPartBuilder.create().uv(108, 64).cuboid(-3.0F, -5.0F, 0.0F, 2.0F, 7.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        ModelPartData crystal2 = leftArm.addChild("crystal2", ModelPartBuilder.create().uv(56, 109).cuboid(-2.0F, -9.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, 0.0F, 1.5F, 0.1745F, 0.0F, -0.1309F));
        ModelPartData cube_r20 = crystal2.addChild("cube_r20", ModelPartBuilder.create().uv(114, 9).cuboid(-3.0F, -3.0F, 0.0F, 5.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, -9.0F, -0.5F, 0.0F, 0.7854F, 0.0F));
        ModelPartData cube_r21 = crystal2.addChild("cube_r21", ModelPartBuilder.create().uv(28, 91).cuboid(-3.0F, -3.0F, 0.0F, 5.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, -9.0F, 0.5F, 0.0F, -0.7854F, 0.0F));
        ModelPartData crystal3 = crystal2.addChild("crystal3", ModelPartBuilder.create().uv(22, 111).cuboid(-2.0F, -8.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(1.5F, 1.0F, 3.0F, 0.1745F, -0.2618F, 0.3491F));
        ModelPartData cube_r22 = crystal3.addChild("cube_r22", ModelPartBuilder.create().uv(124, 81).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        ModelPartData cube_r23 = crystal3.addChild("cube_r23", ModelPartBuilder.create().uv(124, 78).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        ModelPartData crystal4 = leftArm.addChild("crystal4", ModelPartBuilder.create().uv(38, 111).cuboid(-2.0F, -8.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, 0.0F, 4.5F, -0.2618F, 0.2618F, -0.2182F));
        ModelPartData cube_r24 = crystal4.addChild("cube_r24", ModelPartBuilder.create().uv(22, 120).cuboid(-3.0F, -3.0F, 0.0F, 5.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, -8.0F, -0.5F, 0.0F, 0.7854F, 0.0F));
        ModelPartData cube_r25 = crystal4.addChild("cube_r25", ModelPartBuilder.create().uv(12, 119).cuboid(-3.0F, -3.0F, 0.0F, 5.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, -8.0F, 0.5F, 0.0F, -0.7854F, 0.0F));
        return TexturedModelData.of(modelData, 256, 256);
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
            case EROFLAMING ->
                    this.animateMovement(WarriorOfChimericDarknessAnimations.EROFLAMING, limbAngle, limbDistance, 6.0f, 6);
            case SHIELDED_WALK ->
                    this.animateMovement(WarriorOfChimericDarknessAnimations.SHIELDED_WALK, limbAngle, limbDistance, 4.0f, 4);
            case SPINNING ->
                    this.animateMovement(WarriorOfChimericDarknessAnimations.SPIN_ATTACK_WALK, limbAngle, limbDistance, 3.0f, 3);
            default -> this.animateMovement(WarriorOfChimericDarknessAnimations.WALK, limbAngle, limbDistance, 3.0f, 3);
        }
        this.updateAnimation(entity.spawn, WarriorOfChimericDarknessAnimations.SPAWN, animationProgress, 0.5f);
        this.updateAnimation(entity.death, WarriorOfChimericDarknessAnimations.DEATH, animationProgress, 0.5f);
        this.updateAnimation(entity.dash, WarriorOfChimericDarknessAnimations.DASH, animationProgress, 0.5f);
        this.updateAnimation(entity.hammerAttack, WarriorOfChimericDarknessAnimations.HAMMER_ATTACK, animationProgress);
        this.updateAnimation(entity.shieldStriking, WarriorOfChimericDarknessAnimations.SHIELD_STRIKING, animationProgress);
        this.updateAnimation(entity.heavyHammerAttack, WarriorOfChimericDarknessAnimations.HEAVY_HAMMER_ATTACK, animationProgress);
        if (entity.isInWarriorPose(WarriorOfChimericDarkness.WarriorPose.BLASTING)) {
            this.root.getChild("head").yaw += (float) Math.sin(entity.age) / 5f;
            this.root.getChild("head").pitch += (float) Math.sin(entity.age) / 4f;
            ModelPart rightArm = this.getPart().getChild("rightArm");
            ModelPart leftArm = this.getPart().getChild("leftArm");
            rightArm.pitch = -0.75f - (float) (entity.age % 360) / 3f;
            rightArm.roll = 0.25f - (float) (entity.age % 360) / 2f;
            leftArm.pitch = -0.05f - (float) Math.sin(entity.age) / 4f;
            leftArm.roll = -0.1f;
        }
    }
}