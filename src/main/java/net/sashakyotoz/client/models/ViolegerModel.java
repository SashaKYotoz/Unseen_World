package net.sashakyotoz.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.custom.ViolegerEntity;

public class ViolegerModel extends BipedEntityModel<ViolegerEntity> {
    public static final EntityModelLayer VIOLEGER = new EntityModelLayer(UnseenWorld.makeID("violeger_model"), "main");
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    public final ModelPart arms;
    public final ModelPart right_arm;
    public final ModelPart left_arm;
    private final ModelPart right_leg;
    private final ModelPart left_leg;

    public ViolegerModel(ModelPart root) {
        super(root, RenderLayer::getEntityTranslucent);
        this.root = root;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.arms = root.getChild("arms");
        this.right_arm = root.getChild("right_arm");
        this.left_arm = root.getChild("left_arm");
        this.right_leg = root.getChild("right_leg");
        this.left_leg = root.getChild("left_leg");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(96, 0).cuboid(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new Dilation(0.0F))
                .uv(96, 18).cuboid(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new Dilation(0.25F))
                .uv(120, 0).cuboid(-1.0F, -3.0F, -6.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        ModelPartData hat = modelPartData.addChild("hat", new ModelPartBuilder(), ModelTransform.NONE);

        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.3F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData arms = modelPartData.addChild("arms", ModelPartBuilder.create().uv(104, 56).cuboid(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, new Dilation(0.0F))
                .uv(108, 40).cuboid(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F))
                .uv(108, 40).mirrored().cuboid(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 2.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData right_arm = modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(40, 32).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F))
                .uv(64, 9).cuboid(-9.0F, -2.0F, 0.0F, 8.0F, 9.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));

        ModelPartData left_arm = modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(64, 0).cuboid(0.0F, -2.0F, 0.0F, 8.0F, 9.0F, 0.0F, new Dilation(0.0F))
                .uv(48, 48).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(5.0F, 2.0F, 0.0F));

        ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));

        ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(16, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(1.9F, 12.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 64);
    }

    @Override
    public void setAngles(ViolegerEntity livingEntity, float f, float g, float h, float i, float j) {
        this.root.pitch -= Math.abs(MathHelper.cos(f * 0.5F) * 0.9F);
        super.setAngles(livingEntity, f, g, h, i, j);
    }

    @Override
    public void copyBipedStateTo(BipedEntityModel<ViolegerEntity> model) {
        super.copyBipedStateTo(model);
        model.head.copyTransform(this.head);
        model.hat.copyTransform(this.hat);
        model.body.copyTransform(this.body);
        model.rightArm.copyTransform(this.rightArm);
        model.leftArm.copyTransform(this.leftArm);
        model.rightLeg.copyTransform(this.rightLeg);
        model.leftLeg.copyTransform(this.leftLeg);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        right_leg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        left_leg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    public void renderArms(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        right_arm.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        left_arm.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}