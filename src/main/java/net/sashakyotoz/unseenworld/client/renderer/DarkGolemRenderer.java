
package net.sashakyotoz.unseenworld.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Pose;
import net.sashakyotoz.unseenworld.client.model.ModelWarriorOfTheChimericDarkness;
import net.sashakyotoz.unseenworld.entity.DarkGolemEntity;
import org.jetbrains.annotations.NotNull;

public class DarkGolemRenderer extends MobRenderer<DarkGolemEntity, ModelWarriorOfTheChimericDarkness<DarkGolemEntity>> {
	public DarkGolemRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelWarriorOfTheChimericDarkness(context.bakeLayer(ModelWarriorOfTheChimericDarkness.LAYER_LOCATION)), 0.6f);
		this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
	}
	@Override
	protected void setupRotations(@NotNull DarkGolemEntity darkGolemEntity, PoseStack stack, float p_115319_, float p_115320_, float p_115321_) {
		if (this.isShaking(darkGolemEntity))
			p_115320_ += (float)(Math.cos((double)darkGolemEntity.tickCount * 3.25D) * Math.PI * (double)0.4F);
		if (!darkGolemEntity.hasPose(Pose.SLEEPING))
			stack.mulPose(Axis.YP.rotationDegrees(180.0F - p_115320_));
		if (darkGolemEntity.isAutoSpinAttack()) {
			stack.mulPose(Axis.XP.rotationDegrees(-90.0F - darkGolemEntity.getXRot()));
			stack.mulPose(Axis.YP.rotationDegrees(((float)darkGolemEntity.tickCount + p_115321_) * -75.0F));
		}
		else if (isEntityUpsideDown(darkGolemEntity)) {
			stack.translate(0.0F, darkGolemEntity.getBbHeight() + 0.1F, 0.0F);
			stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
		}
	}
	@Override
	public ResourceLocation getTextureLocation(DarkGolemEntity entity) {
		return new ResourceLocation("unseen_world:textures/entities/warrior_of_the_chimeric_darkness.png");
	}
}
