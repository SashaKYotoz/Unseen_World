package net.sashakyotoz.common.config;

import net.sashakyotoz.UnseenWorld;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

//a bit of tweaks to Lyof's management:
//https://github.com/Lyof429/EndsPhantasm/blob/master/src/main/java/net/lyof/phantasm/config/ConfigEntry.java
public class ConfigEntry<T> implements Supplier<T> {
    private final List<String> path;
    private final T fallback;

    public ConfigEntry(String path, @Nullable T fallback) {
        this.path = List.of(path.split("\\."));
        this.fallback = fallback;
    }

    public T get() {
        return this.get(this.fallback);
    }

    @SuppressWarnings("unchecked")
    public T get(T fallback) {
        Map next = ConfigController.CONFIG;
        Object result = null;

        for (String step : this.path) {
            try {
                next = (Map) next.get(step);
            } catch (Exception e) {
                if (Objects.equals(step, this.path.get(this.path.size() - 1)))
                    result = next.get(step);
                else {
                    UnseenWorld.log("Got past end of path : \"" + this.path + "\", defaulting to " + fallback);
                    return fallback;
                }
            }
            if (next == null) {
                UnseenWorld.log("Couldn't get to end of path : \"" + this.path + "\", defaulting to " + fallback);
                return fallback;
            }
        }

        if (fallback instanceof Integer)
            return (T) (Integer) Long.valueOf(Math.round(Double.parseDouble(String.valueOf(result)))).intValue();
        if (fallback instanceof Double)
            return (T) Double.valueOf(String.valueOf(result));
        if (fallback instanceof String)
            return (T) String.valueOf(result);
        if (fallback instanceof Boolean)
            return (T) Boolean.valueOf(String.valueOf(result));
        if (fallback instanceof Map)
            return (T) next;
        if (fallback instanceof List)
            return (T) result;

        UnseenWorld.log("Couldn't find config value for path : \"" + this.path + "\", defaulting to " + fallback);
        return fallback;
    }
}