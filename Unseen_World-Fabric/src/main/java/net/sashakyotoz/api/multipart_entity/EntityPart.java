package net.sashakyotoz.api.multipart_entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class EntityPart extends Entity {
    public final Entity owner;
    private final EntityDimensions hitbox;

    public EntityPart(Entity owner, float width, float height) {
        super(owner.getType(), owner.level());
        this.owner = owner;
        this.hitbox = EntityDimensions.scalable(width, height);
        this.refreshDimensions();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {}

    @Override
    protected void defineSynchedData() {}

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean canBeHitByProjectile() {
        return level().isClientSide() || !super.canBeHitByProjectile() ? false : owner.canBeHitByProjectile();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return level().isClientSide() || isInvulnerableTo(source) ? false : owner.hurt(source, amount);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return owner == null ? super.isInvulnerableTo(damageSource) : owner.isInvulnerableTo(damageSource);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        return owner.interact(player, hand);
    }

    @Override
    public boolean isAlive() {
        return owner == null ? super.isAlive() : owner.isAlive() && super.isAlive();
    }

    @Override
    public boolean is(Entity entity) {
        return this == entity || owner != null && owner == entity;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Nullable
    @Override
    public ItemStack getPickResult() {
        return this.owner.getPickResult();
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return hitbox == null ? super.getDimensions(pose) : hitbox;
    }

    @Override
    public boolean startRiding(Entity vehicle, boolean force) {
        return owner.startRiding(vehicle, force);
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        return owner == null ? super.isAlliedTo(entity) : owner.isAlliedTo(entity);
    }

    @Override
    public boolean isInvisible() {
        return owner == null ? super.isInvisible() : owner.isInvisible();
    }

    @Override
    public boolean isCurrentlyGlowing() {
        return owner == null ? super.isCurrentlyGlowing() : owner.isCurrentlyGlowing();
    }

    @Override
    public boolean isInvisibleTo(Player player) {
        return owner == null ? super.isInvisibleTo(player) : owner.isInvisibleTo(player);
    }

    @Override
    public boolean killedEntity(ServerLevel level, LivingEntity entity) {
        return owner.killedEntity(level, entity);
    }

    @Override
    public boolean isAttackable() {
        return owner != null && owner.isAttackable();
    }

    @Override
    public boolean skipAttackInteraction(Entity entity) {
        return owner.skipAttackInteraction(entity);
    }

    @Override
    public boolean isInvulnerable() {
        return owner != null && owner.isInvulnerable() || super.isInvulnerable();
    }

    @Override
    public boolean hasPose(Pose pose) {
        return owner == null ? super.hasPose(pose) : owner.hasPose(pose);
    }

    @Override
    public boolean isOnPortalCooldown() {
        return owner == null ? super.isOnPortalCooldown() : owner.isOnPortalCooldown();
    }

    @Override
    public boolean onGround() {
        return owner == null ? super.onGround() : owner.onGround();
    }

    @Override
    public boolean isNoGravity() {
        return owner == null ? super.isNoGravity() : owner.isNoGravity();
    }

    @Override
    public boolean dampensVibrations() {
        return true;
    }

    @Override
    public boolean fireImmune() {
        return owner == null ? super.fireImmune() : owner.fireImmune();
    }

    @Override
    public boolean canSpawnSprintParticle() {
        return false;
    }

    public void setRelativePos(double x, double y, double z, double centerX, double centerY, double centerZ, double pitch, double yaw) {
        xOld = getX();
        yOld = getY();
        zOld = getZ();

        double cosYaw = Math.cos(-yaw * 0.017453292);
        double sinYaw = Math.sin(-yaw * 0.017453292);
        double cosPitch = Math.cos(pitch * 0.017453292);
        double sinPitch = Math.sin(pitch * 0.017453292);
        setPos(owner.getX() + centerX + z * sinYaw * cosPitch + x * cosYaw + y * sinYaw * sinPitch,
                owner.getY() + centerY + z * -sinPitch + y * cosPitch,
                owner.getZ() + centerZ + z * cosYaw * cosPitch + x * -sinYaw + y * cosYaw * sinPitch);

        xo = getX();
        yo = getY();
        zo = getZ();
    }

    public void setRelativePos(double x, double y, double z, double centerX, double centerY, double centerZ) {
        setRelativePos(x, y ,z, centerX, centerY, centerZ, owner.getXRot(), owner.getYRot());
    }

    public void setRelativePos(double x, double y, double z, double pitch, double yaw) {
        setRelativePos(x, y ,z, 0, 0, 0, pitch, yaw);
    }

    public void setRelativePos(double x, double y, double z) {
        setRelativePos(x, y ,z, 0, 0, 0, owner.getXRot(), owner.getYRot());
    }
}