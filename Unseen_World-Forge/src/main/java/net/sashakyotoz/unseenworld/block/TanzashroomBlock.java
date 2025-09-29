
package net.sashakyotoz.unseenworld.block;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.PlantType;
import net.sashakyotoz.unseenworld.registries.UnseenWorldBlocks;

public class TanzashroomBlock extends FlowerBlock implements BonemealableBlock {
    public TanzashroomBlock() {
        super(() -> MobEffects.DIG_SPEED, 200, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).sound(SoundType.GRASS).instabreak().hasPostProcess((bs, br, bp) -> true).emissiveRendering((bs, br, bp) -> true).lightLevel(s -> 1).noCollission()
                .offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY));
    }

    @Override
    public int getEffectDuration() {
        return 200;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 100;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 60;
    }

    @Override
    public boolean mayPlaceOn(BlockState groundState, BlockGetter worldIn, BlockPos pos) {
        return groundState.is(UnseenWorldBlocks.TANZANITE_BLOCK.get()) || groundState.is(UnseenWorldBlocks.TANZANITE_BLOCK_BUDDING.get()) || groundState.is(UnseenWorldBlocks.AMETHYST_GRASS_BLOCK.get())
                || groundState.is(UnseenWorldBlocks.DARK_GRASS_BLOCK.get()) || groundState.is(Blocks.BLACKSTONE) || groundState.is(UnseenWorldBlocks.RED_OOZE.get()) || groundState.is(Blocks.DEEPSLATE)
                || groundState.is(UnseenWorldBlocks.TEALIVE_LUMINOUS_GRASS_BLOCK.get());
    }

    @Override
    public boolean canSurvive(BlockState blockstate, LevelReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState groundState = worldIn.getBlockState(blockpos);
        return this.mayPlaceOn(groundState, worldIn, blockpos);
    }

    @Override
    public PlantType getPlantType(BlockGetter world, BlockPos pos) {
        return PlantType.CAVE;
    }

    @Override
    public InteractionResult use(BlockState blockstate, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        super.use(blockstate, level, pos, player, hand, hit);
        if (player.getMainHandItem().is(Items.WARPED_FUNGUS_ON_A_STICK)) {
            player.getMainHandItem().getOrCreateTag().putDouble("CustomModelData", 1);
            player.getMainHandItem().setHoverName(Component.translatable("block.unseen_world.tanzashroom_on_stick"));
            level.removeBlock(pos, true);
        }
        return InteractionResult.SUCCESS;
    }

    @OnlyIn(Dist.CLIENT)
    public static void blockColorLoad(RegisterColorHandlersEvent.Block event) {
        event.getBlockColors().register((bs, world, pos, index) -> world != null && pos != null ? BiomeColors.getAverageGrassColor(world, pos) : GrassColor.get(0.5D, 1.0D), UnseenWorldBlocks.TANZASHROOM.get());
    }

    @OnlyIn(Dist.CLIENT)
    public static void itemColorLoad(RegisterColorHandlersEvent.Item event) {
        event.getItemColors().register((stack, index) -> GrassColor.get(0.5D, 1.0D), UnseenWorldBlocks.TANZASHROOM.get());
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader reader, BlockPos pos, BlockState state, boolean b) {
        return reader.getBlockState(pos.above()).isAir() && reader.getBlockState(pos.above(2)).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return random.nextBoolean();
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        StructureTemplate template = level.getStructureManager().getOrCreate(new ResourceLocation("unseen_world", "tanzashroom_big_mushroom"));
        if (template != null)
            template.placeInWorld(level, BlockPos.containing(pos.getX() - 3, pos.getY(), pos.getZ() - 3), BlockPos.containing(pos.getX() - 3, pos.getY(), pos.getZ() - 3), new StructurePlaceSettings().setRotation(Rotation.NONE).setMirror(Mirror.NONE).setIgnoreEntities(false), random, 3);
    }
}
