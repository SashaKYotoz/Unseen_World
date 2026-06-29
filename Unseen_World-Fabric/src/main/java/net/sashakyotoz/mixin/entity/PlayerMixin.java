package net.sashakyotoz.mixin.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.api.entity_data.data.GripcrystalManaData;
import net.sashakyotoz.common.config.ConfigController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        Player player = (Player) ((Object) this);
        ConfigController.DataItem item = ConfigController.getDataToStack(player.getMainHandItem());
        if (item != null && (item.compatibilityType().equals(ConfigController.Type.EFFECT_REMOVER.type) && item.effectToRemove() != null)
                && player instanceof ServerPlayer player1
                && GripcrystalManaData.getMana((IEntityDataSaver) player1) >= item.manaCost()) {
            if (player.hasEffect(BuiltInRegistries.MOB_EFFECT.get(item.effectToRemove()))) {
                if (player.getRandom().nextInt(7) == 2)
                    GripcrystalManaData.removeMana(((IEntityDataSaver) player1), item.manaCost());
                player.removeEffect(BuiltInRegistries.MOB_EFFECT.get(item.effectToRemove()));
            }
        }
    }
}