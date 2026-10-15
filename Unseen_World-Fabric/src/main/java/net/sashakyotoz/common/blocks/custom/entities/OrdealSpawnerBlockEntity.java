package net.sashakyotoz.common.blocks.custom.entities;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.api.entity_data.data.GrippingData;
import net.sashakyotoz.common.ModSoundEvents;
import net.sashakyotoz.common.blocks.ModBlockEntities;
import net.sashakyotoz.common.blocks.custom.OrdealSpawnerBlock;
import net.sashakyotoz.common.blocks.custom.states.OrdealSpawnerState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrdealSpawnerBlockEntity extends BlockEntity {
    public static final String ORDEAL_TAG = "unseen_world.ordeal";
    public static final String GRIPPING_TAG = "unseen_world.ordeal_gripping";
    private static final int DETECTION_RANGE = 14;
    private static final int SPAWN_RANGE = 4;
    private static final int CHARGE_TICKS = 60;
    private static final int ABANDON_TICKS = 600;
    private static final int REWARD_INTERVAL = 8;
    private static final int GRIPPING_THRESHOLD = 10;
    private static final int GRIPPING_PER_HIT = 4;
    private static final int MAX_SCALED_PLAYERS = 4;
    private static final int SPAWN_PARTICLES_EVENT = 2004;
    private static final int MAX_FAILED_SPAWNS = 20;
    private static final Settings NORMAL = new Settings(6, 2, 2, 40, 24000);
    private static final Settings GRIPPING = new Settings(10, 3, 3, 20, 32000);

    @Nullable
    private ResourceLocation primary;
    @Nullable
    private ResourceLocation secondary;
    private final List<UUID> participants = new ArrayList<>();
    private final List<UUID> trackedMobs = new ArrayList<>();
    private int timer;
    private int idleTicks;
    private int spawned;
    private int totalToSpawn;
    private int rewardsEjected;
    private long cooldownEndsAt;
    private boolean configureSecondaryNext;
    private int failedSpawns;
    private float spin;
    private float oSpin;
    @Nullable
    private Entity displayEntity;

    public OrdealSpawnerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ORDEAL_SPAWNER, pos, state);
    }

    public static void registerEvents() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (source.getEntity() instanceof LivingEntity attacker
                    && attacker.getTags().contains(GRIPPING_TAG)
                    && entity instanceof IGrippingEntity gripping
                    && !entity.isInvulnerableTo(source)
                    && !entity.isDamageSourceBlocked(source)) {
                GrippingData.addGrippingSeconds(gripping, GRIPPING_PER_HIT);
            }
            return true;
        });
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, OrdealSpawnerBlockEntity spawner) {
        if (level instanceof ServerLevel server) {
            switch (state.getValue(OrdealSpawnerBlock.STATE)) {
                case INACTIVE -> spawner.tickInactive(server, pos, state);
                case WAITING_FOR_PLAYERS -> spawner.tickWaiting(server, pos, state);
                case ACTIVE -> spawner.tickActive(server, pos, state);
                case EJECTING_REWARD -> spawner.tickEjecting(server, pos, state);
                case COOLDOWN -> spawner.tickCooldown(server, pos, state);
            }
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, OrdealSpawnerBlockEntity spawner) {
        int speed = state.getValue(OrdealSpawnerBlock.STATE).rotationSpeed;
        spawner.oSpin = spawner.spin;
        if (speed > 0) {
            spawner.spin = (spawner.spin + speed / 400.0F) % 360.0F;
        }
    }

    private void tickInactive(ServerLevel level, BlockPos pos, BlockState state) {
        if (level.getGameTime() % 20L != 0L) {
            return;
        }
        List<ServerPlayer> players = this.detectPlayers(level, pos);
        if (!players.isEmpty()) {
            BlockState current = this.absorbGripping(level, pos, state, players);
            this.timer = 0;
            this.setPhase(level, pos, current, OrdealSpawnerState.WAITING_FOR_PLAYERS);
            level.playSound(null, pos, SoundEvents.BEACON_AMBIENT, SoundSource.BLOCKS, 0.75F, 1.0F);
        }
    }

    private void tickWaiting(ServerLevel level, BlockPos pos, BlockState state) {
        this.timer++;
        if (this.timer % 10 != 0) {
            return;
        }
        List<ServerPlayer> players = this.detectPlayers(level, pos);
        if (players.isEmpty()) {
            this.timer = 0;
            this.setPhase(level, pos, state, OrdealSpawnerState.INACTIVE);
            return;
        }
        BlockState current = this.absorbGripping(level, pos, state, players);
        if (this.timer >= CHARGE_TICKS) {
            this.begin(level, pos, current, players);
        } else if (this.timer % 20 == 0) {
            level.playSound(null, pos, SoundEvents.BEACON_AMBIENT, SoundSource.BLOCKS, 0.75F, 1.0F + this.timer / (float) CHARGE_TICKS);
        }
    }

    private void begin(ServerLevel level, BlockPos pos, BlockState state, List<ServerPlayer> players) {
        this.resolveEntityTypes(state);
        this.participants.clear();
        this.trackedMobs.clear();
        players.forEach(player -> this.participants.add(player.getUUID()));
        this.spawned = 0;
        this.rewardsEjected = 0;
        this.idleTicks = 0;
        this.totalToSpawn = settings(state).total(this.participants.size());
        this.timer = 20;
        this.setPhase(level, pos, state, OrdealSpawnerState.ACTIVE);
        this.sync();
        level.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.25F, state.getValue(OrdealSpawnerBlock.GRIPPING) ? 0.6F : 0.9F);
    }

    private void tickActive(ServerLevel level, BlockPos pos, BlockState state) {
        Settings settings = settings(state);
        if (level.getGameTime() % 20L == 0L) {
            this.pruneMobs(level);
            List<ServerPlayer> players = this.detectPlayers(level, pos);
            if (players.isEmpty()) {
                this.idleTicks += 20;
                if (this.idleTicks >= ABANDON_TICKS) {
                    this.abandon(level, pos, state);
                    return;
                }
            } else {
                this.idleTicks = 0;
                for (ServerPlayer player : players) {
                    if (!this.participants.contains(player.getUUID())) {
                        this.participants.add(player.getUUID());
                    }
                }
                this.totalToSpawn = Math.max(this.totalToSpawn, settings.total(this.participants.size()));
            }
        }
        if (this.spawned >= this.totalToSpawn) {
            if (this.trackedMobs.isEmpty()) {
                this.timer = 0;
                this.setPhase(level, pos, state, OrdealSpawnerState.EJECTING_REWARD);
                level.playSound(null, pos, ModSoundEvents.ORDEAL_SPAWNER_REWARDING, SoundSource.BLOCKS, 1.0F, 1.25F);
            }
            return;
        }
        if (this.timer > 0) {
            this.timer--;
            return;
        }
        if (this.idleTicks > 0 || this.trackedMobs.size() >= settings.cap(this.participants.size())) {
            return;
        }
        if (this.spawnMob(level, pos, state)) {
            this.spawned++;
            this.failedSpawns = 0;
            this.timer = settings.spawnInterval();
            this.sync();
        } else if (++this.failedSpawns >= MAX_FAILED_SPAWNS) {
            this.spawned++;
            this.failedSpawns = 0;
            this.sync();
        } else {
            this.timer = 10;
        }
    }

    private void tickEjecting(ServerLevel level, BlockPos pos, BlockState state) {
        this.timer++;
        if (this.timer % REWARD_INTERVAL != 0) {
            return;
        }
        if (this.rewardsEjected < Math.max(1, this.participants.size())) {
            ServerPlayer player = this.rewardsEjected < this.participants.size()
                    ? level.getServer().getPlayerList().getPlayer(this.participants.get(this.rewardsEjected))
                    : null;
            this.ejectReward(level, pos, state, player);
            this.rewardsEjected++;
            this.setChanged();
            return;
        }
        this.participants.clear();
        this.trackedMobs.clear();
        this.cooldownEndsAt = level.getGameTime() + settings(state).cooldown();
        this.setPhase(level, pos, state, OrdealSpawnerState.COOLDOWN);
    }

    private void tickCooldown(ServerLevel level, BlockPos pos, BlockState state) {
        if (level.getGameTime() % 20L == 0L && level.getGameTime() >= this.cooldownEndsAt) {
            this.spawned = 0;
            this.totalToSpawn = 0;
            this.setPhase(level, pos, state.setValue(OrdealSpawnerBlock.GRIPPING, false), OrdealSpawnerState.INACTIVE);
            this.sync();
            level.playSound(null, pos, ModSoundEvents.ORDEAL_SPAWNER_REWARDING, SoundSource.BLOCKS, 0.75F, 1.75F);
        }
    }

    private List<ServerPlayer> detectPlayers(ServerLevel level, BlockPos pos) {
        Vec3 center = Vec3.atCenterOf(pos);
        List<ServerPlayer> found = new ArrayList<>();
        for (ServerPlayer player : level.players()) {
            if (player.isSpectator() || player.isCreative() || !player.isAlive()) {
                continue;
            }
            if (player.distanceToSqr(center) <= DETECTION_RANGE * DETECTION_RANGE
                    && hasLineOfSight(level, center, player.getEyePosition(), player)) {
                found.add(player);
            }
        }
        return found;
    }

    private BlockState absorbGripping(ServerLevel level, BlockPos pos, BlockState state, List<ServerPlayer> players) {
        if (state.getValue(OrdealSpawnerBlock.GRIPPING)) {
            return state;
        }
        for (ServerPlayer player : players) {
            if (player instanceof IGrippingEntity gripping && GrippingData.consumeGripping(gripping, GRIPPING_THRESHOLD)) {
                BlockState gripped = state.setValue(OrdealSpawnerBlock.GRIPPING, true);
                level.setBlock(pos, gripped, Block.UPDATE_ALL);
                level.playSound(null, pos, SoundEvents.SOUL_ESCAPE, SoundSource.BLOCKS, 2.0F, 0.6F);
                level.playSound(null, player.blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.0F, 0.5F);
                level.sendParticles(ParticleTypes.REVERSE_PORTAL, player.getX(), player.getY(0.5D), player.getZ(), 30, 0.3D, 0.6D, 0.3D, 0.05D);
                level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 30, 0.4D, 0.4D, 0.4D, 0.02D);
                return gripped;
            }
        }
        return state;
    }

    private boolean spawnMob(ServerLevel level, BlockPos pos, BlockState state) {
        EntityType<?> type = this.currentType(state);
        ServerPlayer target = this.nearestParticipant(level, pos);
        RandomSource random = level.getRandom();
        Vec3 center = Vec3.atCenterOf(pos);
        boolean gripping = state.getValue(OrdealSpawnerBlock.GRIPPING);
        for (int attempt = 0; attempt < 6; attempt++) {
            BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos(
                    pos.getX() + random.nextInt(SPAWN_RANGE * 2 + 1) - SPAWN_RANGE,
                    pos.getY() + random.nextInt(3) - 1,
                    pos.getZ() + random.nextInt(SPAWN_RANGE * 2 + 1) - SPAWN_RANGE);
            if (cursor.equals(pos)) {
                continue;
            }
            for (int drop = 0; drop < 4 && !hasFloor(level, cursor); drop++) {
                cursor.move(Direction.DOWN);
            }
            if (!hasFloor(level, cursor)) {
                continue;
            }
            double x = cursor.getX() + 0.5D;
            double y = cursor.getY();
            double z = cursor.getZ() + 0.5D;
            if (!level.noCollision(type.getAABB(x, y, z))) {
                continue;
            }
            Vec3 body = new Vec3(x, y + type.getHeight() * 0.5D, z);
            if (target != null && !hasLineOfSight(level, center, body, target)) {
                continue;
            }
            Entity entity = type.create(level);
            if (entity == null) {
                return true;
            }
            entity.moveTo(x, y, z, random.nextFloat() * 360.0F, 0.0F);
            if (entity instanceof Mob mob) {
                mob.finalizeSpawn(level, level.getCurrentDifficultyAt(cursor), MobSpawnType.SPAWNER, null, null);
                mob.setPersistenceRequired();
                if (target != null) {
                    mob.setTarget(target);
                }
            }
            entity.addTag(ORDEAL_TAG);
            if (gripping) {
                entity.addTag(GRIPPING_TAG);
            }
            if (!level.tryAddFreshEntityWithPassengers(entity)) {
                return false;
            }
            this.trackedMobs.add(entity.getUUID());
            level.levelEvent(SPAWN_PARTICLES_EVENT, pos, 0);
            level.levelEvent(SPAWN_PARTICLES_EVENT, cursor, 0);
            if (gripping) {
                level.sendParticles(ParticleTypes.REVERSE_PORTAL, x, body.y, z, 24, type.getWidth() * 0.5D, type.getHeight() * 0.5D, type.getWidth() * 0.5D, 0.05D);
            }
            level.playSound(null, x, y, z, SoundEvents.ILLUSIONER_MIRROR_MOVE, SoundSource.HOSTILE, 0.6F, gripping ? 0.6F : 1.1F);
            return true;
        }
        return false;
    }

    private void ejectReward(ServerLevel level, BlockPos pos, BlockState state, @Nullable ServerPlayer player) {
        OrdealSpawnerBlock.Type type = state.getValue(OrdealSpawnerBlock.TYPE);
        LootTable table = level.getServer().getLootData().getLootTable(type.lootTable(state.getValue(OrdealSpawnerBlock.GRIPPING)));
        LootParams.Builder params = new LootParams.Builder(level).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos));
        if (player != null) {
            params.withParameter(LootContextParams.THIS_ENTITY, player).withLuck(player.getLuck());
        }
        ObjectArrayList<ItemStack> loot = table.getRandomItems(params.create(LootContextParamSets.CHEST));
        for (ItemStack stack : loot) {
            ItemEntity item = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 1.1D, pos.getZ() + 0.5D, stack);
            item.setDeltaMovement(level.random.nextGaussian() * 0.05D, 0.25D, level.random.nextGaussian() * 0.05D);
            item.setDefaultPickUpDelay();
            level.addFreshEntity(item);
        }
        level.sendParticles(ParticleTypes.END_ROD, pos.getX() + 0.5D, pos.getY() + 1.1D, pos.getZ() + 0.5D, 6, 0.2D, 0.1D, 0.2D, 0.02D);
        level.playSound(null, pos, ModSoundEvents.ORDEAL_SPAWNER_REWARDING, SoundSource.BLOCKS, 1.0F, 0.75F + level.random.nextFloat() * 0.25F);
    }

    private void abandon(ServerLevel level, BlockPos pos, BlockState state) {
        this.dismissMobs(level);
        this.participants.clear();
        this.timer = 0;
        this.idleTicks = 0;
        this.spawned = 0;
        this.totalToSpawn = 0;
        this.setPhase(level, pos, state, OrdealSpawnerState.INACTIVE);
        this.sync();
        level.playSound(null, pos, SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 1.0F, 0.8F);
    }

    public void dismissMobs(ServerLevel level) {
        for (UUID uuid : this.trackedMobs) {
            Entity entity = level.getEntity(uuid);
            if (entity != null) {
                level.sendParticles(ParticleTypes.POOF, entity.getX(), entity.getY(0.5D), entity.getZ(), 8, 0.3D, 0.3D, 0.3D, 0.02D);
                entity.discard();
            }
        }
        this.trackedMobs.clear();
    }

    private void pruneMobs(ServerLevel level) {
        this.trackedMobs.removeIf(uuid -> {
            Entity entity = level.getEntity(uuid);
            return entity == null || !entity.isAlive();
        });
    }

    @Nullable
    private ServerPlayer nearestParticipant(ServerLevel level, BlockPos pos) {
        ServerPlayer nearest = null;
        double best = Double.MAX_VALUE;
        for (UUID uuid : this.participants) {
            if (level.getPlayerByUUID(uuid) instanceof ServerPlayer player && player.isAlive() && !player.isSpectator()) {
                double distance = player.distanceToSqr(Vec3.atCenterOf(pos));
                if (distance < best) {
                    best = distance;
                    nearest = player;
                }
            }
        }
        return nearest;
    }

    private void resolveEntityTypes(BlockState state) {
        OrdealSpawnerBlock.Type type = state.getValue(OrdealSpawnerBlock.TYPE);
        if (this.primary == null || !BuiltInRegistries.ENTITY_TYPE.containsKey(this.primary)) {
            this.primary = BuiltInRegistries.ENTITY_TYPE.getKey(type.defaultEntity(false));
        }
        if (this.secondary == null || !BuiltInRegistries.ENTITY_TYPE.containsKey(this.secondary)) {
            this.secondary = BuiltInRegistries.ENTITY_TYPE.getKey(type.defaultEntity(true));
        }
    }

    private EntityType<?> currentType(BlockState state) {
        boolean secondPhase = this.totalToSpawn > 0 && this.spawned >= (this.totalToSpawn + 1) / 2;
        ResourceLocation id = secondPhase ? this.secondary : this.primary;
        EntityType<?> fallback = state.getValue(OrdealSpawnerBlock.TYPE).defaultEntity(secondPhase);
        return id == null ? fallback : BuiltInRegistries.ENTITY_TYPE.getOptional(id).orElse(fallback);
    }

    public Component configure(EntityType<?> type) {
        boolean secondarySlot = this.configureSecondaryNext;
        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(type);
        if (secondarySlot) {
            this.secondary = id;
        } else {
            this.primary = id;
        }
        this.configureSecondaryNext = !secondarySlot;
        this.displayEntity = null;
        this.sync();
        return Component.translatable(secondarySlot
                ? "block.unseen_world.ordeal_spawner.secondary"
                : "block.unseen_world.ordeal_spawner.primary", type.getDescription());
    }

    @Nullable
    public Entity getOrCreateDisplayEntity(Level level) {
        EntityType<?> type = this.currentType(this.getBlockState());
        if (this.displayEntity == null || this.displayEntity.getType() != type) {
            this.displayEntity = type.create(level);
        }
        return this.displayEntity;
    }

    public float getSpin(float partialTick) {
        return this.oSpin + (this.spin - this.oSpin) * partialTick;
    }

    private static boolean hasFloor(Level level, BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    private static boolean hasLineOfSight(Level level, Vec3 from, Vec3 to, Entity context) {
        Vec3 direction = to.subtract(from);
        if (direction.lengthSqr() < 1.0E-4D) {
            return true;
        }
        Vec3 start = from.add(direction.normalize().scale(0.75D));
        return level.clip(new ClipContext(start, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, context)).getType() == HitResult.Type.MISS;
    }

    private static Settings settings(BlockState state) {
        return state.getValue(OrdealSpawnerBlock.GRIPPING) ? GRIPPING : NORMAL;
    }

    private void setPhase(ServerLevel level, BlockPos pos, BlockState state, OrdealSpawnerState phase) {
        level.setBlock(pos, state.setValue(OrdealSpawnerBlock.STATE, phase), Block.UPDATE_ALL);
        this.setChanged();
    }

    private void sync() {
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.primary = tag.contains("primary", Tag.TAG_STRING) ? ResourceLocation.tryParse(tag.getString("primary")) : null;
        this.secondary = tag.contains("secondary", Tag.TAG_STRING) ? ResourceLocation.tryParse(tag.getString("secondary")) : null;
        this.spawned = tag.getInt("spawned");
        this.totalToSpawn = tag.getInt("total");
        this.timer = tag.getInt("timer");
        this.idleTicks = tag.getInt("idle");
        this.rewardsEjected = tag.getInt("rewards_ejected");
        this.cooldownEndsAt = tag.getLong("cooldown_ends_at");
        readUuids(tag.getList("participants", Tag.TAG_INT_ARRAY), this.participants);
        readUuids(tag.getList("mobs", Tag.TAG_INT_ARRAY), this.trackedMobs);
        this.displayEntity = null;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        this.writeDisplayData(tag);
        tag.putInt("timer", this.timer);
        tag.putInt("idle", this.idleTicks);
        tag.putInt("rewards_ejected", this.rewardsEjected);
        tag.putLong("cooldown_ends_at", this.cooldownEndsAt);
        tag.put("participants", writeUuids(this.participants));
        tag.put("mobs", writeUuids(this.trackedMobs));
    }

    private void writeDisplayData(CompoundTag tag) {
        if (this.primary != null) {
            tag.putString("primary", this.primary.toString());
        }
        if (this.secondary != null) {
            tag.putString("secondary", this.secondary.toString());
        }
        tag.putInt("spawned", this.spawned);
        tag.putInt("total", this.totalToSpawn);
    }

    private static void readUuids(ListTag list, List<UUID> target) {
        target.clear();
        for (Tag entry : list) {
            target.add(NbtUtils.loadUUID(entry));
        }
    }

    private static ListTag writeUuids(List<UUID> source) {
        ListTag list = new ListTag();
        for (UUID uuid : source) {
            list.add(NbtUtils.createUUID(uuid));
        }
        return list;
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.writeDisplayData(tag);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private record Settings(int baseMobs, int mobsPerPlayer, int simultaneous, int spawnInterval, int cooldown) {
        int total(int players) {
            return this.baseMobs + this.mobsPerPlayer * (Math.min(Math.max(players, 1), MAX_SCALED_PLAYERS) - 1);
        }

        int cap(int players) {
            return this.simultaneous + Math.min(Math.max(players, 1), MAX_SCALED_PLAYERS) - 1;
        }
    }
}
