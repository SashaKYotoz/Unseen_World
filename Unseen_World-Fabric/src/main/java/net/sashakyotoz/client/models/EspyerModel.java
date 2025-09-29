package net.sashakyotoz.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.custom.EspyerEntity;

public class EspyerModel extends SinglePartEntityModel<EspyerEntity> {
    public static final EntityModelLayer ESPYER = new EntityModelLayer(UnseenWorld.makeID("espyer"), "main");
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;

    public EspyerModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
        this.rightHindLeg = this.body.getChild("rightHindLeg");
        this.leftHindLeg = this.body.getChild("leftHindLeg");
        this.rightFrontLeg = this.body.getChild("rightFrontLeg");
        this.leftFrontLeg = this.body.getChild("leftFrontLeg");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 16).cuboid(-4.0F, -22.0F, -1.0F, 8.0F, 12.0F, 5.0F, new Dilation(0.0F))
                .uv(50, 51).cuboid(2.0F, -28.0F, -1.0F, 0.0F, 6.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData Body_r1 = body.addChild("Body_r1", ModelPartBuilder.create().uv(26, 16).cuboid(-4.0F, -6.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.0F, 0.0F, -1.5708F, -1.5708F));

        ModelPartData Head_r1 = body.addChild("Head_r1", ModelPartBuilder.create().uv(22, 59).cuboid(1.0F, -4.0F, -2.0F, 0.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -3.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData Head_r2 = body.addChild("Head_r2", ModelPartBuilder.create().uv(24, 59).cuboid(1.0F, -4.0F, -2.0F, 0.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, -2.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -5.0F, -8.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, -19.0F, -1.0F));

        ModelPartData Head_r3 = head.addChild("Head_r3", ModelPartBuilder.create().uv(20, 59).cuboid(1.0F, -4.0F, -2.0F, 0.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(18, 59).cuboid(0.0F, -4.0F, 2.0F, 0.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(16, 59).cuboid(-4.0F, -4.0F, -1.0F, 0.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 7.0F, -5.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData rightHindLeg = body.addChild("rightHindLeg", ModelPartBuilder.create().uv(0, 52).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, -6.0F, 4.0F));

        ModelPartData leftHindLeg = body.addChild("leftHindLeg", ModelPartBuilder.create().uv(42, 32).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, -6.0F, 4.0F));

        ModelPartData rightFrontLeg = body.addChild("rightFrontLeg", ModelPartBuilder.create().uv(48, 0).cuboid(-5.0F, -3.0F, -2.0F, 4.0F, 9.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, -6.0F, -4.0F));

        ModelPartData leftFrontLeg = body.addChild("leftFrontLeg", ModelPartBuilder.create().uv(28, 51).cuboid(1.0F, -3.0F, -2.0F, 4.0F, 9.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, -6.0F, -4.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(EspyerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yaw = netHeadYaw * (float) (Math.PI / 180.0);
        this.head.pitch = headPitch * (float) (Math.PI / 180.0);
        this.leftHindLeg.pitch = MathHelper.cos(limbSwing * 0.5F) * 1.3F * limbSwingAmount;
        this.rightHindLeg.pitch = MathHelper.cos(limbSwing * 0.5F + (float) Math.PI) * 1.3F * limbSwingAmount;
        this.leftFrontLeg.pitch = MathHelper.cos(limbSwing * 0.5F + (float) Math.PI) * 1.3F * limbSwingAmount;
        this.rightFrontLeg.pitch = MathHelper.cos(limbSwing * 0.5F) * 1.3F * limbSwingAmount;
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