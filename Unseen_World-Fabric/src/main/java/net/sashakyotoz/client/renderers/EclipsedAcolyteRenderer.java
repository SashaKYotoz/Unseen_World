package net.sashakyotoz.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.EclipsedAcolyteModel;
import net.sashakyotoz.client.renderers.layers.GlowingPartsLayer;
import net.sashakyotoz.common.entities.custom.EclipsedAcolyteEntity;

public class EclipsedAcolyteRenderer extends MobRenderer<EclipsedAcolyteEntity, EclipsedAcolyteModel<EclipsedAcolyteEntity>> {
    private static final ResourceLocation TEXTURE = UnseenWorld.MOD.makeID("textures/entity/eclipsed_acolyte/eclipsed_acolyte.png");
    private static final ResourceLocation GLOWING_PARTS = UnseenWorld.MOD.makeID("textures/entity/eclipsed_acolyte/eclipsed_acolyte_glowing_parts.png");

    public EclipsedAcolyteRenderer(EntityRendererProvider.Context context) {
        super(context, new EclipsedAcolyteModel<>(context.bakeLayer(EclipsedAcolyteModel.ECLIPSED_ACOLYTE)), 0.6F);
        this.addLayer(new GlowingPartsLayer<>(this, GLOWING_PARTS, (entity, tickDelta, progress) -> 1));
    }

    @Override
    public ResourceLocation getTextureLocation(EclipsedAcolyteEntity entity) {
        return TEXTURE;
    }
}
