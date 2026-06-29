package net.sashakyotoz.client.particles.custom.effects;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.PositionSourceType;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.client.particles.ModParticleTypes;

import java.util.Locale;

public class WindVibrationParticleEffect implements ParticleOptions {
    public static final Codec<WindVibrationParticleEffect> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            PositionSource.CODEC.fieldOf("destination").forGetter(effect -> effect.destination),
                            Codec.INT.fieldOf("arrival_in_ticks").forGetter(effect -> effect.arrivalInTicks)
                    )
                    .apply(instance, WindVibrationParticleEffect::new)
    );
    public static final ParticleOptions.Deserializer<WindVibrationParticleEffect> PARAMETERS_FACTORY = new ParticleOptions.Deserializer<>() {
        public WindVibrationParticleEffect fromCommand(ParticleType<WindVibrationParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            float f = (float) stringReader.readDouble();
            stringReader.expect(' ');
            float g = (float) stringReader.readDouble();
            stringReader.expect(' ');
            float h = (float) stringReader.readDouble();
            stringReader.expect(' ');
            int i = stringReader.readInt();
            BlockPos blockPos = BlockPos.containing(f, g, h);
            return new WindVibrationParticleEffect(new BlockPositionSource(blockPos), i);
        }

        public WindVibrationParticleEffect fromNetwork(ParticleType<WindVibrationParticleEffect> particleType, FriendlyByteBuf packetByteBuf) {
            PositionSource positionSource = PositionSourceType.fromNetwork(packetByteBuf);
            int i = packetByteBuf.readVarInt();
            return new WindVibrationParticleEffect(positionSource, i);
        }
    };
    private final PositionSource destination;
    private final int arrivalInTicks;

    public WindVibrationParticleEffect(PositionSource destination, int arrivalInTicks) {
        this.destination = destination;
        this.arrivalInTicks = arrivalInTicks;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        PositionSourceType.toNetwork(this.destination, buf);
        buf.writeVarInt(this.arrivalInTicks);
    }

    @Override
    public String writeToString() {
        Vec3 vec3d = this.destination.getPosition(null).get();
        double d = vec3d.x();
        double e = vec3d.y();
        double f = vec3d.z();
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %d", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), d, e, f, this.arrivalInTicks);
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