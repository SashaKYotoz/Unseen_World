package net.sashakyotoz.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.DreadflapModel;
import net.sashakyotoz.common.entities.custom.DreadflapEntity;

public class DreadflapRenderer extends MobRenderer<DreadflapEntity, DreadflapModel> {

    public DreadflapRenderer(EntityRendererProvider.Context context) {
        super(context, new DreadflapModel(context.bakeLayer(DreadflapModel.DREADFLAP)), 0.5F);
    }
    @Override
    protected void setupRotations(DreadflapEntity entity, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks, float scale) {
        if (entity.isRoosting())
            poseStack.translate(0.0F, -0.1F, 0.0F);
        else
            poseStack.translate(0.0F, Mth.cos(ageInTicks * 0.3F) * 0.1F, 0.0F);

        super.setupRotations(entity, poseStack, ageInTicks, rotationYaw, partialTicks, scale);
    }

    @Override
    protected void scale(DreadflapEntity livingEntity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(0.75F, 0.75F, 0.75F);
    }

    public ResourceLocation getTextureLocation(DreadflapEntity tuskhog) {
        return UnseenWorld.MOD.makeID("textures/entity/dreadflap/dreadflap.png");
    }
}