package net.sashakyotoz.common.blocks.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.api.entity_data.data.GrippingData;
import net.sashakyotoz.common.blocks.ModBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModLanternBlock extends LanternBlock {
    public ModLanternBlock(Properties settings) {
        super(settings);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (random.nextInt(8) == 3) {
            List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(16), entity ->
                    entity instanceof IGrippingEntity entity1 && entity1.getGrippingData() > 0);
            if (!entities.isEmpty() && state.is(ModBlocks.GRIPTONITE_LANTERN)) {
                entities.forEach(entity -> {
                    if (entity instanceof IGrippingEntity entity1)
                        GrippingData.removeGripping(entity1);
                });
                world.sendParticles(ParticleTypes.SPORE_BLOSSOM_AIR, pos.getX(), pos.getY() + 0.1f, pos.getZ(), 5, 0, 0, 0, 1);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag options) {
        super.appendHoverText(stack, world, tooltip, options);
        if (stack.is(ModBlocks.GRIPTONITE_LANTERN.asItem()))
            tooltip.add(Component.translatable("item.unseen_world.griptonite_lantern_tooltip").withStyle(ChatFormatting.DARK_GRAY));
    }
}