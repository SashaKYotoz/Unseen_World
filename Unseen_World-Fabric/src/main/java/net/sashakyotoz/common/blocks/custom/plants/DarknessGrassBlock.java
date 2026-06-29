package net.sashakyotoz.common.blocks.custom.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.world.features.placements.ModPlacements;

import java.util.List;
import java.util.Optional;

public class DarknessGrassBlock extends GrassBlock {
    public DarknessGrassBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

//    @Override
//    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
//        super.afterBreak(world, player, pos, state, blockEntity, tool);
//        if (player instanceof ServerPlayerEntity player1)
//            GrippingData.addGrippingSeconds((IEntityDataSaver) player1,10);
//    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos blockPos = pos.above();
        BlockState blockState = random.nextBoolean() ? ModBlocks.GLOOMWEED.defaultBlockState() : ModBlocks.MURKTUFT.defaultBlockState();
        Optional<Holder.Reference<PlacedFeature>> optional = world.registryAccess()
                .registryOrThrow(Registries.PLACED_FEATURE)
                .getHolder(ModPlacements.GLOOMWEED_PATCH);

        label49:
        for (int i = 0; i < 128; i++) {
            BlockPos blockPos2 = blockPos;

            for (int j = 0; j < i / 16; j++) {
                blockPos2 = blockPos2.offset(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
                if (!world.getBlockState(blockPos2.below()).is(this) || world.getBlockState(blockPos2).isCollisionShapeFullBlock(world, blockPos2)) {
                    continue label49;
                }
            }

            BlockState blockState2 = world.getBlockState(blockPos2);
            if (blockState2.is(blockState.getBlock()) && random.nextInt(10) == 0) {
                ((BonemealableBlock) blockState.getBlock()).performBonemeal(world, random, blockPos2, blockState2);
            }

            if (blockState2.isAir()) {
                Holder<PlacedFeature> registryEntry;
                if (random.nextInt(8) == 0) {
                    List<ConfiguredFeature<?, ?>> list = world.getBiome(blockPos2).value().getGenerationSettings().getFlowerFeatures();
                    if (list.isEmpty()) {
                        continue;
                    }

                    registryEntry = ((RandomPatchConfiguration) list.get(0).config()).feature();
                } else {
                    if (optional.isEmpty())
                        continue;
                    registryEntry = optional.get();
                }

                registryEntry.value().placeWithBiomeCheck(world, world.getChunkSource().getGenerator(), random, blockPos2);
            }
        }
    }
}