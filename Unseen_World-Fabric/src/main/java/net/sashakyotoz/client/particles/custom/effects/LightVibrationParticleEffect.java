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

public class LightVibrationParticleEffect implements ParticleOptions {
    public static final Codec<LightVibrationParticleEffect> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            PositionSource.CODEC.fieldOf("destination").forGetter(effect -> effect.destination),
                            Codec.INT.fieldOf("arrival_in_ticks").forGetter(effect -> effect.arrivalInTicks)
                    )
                    .apply(instance, LightVibrationParticleEffect::new)
    );
    public static final Deserializer<LightVibrationParticleEffect> PARAMETERS_FACTORY = new Deserializer<>() {
        public LightVibrationParticleEffect fromCommand(ParticleType<LightVibrationParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            float f = (float) stringReader.readDouble();
            stringReader.expect(' ');
            float g = (float) stringReader.readDouble();
            stringReader.expect(' ');
            float h = (float) stringReader.readDouble();
            stringReader.expect(' ');
            int i = stringReader.readInt();
            BlockPos blockPos = BlockPos.containing(f, g, h);
            return new LightVibrationParticleEffect(new BlockPositionSource(blockPos), i);
        }

        public LightVibrationParticleEffect fromNetwork(ParticleType<LightVibrationParticleEffect> particleType, FriendlyByteBuf packetByteBuf) {
            PositionSource positionSource = PositionSourceType.fromNetwork(packetByteBuf);
            int i = packetByteBuf.readVarInt();
            return new LightVibrationParticleEffect(positionSource, i);
        }
    };
    private final PositionSource destination;
    private final int arrivalInTicks;

    public LightVibrationParticleEffect(PositionSource destination, int arrivalInTicks) {
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