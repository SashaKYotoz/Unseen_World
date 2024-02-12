package net.sashakyotoz.unseenworld.client.model;

import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.ArmedModel;

import net.sashakyotoz.unseenworld.entity.RedSlylfEntity;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class ModelRed_Sylph<T extends RedSlylfEntity> extends HierarchicalModel<RedSlylfEntity> implements ArmedModel {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("unseen_world", "model_red_sylph"), "main");
    public final ModelPart head;
    public final ModelPart root;
    public final ModelPart right_arm;
    public final ModelPart left_arm;
    public final ModelPart rightWing;
    public final ModelPart leftWing;
    public final ModelPart bb_main;

    public ModelRed_Sylph(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.right_arm = root.getChild("rightArm");
        this.left_arm = root.getChild("leftArm");
        this.rightWing = root.getChild("rightWing");
        this.leftWing = root.getChild("leftWing");
        this.bb_main = root.getChild("body");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 5).addBox(-4.0F, -15.0F, -6.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 21.0F, 0.0F));

        PartDefinition rightArm = partdefinition.addOrReplaceChild("rightArm", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, 15.0F, -3.0F, 0.9599F, -0.0873F, 0.4363F));

        PartDefinition cube_r1 = rightArm.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 21).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.25F))
                .texOffs(0, 21).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5236F, 0.0F, 0.0873F));

        PartDefinition leftArm = partdefinition.addOrReplaceChild("leftArm", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, 15.0F, -3.0F, 0.9599F, -0.0873F, -0.4363F));

        PartDefinition cube_r2 = leftArm.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 21).addBox(0.0F, -1.0F, -1.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.25F))
                .texOffs(0, 21).addBox(0.0F, -1.0F, -1.0F, 1.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5236F, 0.0F, -0.0873F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 5.0F, 0.0F));

        PartDefinition cube_r3 = body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 31).addBox(-3.0F, 7.0F, -7.0F, 6.0F, 10.0F, 6.0F, new CubeDeformation(0.15F))
                .texOffs(32, 5).addBox(-3.0F, 7.0F, -7.0F, 6.0F, 10.0F, 6.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition rightWing = partdefinition.addOrReplaceChild("rightWing", CubeListBuilder.create(), PartPose.offset(-1.0F, 15.0F, 0.0F));

        PartDefinition cube_r4 = rightWing.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(50, 26).mirror().addBox(-2.0F, -1.0F, 2.0F, 7.0F, 13.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.3927F, -0.2618F, 1.5708F));

        PartDefinition leftWing = partdefinition.addOrReplaceChild("leftWing", CubeListBuilder.create(), PartPose.offset(1.0F, 15.0F, 0.0F));

        PartDefinition cube_r5 = leftWing.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(50, 26).addBox(-5.0F, -1.0F, 2.0F, 7.0F, 13.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.3927F, 0.2618F, -1.5708F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        rightWing.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftWing.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    public void setupAnim(RedSlylfEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
        this.head.xRot = headPitch / (180F / (float) Math.PI);
        this.right_arm.xRot = Mth.cos(limbSwing * 0.5F + (float) Math.PI) * limbSwingAmount;
        this.left_arm.xRot = Mth.cos(limbSwing * 0.5F) * limbSwingAmount;
        this.leftWing.yRot = Mth.cos(limbSwing * 0.75F) * limbSwingAmount;
        this.rightWing.yRot = Mth.cos(limbSwing * 0.75F + (float) Math.PI) * limbSwingAmount;
    }

    @Override
    public void translateToHand(HumanoidArm arm, PoseStack stack) {
        this.root.translateAndRotate(stack);
        this.bb_main.translateAndRotate(stack);
        stack.mulPose(Axis.XP.rotation(this.right_arm.xRot));
        stack.scale(0.7F, 0.7F, 0.7F);
        stack.translate(-0.2F, 0.8F, -0.3F);
    }
}
