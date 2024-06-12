package net.sashakyotoz.unseenworld.managers;

import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModItems;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.client.Minecraft;

public class BlazerHelmetShiftEventProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
		Level level = entity.level();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        if (entity instanceof LivingEntity livingEntity && livingEntity.getItemBySlot(EquipmentSlot.HEAD).is(UnseenWorldModItems.BLAZER_HELMET.get())) {
            if (!level.isClientSide()) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 60, 1));
                UnseenWorldMod.queueServerWork(50, () -> {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 160, 1));
                    level.explode(null, x, y, z, 2, Level.ExplosionInteraction.NONE);
                    entity.setDeltaMovement(new Vec3(0, 0.25, 0));
                    if (entity instanceof Player player) {
                        player.getAbilities().invulnerable = true;
                        player.onUpdateAbilities();
                    }
                    if (new Object() {
                        public boolean checkGamemode(Entity entity1) {
                            if (entity1 instanceof ServerPlayer player) {
                                return player.gameMode.getGameModeForPlayer() == GameType.SURVIVAL;
                            } else if (entity1.level().isClientSide() && entity1 instanceof Player player) {
                                return Minecraft.getInstance().getConnection().getPlayerInfo(player.getGameProfile().getId()) != null
                                        && Minecraft.getInstance().getConnection().getPlayerInfo(player.getGameProfile().getId()).getGameMode() == GameType.SURVIVAL;
                            }
                            return false;
                        }
                    }.checkGamemode(entity)) {
                        UnseenWorldMod.queueServerWork(50, () -> {
                            if (entity instanceof Player player) {
                                player.getAbilities().invulnerable = false;
                                player.onUpdateAbilities();
                            }
                        });
                    }
                });
            }else
				level.setBlock(entity.blockPosition().below(),level.getBlockState(entity.blockPosition().below()),3);
        }
    }
}
