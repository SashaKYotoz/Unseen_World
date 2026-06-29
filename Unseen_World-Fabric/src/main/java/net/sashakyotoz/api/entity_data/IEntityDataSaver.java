package net.sashakyotoz.api.entity_data;

import net.minecraft.nbt.CompoundTag;

public interface IEntityDataSaver {
    CompoundTag getPersistentData();
}