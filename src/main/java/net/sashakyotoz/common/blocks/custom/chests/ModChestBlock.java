package net.sashakyotoz.common.blocks.custom.chests;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.gui.blocks.ChestScreenHandler;
import net.sashakyotoz.client.gui.blocks.ModScreenHandlers;
import net.sashakyotoz.common.blocks.ModBlockEntities;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.custom.chests.entities.ModChestBlockEntity;

public class ModChestBlock extends ChestBlock {
    private final ChestTypes type;

    public ModChestBlock(Settings settings, ChestTypes type) {
        super(settings, type::getBlockEntityType);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH)
                .with(CHEST_TYPE, ChestType.SINGLE).with(WATERLOGGED, Boolean.FALSE));
        this.type = type;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return this.type.makeEntity(pos, state);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getHorizontalPlayerFacing().getOpposite();
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        return this.getDefaultState().with(FACING, direction).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    public ChestTypes getType() {
        return type;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient & type == this.type.getBlockEntityType() ? (world1, pos, state1, blockEntity) -> ((ModChestBlockEntity) blockEntity).clientTick() : null;
    }

    public enum ChestTypes {
        DARK_CURRANTSLATE(27, 9, UnseenWorld.makeID("chest/dark_currantslate_chest")),
        GLACIEMITE(27, 9, UnseenWorld.makeID("chest/glaciemite_chest"));

        public final int size;
        public final int rowLength;
        public final Identifier texture;

        ChestTypes(int size, int rowLength, Identifier texture) {
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
                case DARK_CURRANTSLATE -> ModBlockEntities.DARK_CURRANTSLATE_CHEST.instantiate(pos, state);
                case GLACIEMITE -> ModBlockEntities.GLACIEMITE_CHEST.instantiate(pos, state);
            };
        }

        public ScreenHandlerType<ChestScreenHandler> getScreenHandlerType() {
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