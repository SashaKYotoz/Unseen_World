package net.sashakyotoz.unseenworld.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.unseenworld.UnseenWorldMod;

public class ModelBeaconOfWeapons extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(UnseenWorldMod.MODID, "model_beacon_of_weapons"), "main");
    public final ModelPart base;
    public final ModelPart top;
    public final ModelPart beacon;

    public ModelBeaconOfWeapons(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.base = root.getChild("base");
        this.top = root.getChild("top");
        this.beacon = root.getChild("beacon");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, -8.0F, 0.0F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 24.0F, -8.0F));

        PartDefinition top = partdefinition.addOrReplaceChild("top", CubeListBuilder.create().texOffs(-16, 48).addBox(-8.0F, -2.98F, -8.0F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 38).mirror().addBox(-7.0F, -3.0F, 6.98F, 14.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 38).addBox(-7.0F, -3.0F, -6.98F, 14.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 24).addBox(-6.98F, -3.0F, -7.0F, 0.0F, 4.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 24).addBox(6.98F, -3.0F, -7.0F, 0.0F, 4.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

        PartDefinition beacon = partdefinition.addOrReplaceChild("beacon", CubeListBuilder.create().texOffs(96, 44).addBox(-8.0F, -16.0F, -8.0F, 0.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(96, 44).addBox(8.0F, -16.0F, -8.0F, 0.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(96, 60).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(96, 60).addBox(-8.0F, -16.0F, 8.0F, 16.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(16, 48).addBox(-8.0F, -15.98F, -8.0F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(24, 40).addBox(-4.0F, -14.0F, -4.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 25.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 64);
    }
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        base.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        top.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        beacon.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
