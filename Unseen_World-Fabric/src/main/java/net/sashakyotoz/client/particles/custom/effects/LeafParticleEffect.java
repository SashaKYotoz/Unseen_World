package net.sashakyotoz.client.particles.custom.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.sashakyotoz.client.particles.ModParticleTypes;

public record LeafParticleEffect(float red, float green, float blue) implements ParticleOptions {
    public static final MapCodec<LeafParticleEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Codec.FLOAT.fieldOf("red").forGetter(LeafParticleEffect::red),
                            Codec.FLOAT.fieldOf("green").forGetter(LeafParticleEffect::green),
                            Codec.FLOAT.fieldOf("blue").forGetter(LeafParticleEffect::blue)
                    )
                    .apply(instance, LeafParticleEffect::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, LeafParticleEffect> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, LeafParticleEffect::red,
            ByteBufCodecs.FLOAT, LeafParticleEffect::green,
            ByteBufCodecs.FLOAT, LeafParticleEffect::blue,
            LeafParticleEffect::new
    );

    @Override
    public ParticleType<LeafParticleEffect> getType() {
        return ModParticleTypes.LEAF;
    }
}
