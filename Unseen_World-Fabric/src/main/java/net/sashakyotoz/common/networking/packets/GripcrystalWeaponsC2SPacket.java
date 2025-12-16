package net.sashakyotoz.common.networking.packets;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.api.entity_data.data.GripcrystalManaData;
import net.sashakyotoz.common.items.custom.ChimericRockbreakerHammerItem;
import net.sashakyotoz.common.items.custom.EclipsebaneItem;
import net.sashakyotoz.common.items.custom.GrippingAbyssalBowItem;
import net.sashakyotoz.common.items.custom.IGrippingWeapons;
import net.sashakyotoz.common.tags.ModTags;

public class GripcrystalWeaponsC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        if (player != null && player.getMainHandStack().isIn(ModTags.Items.CAN_BE_CHARGED_BY_GRIPCRYSTALS)) {
            ItemStack stack = player.getMainHandStack();
            if (stack.getItem() instanceof EclipsebaneItem) {
                switch (IGrippingWeapons.getPhase(stack)) {
                    case "absorption" -> IGrippingWeapons.setPhase(stack, "light_ray");
                    case "light_ray" -> IGrippingWeapons.setPhase(stack, "blade_shield");
                    case "blade_shield" -> IGrippingWeapons.setPhase(stack, "");
                    default -> IGrippingWeapons.setPhase(stack, "absorption");
                }
            }
            if (stack.getItem() instanceof ChimericRockbreakerHammerItem) {
                switch (IGrippingWeapons.getPhase(stack)) {
                    case "hammer_smashing" ->
                            IGrippingWeapons.setPhase(stack, "heavy_winding");
                    case "heavy_winding" ->
                            IGrippingWeapons.setPhase(stack, "hammer_eroflaming");
                    case "hammer_eroflaming" -> IGrippingWeapons.setPhase(stack, "");
                    default -> IGrippingWeapons.setPhase(stack, "hammer_smashing");
                }
            }
            if (stack.getItem() instanceof GrippingAbyssalBowItem) {
                switch (IGrippingWeapons.getPhase(stack)) {
                    case "crystal_crushing" -> IGrippingWeapons.setPhase(stack, "crystal_rain");
                    case "crystal_rain" -> IGrippingWeapons.setPhase(stack, "crystal_suctioning");
                    case "crystal_suctioning" -> IGrippingWeapons.setPhase(stack, "");
                    default -> IGrippingWeapons.setPhase(stack, "crystal_crushing");
                }
            }
            IEntityDataSaver playerData = (IEntityDataSaver) player;
            GripcrystalManaData.addOpacityDelta(playerData, 1);
            GripcrystalManaData.syncData(GripcrystalManaData.getMana(playerData), GripcrystalManaData.getOpacity(playerData), player);
        }
    }
}