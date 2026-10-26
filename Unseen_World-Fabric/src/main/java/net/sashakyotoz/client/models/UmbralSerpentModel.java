package net.sashakyotoz.client.models;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.animations.UmbralSerpentAnimations;
import net.sashakyotoz.common.entities.custom.UmbralSerpentEntity;

public class UmbralSerpentModel<T extends UmbralSerpentEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation UMBRAL_SERPENT = new ModelLayerLocation(UnseenWorld.MOD.makeID("umbral_serpent"), "main");
    private static final float HEAD_YAW_LIMIT = 35.0F;
    private static final float HEAD_PITCH_LIMIT = 25.0F;

    private final ModelPart root;
    private final ModelPart head;

    public UmbralSerpentModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
    }

    public static LayerDefinition getModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(78, 0).addBox(-5.0F, -4.0F, 0.0F, 10.0F, 8.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(78, 63).mirror().addBox(-5.0F, -4.0F, 0.0F, 10.0F, 6.0F, 15.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offset(0.0F, 15.0F, -7.0F));

        body.addOrReplaceChild("front_mustache_body", CubeListBuilder.create().texOffs(122, 24).mirror().addBox(1.0F, -4.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(122, 24).addBox(-4.0F, -4.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 3.0F));

        body.addOrReplaceChild("behind_mustache_body", CubeListBuilder.create().texOffs(122, 24).mirror().addBox(0.0F, -4.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(122, 24).addBox(-3.0F, -4.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 13.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -4.0F, -7.0F, 10.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, -7.0F));

        head.addOrReplaceChild("right_mustache_head", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -4.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -4.0F, -7.0F));

        head.addOrReplaceChild("left_mustache_head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -4.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -4.0F, -7.0F));

        head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -4.0F, 0.0F, 3.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 0.0F, -3.0F));

        head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-3.0F, -4.0F, 0.0F, 3.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.0F, 0.0F, -3.0F));

        PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 115).addBox(-4.0F, -3.0F, 0.0F, 8.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.0F, 8.0F));

        tail.addOrReplaceChild("front_mustache_tail", CubeListBuilder.create().texOffs(0, 118).addBox(-4.0F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 118).addBox(1.0F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 5.0F));

        PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(0, 103).addBox(-3.0F, -2.0F, 0.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 7.0F));

        tail2.addOrReplaceChild("behind_mustache_tail", CubeListBuilder.create().texOffs(6, 99).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 6.0F));

        partdefinition.addOrReplaceChild("leg_front_left", CubeListBuilder.create().texOffs(115, 33).addBox(-1.0F, 0.0F, -1.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(111, 42).addBox(-2.0F, 5.0F, -2.0F, 5.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 19.0F, -5.0F));

        partdefinition.addOrReplaceChild("leg_front_right", CubeListBuilder.create().texOffs(115, 33).mirror().addBox(-2.0F, 0.0F, -1.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(111, 42).mirror().addBox(-3.0F, 5.0F, -2.0F, 5.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, 19.0F, -5.0F));

        partdefinition.addOrReplaceChild("leg_behind_left", CubeListBuilder.create().texOffs(115, 51).addBox(-1.0F, 0.0F, -1.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(111, 60).addBox(-2.0F, 5.0F, -2.0F, 5.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 19.0F, 6.0F));

        partdefinition.addOrReplaceChild("leg_behind_right", CubeListBuilder.create().texOffs(115, 51).mirror().addBox(-2.0F, 0.0F, -1.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(111, 60).mirror().addBox(-3.0F, 5.0F, -2.0F, 5.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, 19.0F, 6.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        if (!entity.isInSittingPose()) {
            this.head.yRot = Mth.clamp(netHeadYaw, -HEAD_YAW_LIMIT, HEAD_YAW_LIMIT) * Mth.DEG_TO_RAD;
            this.head.xRot = Mth.clamp(headPitch, -HEAD_PITCH_LIMIT, HEAD_PITCH_LIMIT) * Mth.DEG_TO_RAD;
        }
        if (entity.isInWaterOrBubble()) {
            this.animateWalk(UmbralSerpentAnimations.SWIM, limbSwing, limbSwingAmount, 2.0F, 2.5F);
        } else if (!entity.isInSittingPose()) {
            this.animateWalk(UmbralSerpentAnimations.WALK, limbSwing, limbSwingAmount, 3.0F, 3.0F);
        }
        this.animate(entity.landIdle, UmbralSerpentAnimations.LAND_IDLE, ageInTicks);
        this.animate(entity.waterIdle, UmbralSerpentAnimations.WATER_IDLE, ageInTicks);
        this.animate(entity.sit, UmbralSerpentAnimations.SIT, ageInTicks);
        this.animate(entity.attack, UmbralSerpentAnimations.ATTACK, ageInTicks);
    }
}