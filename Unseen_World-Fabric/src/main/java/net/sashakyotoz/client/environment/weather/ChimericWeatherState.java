package net.sashakyotoz.client.environment.weather;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.saveddata.SavedData;
import net.sashakyotoz.common.networking.ModMessages;

public class ChimericWeatherState extends SavedData {
    private static final int MIN_CLEAR_TIME = 12000;
    private static final int MAX_CLEAR_TIME = 24000;
    private static final int GRIPPFALL_DURATION = 6000;
    private static final String[] DIRECTIONS = {"north", "east", "south", "west"};

    private boolean isGrippfallActive = false;
    private String direction = "north";
    private int gripFallTicks = 0;
    private int gripFallCleanTicks = MIN_CLEAR_TIME;

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
        state.direction = tag.getString("direction");
        state.gripFallTicks = tag.getInt("gripFallTicks");
        state.gripFallCleanTicks = tag.getInt("gripFallCleanTicks");
        return state;
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
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
                this.isGrippfallActive = false;
                this.gripFallCleanTicks = MIN_CLEAR_TIME + world.random.nextInt(MAX_CLEAR_TIME - MIN_CLEAR_TIME);
                this.syncToAll();
                this.setDirty();
            }
        } else {
            if (this.gripFallCleanTicks > 0) {
                this.gripFallCleanTicks--;
            } else {
                this.isGrippfallActive = true;
                this.gripFallTicks = GRIPPFALL_DURATION;
                this.direction = DIRECTIONS[this.world.getRandom().nextInt(DIRECTIONS.length)];

                this.syncToAll();
                this.setDirty();
            }
        }
    }

    public void syncToAll() {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(this.isGrippfallActive);
        buf.writeUtf(this.direction);

        for (ServerPlayer player : world.players()) {
            ServerPlayNetworking.send(player, ModMessages.WEATHER_PACKET_ID, buf);
        }
    }

    public void syncToPlayer(ServerPlayer player) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(this.isGrippfallActive);
        buf.writeUtf(this.direction);
        ServerPlayNetworking.send(player, ModMessages.WEATHER_PACKET_ID, buf);
    }

    public void setGrippfallDuration(int duration) {
        if (duration > 0) {
            this.isGrippfallActive = true;
            this.gripFallTicks = duration;
            this.gripFallCleanTicks = 0;
        } else {
            this.isGrippfallActive = false;
            this.gripFallTicks = 0;
            this.gripFallCleanTicks = UniformInt.of(MIN_CLEAR_TIME, MAX_CLEAR_TIME).sample(this.world.random);
        }
        this.setDirty();
        this.syncToAll();
    }

    public boolean isGrippfallActive() {
        return isGrippfallActive;
    }
}