package net.sashakyotoz.client.models;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.entities.animations.RimehornAnimations;
import net.sashakyotoz.common.entities.custom.RimehornEntity;

public class RimehornModel<T extends RimehornEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation RIMEHORN = new ModelLayerLocation(UnseenWorld.MOD.makeID("rimehorn"), "main");
    private static final float BABY_HEAD_SCALE = 1.6F;

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart horns;

    public RimehornModel(ModelPart root) {
        this.root = root.getChild("root");
        this.head = this.root.getChild("head");
        this.horns = this.head.getChild("horns");
    }

    public static LayerDefinition getModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -1.0F));

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(52, -6).addBox(0.0F, 5.0F, -6.0F, 0.0F, 8.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(15, 33).addBox(-7.5F, 0.0F, -2.0F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(15, 33).mirror().addBox(2.5F, 0.0F, -2.0F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -18.0F, -9.0F));

        head.addOrReplaceChild("horns", CubeListBuilder.create()
                .texOffs(0, 37).addBox(-8.0F, -3.0F, -4.0F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(27, 33).addBox(-8.0F, -7.0F, -3.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 37).mirror().addBox(3.0F, -3.0F, -4.0F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(27, 33).mirror().addBox(6.0F, -7.0F, -3.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.ZERO);

        head.addOrReplaceChild("Head_r1", CubeListBuilder.create().texOffs(0, 45).addBox(-4.0F, -5.0F, -9.0F, 7.0F, 8.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -1.0F, 1.0F, 0.9599F, 0.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(62, 0).addBox(-6.5F, -5.0F, -11.0F, 13.0F, 14.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -19.0F, 2.0F));

        body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(62, 25).addBox(-6.0F, -20.0F, -6.0F, 12.0F, 9.0F, 10.0F, new CubeDeformation(0.4F))
                .texOffs(18, 4).addBox(-6.0F, -29.0F, -6.0F, 12.0F, 18.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 19.0F, 1.5708F, 0.0F, 0.0F));

        root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(4.0F, -12.0F, -6.0F));

        root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -12.0F, -6.0F));

        root.addOrReplaceChild("left_leg1", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(4.0F, -12.0F, 7.0F));

        root.addOrReplaceChild("right_leg1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -12.0F, 7.0F));

        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        boolean busy = entity.graze.isStarted() || entity.charge.isStarted();
        if (!busy) {
            this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
            this.head.xRot = headPitch * Mth.DEG_TO_RAD;
        }
        this.horns.visible = !this.young;
        if (this.young) {
            this.head.xScale = BABY_HEAD_SCALE;
            this.head.yScale = BABY_HEAD_SCALE;
            this.head.zScale = BABY_HEAD_SCALE;
        }
        if (!busy) {
            this.animateWalk(RimehornAnimations.WALK, limbSwing, limbSwingAmount, 4.0F, 4.0F);
        }
        this.animate(entity.graze, RimehornAnimations.GRAZE, ageInTicks);
        this.animate(entity.charge, RimehornAnimations.CHARGE, ageInTicks);
    }
}