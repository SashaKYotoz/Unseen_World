package net.sashakyotoz.common.world.carvers.custom.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.carver.CarverConfig;
import net.minecraft.world.gen.carver.CarverDebugConfig;
import net.minecraft.world.gen.heightprovider.HeightProvider;

public class SpiralCaveCarverConfig extends CarverConfig {
    public static final MapCodec<SpiralCaveCarverConfig> CONFIG_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(config -> config.probability),
            HeightProvider.CODEC.fieldOf("y").forGetter(config -> config.y),
            FloatProvider.VALUE_CODEC.fieldOf("yScale").forGetter(config -> config.yScale),
            YOffset.OFFSET_CODEC.fieldOf("lava_level").forGetter(config -> config.lavaLevel),
            CarverDebugConfig.CODEC.optionalFieldOf("debug_settings", CarverDebugConfig.DEFAULT).forGetter(config -> config.debugConfig),
            RegistryCodecs.entryList(RegistryKeys.BLOCK).fieldOf("replaceable").forGetter(config -> config.replaceable),
            Codec.FLOAT.fieldOf("radius").forGetter(config -> config.radius),
            Codec.INT.fieldOf("length").forGetter(config -> config.length),
            Codec.FLOAT.fieldOf("turn_rate").forGetter(config -> config.turnRate),
            Codec.FLOAT.fieldOf("radius_variance").forGetter(config -> config.radiusVariance),
            Codec.INT.fieldOf("vertical_spread").forGetter(config -> config.verticalSpread)
    ).apply(instance, SpiralCaveCarverConfig::new));
    public static final Codec<SpiralCaveCarverConfig> CODEC = CONFIG_CODEC.codec();

    public final float radius;
    public final int length;
    public final float turnRate;
    public final float radiusVariance;
    public final int verticalSpread;

    public SpiralCaveCarverConfig(
            float probability,
            HeightProvider y,
            FloatProvider yScale,
            YOffset lavaLevel,
            CarverDebugConfig debugConfig,
            RegistryEntryList<Block> replaceable,
            float radius,
            int length,
            float turnRate,
            float radiusVariance,
            int verticalSpread
    ) {
        super(probability, y, yScale, lavaLevel, debugConfig, replaceable);
        this.radius = radius;
        this.length = length;
        this.turnRate = turnRate;
        this.radiusVariance = radiusVariance;
        this.verticalSpread = verticalSpread;
    }

    public float radius() {
        return this.radius;
    }

    public int length() {
        return this.length;
    }

    public float turnRate() {
        return this.turnRate;
    }

    public float radiusVariance() {
        return this.radiusVariance;
    }

    public int verticalSpread() {
        return this.verticalSpread;
    }
}