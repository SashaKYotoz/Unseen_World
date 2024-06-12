package net.sashakyotoz.unseenworld.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
    @Inject(method = "renderItem",at= @At("HEAD"))
    public void renderItem(LivingEntity entity, ItemStack stack, ItemDisplayContext context, boolean p_270203_, PoseStack poseStack, MultiBufferSource source, int p_270103_, CallbackInfo ci){
        if ((stack.is(UnseenWorldModItems.VOID_STAFF.get()) || stack.is(UnseenWorldModItems.NETHERIUM_STAFF.get()) || stack.is(UnseenWorldModItems.TEALIVY_FIRE_STAFF.get())
                || stack.is(UnseenWorldModItems.TANZANITE_STAFF.get())) && entity.isUsingItem() && context.firstPerson())
            poseStack.translate(0.25f,-0.5f,-0.65f);
    }
}
