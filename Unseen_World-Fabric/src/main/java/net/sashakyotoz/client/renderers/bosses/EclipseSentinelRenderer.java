package net.sashakyotoz.client.renderers.bosses;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.EclipseSentinelModel;
import net.sashakyotoz.client.renderers.layers.GlowingPartsLayer;
import net.sashakyotoz.common.entities.bosses.EclipseSentinel;

public class EclipseSentinelRenderer extends BossMobRenderer<EclipseSentinel, EclipseSentinelModel> {
    private final ResourceLocation SENTINEL = UnseenWorld.MOD.makeID("textures/entity/eclipse_sentinel/eclipse_sentinel.png");

    public EclipseSentinelRenderer(EntityRendererProvider.Context context) {
        super(context, new EclipseSentinelModel(context.bakeLayer(EclipseSentinelModel.ECLIPSE_SENTINEL)), 0.5f);
        this.addLayer(new GlowingPartsLayer<>(this,
                UnseenWorld.MOD.makeID("textures/entity/eclipse_sentinel/eclipse_sentinel_glowing_parts.png"),
                (entity, tickDelta, progress) -> entity.alphaIntensity()
        ));
    }

    @Override
    public ResourceLocation getTextureLocation(EclipseSentinel entity) {
        return SENTINEL;
    }
}