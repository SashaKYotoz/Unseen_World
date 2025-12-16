package net.sashakyotoz.mixin.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.api.entity_data.data.GripcrystalManaData;
import net.sashakyotoz.common.config.ConfigController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) ((Object) this);
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