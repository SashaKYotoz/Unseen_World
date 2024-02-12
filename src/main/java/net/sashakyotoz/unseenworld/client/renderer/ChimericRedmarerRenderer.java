
package net.sashakyotoz.unseenworld.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.sashakyotoz.unseenworld.client.model.ModelChimericRedmarerWithSaddle;
import net.sashakyotoz.unseenworld.entity.ChimericRedmarerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ChimericRedmarerRenderer extends MobRenderer<ChimericRedmarerEntity, ModelChimericRedmarerWithSaddle<ChimericRedmarerEntity>> {
    private static final ResourceLocation REDMARER_LOCATION = new ResourceLocation("unseen_world:textures/entities/chimeric_redmarer.png");
    private static final ResourceLocation REDMARER_SADDLED_LOCATION = new ResourceLocation("unseen_world:textures/entities/chimeric_redmarer_with_saddle.png");

    public ChimericRedmarerRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelChimericRedmarerWithSaddle(context.bakeLayer(ModelChimericRedmarerWithSaddle.LAYER_LOCATION)), 0.7f);
    }

    protected void scale(ChimericRedmarerEntity entity, PoseStack stack, float p_116068_) {
        if (entity.isBaby()) {
            stack.scale(0.5F, 0.5F, 0.5F);
            this.shadowRadius = 0.25F;
        } else {
            stack.scale(1F, 1F, 1F);
            this.shadowRadius = 0.5F;
        }
    }

    public ResourceLocation getTextureLocation(ChimericRedmarerEntity entity) {
        return entity.isSaddled() ? REDMARER_SADDLED_LOCATION : REDMARER_LOCATION;
    }
}
