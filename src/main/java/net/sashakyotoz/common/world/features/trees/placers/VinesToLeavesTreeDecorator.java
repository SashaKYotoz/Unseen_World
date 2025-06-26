package net.sashakyotoz.common.world.features.trees.placers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import net.sashakyotoz.common.world.features.trees.ModTreePlacerTypes;

public class VinesToLeavesTreeDecorator extends TreeDecorator {
    public static final Codec<VinesToLeavesTreeDecorator> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(treeDecorator -> treeDecorator.probability),
                            BlockStateProvider.TYPE_CODEC.fieldOf("block").forGetter(treeDecorator -> treeDecorator.block),
                            IntProvider.createValidatingCodec(0, 16).fieldOf("height").forGetter(placer -> placer.height)
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
    protected TreeDecoratorType<?> getType() {
        return ModTreePlacerTypes.VINES_TO_LEAVES_DECORATOR;
    }

    @Override
    public void generate(Generator generator) {
        generator.getLeavesPositions().forEach(pos -> {
            if (generator.getRandom().nextFloat() < this.probability) {
                if (generator.isAir(pos.down()) && generator.isAir(pos.down(2)))
                    placeVines(block.get(generator.getRandom(), pos.down()), pos.down(), generator);
            }
        });
    }

    private void placeVines(BlockState state, BlockPos pos, TreeDecorator.Generator generator) {
        generator.replace(pos, state);
        int i = height.get(generator.getRandom());

        for (BlockPos var4 = pos.down(); generator.isAir(var4) && i > 0; i--) {
            if (state.contains(Properties.BERRIES))
                generator.replace(var4, state.with(Properties.BERRIES, generator.getRandom().nextBoolean()));
            else
                generator.replace(var4, state);
            var4 = var4.down();
        }
    }
}