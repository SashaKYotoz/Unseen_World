package net.sashakyotoz.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.animations.VexalBeetleAnimations;
import net.sashakyotoz.common.entities.custom.VexalBeetleEntity;

public class VexalBeetleModel extends SinglePartEntityModel<VexalBeetleEntity> {
    public static final EntityModelLayer VEXAL_BEETLE = new EntityModelLayer(UnseenWorld.makeID("vexal_beetle"), "main");

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart left_claw;
    private final ModelPart right_claw;
    private final ModelPart body;
    private final ModelPart left_wing;
    private final ModelPart wing1;
    private final ModelPart right_wing;
    private final ModelPart wing;
    private final ModelPart right_leg_b;
    private final ModelPart left_leg_b;
    private final ModelPart right_leg_f;
    private final ModelPart left_leg_f;

    public VexalBeetleModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.left_claw = this.head.getChild("left_claw");
        this.right_claw = this.head.getChild("right_claw");
        this.body = root.getChild("body");
        this.left_wing = this.body.getChild("left_wing");
        this.wing1 = this.left_wing.getChild("wing1");
        this.right_wing = this.body.getChild("right_wing");
        this.wing = this.right_wing.getChild("wing");
        this.right_leg_b = root.getChild("right_leg_b");
        this.left_leg_b = root.getChild("left_leg_b");
        this.right_leg_f = root.getChild("right_leg_f");
        this.left_leg_f = root.getChild("left_leg_f");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(32, 4).cuboid(-4.0F, -3.0F, -8.0F, 8.0F, 6.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 17.0F, -3.0F));

        ModelPartData left_claw = head.addChild("left_claw", ModelPartBuilder.create().uv(-8, 29).mirrored().cuboid(-4.0F, 0.0F, -8.0F, 6.0F, 0.0F, 8.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(4.0F, 1.0F, -7.0F));

        ModelPartData right_claw = head.addChild("right_claw", ModelPartBuilder.create().uv(-8, 29).cuboid(-2.0F, 0.0F, -8.0F, 6.0F, 0.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.0F, 1.0F, -7.0F));

        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new Dilation(0.0F))
                .uv(0, 12).cuboid(-5.0F, -3.0F, 3.0F, 10.0F, 5.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 17.0F, 0.0F));

        ModelPartData left_wing = body.addChild("left_wing", ModelPartBuilder.create().uv(0, 52).cuboid(-1.0F, -4.0F, -3.0F, 15.0F, 6.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, -2.0F, 0.0F, 0.1309F, -0.3054F, -0.0873F));

        ModelPartData wing1 = left_wing.addChild("wing1", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, -2.0F, -2.0F));

        ModelPartData cube_r1 = wing1.addChild("cube_r1", ModelPartBuilder.create().uv(-7, 45).cuboid(-1.0F, -4.0F, -1.0F, 15.0F, 0.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 4.0F, 0.0F, 0.0F, -0.6109F, 0.0F));

        ModelPartData right_wing = body.addChild("right_wing", ModelPartBuilder.create().uv(0, 52).mirrored().cuboid(-14.0F, -4.0F, -3.0F, 15.0F, 6.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-3.0F, -2.0F, 0.0F, 0.1309F, 0.3054F, 0.0873F));

        ModelPartData wing = right_wing.addChild("wing", ModelPartBuilder.create(), ModelTransform.pivot(-1.0F, -1.0F, -2.0F));

        ModelPartData cube_r2 = wing.addChild("cube_r2", ModelPartBuilder.create().uv(-7, 45).mirrored().cuboid(-14.0F, -4.0F, -1.0F, 15.0F, 0.0F, 7.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-1.0F, 3.0F, 0.0F, 0.0F, 0.6109F, 0.0F));

        ModelPartData right_leg_b = modelPartData.addChild("right_leg_b", ModelPartBuilder.create().uv(34, 29).cuboid(-11.0F, -1.0F, -2.0F, 12.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-4.0F, 17.0F, 2.0F, 0.0F, 0.5236F, -0.6981F));

        ModelPartData left_leg_b = modelPartData.addChild("left_leg_b", ModelPartBuilder.create().uv(34, 29).mirrored().cuboid(-1.0F, -1.0F, -2.0F, 12.0F, 2.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(4.0F, 17.0F, 2.0F, 0.0F, -0.5236F, 0.6981F));

        ModelPartData right_leg_f = modelPartData.addChild("right_leg_f", ModelPartBuilder.create().uv(34, 29).cuboid(-11.0F, -1.0F, -1.0F, 12.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-4.0F, 17.0F, -1.0F, 0.0F, -0.5236F, -0.6981F));

        ModelPartData left_leg_f = modelPartData.addChild("left_leg_f", ModelPartBuilder.create().uv(34, 29).mirrored().cuboid(-1.0F, -1.0F, -1.0F, 12.0F, 2.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(4.0F, 17.0F, -1.0F, 0.0F, 0.5236F, 0.6981F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(VexalBeetleEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);

        if (entity.getTarget() != null) {
            this.left_claw.yaw = -0.5f * MathHelper.sin(limbSwing);
            this.right_claw.yaw = 0.5f * MathHelper.sin(limbSwing);
        }
        if (entity.isOnGround()) {
            float i = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.4F) * limbSwingAmount;
            float l = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + (float) (Math.PI * 3.0 / 2.0)) * 0.4F) * limbSwingAmount;
            float m = Math.abs(MathHelper.sin(limbSwing * 0.6662F + 0.0F) * 0.4F) * limbSwingAmount;
            float p = Math.abs(MathHelper.sin(limbSwing * 0.6662F + (float) (Math.PI * 3.0 / 2.0)) * 0.4F) * limbSwingAmount;
            this.right_leg_b.yaw += i;
            this.left_leg_b.yaw += -i;
            this.right_leg_f.yaw += l;
            this.left_leg_f.yaw += -l;
            this.right_leg_b.roll += m;
            this.left_leg_b.roll += -m;
            this.right_leg_f.roll += p;
            this.left_leg_f.roll += -p;
        }

        float f = Math.min((float) entity.getVelocity().lengthSquared() * 200.0F, 2F);

        this.updateAnimation(entity.flying, VexalBeetleAnimations.FLYING, ageInTicks, f);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        right_leg_b.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        left_leg_b.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        right_leg_f.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        left_leg_f.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }
}