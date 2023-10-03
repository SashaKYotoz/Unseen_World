
package net.sashakyotoz.unseenworld.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.sashakyotoz.unseenworld.client.model.ModelChimeric_Redmarer_With_Saddle;
import net.sashakyotoz.unseenworld.entity.ChimericPurplemarerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ChimericPurplemarerRenderer extends MobRenderer<ChimericPurplemarerEntity, ModelChimeric_Redmarer_With_Saddle<ChimericPurplemarerEntity>> {
    private static final ResourceLocation PURPLEMARER_LOCATION = new ResourceLocation("unseen_world:textures/entities/chimeric_purplemarer.png");
    private static final ResourceLocation PURPLEMARER_SADDLED_LOCATION = new ResourceLocation("unseen_world:textures/entities/chimeric_purplemarer_with_saddle.png");

    public ChimericPurplemarerRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelChimeric_Redmarer_With_Saddle(context.bakeLayer(ModelChimeric_Redmarer_With_Saddle.LAYER_LOCATION)), 0.7f);
    }
    protected void scale(ChimericPurplemarerEntity p_116066_, PoseStack p_116067_, float p_116068_) {
        if (p_116066_.isBaby()) {
            p_116067_.scale(0.5F, 0.5F, 0.5F);
            this.shadowRadius = 0.25F;
        } else {
            this.shadowRadius = 0.5F;
        }
    }

    @Override
    public ResourceLocation getTextureLocation(ChimericPurplemarerEntity entity) {
        return entity.isSaddled() ? PURPLEMARER_SADDLED_LOCATION : PURPLEMARER_LOCATION;
    }
}
