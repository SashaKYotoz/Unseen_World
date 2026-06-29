package net.sashakyotoz.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.models.ShieldOfWarriorModel;
import net.sashakyotoz.common.items.ModItems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class BuiltinModelItemRendererMixin {
    @Shadow
    @Final
    private EntityModelSet entityModelSet;
    @Unique
    private ShieldOfWarriorModel shieldOfWarriorModel;
    @Unique
    private static final Material SHIELD_BASE = new Material(
            Sheets.SHIELD_SHEET, UnseenWorld.makeID("textures/item/shield_of_warrior_patterned.png")
    );
    @Unique
    private final ResourceLocation shieldLocation = UnseenWorld.makeID("textures/item/shield_of_warrior.png");

    @Inject(method = "onResourceManagerReload", at = @At("HEAD"))
    public void onReload(ResourceManager manager, CallbackInfo ci) {
        this.shieldOfWarriorModel = new ShieldOfWarriorModel(this.entityModelSet.bakeLayer(ShieldOfWarriorModel.SHIELD_OF_WARRIOR));
    }
    @Inject(method = "renderByItem", at = @At("HEAD"))
    public void onRender(ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, CallbackInfo ci) {
        if (stack.is(ModItems.SHIELD_OF_CHIMERIC_WARRIOR)) {
            boolean bl = BlockItem.getBlockEntityData(stack) != null;
            matrices.pushPose();
            matrices.scale(1.0F, -1.0F, -1.0F);
            this.shieldOfWarriorModel.getPlate().render(matrices, vertexConsumers.getBuffer(RenderType.entityCutout(shieldLocation)), light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            this.shieldOfWarriorModel.getHandle().render(matrices, vertexConsumers.getBuffer(RenderType.entityCutout(shieldLocation)), light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            if (bl) {
                matrices.scale(0.5f,0.5f,0.5f);
                matrices.translate(0,-1.25,-0.225);
                List<Pair<Holder<BannerPattern>, DyeColor>> list = BannerBlockEntity.createPatterns(
                        ShieldItem.getColor(stack), BannerBlockEntity.getItemPatterns(stack)
                );
                BannerRenderer.renderPatterns(
                        matrices, vertexConsumers, light, overlay, Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.BANNER).getChild("flag"), SHIELD_BASE, true, list, stack.hasFoil()
                );
            }
            matrices.popPose();
        }
    }
}