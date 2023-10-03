
package net.sashakyotoz.unseenworld.client.renderer;

import net.sashakyotoz.unseenworld.client.model.ModelCavern_Scarecrow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;

import net.sashakyotoz.unseenworld.entity.CavernScarecrowEntity;

public class CavernScarecrowRenderer extends MobRenderer<CavernScarecrowEntity, ModelCavern_Scarecrow<CavernScarecrowEntity>> {
	public CavernScarecrowRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelCavern_Scarecrow(context.bakeLayer(ModelCavern_Scarecrow.LAYER_LOCATION)), 0.5f);
		this.addLayer(new EyesLayer<CavernScarecrowEntity, ModelCavern_Scarecrow<CavernScarecrowEntity>>(this) {
			@Override
			public RenderType renderType() {
				return RenderType.eyes(new ResourceLocation("unseen_world:textures/entities/cavern_scarecrow.png"));
			}
		});
	}

	@Override
	public ResourceLocation getTextureLocation(CavernScarecrowEntity entity) {
		return new ResourceLocation("unseen_world:textures/entities/cavern_scarecrow" + entity.texture + ".png");
	}
}
