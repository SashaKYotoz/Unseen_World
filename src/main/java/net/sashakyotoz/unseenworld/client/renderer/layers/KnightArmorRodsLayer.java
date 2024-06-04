package net.sashakyotoz.unseenworld.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.sashakyotoz.unseenworld.client.model.ModelThe_Wither_Knight_Armor_Rods;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModItems;

public class KnightArmorRodsLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation RODS_LOCATION = new ResourceLocation("unseen_world:textures/entities/the_wither_knight_armor.png");
    private final ModelThe_Wither_Knight_Armor_Rods<T> armorModel;

    public KnightArmorRodsLayer(RenderLayerParent<T, M> p_174493_, EntityModelSet p_174494_) {
        super(p_174493_);
        this.armorModel = new ModelThe_Wither_Knight_Armor_Rods<>(p_174494_.bakeLayer(ModelThe_Wither_Knight_Armor_Rods.LAYER_LOCATION));
    }

    public void render(PoseStack stack, MultiBufferSource bufferSource, int p_116953_, T entity, float p_116955_, float p_116956_, float p_116957_, float p_116958_, float p_116959_, float p_116960_) {
        ItemStack itemstack = entity.getItemBySlot(EquipmentSlot.CHEST);
        if (shouldRender(itemstack, entity)) {
            ResourceLocation resourcelocation;
            resourcelocation = getRodsLocation(itemstack, entity);
            stack.pushPose();
            stack.translate(0.0F, 0.0F, 0.125F);
            this.getParentModel().copyPropertiesTo(this.armorModel);
            this.armorModel.setupAnim(entity, p_116955_, p_116956_, p_116958_, p_116959_, p_116960_);
            VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(bufferSource, RenderType.armorCutoutNoCull(resourcelocation), false, itemstack.hasFoil());
            this.armorModel.renderToBuffer(stack, vertexconsumer, p_116953_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            stack.popPose();
        }
    }

    public boolean shouldRender(ItemStack stack, T entity) {
        return stack.getItem() == UnseenWorldModItems.KNIGHT_ARMOR_CHESTPLATE.get() && entity.isShiftKeyDown();
    }

    public ResourceLocation getRodsLocation(ItemStack stack, T entity) {
        return RODS_LOCATION;
    }
}
