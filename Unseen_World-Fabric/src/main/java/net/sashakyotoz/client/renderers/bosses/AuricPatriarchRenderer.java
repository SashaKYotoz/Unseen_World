package net.sashakyotoz.client.renderers.bosses;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.AuricPatriarchModel;
import net.sashakyotoz.client.renderers.layers.GlowingPartsLayer;
import net.sashakyotoz.common.entities.bosses.AuricPatriarch;

public class AuricPatriarchRenderer extends BossMobRenderer<AuricPatriarch, AuricPatriarchModel> {
    private final ResourceLocation PATRIARCH = UnseenWorld.MOD.makeID("textures/entity/auric_patriarch/auric_patriarch.png");

    public AuricPatriarchRenderer(EntityRendererProvider.Context context) {
        super(context, new AuricPatriarchModel(context.bakeLayer(AuricPatriarchModel.AURIC_PATRIARCH)), 0.5f);
        this.addLayer(new GlowingPartsLayer<>(this,
                UnseenWorld.MOD.makeID("textures/entity/auric_patriarch/auric_patriarch_glowing_parts.png"),
                (entity, tickDelta, progress) -> entity.alphaIntensity()
        ));
    }

    @Override
    public ResourceLocation getTextureLocation(AuricPatriarch entity) {
        return PATRIARCH;
    }
}