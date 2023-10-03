
package net.sashakyotoz.unseenworld.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.sashakyotoz.unseenworld.client.model.ModelChimeric_Redmarer_With_Saddle;
import net.sashakyotoz.unseenworld.entity.ChimericRedmarerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ChimericRedmarerRenderer extends MobRenderer<ChimericRedmarerEntity, ModelChimeric_Redmarer_With_Saddle<ChimericRedmarerEntity>> {
    private static final ResourceLocation REDMARER_LOCATION = new ResourceLocation("unseen_world:textures/entities/chimeric_redmarer.png");
    private static final ResourceLocation REDMARER_SADDLED_LOCATION = new ResourceLocation("unseen_world:textures/entities/chimeric_redmarer_with_saddle.png");

    public ChimericRedmarerRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelChimeric_Redmarer_With_Saddle(context.bakeLayer(ModelChimeric_Redmarer_With_Saddle.LAYER_LOCATION)), 0.7f);
    }

    protected void scale(ChimericRedmarerEntity p_116066_, PoseStack p_116067_, float p_116068_) {
        if (p_116066_.isBaby()) {
            p_116067_.scale(0.5F, 0.5F, 0.5F);
            this.shadowRadius = 0.25F;
        } else {
            this.shadowRadius = 0.5F;
        }
    }

    public ResourceLocation getTextureLocation(ChimericRedmarerEntity entity) {
        return entity.isSaddled() ? REDMARER_SADDLED_LOCATION : REDMARER_LOCATION;
    }
}
