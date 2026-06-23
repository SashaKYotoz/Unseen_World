package net.sashakyotoz.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.bosses.BossLikePathfinderMob;

public class AuricPatriarchModel extends SinglePartEntityModel<BossLikePathfinderMob> {
    public static final EntityModelLayer AURIC_PATRIARCH = new EntityModelLayer(UnseenWorld.makeID("auric_patriarch"), "main");
    private final ModelPart root;
    private final ModelPart general;
    private final ModelPart head;

    public AuricPatriarchModel(ModelPart root) {
        this.root = root;
        this.general = root.getChild("general");
        ModelPart body = this.general.getChild("body");
        ModelPart chest = body.getChild("chest");
        this.head = chest.getChild("head");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData general = modelPartData.addChild("general", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -8.0F, 0.0F));

        ModelPartData body = general.addChild("body", ModelPartBuilder.create().uv(76, 75).cuboid(-5.0F, -15.0F, -0.5F, 10.0F, 10.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 14.0F, -1.0F));

        ModelPartData chest = body.addChild("chest", ModelPartBuilder.create().uv(0, 48).cuboid(-7.5F, -12.0F, -2.0F, 15.0F, 12.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -15.0F, 0.0F));

        ModelPartData head = chest.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-16.0F, -23.0F, 10.7071F, 32.0F, 24.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -12.0F, 2.2929F));

        ModelPartData cube_r1 = head.addChild("cube_r1", ModelPartBuilder.create().uv(66, 1).cuboid(-3.0F, -2.0F, -5.0F, 8.0F, 17.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -15.0F, 1.2071F, 0.0F, 0.7854F, 0.0F));

        ModelPartData cube_r2 = head.addChild("cube_r2", ModelPartBuilder.create().uv(86, 43).cuboid(-3.0F, -1.0F, -4.0F, 7.0F, 8.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -8.0F, 0.7071F, 0.0F, 0.7854F, 0.0F));

        ModelPartData right_arm = chest.addChild("right_arm", ModelPartBuilder.create().uv(0, 68).mirrored().cuboid(-5.0F, -1.75F, -2.5F, 5.0F, 33.0F, 5.0F, new Dilation(0.0F)).mirrored(false)
                .uv(44, 75).mirrored().cuboid(-6.0F, -2.75F, -4.5F, 7.0F, 8.0F, 9.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-8.0F, -10.25F, 2.0F));

        ModelPartData arc = right_arm.addChild("arc", ModelPartBuilder.create().uv(99, 117).cuboid(12.0F, 14.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(101, 123).cuboid(10.0F, 10.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(99, 117).cuboid(9.0F, 11.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(99, 117).cuboid(11.0F, 9.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(110, 118).cuboid(-3.0F, -6.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(109, 121).cuboid(-6.0F, -3.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(107, 117).cuboid(-9.0F, -11.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(107, 119).cuboid(-11.0F, -9.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(101, 121).cuboid(-10.0F, -12.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(105, 121).cuboid(-12.0F, -10.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(108, 114).cuboid(-6.0F, -7.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(97, 111).cuboid(-7.0F, -8.0F, -0.5F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(105, 111).cuboid(-4.0F, -9.0F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(98, 102).cuboid(-6.0F, -10.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(91, 107).cuboid(-8.0F, -12.0F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(112, 114).cuboid(-7.0F, -13.0F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(93, 111).cuboid(-6.0F, -16.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(79, 117).cuboid(-9.0F, -15.0F, -0.5F, 3.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(87, 117).cuboid(-10.0F, -16.0F, -0.5F, 3.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(105, 115).cuboid(-9.0F, -17.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(101, 115).cuboid(-11.0F, -16.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(95, 117).cuboid(-12.0F, -17.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(103, 113).cuboid(-5.0F, -12.0F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(103, 119).cuboid(-4.0F, -13.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(110, 116).cuboid(-7.0F, -6.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(87, 111).cuboid(-8.0F, -7.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(120, 102).cuboid(-9.0F, -4.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(117, 111).cuboid(-10.0F, -6.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(79, 124).cuboid(-12.0F, -8.0F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(103, 105).cuboid(-13.0F, -7.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(79, 104).cuboid(-16.0F, -6.0F, -0.5F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(85, 120).cuboid(-15.0F, -9.0F, -0.5F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(79, 120).cuboid(-16.0F, -10.0F, -0.5F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(99, 119).cuboid(-17.0F, -9.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(95, 119).cuboid(-16.0F, -11.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(103, 117).cuboid(-17.0F, -12.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(99, 108).cuboid(-12.0F, -5.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(97, 115).cuboid(-13.0F, -4.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(97, 113).cuboid(10.0F, 13.0F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(95, 123).cuboid(9.0F, 12.0F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(95, 121).cuboid(9.0F, 7.0F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(91, 115).cuboid(8.0F, 5.0F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(79, 115).cuboid(10.0F, 8.0F, -0.5F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(112, 120).cuboid(14.0F, 12.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(116, 102).cuboid(13.0F, 10.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(123, 105).cuboid(12.0F, 9.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(124, 102).cuboid(7.0F, 9.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(119, 105).cuboid(5.0F, 8.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(90, 102).cuboid(6.0F, 6.0F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(103, 108).cuboid(5.0F, 5.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(111, 111).cuboid(2.0F, 2.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(115, 108).cuboid(1.0F, 1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(109, 108).cuboid(0.0F, 0.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(113, 105).cuboid(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(107, 105).cuboid(-2.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(110, 102).cuboid(-3.0F, -3.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(104, 102).cuboid(-6.0F, -6.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(79, 111).cuboid(-5.0F, -5.0F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(79, 107).cuboid(3.0F, 3.0F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(79, 102).cuboid(3.0F, 6.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(83, 102).cuboid(6.0F, 3.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(87, 106).cuboid(8.0F, 10.0F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, 30.25F, -0.5F, 1.5708F, -0.7854F, -1.5708F));

        ModelPartData left_arm = chest.addChild("left_arm", ModelPartBuilder.create().uv(0, 68).cuboid(0.0F, -1.75F, -2.5F, 5.0F, 33.0F, 5.0F, new Dilation(0.0F))
                .uv(44, 75).cuboid(-1.0F, -2.75F, -4.5F, 7.0F, 8.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, -10.25F, 2.0F));

        ModelPartData right_wing = chest.addChild("right_wing", ModelPartBuilder.create().uv(48, 48).mirrored().cuboid(-19.0F, -15.6667F, 0.0F, 19.0F, 27.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-7.0F, -10.3333F, 9.0F));

        ModelPartData right_wing1 = right_wing.addChild("right_wing1", ModelPartBuilder.create().uv(0, 24).mirrored().cuboid(-25.0F, -12.0F, 0.0F, 25.0F, 24.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-19.0F, -4.6667F, 0.0F));

        ModelPartData right_wing2 = right_wing1.addChild("right_wing2", ModelPartBuilder.create().uv(50, 26).mirrored().cuboid(-22.0F, -9.0F, 0.0F, 22.0F, 17.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-25.0F, 0.0F, 0.0F));

        ModelPartData left_wing = chest.addChild("left_wing", ModelPartBuilder.create().uv(48, 48).cuboid(0.0F, -15.6667F, 0.0F, 19.0F, 27.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(7.0F, -10.3333F, 9.0F));

        ModelPartData left_wing1 = left_wing.addChild("left_wing1", ModelPartBuilder.create().uv(0, 24).cuboid(0.0F, -12.0F, 0.0F, 25.0F, 24.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(19.0F, -4.6667F, 0.0F));

        ModelPartData left_wing2 = left_wing1.addChild("left_wing2", ModelPartBuilder.create().uv(50, 26).cuboid(0.0F, -9.0F, 0.0F, 22.0F, 17.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(25.0F, 0.0F, 0.0F));

        ModelPartData right_leg = general.addChild("right_leg", ModelPartBuilder.create().uv(20, 68).mirrored().cuboid(-3.0F, 0.0F, -3.0F, 6.0F, 24.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-4.0F, 8.0F, 2.0F));

        ModelPartData left_leg = general.addChild("left_leg", ModelPartBuilder.create().uv(20, 68).cuboid(-3.0F, 0.0F, -3.0F, 6.0F, 24.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(4.0F, 8.0F, 2.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public void setAngles(BossLikePathfinderMob entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.head.yaw = netHeadYaw / (180F / (float) Math.PI);
        this.head.pitch = headPitch / (180F / (float) Math.PI);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        general.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }
}