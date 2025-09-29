
package net.sashakyotoz.unseenworld.world.features.underground;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.sashakyotoz.unseenworld.registries.UnseenWorldBlocks;

import java.util.Set;

public class GrassBlockOfShinyRedlightFeature extends OreFeature {
	private final Set<ResourceKey<Level>> generate_dimensions = Set.of(ResourceKey.create(Registries.DIMENSION, new ResourceLocation("unseen_world:the_darkness")));

	public GrassBlockOfShinyRedlightFeature() {
		super(OreConfiguration.CODEC);
	}

	public boolean place(FeaturePlaceContext<OreConfiguration> context) {
		WorldGenLevel world = context.level();
		if (!generate_dimensions.contains(world.getLevel().dimension()))
			return false;
		int x = context.origin().getX();
		int y = context.origin().getY();
		int z = context.origin().getZ();
		double sx;
		double sy;
		double sz;
		if (!world.getBlockState(BlockPos.containing(x, y + 1, z)).canOcclude()) {
			sx = -3;
			for (int i = 0; i < 6; i++) {
				sy = -1;
				for (int j = 0; j < 3; j++) {
					sz = -3;
					for (int k = 0; k < 6; k++) {
						if (((world.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))).getBlock() == UnseenWorldBlocks.DARK_GRASS_BLOCK.get()
								|| (world.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))).getBlock() == UnseenWorldBlocks.AMETHYST_GRASS_BLOCK.get())
								&& !world.getBlockState(BlockPos.containing(x + sx, y + sy + 1, z + sz)).canOcclude()) {
							if (Math.random() < 0.5) {
								BlockPos blockPos = BlockPos.containing(x + sx, y + sy, z + sz);
								BlockState _bs = UnseenWorldBlocks.GRASS_BLOCK_OF_SHINY_REDLIGHT.get().defaultBlockState();
								world.setBlock(blockPos, _bs, 3);
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
