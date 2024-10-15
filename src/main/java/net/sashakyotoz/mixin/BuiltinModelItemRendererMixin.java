package net.sashakyotoz.mixin;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
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

@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererMixin {
    @Shadow
    @Final
    private EntityModelLoader entityModelLoader;
    @Unique
    private ShieldOfWarriorModel shieldOfWarriorModel;
    @Unique
    private static final SpriteIdentifier SHIELD_BASE = new SpriteIdentifier(
            UnseenWorld.makeID("textures/atlas/shield_patterns.png"), UnseenWorld.makeID("textures/item/shield_of_warrior_patterned.png")
    );
    @Unique
    private static final SpriteIdentifier SHIELD_BASE_NO_PATTERN = new SpriteIdentifier(
            UnseenWorld.makeID("textures/atlas/shield_patterns.png"), UnseenWorld.makeID("textures/item/shield_of_warrior.png")
    );

    @Inject(method = "reload", at = @At("HEAD"))
    public void onReload(ResourceManager manager, CallbackInfo ci) {
        this.shieldOfWarriorModel = new ShieldOfWarriorModel(this.entityModelLoader.getModelPart(ShieldOfWarriorModel.SHIELD_OF_WARRIOR));
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void onRender(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
        if (stack.isOf(ModItems.SHIELD_OF_CHIMERIC_WARRIOR)) {
            matrices.push();
            matrices.scale(1.0F, -1.0F, -1.0F);
            this.shieldOfWarriorModel.getHandle().render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(UnseenWorld.makeID("textures/item/shield_of_warrior.png"))), light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            this.shieldOfWarriorModel.getPlate().render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(UnseenWorld.makeID("textures/item/shield_of_warrior.png"))), light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            matrices.pop();
        }
    }
}