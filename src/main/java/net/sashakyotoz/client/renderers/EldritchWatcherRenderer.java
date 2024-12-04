package net.sashakyotoz.client.renderers;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.EldritchWatcherModel;
import net.sashakyotoz.client.renderers.layers.EldritchWatcherBlockFeatureRenderer;
import net.sashakyotoz.client.renderers.layers.EldritchWatcherPulsatingLayer;
import net.sashakyotoz.common.entities.custom.EldritchWatcherEntity;

public class EldritchWatcherRenderer extends DeathFixedMobRenderer<EldritchWatcherEntity, EldritchWatcherModel> {
    public EldritchWatcherRenderer(EntityRendererFactory.Context context) {
        super(context, new EldritchWatcherModel(context.getPart(EldritchWatcherModel.ELDRITCH_WATCHER)), 0.5f);
        this.addFeature(new EldritchWatcherBlockFeatureRenderer(this, context.getBlockRenderManager()));
        this.addFeature(new EldritchWatcherPulsatingLayer<>(this));
    }

    @Override
    protected boolean isShaking(EldritchWatcherEntity entity) {
        return super.isShaking(entity) || entity.isConverting();
    }

    @Override
    public Identifier getTexture(EldritchWatcherEntity entity) {
        return UnseenWorld.makeID("textures/entity/eldritch_watcher/eldritch_watcher.png");
    }
}