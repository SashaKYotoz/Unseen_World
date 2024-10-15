package net.sashakyotoz.utils;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;

public class ReflectionHelper {
    public static Object2ObjectMap<Identifier, DimensionEffects> getMap() {
        try {
            Field field = DimensionEffects.class.getDeclaredField("BY_IDENTIFIER");
            field.setAccessible(true);
            return (Object2ObjectMap<Identifier, DimensionEffects>) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}