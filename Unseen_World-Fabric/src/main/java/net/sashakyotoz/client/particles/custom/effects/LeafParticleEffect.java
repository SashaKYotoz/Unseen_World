package net.sashakyotoz.client.particles.custom.effects;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.sashakyotoz.client.particles.ModParticleTypes;

import java.util.Locale;

public record LeafParticleEffect(float red, float green, float blue) implements ParticleEffect {
    public static final Codec<LeafParticleEffect> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.FLOAT.fieldOf("red").forGetter(effect -> effect.red),
                            Codec.FLOAT.fieldOf("green").forGetter(effect -> effect.green),
                            Codec.FLOAT.fieldOf("blue").forGetter(effect -> effect.blue)
                    )
                    .apply(instance, LeafParticleEffect::new)
    );
    public static final Factory<LeafParticleEffect> PARAMETERS_FACTORY = new Factory<>() {
        public LeafParticleEffect read(ParticleType<LeafParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            float r = stringReader.readFloat();
            stringReader.expect(' ');
            float g = stringReader.readFloat();
            stringReader.expect(' ');
            float b = stringReader.readFloat();
            return new LeafParticleEffect(r, g, b);
        }

        public LeafParticleEffect read(ParticleType<LeafParticleEffect> particleType, PacketByteBuf packetByteBuf) {
            NbtCompound tag = packetByteBuf.readNbt();
            float r = tag.getFloat("red");
            float g = tag.getFloat("green");
            float b = tag.getFloat("blue");
            return new LeafParticleEffect(r, g, b);
        }
    };

    @Override
    public void write(PacketByteBuf buf) {
        NbtCompound nbtElement = new NbtCompound();
        nbtElement.putFloat("red", this.red);
        nbtElement.putFloat("green", this.green);
        nbtElement.putFloat("blue", this.blue);
    }

    @Override
    public String asString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %d", Registries.PARTICLE_TYPE.getId(this.getType()), red,green,blue);
    }

    @Override
    public ParticleType<LeafParticleEffect> getType() {
        return ModParticleTypes.LEAF;
    }
}