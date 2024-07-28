
package net.sashakyotoz.unseenworld.world.features.underground;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;

import net.sashakyotoz.unseenworld.registries.UnseenWorldBlocks;

import java.util.Set;

public class GrizzlyLightBlockFeature extends OreFeature {
	private final Set<ResourceKey<Level>> generate_dimensions = Set.of(ResourceKey.create(Registries.DIMENSION, new ResourceLocation("unseen_world:the_darkness")));

	public GrizzlyLightBlockFeature() {
		super(OreConfiguration.CODEC);
	}

	public boolean place(FeaturePlaceContext<OreConfiguration> context) {
		WorldGenLevel world = context.level();
		if (!generate_dimensions.contains(world.getLevel().dimension()))
			return false;
		int x = context.origin().getX();
		int y = context.origin().getY();
		int z = context.origin().getZ();
		double sx, sy, sz;
		if ((world.getBlockState(BlockPos.containing(x, y + 1, z))).getBlock() == UnseenWorldBlocks.GRIZZLY_LEAVES.get() && (world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == Blocks.AIR) {
			sx = -3;
			for (int x1 = 0; x1 < 6; x1++) {
				sy = -3;
				for (int y1 = 0; y1 < 6; y1++) {
					sz = -3;
					for (int z1 = 0; z1 < 6; z1++) {
						if ((world.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))).getBlock() == UnseenWorldBlocks.GRIZZLY_LEAVES.get()
								&& (world.getBlockState(BlockPos.containing(x + sx, (y + sy) - 1, z + sz))).getBlock() == Blocks.AIR) {
							BlockPos pos = BlockPos.containing(x + sx, (y + sy) - 1, z + sz);
							BlockState blockState = UnseenWorldBlocks.GRIZZLY_LEAVES.get().defaultBlockState();
							world.setBlock(pos, blockState, 3);
							if (Math.random() < 0.25) {
								if ((world.getBlockState(BlockPos.containing(x + sx, (y + sy) - 2, z + sz))).getBlock() == Blocks.AIR) {
									BlockPos blockPos = BlockPos.containing(x + sx, (y + sy) - 2, z + sz);
									BlockState state = UnseenWorldBlocks.GRIZZLY_LIGHT_BLOCK.get().defaultBlockState();
									world.setBlock(blockPos, state, 3);
								}
							}
						}
						sz = sz + 1;
					}
					sy = sy + 1;
				}
				sx = sx + 1;
			}
		}
		return true;
	}
}
