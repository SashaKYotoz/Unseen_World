package net.sashakyotoz.unseenworld.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.sashakyotoz.unseenworld.client.model.ModelBeaconOfWeapons;
import net.sashakyotoz.unseenworld.client.renderer.BeaconOfWeaponsRenderer;
import net.sashakyotoz.unseenworld.util.UnseenWorldModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class BlockEntityWithoutLevelRendererMixin {
@Unique
private ModelBeaconOfWeapons beacon;
@Shadow
private final EntityModelSet entityModelSet;
    public BlockEntityWithoutLevelRendererMixin(EntityModelSet entityModelSet, BlockEntityRendererProvider.Context context) {
        this.entityModelSet = entityModelSet;
    }
    @Inject(method = "renderByItem", at = @At("RETURN"))
    public void renderByItemHumbledlessWorld(ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, CallbackInfo ci) {
        if (itemStack.is(UnseenWorldModItems.BEACON_OF_WEAPONS.get())) {
            long gameTime = Minecraft.getInstance().level.getGameTime();
            float test = (float)(gameTime % 360) * 0.5F;
            float e = ++test;
            float tick = e / 36.0F;
            poseStack.pushPose();
            poseStack.scale(1.1F, 1.1F, 1.1F);
            poseStack.translate(1, 1.2, 0);
            poseStack.mulPose(Axis.XP.rotationDegrees(-180.0F));
            VertexConsumer vertexConsumer2 = ItemRenderer.getFoilBufferDirect(multiBufferSource, RenderType.entityCutoutNoCull(BeaconOfWeaponsRenderer.TEXTURE), false, itemStack.hasFoil());
            this.beacon.renderToBuffer(poseStack, vertexConsumer2, i, j, 1.0F, 1.0F, 1.0F, 1.0F);
            this.beacon.beacon.setRotation(0.0F, tick % 360.0F, 0.0F);
            poseStack.popPose();
        }
    }
    @Inject(method = "onResourceManagerReload", at = @At("RETURN"))
    public void onResourceManagerReloadUnseenWorld(ResourceManager resourceManager, CallbackInfo ci) {
        this.beacon = new ModelBeaconOfWeapons(this.entityModelSet.bakeLayer(ModelBeaconOfWeapons.LAYER_LOCATION));
    }
}
