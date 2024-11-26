package net.sashakyotoz.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.custom.HarmonyWatcherEntity;
import net.sashakyotoz.common.entities.animations.HarmonyWatcherAnimations;

public class HarmonyWatcherModel extends SinglePartEntityModel<HarmonyWatcherEntity> implements ModelWithArms {
    public static final EntityModelLayer HARMONY_WATCHER = new EntityModelLayer(UnseenWorld.makeID("harmony_watcher"), "main");

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart eye;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;

    public HarmonyWatcherModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.eye = head.getChild("eye");
        this.body = root.getChild("body");
        this.rightArm = root.getChild("rightArm");
        this.leftArm = root.getChild("leftArm");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(24, 48).cuboid(-4.0F, -1.0F, -4.0F, 8.0F, 1.0F, 5.0F, new Dilation(0.0F))
                .uv(44, 54).cuboid(-4.0F, -6.0F, -4.0F, 1.0F, 5.0F, 5.0F, new Dilation(0.0F))
                .uv(0, 43).cuboid(-4.0F, -6.0F, 1.0F, 8.0F, 6.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 52).cuboid(-4.0F, -10.0F, -4.0F, 8.0F, 4.0F, 8.0F, new Dilation(0.0F))
                .uv(32, 54).cuboid(3.0F, -6.0F, -4.0F, 1.0F, 5.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 2.0F, 0.0F));

        ModelPartData cube_r1 = head.addChild("cube_r1", ModelPartBuilder.create().uv(0, 38).cuboid(-4.0F, -5.0F, 0.0F, 8.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -6.0F, -4.0F, 0.2618F, 0.0F, 0.0F));

        ModelPartData eye = head.addChild("eye", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.5F, -3.5F, -2.5F));

        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(86, 47).cuboid(-6.0F, -2.0F, -4.0F, 12.0F, 8.0F, 9.0F, new Dilation(0.0F))
                .uv(66, 44).cuboid(-6.0F, 4.0F, -5.0F, 12.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(66, 41).cuboid(-6.0F, -2.0F, -5.0F, 12.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 27).cuboid(-6.0F, 0.0F, -5.0F, 4.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 27).mirrored().cuboid(2.0F, 0.0F, -5.0F, 4.0F, 4.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 6.0F, 0.0F));

        ModelPartData middleStone = body.addChild("middleStone", ModelPartBuilder.create().uv(92, 36).cuboid(-5.0F, -2.0F, -4.0F, 10.0F, 3.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 11.0F, 0.0F));

        ModelPartData bottomStone = body.addChild("bottomStone", ModelPartBuilder.create().uv(104, 29).cuboid(-3.0F, -0.5F, -3.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 15.5F, 0.0F));

        ModelPartData rightArm = modelPartData.addChild("rightArm", ModelPartBuilder.create().uv(96, 0).mirrored().cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)).mirrored(false)
                .uv(116, 0).mirrored().cuboid(-8.0F, -4.0F, 0.0F, 6.0F, 12.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-10.0F, 8.0F, 0.0F));

        ModelPartData leftArm = modelPartData.addChild("leftArm", ModelPartBuilder.create().uv(96, 0).cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(116, 0).cuboid(2.0F, -4.0F, 0.0F, 6.0F, 12.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(10.0F, 8.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 64);
    }

    @Override
    public void setAngles(HarmonyWatcherEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.eye.yaw = netHeadYaw * (float) (Math.PI / 180.0);
        this.animateMovement(entity.isAngry ? HarmonyWatcherAnimations.WALKWHENANGRY : HarmonyWatcherAnimations.WALK,limbSwing,limbSwingAmount,3.0f,3.0f);
        this.updateAnimation(entity.death,HarmonyWatcherAnimations.DEATH,ageInTicks);
        this.updateAnimation(entity.fertilize,HarmonyWatcherAnimations.FERTILIZE,ageInTicks);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        rightArm.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        leftArm.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void setArmAngle(Arm arm, MatrixStack matrices) {
        matrices.translate(0.0F, 0.0625F, 0.1875F);
        matrices.multiply(RotationAxis.POSITIVE_X.rotation(this.leftArm.pitch));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(this.leftArm.yaw));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotation(this.leftArm.roll));
        matrices.scale(0.7F, 0.7F, 0.7F);
    }
}