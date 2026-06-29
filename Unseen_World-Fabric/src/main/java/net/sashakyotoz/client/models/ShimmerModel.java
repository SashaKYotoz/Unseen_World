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
import net.sashakyotoz.common.entities.custom.ShimmerEntity;

public class ShimmerModel extends HierarchicalModel<ShimmerEntity> {
    public static final ModelLayerLocation SHIMMER = new ModelLayerLocation(UnseenWorld.makeID("shimmer"), "main");

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart right_tentacle;
    private final ModelPart left_tentacle;
    private final ModelPart tentacle_0;
    private final ModelPart tentacle_1;
    private final ModelPart tentacle_2;
    private final ModelPart tentacle_3;
    private final ModelPart tentacle_4;
    private final ModelPart tentacle_5;
    private final ModelPart tentacle_6;
    private final ModelPart tentacle_7;

    public ShimmerModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.right_tentacle = this.head.getChild("right_tentacle");
        this.left_tentacle = this.head.getChild("left_tentacle");
        ModelPart directional = this.head.getChild("directional");
        this.tentacle_0 = directional.getChild("tentacle_0");
        this.tentacle_1 = directional.getChild("tentacle_1");
        this.tentacle_2 = directional.getChild("tentacle_2");
        this.tentacle_3 = directional.getChild("tentacle_3");
        ModelPart diagonal = this.head.getChild("diagonal");
        this.tentacle_4 = diagonal.getChild("tentacle_4");
        this.tentacle_5 = diagonal.getChild("tentacle_5");
        this.tentacle_6 = diagonal.getChild("tentacle_6");
        this.tentacle_7 = diagonal.getChild("tentacle_7");
    }

    public static LayerDefinition getTextureLocationdModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition head = modelPartData.addOrReplaceChild("head", CubeListBuilder.create().texOffs(84, 0).addBox(-5.5F, -16.0F, -5.5F, 11.0F, 16.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(63, 13).addBox(-2.5F, -16.0F, -6.5F, 5.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(63, 13).addBox(-2.5F, -16.0F, 5.5F, 5.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 38).addBox(-7.5F, -37.0F, -7.5F, 15.0F, 21.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-10.5F, -35.0F, -10.5F, 21.0F, 17.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(63, 13).addBox(-2.5F, -3.5F, -1.0F, 5.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.5F, -12.5F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(63, 13).addBox(-2.5F, -3.5F, -1.0F, 5.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, -12.5F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r3 = head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(78, 38).addBox(-12.0F, -17.0F, 1.0F, 25.0F, 18.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -1.0F, -1.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition cube_r4 = head.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(78, 38).addBox(-12.0F, -17.0F, 1.0F, 25.0F, 18.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 1.0F, -0.5F, 0.0F, -0.7854F, 0.0F));

        PartDefinition right_tentacle = head.addOrReplaceChild("right_tentacle", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 18.0F, -2.0F, 2.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(12, 0).addBox(-2.0F, 2.0F, -1.0F, 2.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -2.0F, 0.0F));

        PartDefinition left_tentacle = head.addOrReplaceChild("left_tentacle", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, 18.0F, -2.0F, 2.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(12, 0).mirror().addBox(0.0F, 2.0F, -1.0F, 2.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, -2.0F, 0.0F));

        PartDefinition directional = head.addOrReplaceChild("directional", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition tentacle_0 = directional.addOrReplaceChild("tentacle_0", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 4.0F));

        PartDefinition cube_r5 = tentacle_0.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 74).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 19.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition tentacle_1 = directional.addOrReplaceChild("tentacle_1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -4.0F));

        PartDefinition cube_r6 = tentacle_1.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 74).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 19.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

        PartDefinition tentacle_2 = directional.addOrReplaceChild("tentacle_2", CubeListBuilder.create(), PartPose.offset(-4.0F, 0.0F, 0.0F));

        PartDefinition cube_r7 = tentacle_2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 74).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 19.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

        PartDefinition tentacle_3 = directional.addOrReplaceChild("tentacle_3", CubeListBuilder.create(), PartPose.offset(4.0F, 0.0F, 0.0F));

        PartDefinition cube_r8 = tentacle_3.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 74).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 19.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

        PartDefinition diagonal = head.addOrReplaceChild("diagonal", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition tentacle_4 = diagonal.addOrReplaceChild("tentacle_4", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 4.0F));

        PartDefinition cube_r9 = tentacle_4.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 74).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 19.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition tentacle_5 = diagonal.addOrReplaceChild("tentacle_5", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -4.0F));

        PartDefinition cube_r10 = tentacle_5.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 74).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 19.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

        PartDefinition tentacle_6 = diagonal.addOrReplaceChild("tentacle_6", CubeListBuilder.create(), PartPose.offset(-4.0F, 0.0F, 0.0F));

        PartDefinition cube_r11 = tentacle_6.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 74).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 19.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

        PartDefinition tentacle_7 = diagonal.addOrReplaceChild("tentacle_7", CubeListBuilder.create(), PartPose.offset(4.0F, 0.0F, 0.0F));

        PartDefinition cube_r12 = tentacle_7.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 74).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 19.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, -0.1745F));
        return LayerDefinition.create(modelData, 128, 128);
    }

    @Override
    public void setupAnim(ShimmerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float speed = 0.5f;
        float intensity = 0.8f;
        float wave = Mth.sin(ageInTicks * speed) * intensity;

        this.right_tentacle.zRot = wave * 0.5f;
        this.left_tentacle.zRot = -wave * 0.5f;

        this.tentacle_0.xRot = wave;
        this.tentacle_1.xRot = -wave;
        this.tentacle_2.zRot = wave;
        this.tentacle_3.zRot = -wave;
        this.tentacle_4.xRot = -wave;
        this.tentacle_5.xRot = wave;
        this.tentacle_6.zRot = wave;
        this.tentacle_7.zRot = -wave;
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}