
package net.sashakyotoz.unseenworld.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.unseenworld.client.model.ModelTheBlazer;
import net.sashakyotoz.unseenworld.client.model.ModelTheBlazerShieldUp;
import net.sashakyotoz.unseenworld.entity.TheBlazerEntity;

public class TheBlazerRenderer extends MobRenderer<TheBlazerEntity, EntityModel<TheBlazerEntity>> {
	public static EntityModel<TheBlazerEntity> ordinary;
	public static EntityModel<TheBlazerEntity> advanced;
	private static final ResourceLocation THE_BLAZER_LOCATION = new ResourceLocation("unseen_world:textures/entities/the_blazer.png");
	private static final ResourceLocation THE_BLAZER_BLOCKED_LOCATION = new ResourceLocation("unseen_world:textures/entities/the_blazer_is_blocking.png");

	public TheBlazerRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelTheBlazer(context.bakeLayer(ModelTheBlazer.LAYER_LOCATION)), 0.5f);
		ordinary = new ModelTheBlazer(context.bakeLayer(ModelTheBlazer.LAYER_LOCATION));
		advanced = new ModelTheBlazerShieldUp(context.bakeLayer(ModelTheBlazerShieldUp.LAYER_LOCATION));
	}
	public void render(TheBlazerEntity entity, float p_115778_, float p_115779_, PoseStack p_115780_, MultiBufferSource p_115781_, int p_115782_) {
		this.model = blazerModel(entity);
		super.render(entity, p_115778_, p_115779_, p_115780_, p_115781_, p_115782_);
	}
	public ResourceLocation getTextureLocation(TheBlazerEntity entity) {
		return entity.isBlocked() ? THE_BLAZER_BLOCKED_LOCATION : THE_BLAZER_LOCATION;
	}
	public EntityModel<TheBlazerEntity> blazerModel(TheBlazerEntity entity) {
		return entity.isBlocked() ? ordinary : advanced;
	}
}
