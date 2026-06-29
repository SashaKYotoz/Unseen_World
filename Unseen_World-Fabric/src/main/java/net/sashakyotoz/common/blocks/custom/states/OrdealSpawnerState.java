package net.sashakyotoz.common.blocks.custom.states;

import net.minecraft.util.StringRepresentable;

public enum OrdealSpawnerState implements StringRepresentable {
    INACTIVE("inactive", 0, -1),
    WAITING_FOR_PLAYERS("waiting_for_players", 4, 200),
    ACTIVE("active", 8, 1000),
    EJECTING_REWARD("ejecting_reward", 8, -1),
    COOLDOWN("cooldown", 0, -1);
    public final String id;
    public final int luminance;
    public final int rotationSpeed;

    OrdealSpawnerState(String id, int luminance, int rotationSpeed) {
        this.id = id;
        this.luminance = luminance;
        this.rotationSpeed = rotationSpeed;
    }

    @Override
    public String getSerializedName() {
        return this.id;
    }
}