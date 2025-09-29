
package net.sashakyotoz.unseenworld.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.sashakyotoz.unseenworld.client.model.ModelChimericRedmarerWithSaddle;
import net.sashakyotoz.unseenworld.entity.ChimericPurplemarerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ChimericPurplemarerRenderer extends MobRenderer<ChimericPurplemarerEntity, ModelChimericRedmarerWithSaddle<ChimericPurplemarerEntity>> {
    private static final ResourceLocation PURPLEMARER_LOCATION = new ResourceLocation("unseen_world:textures/entities/chimeric_purplemarer.png");
    private static final ResourceLocation PURPLEMARER_SADDLED_LOCATION = new ResourceLocation("unseen_world:textures/entities/chimeric_purplemarer_with_saddle.png");

    public ChimericPurplemarerRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelChimericRedmarerWithSaddle(context.bakeLayer(ModelChimericRedmarerWithSaddle.LAYER_LOCATION)), 0.7f);
    }
    protected void scale(ChimericPurplemarerEntity entity, PoseStack stack, float p_116068_) {
        if (entity.isBaby()) {
            stack.scale(0.5F, 0.5F, 0.5F);
            this.shadowRadius = 0.25F;
        } else {
            stack.scale(1F, 1F, 1F);
            this.shadowRadius = 0.5F;
        }
    }

    @Override
    public ResourceLocation getTextureLocation(ChimericPurplemarerEntity entity) {
        return entity.isSaddled() ? PURPLEMARER_SADDLED_LOCATION : PURPLEMARER_LOCATION;
    }
}
