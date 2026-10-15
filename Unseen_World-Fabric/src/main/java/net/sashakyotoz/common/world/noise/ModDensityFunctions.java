package net.sashakyotoz.common.world.noise;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.sashakyotoz.UnseenWorld;

public final class ModDensityFunctions {
    public static final int MIN_Y = -64;
    public static final int HEIGHT = 384;
    public static final int MAX_Y = MIN_Y + HEIGHT;

    public static final ResourceKey<DensityFunction> TEMPERATURE = key("chimeric_darkness/temperature");
    public static final ResourceKey<DensityFunction> VEGETATION = key("chimeric_darkness/vegetation");
    public static final ResourceKey<DensityFunction> VALLEY_MASK = key("chimeric_darkness/valley_mask");
    public static final ResourceKey<DensityFunction> VALLEY_PILLARS = key("chimeric_darkness/valley_pillars");
    public static final ResourceKey<DensityFunction> SPIRE_MASK = key("chimeric_darkness/spire_mask");
    public static final ResourceKey<DensityFunction> SEA_SPIRES = key("chimeric_darkness/sea_spires");
    public static final ResourceKey<DensityFunction> CAVES = key("chimeric_darkness/caves");
    public static final ResourceKey<DensityFunction> INITIAL_DENSITY = key("chimeric_darkness/initial_density");
    public static final ResourceKey<DensityFunction> FINAL_DENSITY = key("chimeric_darkness/final_density");

    private static final double PILLAR_THRESHOLD = 0.55D;
    private static final double PILLAR_SHARPNESS = 6.0D;
    private static final double PILLAR_BASE_Y = 70.0D;
    private static final double PILLAR_RISE = 44.0D;
    private static final double PILLAR_SOFTNESS = 16.0D;
    private static final int PILLAR_FOOT_Y = 40;

    private static final double SPIRE_THRESHOLD = 0.62D;
    private static final double SPIRE_SHARPNESS = 7.0D;
    private static final double SPIRE_BASE_Y = 50.0D;
    private static final double SPIRE_RISE = 40.0D;
    private static final double SPIRE_SOFTNESS = 10.0D;
    private static final int SPIRE_FOOT_Y = 12;

    private ModDensityFunctions() {
    }

    public static void bootstrap(BootstapContext<DensityFunction> context) {
        HolderGetter<DensityFunction> functions = context.lookup(Registries.DENSITY_FUNCTION);
        HolderGetter<NormalNoise.NoiseParameters> noises = context.lookup(Registries.NOISE);

        DensityFunction shiftX = vanilla(functions, "shift_x");
        DensityFunction shiftZ = vanilla(functions, "shift_z");
        DensityFunction continents = vanilla(functions, "overworld/continents");
        DensityFunction erosion = vanilla(functions, "overworld/erosion");
        DensityFunction ridges = vanilla(functions, "overworld/ridges");
        DensityFunction depth = vanilla(functions, "overworld/depth");
        DensityFunction factor = vanilla(functions, "overworld/factor");
        DensityFunction slopedCheese = vanilla(functions, "overworld/sloped_cheese");
        DensityFunction entrances = vanilla(functions, "overworld/caves/entrances");
        DensityFunction noodle = vanilla(functions, "overworld/caves/noodle");
        DensityFunction pillars = vanilla(functions, "overworld/caves/pillars");
        DensityFunction spaghetti2d = vanilla(functions, "overworld/caves/spaghetti_2d");
        DensityFunction roughness = vanilla(functions, "overworld/caves/spaghetti_roughness_function");

        DensityFunction temperature = holder(context.register(TEMPERATURE,
                DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25D, noises.getOrThrow(Noises.TEMPERATURE))));
        context.register(VEGETATION, DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25D, noises.getOrThrow(Noises.VEGETATION)));

        DensityFunction valleyMask = holder(context.register(VALLEY_MASK, DensityFunctions.cache2d(product(
                ramp(erosion, ChimericDarknessClimate.VALLEY_EROSION_MIN, ChimericDarknessClimate.VALLEY_EROSION_MIN + ChimericDarknessClimate.MASK_FADE),
                ramp(erosion, ChimericDarknessClimate.VALLEY_EROSION_MAX, ChimericDarknessClimate.VALLEY_EROSION_MAX - ChimericDarknessClimate.MASK_FADE),
                ramp(continents, ChimericDarknessClimate.VALLEY_CONTINENTALNESS_MIN, ChimericDarknessClimate.VALLEY_CONTINENTALNESS_MIN + ChimericDarknessClimate.MASK_FADE),
                ramp(continents, ChimericDarknessClimate.VALLEY_CONTINENTALNESS_MAX, ChimericDarknessClimate.VALLEY_CONTINENTALNESS_MAX - ChimericDarknessClimate.MASK_FADE),
                ramp(ridges.abs(), ChimericDarknessClimate.RIVER_WEIRDNESS, ChimericDarknessClimate.RIVER_WEIRDNESS + ChimericDarknessClimate.MASK_FADE)))));

        DensityFunction spireMask = holder(context.register(SPIRE_MASK, DensityFunctions.cache2d(product(
                ramp(continents, ChimericDarknessClimate.SPIRE_CONTINENTALNESS_MAX, ChimericDarknessClimate.SPIRE_CONTINENTALNESS_MAX - ChimericDarknessClimate.MASK_FADE),
                ramp(continents, ChimericDarknessClimate.SPIRE_CONTINENTALNESS_MIN, ChimericDarknessClimate.SPIRE_CONTINENTALNESS_MIN + ChimericDarknessClimate.MASK_FADE),
                ramp(temperature, ChimericDarknessClimate.SPIRE_TEMPERATURE_MAX, ChimericDarknessClimate.SPIRE_TEMPERATURE_MAX - ChimericDarknessClimate.MASK_FADE)))));

        DensityFunction valleyPillars = holder(context.register(VALLEY_PILLARS, masked(valleyMask, columns(
                noises.getOrThrow(ModNoises.VALLEY_PILLAR), noises.getOrThrow(ModNoises.VALLEY_PILLAR_HEIGHT),
                PILLAR_THRESHOLD, PILLAR_SHARPNESS, PILLAR_BASE_Y, PILLAR_RISE, PILLAR_SOFTNESS, PILLAR_FOOT_Y))));

        DensityFunction seaSpires = holder(context.register(SEA_SPIRES, masked(spireMask, columns(
                noises.getOrThrow(ModNoises.SEA_SPIRE), noises.getOrThrow(ModNoises.SEA_SPIRE_HEIGHT),
                SPIRE_THRESHOLD, SPIRE_SHARPNESS, SPIRE_BASE_Y, SPIRE_RISE, SPIRE_SOFTNESS, SPIRE_FOOT_Y))));

        DensityFunction caveLayer = DensityFunctions.mul(DensityFunctions.constant(4.0D), DensityFunctions.noise(noises.getOrThrow(Noises.CAVE_LAYER), 3.0D, 6.0D).square());
        DensityFunction cheese = DensityFunctions.add(
                DensityFunctions.add(DensityFunctions.constant(0.27D), DensityFunctions.noise(noises.getOrThrow(ModNoises.CAVE_CHEESE), 1.0D, 1.5D)).clamp(-1.0D, 1.0D),
                DensityFunctions.add(DensityFunctions.constant(1.5D), DensityFunctions.mul(DensityFunctions.constant(-0.64D), slopedCheese)).clamp(0.0D, 0.5D));
        DensityFunction carved = DensityFunctions.min(
                DensityFunctions.min(DensityFunctions.add(caveLayer, cheese), entrances),
                DensityFunctions.add(spaghetti2d, roughness));
        DensityFunction caves = holder(context.register(CAVES, DensityFunctions.max(carved,
                DensityFunctions.rangeChoice(pillars, -1000000.0D, 0.03D, DensityFunctions.constant(-1000000.0D), pillars))));

        context.register(INITIAL_DENSITY, slide(
                DensityFunctions.add(
                        DensityFunctions.mul(DensityFunctions.constant(4.0D), DensityFunctions.mul(depth, DensityFunctions.cache2d(factor)).quarterNegative()),
                        DensityFunctions.constant(-0.703125D)).clamp(-64.0D, 64.0D)));

        DensityFunction terrain = DensityFunctions.rangeChoice(slopedCheese, -1000000.0D, 1.5625D,
                DensityFunctions.min(slopedCheese, DensityFunctions.mul(DensityFunctions.constant(5.0D), entrances)), caves);
        DensityFunction shaped = DensityFunctions.mul(DensityFunctions.interpolated(DensityFunctions.blendDensity(slide(terrain))), DensityFunctions.constant(0.64D)).squeeze();
        context.register(FINAL_DENSITY, DensityFunctions.max(
                DensityFunctions.min(shaped, noodle),
                DensityFunctions.max(valleyPillars, seaSpires)));
    }

    private static DensityFunction columns(Holder<NormalNoise.NoiseParameters> shapeNoise, Holder<NormalNoise.NoiseParameters> heightNoise,
                                           double threshold, double sharpness, double baseY, double rise, double softness, int footY) {
        DensityFunction shape = DensityFunctions.cache2d(DensityFunctions.mul(DensityFunctions.constant(sharpness),
                DensityFunctions.add(DensityFunctions.noise(shapeNoise, 1.0D, 0.0D).abs(), DensityFunctions.constant(-threshold))));
        DensityFunction tops = DensityFunctions.cache2d(DensityFunctions.mul(DensityFunctions.constant(rise / softness),
                DensityFunctions.noise(heightNoise, 1.0D, 0.0D).abs()));
        DensityFunction height = DensityFunctions.add(
                DensityFunctions.yClampedGradient(MIN_Y, MAX_Y, (baseY - MIN_Y) / softness, (baseY - MAX_Y) / softness), tops);
        DensityFunction foot = DensityFunctions.yClampedGradient(footY - 8, footY + 8, -1.0D, 1.0D);
        return DensityFunctions.min(DensityFunctions.min(shape, height), foot);
    }

    private static DensityFunction masked(DensityFunction mask, DensityFunction body) {
        return DensityFunctions.add(DensityFunctions.mul(mask, DensityFunctions.add(body, DensityFunctions.constant(1.0D))), DensityFunctions.constant(-1.0D));
    }

    private static DensityFunction slide(DensityFunction input) {
        DensityFunction top = lerp(DensityFunctions.yClampedGradient(MAX_Y - 80, MAX_Y - 64, 1.0D, 0.0D), -0.078125D, input);
        return lerp(DensityFunctions.yClampedGradient(MIN_Y, MIN_Y + 24, 0.0D, 1.0D), 0.1171875D, top);
    }

    private static DensityFunction lerp(DensityFunction delta, double from, DensityFunction to) {
        return DensityFunctions.add(DensityFunctions.constant(from),
                DensityFunctions.mul(delta, DensityFunctions.add(to, DensityFunctions.constant(-from))));
    }

    private static DensityFunction ramp(DensityFunction input, double zeroAt, double oneAt) {
        return DensityFunctions.mul(DensityFunctions.add(input, DensityFunctions.constant(-zeroAt)),
                DensityFunctions.constant(1.0D / (oneAt - zeroAt))).clamp(0.0D, 1.0D);
    }

    private static DensityFunction product(DensityFunction... factors) {
        DensityFunction result = factors[0];
        for (int i = 1; i < factors.length; i++) {
            result = DensityFunctions.mul(result, factors[i]);
        }
        return result;
    }

    private static DensityFunction holder(Holder<DensityFunction> holder) {
        return new DensityFunctions.HolderHolder(holder);
    }

    private static DensityFunction vanilla(HolderGetter<DensityFunction> functions, String path) {
        return new DensityFunctions.HolderHolder(functions.getOrThrow(ResourceKey.create(Registries.DENSITY_FUNCTION, new ResourceLocation("minecraft", path))));
    }

    private static ResourceKey<DensityFunction> key(String name) {
        return ResourceKey.create(Registries.DENSITY_FUNCTION, UnseenWorld.MOD.makeID(name));
    }
}
