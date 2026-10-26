package net.sashakyotoz.common.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.entities.custom.*;
import net.sashakyotoz.common.entities.custom.basic.WhaleEntity;

import java.time.LocalDate;
import java.time.Month;

public final class ModSpawnRules {
    private static final int REEF_SEARCH_DEPTH = 10;
    private static final int REEF_SEARCH_OFFSET = 2;
    private static final double WHALE_GROUP_RADIUS = 32.0D;
    private static final int WHALE_GROUP_LIMIT = 4;
    private static final int DREADFLAP_BASE_LIGHT = 4;
    private static final int DREADFLAP_HALLOWEEN_LIGHT = 7;
    private static final int ESPYER_MAX_Y = 32;
    private static final int ELDRITCH_MIN_Y = -32;
    private static final int ELDRITCH_MAX_Y = 96;
    private static final double PLAYER_NEARBY_RADIUS = 32.0D;
    private static final int GLEAMCARVER_MAX_Y = 16;
    private static final int SERPENT_WATER_RADIUS = 5;

    private ModSpawnRules() {
    }

    public static void register() {
        SpawnPlacements.register(ModEntities.SHIMMER, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModSpawnRules::checkShimmerSpawnRules);
        SpawnPlacements.register(ModEntities.GLOOMWHALE, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModSpawnRules::checkWhaleSpawnRules);
        SpawnPlacements.register(ModEntities.GRIPPING_GLOOMWHALE, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModSpawnRules::checkWhaleSpawnRules);
        SpawnPlacements.register(ModEntities.DREADFLAP, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModSpawnRules::checkDreadflapSpawnRules);
        SpawnPlacements.register(ModEntities.VEXAL_BEETLE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModSpawnRules::checkBeetleSpawnRules);
        SpawnPlacements.register(ModEntities.ESPYER, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModSpawnRules::checkEspyerSpawnRules);
        SpawnPlacements.register(ModEntities.ELDRITCH_WATCHER, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModSpawnRules::checkEldritchWatcherSpawnRules);
        SpawnPlacements.register(ModEntities.HARMONY_WATCHER, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModSpawnRules::checkHarmonyWatcherSpawnRules);
        SpawnPlacements.register(ModEntities.GLEAMCARVER, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModSpawnRules::checkGleamcarverSpawnRules);
        SpawnPlacements.register(ModEntities.SABERPARD, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModSpawnRules::checkSaberpardSpawnRules);
        SpawnPlacements.register(ModEntities.TUSKHOG, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModSpawnRules::checkTuskhogSpawnRules);
        SpawnPlacements.register(ModEntities.RIMEHORN, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModSpawnRules::checkRimehornSpawnRules);
        SpawnPlacements.register(ModEntities.ECLIPSED_ACOLYTE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModSpawnRules::checkEclipsedAcolyteSpawnRules);
        SpawnPlacements.register(ModEntities.UMBRAL_SERPENT, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ModSpawnRules::checkUmbralSerpentSpawnRules);
    }

    public static boolean checkShimmerSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        for (int i = 0; i < 3; i++) {
            if (!level.getFluidState(pos.above(i)).is(FluidTags.WATER)) {
                return false;
            }
        }
        if (isForced(spawnType)) {
            return true;
        }
        if (isAboveReef(level, pos)) {
            return true;
        }
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (isAboveReef(level, pos.relative(direction, REEF_SEARCH_OFFSET))) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkWhaleSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        if (!level.getFluidState(pos).is(FluidTags.WATER) || !level.getFluidState(pos.above()).is(FluidTags.WATER)) {
            return false;
        }
        if (isForced(spawnType)) {
            return true;
        }
        if (random.nextInt(7) != 0) {
            return false;
        }
        AABB area = type.getDimensions().makeBoundingBox(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D).inflate(WHALE_GROUP_RADIUS);
        return level.getEntities(EntityTypeTest.forClass(WhaleEntity.class), area, LivingEntity::isAlive).size() < WHALE_GROUP_LIMIT;
    }

    public static boolean checkDreadflapSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        if (isForced(spawnType)) {
            return true;
        }
        if (pos.getY() >= level.getSeaLevel()) {
            return false;
        }
        int maxLight = DREADFLAP_BASE_LIGHT;
        if (isAroundHalloween()) {
            maxLight = DREADFLAP_HALLOWEEN_LIGHT;
        } else if (random.nextBoolean()) {
            return false;
        }
        return level.getMaxLocalRawBrightness(pos) <= random.nextInt(maxLight) && level.getBlockState(pos.above()).isAir();
    }

    public static boolean checkBeetleSpawnRules(EntityType<VexalBeetleEntity> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isValidSpawn(level, below, type) && level.getRawBrightness(pos, 0) <= 10;
    }

    public static boolean checkEspyerSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        if (spawnType == MobSpawnType.SPAWNER) {
            return true;
        }
        BlockPos below = pos.below();
        return level.getBlockState(below).isValidSpawn(level, below, type)
                && below.getY() < ESPYER_MAX_Y
                && !level.canSeeSky(pos)
                && level.hasNearbyAlivePlayer(pos.getX(), pos.getY(), pos.getZ(), PLAYER_NEARBY_RADIUS)
                && countNearby(level, EspyerEntity.class, pos) < 3;
    }

    public static boolean checkEldritchWatcherSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        if (spawnType == MobSpawnType.SPAWNER) {
            return true;
        }
        BlockPos below = pos.below();
        return level.getBlockState(below).isValidSpawn(level, below, type)
                && below.getY() > ELDRITCH_MIN_Y
                && below.getY() < ELDRITCH_MAX_Y
                && level.hasNearbyAlivePlayer(pos.getX(), pos.getY(), pos.getZ(), PLAYER_NEARBY_RADIUS)
                && countNearby(level, EldritchWatcherEntity.class, pos) < 3;
    }

    public static boolean checkHarmonyWatcherSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return checkSaberpardSpawnRules(type, level, spawnType, pos, null);
    }

    public static boolean checkGleamcarverSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.above()).isAir() && (isForced(spawnType) || pos.getY() < GLEAMCARVER_MAX_Y);
    }

    public static boolean checkSaberpardSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return isForced(spawnType) || isSpawnableGround(level, pos.below(), type);
    }

    public static boolean checkTuskhogSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        if (isForced(spawnType)) {
            return true;
        }
        return isSpawnableGround(level, pos.below(), type)
                && countNearby(level, TuskhogEntity.class, pos) < 4;
    }

    public static boolean checkRimehornSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        if (isForced(spawnType)) {
            return true;
        }
        BlockPos belowPos = pos.below();
        BlockState below = level.getBlockState(belowPos);
        return (isSpawnableGround(level, belowPos, type) || below.is(BlockTags.GOATS_SPAWNABLE_ON) || below.is(ModBlocks.GLACIEMITE))
                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty();
    }

    public static boolean checkEclipsedAcolyteSpawnRules(EntityType<? extends Monster> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        if (isForced(spawnType)) {
            return true;
        }
        return Monster.checkMonsterSpawnRules(type, level, spawnType, pos, random)
                && level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()
                && level.getBlockState(pos.above(2)).getCollisionShape(level, pos.above(2)).isEmpty();
    }

    public static boolean checkUmbralSerpentSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        if (isForced(spawnType)) {
            return true;
        }
        BlockPos belowPos = pos.below();
        return isSpawnableGround(level, belowPos, type) || (isNearWater(level, pos) && countNearby(level, UmbralSerpentEntity.class, pos) < 4);
    }

    private static boolean isNearWater(ServerLevelAccessor level, BlockPos center) {
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-SERPENT_WATER_RADIUS, -2, -SERPENT_WATER_RADIUS), center.offset(SERPENT_WATER_RADIUS, 0, SERPENT_WATER_RADIUS))) {
            if (level.getFluidState(pos).is(FluidTags.WATER)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSpawnableGround(ServerLevelAccessor level, BlockPos below, EntityType<?> type) {
        BlockState ground = level.getBlockState(below);
        return ground.isValidSpawn(level, below, type) || ground.is(BlockTags.DIRT) || ground.is(BlockTags.ANIMALS_SPAWNABLE_ON);
    }

    private static boolean isForced(MobSpawnType spawnType) {
        return spawnType == MobSpawnType.SPAWNER || spawnType == MobSpawnType.SPAWN_EGG || spawnType == MobSpawnType.COMMAND;
    }

    private static <T extends LivingEntity> int countNearby(ServerLevelAccessor level, Class<T> type, BlockPos pos) {
        return level.getEntities(EntityTypeTest.forClass(type), new AABB(pos).inflate(64.0D), LivingEntity::isAlive).size();
    }

    private static boolean isAroundHalloween() {
        LocalDate date = LocalDate.now();
        int day = date.getDayOfMonth();
        Month month = date.getMonth();
        return month == Month.OCTOBER && day >= 20 || month == Month.NOVEMBER && day <= 3;
    }

    private static boolean isAboveReef(ServerLevelAccessor level, BlockPos start) {
        BlockPos.MutableBlockPos pos = start.mutable();
        for (int i = 0; i < REEF_SEARCH_DEPTH; i++) {
            BlockState state = level.getBlockState(pos);
            if (isUmbralReef(state)) {
                return true;
            }
            if (!state.getFluidState().is(FluidTags.WATER)) {
                return false;
            }
            pos.move(Direction.DOWN);
        }
        return false;
    }

    private static boolean isUmbralReef(BlockState state) {
        return state.is(ModBlocks.UMBRAL_CORAL_BLOCK)
                || state.is(ModBlocks.UMBRAL_CORAL)
                || state.is(ModBlocks.UMBRAL_CORAL_FAN)
                || state.is(ModBlocks.UMBRAL_CORAL_WALL_FAN);
    }
}