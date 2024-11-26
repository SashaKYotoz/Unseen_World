package net.sashakyotoz.client.renderers.layers;

import net.minecraft.client.render.entity.feature.EnergySwirlOverlayFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.util.Identifier;
import net.sashakyotoz.client.models.EspyerModel;
import net.sashakyotoz.common.entities.custom.EspyerEntity;

public class EspyerChargeFeatureRenderer extends EnergySwirlOverlayFeatureRenderer<EspyerEntity, EspyerModel> {
    private static final Identifier SKIN = Identifier.of("minecraft","textures/entity/creeper/creeper_armor.png");
    private final EspyerModel model;

    public EspyerChargeFeatureRenderer(FeatureRendererContext<EspyerEntity, EspyerModel> context, EntityModelLoader loader) {
        super(context);
        this.model = new EspyerModel(loader.getModelPart(EspyerModel.ESPYER));
    }

    @Override
    protected float getEnergySwirlX(float partialAge) {
        return partialAge * 0.01F;
    }

    @Override
    protected Identifier getEnergySwirlTexture() {
        return SKIN;
    }

    @Override
    protected EntityModel<EspyerEntity> getEnergySwirlModel() {
        return this.model;
    }
}