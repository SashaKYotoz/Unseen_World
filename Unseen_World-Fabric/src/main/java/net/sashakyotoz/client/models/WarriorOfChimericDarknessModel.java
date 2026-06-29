package net.sashakyotoz.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.bosses.WarriorOfChimericDarkness;

public class WarriorOfChimericDarknessModel extends HierarchicalModel<WarriorOfChimericDarkness> {
    public static final ModelLayerLocation WARRIOR_OF_CHIMERIC_DARKNESS = new ModelLayerLocation(UnseenWorld.makeID("warrior_of_chimeric_darkness"), "main");
    private final ModelPart root;
    private final ModelPart head;

    public WarriorOfChimericDarknessModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.root = root;
        ModelPart body = this.root.getChild("body");
        ModelPart chest = body.getChild("chest");
        this.head = chest.getChild("head");
    }

    public static LayerDefinition getTextureLocationdModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition body = modelPartData.addOrReplaceChild("body", CubeListBuilder.create().texOffs(34, 0).addBox(-6.5F, -5.0F, -3.0F, 13.0F, 7.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(34, 13).addBox(-6.5F, -5.0F, -3.0F, 13.0F, 4.0F, 6.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 10.0F, 0.0F));

        PartDefinition chest = body.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(0, 23).addBox(-12.5F, -15.0F, -5.5F, 25.0F, 16.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 0.0F));

        PartDefinition head = chest.addOrReplaceChild("head", CubeListBuilder.create().texOffs(13, 0).addBox(-1.9F, -12.0F, -5.6F, 4.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(122, 0).addBox(-2.9F, -14.0F, -4.6F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(122, 0).mirror().addBox(0.1F, -14.0F, -4.6F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -15.0F, 0.0F));

        PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(100, 0).addBox(-4.0941F, -3.0F, -4.0F, 7.0F, 3.0F, 7.0F, new CubeDeformation(0.325F))
                .texOffs(100, 10).addBox(-4.0941F, 0.5F, -4.0F, 7.0F, 10.0F, 7.0F, new CubeDeformation(0.325F))
                .texOffs(72, 0).addBox(-4.0941F, 0.5F, -4.0F, 7.0F, 10.0F, 7.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0941F, -10.5F, 0.641F, 0.0F, -0.7854F, 0.0F));

        PartDefinition right_arm = chest.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(28, 50).addBox(-7.5F, 4.0F, -3.5F, 6.0F, 15.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-11.0F, -14.0F, 0.0F));

        PartDefinition cube_r2 = right_arm.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(74, 52).addBox(-9.0F, -5.0F, -7.0F, 13.0F, 11.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

        PartDefinition hammer = right_arm.addOrReplaceChild("hammer", CubeListBuilder.create(), PartPose.offset(-4.5F, 11.0F, -5.0F));

        PartDefinition pick = hammer.addOrReplaceChild("pick", CubeListBuilder.create().texOffs(1, 115).addBox(-3.0F, -10.0F, -4.0F, 6.0F, 6.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 98).mirror().addBox(-4.0F, -18.0F, -5.0F, 8.0F, 8.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 98).mirror().addBox(-4.0F, -4.0F, -5.0F, 8.0F, 8.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 15.0F, -18.0F));

        PartDefinition stick = hammer.addOrReplaceChild("stick", CubeListBuilder.create().texOffs(27, 115).addBox(0.0F, 0.0F, -23.0F, 0.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 11.0F));

        PartDefinition cube_r3 = stick.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(25, 98).addBox(-0.5F, -0.95F, -1.05F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -16.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r4 = stick.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(31, 98).addBox(-0.5F, -0.85F, -1.15F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -17.25F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r5 = stick.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 115).addBox(0.0F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, -20.25F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r6 = stick.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 98).addBox(-0.5F, -0.925F, -1.075F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -18.75F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r7 = stick.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 98).addBox(-0.5F, -0.95F, -1.05F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -21.5F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r8 = stick.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 98).addBox(-0.5F, -0.925F, -1.075F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -24.25F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r9 = stick.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 115).addBox(-0.5F, -0.85F, -1.15F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -14.5F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r10 = stick.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 98).addBox(-0.5F, -0.95F, -1.05F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -13.25F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r11 = stick.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 115).addBox(0.0F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, -12.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r12 = stick.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 98).addBox(-0.5F, -0.925F, -1.075F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -10.5F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r13 = stick.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 115).addBox(-0.5F, -0.85F, -1.15F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -9.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r14 = stick.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 98).addBox(-0.5F, -0.95F, -1.05F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r15 = stick.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(0, 115).addBox(-0.5F, -0.85F, -1.15F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -0.75F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r16 = stick.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(0, 98).addBox(-0.5F, -0.95F, -1.05F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.25F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r17 = stick.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(0, 115).addBox(-0.5F, -0.85F, -1.15F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -3.5F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r18 = stick.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(0, 98).addBox(-0.5F, -0.925F, -1.075F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -5.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r19 = stick.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(0, 115).addBox(0.0F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, -6.5F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r20 = stick.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(0, 98).addBox(-0.5F, -0.95F, -1.05F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -7.75F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r21 = stick.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(0, 115).addBox(-0.5F, -0.85F, -1.15F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -22.75F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r22 = stick.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(0, 102).addBox(0.0F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, -25.75F, -0.7854F, 0.0F, 0.0F));

        PartDefinition left_arm = chest.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(28, 50).mirror().addBox(1.5F, 4.0F, -3.5F, 6.0F, 15.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(11.0F, -14.0F, 0.0F));

        PartDefinition cube_r23 = left_arm.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(88, 19).addBox(1.5F, -6.0F, -1.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.25F, -4.0F, 4.75F, -0.2618F, 0.0F, -0.0873F));

        PartDefinition cube_r24 = left_arm.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(88, 19).mirror().addBox(-1.5F, -4.25F, -1.75F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(7.0F, -5.0F, 2.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition cube_r25 = left_arm.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(88, 19).mirror().addBox(-1.5F, -4.25F, -1.75F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(88, 19).addBox(-1.5F, -4.25F, -8.75F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -6.0F, 4.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition cube_r26 = left_arm.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(88, 19).mirror().addBox(1.5F, -6.0F, -2.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.25F, -4.0F, -4.75F, 0.2618F, 0.0F, -0.0873F));

        PartDefinition cube_r27 = left_arm.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(72, 17).mirror().addBox(0.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.0F, -12.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

        PartDefinition cube_r28 = left_arm.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(74, 27).mirror().addBox(-4.0F, -5.0F, -7.0F, 13.0F, 11.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

        PartDefinition shield = left_arm.addOrReplaceChild("shield", CubeListBuilder.create().texOffs(54, 50).mirror().addBox(1.0F, 9.0F, -5.0F, 6.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(68, 82).addBox(7.0F, 2.0F, -14.0F, 2.0F, 18.0F, 28.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 6.0F, 1.0F));

        PartDefinition cube_r29 = shield.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(102, 104).mirror().addBox(-2.0F, -1.5F, 0.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(116, 102).addBox(-5.0F, -18.5F, 0.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(102, 104).mirror().addBox(-2.0F, -18.5F, 0.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(116, 102).addBox(-5.0F, -3.5F, 0.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.5F, 19.5F, -10.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r30 = shield.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(116, 102).mirror().addBox(-5.0F, -1.5F, 0.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(116, 102).mirror().addBox(-5.0F, -16.5F, 0.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(9.5F, 17.5F, 17.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r31 = shield.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(102, 104).addBox(-2.0F, -1.5F, 0.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(102, 104).addBox(-2.0F, -18.5F, 0.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.5F, 19.5F, 12.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition right_leg = modelPartData.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 50).addBox(-4.0F, -2.0F, -3.5F, 7.0F, 16.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.5F, 10.0F, 0.0F));

        PartDefinition left_leg = modelPartData.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 50).mirror().addBox(-3.0F, -2.0F, -3.5F, 7.0F, 16.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.5F, 10.0F, 0.0F));
        return LayerDefinition.create(modelData, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        root.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(WarriorOfChimericDarkness entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = headYaw / (180F / (float) Math.PI);
        this.head.xRot = headPitch / (180F / (float) Math.PI);
    }
}