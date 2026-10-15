package net.sashakyotoz.common.entities.util;

import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public final class BossParticles {
    private BossParticles() {
    }

    public static Vector3f rgb(int color) {
        return new Vector3f(((color >> 16) & 0xFF) / 255.0F, ((color >> 8) & 0xFF) / 255.0F, (color & 0xFF) / 255.0F);
    }

    public static DustParticleOptions dust(int color, float scale) {
        return new DustParticleOptions(rgb(color), scale);
    }

    public static DustColorTransitionOptions fade(int from, int to, float scale) {
        return new DustColorTransitionOptions(rgb(from), rgb(to), scale);
    }

    public static void ring(Level level, ParticleOptions particle, Vec3 center, double radius, int count, double outwardSpeed, double upSpeed) {
        for (int i = 0; i < count; i++) {
            double angle = i * (Math.PI * 2.0D / count);
            double cos = Math.cos(angle);
            double sin = Math.sin(angle);
            level.addParticle(particle, center.x + cos * radius, center.y, center.z + sin * radius, cos * outwardSpeed, upSpeed, sin * outwardSpeed);
        }
    }

    public static void verticalRing(Level level, ParticleOptions particle, Vec3 center, Vec3 axis, double radius, int count, double phase) {
        Vec3 side = new Vec3(axis.z, 0.0D, -axis.x).normalize();
        for (int i = 0; i < count; i++) {
            double angle = phase + i * (Math.PI * 2.0D / count);
            Vec3 point = center.add(side.scale(Math.cos(angle) * radius)).add(0.0D, Math.sin(angle) * radius, 0.0D);
            level.addParticle(particle, point.x, point.y, point.z, 0.0D, 0.0D, 0.0D);
        }
    }

    public static void line(Level level, ParticleOptions particle, Vec3 from, Vec3 to, double step, double upSpeed) {
        Vec3 delta = to.subtract(from);
        double length = delta.length();
        if (length < 1.0E-4D) {
            return;
        }
        Vec3 direction = delta.scale(1.0D / length);
        for (double d = 0.0D; d <= length; d += step) {
            Vec3 point = from.add(direction.scale(d));
            level.addParticle(particle, point.x, point.y, point.z, 0.0D, upSpeed, 0.0D);
        }
    }

    public static void converge(Level level, RandomSource random, ParticleOptions particle, Vec3 center, double radius, int count) {
        for (int i = 0; i < count; i++) {
            Vec3 offset = randomUnit(random).scale(radius * (0.6D + random.nextDouble() * 0.4D));
            level.addParticle(particle, center.x, center.y, center.z, offset.x, offset.y, offset.z);
        }
    }

    public static void burst(Level level, RandomSource random, ParticleOptions particle, Vec3 center, int count, double speed) {
        for (int i = 0; i < count; i++) {
            Vec3 velocity = randomUnit(random).scale(speed * (0.5D + random.nextDouble() * 0.5D));
            level.addParticle(particle, center.x, center.y, center.z, velocity.x, velocity.y, velocity.z);
        }
    }

    public static void helix(Level level, ParticleOptions particle, Vec3 base, double radius, double height, int tick, int strands, int period) {
        double progress = (tick % period) / (double) period;
        for (int strand = 0; strand < strands; strand++) {
            double angle = progress * Math.PI * 4.0D + strand * (Math.PI * 2.0D / strands);
            level.addParticle(particle, base.x + Math.sin(angle) * radius, base.y + progress * height, base.z + Math.cos(angle) * radius, 0.0D, 0.0D, 0.0D);
        }
    }

    public static void column(Level level, RandomSource random, ParticleOptions particle, Vec3 base, double radius, int count, double upSpeed) {
        for (int i = 0; i < count; i++) {
            double angle = random.nextDouble() * Math.PI * 2.0D;
            double r = radius * Math.sqrt(random.nextDouble());
            level.addParticle(particle, base.x + Math.cos(angle) * r, base.y + random.nextDouble() * 0.5D, base.z + Math.sin(angle) * r, 0.0D, upSpeed * (0.6D + random.nextDouble() * 0.4D), 0.0D);
        }
    }

    public static void dome(Level level, RandomSource random, ParticleOptions particle, Vec3 center, double radius, int count) {
        for (int i = 0; i < count; i++) {
            Vec3 direction = randomUnit(random);
            if (direction.y < 0.0D) {
                direction = new Vec3(direction.x, -direction.y, direction.z);
            }
            Vec3 point = center.add(direction.scale(radius));
            level.addParticle(particle, point.x, point.y, point.z, 0.0D, 0.0D, 0.0D);
        }
    }

    public static void motes(Level level, RandomSource random, ParticleOptions particle, Vec3 center, double radius, double height, int count, double upSpeed) {
        for (int i = 0; i < count; i++) {
            double angle = random.nextDouble() * Math.PI * 2.0D;
            double r = radius * (0.5D + random.nextDouble() * 0.5D);
            level.addParticle(particle, center.x + Math.cos(angle) * r, center.y + random.nextDouble() * height, center.z + Math.sin(angle) * r, 0.0D, upSpeed, 0.0D);
        }
    }

    public static Vec3 randomUnit(RandomSource random) {
        double theta = random.nextDouble() * Math.PI * 2.0D;
        double z = random.nextDouble() * 2.0D - 1.0D;
        double r = Math.sqrt(1.0D - z * z);
        return new Vec3(r * Math.cos(theta), z, r * Math.sin(theta));
    }
}
