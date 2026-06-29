package net.sashakyotoz.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.animations.EclipseSentinelAnimations;
import net.sashakyotoz.common.entities.bosses.EclipseSentinel;

public class EclipseSentinelModel extends HierarchicalModel<EclipseSentinel> {
    public static final ModelLayerLocation ECLIPSE_SENTINEL = new ModelLayerLocation(UnseenWorld.makeID("eclipse_sentinel"), "main");
    private final ModelPart root;
    private final ModelPart head;

    public EclipseSentinelModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.root = root;
        ModelPart body = this.root.getChild("body");
        ModelPart chest = body.getChild("chest");
        this.head = chest.getChild("head");
    }

    public static LayerDefinition getTextureLocationdModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition body = modelPartData.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(-7.5F, -10.0F, -2.5F, 15.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(35, 0).mirror().addBox(-1.0F, -10.0F, -4.0F, 5.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(7.0F, 2.0F, 0.5F, 0.0F, 0.0F, -0.3054F));

        PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(35, 0).addBox(-4.0F, -10.0F, -4.0F, 5.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, 2.0F, 0.5F, 0.0F, 0.0F, 0.3054F));

        PartDefinition chest = body.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(0, 32).addBox(-10.0F, -17.0F, -4.0F, 19.0F, 17.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -10.0F, 0.0F));

        PartDefinition head = chest.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(-0.5F, -17.0F, 0.0F));

        PartDefinition cube_r3 = head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(76, 19).addBox(-3.5F, -9.0F, -3.5F, 7.0F, 10.0F, 7.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r4 = head.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(104, 19).addBox(-3.0F, -7.0F, -3.0F, 6.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.0F, -0.7854F, 0.0F));

        PartDefinition right_arm = chest.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(59, 0).addBox(-5.5F, 5.0F, -2.0F, 4.0F, 16.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-10.0F, -16.0F, 0.0F));

        PartDefinition cube_r5 = right_arm.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(24, 7).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -2.0F, 4.0F, -0.2618F, 0.0F, -0.0873F));

        PartDefinition cube_r6 = right_arm.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(24, 7).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -2.0F, -4.0F, 0.2618F, 0.0F, -0.0873F));

        PartDefinition cube_r7 = right_arm.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.25F, -2.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition cube_r8 = right_arm.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(24, 7).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -2.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

        PartDefinition cube_r9 = right_arm.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(32, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -2.0F, -3.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition cube_r10 = right_arm.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(32, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -1.5F, 3.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition cube_r11 = right_arm.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(88, 0).addBox(-8.0F, -4.0F, -5.0F, 10.0F, 9.0F, 10.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(1.0F, 1.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

        PartDefinition sword = right_arm.addOrReplaceChild("sword", CubeListBuilder.create().texOffs(0, 94).addBox(5.0F, 5.0F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 94).addBox(4.0F, 4.0F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(6, 98).addBox(3.0F, 3.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 98).addBox(2.0F, 2.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 94).addBox(-7.0F, -7.0F, -0.5F, 10.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 108).addBox(-7.0F, 3.0F, -0.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 108).addBox(-4.0F, 6.0F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 110).addBox(-3.0F, 7.0F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(17, 105).addBox(-11.0F, 3.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(27, 105).addBox(-9.0F, 1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 101).addBox(3.0F, -7.0F, -0.5F, 3.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 107).addBox(6.0F, -4.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 107).addBox(7.0F, -3.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 101).addBox(3.0F, -11.0F, -0.5F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 106).addBox(1.0F, -9.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 111).addBox(-12.0F, -12.0F, -0.5F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(38, 94).addBox(-24.0F, -24.0F, -0.5F, 10.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 117).addBox(-10.0F, -7.0F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(12, 114).addBox(-8.0F, -4.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 114).addBox(-9.0F, -4.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 114).addBox(-11.0F, -6.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(26, 112).addBox(-14.0F, -23.0F, -0.5F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(22, 114).addBox(-12.0F, -22.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 120).addBox(-11.0F, -21.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(36, 118).addBox(-12.0F, -17.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 118).addBox(-11.0F, -16.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 120).addBox(-10.0F, -15.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 122).addBox(-9.0F, -14.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 114).addBox(-8.0F, -13.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 114).addBox(-7.0F, -12.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(10, 121).addBox(-7.0F, -11.0F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 121).addBox(-12.0F, -7.0F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(32, 112).addBox(-13.0F, -18.0F, -0.5F, 1.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(40, 112).addBox(-14.0F, -19.0F, -0.5F, 1.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(36, 112).addBox(-15.0F, -14.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 123).addBox(-16.0F, -14.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 120).addBox(-17.0F, -14.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 124).addBox(-18.0F, -14.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 114).addBox(-19.0F, -14.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(22, 117).addBox(-23.0F, -14.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 112).addBox(-22.0F, -12.0F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(18, 116).addBox(-21.0F, -11.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 114).addBox(-4.0F, -9.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(12, 112).addBox(-4.0F, -8.0F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 117).addBox(-7.0F, -10.0F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, 21.0F, -8.0F, 1.5708F, -0.7854F, -1.5708F));

        PartDefinition trail = sword.addOrReplaceChild("trail", CubeListBuilder.create().texOffs(100, 112).addBox(-16.0F, -3.0F, 0.0F, 14.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -20.0F, 0.0F));

        PartDefinition left_arm = chest.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(59, 0).mirror().addBox(1.5F, 5.0F, -2.0F, 4.0F, 16.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(9.0F, -16.0F, 0.0F));

        PartDefinition cube_r12 = left_arm.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(24, 7).mirror().addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, -2.0F, 4.0F, -0.2618F, 0.0F, 0.0873F));

        PartDefinition cube_r13 = left_arm.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(24, 7).mirror().addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, -2.0F, -4.0F, 0.2618F, 0.0F, 0.0873F));

        PartDefinition cube_r14 = left_arm.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(24, 0).mirror().addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(6.25F, -2.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition cube_r15 = left_arm.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(24, 7).mirror().addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

        PartDefinition cube_r16 = left_arm.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, -2.0F, -3.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition cube_r17 = left_arm.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, -1.5F, 3.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition cube_r18 = left_arm.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(88, 0).mirror().addBox(-2.0F, -4.0F, -5.0F, 10.0F, 9.0F, 10.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

        PartDefinition eclipse_circle = chest.addOrReplaceChild("eclipse_circle", CubeListBuilder.create().texOffs(54, 50).addBox(-18.5F, -18.5F, 0.0F, 37.0F, 37.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -6.5F, 16.0F));

        PartDefinition right_leg = modelPartData.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(104, 32).addBox(-3.0F, -1.0F, -4.0F, 6.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 13.0F, 1.0F));

        PartDefinition left_leg = modelPartData.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(104, 32).mirror().addBox(-3.0F, -1.0F, -4.0F, 6.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, 13.0F, 1.0F));
        return LayerDefinition.create(modelData, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        root.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }


    @Override
    public void setupAnim(EclipseSentinel entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }
}