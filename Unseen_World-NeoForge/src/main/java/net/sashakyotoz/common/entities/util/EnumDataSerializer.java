package net.sashakyotoz.common.entities.util;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;

public final class EnumDataSerializer {
    private EnumDataSerializer() {
    }

    public static <E extends Enum<E>> EntityDataSerializer<E> of(Class<E> type) {
        E[] values = type.getEnumConstants();
        return EntityDataSerializer.forValueType(ByteBufCodecs.idMapper(index -> values[index], Enum::ordinal));
    }
}
