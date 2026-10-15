package net.sashakyotoz.common.world.biomes;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;

import java.util.*;

import static net.sashakyotoz.common.world.noise.ChimericDarknessClimate.*;

public final class ChimericDarknessBiomeParameters {
    private static final Climate.Parameter PLACEHOLDER = Climate.Parameter.point(0.0F);

    private static final ResourceKey<Biome>[][] MIDDLE = table(
            row(ModBiomes.GLACIEMITE_TUNDRA, ModBiomes.GLACIEMITE_TUNDRA, ModBiomes.SNOWY_GRIZZLY_THICKET, ModBiomes.SNOWY_GRIZZLY_THICKET, ModBiomes.SNOWY_GRIZZLY_THICKET),
            row(ModBiomes.DARK_WASTELAND, ModBiomes.GLOOMWEED_PLAINS, ModBiomes.GRIZZLY_THICKET, ModBiomes.GRIZZLY_THICKET, ModBiomes.GRIZZLY_THICKET),
            row(ModBiomes.GLOOMWEED_PLAINS, ModBiomes.GLOOMWEED_PLAINS, ModBiomes.AMETHYST_FOREST, ModBiomes.AMETHYST_FOREST, ModBiomes.AMETHYST_FOREST),
            row(ModBiomes.DARK_WASTELAND, ModBiomes.GLOOMWEED_PLAINS, ModBiomes.CRIMSONVEIL_WOODS, ModBiomes.CRIMSONVEIL_WOODS, ModBiomes.OLD_GROWTH_CRIMSONVEIL_WOODS),
            row(ModBiomes.DARK_WASTELAND, ModBiomes.DARK_WASTELAND, ModBiomes.BURLYWOOD_JUNGLE, ModBiomes.BURLYWOOD_JUNGLE, ModBiomes.BURLYWOOD_JUNGLE));

    private static final ResourceKey<Biome>[][] MIDDLE_VARIANT = table(
            row(ModBiomes.GLACIEMITE_TUNDRA, ModBiomes.SNOWY_GRIZZLY_THICKET, ModBiomes.SNOWY_GRIZZLY_THICKET, ModBiomes.SNOWY_GRIZZLY_THICKET, ModBiomes.SNOWY_GRIZZLY_THICKET),
            row(ModBiomes.DARK_WASTELAND, ModBiomes.GRIZZLY_THICKET, ModBiomes.GRIZZLY_THICKET, ModBiomes.GRIZZLY_THICKET, ModBiomes.GRIZZLY_THICKET),
            row(ModBiomes.GLOOMWEED_PLAINS, ModBiomes.AMETHYST_FOREST, ModBiomes.AMETHYST_FOREST, ModBiomes.AMETHYST_FOREST, ModBiomes.AMETHYST_FOREST),
            row(ModBiomes.DARK_WASTELAND, ModBiomes.CRIMSONVEIL_WOODS, ModBiomes.CRIMSONVEIL_WOODS, ModBiomes.OLD_GROWTH_CRIMSONVEIL_WOODS, ModBiomes.OLD_GROWTH_CRIMSONVEIL_WOODS),
            row(ModBiomes.DARK_WASTELAND, ModBiomes.BURLYWOOD_JUNGLE, ModBiomes.BURLYWOOD_JUNGLE, ModBiomes.BURLYWOOD_JUNGLE, ModBiomes.BURLYWOOD_JUNGLE));

    private static final ResourceKey<Biome>[][] PLATEAU = table(
            row(ModBiomes.GLACIEMITE_TUNDRA, ModBiomes.SNOWY_GRIZZLY_THICKET, ModBiomes.SNOWY_GRIZZLY_THICKET, ModBiomes.SNOWY_GRIZZLY_THICKET, ModBiomes.SNOWY_GRIZZLY_THICKET),
            row(ModBiomes.DARK_WASTELAND, ModBiomes.GRIZZLY_HIGHLANDS, ModBiomes.GRIZZLY_HIGHLANDS, ModBiomes.GRIZZLY_THICKET, ModBiomes.GRIZZLY_THICKET),
            row(ModBiomes.AMETHYST_CHIMERIES, ModBiomes.AMETHYST_CHIMERIES, ModBiomes.AMETHYST_CHIMERIES, ModBiomes.AMETHYST_FOREST, ModBiomes.AMETHYST_FOREST),
            row(ModBiomes.CRIMSONVEIL_PLATEAU, ModBiomes.CRIMSONVEIL_PLATEAU, ModBiomes.CRIMSONVEIL_PLATEAU, ModBiomes.CRIMSONVEIL_PLATEAU, ModBiomes.OLD_GROWTH_CRIMSONVEIL_WOODS),
            row(ModBiomes.CRIMSONVEIL_PLATEAU, ModBiomes.CRIMSONVEIL_PLATEAU, ModBiomes.CRIMSONVEIL_PLATEAU, ModBiomes.BURLYWOOD_JUNGLE, ModBiomes.BURLYWOOD_JUNGLE));

    private static final ResourceKey<Biome>[][] HIGHLAND = table(
            row(ModBiomes.SNOWY_GRIZZLY_THICKET, ModBiomes.SNOWY_GRIZZLY_THICKET, ModBiomes.SNOWY_GRIZZLY_THICKET, ModBiomes.SNOWY_GRIZZLY_THICKET, ModBiomes.SNOWY_GRIZZLY_THICKET),
            row(ModBiomes.GRIZZLY_HIGHLANDS, ModBiomes.GRIZZLY_HIGHLANDS, ModBiomes.GRIZZLY_HIGHLANDS, ModBiomes.GRIZZLY_HIGHLANDS, ModBiomes.TEALIVY_HIGHLANDS),
            row(ModBiomes.GRIZZLY_HIGHLANDS, ModBiomes.GRIZZLY_HIGHLANDS, ModBiomes.TEALIVY_HIGHLANDS, ModBiomes.TEALIVY_HIGHLANDS, ModBiomes.TEALIVY_HIGHLANDS),
            row(ModBiomes.TEALIVY_HIGHLANDS, ModBiomes.TEALIVY_HIGHLANDS, ModBiomes.TEALIVY_HIGHLANDS, ModBiomes.TEALIVY_HIGHLANDS, ModBiomes.TEALIVY_HIGHLANDS),
            row(ModBiomes.GRIZZLY_HIGHLANDS, ModBiomes.GRIZZLY_HIGHLANDS, ModBiomes.TEALIVY_HIGHLANDS, ModBiomes.TEALIVY_HIGHLANDS, ModBiomes.TEALIVY_HIGHLANDS));

    private ChimericDarknessBiomeParameters() {
    }

    public static Climate.ParameterList<Holder<Biome>> build(HolderGetter<Biome> biomes) {
        List<Entry> entries = compact(entries());
        List<Pair<Climate.ParameterPoint, Holder<Biome>>> points = new ArrayList<>(entries.size());
        for (Entry entry : entries) {
            points.add(Pair.of(entry.point(), biomes.getOrThrow(entry.biome())));
        }
        return new Climate.ParameterList<>(points);
    }

    public static Set<ResourceKey<Biome>> placedBiomes() {
        Set<ResourceKey<Biome>> placed = new HashSet<>();
        for (Entry entry : entries()) {
            placed.add(entry.biome());
        }
        return placed;
    }

    private static List<Entry> entries() {
        List<Entry> entries = new ArrayList<>();
        addOceans(entries);
        addInland(entries);
        addCaves(entries);
        Set<ResourceKey<Biome>> placed = new HashSet<>();
        entries.forEach(entry -> placed.add(entry.biome()));
        for (ResourceKey<Biome> biome : ModBiomes.ALL) {
            if (!placed.contains(biome)) {
                throw new IllegalStateException("Biome " + biome.location() + " is registered but never placed in Chimeric Darkness");
            }
        }
        return entries;
    }

    private static void addOceans(List<Entry> entries) {
        for (int t = 0; t < TEMPERATURES.length; t++) {
            ResourceKey<Biome> deep = t == 0 ? ModBiomes.FRIGID_SPIRE_OCEAN : ModBiomes.DARK_LIFELESS_OCEAN;
            ResourceKey<Biome> shallow = t == 0 ? ModBiomes.FRIGID_SPIRE_OCEAN : ModBiomes.DARK_OCEAN;
            surface(entries, TEMPERATURES[t], FULL, ISLANDS, FULL, FULL, ModBiomes.DARK_WASTELAND);
            surface(entries, TEMPERATURES[t], FULL, DEEP_OCEAN, FULL, FULL, deep);
            surface(entries, TEMPERATURES[t], FULL, OCEAN, FULL, FULL, shallow);
        }
    }

    private static void addInland(List<Entry> entries) {
        slice(entries, Slice.MID, -1.0F, -0.93333334F, false);
        slice(entries, Slice.HIGH, -0.93333334F, -0.7666667F, false);
        slice(entries, Slice.PEAK, -0.7666667F, -0.56666666F, false);
        slice(entries, Slice.HIGH, -0.56666666F, -0.4F, false);
        slice(entries, Slice.MID, -0.4F, -0.26666668F, false);
        slice(entries, Slice.LOW, -0.26666668F, -RIVER_WEIRDNESS, false);
        slice(entries, Slice.VALLEY, -RIVER_WEIRDNESS, RIVER_WEIRDNESS, false);
        slice(entries, Slice.LOW, RIVER_WEIRDNESS, 0.26666668F, true);
        slice(entries, Slice.MID, 0.26666668F, 0.4F, true);
        slice(entries, Slice.HIGH, 0.4F, 0.56666666F, true);
        slice(entries, Slice.PEAK, 0.56666666F, 0.7666667F, true);
        slice(entries, Slice.HIGH, 0.7666667F, 0.93333334F, true);
        slice(entries, Slice.MID, 0.93333334F, 1.0F, true);
    }

    private static void slice(List<Entry> entries, Slice slice, float minWeirdness, float maxWeirdness, boolean variant) {
        Climate.Parameter weirdness = Climate.Parameter.span(minWeirdness, maxWeirdness);
        for (int t = 0; t < TEMPERATURES.length; t++) {
            for (int h = 0; h < HUMIDITIES.length; h++) {
                for (int c = 0; c < INLAND.length; c++) {
                    for (int e = 0; e < EROSIONS.length; e++) {
                        ResourceKey<Biome> biome = pick(slice, c, e, t, h, variant);
                        surface(entries, TEMPERATURES[t], HUMIDITIES[h], INLAND[c], EROSIONS[e], weirdness, biome);
                    }
                }
            }
        }
    }

    private static ResourceKey<Biome> pick(Slice slice, int c, int e, int t, int h, boolean variant) {
        boolean valleyBand = (c == NEAR_INLAND || c == MID_INLAND) && e == VALLEY_EROSION_INDEX;
        return switch (slice) {
            case VALLEY -> {
                if (c == FAR_INLAND) {
                    yield e <= 1 ? highland(t, h) : e == 6 ? wetland(t, h) : middle(t, h, variant);
                }
                yield e == 6 ? wetland(t, h) : river(t);
            }
            case LOW -> {
                if (c == COAST) {
                    yield e == 6 ? wetland(t, h) : shore(t);
                }
                if (valleyBand) {
                    yield valley(t, h);
                }
                if (c == FAR_INLAND) {
                    yield e <= 1 ? highland(t, h) : e <= 3 ? plateau(t, h) : e == 6 ? wetland(t, h) : middle(t, h, variant);
                }
                yield e <= 1 ? plateau(t, h) : e == 6 ? wetland(t, h) : middle(t, h, variant);
            }
            case MID -> {
                if (sentinelDomain(c, e, t, h)) {
                    yield ModBiomes.PENUMBRAL_STEPPE;
                }
                if (c == COAST) {
                    yield e <= 2 ? middle(t, h, variant) : e == 6 ? wetland(t, h) : shore(t);
                }
                if (valleyBand) {
                    yield valley(t, h);
                }
                if (c == NEAR_INLAND) {
                    yield e <= 1 ? plateau(t, h) : e == 6 ? wetland(t, h) : middle(t, h, variant);
                }
                if (c == MID_INLAND) {
                    yield e <= 1 ? highland(t, h) : e == 2 ? plateau(t, h) : e == 6 ? wetland(t, h) : middle(t, h, variant);
                }
                yield e <= 1 ? highland(t, h) : e <= 3 ? plateau(t, h) : middle(t, h, variant);
            }
            case HIGH -> {
                if (c == COAST) {
                    yield middle(t, h, variant);
                }
                if (valleyBand) {
                    yield valley(t, h);
                }
                if (c == NEAR_INLAND) {
                    yield e <= 1 ? highland(t, h) : e <= 3 ? plateau(t, h) : middle(t, h, variant);
                }
                if (c == MID_INLAND) {
                    yield e <= 1 ? peak(t) : e <= 3 ? highland(t, h) : middle(t, h, variant);
                }
                yield e <= 1 ? peak(t) : e <= 3 ? highland(t, h) : plateau(t, h);
            }
            case PEAK -> {
                if (c == COAST) {
                    yield middle(t, h, variant);
                }
                if (patriarchDomain(e, t, variant)) {
                    yield ModBiomes.AURIC_HEIGHTS;
                }
                if (valleyBand) {
                    yield valley(t, h);
                }
                if (c == NEAR_INLAND) {
                    yield e <= 1 ? peak(t) : e <= 3 ? highland(t, h) : middle(t, h, variant);
                }
                yield e <= 1 ? peak(t) : e <= 3 ? highland(t, h) : e <= 5 ? plateau(t, h) : middle(t, h, variant);
            }
        };
    }

    private static boolean sentinelDomain(int c, int e, int t, int h) {
        return (c == MID_INLAND || c == FAR_INLAND) && (e == 2 || e == 3) && (t == 2 || t == 3) && h <= 2;
    }

    private static boolean patriarchDomain(int e, int t, boolean variant) {
        return variant && e <= 1 && t >= 3;
    }

    private static ResourceKey<Biome> middle(int t, int h, boolean variant) {
        return variant ? MIDDLE_VARIANT[t][h] : MIDDLE[t][h];
    }

    private static ResourceKey<Biome> plateau(int t, int h) {
        return PLATEAU[t][h];
    }

    private static ResourceKey<Biome> highland(int t, int h) {
        return HIGHLAND[t][h];
    }

    private static ResourceKey<Biome> peak(int t) {
        return t <= 1 ? ModBiomes.GLACIEMITE_PEAKS : ModBiomes.CURRANTSLATE_PEAKS;
    }

    private static ResourceKey<Biome> shore(int t) {
        return t == 0 ? ModBiomes.GLACIEMITE_TUNDRA : ModBiomes.GREYNISH_SHORE;
    }

    private static ResourceKey<Biome> river(int t) {
        return t <= 2 ? ModBiomes.DARK_RIVER : ModBiomes.LUKEWARM_DARK_RIVER;
    }

    private static ResourceKey<Biome> wetland(int t, int h) {
        if (t == 0) {
            return ModBiomes.GLACIEMITE_TUNDRA;
        }
        return t >= 3 && h >= 2 ? ModBiomes.CRIMSONVEIL_MIRE : ModBiomes.ASHEN_MARSH;
    }

    private static ResourceKey<Biome> valley(int t, int h) {
        if (t <= 1) {
            return ModBiomes.THE_DARKNESS;
        }
        return h >= 3 ? ModBiomes.TEALIVY_VALLEY : ModBiomes.GREENISH_VALLEY;
    }

    private static void addCaves(List<Entry> entries) {
        Climate.Parameter any = Climate.Parameter.span(-2.0F, 2.0F);
        Climate.Parameter cold = Climate.Parameter.span(-2.0F, CAVE_COLD_MAX);
        Climate.Parameter notCold = Climate.Parameter.span(CAVE_COLD_MAX, 2.0F);
        Climate.Parameter humid = Climate.Parameter.span(CAVE_HUMID_MIN, 2.0F);
        Climate.Parameter dry = Climate.Parameter.span(-2.0F, CAVE_HUMID_MIN);
        entries.add(new Entry(cold, any, any, any, CAVE_DEPTH, any, 0.0F, ModBiomes.SHINY_CAVERNS));
        entries.add(new Entry(notCold, humid, any, any, CAVE_DEPTH, any, 0.0F, ModBiomes.BURLYWOOD_HOLLOWS));
        entries.add(new Entry(notCold, dry, any, any, CAVE_DEPTH, any, 0.0F, ModBiomes.TANZANITE_CAVES));
        entries.add(new Entry(any, any, any, any, DEEP_DEPTH, any, 0.0F, ModBiomes.DEEP_GLACIEMITE_CAVES));
    }

    private static void surface(List<Entry> entries, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, ResourceKey<Biome> biome) {
        entries.add(new Entry(temperature, humidity, continentalness, erosion, SURFACE_DEPTH, weirdness, 0.0F, biome));
    }

    private static List<Entry> compact(List<Entry> input) {
        List<Entry> current = input;
        int before;
        do {
            before = current.size();
            for (int axis = 0; axis < 6; axis++) {
                current = mergeAlong(current, axis);
            }
        } while (current.size() < before);
        return current;
    }

    private static List<Entry> mergeAlong(List<Entry> input, int axis) {
        Map<Entry, List<Entry>> groups = new LinkedHashMap<>();
        for (Entry entry : input) {
            groups.computeIfAbsent(entry.with(axis, PLACEHOLDER), key -> new ArrayList<>()).add(entry);
        }
        List<Entry> output = new ArrayList<>(input.size());
        for (List<Entry> group : groups.values()) {
            group.sort(Comparator.comparingLong(entry -> entry.axis(axis).min()));
            Entry run = group.get(0);
            for (int i = 1; i < group.size(); i++) {
                Entry next = group.get(i);
                if (run.axis(axis).max() == next.axis(axis).min()) {
                    run = run.with(axis, Climate.Parameter.span(run.axis(axis), next.axis(axis)));
                } else {
                    output.add(run);
                    run = next;
                }
            }
            output.add(run);
        }
        return output;
    }

    @SafeVarargs
    private static ResourceKey<Biome>[] row(ResourceKey<Biome>... biomes) {
        return biomes;
    }

    @SafeVarargs
    private static ResourceKey<Biome>[][] table(ResourceKey<Biome>[]... rows) {
        return rows;
    }

    private enum Slice {
        VALLEY,
        LOW,
        MID,
        HIGH,
        PEAK
    }

    private record Entry(Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter depth, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) {
        Climate.Parameter axis(int axis) {
            return switch (axis) {
                case 0 -> this.temperature;
                case 1 -> this.humidity;
                case 2 -> this.continentalness;
                case 3 -> this.erosion;
                case 4 -> this.depth;
                default -> this.weirdness;
            };
        }

        Entry with(int axis, Climate.Parameter value) {
            return switch (axis) {
                case 0 -> new Entry(value, this.humidity, this.continentalness, this.erosion, this.depth, this.weirdness, this.offset, this.biome);
                case 1 -> new Entry(this.temperature, value, this.continentalness, this.erosion, this.depth, this.weirdness, this.offset, this.biome);
                case 2 -> new Entry(this.temperature, this.humidity, value, this.erosion, this.depth, this.weirdness, this.offset, this.biome);
                case 3 -> new Entry(this.temperature, this.humidity, this.continentalness, value, this.depth, this.weirdness, this.offset, this.biome);
                case 4 -> new Entry(this.temperature, this.humidity, this.continentalness, this.erosion, value, this.weirdness, this.offset, this.biome);
                default -> new Entry(this.temperature, this.humidity, this.continentalness, this.erosion, this.depth, value, this.offset, this.biome);
            };
        }

        Climate.ParameterPoint point() {
            return Climate.parameters(this.temperature, this.humidity, this.continentalness, this.erosion, this.depth, this.weirdness, this.offset);
        }
    }
}