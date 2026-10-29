package net.sashakyotoz.client.particles.custom.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.gameevent.PositionSource;
import net.sashakyotoz.client.particles.ModParticleTypes;

public class WindVibrationParticleEffect implements ParticleOptions {
    public static final MapCodec<WindVibrationParticleEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            PositionSource.CODEC.fieldOf("destination").forGetter(WindVibrationParticleEffect::getVibration),
                            Codec.INT.fieldOf("arrival_in_ticks").forGetter(WindVibrationParticleEffect::getArrivalInTicks)
                    )
                    .apply(instance, WindVibrationParticleEffect::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, WindVibrationParticleEffect> STREAM_CODEC = StreamCodec.composite(
            PositionSource.STREAM_CODEC, WindVibrationParticleEffect::getVibration,
            ByteBufCodecs.VAR_INT, WindVibrationParticleEffect::getArrivalInTicks,
            WindVibrationParticleEffect::new
    );
    private final PositionSource destination;
    private final int arrivalInTicks;

    public WindVibrationParticleEffect(PositionSource destination, int arrivalInTicks) {
        this.destination = destination;
        this.arrivalInTicks = arrivalInTicks;
    }

    @Override
    public ParticleType<WindVibrationParticleEffect> getType() {
        return ModParticleTypes.WIND_VIBRATION;
    }

    public PositionSource getVibration() {
        return this.destination;
    }

    public int getArrivalInTicks() {
        return this.arrivalInTicks;
    }
}
