package net.sashakyotoz.common.networking.packets;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.items.custom.EclipsebaneItem;
import net.sashakyotoz.utils.ActionsUtils;

import java.util.List;

public class AbilityClickC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                               FriendlyByteBuf buf, PacketSender responseSender) {
        ItemStack stack = player.getMainHandItem();
        player.swing(InteractionHand.MAIN_HAND);
        if (stack.getItem() instanceof EclipsebaneItem && player.getInventory().contains(ModItems.GRANULATED_GRIPTONITE.getDefaultInstance())) {
            player.playSound(SoundEvents.ARMOR_EQUIP_ELYTRA, 2, 2.5f);
            ActionsUtils.rayCastAlong(player.level(), player, 30, (level, pos) -> {
                List<LivingEntity> entities = player.level().getEntitiesOfClass(LivingEntity.class, new AABB(pos.getCenter(), pos.getCenter()).inflate(1.25), LivingEntity::canBeHitByProjectile);
                for (LivingEntity entity : entities) {
                    if (entity != player)
                        entity.hurt(player.damageSources().magic(), 8);
                }
            });
        }
    }
}