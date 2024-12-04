package net.sashakyotoz.mixin;

import com.mojang.authlib.GameProfile;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.networking.data.GripcrystalManaData;
import net.sashakyotoz.common.networking.data.GrippingData;
import net.sashakyotoz.common.tags.ModTags;
import net.sashakyotoz.utils.ActionsManager;
import net.sashakyotoz.utils.IEntityDataSaver;
import net.sashakyotoz.utils.JsonWorldController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    public void onSetUp(World world, BlockPos pos, float yaw, GameProfile gameProfile, CallbackInfo ci) {
        if (world instanceof ServerWorld serverWorld)
            JsonWorldController.data.put(0, JsonWorldController.loadData(serverWorld));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) ((Object) this);
        if (player instanceof ServerPlayerEntity player1 && player1.age % 200 == 0)
            GripcrystalManaData.addMana((IEntityDataSaver) player1, 0);
        if (player instanceof ServerPlayerEntity player1) {
            if (player1.age % 20 == 0 && GrippingData.getGrippingTime((IEntityDataSaver) player1) > 0) {
                GrippingData.removeGrippingPerTick((IEntityDataSaver) player1);
                player1.damage(player1.getDamageSources().starve(), 2);
                player1.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 1, false, false));
            }
            if (player1.age % 40 == 0 && player1.getRandom().nextInt(13) == 3
                    && GripcrystalManaData.getMana((IEntityDataSaver) player1) > 0
                    && player1.getInventory().containsAny(item -> item.isOf(ModItems.GRIPTONITE)))
                GripcrystalManaData.removeMana((IEntityDataSaver) player1, 1);
        }
        if (ActionsManager.isModLoaded("minecells")
                && player.getMainHandStack().getItem().getTranslationKey().contains("cursed_sword")
                && player instanceof ServerPlayerEntity player1
                && GripcrystalManaData.getMana((IEntityDataSaver) player1) > 0) {
            if (player.hasStatusEffect(Registries.STATUS_EFFECT.get(new Identifier("minecells:cursed")))) {
                if (player.getRandom().nextInt(7) == 2)
                    GripcrystalManaData.removeMana(((IEntityDataSaver) player1), 1);
                player.removeStatusEffect(Registries.STATUS_EFFECT.get(new Identifier("minecells:cursed")));
            }
        }
    }
}