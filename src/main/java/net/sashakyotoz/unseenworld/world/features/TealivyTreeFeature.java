
package net.sashakyotoz.unseenworld.world.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.sashakyotoz.unseenworld.registries.UnseenWorldBlocks;

import java.util.List;
import java.util.Set;

public class TealivyTreeFeature extends Feature<NoneFeatureConfiguration> {
	private final Set<ResourceKey<Level>> generate_dimensions = Set.of(ResourceKey.create(Registries.DIMENSION, new ResourceLocation("unseen_world:the_darkness")));
	private final List<Block> base_blocks;
	private StructureTemplate template = null;

	public TealivyTreeFeature() {
		super(NoneFeatureConfiguration.CODEC);
		base_blocks = List.of(UnseenWorldBlocks.TEALIVE_LUMINOUS_GRASS_BLOCK.get());
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		RandomSource random = context.random();
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		if (!generate_dimensions.contains(level.getLevel().dimension()))
			return false;
		if (template == null)
			template = level.getLevel().getStructureManager().getOrCreate(new ResourceLocation("unseen_world", "tealivy_tree"));
		boolean anyPlaced = false;
		if ((random.nextInt(1000000) + 1) <= 250000) {
			int count = random.nextInt(1) + 1;
			for (int a = 0; a < count; a++) {
				int i = pos.getX() + random.nextInt(16);
				int k = pos.getZ() + random.nextInt(16);
				int j = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, i, k) - 1;
				if (!base_blocks.contains(level.getBlockState(new BlockPos(i, j, k)).getBlock()))
					continue;
				BlockPos spawnTo = new BlockPos(i, j -1, k);
				if (template.placeInWorld(context.level(), spawnTo, spawnTo, new StructurePlaceSettings().setMirror(Mirror.values()[random.nextInt(2)]).setRotation(Rotation.values()[random.nextInt(3)]).setRandom(random)
						.addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK).setIgnoreEntities(false), random, 2)) {
					anyPlaced = true;
				}
			}
		}
		return anyPlaced;
	}
}
