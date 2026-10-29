package net.sashakyotoz.common.world.carvers.custom.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;

public class SpiralCaveCarverConfig extends CarverConfiguration {
    public static final MapCodec<SpiralCaveCarverConfig> CONFIG_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(config -> config.probability),
            HeightProvider.CODEC.fieldOf("y").forGetter(config -> config.y),
            FloatProvider.CODEC.fieldOf("yScale").forGetter(config -> config.yScale),
            VerticalAnchor.CODEC.fieldOf("lava_level").forGetter(config -> config.lavaLevel),
            CarverDebugSettings.CODEC.optionalFieldOf("debug_settings", CarverDebugSettings.DEFAULT).forGetter(config -> config.debugSettings),
            RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("replaceable").forGetter(config -> config.replaceable),
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
            VerticalAnchor lavaLevel,
            CarverDebugSettings debugConfig,
            HolderSet<Block> replaceable,
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