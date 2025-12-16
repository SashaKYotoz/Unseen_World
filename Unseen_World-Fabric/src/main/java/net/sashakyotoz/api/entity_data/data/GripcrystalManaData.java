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
        syncData(mana, nbt.getFloat("ability_opacity"), (ServerPlayerEntity) player);
        return mana;
    }

    public static float addOpacityDelta(IEntityDataSaver player, float amount) {
        NbtCompound nbt = player.getPersistentData();
        float opacity = nbt.getFloat("ability_opacity");
        if (opacity + amount >= 1)
            opacity = 1;
        else
            opacity += amount;
        nbt.putFloat("ability_opacity", opacity);
        syncData(nbt.getInt("gripcrystal_mana"), opacity, (ServerPlayerEntity) player);
        return opacity;
    }

    public static int getMana(IEntityDataSaver player) {
        return player.getPersistentData().getInt("gripcrystal_mana");
    }

    public static float getOpacity(IEntityDataSaver player) {
        return player.getPersistentData().getFloat("ability_opacity");
    }

    public static int removeMana(IEntityDataSaver player, int amount) {
        NbtCompound nbt = player.getPersistentData();
        int mana = nbt.getInt("gripcrystal_mana");
        if (mana - amount < 0)
            mana = 0;
        else
            mana -= amount;
        nbt.putInt("gripcrystal_mana", mana);
        syncData(mana, nbt.getFloat("ability_opacity"), (ServerPlayerEntity) player);
        return mana;
    }

    public static float removeOpacity(IEntityDataSaver player, float amount) {
        NbtCompound nbt = player.getPersistentData();
        float opacity = nbt.getFloat("ability_opacity");
        if (opacity - amount < 0)
            opacity = 0;
        else
            opacity -= amount;
        nbt.putFloat("ability_opacity", opacity);
        syncData(nbt.getInt("gripcrystal_mana"), opacity, (ServerPlayerEntity) player);
        return opacity;
    }

    public static void syncData(int mana, float opacity, ServerPlayerEntity player) {
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(mana);
        buffer.writeFloat(opacity);
        ServerPlayNetworking.send(player, ModMessages.GRIPCRYSTAL_MANA_HANDLER, buffer);
    }
}
