package net.sashakyotoz.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.bosses.BossLikePathfinderMob;

public class AuricPatriarchModel extends HierarchicalModel<BossLikePathfinderMob> {
    public static final ModelLayerLocation AURIC_PATRIARCH = new ModelLayerLocation(UnseenWorld.makeID("auric_patriarch"), "main");
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

    public static LayerDefinition getTextureLocationdModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition general = modelPartData.addOrReplaceChild("general", CubeListBuilder.create(), PartPose.offset(0.0F, -8.0F, 0.0F));

        PartDefinition body = general.addOrReplaceChild("body", CubeListBuilder.create().texOffs(76, 75).addBox(-5.0F, -15.0F, -0.5F, 10.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.0F, -1.0F));

        PartDefinition chest = body.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(0, 48).addBox(-7.5F, -12.0F, -2.0F, 15.0F, 12.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -15.0F, 0.0F));

        PartDefinition head = chest.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, -23.0F, 10.7071F, 32.0F, 24.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 2.2929F));

        PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(66, 1).addBox(-3.0F, -2.0F, -5.0F, 8.0F, 17.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -15.0F, 1.2071F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(86, 43).addBox(-3.0F, -1.0F, -4.0F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.7071F, 0.0F, 0.7854F, 0.0F));

        PartDefinition right_arm = chest.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 68).mirror().addBox(-5.0F, -1.75F, -2.5F, 5.0F, 33.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(44, 75).mirror().addBox(-6.0F, -2.75F, -4.5F, 7.0F, 8.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-8.0F, -10.25F, 2.0F));

        PartDefinition arc = right_arm.addOrReplaceChild("arc", CubeListBuilder.create().texOffs(99, 117).addBox(12.0F, 14.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(101, 123).addBox(10.0F, 10.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(99, 117).addBox(9.0F, 11.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(99, 117).addBox(11.0F, 9.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(110, 118).addBox(-3.0F, -6.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(109, 121).addBox(-6.0F, -3.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(107, 117).addBox(-9.0F, -11.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(107, 119).addBox(-11.0F, -9.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(101, 121).addBox(-10.0F, -12.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(105, 121).addBox(-12.0F, -10.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(108, 114).addBox(-6.0F, -7.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(97, 111).addBox(-7.0F, -8.0F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(105, 111).addBox(-4.0F, -9.0F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(98, 102).addBox(-6.0F, -10.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(91, 107).addBox(-8.0F, -12.0F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(112, 114).addBox(-7.0F, -13.0F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(93, 111).addBox(-6.0F, -16.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(79, 117).addBox(-9.0F, -15.0F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(87, 117).addBox(-10.0F, -16.0F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(105, 115).addBox(-9.0F, -17.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(101, 115).addBox(-11.0F, -16.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(95, 117).addBox(-12.0F, -17.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(103, 113).addBox(-5.0F, -12.0F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(103, 119).addBox(-4.0F, -13.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(110, 116).addBox(-7.0F, -6.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(87, 111).addBox(-8.0F, -7.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(120, 102).addBox(-9.0F, -4.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(117, 111).addBox(-10.0F, -6.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(79, 124).addBox(-12.0F, -8.0F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(103, 105).addBox(-13.0F, -7.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(79, 104).addBox(-16.0F, -6.0F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(85, 120).addBox(-15.0F, -9.0F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(79, 120).addBox(-16.0F, -10.0F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(99, 119).addBox(-17.0F, -9.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(95, 119).addBox(-16.0F, -11.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(103, 117).addBox(-17.0F, -12.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(99, 108).addBox(-12.0F, -5.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(97, 115).addBox(-13.0F, -4.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(97, 113).addBox(10.0F, 13.0F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(95, 123).addBox(9.0F, 12.0F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(95, 121).addBox(9.0F, 7.0F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(91, 115).addBox(8.0F, 5.0F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(79, 115).addBox(10.0F, 8.0F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(112, 120).addBox(14.0F, 12.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(116, 102).addBox(13.0F, 10.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(123, 105).addBox(12.0F, 9.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(124, 102).addBox(7.0F, 9.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(119, 105).addBox(5.0F, 8.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(90, 102).addBox(6.0F, 6.0F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(103, 108).addBox(5.0F, 5.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(111, 111).addBox(2.0F, 2.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(115, 108).addBox(1.0F, 1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(109, 108).addBox(0.0F, 0.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(113, 105).addBox(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(107, 105).addBox(-2.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(110, 102).addBox(-3.0F, -3.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(104, 102).addBox(-6.0F, -6.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(79, 111).addBox(-5.0F, -5.0F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(79, 107).addBox(3.0F, 3.0F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(79, 102).addBox(3.0F, 6.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(83, 102).addBox(6.0F, 3.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(87, 106).addBox(8.0F, 10.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 30.25F, -0.5F, 1.5708F, -0.7854F, -1.5708F));

        PartDefinition left_arm = chest.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 68).addBox(0.0F, -1.75F, -2.5F, 5.0F, 33.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(44, 75).addBox(-1.0F, -2.75F, -4.5F, 7.0F, 8.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, -10.25F, 2.0F));

        PartDefinition right_wing = chest.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(48, 48).mirror().addBox(-19.0F, -15.6667F, 0.0F, 19.0F, 27.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-7.0F, -10.3333F, 9.0F));

        PartDefinition right_wing1 = right_wing.addOrReplaceChild("right_wing1", CubeListBuilder.create().texOffs(0, 24).mirror().addBox(-25.0F, -12.0F, 0.0F, 25.0F, 24.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-19.0F, -4.6667F, 0.0F));

        PartDefinition right_wing2 = right_wing1.addOrReplaceChild("right_wing2", CubeListBuilder.create().texOffs(50, 26).mirror().addBox(-22.0F, -9.0F, 0.0F, 22.0F, 17.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-25.0F, 0.0F, 0.0F));

        PartDefinition left_wing = chest.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(48, 48).addBox(0.0F, -15.6667F, 0.0F, 19.0F, 27.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, -10.3333F, 9.0F));

        PartDefinition left_wing1 = left_wing.addOrReplaceChild("left_wing1", CubeListBuilder.create().texOffs(0, 24).addBox(0.0F, -12.0F, 0.0F, 25.0F, 24.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(19.0F, -4.6667F, 0.0F));

        PartDefinition left_wing2 = left_wing1.addOrReplaceChild("left_wing2", CubeListBuilder.create().texOffs(50, 26).addBox(0.0F, -9.0F, 0.0F, 22.0F, 17.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(25.0F, 0.0F, 0.0F));

        PartDefinition right_leg = general.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(20, 68).mirror().addBox(-3.0F, 0.0F, -3.0F, 6.0F, 24.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, 8.0F, 2.0F));

        PartDefinition left_leg = general.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(20, 68).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 24.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 8.0F, 2.0F));
        return LayerDefinition.create(modelData, 128, 128);
    }


    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        general.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(BossLikePathfinderMob entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
        this.head.xRot = headPitch / (180F / (float) Math.PI);
    }
}