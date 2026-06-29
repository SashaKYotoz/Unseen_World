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
import net.sashakyotoz.common.entities.animations.VexalBeetleAnimations;
import net.sashakyotoz.common.entities.custom.VexalBeetleEntity;

public class VexalBeetleModel extends HierarchicalModel<VexalBeetleEntity> {
    public static final ModelLayerLocation VEXAL_BEETLE = new ModelLayerLocation(UnseenWorld.makeID("vexal_beetle"), "main");

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart left_claw;
    private final ModelPart right_claw;
    private final ModelPart body;
    private final ModelPart left_wing;
    private final ModelPart wing1;
    private final ModelPart right_wing;
    private final ModelPart wing;
    private final ModelPart right_leg_b;
    private final ModelPart left_leg_b;
    private final ModelPart right_leg_f;
    private final ModelPart left_leg_f;

    public VexalBeetleModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.left_claw = this.head.getChild("left_claw");
        this.right_claw = this.head.getChild("right_claw");
        this.body = root.getChild("body");
        this.left_wing = this.body.getChild("left_wing");
        this.wing1 = this.left_wing.getChild("wing1");
        this.right_wing = this.body.getChild("right_wing");
        this.wing = this.right_wing.getChild("wing");
        this.right_leg_b = root.getChild("right_leg_b");
        this.left_leg_b = root.getChild("left_leg_b");
        this.right_leg_f = root.getChild("right_leg_f");
        this.left_leg_f = root.getChild("left_leg_f");
    }

    public static LayerDefinition getTextureLocationdModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition head = modelPartData.addOrReplaceChild("head", CubeListBuilder.create().texOffs(32, 4).addBox(-4.0F, -3.0F, -8.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.0F, -3.0F));

        PartDefinition left_claw = head.addOrReplaceChild("left_claw", CubeListBuilder.create().texOffs(-8, 29).mirror().addBox(-4.0F, 0.0F, -8.0F, 6.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(4.0F, 1.0F, -7.0F));

        PartDefinition right_claw = head.addOrReplaceChild("right_claw", CubeListBuilder.create().texOffs(-8, 29).addBox(-2.0F, 0.0F, -8.0F, 6.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 1.0F, -7.0F));

        PartDefinition body = modelPartData.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 12).addBox(-5.0F, -3.0F, 3.0F, 10.0F, 5.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.0F, 0.0F));

        PartDefinition left_wing = body.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(0, 52).addBox(-1.0F, -4.0F, -3.0F, 15.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -2.0F, 0.0F, 0.1309F, -0.3054F, -0.0873F));

        PartDefinition wing1 = left_wing.addOrReplaceChild("wing1", CubeListBuilder.create(), PartPose.offset(1.0F, -2.0F, -2.0F));

        PartDefinition cube_r1 = wing1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(-7, 45).addBox(-1.0F, -4.0F, -1.0F, 15.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 4.0F, 0.0F, 0.0F, -0.6109F, 0.0F));

        PartDefinition right_wing = body.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 52).mirror().addBox(-14.0F, -4.0F, -3.0F, 15.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -2.0F, 0.0F, 0.1309F, 0.3054F, 0.0873F));

        PartDefinition wing = right_wing.addOrReplaceChild("wing", CubeListBuilder.create(), PartPose.offset(-1.0F, -1.0F, -2.0F));

        PartDefinition cube_r2 = wing.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(-7, 45).mirror().addBox(-14.0F, -4.0F, -1.0F, 15.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 3.0F, 0.0F, 0.0F, 0.6109F, 0.0F));

        PartDefinition right_leg_b = modelPartData.addOrReplaceChild("right_leg_b", CubeListBuilder.create().texOffs(34, 29).addBox(-11.0F, -1.0F, -2.0F, 12.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 17.0F, 2.0F, 0.0F, 0.5236F, -0.6981F));

        PartDefinition left_leg_b = modelPartData.addOrReplaceChild("left_leg_b", CubeListBuilder.create().texOffs(34, 29).mirror().addBox(-1.0F, -1.0F, -2.0F, 12.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, 17.0F, 2.0F, 0.0F, -0.5236F, 0.6981F));

        PartDefinition right_leg_f = modelPartData.addOrReplaceChild("right_leg_f", CubeListBuilder.create().texOffs(34, 29).addBox(-11.0F, -1.0F, -1.0F, 12.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 17.0F, -1.0F, 0.0F, -0.5236F, -0.6981F));

        PartDefinition left_leg_f = modelPartData.addOrReplaceChild("left_leg_f", CubeListBuilder.create().texOffs(34, 29).mirror().addBox(-1.0F, -1.0F, -1.0F, 12.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, 17.0F, -1.0F, 0.0F, 0.5236F, 0.6981F));
        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public void setupAnim(VexalBeetleEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        if (entity.getTarget() != null) {
            this.left_claw.yRot = -0.5f * Mth.sin(limbSwing);
            this.right_claw.yRot = 0.5f * Mth.sin(limbSwing);
        }
        if (entity.onGround()) {
            float i = -(Mth.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.4F) * limbSwingAmount;
            float l = -(Mth.cos(limbSwing * 0.6662F * 2.0F + (float) (Math.PI * 3.0 / 2.0)) * 0.4F) * limbSwingAmount;
            float m = Math.abs(Mth.sin(limbSwing * 0.6662F + 0.0F) * 0.4F) * limbSwingAmount;
            float p = Math.abs(Mth.sin(limbSwing * 0.6662F + (float) (Math.PI * 3.0 / 2.0)) * 0.4F) * limbSwingAmount;
            this.right_leg_b.yRot += i;
            this.left_leg_b.yRot += -i;
            this.right_leg_f.yRot += l;
            this.left_leg_f.yRot += -l;
            this.right_leg_b.zRot += m;
            this.left_leg_b.zRot += -m;
            this.right_leg_f.zRot += p;
            this.left_leg_f.zRot += -p;
        }

        float f = Math.min((float) entity.getDeltaMovement().lengthSqr() * 200.0F, 2F);

        this.animate(entity.flying, VexalBeetleAnimations.FLYING, ageInTicks, f);
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        right_leg_b.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        left_leg_b.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        right_leg_f.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        left_leg_f.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}