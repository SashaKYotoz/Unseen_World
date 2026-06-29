package net.sashakyotoz.client.particles.custom.effects;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.sashakyotoz.client.particles.ModParticleTypes;

import java.util.Locale;

public record LeafParticleEffect(float red, float green, float blue) implements ParticleOptions {
    public static final Codec<LeafParticleEffect> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.FLOAT.fieldOf("red").forGetter(effect -> effect.red),
                            Codec.FLOAT.fieldOf("green").forGetter(effect -> effect.green),
                            Codec.FLOAT.fieldOf("blue").forGetter(effect -> effect.blue)
                    )
                    .apply(instance, LeafParticleEffect::new)
    );
    public static final Deserializer<LeafParticleEffect> PARAMETERS_FACTORY = new Deserializer<>() {
        public LeafParticleEffect fromCommand(ParticleType<LeafParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            float r = stringReader.readFloat();
            stringReader.expect(' ');
            float g = stringReader.readFloat();
            stringReader.expect(' ');
            float b = stringReader.readFloat();
            return new LeafParticleEffect(r, g, b);
        }

        public LeafParticleEffect fromNetwork(ParticleType<LeafParticleEffect> particleType, FriendlyByteBuf packetByteBuf) {
            CompoundTag tag = packetByteBuf.readNbt();
            float r = tag.getFloat("red");
            float g = tag.getFloat("green");
            float b = tag.getFloat("blue");
            return new LeafParticleEffect(r, g, b);
        }
    };

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        CompoundTag nbtElement = new CompoundTag();
        nbtElement.putFloat("red", this.red);
        nbtElement.putFloat("green", this.green);
        nbtElement.putFloat("blue", this.blue);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %d", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), red,green,blue);
    }

    @Override
    public ParticleType<LeafParticleEffect> getType() {
        return ModParticleTypes.LEAF;
    }
}