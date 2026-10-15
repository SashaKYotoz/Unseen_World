package net.sashakyotoz.common.world.noise;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.world.biomes.ModBiomes;

public final class ChimericDarknessSurfaceRules {
    public static final int SEA_LEVEL = 58;
    private static final int CORONA_LAYER_BOTTOM = 32;
    private static final double CORONA_DYKE_WIDTH = 0.06D;
    private static final double GILDED_VEIN_WIDTH = 0.2D;
    private static final double CRYSTAL_TANZANITE_THRESHOLD = 0.35D;
    private static final double CRYSTAL_AMETHYST_THRESHOLD = -0.1D;
    private static final double CORONA_LINING_THRESHOLD = 0.25D;

    private ChimericDarknessSurfaceRules() {
    }

    public static BlockState modBlock(String name) {
        return BuiltInRegistries.BLOCK.getOptional(UnseenWorld.MOD.makeID(name))
                .orElseThrow(() -> new IllegalStateException("Block unseen_world:" + name + " is not registered"))
                .defaultBlockState();
    }

    public static BlockState modBlockOr(String name, String fallback) {
        return BuiltInRegistries.BLOCK.getOptional(UnseenWorld.MOD.makeID(name))
                .map(block -> block.defaultBlockState())
                .orElseGet(() -> modBlock(fallback));
    }

    public static SurfaceRules.RuleSource build() {
        Palette p = new Palette();
        return SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.verticalGradient("unseen_world:bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), p.bedrock),
                SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), surface(p)),
                strata(p),
                caves(p));
    }

    private static SurfaceRules.RuleSource strata(Palette p) {
        return SurfaceRules.sequence(
                biome(SurfaceRules.ifTrue(SurfaceRules.yBlockCheck(VerticalAnchor.absolute(CORONA_LAYER_BOTTOM), 0),
                        SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(ModNoises.PATCHES, -CORONA_DYKE_WIDTH, CORONA_DYKE_WIDTH), p.currantslate),
                                p.coronaStone)), ModBiomes.PENUMBRAL_STEPPE),
                biome(SurfaceRules.ifTrue(SurfaceRules.noiseCondition(ModNoises.PATCHES, -GILDED_VEIN_WIDTH, GILDED_VEIN_WIDTH), p.gildedCurrantslate), ModBiomes.AURIC_HEIGHTS));
    }

    private static SurfaceRules.RuleSource surface(Palette p) {
        return SurfaceRules.sequence(
                biome(SurfaceRules.sequence(
                        SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(-1, 0), p.packedIce)),
                        p.glaciemite), ModBiomes.FRIGID_SPIRE_OCEAN),
                biome(SurfaceRules.sequence(
                        SurfaceRules.ifTrue(SurfaceRules.steep(), p.packedIce),
                        SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(powderSnow(p), p.snowBlock)),
                        p.glaciemite), ModBiomes.GLACIEMITE_PEAKS),
                biome(cover(SurfaceRules.sequence(powderSnow(p), patch(0.4D, p.glaciemite), p.snowBlock), p.glaciemite), ModBiomes.GLACIEMITE_TUNDRA),
                biome(cover(SurfaceRules.sequence(powderSnow(p), p.grimwoodGrass), p.currantslate), ModBiomes.SNOWY_GRIZZLY_THICKET),
                biome(SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, patch(0.15D, p.grippingCurrantslateUp)), ModBiomes.CURRANTSLATE_PEAKS),
                biome(SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.steep(), p.currantslate), cover(p.grimwoodGrass, p.currantslate)), ModBiomes.GRIZZLY_HIGHLANDS),
                biome(SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.steep(), p.currantslate), cover(p.tealivyGrass, p.tealivyDirt)), ModBiomes.TEALIVY_HIGHLANDS),
                biome(pools(p, 0.0D), ModBiomes.ASHEN_MARSH),
                biome(pools(p, -0.15D), ModBiomes.CRIMSONVEIL_MIRE),
                biome(cover(SurfaceRules.sequence(
                        SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SURFACE, -0.1D, 0.25D), p.ashenOoze),
                        p.nightdarkGrass), p.mud), ModBiomes.ASHEN_MARSH),
                biome(cover(p.nightdarkGrass, p.mud), ModBiomes.CRIMSONVEIL_MIRE),
                biome(cover(SurfaceRules.sequence(
                        SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SURFACE, 0.0D), p.grippingCurrantslateUp),
                        p.nightdarkDirt), p.nightdarkDirt), ModBiomes.DARK_WASTELAND),
                biome(cover(SurfaceRules.sequence(patch(0.45D, p.nightdarkDirt), p.nightdarkGrass), p.nightdarkDirt), ModBiomes.GLOOMWEED_PLAINS),
                biome(SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.steep(), p.currantslate), cover(p.nightdarkGrass, p.nightdarkDirt)), ModBiomes.CRIMSONVEIL_PLATEAU),
                biome(cover(p.nightdarkGrass, p.nightdarkDirt), ModBiomes.CRIMSONVEIL_WOODS, ModBiomes.OLD_GROWTH_CRIMSONVEIL_WOODS),
                biome(SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.steep(), p.currantslate), cover(SurfaceRules.sequence(patch(0.4D, p.amethystBlock), p.amethystGrass), p.currantslate)), ModBiomes.AMETHYST_CHIMERIES),
                biome(cover(p.amethystGrass, p.currantslate), ModBiomes.AMETHYST_FOREST),
                biome(cover(p.grimwoodGrass, p.currantslate), ModBiomes.GRIZZLY_THICKET),
                biome(cover(SurfaceRules.sequence(patch(0.5D, p.coarseDirt), p.grass), p.dirt), ModBiomes.GREENISH_VALLEY),
                biome(cover(SurfaceRules.sequence(patch(0.55D, p.podzol), p.grass), p.dirt), ModBiomes.BURLYWOOD_JUNGLE),
                biome(cover(p.tealivyGrass, p.tealivyDirt), ModBiomes.TEALIVY_VALLEY),
                biome(cover(p.grippingGlaciemiteUp, p.glaciemite), ModBiomes.THE_DARKNESS),
                biome(cover(SurfaceRules.sequence(patch(0.55D, p.coronaStone), p.penumbralGrass), p.nightdarkDirt), ModBiomes.PENUMBRAL_STEPPE),
                biome(SurfaceRules.sequence(
                        SurfaceRules.ifTrue(SurfaceRules.steep(), p.gildedCurrantslate),
                        SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(patch(0.35D, p.auricOre), SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(-1, 0), p.auricGrass))),
                        SurfaceRules.ifTrue(SurfaceRules.noiseCondition(ModNoises.PATCHES, -GILDED_VEIN_WIDTH, GILDED_VEIN_WIDTH), p.gildedCurrantslate)), ModBiomes.AURIC_HEIGHTS),
                biome(SurfaceRules.sequence(floor(p.sand, p.sand), SurfaceRules.ifTrue(SurfaceRules.DEEP_UNDER_FLOOR, p.sandstone)), ModBiomes.GREYNISH_SHORE),
                biome(SurfaceRules.sequence(floor(p.sand, p.sand), SurfaceRules.ifTrue(SurfaceRules.DEEP_UNDER_FLOOR, p.sandstone)), ModBiomes.DARK_OCEAN, ModBiomes.LUKEWARM_DARK_RIVER),
                biome(floor(p.ashenOoze, p.ashenOoze), ModBiomes.DARK_LIFELESS_OCEAN, ModBiomes.DARK_RIVER));
    }

    private static SurfaceRules.RuleSource caves(Palette p) {
        SurfaceRules.RuleSource crystal = SurfaceRules.sequence(patch(CRYSTAL_TANZANITE_THRESHOLD, p.tanzanite), patch(CRYSTAL_AMETHYST_THRESHOLD, p.amethystBlock));
        SurfaceRules.RuleSource lining = SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SURFACE, CORONA_LINING_THRESHOLD), p.coronaStone),
                p.calcite);
        return SurfaceRules.sequence(
                biome(SurfaceRules.sequence(
                        SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, patch(0.3D, p.grippingGlaciemiteUp)),
                        SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, patch(0.3D, p.grippingGlaciemiteDown)),
                        p.glaciemite), ModBiomes.DEEP_GLACIEMITE_CAVES),
                biome(SurfaceRules.sequence(
                        SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(powderSnow(p), patch(0.45D, p.rimeIce), patch(0.25D, p.packedIce), p.glaciemite)),
                        SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, p.packedIce),
                        SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, lining),
                        SurfaceRules.ifTrue(SurfaceRules.UNDER_CEILING, lining)), ModBiomes.SHINY_CAVERNS),
                biome(SurfaceRules.sequence(
                        SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, crystal),
                        SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, crystal),
                        SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, lining),
                        SurfaceRules.ifTrue(SurfaceRules.UNDER_CEILING, lining)), ModBiomes.TANZANITE_CAVES),
                biome(SurfaceRules.sequence(
                        SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, p.moss),
                        SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, p.moss),
                        SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, p.rootedDirt)), ModBiomes.BURLYWOOD_HOLLOWS));
    }

    private static SurfaceRules.RuleSource cover(SurfaceRules.RuleSource top, SurfaceRules.RuleSource under) {
        return SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(-1, 0), top), under)),
                SurfaceRules.ifTrue(SurfaceRules.waterStartCheck(-6, -1), SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, under)));
    }

    private static SurfaceRules.RuleSource floor(SurfaceRules.RuleSource top, SurfaceRules.RuleSource under) {
        return SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, top),
                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, under));
    }

    private static SurfaceRules.RuleSource pools(Palette p, double threshold) {
        return SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                SurfaceRules.ifTrue(SurfaceRules.yBlockCheck(VerticalAnchor.absolute(SEA_LEVEL - 2), 0),
                        SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.yBlockCheck(VerticalAnchor.absolute(SEA_LEVEL), 0)),
                                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SWAMP, threshold), p.darkWater))));
    }

    private static SurfaceRules.RuleSource powderSnow(Palette p) {
        return SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.POWDER_SNOW, 0.45D, 0.58D), SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(0, 0), p.powderSnow));
    }

    private static SurfaceRules.RuleSource patch(double threshold, SurfaceRules.RuleSource rule) {
        return SurfaceRules.ifTrue(SurfaceRules.noiseCondition(ModNoises.PATCHES, threshold), rule);
    }

    @SafeVarargs
    private static SurfaceRules.RuleSource biome(SurfaceRules.RuleSource rule, ResourceKey<Biome>... biomes) {
        return SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes), rule);
    }

    private static final class Palette {
        final SurfaceRules.RuleSource bedrock = SurfaceRules.state(Blocks.BEDROCK.defaultBlockState());
        final SurfaceRules.RuleSource currantslate = mod("dark_currantslate");
        final SurfaceRules.RuleSource glaciemite = mod("glaciemite");
        final SurfaceRules.RuleSource grippingGlaciemiteUp = mod("gripping_glaciemite", Direction.UP);
        final SurfaceRules.RuleSource grippingGlaciemiteDown = mod("gripping_glaciemite", Direction.DOWN);
        final SurfaceRules.RuleSource grippingCurrantslateUp = mod("gripping_dark_currantslate", Direction.UP);
        final SurfaceRules.RuleSource nightdarkGrass = mod("nightdark_grass_block");
        final SurfaceRules.RuleSource nightdarkDirt = mod("nightdark_dirt");
        final SurfaceRules.RuleSource grimwoodGrass = mod("grimwood_grass_block");
        final SurfaceRules.RuleSource amethystGrass = mod("amethyst_grass_block");
        final SurfaceRules.RuleSource tealivyGrass = mod("tealivy_grass_block");
        final SurfaceRules.RuleSource tealivyDirt = mod("tealivy_dirt");
        final SurfaceRules.RuleSource sand = mod("glimmergrain_sand");
        final SurfaceRules.RuleSource sandstone = mod("glimmergrain_sandstone");
        final SurfaceRules.RuleSource ashenOoze = mod("ashen_ooze");
        final SurfaceRules.RuleSource tanzanite = mod("tanzanite_block");
        final SurfaceRules.RuleSource darkWater = mod("dark_water");
        final SurfaceRules.RuleSource rimeIce = SurfaceRules.state(ModBlocks.RIME_ICE.defaultBlockState());
        final SurfaceRules.RuleSource penumbralGrass = SurfaceRules.state(ModBlocks.PENUMBRAL_GRASS_BLOCK.defaultBlockState());
        final SurfaceRules.RuleSource coronaStone = SurfaceRules.state(ModBlocks.CORONA_STONE.defaultBlockState());
        final SurfaceRules.RuleSource gildedCurrantslate = SurfaceRules.state(ModBlocks.GILDED_CURRANTSLATE.defaultBlockState());
        final SurfaceRules.RuleSource auricOre = SurfaceRules.state(modBlockOr("auric_vein_ore", "dark_currantslate"));
        final SurfaceRules.RuleSource auricGrass = SurfaceRules.state(modBlockOr("auric_grass_block", "grimwood_grass_block"));
        final SurfaceRules.RuleSource grass = SurfaceRules.state(Blocks.GRASS_BLOCK.defaultBlockState());
        final SurfaceRules.RuleSource dirt = SurfaceRules.state(Blocks.DIRT.defaultBlockState());
        final SurfaceRules.RuleSource coarseDirt = SurfaceRules.state(Blocks.COARSE_DIRT.defaultBlockState());
        final SurfaceRules.RuleSource podzol = SurfaceRules.state(Blocks.PODZOL.defaultBlockState());
        final SurfaceRules.RuleSource mud = SurfaceRules.state(Blocks.MUD.defaultBlockState());
        final SurfaceRules.RuleSource moss = SurfaceRules.state(Blocks.MOSS_BLOCK.defaultBlockState());
        final SurfaceRules.RuleSource rootedDirt = SurfaceRules.state(Blocks.ROOTED_DIRT.defaultBlockState());
        final SurfaceRules.RuleSource amethystBlock = SurfaceRules.state(Blocks.AMETHYST_BLOCK.defaultBlockState());
        final SurfaceRules.RuleSource calcite = SurfaceRules.state(Blocks.CALCITE.defaultBlockState());
        final SurfaceRules.RuleSource packedIce = SurfaceRules.state(Blocks.PACKED_ICE.defaultBlockState());
        final SurfaceRules.RuleSource snowBlock = SurfaceRules.state(Blocks.SNOW_BLOCK.defaultBlockState());
        final SurfaceRules.RuleSource powderSnow = SurfaceRules.state(Blocks.POWDER_SNOW.defaultBlockState());

        private static SurfaceRules.RuleSource mod(String name) {
            return SurfaceRules.state(modBlock(name));
        }

        private static SurfaceRules.RuleSource mod(String name, Direction facing) {
            BlockState state = modBlock(name);
            if (state.hasProperty(BlockStateProperties.FACING)) {
                state = state.setValue(BlockStateProperties.FACING, facing);
            }
            return SurfaceRules.state(state);
        }
    }
}