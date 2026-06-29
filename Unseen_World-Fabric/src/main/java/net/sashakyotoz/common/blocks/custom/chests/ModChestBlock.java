package net.sashakyotoz.common.blocks.custom.chests;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.gui.blocks.ChestScreenHandler;
import net.sashakyotoz.client.gui.blocks.ModScreenHandlers;
import net.sashakyotoz.common.blocks.ModBlockEntities;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.custom.chests.entities.ModChestBlockEntity;

public class ModChestBlock extends ChestBlock {
    private final ChestTypes type;

    public ModChestBlock(Properties settings, ChestTypes type) {
        super(settings, type::getBlockEntityType);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH)
                .setValue(TYPE, ChestType.SINGLE).setValue(WATERLOGGED, Boolean.FALSE));
        this.type = type;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return this.type.makeEntity(pos, state);
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction direction = ctx.getHorizontalDirection().getOpposite();
        FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
        return this.defaultBlockState().setValue(FACING, direction).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    public ChestTypes getType() {
        return type;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return world.isClientSide & type == this.type.getBlockEntityType() ? (world1, pos, state1, blockEntity) -> ((ModChestBlockEntity) blockEntity).clientTick() : null;
    }

    public enum ChestTypes {
        DARK_CURRANTSLATE(27, 9, UnseenWorld.makeID("chest/dark_currantslate_chest")),
        GLACIEMITE(27, 9, UnseenWorld.makeID("chest/glaciemite_chest"));

        public final int size;
        public final int rowLength;
        public final ResourceLocation texture;

        ChestTypes(int size, int rowLength, ResourceLocation texture) {
            this.size = size;
            this.rowLength = rowLength;
            this.texture = texture;
        }

        public int getRowCount() {
            return this.size / this.rowLength;
        }

        public static Block get(ChestTypes type) {
            return switch (type) {
                case DARK_CURRANTSLATE -> ModBlocks.DARK_CURRANTSLATE_CHEST;
                case GLACIEMITE -> ModBlocks.GLACIEMITE_CHEST;
            };
        }

        public ChestBlockEntity makeEntity(BlockPos pos, BlockState state) {
            return switch (this) {
                case DARK_CURRANTSLATE -> ModBlockEntities.DARK_CURRANTSLATE_CHEST.create(pos, state);
                case GLACIEMITE -> ModBlockEntities.GLACIEMITE_CHEST.create(pos, state);
            };
        }

        public MenuType<ChestScreenHandler> getScreenHandlerType() {
            return switch (this) {
                case DARK_CURRANTSLATE -> ModScreenHandlers.DARK_CURRANTSLATE_CHEST;
                case GLACIEMITE -> ModScreenHandlers.GLACIEMITE_CHEST;
            };
        }

        public BlockEntityType<? extends ChestBlockEntity> getBlockEntityType() {
            return switch (this) {
                case DARK_CURRANTSLATE -> ModBlockEntities.DARK_CURRANTSLATE_CHEST;
                case GLACIEMITE -> ModBlockEntities.GLACIEMITE_CHEST;
            };
        }
    }
}