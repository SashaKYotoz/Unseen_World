package net.sashakyotoz.unseenworld.managers;

import net.minecraft.sounds.SoundEvents;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.sashakyotoz.unseenworld.registries.UnseenWorldModBlocks;

public class VoidStaffProjectileHitsBlockProcedure {
    public static void execute(Level level, double x, double y, double z) {
        double sx;
        double sy;
        double sz;
        if (!level.isClientSide()) {
            level.playSound(null, BlockPos.containing(x, y, z), SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.NEUTRAL, 1, 1);
            level.explode(null, x, y, z, 1, Level.ExplosionInteraction.NONE);
        } else
            level.playLocalSound(x, y, z, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.NEUTRAL, 1, 1, false);
        sx = -2;
        for (int i = 0; i < 4; i++) {
            sy = -2;
            for (int k = 0; k < 4; k++) {
                sz = -2;
                for (int j = 0; j < 4; j++) {
                    if ((level.getBlockState(BlockPos.containing(i + sx, y + sy, z + sz))).is(Blocks.GRASS_BLOCK)) {
                        BlockPos pos = BlockPos.containing(i + sx, y + sy, z + sz);
                        BlockState state = UnseenWorldModBlocks.DARK_GRASS_BLOCK.get().defaultBlockState();
                        level.setBlock(pos, state, 3);
                    } else if ((level.getBlockState(BlockPos.containing(i + sx, y + sy, z + sz))).is(Blocks.STONE)) {
                        BlockPos pos = BlockPos.containing(i + sx, y + sy, z + sz);
                        BlockState state = Blocks.BLACKSTONE.defaultBlockState();
                        level.setBlock(pos, state, 3);
                    } else if ((level.getBlockState(BlockPos.containing(i + sx, y + sy, z + sz))).is(UnseenWorldModBlocks.DARK_GRASS_BLOCK.get())) {
                        if (Math.random() < 0.25) {
                            BlockPos pos = BlockPos.containing(i + sx, y + sy + 1, z + sz);
                            BlockState state = UnseenWorldModBlocks.DARK_CRIMSON_SAPLING.get().defaultBlockState();
                            level.setBlock(pos, state, 3);
                        }
                    } else if ((level.getBlockState(BlockPos.containing(i + sx, y + sy, z + sz))).getBlock() == UnseenWorldModBlocks.DARK_WATER.get() || (level.getBlockState(BlockPos.containing(i + sx, y + sy, z + sz))).getBlock() == Blocks.LAVA) {
                        if (level instanceof ServerLevel serverLevel)
                            serverLevel.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(i, y, z), Vec2.ZERO, serverLevel, 4, "", Component.literal(""), serverLevel.getServer(), null).withSuppressedOutput(),
                                    "kill @e[type=unseen_world:projectile_void_staff]");
                    }
                    sz = sz + 1;
                }
                sy = sy + 1;
            }
            sx = sx + 1;
        }
    }
}
