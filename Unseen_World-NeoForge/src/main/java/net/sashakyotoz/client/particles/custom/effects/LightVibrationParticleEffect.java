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

public class LightVibrationParticleEffect implements ParticleOptions {
    public static final MapCodec<LightVibrationParticleEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            PositionSource.CODEC.fieldOf("destination").forGetter(LightVibrationParticleEffect::getVibration),
                            Codec.INT.fieldOf("arrival_in_ticks").forGetter(LightVibrationParticleEffect::getArrivalInTicks)
                    )
                    .apply(instance, LightVibrationParticleEffect::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, LightVibrationParticleEffect> STREAM_CODEC = StreamCodec.composite(
            PositionSource.STREAM_CODEC, LightVibrationParticleEffect::getVibration,
            ByteBufCodecs.VAR_INT, LightVibrationParticleEffect::getArrivalInTicks,
            LightVibrationParticleEffect::new
    );
    private final PositionSource destination;
    private final int arrivalInTicks;

    public LightVibrationParticleEffect(PositionSource destination, int arrivalInTicks) {
        this.destination = destination;
        this.arrivalInTicks = arrivalInTicks;
    }

    @Override
    public ParticleType<LightVibrationParticleEffect> getType() {
        return ModParticleTypes.LIGHT_VIBRATION;
    }

    public PositionSource getVibration() {
        return this.destination;
    }

    public int getArrivalInTicks() {
        return this.arrivalInTicks;
    }
}
