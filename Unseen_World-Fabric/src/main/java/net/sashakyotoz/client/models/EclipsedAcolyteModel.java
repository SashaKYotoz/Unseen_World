package net.sashakyotoz.client.models;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.animations.EclipsedAcolyteAnimations;
import net.sashakyotoz.common.entities.custom.EclipsedAcolyteEntity;

public class EclipsedAcolyteModel<T extends EclipsedAcolyteEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation ECLIPSED_ACOLYTE = new ModelLayerLocation(UnseenWorld.MOD.makeID("eclipsed_acolyte"), "main");
    private final ModelPart root;
    private final ModelPart head;

    public EclipsedAcolyteModel(ModelPart root) {
        this.root = root.getChild("root");
        this.head = this.root.getChild("waist").getChild("head");
    }

    public static LayerDefinition getModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition waist = root.addOrReplaceChild("waist", CubeListBuilder.create(), PartPose.offset(0.0F, -19.0F, 0.0F));

        waist.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -3.0F, 8.0F, 10.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(28, 31).addBox(-3.0F, -13.0F, -3.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(12, 40).addBox(3.0F, -13.0F, 0.0F, 9.0F, 14.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(34, 12).addBox(-12.0F, -14.0F, 0.0F, 9.0F, 14.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 0.0F));

        PartDefinition body = waist.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-6.0F, -3.0F, -3.0F, 6.0F, 13.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(28, 0).mirror().addBox(0.0F, -3.0F, -3.0F, 6.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -7.0F, 1.0F));

        body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(86, 0).mirror().addBox(-3.0F, -1.0F, -1.5F, 3.0F, 16.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(52, 12).mirror().addBox(-3.0F, -5.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(110, 0).mirror().addBox(-4.0F, 2.0F, -2.5F, 4.0F, 14.0F, 5.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offset(-6.0F, -2.0F, -0.5F));

        body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(98, 0).mirror().addBox(-1.0F, -1.5F, -1.5F, 3.0F, 21.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(110, 0).addBox(-1.0F, 6.5F, -2.5F, 4.0F, 14.0F, 5.0F, new CubeDeformation(0.1F)), PartPose.offset(7.0F, -2.5F, 0.5F));

        root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(104, 25).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 16.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.5F, -16.0F, 0.5F));

        root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(116, 25).mirror().addBox(0.0F, -1.5F, -1.5F, 3.0F, 19.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(12, 34).mirror().addBox(0.0F, -4.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -17.5F, 0.5F));

        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        boolean busy = entity.isDormant() || entity.awaken.isStarted() || entity.slam.isStarted() || entity.sweep.isStarted();
        this.animateWalk(EclipsedAcolyteAnimations.WALK, limbSwing, limbSwingAmount, 2.0F, 2.5F);
        if (!busy) {
            this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
            this.head.xRot = headPitch * Mth.DEG_TO_RAD;
        }
        this.animate(entity.idle, EclipsedAcolyteAnimations.IDLE, ageInTicks);
        this.animate(entity.dormant, EclipsedAcolyteAnimations.DORMANT, ageInTicks);
        this.animate(entity.awaken, EclipsedAcolyteAnimations.AWAKEN, ageInTicks);
        this.animate(entity.slam, EclipsedAcolyteAnimations.SLAM, ageInTicks);
        this.animate(entity.sweep, EclipsedAcolyteAnimations.SWEEP, ageInTicks);
    }
}