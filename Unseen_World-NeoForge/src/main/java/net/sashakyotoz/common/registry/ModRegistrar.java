package net.sashakyotoz.common.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ModRegistrar {
    private static final Map<ResourceKey<? extends Registry<?>>, List<Entry<?>>> PENDING = new LinkedHashMap<>();

    private ModRegistrar() {
    }

    public static <T, V extends T> V register(ResourceKey<? extends Registry<T>> registry, ResourceLocation id, V value) {
        PENDING.computeIfAbsent(registry, key -> new ArrayList<>()).add(new Entry<>(registry, id, value));
        return value;
    }

    public static <T> Holder<T> registerForHolder(ResourceKey<Registry<T>> registry, ResourceLocation id, T value) {
        register(registry, id, value);
        return DeferredHolder.create(registry, id);
    }

    public static void onRegister(RegisterEvent event) {
        List<Entry<?>> entries = PENDING.get(event.getRegistryKey());
        if (entries != null) {
            for (Entry<?> entry : entries) {
                entry.register(event);
            }
        }
    }

    private record Entry<T>(ResourceKey<? extends Registry<T>> registry, ResourceLocation id, T value) {
        void register(RegisterEvent event) {
            event.register(this.registry, this.id, () -> this.value);
        }
    }
}
