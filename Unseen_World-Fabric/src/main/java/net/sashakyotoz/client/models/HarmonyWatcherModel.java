package net.sashakyotoz.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.config.ModMainConfig;
import net.sashakyotoz.common.entities.animations.HarmonyWatcherAnimations;
import net.sashakyotoz.common.entities.custom.HarmonyWatcherEntity;

public class HarmonyWatcherModel extends HierarchicalModel<HarmonyWatcherEntity> implements ArmedModel {
    public static final ModelLayerLocation HARMONY_WATCHER = new ModelLayerLocation(UnseenWorld.makeID("harmony_watcher"), "main");

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart eye;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;

    public HarmonyWatcherModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.eye = head.getChild("eye");
        this.body = root.getChild("body");
        this.rightArm = root.getChild("rightArm");
        this.leftArm = root.getChild("leftArm");
    }

    public static LayerDefinition getTextureLocationdModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition head = modelPartData.addOrReplaceChild("head", CubeListBuilder.create().texOffs(24, 48).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(44, 54).addBox(-4.0F, -6.0F, -4.0F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 43).addBox(-4.0F, -6.0F, 1.0F, 8.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 52).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(32, 54).addBox(3.0F, -6.0F, -4.0F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 38).addBox(-4.0F, -5.0F, 0.0F, 8.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, -4.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition eye = head.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -3.5F, -2.5F));

        PartDefinition body = modelPartData.addOrReplaceChild("body", CubeListBuilder.create().texOffs(86, 47).addBox(-6.0F, -2.0F, -4.0F, 12.0F, 8.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(66, 44).addBox(-6.0F, 4.0F, -5.0F, 12.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(66, 41).addBox(-6.0F, -2.0F, -5.0F, 12.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).addBox(-6.0F, 0.0F, -5.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).mirror().addBox(2.0F, 0.0F, -5.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition middleStone = body.addOrReplaceChild("middleStone", CubeListBuilder.create().texOffs(92, 36).addBox(-5.0F, -2.0F, -4.0F, 10.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 11.0F, 0.0F));

        PartDefinition bottomStone = body.addOrReplaceChild("bottomStone", CubeListBuilder.create().texOffs(104, 29).addBox(-3.0F, -0.5F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.5F, 0.0F));

        PartDefinition rightArm = modelPartData.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(96, 0).mirror().addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(116, 0).mirror().addBox(-8.0F, -4.0F, 0.0F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-10.0F, 8.0F, 0.0F));

        PartDefinition leftArm = modelPartData.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(96, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(116, 0).addBox(2.0F, -4.0F, 0.0F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(10.0F, 8.0F, 0.0F));
        return LayerDefinition.create(modelData, 128, 64);
    }

    @Override
    public void setupAnim(HarmonyWatcherEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.eye.yRot = netHeadYaw * (float) (Math.PI / 180.0);
        this.animateWalk(entity.isAngry ? HarmonyWatcherAnimations.WALK_WHEN_ANGRY : HarmonyWatcherAnimations.WALK, limbSwing, limbSwingAmount, 3.0f, 3.0f);
        this.animate(entity.fertilize, HarmonyWatcherAnimations.FERTILIZE, ageInTicks);
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        rightArm.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        leftArm.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void translateToHand(HumanoidArm arm, PoseStack matrices) {
        matrices.translate(0.0F, 0.0625F, 0.1875F);
        matrices.mulPose(Axis.XP.rotation(this.leftArm.xRot));
        matrices.mulPose(Axis.YP.rotation(this.leftArm.yRot));
        matrices.mulPose(Axis.ZP.rotation(this.leftArm.zRot));
        matrices.scale(0.7F, 0.7F, 0.7F);
    }
}