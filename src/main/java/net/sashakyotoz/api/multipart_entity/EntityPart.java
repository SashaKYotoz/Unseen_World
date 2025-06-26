package net.sashakyotoz.api.multipart_entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;

public class EntityPart extends Entity {
    public final Entity owner;
    private final EntityDimensions hitbox;

    public EntityPart(Entity owner, float width, float height) {
        super(owner.getType(), owner.getWorld());
        this.owner = owner;
        this.hitbox = EntityDimensions.changing(width, height);
        this.calculateDimensions();
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {}

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {}

    @Override
    protected void initDataTracker() {}

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public boolean canBeHitByProjectile() {
        return getWorld().isClient() || !super.canBeHitByProjectile() ? false : owner.canBeHitByProjectile();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return getWorld().isClient() || isInvulnerableTo(source) ? false : owner.damage(source, amount);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return owner.isInvulnerableTo(damageSource);
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        return owner.interact(player, hand);
    }

    @Override
    public boolean isAlive() {
        return owner.isAlive() && super.isAlive();
    }

    @Override
    public boolean isPartOf(Entity entity) {
        return this == entity || owner == entity;
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean shouldSave() {
        return false;
    }

    @Nullable
    @Override
    public ItemStack getPickBlockStack() {
        return this.owner.getPickBlockStack();
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return hitbox;
    }

    @Override
    public boolean startRiding(Entity vehicle, boolean force) {
        return owner.startRiding(vehicle, force);
    }

    @Override
    public boolean isTeammate(Entity entity) {
        return owner.isTeammate(entity);
    }

    @Override
    public boolean isInvisible() {
        return owner.isInvisible();
    }

    @Override
    public boolean isGlowing() {
        return owner.isGlowing();
    }

    @Override
    public boolean isInvisibleTo(PlayerEntity player) {
        return owner.isInvisibleTo(player);
    }

    @Override
    public boolean onKilledOther(ServerWorld level, LivingEntity entity) {
        return owner.onKilledOther(level, entity);
    }

    @Override
    public boolean isAttackable() {
        return owner.isAttackable();
    }

    @Override
    public boolean handleAttack(Entity entity) {
        return owner.handleAttack(entity);
    }

    @Override
    public boolean isInvulnerable() {
        return owner.isInvulnerable() || super.isInvulnerable();
    }

    @Override
    public boolean isInPose(EntityPose pose) {
        return owner.isInPose(pose);
    }

    @Override
    public boolean hasPortalCooldown() {
        return owner.hasPortalCooldown();
    }

    @Override
    public boolean isOnGround() {
        return owner.isOnGround();
    }

    @Override
    public boolean hasNoGravity() {
        return owner.hasNoGravity();
    }

    @Override
    public boolean occludeVibrationSignals() {
        return true;
    }

    @Override
    public boolean isFireImmune() {
        return owner.isFireImmune();
    }

    @Override
    public boolean shouldSpawnSprintingParticles() {
        return false;
    }

    /**
     * <p>Sets position relative to position of the owner entity.</p>
     * <p><b>x, y, z</b> - offsets relative to the owner's position (without accounting any rotations).</p>
     * <p><b>centerX, centerY, centerZ</b> - offset from owner's position relative to which part will be placed. Not affected by rotations.</p>
     * <p><b>pitch, yaw</b> - passed X and Y rotations (in degrees), relative to which offsets will be transformed.</p>
     */
    public void setRelativePos(double x, double y, double z, double centerX, double centerY, double centerZ, double pitch, double yaw) {
        lastRenderX = getX();
        lastRenderY = getY();
        lastRenderZ = getZ();

        double cosYaw = Math.cos(-yaw * 0.017453292);
        double sinYaw = Math.sin(-yaw * 0.017453292);
        double cosPitch = Math.cos(pitch * 0.017453292);
        double sinPitch = Math.sin(pitch * 0.017453292);
        setPosition(owner.getX() + centerX + z * sinYaw * cosPitch + x * cosYaw + y * sinYaw * sinPitch,
                owner.getY() + centerY + z * -sinPitch + y * cosPitch,
                owner.getZ() + centerZ + z * cosYaw * cosPitch + x * -sinYaw + y * cosYaw * sinPitch);

        prevX = getX();
        prevY = getY();
        prevZ = getZ();
    }

    public void setRelativePos(double x, double y, double z, double centerX, double centerY, double centerZ) {
        setRelativePos(x, y ,z, centerX, centerY, centerZ, owner.getPitch(), owner.getYaw());
    }

    public void setRelativePos(double x, double y, double z, double pitch, double yaw) {
        setRelativePos(x, y ,z, 0, 0, 0, pitch, yaw);
    }

    public void setRelativePos(double x, double y, double z) {
        setRelativePos(x, y ,z, 0, 0, 0, owner.getPitch(), owner.getYaw());
    }
}