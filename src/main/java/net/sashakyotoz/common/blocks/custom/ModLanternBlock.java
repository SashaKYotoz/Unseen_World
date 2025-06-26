package net.sashakyotoz.common.blocks.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.LanternBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.api.entity_data.data.GrippingData;
import net.sashakyotoz.common.blocks.ModBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModLanternBlock extends LanternBlock {
    public ModLanternBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextInt(8) == 3) {
            List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, new Box(pos).expand(16), entity ->
                    entity instanceof IGrippingEntity entity1 && entity1.getGrippingData() > 0);
            if (!entities.isEmpty() && state.isOf(ModBlocks.GRIPTONITE_LANTERN)) {
                entities.forEach(entity -> {
                    if (entity instanceof IGrippingEntity entity1)
                        GrippingData.removeGripping(entity1);
                });
                world.spawnParticles(ParticleTypes.SPORE_BLOSSOM_AIR, pos.getX(), pos.getY() + 0.1f, pos.getZ(), 5, 0, 0, 0, 1);
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);
        if (stack.isOf(ModBlocks.GRIPTONITE_LANTERN.asItem()))
            tooltip.add(Text.translatable("item.unseen_world.griptonite_lantern_tooltip").formatted(Formatting.DARK_GRAY));
    }
}