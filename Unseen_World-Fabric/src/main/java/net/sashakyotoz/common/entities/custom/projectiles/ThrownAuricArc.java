package net.sashakyotoz.common.entities.custom.projectiles;

import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.mixin.access.ThrownTridentAccessor;
import org.joml.Vector3f;

public class ThrownAuricArc extends ThrownTrident {
    private static final String SUNBURST_KEY = "Sunburst";
    private static final float MARK_RADIUS = 2.0F;
    private static final int MARK_DURATION = 60;
    private static final int MARK_EFFECT_TICKS = 160;
    private static final DustParticleOptions MARK_PARTICLE = new DustParticleOptions(new Vector3f(1.0F, 0.78F, 0.25F), 1.0F);

    private boolean sunburst;

    public ThrownAuricArc(EntityType<? extends ThrownAuricArc> type, Level level) {
        super(type, level);
    }

    public static ThrownAuricArc create(Level level, LivingEntity owner, ItemStack stack, boolean sunburst) {
        ThrownAuricArc arc = new ThrownAuricArc(ModEntities.THROWN_AURIC_ARC, level);
        arc.setOwner(owner);
        arc.setPos(owner.getX(), owner.getEyeY() - 0.1D, owner.getZ());
        if (owner instanceof Player) {
            arc.pickup = Pickup.ALLOWED;
        }
        arc.setPickupItemStack(stack.copy());
        byte loyalty = level instanceof ServerLevel server
                ? (byte) Mth.clamp(EnchantmentHelper.getTridentReturnToOwnerAcceleration(server, stack, arc), 0, 127)
                : 0;
        arc.getEntityData().set(ThrownTridentAccessor.unseen_world$getLoyaltyId(), loyalty);
        arc.getEntityData().set(ThrownTridentAccessor.unseen_world$getFoilId(), stack.hasFoil());
        arc.sunburst = sunburst;
        return arc;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        this.leaveGildedMark(result.getEntity().position());
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.leaveGildedMark(result.getLocation());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean(SUNBURST_KEY, this.sunburst);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.sunburst = tag.getBoolean(SUNBURST_KEY);
    }

    private void leaveGildedMark(Vec3 at) {
        if (!this.sunburst || this.level().isClientSide) {
            return;
        }
        this.sunburst = false;
        AreaEffectCloud cloud = new AreaEffectCloud(this.level(), at.x, at.y, at.z);
        if (this.getOwner() instanceof LivingEntity owner) {
            cloud.setOwner(owner);
        }
        cloud.setRadius(MARK_RADIUS);
        cloud.setDuration(MARK_DURATION);
        cloud.setWaitTime(0);
        cloud.setRadiusPerTick(0.0F);
        cloud.setParticle(MARK_PARTICLE);
        cloud.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, MARK_EFFECT_TICKS, 0));
        this.level().addFreshEntity(cloud);
    }
}