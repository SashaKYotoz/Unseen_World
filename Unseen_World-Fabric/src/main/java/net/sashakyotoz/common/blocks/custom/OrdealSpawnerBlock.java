package net.sashakyotoz.common.blocks.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.sashakyotoz.common.blocks.ModBlockEntities;
import net.sashakyotoz.common.blocks.custom.entities.OrdealSpawnerBlockEntity;
import net.sashakyotoz.common.blocks.custom.states.OrdealSpawnerState;

public class OrdealSpawnerBlock extends BlockWithEntity {
    public static final BooleanProperty GRIPPING = BooleanProperty.of("gripping");
    public static final EnumProperty<OrdealSpawnerState> STATE = EnumProperty.of("state", OrdealSpawnerState.class);
    public static final EnumProperty<Type> TYPE = EnumProperty.of("type", Type.class);

    public OrdealSpawnerBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(GRIPPING, false)
                .with(STATE, OrdealSpawnerState.INACTIVE)
                .with(TYPE, Type.GRIPCRYSTAL));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(GRIPPING).add(STATE).add(TYPE);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new OrdealSpawnerBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.ORDEAL_SPAWNER, OrdealSpawnerBlockEntity::tick);
    }

    public enum Type implements StringIdentifiable {
        GRIPCRYSTAL("gripcrystal"),
        ABYSSAL("abyssal"),
        AURIC("auric");

        Type(String name) {
            this.name = name;
        }

        public final String name;

        @Override
        public String asString() {
            return this.name;
        }
    }
}