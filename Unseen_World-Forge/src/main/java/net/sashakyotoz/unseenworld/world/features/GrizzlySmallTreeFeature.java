
package net.sashakyotoz.unseenworld.world.features;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.sashakyotoz.unseenworld.UnseenWorld;
import net.sashakyotoz.unseenworld.registries.UnseenWorldBlocks;

import java.util.List;

public class GrizzlySmallTreeFeature extends Feature<NoneFeatureConfiguration> {
private final List<Block> base_blocks;
	private StructureTemplate template = null;

	public GrizzlySmallTreeFeature() {
		super(NoneFeatureConfiguration.CODEC);
		base_blocks = List.of(Blocks.BLACKSTONE, UnseenWorldBlocks.GRASS_BLOCK_OF_SHINY_REDLIGHT.get());
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
		if (!level.getLevel().dimension().equals(UnseenWorld.THE_DARKNESS))
			return false;
		if (template == null)
			template = level.getLevel().getStructureManager().getOrCreate(UnseenWorld.makeId("grizzly_small_tree"));
		boolean anyPlaced = false;
		if ((context.random().nextInt(1000000) + 1) <= 1500) {
			int count = context.random().nextInt(1) + 1;
			for (int a = 0; a < count; a++) {
				int i = context.origin().getX() + context.random().nextInt(16);
				int k = context.origin().getZ() + context.random().nextInt(16);
				int j = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, i, k) - 1;
				if (!base_blocks.contains(level.getBlockState(new BlockPos(i, j, k)).getBlock()))
					continue;
				BlockPos spawnTo = new BlockPos(i, j + -1, k);
				if (template.placeInWorld(level, spawnTo, spawnTo, new StructurePlaceSettings().setMirror(Mirror.values()[context.random().nextInt(2)]).setRotation(Rotation.values()[context.random().nextInt(3)]).setRandom(context.random())
						.addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK).setIgnoreEntities(false), context.random(), 2)) {
					anyPlaced = true;
				}
			}
		}
		return anyPlaced;
	}
}
