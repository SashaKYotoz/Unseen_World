package net.sashakyotoz.client.environment.weather;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.PersistentState;
import net.sashakyotoz.common.networking.ModMessages;

public class ChimericWeatherState extends PersistentState {
    private static final int MIN_CLEAR_TIME = 12000;
    private static final int MAX_CLEAR_TIME = 24000;
    private static final int GRIPPFALL_DURATION = 6000;
    private static final String[] DIRECTIONS = {"north", "east", "south", "west"};

    private boolean isGrippfallActive = false;
    private String direction = "north";
    private int gripFallTicks = 0;
    private int gripFallCleanTicks = MIN_CLEAR_TIME;

    private ServerWorld world;

    public static ChimericWeatherState get(ServerWorld world) {
        ChimericWeatherState state = world.getPersistentStateManager().getOrCreate(
                ChimericWeatherState::fromNbt,
                ChimericWeatherState::new,
                "chimeric_weather"
        );
        state.world = world;
        return state;
    }

    public static ChimericWeatherState fromNbt(NbtCompound tag) {
        ChimericWeatherState state = new ChimericWeatherState();
        state.isGrippfallActive = tag.getBoolean("isGrippfallActive");
        state.direction = tag.getString("direction");
        state.gripFallTicks = tag.getInt("gripFallTicks");
        state.gripFallCleanTicks = tag.getInt("gripFallCleanTicks");
        return state;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
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
                this.markDirty();
            }
        } else {
            if (this.gripFallCleanTicks > 0) {
                this.gripFallCleanTicks--;
            } else {
                this.isGrippfallActive = true;
                this.gripFallTicks = GRIPPFALL_DURATION;
                this.direction = DIRECTIONS[this.world.getRandom().nextInt(DIRECTIONS.length)];

                this.syncToAll();
                this.markDirty();
            }
        }
    }

    public void syncToAll() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(this.isGrippfallActive);
        buf.writeString(this.direction);

        for (ServerPlayerEntity player : world.getPlayers()) {
            ServerPlayNetworking.send(player, ModMessages.WEATHER_PACKET_ID, buf);
        }
    }

    public void syncToPlayer(ServerPlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(this.isGrippfallActive);
        buf.writeString(this.direction);
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
            this.gripFallCleanTicks = UniformIntProvider.create(MIN_CLEAR_TIME, MAX_CLEAR_TIME).get(this.world.random);
        }
        this.markDirty();
        this.syncToAll();
    }

    public boolean isGrippfallActive() {
        return isGrippfallActive;
    }
}