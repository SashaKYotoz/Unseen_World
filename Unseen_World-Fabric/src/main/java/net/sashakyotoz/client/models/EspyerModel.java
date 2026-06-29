package net.sashakyotoz.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.custom.EspyerEntity;

public class EspyerModel extends HierarchicalModel<EspyerEntity> {
    public static final ModelLayerLocation ESPYER = new ModelLayerLocation(UnseenWorld.makeID("espyer"), "main");
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

    public static LayerDefinition getTextureLocationdModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition body = modelPartData.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -22.0F, -1.0F, 8.0F, 12.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(50, 51).addBox(2.0F, -28.0F, -1.0F, 0.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition Body_r1 = body.addOrReplaceChild("Body_r1", CubeListBuilder.create().texOffs(26, 16).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -1.5708F, -1.5708F));

        PartDefinition Head_r1 = body.addOrReplaceChild("Head_r1", CubeListBuilder.create().texOffs(22, 59).addBox(1.0F, -4.0F, -2.0F, 0.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -3.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition Head_r2 = body.addOrReplaceChild("Head_r2", CubeListBuilder.create().texOffs(24, 59).addBox(1.0F, -4.0F, -2.0F, 0.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -2.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -5.0F, -8.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -19.0F, -1.0F));

        PartDefinition Head_r3 = head.addOrReplaceChild("Head_r3", CubeListBuilder.create().texOffs(20, 59).addBox(1.0F, -4.0F, -2.0F, 0.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(18, 59).addBox(0.0F, -4.0F, 2.0F, 0.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 59).addBox(-4.0F, -4.0F, -1.0F, 0.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, -5.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition rightHindLeg = body.addOrReplaceChild("rightHindLeg", CubeListBuilder.create().texOffs(0, 52).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -6.0F, 4.0F));

        PartDefinition leftHindLeg = body.addOrReplaceChild("leftHindLeg", CubeListBuilder.create().texOffs(42, 32).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -6.0F, 4.0F));

        PartDefinition rightFrontLeg = body.addOrReplaceChild("rightFrontLeg", CubeListBuilder.create().texOffs(48, 0).addBox(-5.0F, -3.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -6.0F, -4.0F));

        PartDefinition leftFrontLeg = body.addOrReplaceChild("leftFrontLeg", CubeListBuilder.create().texOffs(28, 51).addBox(1.0F, -3.0F, -2.0F, 4.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -6.0F, -4.0F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(EspyerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw * (float) (Math.PI / 180.0);
        this.head.xRot = headPitch * (float) (Math.PI / 180.0);
        this.leftHindLeg.xRot = Mth.cos(limbSwing * 0.5F) * 1.3F * limbSwingAmount;
        this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.5F + (float) Math.PI) * 1.3F * limbSwingAmount;
        this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.5F + (float) Math.PI) * 1.3F * limbSwingAmount;
        this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.5F) * 1.3F * limbSwingAmount;
    }
}