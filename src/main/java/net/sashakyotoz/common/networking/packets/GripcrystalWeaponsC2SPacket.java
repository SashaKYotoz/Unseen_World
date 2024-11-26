package net.sashakyotoz.common.networking.packets;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.sashakyotoz.common.items.custom.ChimericRockbreakerHammerItem;
import net.sashakyotoz.common.items.custom.EclipsebaneItem;
import net.sashakyotoz.common.items.custom.GrippingAbyssalBowItem;
import net.sashakyotoz.common.networking.data.GripcrystalManaData;
import net.sashakyotoz.common.tags.ModTags;
import net.sashakyotoz.utils.IEntityDataSaver;

public class GripcrystalWeaponsC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        if (player != null && player.getMainHandStack().isIn(ModTags.Items.CAN_BE_CHARGED_BY_GRIPCRYSTALS)) {
            ItemStack stack = player.getMainHandStack();
            if (stack.getItem() instanceof EclipsebaneItem item) {
                switch (item.getItemPhase(stack)) {
                    case "absorption" -> stack.getOrCreateNbt().putString("eclipsebane_phase", "light_ray");
                    case "light_ray" -> stack.getOrCreateNbt().putString("eclipsebane_phase", "blade_shield");
                    case "blade_shield" -> stack.getOrCreateNbt().putString("eclipsebane_phase", "");
                    default -> stack.getOrCreateNbt().putString("eclipsebane_phase", "absorption");
                }
            }
            if (stack.getItem() instanceof ChimericRockbreakerHammerItem item) {
                switch (item.getItemPhase(stack)) {
                    case "hammer_smashing" ->
                            stack.getOrCreateNbt().putString("rockbreaker_hammer_phase", "heavy_winding");
                    case "heavy_winding" ->
                            stack.getOrCreateNbt().putString("rockbreaker_hammer_phase", "hammer_eroflaming");
                    case "hammer_eroflaming" -> stack.getOrCreateNbt().putString("rockbreaker_hammer_phase", "");
                    default -> stack.getOrCreateNbt().putString("rockbreaker_hammer_phase", "hammer_smashing");
                }
            }
            if (stack.getItem() instanceof GrippingAbyssalBowItem item) {
                switch (item.getItemPhase(stack)) {
                    case "crystal_crushing" -> stack.getOrCreateNbt().putString("bow_phase", "crystal_rain");
                    case "crystal_rain" -> stack.getOrCreateNbt().putString("bow_phase", "crystal_suctioning");
                    case "crystal_suctioning" -> stack.getOrCreateNbt().putString("bow_phase", "");
                    default -> stack.getOrCreateNbt().putString("bow_phase", "crystal_crushing");
                }
            }
            GripcrystalManaData.syncMana(((IEntityDataSaver) player).getPersistentData().getInt("gripcrystal_mana"), player);
        }
    }
}