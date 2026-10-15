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
import net.sashakyotoz.common.entities.custom.EldritchWatcherEntity;
import net.sashakyotoz.common.entities.custom.EspyerEntity;
import net.sashakyotoz.common.entities.custom.TuskhogEntity;
import net.sashakyotoz.common.entities.custom.VexalBeetleEntity;
import net.sashakyotoz.common.entities.custom.basic.WhaleEntity;

import java.time.LocalDate;
import java.time.Month;

public final class ModSpawnRules {
    private static final int SHIMMER_HEIGHT = 3;
    private static final int REEF_SEARCH_DEPTH = 10;
    private static final int REEF_SEARCH_OFFSET = 2;
    private static final double WHALE_GROUP_RADIUS = 32.0D;
    private static final int WHALE_GROUP_LIMIT = 4;
    private static final int WHALE_SPAWN_CHANCE = 7;
    private static final int DREADFLAP_BASE_LIGHT = 4;
    private static final int DREADFLAP_HALLOWEEN_LIGHT = 7;
    private static final int BEETLE_MAX_LIGHT = 9;
    private static final double LOCAL_CAP_RADIUS = 64.0D;
    private static final int ESPYER_LOCAL_CAP = 3;
    private static final int ESPYER_MAX_Y = 32;
    private static final int ELDRITCH_LOCAL_CAP = 3;
    private static final int ELDRITCH_MIN_Y = -32;
    private static final int ELDRITCH_MAX_Y = 96;
    private static final double PLAYER_NEARBY_RADIUS = 32.0D;
    private static final int TUSKHOG_LOCAL_CAP = 4;
    private static final int GLEAMCARVER_MAX_Y = 16;

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
    }

    public static boolean checkShimmerSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        for (int i = 0; i < SHIMMER_HEIGHT; i++) {
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
        if (random.nextInt(WHALE_SPAWN_CHANCE) != 0) {
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
        return Mob.checkMobSpawnRules(type, level, spawnType, pos, random)
                && (spawnType == MobSpawnType.SPAWNER || level.getRawBrightness(pos, 0) <= BEETLE_MAX_LIGHT);
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
                && countNearby(level, EspyerEntity.class, pos) < ESPYER_LOCAL_CAP;
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
                && countNearby(level, EldritchWatcherEntity.class, pos) < ELDRITCH_LOCAL_CAP;
    }

    public static boolean checkHarmonyWatcherSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        if (spawnType == MobSpawnType.SPAWNER) {
            return true;
        }
        BlockPos below = pos.below();
        return level.getBlockState(below).isValidSpawn(level, below, type) && random.nextInt(6) == 1;
    }

    public static boolean checkGleamcarverSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.above()).isAir() && (isForced(spawnType) || pos.getY() < GLEAMCARVER_MAX_Y);
    }

    public static boolean checkSaberpardSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return isForced(spawnType) || level.getBlockState(pos.below()).is(BlockTags.DIRT);
    }

    public static boolean checkTuskhogSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        if (isForced(spawnType)) {
            return true;
        }
        return level.getBlockState(pos.below()).is(BlockTags.DIRT)
                && random.nextInt(4) == 1
                && countNearby(level, TuskhogEntity.class, pos) < TUSKHOG_LOCAL_CAP;
    }

    public static boolean checkRimehornSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        if (isForced(spawnType)) {
            return true;
        }
        BlockState ground = level.getBlockState(pos.below());
        return (ground.is(BlockTags.GOATS_SPAWNABLE_ON) || ground.is(BlockTags.SNOW) || ground.is(BlockTags.DIRT) || ground.is(ModBlocks.GLACIEMITE))
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

    private static boolean isForced(MobSpawnType spawnType) {
        return spawnType == MobSpawnType.SPAWNER || spawnType == MobSpawnType.SPAWN_EGG || spawnType == MobSpawnType.COMMAND;
    }

    private static <T extends LivingEntity> int countNearby(ServerLevelAccessor level, Class<T> type, BlockPos pos) {
        return level.getEntities(EntityTypeTest.forClass(type), new AABB(pos).inflate(LOCAL_CAP_RADIUS), LivingEntity::isAlive).size();
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