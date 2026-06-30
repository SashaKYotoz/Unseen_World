package net.sashakyotoz.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.config.ModMainConfig;
import net.sashakyotoz.common.entities.animations.GleamcarverAnimations;
import net.sashakyotoz.common.entities.custom.GleamcarverEntity;

public class GleamcarverModel extends HierarchicalModel<GleamcarverEntity> {
    public static final ModelLayerLocation GLEAMCARVER = new ModelLayerLocation(UnseenWorld.makeID("gleamcarver"), "main");

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart forwardRightPaw;
    private final ModelPart forwardLeftPaw;
    private final ModelPart middleRightPaw;
    private final ModelPart middleLeftPaw;
    private final ModelPart backRightPaw;
    private final ModelPart backLeftPaw;

    public GleamcarverModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.forwardRightPaw = root.getChild("forwardRightPaw");
        this.forwardLeftPaw = root.getChild("forwardLeftPaw");
        this.middleRightPaw = root.getChild("middleRightPaw");
        this.middleLeftPaw = root.getChild("middleLeftPaw");
        this.backRightPaw = root.getChild("backRightPaw");
        this.backLeftPaw = root.getChild("backLeftPaw");
    }

    public static LayerDefinition getTextureLocationdModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition head = modelPartData.addOrReplaceChild("head", CubeListBuilder.create().texOffs(50, 0).addBox(-2.5F, -1.0F, -1.5F, 5.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.0F, -5.0F));

        PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(30, -2).addBox(0.0F, -2.0F, -1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -1.0F, -0.5F, 0.0F, -0.1745F, 0.0F));

        PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(30, -2).addBox(0.0F, -2.0F, -1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -1.0F, -0.5F, 0.0F, 0.1745F, 0.0F));

        PartDefinition cube_r3 = head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(32, 0).addBox(-1.5F, 0.1F, -1.0F, 3.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, -1.75F, 0.1745F, 0.0F, 0.0F));

        PartDefinition body = modelPartData.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -5.0F, -4.5F, 8.0F, 5.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(40, -3).addBox(0.0F, -4.0F, 4.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.0F, 0.0F));

        PartDefinition cube_r4 = body.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(32, 16).addBox(-8.0F, -16.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition cube_r5 = body.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 24).addBox(-8.0F, -8.0F, 0.0F, 16.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0436F, 0.0F));

        PartDefinition cube_r6 = body.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(32, 16).mirror().addBox(-8.0F, -16.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition forwardRightPaw = modelPartData.addOrReplaceChild("forwardRightPaw", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.5F, -3.0F, 0.0F, -0.2618F, 0.0F));

        PartDefinition cube_r7 = forwardRightPaw.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(52, 8).addBox(-4.0F, 1.0F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -3.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition forwardLeftPaw = modelPartData.addOrReplaceChild("forwardLeftPaw", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 23.5F, -3.0F, 0.0F, 0.2618F, 0.0F));

        PartDefinition cube_r8 = forwardLeftPaw.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(52, 5).addBox(0.0F, 1.0F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition middleRightPaw = modelPartData.addOrReplaceChild("middleRightPaw", CubeListBuilder.create(), PartPose.offset(0.0F, 24.5F, 0.0F));

        PartDefinition cube_r9 = middleRightPaw.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(52, 8).addBox(-4.0F, 1.0F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -3.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition middleLeftPaw = modelPartData.addOrReplaceChild("middleLeftPaw", CubeListBuilder.create(), PartPose.offset(0.0F, 23.5F, 0.0F));

        PartDefinition cube_r10 = middleLeftPaw.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(52, 5).addBox(0.0F, 1.0F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition backRightPaw = modelPartData.addOrReplaceChild("backRightPaw", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.5F, 3.0F, 0.0F, 0.2618F, 0.0F));

        PartDefinition cube_r11 = backRightPaw.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(52, 8).addBox(-4.0F, 1.0F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -3.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition backLeftPaw = modelPartData.addOrReplaceChild("backLeftPaw", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 23.5F, 3.0F, 0.0F, -0.2618F, 0.0F));

        PartDefinition cube_r12 = backLeftPaw.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(52, 5).addBox(0.0F, 1.0F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.2618F));
        return LayerDefinition.create(modelData, 64, 32);
    }

    @Override
    public void setupAnim(GleamcarverEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animateWalk(GleamcarverAnimations.WALK, limbSwing, limbSwingAmount, 4.0F, 2.5F);
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        forwardRightPaw.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        forwardLeftPaw.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        middleRightPaw.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        middleLeftPaw.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        backRightPaw.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        backLeftPaw.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}