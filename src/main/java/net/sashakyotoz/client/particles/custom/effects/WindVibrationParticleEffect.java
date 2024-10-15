package net.sashakyotoz.client.particles.custom.effects;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.PositionSourceType;
import net.sashakyotoz.client.particles.ModParticleTypes;

import java.util.Locale;

public class WindVibrationParticleEffect implements ParticleEffect {
    public static final Codec<WindVibrationParticleEffect> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            PositionSource.CODEC.fieldOf("destination").forGetter(effect -> effect.destination),
                            Codec.INT.fieldOf("arrival_in_ticks").forGetter(effect -> effect.arrivalInTicks)
                    )
                    .apply(instance, WindVibrationParticleEffect::new)
    );
    public static final ParticleEffect.Factory<WindVibrationParticleEffect> PARAMETERS_FACTORY = new ParticleEffect.Factory<>() {
        public WindVibrationParticleEffect read(ParticleType<WindVibrationParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            float f = (float) stringReader.readDouble();
            stringReader.expect(' ');
            float g = (float) stringReader.readDouble();
            stringReader.expect(' ');
            float h = (float) stringReader.readDouble();
            stringReader.expect(' ');
            int i = stringReader.readInt();
            BlockPos blockPos = BlockPos.ofFloored(f, g, h);
            return new WindVibrationParticleEffect(new BlockPositionSource(blockPos), i);
        }

        public WindVibrationParticleEffect read(ParticleType<WindVibrationParticleEffect> particleType, PacketByteBuf packetByteBuf) {
            PositionSource positionSource = PositionSourceType.read(packetByteBuf);
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
    public void write(PacketByteBuf buf) {
        PositionSourceType.write(this.destination, buf);
        buf.writeVarInt(this.arrivalInTicks);
    }

    @Override
    public String asString() {
        Vec3d vec3d = this.destination.getPos(null).get();
        double d = vec3d.getX();
        double e = vec3d.getY();
        double f = vec3d.getZ();
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %d", Registries.PARTICLE_TYPE.getId(this.getType()), d, e, f, this.arrivalInTicks);
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