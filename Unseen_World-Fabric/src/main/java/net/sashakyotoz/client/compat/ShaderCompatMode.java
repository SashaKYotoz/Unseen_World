package net.sashakyotoz.client.compat;

public enum ShaderCompatMode {
    VANILLA,
    NETHER_PROFILE,
    PACK_DEFAULT;

    private static volatile ShaderCompatMode current = VANILLA;

    public static ShaderCompatMode get() {
        return current;
    }

    public static void set(ShaderCompatMode mode) {
        current = mode == null ? VANILLA : mode;
    }
}