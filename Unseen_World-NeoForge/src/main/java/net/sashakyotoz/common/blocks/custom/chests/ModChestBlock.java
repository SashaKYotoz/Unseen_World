package net.sashakyotoz.common.blocks.custom.chests;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.ChestMenu;
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
import net.minecraft.world.level.material.Fluids;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.client.gui.blocks.ModScreenHandlers;
import net.sashakyotoz.common.blocks.ModBlockEntities;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.custom.chests.entities.ModChestBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

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

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }

    public ChestTypes getType() {
        return this.type;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide && type == this.type.getBlockEntityType()
                ? (tickLevel, pos, tickState, blockEntity) -> ((ModChestBlockEntity) blockEntity).clientTick()
                : null;
    }

    public enum ChestTypes {
        DARK_CURRANTSLATE("dark_currantslate_chest", () -> ModBlocks.DARK_CURRANTSLATE_CHEST,
                () -> ModBlockEntities.DARK_CURRANTSLATE_CHEST, () -> ModScreenHandlers.DARK_CURRANTSLATE_CHEST),
        GLACIEMITE("glaciemite_chest", () -> ModBlocks.GLACIEMITE_CHEST,
                () -> ModBlockEntities.GLACIEMITE_CHEST, () -> ModScreenHandlers.GLACIEMITE_CHEST),
        CORONA_STONE("corona_stone_chest", () -> ModBlocks.CORONA_STONE_CHEST,
                () -> ModBlockEntities.CORONA_STONE_CHEST, () -> ModScreenHandlers.CORONA_STONE_CHEST);

        public final int size = 27;
        public final int rowLength = 9;
        public final ResourceLocation texture;
        private final Supplier<Block> block;
        private final Supplier<BlockEntityType<ModChestBlockEntity>> blockEntityType;
        private final Supplier<MenuType<ChestMenu>> menuType;

        ChestTypes(String id, Supplier<Block> block, Supplier<BlockEntityType<ModChestBlockEntity>> blockEntityType,
                   Supplier<MenuType<ChestMenu>> menuType) {
            this.texture = UnseenWorld.MOD.makeID("chest/" + id);
            this.block = block;
            this.blockEntityType = blockEntityType;
            this.menuType = menuType;
        }

        public int getRowCount() {
            return this.size / this.rowLength;
        }

        public static Block get(ChestTypes type) {
            return type.block.get();
        }

        public ChestBlockEntity makeEntity(BlockPos pos, BlockState state) {
            return new ModChestBlockEntity(this, pos, state);
        }

        public MenuType<ChestMenu> getScreenHandlerType() {
            return this.menuType.get();
        }

        public BlockEntityType<ModChestBlockEntity> getBlockEntityType() {
            return this.blockEntityType.get();
        }
    }
}
