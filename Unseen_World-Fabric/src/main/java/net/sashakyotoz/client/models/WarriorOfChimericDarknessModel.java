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
    private final ModelPart head;

    public WarriorOfChimericDarknessModel(ModelPart root) {
        super(RenderLayer::getEntityTranslucent);
        this.root = root;
        ModelPart body = this.root.getChild("body");
        ModelPart chest = body.getChild("chest");
        this.head = chest.getChild("head");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(34, 0).cuboid(-6.5F, -5.0F, -3.0F, 13.0F, 7.0F, 6.0F, new Dilation(0.0F))
                .uv(34, 13).cuboid(-6.5F, -5.0F, -3.0F, 13.0F, 4.0F, 6.0F, new Dilation(0.1F)), ModelTransform.pivot(0.0F, 10.0F, 0.0F));

        ModelPartData chest = body.addChild("chest", ModelPartBuilder.create().uv(0, 23).cuboid(-12.5F, -15.0F, -5.5F, 25.0F, 16.0F, 11.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -6.0F, 0.0F));

        ModelPartData head = chest.addChild("head", ModelPartBuilder.create().uv(13, 0).cuboid(-1.9F, -12.0F, -5.6F, 4.0F, 5.0F, 3.0F, new Dilation(0.0F))
                .uv(122, 0).cuboid(-2.9F, -14.0F, -4.6F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F))
                .uv(122, 0).mirrored().cuboid(0.1F, -14.0F, -4.6F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, -15.0F, 0.0F));

        ModelPartData cube_r1 = head.addChild("cube_r1", ModelPartBuilder.create().uv(100, 0).cuboid(-4.0941F, -3.0F, -4.0F, 7.0F, 3.0F, 7.0F, new Dilation(0.325F))
                .uv(100, 10).cuboid(-4.0941F, 0.5F, -4.0F, 7.0F, 10.0F, 7.0F, new Dilation(0.325F))
                .uv(72, 0).cuboid(-4.0941F, 0.5F, -4.0F, 7.0F, 10.0F, 7.0F, new Dilation(0.25F)), ModelTransform.of(0.0941F, -10.5F, 0.641F, 0.0F, -0.7854F, 0.0F));

        ModelPartData right_arm = chest.addChild("right_arm", ModelPartBuilder.create().uv(28, 50).cuboid(-7.5F, 4.0F, -3.5F, 6.0F, 15.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(-11.0F, -14.0F, 0.0F));

        ModelPartData cube_r2 = right_arm.addChild("cube_r2", ModelPartBuilder.create().uv(74, 52).cuboid(-9.0F, -5.0F, -7.0F, 13.0F, 11.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

        ModelPartData hammer = right_arm.addChild("hammer", ModelPartBuilder.create(), ModelTransform.pivot(-4.5F, 11.0F, -5.0F));

        ModelPartData pick = hammer.addChild("pick", ModelPartBuilder.create().uv(1, 115).cuboid(-3.0F, -10.0F, -4.0F, 6.0F, 6.0F, 7.0F, new Dilation(0.0F))
                .uv(0, 98).mirrored().cuboid(-4.0F, -18.0F, -5.0F, 8.0F, 8.0F, 9.0F, new Dilation(0.0F)).mirrored(false)
                .uv(0, 98).mirrored().cuboid(-4.0F, -4.0F, -5.0F, 8.0F, 8.0F, 9.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 15.0F, -18.0F));

        ModelPartData stick = hammer.addChild("stick", ModelPartBuilder.create().uv(27, 115).cuboid(0.0F, 0.0F, -23.0F, 0.0F, 5.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 8.0F, 11.0F));

        ModelPartData cube_r3 = stick.addChild("cube_r3", ModelPartBuilder.create().uv(25, 98).cuboid(-0.5F, -0.95F, -1.05F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -16.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r4 = stick.addChild("cube_r4", ModelPartBuilder.create().uv(31, 98).cuboid(-0.5F, -0.85F, -1.15F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -17.25F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r5 = stick.addChild("cube_r5", ModelPartBuilder.create().uv(0, 115).cuboid(0.0F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, 0.0F, -20.25F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r6 = stick.addChild("cube_r6", ModelPartBuilder.create().uv(0, 98).cuboid(-0.5F, -0.925F, -1.075F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -18.75F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r7 = stick.addChild("cube_r7", ModelPartBuilder.create().uv(0, 98).cuboid(-0.5F, -0.95F, -1.05F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -21.5F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r8 = stick.addChild("cube_r8", ModelPartBuilder.create().uv(0, 98).cuboid(-0.5F, -0.925F, -1.075F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -24.25F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r9 = stick.addChild("cube_r9", ModelPartBuilder.create().uv(0, 115).cuboid(-0.5F, -0.85F, -1.15F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -14.5F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r10 = stick.addChild("cube_r10", ModelPartBuilder.create().uv(0, 98).cuboid(-0.5F, -0.95F, -1.05F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -13.25F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r11 = stick.addChild("cube_r11", ModelPartBuilder.create().uv(0, 115).cuboid(0.0F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, 0.0F, -12.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r12 = stick.addChild("cube_r12", ModelPartBuilder.create().uv(0, 98).cuboid(-0.5F, -0.925F, -1.075F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -10.5F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r13 = stick.addChild("cube_r13", ModelPartBuilder.create().uv(0, 115).cuboid(-0.5F, -0.85F, -1.15F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -9.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r14 = stick.addChild("cube_r14", ModelPartBuilder.create().uv(0, 98).cuboid(-0.5F, -0.95F, -1.05F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.5F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r15 = stick.addChild("cube_r15", ModelPartBuilder.create().uv(0, 115).cuboid(-0.5F, -0.85F, -1.15F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -0.75F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r16 = stick.addChild("cube_r16", ModelPartBuilder.create().uv(0, 98).cuboid(-0.5F, -0.95F, -1.05F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -2.25F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r17 = stick.addChild("cube_r17", ModelPartBuilder.create().uv(0, 115).cuboid(-0.5F, -0.85F, -1.15F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -3.5F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r18 = stick.addChild("cube_r18", ModelPartBuilder.create().uv(0, 98).cuboid(-0.5F, -0.925F, -1.075F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -5.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r19 = stick.addChild("cube_r19", ModelPartBuilder.create().uv(0, 115).cuboid(0.0F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, 0.0F, -6.5F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r20 = stick.addChild("cube_r20", ModelPartBuilder.create().uv(0, 98).cuboid(-0.5F, -0.95F, -1.05F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -7.75F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r21 = stick.addChild("cube_r21", ModelPartBuilder.create().uv(0, 115).cuboid(-0.5F, -0.85F, -1.15F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -22.75F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r22 = stick.addChild("cube_r22", ModelPartBuilder.create().uv(0, 102).cuboid(0.0F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, 0.0F, -25.75F, -0.7854F, 0.0F, 0.0F));

        ModelPartData left_arm = chest.addChild("left_arm", ModelPartBuilder.create().uv(28, 50).mirrored().cuboid(1.5F, 4.0F, -3.5F, 6.0F, 15.0F, 7.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(11.0F, -14.0F, 0.0F));

        ModelPartData cube_r23 = left_arm.addChild("cube_r23", ModelPartBuilder.create().uv(88, 19).cuboid(1.5F, -6.0F, -1.0F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(3.25F, -4.0F, 4.75F, -0.2618F, 0.0F, -0.0873F));

        ModelPartData cube_r24 = left_arm.addChild("cube_r24", ModelPartBuilder.create().uv(88, 19).mirrored().cuboid(-1.5F, -4.25F, -1.75F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(7.0F, -5.0F, 2.0F, 0.0F, 0.0F, 0.2618F));

        ModelPartData cube_r25 = left_arm.addChild("cube_r25", ModelPartBuilder.create().uv(88, 19).mirrored().cuboid(-1.5F, -4.25F, -1.75F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
                .uv(88, 19).cuboid(-1.5F, -4.25F, -8.75F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, -6.0F, 4.0F, 0.0F, 0.0F, -0.2618F));

        ModelPartData cube_r26 = left_arm.addChild("cube_r26", ModelPartBuilder.create().uv(88, 19).mirrored().cuboid(1.5F, -6.0F, -2.0F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(2.25F, -4.0F, -4.75F, 0.2618F, 0.0F, -0.0873F));

        ModelPartData cube_r27 = left_arm.addChild("cube_r27", ModelPartBuilder.create().uv(72, 17).mirrored().cuboid(0.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(2.0F, -12.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

        ModelPartData cube_r28 = left_arm.addChild("cube_r28", ModelPartBuilder.create().uv(74, 27).mirrored().cuboid(-4.0F, -5.0F, -7.0F, 13.0F, 11.0F, 14.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

        ModelPartData shield = left_arm.addChild("shield", ModelPartBuilder.create().uv(54, 50).mirrored().cuboid(1.0F, 9.0F, -5.0F, 6.0F, 3.0F, 8.0F, new Dilation(0.0F)).mirrored(false)
                .uv(68, 82).cuboid(7.0F, 2.0F, -14.0F, 2.0F, 18.0F, 28.0F, new Dilation(0.0F)), ModelTransform.pivot(1.5F, 6.0F, 1.0F));

        ModelPartData cube_r29 = shield.addChild("cube_r29", ModelPartBuilder.create().uv(102, 104).mirrored().cuboid(-2.0F, -1.5F, 0.0F, 2.0F, 3.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
                .uv(116, 102).cuboid(-5.0F, -18.5F, 0.0F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F))
                .uv(102, 104).mirrored().cuboid(-2.0F, -18.5F, 0.0F, 2.0F, 3.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
                .uv(116, 102).cuboid(-5.0F, -3.5F, 0.0F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(9.5F, 19.5F, -10.0F, 0.0F, -1.5708F, 0.0F));

        ModelPartData cube_r30 = shield.addChild("cube_r30", ModelPartBuilder.create().uv(116, 102).mirrored().cuboid(-5.0F, -1.5F, 0.0F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
                .uv(116, 102).mirrored().cuboid(-5.0F, -16.5F, 0.0F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(9.5F, 17.5F, 17.0F, 0.0F, -1.5708F, 0.0F));

        ModelPartData cube_r31 = shield.addChild("cube_r31", ModelPartBuilder.create().uv(102, 104).cuboid(-2.0F, -1.5F, 0.0F, 2.0F, 3.0F, 3.0F, new Dilation(0.0F))
                .uv(102, 104).cuboid(-2.0F, -18.5F, 0.0F, 2.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(9.5F, 19.5F, 12.0F, 0.0F, -1.5708F, 0.0F));

        ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(0, 50).cuboid(-4.0F, -2.0F, -3.5F, 7.0F, 16.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.5F, 10.0F, 0.0F));

        ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(0, 50).mirrored().cuboid(-3.0F, -2.0F, -3.5F, 7.0F, 16.0F, 7.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(5.5F, 10.0F, 0.0F));
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
        this.head.yaw = headYaw / (180F / (float) Math.PI);
        this.head.pitch = headPitch / (180F / (float) Math.PI);
    }
}