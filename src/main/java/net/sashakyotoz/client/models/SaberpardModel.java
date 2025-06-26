package net.sashakyotoz.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.custom.SaberpardEntity;

public class SaberpardModel extends SinglePartEntityModel<SaberpardEntity> {
    public static final EntityModelLayer SABERPARD = new EntityModelLayer(UnseenWorld.makeID("saberpard"), "main");
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart upperTail;
    private final ModelPart lowerTail;
    private final ModelPart leftHindLeg;
    private final ModelPart rightHindLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart rightFrontLeg;

    private int animationState = 1;

    public SaberpardModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
        this.upperTail = this.body.getChild("tail1");
        this.lowerTail = this.upperTail.getChild("tail2");
        this.leftHindLeg = this.body.getChild("backLegL");
        this.rightHindLeg = this.body.getChild("backLegR");
        this.leftFrontLeg = this.body.getChild("frontLegL");
        this.rightFrontLeg = this.body.getChild("frontLegR");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 17.0F, 0.0F));

        ModelPartData belly = body.addChild("belly", ModelPartBuilder.create().uv(28, 9).cuboid(-3.0F, -8.0F, -3.0F, 6.0F, 16.0F, 6.0F, new Dilation(0.0F))
                .uv(0, 10).cuboid(-4.0F, -11.0F, -1.5F, 8.0F, 5.0F, 6.0F, new Dilation(0.0F))
                .uv(30, 0).cuboid(-4.0F, -6.0F, -0.5F, 8.0F, 4.0F, 5.0F, new Dilation(0.0F))
                .uv(0, 32).cuboid(-4.0F, -11.0F, -3.5F, 8.0F, 5.0F, 3.0F, new Dilation(0.01F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(24, 48).cuboid(-3.5F, -3.0F, -5.0F, 7.0F, 5.0F, 5.0F, new Dilation(0.0F))
                .uv(54, 15).cuboid(-1.5F, -0.0156F, -7.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(52, 19).cuboid(-1.5F, 1.9844F, -7.0F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(55, 37).mirrored().cuboid(-3.0F, -5.0F, -3.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(48, 37).cuboid(1.0F, -5.0F, -3.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.0F, -11.0F));

        ModelPartData tail1 = body.addChild("tail1", ModelPartBuilder.create().uv(4, 23).cuboid(-0.5F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F, new Dilation(0.0F))
                .uv(1, 51).cuboid(-0.5F, 1.0F, -2.0F, 1.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, 7.0F, 0.7854F, 0.0F, 0.0F));

        ModelPartData tail2 = tail1.addChild("tail2", ModelPartBuilder.create().uv(8, 23).cuboid(-0.5F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F, new Dilation(0.0F))
                .uv(1, 50).cuboid(-0.5F, -1.0F, -2.0F, 1.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 8.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        ModelPartData backLegL = body.addChild("backLegL", ModelPartBuilder.create().uv(12, 24).mirrored().cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F)).mirrored(false)
                .uv(58, -1).cuboid(-1.1F, 4.0F, -2.0F, 0.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(58, -1).cuboid(0.9F, 4.0F, -2.0F, 0.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(2.1F, 1.0F, 6.0F));

        ModelPartData backLegR = body.addChild("backLegR", ModelPartBuilder.create().uv(12, 24).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F))
                .uv(58, -1).cuboid(-0.9F, 4.0F, -2.0F, 0.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(58, -1).cuboid(1.1F, 4.0F, -2.0F, 0.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.1F, 1.0F, 6.0F));

        ModelPartData frontLegL = body.addChild("frontLegL", ModelPartBuilder.create().uv(56, 3).cuboid(-1.0F, -1.2F, -1.0F, 2.0F, 10.0F, 2.0F, new Dilation(0.0F))
                .uv(58, -1).cuboid(1.0F, 6.8F, -2.0F, 0.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(58, -1).cuboid(-1.0F, 6.8F, -2.0F, 0.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(2.2F, -2.0F, -5.0F));

        ModelPartData frontLegR = body.addChild("frontLegR", ModelPartBuilder.create().uv(56, 3).cuboid(-1.0F, -1.2F, -1.0F, 2.0F, 10.0F, 2.0F, new Dilation(0.0F))
                .uv(58, -1).cuboid(-1.0F, 6.8F, -2.0F, 0.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(58, -1).cuboid(1.0F, 6.8F, -2.0F, 0.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.2F, -2.0F, -5.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(SaberpardEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.pitch = headPitch * (float) (Math.PI / 180.0);
        this.head.yaw = netHeadYaw * (float) (Math.PI / 180.0);
        if (this.animationState != 3) {
            if (this.animationState == 2) {
                this.leftHindLeg.pitch = MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount;
                this.rightHindLeg.pitch = MathHelper.cos(limbSwing * 0.6662F + 0.3F) * limbSwingAmount;
                this.leftFrontLeg.pitch = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI + 0.3F) * limbSwingAmount;
                this.rightFrontLeg.pitch = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
                this.lowerTail.pitch = 1.7278761F + (float) (Math.PI / 10) * MathHelper.cos(limbSwing) * limbSwingAmount;
            } else {
                this.leftHindLeg.pitch = MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount;
                this.rightHindLeg.pitch = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
                this.leftFrontLeg.pitch = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
                this.rightFrontLeg.pitch = MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount;
                if (this.animationState == 1) {
                    this.lowerTail.pitch = 1.7278761F + (float) (Math.PI / 4) * MathHelper.cos(limbSwing) * limbSwingAmount;
                } else {
                    this.lowerTail.pitch = 1.7278761F + 0.47123894F * MathHelper.cos(limbSwing) * limbSwingAmount;
                }
            }
        }
    }

    @Override
    public void animateModel(SaberpardEntity entity, float limbAngle, float limbDistance, float tickDelta) {
        if (entity.isInSneakingPose()) {
            this.body.pivotY++;
            this.head.pivotY += 2.0F;
            this.upperTail.pivotY++;
            this.lowerTail.pivotY -= 3.0F;
            this.lowerTail.pivotZ += 1.0F;
            this.upperTail.pitch = (float) (Math.PI / 2);
            this.lowerTail.pitch = (float) (Math.PI / 2);
            this.animationState = 0;
        } else if (entity.isSprinting()) {
            this.lowerTail.pivotY = this.upperTail.pivotY;
            this.lowerTail.pivotZ += 1.0F;
            this.upperTail.pitch = (float) (Math.PI / 2);
            this.lowerTail.pitch = (float) (Math.PI / 2);
            this.animationState = 2;
        } else {
            this.animationState = 1;
        }
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