package net.sashakyotoz.common.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.sashakyotoz.common.blocks.ModBlockEntities;
import net.sashakyotoz.common.blocks.custom.entities.OrdealSpawnerBlockEntity;
import net.sashakyotoz.common.blocks.custom.states.OrdealSpawnerState;

public class OrdealSpawnerBlock extends BaseEntityBlock {
    public static final BooleanProperty GRIPPING = BooleanProperty.create("gripping");
    public static final EnumProperty<OrdealSpawnerState> STATE = EnumProperty.create("state", OrdealSpawnerState.class);
    public static final EnumProperty<Type> TYPE = EnumProperty.create("type", Type.class);

    public OrdealSpawnerBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(GRIPPING, false)
                .setValue(STATE, OrdealSpawnerState.INACTIVE)
                .setValue(TYPE, Type.GRIPCRYSTAL));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(GRIPPING).add(STATE).add(TYPE);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OrdealSpawnerBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        return super.use(state, world, pos, player, hand, hit);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.ORDEAL_SPAWNER, OrdealSpawnerBlockEntity::tick);
    }

    public enum Type implements StringRepresentable {
        GRIPCRYSTAL("gripcrystal"),
        ABYSSAL("abyssal"),
        AURIC("auric");

        Type(String name) {
            this.name = name;
        }

        public final String name;

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}