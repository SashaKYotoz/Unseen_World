package net.sashakyotoz.unseenworld.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.unseenworld.entity.SnowdrifterEntity;
import net.sashakyotoz.unseenworld.entity.animations.SnowdrifterAnimations;

public class ModelSnowdrifter<T extends SnowdrifterEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("unseen_world", "snowdrifter_model"), "main");
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart root;

    public ModelSnowdrifter(ModelPart root) {
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.root = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(96, 0).addBox(-3.5F, -4.0F, -9.0F, 7.0F, 6.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.0F, -8.0F));

        PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, -8).addBox(0.0F, 0.0F, -4.0F, 0.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -3.0F, -4.0F, 0.0F, 0.0F, 0.0873F));

        PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, -8).addBox(0.0F, 0.0F, -4.0F, 0.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, -3.0F, -4.0F, 0.0F, 0.0F, -0.0873F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -8.0F, -8.0F, 10.0F, 10.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 26).addBox(-5.0F, -8.0F, -8.0F, 10.0F, 11.0F, 16.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 16.0F, 0.0F));

        PartDefinition rightForwardPaw = body.addOrReplaceChild("rightForwardPaw", CubeListBuilder.create().texOffs(108, 34).mirror().addBox(-1.0F, 0.0F, -1.0F, 5.0F, 12.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.0F, -4.0F, -6.0F));

        PartDefinition rightBackPaw = body.addOrReplaceChild("rightBackPaw", CubeListBuilder.create().texOffs(108, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 5.0F, 12.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.0F, -4.0F, 3.0F));

        PartDefinition leftForwardPaw = body.addOrReplaceChild("leftForwardPaw", CubeListBuilder.create().texOffs(108, 34).addBox(-4.0F, 0.0F, -1.0F, 5.0F, 12.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -4.0F, -6.0F));

        PartDefinition leftBackPaw = body.addOrReplaceChild("leftBackPaw", CubeListBuilder.create().texOffs(108, 16).addBox(-4.0F, 0.0F, -1.0F, 5.0F, 12.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -4.0F, 3.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(52, 0).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 7.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(52, 0).mirror().addBox(-3.0F, -2.0F, 0.0F, 6.0F, 7.0F, 13.0F, new CubeDeformation(-0.25F)).mirror(false), PartPose.offset(0.0F, -2.0F, 8.0F));

        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
        this.head.xRot = headPitch / (180F / (float) Math.PI);
        this.animateWalk(SnowdrifterAnimations.WALK,limbSwing,limbSwingAmount,2.0F, 2.5F);
        this.animate(entity.attack,SnowdrifterAnimations.ATTACK,ageInTicks);
        this.animate(entity.digging,SnowdrifterAnimations.DIGGING,ageInTicks);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
