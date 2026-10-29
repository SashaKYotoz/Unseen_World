package net.sashakyotoz.common.blocks.custom.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.sashakyotoz.common.blocks.ModBlockEntities;
import net.sashakyotoz.common.blocks.custom.EffigyBlock;
import net.sashakyotoz.common.blocks.custom.EngravedBricksBlock;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class EffigyBlockEntity extends BlockEntity {
    public static final String BOSS_TAG_PREFIX = "unseen_world.effigy@";
    private static final int AWAKENING_TICKS = 100;
    private static final int SEAL_CHECK_INTERVAL = 10;
    private static final int BOSS_CHECK_INTERVAL = 40;

    private int keys;
    private int timer;
    @Nullable
    private UUID boss;
    @Nullable
    private BlockPos lastBossPos;
    @Nullable
    private Entity statue;
    private boolean sealsValidated;

    public EffigyBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.EFFIGY, pos, state);
    }

    public static void registerEvents() {
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, (LivingDeathEvent event) -> {
            LivingEntity entity = event.getEntity();
            if (entity.level().isClientSide) {
                return;
            }
            for (String tag : entity.getTags()) {
                if (tag.startsWith(BOSS_TAG_PREFIX)) {
                    BlockPos pos = parseTag(tag);
                    if (pos != null && entity.level().isLoaded(pos)
                            && entity.level().getBlockEntity(pos) instanceof EffigyBlockEntity effigy) {
                        effigy.onBossDefeated(entity);
                    }
                    return;
                }
            }
        });
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, EffigyBlockEntity effigy) {
        if (level instanceof ServerLevel server) {
            switch (state.getValue(EffigyBlock.PHASE)) {
                case DORMANT -> effigy.tickDormant(server, pos, state);
                case AWAKENING -> effigy.tickAwakening(server, pos, state);
                case AWAKENED -> effigy.tickAwakened(server, pos, state);
            }
        }
    }

    public static int countSeals(Level level, BlockPos pos) {
        int seals = 0;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (EngravedBricksBlock.feeds(level.getBlockState(pos.relative(direction, EffigyBlock.SEAL_DISTANCE)), direction.getOpposite())) {
                seals++;
            }
        }
        return seals;
    }

    private void tickDormant(ServerLevel level, BlockPos pos, BlockState state) {
        if (!this.sealsValidated) {
            this.sealsValidated = true;
            EffigyBlock.ensureParts(level, pos, state);
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPos neighbor = pos.relative(direction, EffigyBlock.SEAL_DISTANCE);
                BlockState neighborState = level.getBlockState(neighbor);
                if (neighborState.getBlock() instanceof EngravedBricksBlock) {
                    level.scheduleTick(neighbor, neighborState.getBlock(), 1);
                }
            }
            return;
        }
        EffigyBlock.Boss boss = state.getValue(EffigyBlock.BOSS);
        if (this.keys < boss.keysRequired || level.getGameTime() % SEAL_CHECK_INTERVAL != 0L) {
            return;
        }
        if (countSeals(level, pos) >= boss.sealsRequired) {
            this.timer = 0;
            this.setPhase(level, pos, state, EffigyBlock.Phase.AWAKENING);
            level.playSound(null, pos, SoundEvents.BEACON_POWER_SELECT, SoundSource.BLOCKS, 1.5F, 0.6F);
            level.playSound(null, pos, SoundEvents.AMBIENT_CAVE.value(), SoundSource.HOSTILE, 1.5F, 0.7F);
        }
    }

    private void tickAwakening(ServerLevel level, BlockPos pos, BlockState state) {
        EffigyBlock.Boss boss = state.getValue(EffigyBlock.BOSS);
        this.timer++;
        if (this.timer % SEAL_CHECK_INTERVAL == 0 && countSeals(level, pos) < boss.sealsRequired) {
            this.timer = 0;
            this.setPhase(level, pos, state, EffigyBlock.Phase.DORMANT);
            level.playSound(null, pos, SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 1.2F, 0.8F);
            return;
        }
        if (this.timer % 5 == 0) {
            level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, boss.material().defaultBlockState()),
                    pos.getX() + 0.5D, pos.getY() + 2.0D, pos.getZ() + 0.5D, 6, 0.6D, 1.2D, 0.6D, 0.05D);
        }
        if (this.timer % 20 == 0) {
            level.playSound(null, pos, SoundEvents.DEEPSLATE_BREAK, SoundSource.BLOCKS, 1.5F, 0.5F + this.timer / (float) AWAKENING_TICKS * 0.4F);
        }
        if (this.timer >= AWAKENING_TICKS) {
            this.summon(level, pos, state, boss);
        }
    }

    private void summon(ServerLevel level, BlockPos pos, BlockState state, EffigyBlock.Boss boss) {
        Entity entity = boss.entity().create(level);
        if (entity == null) {
            this.timer = 0;
            this.setPhase(level, pos, state, EffigyBlock.Phase.DORMANT);
            return;
        }
        float yaw = state.getValue(EffigyBlock.FACING).toYRot();
        entity.moveTo(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, yaw, 0.0F);
        entity.setYHeadRot(yaw);
        if (entity instanceof Mob mob) {
            mob.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.EVENT, null);
            mob.setPersistenceRequired();
            Player nearest = level.getNearestPlayer(entity, 48.0D);
            if (nearest != null && !nearest.isCreative() && !nearest.isSpectator()) {
                mob.setTarget(nearest);
            }
        }
        entity.addTag(BOSS_TAG_PREFIX + pos.asLong());
        if (!level.addFreshEntity(entity)) {
            this.timer = 0;
            this.setPhase(level, pos, state, EffigyBlock.Phase.DORMANT);
            return;
        }
        this.keys = 0;
        this.timer = 0;
        this.boss = entity.getUUID();
        this.lastBossPos = entity.blockPosition();
        this.setPhase(level, pos, state, EffigyBlock.Phase.AWAKENED);
        level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, boss.material().defaultBlockState()),
                entity.getX(), entity.getY(0.5D), entity.getZ(), 120, entity.getBbWidth() * 0.5D, entity.getBbHeight() * 0.5D, entity.getBbWidth() * 0.5D, 0.15D);
        level.playSound(null, pos, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.HOSTILE, 1.0F, 0.6F);
        level.playSound(null, pos, SoundEvents.DEEPSLATE_BRICKS_BREAK, SoundSource.HOSTILE, 2.0F, 0.5F);
        level.gameEvent(entity, GameEvent.ENTITY_PLACE, pos.above());
    }

    private void tickAwakened(ServerLevel level, BlockPos pos, BlockState state) {
        if (level.getGameTime() % BOSS_CHECK_INTERVAL != 0L) {
            return;
        }
        Entity entity = this.boss == null ? null : level.getEntity(this.boss);
        if (entity != null) {
            if (entity.isAlive()) {
                this.lastBossPos = entity.blockPosition();
                this.setChanged();
            }
            return;
        }
        BlockPos checked = this.lastBossPos == null ? pos : this.lastBossPos;
        if (this.boss == null || level.areEntitiesLoaded(ChunkPos.asLong(checked))) {
            this.reset(level, pos, state);
        }
    }

    private void onBossDefeated(LivingEntity entity) {
        if (this.level instanceof ServerLevel server && entity.getUUID().equals(this.boss)) {
            this.reset(server, this.worldPosition, this.getBlockState());
            server.playSound(null, this.worldPosition, SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 1.5F, 0.6F);
        }
    }

    private void reset(ServerLevel level, BlockPos pos, BlockState state) {
        this.boss = null;
        this.lastBossPos = null;
        this.timer = 0;
        this.setPhase(level, pos, state, EffigyBlock.Phase.DORMANT);
        level.sendParticles(ParticleTypes.WHITE_ASH, pos.getX() + 0.5D, pos.getY() + 2.0D, pos.getZ() + 0.5D, 40, 0.6D, 1.2D, 0.6D, 0.01D);
    }

    public void insertKey(Player player, ItemStack stack) {
        BlockState state = this.getBlockState();
        EffigyBlock.Boss boss = state.getValue(EffigyBlock.BOSS);
        if (state.getValue(EffigyBlock.PHASE) != EffigyBlock.Phase.DORMANT) {
            player.displayClientMessage(Component.translatable("block.unseen_world.effigy.awake"), true);
            return;
        }
        if (this.keys >= boss.keysRequired) {
            player.displayClientMessage(Component.translatable("block.unseen_world.effigy.full"), true);
            return;
        }
        this.keys++;
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        this.sync();
        if (this.level != null) {
            this.level.playSound(null, this.worldPosition, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1.0F, 0.8F + this.keys * 0.1F);
            this.level.gameEvent(player, GameEvent.BLOCK_CHANGE, this.worldPosition);
        }
        player.displayClientMessage(this.status(), true);
    }

    public void retrieveKey(Player player) {
        BlockState state = this.getBlockState();
        if (state.getValue(EffigyBlock.PHASE) != EffigyBlock.Phase.DORMANT || this.keys <= 0) {
            player.displayClientMessage(this.status(), true);
            return;
        }
        this.keys--;
        ItemStack key = new ItemStack(state.getValue(EffigyBlock.BOSS).key());
        if (!player.getInventory().add(key)) {
            player.drop(key, false);
        }
        this.sync();
        if (this.level != null) {
            this.level.playSound(null, this.worldPosition, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 0.7F);
            this.level.gameEvent(player, GameEvent.BLOCK_CHANGE, this.worldPosition);
        }
    }

    public Component status() {
        EffigyBlock.Boss boss = this.getBlockState().getValue(EffigyBlock.BOSS);
        int seals = this.level == null ? 0 : countSeals(this.level, this.worldPosition);
        return Component.translatable("block.unseen_world.effigy.status",
                this.keys, boss.keysRequired, Math.min(seals, boss.sealsRequired), boss.sealsRequired);
    }

    public int getKeys() {
        return this.keys;
    }

    @Nullable
    public Entity getOrCreateStatue(Level level) {
        EffigyBlock.Boss boss = this.getBlockState().getValue(EffigyBlock.BOSS);
        if (this.statue == null || this.statue.getType() != boss.entity()) {
            this.statue = boss.entity().create(level);
        }
        return this.statue;
    }

    private void setPhase(ServerLevel level, BlockPos pos, BlockState state, EffigyBlock.Phase phase) {
        level.setBlock(pos, state.setValue(EffigyBlock.PHASE, phase), Block.UPDATE_ALL);
        this.sync();
    }

    private void sync() {
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Nullable
    private static BlockPos readPos(CompoundTag tag, String key) {
        if (tag.contains(key, Tag.TAG_COMPOUND)) {
            CompoundTag legacy = tag.getCompound(key);
            return new BlockPos(legacy.getInt("X"), legacy.getInt("Y"), legacy.getInt("Z"));
        }
        return NbtUtils.readBlockPos(tag, key).orElse(null);
    }

    @Nullable
    private static BlockPos parseTag(String tag) {
        try {
            return BlockPos.of(Long.parseLong(tag.substring(BOSS_TAG_PREFIX.length())));
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.keys = tag.getInt("keys");
        this.timer = tag.getInt("timer");
        this.boss = tag.hasUUID("boss") ? tag.getUUID("boss") : null;
        this.lastBossPos = readPos(tag, "last_boss_pos");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("keys", this.keys);
        tag.putInt("timer", this.timer);
        if (this.boss != null) {
            tag.putUUID("boss", this.boss);
        }
        if (this.lastBossPos != null) {
            tag.put("last_boss_pos", NbtUtils.writeBlockPos(this.lastBossPos));
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("keys", this.keys);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
