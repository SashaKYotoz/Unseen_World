package net.sashakyotoz.client.environment.weather;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.saveddata.SavedData;
import net.sashakyotoz.common.networking.ModMessages;

public class ChimericWeatherState extends SavedData {
    private static final IntProvider CLEAR_TIME = UniformInt.of(12000, 32000);
    private static final int GRIPPFALL_DURATION = 6000;
    private static final String[] DIRECTIONS = {"north", "east", "south", "west"};

    private boolean isGrippfallActive = false;
    private String direction = DIRECTIONS[0];
    private int gripFallTicks = 0;
    private int gripFallCleanTicks = 12000;

    private ServerLevel world;

    public static ChimericWeatherState get(ServerLevel world) {
        ChimericWeatherState state = world.getDataStorage().computeIfAbsent(
                ChimericWeatherState::fromNbt,
                ChimericWeatherState::new,
                "chimeric_weather"
        );
        state.world = world;
        return state;
    }

    public static ChimericWeatherState fromNbt(CompoundTag tag) {
        ChimericWeatherState state = new ChimericWeatherState();
        state.isGrippfallActive = tag.getBoolean("isGrippfallActive");
        String direction = tag.getString("direction");
        state.direction = direction.isEmpty() ? DIRECTIONS[0] : direction;
        state.gripFallTicks = tag.getInt("gripFallTicks");
        state.gripFallCleanTicks = tag.getInt("gripFallCleanTicks");
        return state;
    }

    @Override
    public CompoundTag save(CompoundTag nbt, HolderLookup.Provider registries) {
        nbt.putBoolean("isGrippfallActive", isGrippfallActive);
        nbt.putString("direction", direction);
        nbt.putInt("gripFallTicks", gripFallTicks);
        nbt.putInt("gripFallCleanTicks", gripFallCleanTicks);
        return nbt;
    }

    public void tick() {
        if (this.isGrippfallActive) {
            if (this.gripFallTicks > 0) {
                this.gripFallTicks--;
            } else {
                this.clearGrippfall(-1);
            }
        } else if (this.gripFallCleanTicks > 0) {
            this.gripFallCleanTicks--;
        } else {
            this.startGrippfall(-1);
        }
    }

    public void startGrippfall(int duration) {
        this.isGrippfallActive = true;
        this.gripFallTicks = duration > 0 ? duration : GRIPPFALL_DURATION;
        this.gripFallCleanTicks = 0;
        this.direction = DIRECTIONS[this.world.getRandom().nextInt(DIRECTIONS.length)];
        this.setDirty();
        this.syncToAll();
    }

    public void clearGrippfall(int duration) {
        boolean wasActive = this.isGrippfallActive;
        this.isGrippfallActive = false;
        this.gripFallTicks = 0;
        this.gripFallCleanTicks = duration > 0 ? duration : CLEAR_TIME.sample(this.world.getRandom());
        this.setDirty();
        if (wasActive) {
            this.syncToAll();
        }
    }

    public void syncToAll() {
        for (ServerPlayer player : this.world.players()) {
            this.syncTo(player);
        }
    }

    public void syncTo(ServerPlayer player) {
        ModMessages.sendWeather(player, this.isGrippfallActive, this.direction);
    }

    public boolean isGrippfallActive() {
        return isGrippfallActive;
    }
}