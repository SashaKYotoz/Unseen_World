package net.sashakyotoz.common.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.sashakyotoz.UnseenWorld;

import java.util.OptionalLong;

public class ModDimensions {
    public static final ResourceKey<LevelStem> CHIMERIC_DARKNESS_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            UnseenWorld.makeID("chimeric_darkness"));
    public static final ResourceKey<Level> CHIMERIC_DARKNESS_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            UnseenWorld.makeID("chimeric_darkness"));
    public static final ResourceKey<DimensionType> CHIMERIC_DARKNESS_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            UnseenWorld.makeID("chimeric_darkness_type"));

    public static void bootstrapType(BootstapContext<DimensionType> context) {
        context.register(CHIMERIC_DARKNESS_TYPE, new DimensionType(
                OptionalLong.of(16000), // fixedTime
                false, // hasSkylight
                false, // hasCeiling
                false, // ultraWarm
                false, // natural
                16.0, // coordinateScale
                true, // bedWorks
                true, // respawnAnchorWorks
                -64, // minY
                384, // height
                320, // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                UnseenWorld.makeID("the_chimeric_darkness"), // effectsLocation
                0.01F, // ambientLight
                new DimensionType.MonsterSettings(false, false, UniformInt.of(0, 4), 0)));
    }
}
