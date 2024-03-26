package net.sashakyotoz.unseenworld.managers;

import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.util.UnseenWorldModItems;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.client.Minecraft;

public class BlazerHelmetTickEventProcedure {
	public static void execute(Level level, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof LivingEntity livingEntity && livingEntity.getItemBySlot(EquipmentSlot.HEAD).getItem() == UnseenWorldModItems.BLAZER_HELMET.get()) {
			if (!level.isClientSide())
				livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 60, 1));
			UnseenWorldMod.queueServerWork(50, () -> {
				if (!level.isClientSide())
					livingEntity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 160, 1));
				if (!level.isClientSide())
					level.explode(null, x, y, z, 2, Level.ExplosionInteraction.NONE);
				entity.setDeltaMovement(new Vec3(0, 0.25, 0));
				if (entity instanceof Player player) {
					player.getAbilities().invulnerable = true;
					player.onUpdateAbilities();
				}
				if (new Object() {
					public boolean checkGamemode(Entity _ent) {
						if (_ent instanceof ServerPlayer player) {
							return player.gameMode.getGameModeForPlayer() == GameType.SURVIVAL;
						} else if (_ent.level().isClientSide() && _ent instanceof Player player) {
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
		}
	}
}
