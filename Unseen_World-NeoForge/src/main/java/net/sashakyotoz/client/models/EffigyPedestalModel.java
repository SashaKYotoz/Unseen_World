package net.sashakyotoz.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.sashakyotoz.UnseenWorld;

public class EffigyPedestalModel {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(UnseenWorld.MOD.makeID("effigy_pedestal"), "main");
    public static final int TEXTURE_WIDTH = 256;
    public static final int TEXTURE_HEIGHT = 256;
    private final ModelPart root;

    public EffigyPedestalModel(ModelPart root) {
        this.root = root;
    }

    public static LayerDefinition getModelData() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition pedestal = root.addOrReplaceChild("pedestal", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        pedestal.addOrReplaceChild("plinth", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-24.0F, -4.0F, -24.0F, 48.0F, 4.0F, 48.0F), PartPose.ZERO);
        pedestal.addOrReplaceChild("step", CubeListBuilder.create()
                .texOffs(0, 52).addBox(-22.0F, -6.0F, -22.0F, 44.0F, 2.0F, 44.0F), PartPose.ZERO);
        pedestal.addOrReplaceChild("core", CubeListBuilder.create()
                .texOffs(0, 98).addBox(-18.0F, -12.0F, -18.0F, 36.0F, 6.0F, 36.0F), PartPose.ZERO);
        pedestal.addOrReplaceChild("cap", CubeListBuilder.create()
                .texOffs(0, 140).addBox(-21.0F, -16.0F, -21.0F, 42.0F, 4.0F, 42.0F), PartPose.ZERO);
        return LayerDefinition.create(mesh, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        this.root.render(poseStack, consumer, light, overlay);
    }
}
