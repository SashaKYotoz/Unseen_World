package net.sashakyotoz.unseenworld.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.Level;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.block.entity.BeaconOfWeaponsBlockEntity;
import net.sashakyotoz.unseenworld.client.model.ModelBeaconOfWeapons;

import java.util.Calendar;

public class BeaconOfWeaponsRenderer <T extends BeaconOfWeaponsBlockEntity> implements BlockEntityRenderer<T> {
    public static ResourceLocation TEXTURE = new ResourceLocation(UnseenWorldMod.MODID, "textures/entities/beacon_of_weapons.png");
    public static ResourceLocation CHRISTMAS_TEXTURE = new ResourceLocation(UnseenWorldMod.MODID, "textures/entities/beacon_of_weapons_christmas.png");
    private final ItemRenderer itemRenderer;
    private boolean xmasTexture = false;
    private static ModelBeaconOfWeapons beacon;
    public BeaconOfWeaponsRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
        beacon = new ModelBeaconOfWeapons(context.bakeLayer(ModelBeaconOfWeapons.LAYER_LOCATION));
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.MONTH) + 1 == 12 && calendar.get(Calendar.DATE) >= 20 && calendar.get(Calendar.DATE) <= 30) {
            this.xmasTexture = true;
        }
    }

    @Override
    public void render(T entity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        Level world = entity.getLevel();
        assert world != null;
        long gameTime = world.getGameTime();
        float offsetY;
        poseStack.mulPose(Axis.XP.rotationDegrees(-180.0F));
        if(xmasTexture)
            beacon.renderToBuffer(poseStack, multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(CHRISTMAS_TEXTURE)), i, j, 1.0F, 1.0F, 1.0F, 1.0F);
        else
            beacon.renderToBuffer(poseStack, multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)), i, j, 1.0F, 1.0F, 1.0F, 1.0F);
        beacon.top.setPos(8.0F, -8.0F, -8.0F);
        beacon.base.setPos(16.0F, 0F, -16.0F);
        float test;
        if (BeaconOfWeaponsBlockEntity.item.isEmpty()){
            test = (float)(gameTime % 1080) * 0.325F;
            offsetY = (float)Math.sin((float)gameTime / 8.0F) * 0.75F;
        }
        else{
            test =(float)(gameTime % 360) * 0.5F;
            offsetY = (float)Math.sin((float)gameTime / 8.0F);
        }
        beacon.beacon.setPos(8.0F, 0F - offsetY, -8.0F);
        float e = ++test;
        float tick = e / 36.0F;
        beacon.beacon.setRotation(0.0F, tick % 360.0F, 0.0F);
        if (entity.hasLevel() && !entity.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5, (double)offsetY*0.15 - 1.5, -0.5);
            poseStack.scale(0.35F, 0.35F, 0.35F);
            poseStack.mulPose(Axis.YP.rotationDegrees(offsetY % 90.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(-180.0F));
            this.itemRenderer.renderStatic(entity.getItem(0), ItemDisplayContext.FIXED, i, OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, entity.getLevel(), j);
            poseStack.popPose();
        }
    }
}
