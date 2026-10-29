package net.sashakyotoz.common.items.combat;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.utils.CombatHelper;

import java.util.OptionalInt;

public final class FissureTask implements ServerTasks.Task {

    private final LivingEntity owner;
    private final Vec3 origin;
    private final Vec3 direction;
    private final IntSet struck = new IntOpenHashSet();
    private int feetY;
    private int distance;

    private FissureTask(LivingEntity owner) {
        this.owner = owner;
        this.origin = owner.position();
        this.direction = Vec3.directionFromRotation(0.0F, owner.getYRot());
        this.feetY = owner.getBlockY();
    }

    public static void start(ServerLevel level, LivingEntity owner) {
        ServerTasks.schedule(level, new FissureTask(owner));
        level.playSound(null, owner.getX(), owner.getY(), owner.getZ(), SoundEvents.IRON_GOLEM_ATTACK, SoundSource.PLAYERS, 1.0F, 0.6F);
    }

    @Override
    public boolean tick(ServerLevel level) {
        if (this.owner.isRemoved() || this.owner.level() != level) {
            return true;
        }
        for (int step = 0; step < 2; step++) {
            this.distance++;
            if (this.distance > 8) {
                return true;
            }
            Vec3 point = this.origin.add(this.direction.scale(this.distance));
            int x = Mth.floor(point.x);
            int z = Mth.floor(point.z);
            if (!level.isLoaded(new BlockPos(x, this.feetY, z))) {
                return true;
            }
            OptionalInt nextFeetY = this.findFeetY(level, x, z);
            if (nextFeetY.isEmpty()) {
                return true;
            }
            this.feetY = nextFeetY.getAsInt();
            this.erupt(level, new BlockPos(x, this.feetY, z));
        }
        return false;
    }

    private OptionalInt findFeetY(ServerLevel level, int x, int z) {
        BlockPos feet = new BlockPos(x, this.feetY, z);
        if (isSolid(level, feet)) {
            return isSolid(level, feet.above()) ? OptionalInt.empty() : OptionalInt.of(this.feetY + 1);
        }
        for (int drop = 1; drop <= 3; drop++) {
            if (isSolid(level, feet.below(drop))) {
                return OptionalInt.of(this.feetY - drop + 1);
            }
        }
        return OptionalInt.empty();
    }

    private void erupt(ServerLevel level, BlockPos feet) {
        BlockState ground = level.getBlockState(feet.below());
        double centerX = feet.getX() + 0.5D;
        double centerZ = feet.getZ() + 0.5D;
        if (!ground.isAir()) {
            level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, ground), centerX, feet.getY() + 0.1D, centerZ, 12, 0.3D, 0.05D, 0.3D, 0.15D);
            if (this.distance % 2 == 0) {
                level.playSound(null, centerX, feet.getY(), centerZ, ground.getSoundType().getBreakSound(), SoundSource.PLAYERS, 0.8F, 0.7F);
            }
        }
        AABB area = new AABB(feet).inflate(0.25D, 0.0D, 0.25D).expandTowards(0.0D, 0.5D, 0.0D);
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, area, entity -> CombatHelper.canHit(this.owner, entity))) {
            if (this.struck.add(target.getId())) {
                target.hurt(CombatHelper.attackSource(this.owner), 8.0F);
                CombatHelper.pop(target, 0.5D);
            }
        }
    }

    private static boolean isSolid(ServerLevel level, BlockPos pos) {
        return !level.getBlockState(pos).getCollisionShape(level, pos).isEmpty();
    }
}