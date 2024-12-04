package net.sashakyotoz.common.blocks.custom.plants;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.networking.data.GrippingData;
import net.sashakyotoz.common.world.features.placements.ModPlacements;
import net.sashakyotoz.utils.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class DarknessGrassBlock extends GrassBlock {
    public DarknessGrassBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

//    @Override
//    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
//        super.afterBreak(world, player, pos, state, blockEntity, tool);
//        if (player instanceof ServerPlayerEntity player1)
//            GrippingData.addGrippingSeconds((IEntityDataSaver) player1,10);
//    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        BlockPos blockPos = pos.up();
        BlockState blockState = ModBlocks.GLOOMWEED.getDefaultState();
        Optional<RegistryEntry.Reference<PlacedFeature>> optional = world.getRegistryManager()
                .get(RegistryKeys.PLACED_FEATURE)
                .getEntry(ModPlacements.GLOOMWEED_PATCH);

        label49:
        for (int i = 0; i < 128; i++) {
            BlockPos blockPos2 = blockPos;

            for (int j = 0; j < i / 16; j++) {
                blockPos2 = blockPos2.add(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
                if (!world.getBlockState(blockPos2.down()).isOf(this) || world.getBlockState(blockPos2).isFullCube(world, blockPos2)) {
                    continue label49;
                }
            }

            BlockState blockState2 = world.getBlockState(blockPos2);
            if (blockState2.isOf(blockState.getBlock()) && random.nextInt(10) == 0) {
                ((Fertilizable)blockState.getBlock()).grow(world, random, blockPos2, blockState2);
            }

            if (blockState2.isAir()) {
                RegistryEntry<PlacedFeature> registryEntry;
                if (random.nextInt(8) == 0) {
                    List<ConfiguredFeature<?, ?>> list = world.getBiome(blockPos2).value().getGenerationSettings().getFlowerFeatures();
                    if (list.isEmpty()) {
                        continue;
                    }

                    registryEntry = ((RandomPatchFeatureConfig)((ConfiguredFeature)list.get(0)).config()).feature();
                } else {
                    if (optional.isEmpty())
                        continue;
                    registryEntry = optional.get();
                }

                registryEntry.value().generate(world, world.getChunkManager().getChunkGenerator(), random, blockPos2);
            }
        }
    }
}