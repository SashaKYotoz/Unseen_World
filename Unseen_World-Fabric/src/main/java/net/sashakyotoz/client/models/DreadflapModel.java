package net.sashakyotoz.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.animations.DreadflapAnimations;
import net.sashakyotoz.common.entities.custom.DreadflapEntity;

public class DreadflapModel extends HierarchicalModel<DreadflapEntity> {
    public static final ModelLayerLocation DREADFLAP = new ModelLayerLocation(UnseenWorld.makeID("dreadflap"), "main");

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;

    public DreadflapModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
    }

    public static LayerDefinition getTextureLocationdModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition body = modelPartData.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 11.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(26, 0).addBox(-2.5F, -4.0F, -5.0F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(24, 23).addBox(2.5F, -10.0F, 0.0F, 3.0F, 9.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(24, 23).mirror().addBox(-5.5F, -10.0F, 0.0F, 3.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -1.0F, 1.0F));

        PartDefinition main_body = body.addOrReplaceChild("main_body", CubeListBuilder.create().texOffs(28, 9).addBox(-2.5F, 0.0F, -2.0F, 5.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(31, 19).addBox(-1.5F, 6.0F, -1.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(16, 19).addBox(0.0F, 6.0F, 0.0F, 0.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 7).addBox(0.0F, -2.0F, 0.0F, 0.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 1.0F));

        PartDefinition cube_r1 = main_body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(16, 19).mirror().addBox(-1.5F, 4.0F, 0.5F, 0.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 3.0F, 2.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r2 = main_body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(16, 19).addBox(1.5F, 4.0F, 0.5F, 0.0F, 9.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 2.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition right_s_b = main_body.addOrReplaceChild("right_s_b", CubeListBuilder.create().texOffs(16, 0).mirror().addBox(-4.0F, -10.0F, 0.0F, 5.0F, 13.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.5F, 1.0F, 1.0F));

        PartDefinition right_wing = right_s_b.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-8.0F, -11.0F, 0.0F, 8.0F, 28.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, 0.0F, 0.0F));

        PartDefinition left_s_b = main_body.addOrReplaceChild("left_s_b", CubeListBuilder.create().texOffs(16, 0).addBox(-1.0F, -10.0F, 0.0F, 5.0F, 13.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 1.0F, 1.0F));

        PartDefinition left_wing = left_s_b.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -11.0F, 0.0F, 8.0F, 28.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, 0.0F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    private void setRoostingHeadAngles(float yaw) {
        this.head.yRot = yaw * (float) (Math.PI / 180.0);
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(DreadflapEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        if (entity.isRoosting())
            this.setRoostingHeadAngles(netHeadYaw);

        float f = Math.min((float) entity.getDeltaMovement().lengthSqr() * 50.0F, 1.5F);

        this.animate(entity.flying, DreadflapAnimations.FLYING, ageInTicks, f);
        this.animate(entity.roosting, DreadflapAnimations.ROOSTING, ageInTicks, f);
    }
}