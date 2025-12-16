package net.sashakyotoz.mixin.client;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.api.entity_data.data.GripcrystalManaData;
import net.sashakyotoz.client.environment.ClientTicks;
import net.sashakyotoz.client.models.EclipsebaneModel;
import net.sashakyotoz.client.models.ShieldOfWarriorModel;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.items.custom.EclipsebaneItem;
import net.sashakyotoz.common.items.custom.IGrippingWeapons;
import net.sashakyotoz.utils.Oscillator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.List;

@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererMixin {
    @Shadow
    @Final
    private EntityModelLoader entityModelLoader;
    @Unique
    private ShieldOfWarriorModel shieldOfWarriorModel;
    @Unique
    private EclipsebaneModel eclipsebaneModel;
    @Unique
    private static final SpriteIdentifier SHIELD_BASE = new SpriteIdentifier(
            TexturedRenderLayers.SHIELD_PATTERNS_ATLAS_TEXTURE, UnseenWorld.makeID("textures/item/shield_of_warrior_patterned.png")
    );
    @Unique
    private final Identifier shieldLocation = UnseenWorld.makeID("textures/item/shield_of_warrior.png");

    @Inject(method = "reload", at = @At("HEAD"))
    public void onReload(ResourceManager manager, CallbackInfo ci) {
        this.shieldOfWarriorModel = new ShieldOfWarriorModel(this.entityModelLoader.getModelPart(ShieldOfWarriorModel.SHIELD_OF_WARRIOR));
        this.eclipsebaneModel = new EclipsebaneModel(this.entityModelLoader.getModelPart(EclipsebaneModel.ECLIPSEBANE));
    }
    @Inject(method = "render", at = @At("HEAD"))
    public void onRender(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
        if (stack.isOf(ModItems.SHIELD_OF_CHIMERIC_WARRIOR)) {
            boolean bl = BlockItem.getBlockEntityNbt(stack) != null;
            matrices.push();
            matrices.scale(1.0F, -1.0F, -1.0F);
            this.shieldOfWarriorModel.getPlate().render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(shieldLocation)), light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            this.shieldOfWarriorModel.getHandle().render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(shieldLocation)), light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            if (bl) {
                matrices.scale(0.5f,0.5f,0.5f);
                matrices.translate(0,-1.25,-0.225);
                List<Pair<RegistryEntry<BannerPattern>, DyeColor>> list = BannerBlockEntity.getPatternsFromNbt(
                        ShieldItem.getColor(stack), BannerBlockEntity.getPatternListNbt(stack)
                );
                BannerBlockEntityRenderer.renderCanvas(
                        matrices, vertexConsumers, light, overlay, MinecraftClient.getInstance().getEntityModelLoader().getModelPart(EntityModelLayers.BANNER).getChild("flag"), SHIELD_BASE, true, list, stack.hasGlint()
                );
            }
            matrices.pop();
        }
        if (stack.getItem() instanceof EclipsebaneItem) {
            matrices.push();
            matrices.scale(-1.0F, -1.0F, 1.0F);
            switch (mode) {
                case THIRD_PERSON_LEFT_HAND, THIRD_PERSON_RIGHT_HAND -> matrices.translate(-0.3, -0.3, 0.5);
                case FIRST_PERSON_RIGHT_HAND, FIRST_PERSON_LEFT_HAND -> matrices.translate(-0.125, -0.5, 0.5);
                case GUI -> matrices.translate(-0.125, -0.5, 0);
            }
            this.eclipsebaneModel.getHandle().render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(UnseenWorld.makeID("textures/item/eclipsebane.png"))), light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            MinecraftClient client = MinecraftClient.getInstance();
            PlayerEntity player = client.player;
            int color = 0xFFFFFF;
            switch (IGrippingWeapons.getPhase(stack)) {
                case "light_ray" -> {
                    if (player != null && GripcrystalManaData.getMana((IEntityDataSaver) player) > 0)
                        color = Color.HSBtoRGB(ClientTicks.getHalfTicks() / 10 % 360, 1F, 1.0F);
                }
                case "absorption" -> {
                    if (player != null && GripcrystalManaData.getMana((IEntityDataSaver) player) < 48)
                        color = Color.HSBtoRGB(240, 1F, (float) Oscillator.getOscillatingValue(ClientTicks.getTicks()));
                }
                case "blade_shield" ->
                        color = Color.HSBtoRGB(300, (float) Oscillator.getOscillatingValue(ClientTicks.getTicks()), 1F);
            }
            float r = (float) (color >> 16 & 255) / 255.0F;
            float g = (float) (color >> 8 & 255) / 255.0F;
            float b = (float) (color & 255) / 255.0F;
            this.eclipsebaneModel.getBlade().render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(UnseenWorld.makeID("textures/item/eclipsebane.png"))), light, overlay, r, g, b, 1.0F);
            matrices.pop();
        }
    }
}