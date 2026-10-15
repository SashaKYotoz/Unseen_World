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
import net.sashakyotoz.common.entities.custom.basic.WhaleEntity;

public class GloomwhaleModel<T extends WhaleEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation GLOOMWHALE = new ModelLayerLocation(UnseenWorld.MOD.makeID("gloomwhale"), "main");
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart jaw;
    private final ModelPart tail;
    private final ModelPart left_fin;
    private final ModelPart right_fin;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart tail4;

    public GloomwhaleModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.jaw = this.head.getChild("jaw");
        this.tail = this.head.getChild("tail");
        this.left_fin = this.tail.getChild("left_fin");
        this.right_fin = this.tail.getChild("right_fin");
        this.tail1 = this.tail.getChild("tail1");
        this.tail2 = this.tail1.getChild("tail2");
        this.tail3 = this.tail2.getChild("tail3");
        this.tail4 = this.tail3.getChild("tail4");
    }

    public static LayerDefinition getModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition head = modelPartData.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 160).addBox(-21.0F, -18.0F, -60.0F, 42.0F, 32.0F, 64.0F, new CubeDeformation(0.0F))
                .texOffs(0, 7).addBox(18.0F, -13.0F, -61.0F, 0.0F, 12.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(2, 6).addBox(-20.0F, -18.0F, -62.0F, 0.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 16.0F));

        head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(-6, 2).mirror().addBox(0.0F, 0.0F, -5.0F, 5.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(24.0F, -11.0F, -3.0F, -2.3562F, 0.0F, 3.1416F));
        head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(-8, 0).mirror().addBox(-6.0F, 0.0F, -4.0F, 6.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-20.0F, -4.0F, -24.0F, -1.5708F, 0.3927F, 0.0F));
        head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(-8, 0).mirror().addBox(-6.0F, 0.0F, -4.0F, 6.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(21.0F, -4.0F, -22.0F, -1.5708F, 0.3927F, 3.1416F));
        head.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(-8, 0).addBox(0.0F, 0.0F, -5.0F, 6.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(21.0F, 8.0F, -3.0F, 0.0F, 0.0F, -0.3927F));
        head.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(88, 0).mirror().addBox(-6.0F, -12.0F, 0.0F, 10.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-15.0F, -17.0F, -55.0F, 0.3927F, 0.7854F, 0.0F));
        head.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(88, 0).mirror().addBox(-6.0F, -12.0F, 0.0F, 12.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(15.0F, -15.0F, -19.0F, -0.3927F, 0.0F, 0.0F));
        head.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(88, 0).addBox(-6.0F, -12.0F, 0.0F, 12.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.0F, -16.0F, -33.0F, -0.3927F, 0.3927F, 0.0F));
        head.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(88, 0).addBox(-6.0F, -12.0F, 0.0F, 12.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -18.0F, -60.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition crystal_g = head.addOrReplaceChild("crystal_g", CubeListBuilder.create(), PartPose.offset(5.0F, -16.0F, -51.5F));
        PartDefinition crystal = crystal_g.addOrReplaceChild("crystal", CubeListBuilder.create().texOffs(8, 58).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0873F, -0.2182F));
        crystal.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 61).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        crystal.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 58).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition crystal1 = crystal_g.addOrReplaceChild("crystal1", CubeListBuilder.create().texOffs(8, 58).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, -0.1309F, 0.0F, 0.2618F));
        crystal1.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 61).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        crystal1.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 58).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition crystal2 = crystal_g.addOrReplaceChild("crystal2", CubeListBuilder.create().texOffs(8, 58).mirror().addBox(-2.0F, -8.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(11.0F, 0.0F, 6.0F, 0.1309F, 0.0F, -0.2618F));
        crystal2.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 61).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        crystal2.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 58).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition crystal3 = crystal_g.addOrReplaceChild("crystal3", CubeListBuilder.create().texOffs(8, 58).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.0F, 2.0F, -6.0F, -0.2618F, -0.0873F, 0.2182F));
        crystal3.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(0, 61).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        crystal3.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(0, 58).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition crystal_g1 = head.addOrReplaceChild("crystal_g1", CubeListBuilder.create(), PartPose.offsetAndRotation(12.0F, -15.0F, -51.5F, 0.0F, -1.5708F, 0.0F));
        PartDefinition crystal4 = crystal_g1.addOrReplaceChild("crystal4", CubeListBuilder.create().texOffs(8, 58).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0873F, -0.2182F));
        crystal4.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(0, 61).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        crystal4.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(0, 58).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition crystal5 = crystal_g1.addOrReplaceChild("crystal5", CubeListBuilder.create().texOffs(8, 58).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, -0.1309F, 0.0F, 0.2618F));
        crystal5.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(0, 61).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        crystal5.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(0, 58).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition crystal6 = crystal_g1.addOrReplaceChild("crystal6", CubeListBuilder.create().texOffs(8, 58).mirror().addBox(-2.0F, -8.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(11.0F, 0.0F, 6.0F, 0.1309F, 0.0F, -0.2618F));
        crystal6.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(0, 61).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        crystal6.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(0, 58).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition crystal7 = crystal_g1.addOrReplaceChild("crystal7", CubeListBuilder.create().texOffs(8, 58).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.0F, 2.0F, -6.0F, -0.2618F, -0.0873F, 0.2182F));
        crystal7.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(0, 61).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        crystal7.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(0, 58).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition crystal_g2 = head.addOrReplaceChild("crystal_g2", CubeListBuilder.create(), PartPose.offset(-23.0F, -16.0F, -55.5F));
        PartDefinition crystal8 = crystal_g2.addOrReplaceChild("crystal8", CubeListBuilder.create().texOffs(8, 58).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 2.0F, -2.0F, 0.4878F, 0.2482F, -0.5186F));
        crystal8.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(0, 61).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        crystal8.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(0, 58).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition crystal9 = crystal_g2.addOrReplaceChild("crystal9", CubeListBuilder.create().texOffs(8, 58).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 1.0F, 7.0F, -0.1309F, 0.0F, 0.2618F));
        crystal9.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(0, 61).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        crystal9.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(0, 58).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition crystal10 = crystal_g2.addOrReplaceChild("crystal10", CubeListBuilder.create().texOffs(8, 58).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 2.0F, 0.0F, 1.0663F, -0.253F, 0.3624F));
        crystal10.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(0, 61).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        crystal10.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(0, 58).addBox(-2.0F, -3.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(0, 95).addBox(-15.0F, 0.0F, -58.0F, 30.0F, 6.0F, 58.0F, new CubeDeformation(0.02F))
                .texOffs(176, 100).addBox(-14.0F, -1.0F, -58.0F, 28.0F, 1.0F, 58.0F, new CubeDeformation(0.0F))
                .texOffs(212, 192).addBox(-15.0F, 6.0F, -58.0F, 30.0F, 6.0F, 58.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.0F, 4.0F));

        PartDefinition tail = head.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, -12.0F, 4.0F, 32.0F, 34.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

        tail.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(88, 0).addBox(-7.0F, -11.0F, 0.0F, 12.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0F, -12.0F, 14.0F, -0.3927F, 0.0F, 0.0F));
        tail.addOrReplaceChild("left_fin", CubeListBuilder.create().texOffs(364, 232).addBox(0.0F, 0.0F, -8.0F, 32.0F, 0.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.0F, 8.0F, 12.0F, 0.0F, 0.0F, 0.48F));
        tail.addOrReplaceChild("right_fin", CubeListBuilder.create().texOffs(364, 232).mirror().addBox(-32.0F, 0.0F, -8.0F, 32.0F, 0.0F, 24.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-16.0F, 8.0F, 12.0F, 0.0F, 0.0F, -0.7418F));

        PartDefinition tail1 = tail.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(112, 0).addBox(-13.0F, -10.0F, 6.0F, 26.0F, 31.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 22.0F));
        tail1.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(94, 3).addBox(-7.0F, -5.0F, 0.0F, 6.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, -3.0F, 19.0F, -0.3927F, 0.0F, -1.5708F));

        PartDefinition tail2 = tail1.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(208, 0).addBox(-11.0F, -3.0F, 4.0F, 22.0F, 25.0F, 25.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 24.0F));
        PartDefinition tail3 = tail2.addOrReplaceChild("tail3", CubeListBuilder.create().texOffs(302, 0).addBox(-7.0F, -8.0F, 4.0F, 14.0F, 17.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, 25.0F));
        tail3.addOrReplaceChild("tail4", CubeListBuilder.create().texOffs(356, 200).addBox(-21.0F, 0.0F, -5.0F, 42.0F, 0.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 18.0F));

        return LayerDefinition.create(modelData, 512, 256);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float animationProgress, float headYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        boolean hasTarget = entity.hasSyncedTarget();

        this.head.xRot = headPitch * ((float) Math.PI / 180.0F);
        this.head.yRot = headYaw * ((float) Math.PI / 180.0F);

        float waveFreq = 0.3F;
        float waveAmp = 0.35F * limbSwingAmount;

        this.tail.xRot = Mth.cos(limbSwing * waveFreq) * waveAmp * 0.3F;
        this.tail1.xRot = Mth.cos(limbSwing * waveFreq - 0.6F) * waveAmp * 0.5F;
        this.tail2.xRot = Mth.cos(limbSwing * waveFreq - 1.2F) * waveAmp * 0.7F;
        this.tail3.xRot = Mth.cos(limbSwing * waveFreq - 1.8F) * waveAmp * 0.9F;
        this.tail4.xRot = Mth.cos(limbSwing * waveFreq - 2.4F) * waveAmp * 1.1F;

        float finSwing = Mth.cos(limbSwing * waveFreq * 0.5F) * limbSwingAmount;
        this.right_fin.zRot = 0.25F + finSwing * 0.4F;
        this.left_fin.zRot = -0.25F - finSwing * 0.4F;
        this.right_fin.yRot = 0.15F + finSwing * 0.2F;
        this.left_fin.yRot = -0.15F - finSwing * 0.2F;

        if (hasTarget) {
            float minDistance = 3.0F;
            float maxDistance = 16.0F;
            float currentDistance = Mth.clamp((float) Math.sqrt(entity.getSyncedTargetDistanceSqr()), minDistance, maxDistance);
            float proximityFactor = 1.0F - ((currentDistance - minDistance) / (maxDistance - minDistance));
            this.head.xRot += -0.5f * proximityFactor;
            float baseAngle = proximityFactor * 0.5235988F;
            float swingOffset = Mth.cos(animationProgress * 0.25F) * 0.05235988F;

            this.jaw.xRot = baseAngle + swingOffset;
        } else if (!entity.onGround()) {
            this.jaw.xRot = Mth.cos(animationProgress * 0.1F) * 0.025F;
        } else {
            this.jaw.xRot = 0.0F;
        }
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.root.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}