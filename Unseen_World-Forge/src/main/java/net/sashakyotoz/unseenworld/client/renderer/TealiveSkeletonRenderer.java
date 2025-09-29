
package net.sashakyotoz.unseenworld.client.renderer;

import net.sashakyotoz.unseenworld.client.model.ModelTealive_Skeleton;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;

import net.sashakyotoz.unseenworld.entity.TealiveSkeletonEntity;

import java.util.Calendar;

public class TealiveSkeletonRenderer extends MobRenderer<TealiveSkeletonEntity, ModelTealive_Skeleton<TealiveSkeletonEntity>> {
    private static final ResourceLocation halloween_texture = new ResourceLocation("unseen_world:textures/entities/skeletons/tealive_skeleton_halloween.png");
    private boolean halloweenTexture = false;

    public TealiveSkeletonRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelTealive_Skeleton(context.bakeLayer(ModelTealive_Skeleton.LAYER_LOCATION)), 0.5f);
        this.addLayer(new EyesLayer<>(this) {
            @Override
            public RenderType renderType() {
                return RenderType.eyes(new ResourceLocation("unseen_world:textures/entities/skeletons/glow_tealive_skeleton.png"));
            }
        });
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.MONTH) + 1 == 10 && calendar.get(Calendar.DATE) >= 28) {
            this.halloweenTexture = true;
        }
    }

    @Override
    public ResourceLocation getTextureLocation(TealiveSkeletonEntity entity) {
        return halloweenTexture ? halloween_texture :
                new ResourceLocation("unseen_world:textures/entities/skeletons/tealive_skeleton.png");
    }
}
