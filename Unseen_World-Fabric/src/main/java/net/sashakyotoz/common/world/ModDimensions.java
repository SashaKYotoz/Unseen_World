package net.sashakyotoz.common.world;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.sashakyotoz.UnseenWorld;

import java.util.OptionalLong;

public class ModDimensions {
    public static final RegistryKey<DimensionOptions> CHIMERIC_DARKNESS_KEY = RegistryKey.of(RegistryKeys.DIMENSION,
            UnseenWorld.makeID("chimeric_darkness"));
    public static final RegistryKey<World> CHIMERIC_DARKNESS_LEVEL_KEY = RegistryKey.of(RegistryKeys.WORLD,
            UnseenWorld.makeID("chimeric_darkness"));
    public static final RegistryKey<DimensionType> CHIMERIC_DARKNESS_TYPE = RegistryKey.of(RegistryKeys.DIMENSION_TYPE,
            UnseenWorld.makeID("chimeric_darkness_type"));

    public static void bootstrapType(Registerable<DimensionType> context) {
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
                0.0075F, // ambientLight
                new DimensionType.MonsterSettings(false, false, UniformIntProvider.create(0, 4), 0)));
    }
}
