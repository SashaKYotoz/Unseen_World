package net.sashakyotoz.common.networking.packets;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.api.entity_data.data.GripcrystalManaData;
import net.sashakyotoz.client.particles.custom.effects.LightVibrationParticleEffect;
import net.sashakyotoz.client.particles.custom.effects.WindVibrationParticleEffect;
import net.sashakyotoz.common.config.ConfigController;
import net.sashakyotoz.common.items.custom.ChimericRockbreakerHammerItem;
import net.sashakyotoz.common.items.custom.EclipsebaneItem;
import net.sashakyotoz.common.items.custom.IGrippingWeapons;
import net.sashakyotoz.common.tags.ModTags;
import net.sashakyotoz.utils.ActionsUtils;

import java.util.List;

public class AbilityClickC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                               FriendlyByteBuf buf, PacketSender responseSender) {
        ItemStack stack = player.getMainHandItem();
        player.swing(InteractionHand.MAIN_HAND);
        if (stack.is(ModTags.Items.CAN_BE_CHARGED_BY_GRIPCRYSTALS) && GripcrystalManaData.getMana((IEntityDataSaver) player) > 0) {
            if (stack.getItem() instanceof EclipsebaneItem
                    && IGrippingWeapons.getPhase(player.getMainHandItem()).equals("light_ray")
                    && GripcrystalManaData.getMana((IEntityDataSaver) player) >= 3) {
                GripcrystalManaData.removeMana((IEntityDataSaver) player, 3);
                player.playSound(SoundEvents.ARMOR_EQUIP_ELYTRA, 2, 2.5f);
                float scaling = 0;
                Level world = player.level();
                BlockPos particlePos = player.level().clip(new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getViewVector(1f).scale(9)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos();
                player.serverLevel().sendParticles(new LightVibrationParticleEffect(new BlockPositionSource(particlePos), 15), player.getX(), player.getY() + 1, player.getZ(), 3, 0, 0, 0, 1);
                for (int i1 = 0; i1 < 9; i1++) {
                    BlockPos pos = world.clip(new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos();
                    if (!player.level().getBlockState(pos).canOcclude())
                        scaling = scaling + 1;
                    BlockPos pos1 = player.level().clip(new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos();
                    List<LivingEntity> entities = player.level().getEntitiesOfClass(LivingEntity.class, new AABB(pos1.getCenter(), pos1.getCenter()).inflate(1.25), LivingEntity::isPickable);
                    for (LivingEntity entity : entities) {
                        if (entity != player)
                            entity.hurt(player.damageSources().magic(), 10);
                    }
                }
            }
            if (stack.getItem() instanceof ChimericRockbreakerHammerItem
                    && IGrippingWeapons.getPhase(player.getMainHandItem()).equals("heavy_winding")
                    && GripcrystalManaData.getMana((IEntityDataSaver) player) > 12) {
                GripcrystalManaData.removeMana((IEntityDataSaver) player, 12);
                player.serverLevel().playSound(player, player.blockPosition().above(), SoundEvents.LLAMA_SWAG, SoundSource.PLAYERS, 2, 1.75f);
                float scaling = 0;
                BlockPos particlePos = player.level().clip(new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getViewVector(1f).scale(10)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos();
                player.serverLevel().sendParticles(new WindVibrationParticleEffect(new BlockPositionSource(particlePos), 15), player.getX(), player.getY() + 1, player.getZ(), 3, 0, 0, 0, 1);
                for (int i1 = 0; i1 < 10; i1++) {
                    BlockPos pos = player.level().clip(new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos();
                    if (!player.level().getBlockState(pos).canOcclude())
                        scaling = scaling + 1;
                    else
                        player.level().explode(player, pos.getX(), pos.getY(), pos.getZ(), 2, Level.ExplosionInteraction.BLOCK);
                    BlockPos pos1 = player.level().clip(new ClipContext(player.getEyePosition(), player.getEyePosition().add(player.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos();
                    List<LivingEntity> entities = player.level().getEntitiesOfClass(LivingEntity.class, new AABB(pos1.getCenter(), pos1.getCenter()).inflate(1.25), LivingEntity::isPickable);
                    for (LivingEntity entity : entities) {
                        if (entity != player) {
                            entity.hurt(player.damageSources().magic(), 6);
                            entity.setTicksFrozen(100);
                            entity.setDeltaMovement(
                                    ActionsUtils.getXVector(2, player.getYRot()),
                                    ActionsUtils.getYVector(0.5, player.getXRot()),
                                    ActionsUtils.getZVector(2, player.getYRot())
                            );
                        }
                    }
                }
            }
        }
        ConfigController.DataItem dataItem = ConfigController.getDataToStack(stack);
        if (dataItem != null) {
            if (GripcrystalManaData.getMana((IEntityDataSaver) player) > dataItem.manaCost()) {
                if (dataItem.actionToPerform() != null)
                    dataItem.actionToPerform().forEach(s -> {
                        if (!s.contains("damageAll"))
                            player.serverLevel().getServer().getCommands().performPrefixedCommand(
                                    new CommandSourceStack(player, new Vec3(player.getX(), player.getY(), player.getZ()), Vec2.ZERO, player.serverLevel(),
                                            4, "", Component.literal(""), server, player)
                                            .withSource(CommandSource.NULL), s
                            );
                        else
                            ActionsUtils.hitNearbyMobs(player, ConfigController.getIntegers(s).get(0), ConfigController.getIntegers(s).get(1));
                    });
                GripcrystalManaData.removeMana((IEntityDataSaver) player, dataItem.manaCost());
                player.getCooldowns().addCooldown(stack.getItem(), 20);
            }
        }
    }
}