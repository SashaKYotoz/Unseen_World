package net.sashakyotoz.unseenworld.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.sashakyotoz.unseenworld.client.model.ModelBeaconOfWeapons;
import net.sashakyotoz.unseenworld.client.renderer.BeaconOfWeaponsRenderer;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModBlocks;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModItems;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class BlockEntityWithoutLevelRendererMixin {
@Unique
public ModelBeaconOfWeapons unseenworld$beacon;
@Mutable
@Final
@Shadow
@NotNull
private final EntityModelSet entityModelSet;
    public BlockEntityWithoutLevelRendererMixin(@NotNull EntityModelSet entityModelSet) {
        this.entityModelSet = entityModelSet;
    }
    @Inject(method = "onResourceManagerReload", at = @At("HEAD"))
    public void onResourceManagerReloadUnseenWorld(ResourceManager resourceManager, CallbackInfo ci) {
        this.unseenworld$beacon = new ModelBeaconOfWeapons(this.entityModelSet.bakeLayer(ModelBeaconOfWeapons.LAYER_LOCATION));
    }
    @Inject(method = "renderByItem", at = @At("RETURN"))
    public void renderByItemUnseenWorld(ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, CallbackInfo ci) {
        Minecraft minecraft =  Minecraft.getInstance();
        if (itemStack.is(UnseenWorldModBlocks.BEACON_OF_WEAPONS.get().asItem()) && minecraft.level != null) {
            long gameTime = minecraft.level.getGameTime();
            float test = (float)(gameTime % 360) * 0.5F;
            float e = ++test;
            float tick = e / 36.0F;
            poseStack.pushPose();
            poseStack.scale(1.1F, 1.1F, 1.1F);
            poseStack.translate(1, 1.2, 0);
            poseStack.mulPose(Axis.XP.rotationDegrees(-180.0F));
            VertexConsumer vertexConsumer2 = ItemRenderer.getFoilBufferDirect(multiBufferSource, RenderType.entityCutoutNoCull(BeaconOfWeaponsRenderer.TEXTURE), false, itemStack.hasFoil());
            this.unseenworld$beacon.renderToBuffer(poseStack, vertexConsumer2, i, j, 1.0F, 1.0F, 1.0F, 1.0F);
            this.unseenworld$beacon.beacon.setRotation(0.0F, tick % 360.0F, 0.0F);
            poseStack.popPose();
        }
    }
}
