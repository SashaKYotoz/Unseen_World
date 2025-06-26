package net.sashakyotoz.api.entity_data.data;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.common.networking.ModMessages;

public class GripcrystalManaData {
    public static int addMana(IEntityDataSaver player, int amount) {
        NbtCompound nbt = player.getPersistentData();
        int mana = nbt.getInt("gripcrystal_mana");
        if (mana + amount >= 48)
            mana = 48;
        else
            mana += amount;
        nbt.putInt("gripcrystal_mana", mana);
        syncMana(mana, (ServerPlayerEntity) player);
        return mana;
    }

    public static int getMana(IEntityDataSaver player) {
        return player.getPersistentData().getInt("gripcrystal_mana");
    }

    public static int removeMana(IEntityDataSaver player, int amount) {
        NbtCompound nbt = player.getPersistentData();
        int mana = nbt.getInt("gripcrystal_mana");
        if (mana - amount < 0)
            mana = 0;
        else
            mana -= amount;
        nbt.putInt("gripcrystal_mana", mana);
        syncMana(mana, (ServerPlayerEntity) player);
        return mana;
    }

    public static void syncMana(int mana, ServerPlayerEntity player) {
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(mana);
        ServerPlayNetworking.send(player, ModMessages.GRIPCRYSTAL_MANA_HANDLER, buffer);
    }
}
