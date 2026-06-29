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
import net.sashakyotoz.common.entities.custom.SaberpardEntity;

public class SaberpardModel extends HierarchicalModel<SaberpardEntity> {
    public static final ModelLayerLocation SABERPARD = new ModelLayerLocation(UnseenWorld.makeID("saberpard"), "main");
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

    public static LayerDefinition getTextureLocationdModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition body = modelPartData.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

        PartDefinition belly = body.addOrReplaceChild("belly", CubeListBuilder.create().texOffs(28, 9).addBox(-3.0F, -8.0F, -3.0F, 6.0F, 16.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 10).addBox(-4.0F, -11.0F, -1.5F, 8.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(30, 0).addBox(-4.0F, -6.0F, -0.5F, 8.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(-4.0F, -11.0F, -3.5F, 8.0F, 5.0F, 3.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(24, 48).addBox(-3.5F, -3.0F, -5.0F, 7.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(54, 15).addBox(-1.5F, -0.0156F, -7.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(52, 19).addBox(-1.5F, 1.9844F, -7.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(55, 37).mirror().addBox(-3.0F, -5.0F, -3.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(48, 37).addBox(1.0F, -5.0F, -3.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -11.0F));

        PartDefinition tail1 = body.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(4, 23).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(1, 51).addBox(-0.5F, 1.0F, -2.0F, 1.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 7.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition tail2 = tail1.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(8, 23).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(1, 50).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition backLegL = body.addOrReplaceChild("backLegL", CubeListBuilder.create().texOffs(12, 24).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(58, -1).addBox(-1.1F, 4.0F, -2.0F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(58, -1).addBox(0.9F, 4.0F, -2.0F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.1F, 1.0F, 6.0F));

        PartDefinition backLegR = body.addOrReplaceChild("backLegR", CubeListBuilder.create().texOffs(12, 24).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(58, -1).addBox(-0.9F, 4.0F, -2.0F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(58, -1).addBox(1.1F, 4.0F, -2.0F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.1F, 1.0F, 6.0F));

        PartDefinition frontLegL = body.addOrReplaceChild("frontLegL", CubeListBuilder.create().texOffs(56, 3).addBox(-1.0F, -1.2F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(58, -1).addBox(1.0F, 6.8F, -2.0F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(58, -1).addBox(-1.0F, 6.8F, -2.0F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.2F, -2.0F, -5.0F));

        PartDefinition frontLegR = body.addOrReplaceChild("frontLegR", CubeListBuilder.create().texOffs(56, 3).addBox(-1.0F, -1.2F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(58, -1).addBox(-1.0F, 6.8F, -2.0F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(58, -1).addBox(1.0F, 6.8F, -2.0F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.2F, -2.0F, -5.0F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public void setupAnim(SaberpardEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * (float) (Math.PI / 180.0);
        this.head.yRot = netHeadYaw * (float) (Math.PI / 180.0);
        if (this.animationState != 3) {
            if (this.animationState == 2) {
                this.leftHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
                this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F + 0.3F) * limbSwingAmount;
                this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI + 0.3F) * limbSwingAmount;
                this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
                this.lowerTail.xRot = 1.7278761F + (float) (Math.PI / 10) * Mth.cos(limbSwing) * limbSwingAmount;
            } else {
                this.leftHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
                this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
                this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
                this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
                if (this.animationState == 1) {
                    this.lowerTail.xRot = 1.7278761F + (float) (Math.PI / 4) * Mth.cos(limbSwing) * limbSwingAmount;
                } else {
                    this.lowerTail.xRot = 1.7278761F + 0.47123894F * Mth.cos(limbSwing) * limbSwingAmount;
                }
            }
        }
    }

    @Override
    public void prepareMobModel(SaberpardEntity entity, float limbAngle, float limbDistance, float tickDelta) {
        if (entity.isCrouching()) {
            this.body.y++;
            this.head.y += 2.0F;
            this.upperTail.y++;
            this.lowerTail.y -= 3.0F;
            this.lowerTail.z += 1.0F;
            this.upperTail.xRot = (float) (Math.PI / 2);
            this.lowerTail.xRot = (float) (Math.PI / 2);
            this.animationState = 0;
        } else if (entity.isSprinting()) {
            this.lowerTail.y = this.upperTail.y;
            this.lowerTail.z += 1.0F;
            this.upperTail.xRot = (float) (Math.PI / 2);
            this.lowerTail.xRot = (float) (Math.PI / 2);
            this.animationState = 2;
        } else {
            this.animationState = 1;
        }
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}