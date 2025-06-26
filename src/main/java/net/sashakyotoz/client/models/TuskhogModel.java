package net.sashakyotoz.client.models;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.custom.TuskhogEntity;

public class TuskhogModel<T extends TuskhogEntity> extends AnimalModel<T> {
    public static final EntityModelLayer TUSKHOG = new EntityModelLayer(UnseenWorld.makeID("tuskhog"), "main");
    private final ModelPart root;
    protected final ModelPart head;
    protected final ModelPart body;
    protected final ModelPart rightHindLeg;
    protected final ModelPart leftHindLeg;
    protected final ModelPart rightFrontLeg;
    protected final ModelPart leftFrontLeg;

    public TuskhogModel(ModelPart root) {
        super(false, 0,0,0,0,0);
        this.root = root;
        this.head = root.getChild(EntityModelPartNames.HEAD);
        this.body = root.getChild(EntityModelPartNames.BODY);
        this.rightHindLeg = root.getChild(EntityModelPartNames.RIGHT_HIND_LEG);
        this.leftHindLeg = root.getChild(EntityModelPartNames.LEFT_HIND_LEG);
        this.rightFrontLeg = root.getChild(EntityModelPartNames.RIGHT_FRONT_LEG);
        this.leftFrontLeg = root.getChild(EntityModelPartNames.LEFT_FRONT_LEG);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
                .uv(16, 16).cuboid(-2.0F, 0.0F, -9.0F, 4.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 58).mirrored().cuboid(-9.0F, -3.0F, -6.0F, 5.0F, 3.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
                .uv(16, 58).cuboid(7.0F, -3.0F, -9.0F, 2.0F, 3.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 58).cuboid(4.0F, -3.0F, -6.0F, 5.0F, 3.0F, 3.0F, new Dilation(0.0F))
                .uv(16, 58).mirrored().cuboid(-9.0F, -3.0F, -9.0F, 2.0F, 3.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
                .uv(0, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 12.0F, -7.0F));

        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(26, 56).cuboid(5.0F, 1.0F, -13.0F, 8.0F, 0.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 13.0F, 9.0F));

        ModelPartData body_r1 = body.addChild("body_r1", ModelPartBuilder.create().uv(50, 50).cuboid(0.0F, -3.5F, -1.0F, 0.0F, 7.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, -9.5F, 1.5708F, 0.7854F, 0.0F));

        ModelPartData body_r2 = body.addChild("body_r2", ModelPartBuilder.create().uv(50, 50).cuboid(0.0F, -3.5F, -1.0F, 0.0F, 7.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, -9.5F, 1.5708F, -0.7854F, 0.0F));

        ModelPartData body_r3 = body.addChild("body_r3", ModelPartBuilder.create().uv(26, 56).cuboid(0.0F, 0.0F, -4.0F, 8.0F, 0.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, 1.0F, -9.0F, 1.5708F, 0.0F, 0.0F));

        ModelPartData body_r4 = body.addChild("body_r4", ModelPartBuilder.create().uv(28, 32).cuboid(-5.0F, -18.0F, -5.0F, 10.0F, 16.0F, 8.0F, new Dilation(0.5F))
                .uv(28, 8).cuboid(-5.0F, -18.0F, -5.0F, 10.0F, 16.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        ModelPartData right_hind_leg = modelPartData.addChild("right_hind_leg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.0F, 18.0F, 6.0F));

        ModelPartData left_hind_leg = modelPartData.addChild("left_hind_leg", ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(3.0F, 18.0F, 6.0F));

        ModelPartData right_front_leg = modelPartData.addChild("right_front_leg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.0F, 18.0F, -6.0F));

        ModelPartData left_front_leg = modelPartData.addChild("left_front_leg", ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(3.0F, 18.0F, -6.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        root.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(this.body, this.rightHindLeg, this.leftHindLeg, this.rightFrontLeg, this.leftFrontLeg);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.head.pitch = headPitch * (float) (Math.PI / 180.0);
        this.head.yaw = headYaw * (float) (Math.PI / 180.0);
        float k = entity.getHeadPitch();
        if (k != 0.0F)
            this.head.pitch = k;
        this.rightHindLeg.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
        this.leftHindLeg.pitch = MathHelper.cos(limbAngle * 0.6662F + (float) Math.PI) * 1.4F * limbDistance;
        this.rightFrontLeg.pitch = MathHelper.cos(limbAngle * 0.6662F + (float) Math.PI) * 1.4F * limbDistance;
        this.leftFrontLeg.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
    }
}