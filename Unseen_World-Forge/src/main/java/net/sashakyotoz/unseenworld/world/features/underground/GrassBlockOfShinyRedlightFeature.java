
package net.sashakyotoz.unseenworld.world.features.underground;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.sashakyotoz.unseenworld.UnseenWorld;
import net.sashakyotoz.unseenworld.registries.UnseenWorldBlocks;

public class GrassBlockOfShinyRedlightFeature extends OreFeature {
	public GrassBlockOfShinyRedlightFeature() {
		super(OreConfiguration.CODEC);
	}

	public boolean place(FeaturePlaceContext<OreConfiguration> context) {
		WorldGenLevel level = context.level();
		if (!level.getLevel().dimension().equals(UnseenWorld.THE_DARKNESS))
			return false;
		int x = context.origin().getX();
		int y = context.origin().getY();
		int z = context.origin().getZ();
		double sx;
		double sy;
		double sz;
		if (!level.getBlockState(BlockPos.containing(x, y + 1, z)).canOcclude()) {
			sx = -3;
			for (int i = 0; i < 6; i++) {
				sy = -1;
				for (int j = 0; j < 3; j++) {
					sz = -3;
					for (int k = 0; k < 6; k++) {
						if (((level.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))).getBlock() == UnseenWorldBlocks.DARK_GRASS_BLOCK.get()
								|| (level.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))).getBlock() == UnseenWorldBlocks.AMETHYST_GRASS_BLOCK.get())
								&& !level.getBlockState(BlockPos.containing(x + sx, y + sy + 1, z + sz)).canOcclude()) {
							if (Math.random() < 0.5) {
								BlockPos blockPos = BlockPos.containing(x + sx, y + sy, z + sz);
								BlockState _bs = UnseenWorldBlocks.GRASS_BLOCK_OF_SHINY_REDLIGHT.get().defaultBlockState();
								level.setBlock(blockPos, _bs, 3);
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
