package net.sashakyotoz.unseenworld.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.client.model.ModelSnowdrifter;
import net.sashakyotoz.unseenworld.entity.SnowdrifterEntity;

public class SnowdrifterRenderer extends MobRenderer<SnowdrifterEntity, ModelSnowdrifter<SnowdrifterEntity>> {
    public SnowdrifterRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelSnowdrifter<>(context.bakeLayer(ModelSnowdrifter.LAYER_LOCATION)),1f);
    }

//    @Override
//    public void render(SnowdrifterEntity entity,float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
//        super.render(entity, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
//    }

    @Override
    protected void scale(SnowdrifterEntity entity, PoseStack stack, float p_115316_) {
        stack.translate(0,entity.isUnderSnow() ? 1 : 0,0);
        super.scale(entity, stack, p_115316_);
    }

    @Override
    public ResourceLocation getTextureLocation(SnowdrifterEntity entity) {
        return new ResourceLocation(UnseenWorldMod.MODID,"textures/entities/snowdrifter.png");
    }
}
