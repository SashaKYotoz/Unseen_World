package net.sashakyotoz.unseenworld.managers;

import net.sashakyotoz.unseenworld.util.UnseenWorldModParticleTypes;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;

public class MeteoritestrophyOnEffectActiveTickProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z) {
        if (Math.random() < 0.25) {
            for (int index0 = 0; index0 < (int) Mth.nextDouble(RandomSource.create(), 1, 3); index0++) {
                if (Math.random() < 0.125) {
                    if (world instanceof Level _level && !_level.isClientSide())
                        _level.explode(null, (x + Mth.nextDouble(RandomSource.create(), -5, 5)), y, (z + Mth.nextDouble(RandomSource.create(), -5, 5)), 2, Level.ExplosionInteraction.BLOCK);
                    if (world instanceof ServerLevel _level)
                        _level.sendParticles(UnseenWorldModParticleTypes.REDNESS.get(), (x + Mth.nextDouble(RandomSource.create(), -5, 5)), (y + 5), (z + Mth.nextDouble(RandomSource.create(), -5, 5)), 9, 5, 5, 5, 1);
                }
            }
        }
    }
}
