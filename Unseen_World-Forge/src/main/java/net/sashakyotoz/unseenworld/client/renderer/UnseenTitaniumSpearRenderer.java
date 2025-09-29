package net.sashakyotoz.unseenworld.client.renderer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.unseenworld.client.model.ModelTealivy_Void_Spear;
import net.sashakyotoz.unseenworld.entity.UnseenTitaniumSpearEntity;

public class UnseenTitaniumSpearRenderer extends SpearRenderer<UnseenTitaniumSpearEntity> {
	private static final ResourceLocation texture = new ResourceLocation("unseen_world:textures/entities/titaniumy_unseen_spear.png");
	private final ModelTealivy_Void_Spear model;

	public UnseenTitaniumSpearRenderer(EntityRendererProvider.Context context) {
		super(context);
		model = new ModelTealivy_Void_Spear(context.bakeLayer(ModelTealivy_Void_Spear.LAYER_LOCATION));
	}

    @Override
    public EntityModel<UnseenTitaniumSpearEntity> spearModel() {
        return this.model;
    }

	@Override
	public ResourceLocation getTextureLocation(UnseenTitaniumSpearEntity entity) {
		return texture;
	}
}