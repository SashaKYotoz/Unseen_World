package net.sashakyotoz.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.UmbralSerpentModel;
import net.sashakyotoz.common.entities.custom.UmbralSerpentEntity;

public class UmbralSerpentRenderer extends MobRenderer<UmbralSerpentEntity, UmbralSerpentModel<UmbralSerpentEntity>> {
    private static final ResourceLocation TEXTURE = UnseenWorld.MOD.makeID("textures/entity/umbral_serpent/umbral_serpent.png");
    private static final ResourceLocation SADDLED_TEXTURE = UnseenWorld.MOD.makeID("textures/entity/umbral_serpent/umbral_serpent_saddled.png");

    public UmbralSerpentRenderer(EntityRendererProvider.Context context) {
        super(context, new UmbralSerpentModel<>(context.bakeLayer(UmbralSerpentModel.UMBRAL_SERPENT)), 0.6F);
    }

    @Override
    protected void scale(UmbralSerpentEntity livingEntity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(1.2F, 1.2F, 1.2F);
    }

    @Override
    public ResourceLocation getTextureLocation(UmbralSerpentEntity entity) {
        return entity.isSaddled() ? SADDLED_TEXTURE : TEXTURE;
    }
}
