package net.sashakyotoz.common.world.features.trees.placers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.sashakyotoz.common.blocks.custom.plants.HangingFruitBlock;
import net.sashakyotoz.common.world.features.trees.ModTreePlacerTypes;

public class VinesToLeavesTreeDecorator extends TreeDecorator {
    public static final Codec<VinesToLeavesTreeDecorator> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(treeDecorator -> treeDecorator.probability),
                            BlockStateProvider.CODEC.fieldOf("block").forGetter(treeDecorator -> treeDecorator.block),
                            IntProvider.codec(0, 16).fieldOf("height").forGetter(placer -> placer.height)
                    )
                    .apply(instance, VinesToLeavesTreeDecorator::new)
    );
    protected final float probability;
    protected final BlockStateProvider block;
    protected final IntProvider height;

    public VinesToLeavesTreeDecorator(
            float probability, BlockStateProvider block, IntProvider height
    ) {
        this.probability = probability;
        this.block = block;
        this.height = height;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return ModTreePlacerTypes.VINES_TO_LEAVES_DECORATOR;
    }

    @Override
    public void place(Context generator) {
        generator.leaves().forEach(pos -> {
            if (generator.random().nextFloat() < this.probability) {
                if (generator.isAir(pos.below()) && generator.isAir(pos.below(2)))
                    placeVines(block.getState(generator.random(), pos.below()), pos.below(), generator);
            }
        });
    }

    private void placeVines(BlockState state, BlockPos pos, TreeDecorator.Context generator) {
        generator.setBlock(pos, state);
        int i = height.sample(generator.random());
        int tmpI = i;
        for (BlockPos pos1 = pos.below(); generator.isAir(pos1) && tmpI > 0; tmpI--) {
            if (state.hasProperty(BlockStateProperties.BERRIES))
                generator.setBlock(pos1, state.setValue(BlockStateProperties.BERRIES, generator.random().nextBoolean()));
            if (state.hasProperty(HangingFruitBlock.HAS_FRUIT) && !state.hasProperty(BlockStateProperties.BERRIES))
                generator.setBlock(pos1, state.setValue(HangingFruitBlock.HAS_FRUIT, false));
            else
                generator.setBlock(pos1, state);
            pos1 = pos1.below();
        }
        if (state.hasProperty(HangingFruitBlock.HAS_FRUIT) && !state.hasProperty(BlockStateProperties.BERRIES))
            generator.setBlock(pos.below(i), state.setValue(HangingFruitBlock.HAS_FRUIT, true));
    }
}