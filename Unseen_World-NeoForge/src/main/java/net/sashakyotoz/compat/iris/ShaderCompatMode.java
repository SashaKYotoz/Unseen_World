package net.sashakyotoz.compat.iris;

import com.google.gson.JsonElement;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.config.ModMainConfig;

import java.util.Locale;

public enum ShaderCompatMode {
    VANILLA,
    NETHER_PROFILE,
    PACK_DEFAULT;

    public static ShaderCompatMode get() {
        ShaderCompatMode mode = ModMainConfig.shaderCompatMode.get();
        return mode == null ? VANILLA : mode;
    }

    public String configName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public static ShaderCompatMode fromJson(JsonElement json) {
        if (json == null || !json.isJsonPrimitive()) {
            UnseenWorld.log().warn("Invalid shader_compat_mode value " + json + ", using " + VANILLA.configName());
            return VANILLA;
        }
        String value = json.getAsString().trim();
        for (ShaderCompatMode mode : values()) {
            if (mode.configName().equalsIgnoreCase(value)) {
                return mode;
            }
        }
        UnseenWorld.log().warn("Unknown shader_compat_mode \"" + value + "\", expected vanilla, nether_profile or pack_default; using " + VANILLA.configName());
        return VANILLA;
    }
}