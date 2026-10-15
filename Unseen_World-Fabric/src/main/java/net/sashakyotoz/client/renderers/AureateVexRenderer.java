package net.sashakyotoz.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.AureateVexModel;
import net.sashakyotoz.client.renderers.layers.GlowingPartsLayer;
import net.sashakyotoz.common.entities.custom.AureateVexEntity;

public class AureateVexRenderer extends MobRenderer<AureateVexEntity, AureateVexModel<AureateVexEntity>> {
    private final ResourceLocation VEX = UnseenWorld.MOD.makeID("textures/entity/aureate_vex/aureate_vex.png");

    public AureateVexRenderer(EntityRendererProvider.Context context) {
        super(context, new AureateVexModel<>(context.bakeLayer(AureateVexModel.AUREATE_VEX)), 0.5f);
        this.addLayer(new GlowingPartsLayer<>(this,
                UnseenWorld.MOD.makeID("textures/entity/aureate_vex/aureate_vex_glowing_parts.png"),
                (entity, tickDelta, progress) -> entity.isCharging() ? 1.0f : 0.25f
        ));
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(AureateVexEntity entity) {
        return VEX;
    }
}
