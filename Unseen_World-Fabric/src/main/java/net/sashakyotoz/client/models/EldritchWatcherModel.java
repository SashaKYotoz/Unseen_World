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
import net.sashakyotoz.client.environment.ClientTicks;
import net.sashakyotoz.common.config.ModMainConfig;
import net.sashakyotoz.common.entities.animations.EldritchWatcherAnimations;
import net.sashakyotoz.common.entities.custom.EldritchWatcherEntity;
import net.sashakyotoz.utils.Oscillator;

public class EldritchWatcherModel extends HierarchicalModel<EldritchWatcherEntity> {
    public static final ModelLayerLocation ELDRITCH_WATCHER = new ModelLayerLocation(UnseenWorld.makeID("eldritch_watcher"), "main");
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart right_arm;
    private final ModelPart left_arm;
    private final ModelPart head;
    private final ModelPart right_leg;
    private final ModelPart left_leg;

    public EldritchWatcherModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.root = root.getChild("root");
        this.body = this.root.getChild("body");
        this.right_arm = this.body.getChild("right_arm");
        this.left_arm = this.body.getChild("left_arm");
        this.head = this.body.getChild("head");
        this.right_leg = this.root.getChild("right_leg");
        this.left_leg = this.root.getChild("left_leg");
    }

    public static LayerDefinition getTextureLocationdModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition root = modelPartData.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 28).addBox(-6.0F, -12.0F, -4.0F, 12.0F, 20.0F, 8.0F, new CubeDeformation(0.3F))
                .texOffs(0, 0).addBox(-6.0F, -12.0F, -4.0F, 12.0F, 20.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, -0.75F, 0.0436F, 0.0F, 0.0F));

        PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-6.0F, -4.0F, 0.0F));

        PartDefinition cube_r2 = right_arm.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(61, 32).addBox(-4.0F, 13.0F, -5.0F, 5.0F, 12.0F, 6.0F, new CubeDeformation(0.35F)), PartPose.offsetAndRotation(-0.25F, -5.0F, 1.0F, 0.0F, 0.0F, 0.0436F));

        PartDefinition cube_r3 = right_arm.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(61, 32).mirror().addBox(-4.0F, 13.0F, -5.0F, 5.0F, 12.0F, 6.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offsetAndRotation(-0.25F, -8.0F, 1.0F, 0.0F, 0.0F, 0.0436F));

        PartDefinition cube_r4 = right_arm.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(40, 32).mirror().addBox(-4.0F, -5.0F, -2.0F, 5.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0436F));

        PartDefinition cube_r5 = right_arm.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(84, 24).mirror().addBox(-4.0F, -5.0F, -5.0F, 5.0F, 30.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0436F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(6.0F, -4.0F, 0.0F));

        PartDefinition cube_r6 = left_arm.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(40, 32).addBox(-1.0F, -5.0F, -2.0F, 5.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, 13.0F, 1.0F, 0.0F, 0.0F, -0.0436F));

        PartDefinition cube_r7 = left_arm.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(61, 32).mirror().addBox(-1.0F, 13.0F, -5.0F, 5.0F, 12.0F, 6.0F, new CubeDeformation(0.35F)).mirror(false), PartPose.offsetAndRotation(0.25F, -15.0F, 1.0F, 0.0F, 0.0F, -0.0436F));

        PartDefinition cube_r8 = left_arm.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(61, 32).addBox(-1.0F, 13.0F, -5.0F, 5.0F, 12.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.25F, -18.0F, 1.0F, 0.0F, 0.0F, -0.0436F));

        PartDefinition cube_r9 = left_arm.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(106, 24).addBox(-1.0F, -5.0F, -5.0F, 5.0F, 30.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 1.0F, 0.0F, 0.0F, -0.0436F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(40, 0).addBox(-5.0F, -24.0F, -3.0F, 10.0F, 24.0F, 8.0F, new CubeDeformation(0.001F))
                .texOffs(40, 0).mirror().addBox(-5.0F, -24.0F, -3.0F, 10.0F, 9.0F, 8.0F, new CubeDeformation(0.015F)).mirror(false), PartPose.offset(0.0F, -8.0F, -2.0F));

        PartDefinition cube_r10 = head.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(40, 44).addBox(-3.0F, -4.0F, -3.0F, 4.0F, 12.0F, 8.0F, new CubeDeformation(0.1F))
                .texOffs(76, 0).addBox(-3.0F, -4.0F, -3.0F, 4.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5F, -17.0F, 0.0F, 0.0F, -0.2618F, 0.0F));

        PartDefinition cube_r11 = head.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(76, 0).mirror().addBox(-1.0F, -4.0F, -4.0F, 4.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.5F, -17.0F, 1.0F, 0.0F, 0.2618F, 0.0F));

        PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(106, 0).addBox(-3.0F, -2.0F, -3.0F, 5.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 12.0F, 0.0F));

        PartDefinition right_feet = right_leg.addOrReplaceChild("right_feet", CubeListBuilder.create().texOffs(106, 0).addBox(-2.0F, 0.0F, -3.0F, 5.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 8.0F, 0.0F));

        PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(106, 0).mirror().addBox(-2.0F, -2.0F, -3.0F, 5.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(3.0F, 12.0F, 0.0F));

        PartDefinition left_feet = left_leg.addOrReplaceChild("left_feet", CubeListBuilder.create().texOffs(106, 0).mirror().addBox(-3.0F, 0.0F, -3.0F, 5.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, 8.0F, 0.0F));
        return LayerDefinition.create(modelData, 128, 64);
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
    public void setupAnim(EldritchWatcherEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animateWalk(EldritchWatcherAnimations.WALK, limbSwing, limbSwingAmount, 2, 2);
        if (!entity.isDeadOrDying())
            this.body.zRot = (float) Oscillator.getOscillatingWithNegativeValue(ClientTicks.getTicks()) * 0.35f;
        this.animate(entity.attack, EldritchWatcherAnimations.ATTACK, ageInTicks);
        if (ModMainConfig.doAdvancedDeathForMobs)
            this.animate(entity.death, EldritchWatcherAnimations.DEATH, ageInTicks);
        if (entity.isCarringBlock()) {
            this.right_arm.xRot = -0.5F;
            this.right_arm.zRot = 0.05F;
        }
    }
}