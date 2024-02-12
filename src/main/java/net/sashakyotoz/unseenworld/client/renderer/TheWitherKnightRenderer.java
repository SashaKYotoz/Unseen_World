
package net.sashakyotoz.unseenworld.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.sashakyotoz.unseenworld.client.model.ModelThe_Wither_Knight;
import net.sashakyotoz.unseenworld.client.model.ModelThe_Wither_Knight_Advanced;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.sashakyotoz.unseenworld.client.model.WitherArmedModel;
import net.sashakyotoz.unseenworld.entity.TheWitherKnightEntity;

public class TheWitherKnightRenderer extends MobRenderer<TheWitherKnightEntity, WitherArmedModel<TheWitherKnightEntity>> {
	private final WitherArmedModel<TheWitherKnightEntity> ordinary;
	private final WitherArmedModel<TheWitherKnightEntity> advanced;
	private static final ResourceLocation THE_WITHER_KNIGHT = new ResourceLocation("unseen_world:textures/entities/the_wither_knight.png");
	private static final ResourceLocation THE_WITHER_KNIGHT_ADVANCED = new ResourceLocation("unseen_world:textures/entities/the_wither_knight_advanced.png");
	public TheWitherKnightRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelThe_Wither_Knight<>(context.bakeLayer(ModelThe_Wither_Knight.LAYER_LOCATION)), 0.5f);
		this.ordinary = new ModelThe_Wither_Knight<>(context.bakeLayer(ModelThe_Wither_Knight.LAYER_LOCATION));
		this.advanced = new ModelThe_Wither_Knight_Advanced(context.bakeLayer(ModelThe_Wither_Knight_Advanced.LAYER_LOCATION));
		this.addLayer(new ItemInHandLayer<>(this,context.getItemInHandRenderer()));
	}
	public void render(TheWitherKnightEntity entity, float p_115778_, float p_115779_, PoseStack p_115780_, MultiBufferSource p_115781_, int p_115782_) {
		if (entity.isAdvanced())
			this.model = this.advanced;
		else {
			this.model = this.ordinary;
		}
		super.render(entity, p_115778_, p_115779_, p_115780_, p_115781_, p_115782_);
	}

	@Override
	public ResourceLocation getTextureLocation(TheWitherKnightEntity entity) {
		return entity.isAdvanced() ? THE_WITHER_KNIGHT_ADVANCED : THE_WITHER_KNIGHT;
	}
}
