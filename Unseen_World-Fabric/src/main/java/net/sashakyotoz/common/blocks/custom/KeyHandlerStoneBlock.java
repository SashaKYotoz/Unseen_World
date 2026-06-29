package net.sashakyotoz.common.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sashakyotoz.common.blocks.ModBlockEntities;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.custom.entities.KeyHandlerStoneBlockEntity;
import net.sashakyotoz.common.blocks.custom.entities.KeyHandlerStoneData;
import net.sashakyotoz.common.config.ModMainConfig;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.entities.bosses.EclipseSentinel;
import net.sashakyotoz.common.entities.bosses.WarriorOfChimericDarkness;
import net.sashakyotoz.common.items.ModItems;

import java.util.function.Consumer;

public class KeyHandlerStoneBlock extends BaseEntityBlock {
    public static final BooleanProperty LOCKED = BlockStateProperties.LOCKED;

    public static final EnumProperty<KeyHandlerType> HANDLER_TYPE = EnumProperty.create("handler_type", KeyHandlerType.class);

    private final VoxelShape KEY_HANDLER_STONE = Shapes.or(
            KeyHandlerStoneBlock.box(0, 0, 0, 16, 4, 16),
            KeyHandlerStoneBlock.box(1, 4, 1, 15, 6, 15),
            KeyHandlerStoneBlock.box(3, 6, 3, 13, 14, 13),
            KeyHandlerStoneBlock.box(1, 14, 1, 15, 16, 15),
            KeyHandlerStoneBlock.box(2, 16, 2, 14, 18, 14));

    public KeyHandlerStoneBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(HANDLER_TYPE, KeyHandlerType.GLACIEMITE).setValue(LOCKED, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LOCKED).add(HANDLER_TYPE);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new KeyHandlerStoneBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return KEY_HANDLER_STONE;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof KeyHandlerStoneBlockEntity entity && entity.data.cooldown() <= 0) {
            if (!state.getValue(LOCKED) && player instanceof ServerPlayer serverPlayer)
                serverPlayer.displayClientMessage(Component.translatable("gameplay.unseen_world.key_handler_cooldown", entity.data.cooldown()), true);
            Item requiredKey;
            Consumer<BlockPos> entitySpawner;
            switch (state.getValue(HANDLER_TYPE)) {
                case DARK_CURRANTSLATE -> {
                    requiredKey = ModItems.ABYSSAL_KEY;
                    entitySpawner = (targetPos) -> {
                        EclipseSentinel entityToSpawn = new EclipseSentinel(ModEntities.ECLIPSE_SENTINEL, world);
                        entityToSpawn.teleportToWithTicket(targetPos.getX(), targetPos.getY() + 0.5f, targetPos.getZ());
                        world.addFreshEntity(entityToSpawn);
                    };
                }
                case GLACIEMITE -> {
                    requiredKey = ModItems.GRIPCRYSTAL_KEY;
                    entitySpawner = (targetPos) -> {
                        WarriorOfChimericDarkness entityToSpawn = new WarriorOfChimericDarkness(ModEntities.WARRIOR_OF_CHIMERIC_DARKNESS, world);
                        entityToSpawn.teleportToWithTicket(targetPos.getX(), targetPos.getY() + 0.5f, targetPos.getZ());
                        world.addFreshEntity(entityToSpawn);
                    };
                }
                case GOLDEN -> {
                    requiredKey = ModItems.AURIC_KEY;
                    entitySpawner = (targetPos) -> {
                        WarriorOfChimericDarkness entityToSpawn = new WarriorOfChimericDarkness(ModEntities.WARRIOR_OF_CHIMERIC_DARKNESS, world);
                        entityToSpawn.teleportToWithTicket(targetPos.getX(), targetPos.getY() + 0.5f, targetPos.getZ());
                        world.addFreshEntity(entityToSpawn);
                    };
                }
                default -> {
                    return super.use(state, world, pos, player, hand, hit);
                }
            }

            if (player.getMainHandItem().is(requiredKey)) {
                player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), 20);

                entity.data = entity.data.firstKeyIn() ? new KeyHandlerStoneData(true, entity.data.firstKeyOffset(), true, -1f, entity.data.cooldown())
                        : new KeyHandlerStoneData(true, 1, entity.data.secondKeyIn(), entity.data.secondKeyOffset(), entity.data.cooldown());

                player.getMainHandItem().shrink(1);
                world.playSound(player, pos, SoundEvents.METAL_HIT, SoundSource.BLOCKS, 2.5f, 2);
            }

            if (entity.data.firstKeyIn() && entity.data.secondKeyIn() && !world.isClientSide()) {
                world.playSound(player, pos, SoundEvents.PORTAL_TRIGGER, SoundSource.BLOCKS, 2, 2);
                world.setBlockAndUpdate(pos, state.setValue(LOCKED, false));

                BlockPos targetPos = getPosOfTranslocatone(world, pos);
                world.getEntitiesOfClass(Player.class, new AABB(pos.getCenter(), pos.getCenter()),
                                player1 -> player1.isAlive() && !player1.isCreative() && !player1.isSpectator())
                        .forEach(entity1 -> entity1.teleportToWithTicket(entity1.getX(), targetPos.getY() + 1, entity1.getZ()));
                setCooldown(entity);

                if (!pos.equals(targetPos))
                    entitySpawner.accept(targetPos);
            }
        }
        return super.use(state, world, pos, player, hand, hit);
    }


    private void setCooldown(KeyHandlerStoneBlockEntity entity) {
        entity.data = new KeyHandlerStoneData(
                entity.data.firstKeyIn(),
                entity.data.firstKeyOffset(),
                entity.data.secondKeyIn(),
                entity.data.secondKeyOffset(),
                ModMainConfig.keyHandlerBlockCooldown
        );
    }

    private BlockPos getPosOfTranslocatone(Level world, BlockPos pos) {
        int radius = 3;
        int height = 31;
        for (int y = -height; y < height; y++) {
            for (int x = -radius; x < radius; x++) {
                for (int z = -radius; z < radius; z++) {
                    BlockPos pos1 = pos.offset(x, y, z);
                    if (world.getBlockState(pos1).is(ModBlocks.GLACIEMITE_TRANSLOCATONE)) {
                        world.setBlockAndUpdate(pos1, world.getBlockState(pos1).setValue(BlockStateProperties.TRIGGERED, true));
                        return pos1;
                    }
                }
            }
        }
        return pos;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.KEY_HANDLER,
                world.isClientSide() ? KeyHandlerStoneBlockEntity::clientTick : KeyHandlerStoneBlockEntity::serverTick);
    }

    public enum KeyHandlerType implements StringRepresentable {
        GLACIEMITE("glaciemite"),
        DARK_CURRANTSLATE("dark_currantslate"),
        GOLDEN("golden");
        public final String id;

        KeyHandlerType(String id) {
            this.id = id;
        }

        @Override
        public String getSerializedName() {
            return this.id;
        }
    }
}