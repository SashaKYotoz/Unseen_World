package net.sashakyotoz.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.custom.AureateVexEntity;

public class AureateVexModel<T extends AureateVexEntity> extends HierarchicalModel<T> implements ArmedModel {
    public static final ModelLayerLocation AUREATE_VEX = new ModelLayerLocation(UnseenWorld.MOD.makeID("aureate_vex"), "main");

    private static final float DEG_TO_RAD = (float) Math.PI / 180.0F;
    private static final float WING_Y_ROT_LEFT = -0.4712389F;
    private static final float WING_Y_ROT_RIGHT = 0.4712389F;

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart leftWing;
    private final ModelPart rightWing;

    public AureateVexModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.root = root;
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
        this.rightArm = this.body.getChild("rightArm");
        this.leftArm = this.body.getChild("leftArm");
        this.leftWing = this.body.getChild("leftWing");
        this.rightWing = this.body.getChild("rightWing");
    }

    public static LayerDefinition getModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 10).addBox(-1.5F, -4.0F, -1.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 17).addBox(-1.5F, -2.0F, -1.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        body.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-2.5F, -5.0F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(16, 16).addBox(-4.0F, -6.5F, 4.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

        PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create()
                .texOffs(23, 0).mirror().addBox(-1.25F, -0.5F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offset(-1.75F, -3.75F, 0.0F));

        rightArm.addOrReplaceChild("rightItem", CubeListBuilder.create(), PartPose.offset(-0.25F, 2.75F, 0.0F));

        body.addOrReplaceChild("leftArm", CubeListBuilder.create()
                .texOffs(23, 0).addBox(-0.75F, -0.5F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offset(1.75F, -3.75F, 0.0F));

        body.addOrReplaceChild("leftWing", CubeListBuilder.create()
                .texOffs(14, 26).mirror().addBox(0.0F, -1.0F, 0.0F, 9.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, -3.0F, 1.0F));

        body.addOrReplaceChild("rightWing", CubeListBuilder.create()
                .texOffs(14, 26).addBox(-9.0F, -1.0F, 0.0F, 9.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -3.0F, 1.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        if (entity.isCharging()) {
            this.body.xRot = 0.0F;
            this.setArmsCharging(entity.getMainHandItem(), entity.getOffhandItem(), Mth.cos(ageInTicks * 5.5F * DEG_TO_RAD) * 0.1F);
        } else {
            this.body.xRot = 0.15707964F;
        }

        this.head.yRot = netHeadYaw * DEG_TO_RAD;
        this.head.xRot = (headPitch * DEG_TO_RAD) - this.body.xRot;

        float armWave = Mth.cos(ageInTicks * 5.5F * DEG_TO_RAD) * 0.1F;
        if (!entity.isCharging()) {
            this.rightArm.zRot = ((float) Math.PI / 5.0F) + armWave;
            this.leftArm.zRot = -(((float) Math.PI / 5.0F) + armWave);
        }

        float wingFlap = Mth.cos(ageInTicks * 45.836624F * DEG_TO_RAD) * DEG_TO_RAD * 16.2F;
        this.leftWing.yRot = WING_Y_ROT_LEFT + wingFlap;
        this.rightWing.yRot = WING_Y_ROT_RIGHT - wingFlap;
        this.leftWing.xRot = 0.47123888F;
        this.leftWing.zRot = -0.47123888F;
        this.rightWing.xRot = 0.47123888F;
        this.rightWing.zRot = 0.47123888F;
    }

    private void setArmsCharging(ItemStack rightHandItem, ItemStack leftHandItem, float armWave) {
        if (rightHandItem.isEmpty() && leftHandItem.isEmpty()) {
            this.rightArm.xRot = -1.2217305F;
            this.rightArm.yRot = 0.2617994F;
            this.rightArm.zRot = -0.47123888F - armWave;
            this.leftArm.xRot = -1.2217305F;
            this.leftArm.yRot = -0.2617994F;
            this.leftArm.zRot = 0.47123888F + armWave;
        } else {
            if (!rightHandItem.isEmpty()) {
                this.rightArm.xRot = 3.6651914F;
                this.rightArm.yRot = 0.2617994F;
                this.rightArm.zRot = -0.47123888F - armWave;
            }
            if (!leftHandItem.isEmpty()) {
                this.leftArm.xRot = 3.6651914F;
                this.leftArm.yRot = -0.2617994F;
                this.leftArm.zRot = 0.47123888F + armWave;
            }
        }
    }

    @Override
    public void translateToHand(HumanoidArm side, PoseStack poseStack) {
        boolean isRight = side == HumanoidArm.RIGHT;
        ModelPart arm = isRight ? this.rightArm : this.leftArm;
        this.root.translateAndRotate(poseStack);
        this.body.translateAndRotate(poseStack);
        arm.translateAndRotate(poseStack);
        poseStack.scale(0.55F, 0.55F, 0.55F);
        this.offsetStackPosition(poseStack, isRight);
    }

    private void offsetStackPosition(PoseStack poseStack, boolean rightSide) {
        if (rightSide) {
            poseStack.translate(0.046875D, -0.15625D, 0.078125D);
        } else {
            poseStack.translate(-0.046875D, -0.15625D, 0.078125D);
        }
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}