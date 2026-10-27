package net.sashakyotoz.common.entities.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class MinionOwnership {
    private static final String OWNER_KEY = "Owner";

    private final Mob minion;
    private final EntityDataAccessor<Integer> ownerId;
    @Nullable
    private Entity owner;
    @Nullable
    private UUID ownerUUID;

    public MinionOwnership(Mob minion, EntityDataAccessor<Integer> ownerId) {
        this.minion = minion;
        this.ownerId = ownerId;
    }

    public static void define(SynchedEntityData.Builder data, EntityDataAccessor<Integer> ownerId) {
        data.define(ownerId, -1);
    }

    public void set(@Nullable Entity owner) {
        this.owner = owner;
        this.ownerUUID = owner == null ? null : owner.getUUID();
        this.minion.getEntityData().set(this.ownerId, owner == null ? -1 : owner.getId());
    }

    @Nullable
    public Entity get() {
        if (this.minion.level().isClientSide()) {
            int id = this.minion.getEntityData().get(this.ownerId);
            return id != -1 ? this.minion.level().getEntity(id) : null;
        }
        if (this.owner != null && !this.owner.isRemoved()) {
            return this.owner;
        }
        if (this.ownerUUID != null && this.minion.level() instanceof ServerLevel serverLevel) {
            this.owner = serverLevel.getEntity(this.ownerUUID);
            this.minion.getEntityData().set(this.ownerId, this.owner == null ? -1 : this.owner.getId());
            return this.owner;
        }
        return null;
    }

    public void save(CompoundTag tag) {
        if (this.ownerUUID != null) {
            tag.putUUID(OWNER_KEY, this.ownerUUID);
        }
    }

    public void load(CompoundTag tag) {
        if (tag.hasUUID(OWNER_KEY)) {
            this.ownerUUID = tag.getUUID(OWNER_KEY);
        } else if (tag.contains(OWNER_KEY)) {
            this.ownerUUID = OldUsersConverter.convertMobOwnerIfNecessary(this.minion.getServer(), tag.getString(OWNER_KEY));
        }
    }
}
