package net.sashakyotoz.mixin.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.api.entity_data.data.GripcrystalManaData;
import net.sashakyotoz.api.entity_data.data.GrippingData;
import net.sashakyotoz.common.config.ConfigController;
import net.sashakyotoz.common.items.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) ((Object) this);
        if (player instanceof ServerPlayerEntity player1) {
            if (player1.age % 100 == 0)
                GripcrystalManaData.addMana((IEntityDataSaver) player1, 0);
            if (player.age % 40 == 0 && player.getRandom().nextInt(13) == 3
                    && GripcrystalManaData.getMana((IEntityDataSaver) player1) > 0
                    && player.getInventory().containsAny(item -> item.isOf(ModItems.GRIPTONITE)))
                GripcrystalManaData.removeMana((IEntityDataSaver) player1, 1);
        }
        if (player instanceof IGrippingEntity player1) {
            if (player.age % 20 == 0 && player1.getGrippingData() > 0) {
                GrippingData.removeGrippingPerTick(player1);
                player.damage(player.getDamageSources().starve(), 2);
            }
        }
        ConfigController.DataItem item = ConfigController.getDataToStack(player.getMainHandStack());
        if (item != null && (item.compatibilityType().equals(ConfigController.Type.EFFECT_REMOVER.type) && item.effectToRemove() != null)
                && player instanceof ServerPlayerEntity player1
                && GripcrystalManaData.getMana((IEntityDataSaver) player1) >= item.manaCost()) {
            if (player.hasStatusEffect(Registries.STATUS_EFFECT.get(item.effectToRemove()))) {
                if (player.getRandom().nextInt(7) == 2)
                    GripcrystalManaData.removeMana(((IEntityDataSaver) player1), item.manaCost());
                player.removeStatusEffect(Registries.STATUS_EFFECT.get(item.effectToRemove()));
            }
        }
    }
}