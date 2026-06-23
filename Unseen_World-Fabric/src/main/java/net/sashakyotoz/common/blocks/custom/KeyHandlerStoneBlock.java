package net.sashakyotoz.common.blocks.custom;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.sashakyotoz.common.blocks.ModBlockEntities;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.custom.entities.KeyHandlerStoneBlockEntity;
import net.sashakyotoz.common.blocks.custom.entities.KeyHandlerStoneData;
import net.sashakyotoz.common.config.ConfigEntries;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.entities.bosses.EclipseSentinel;
import net.sashakyotoz.common.entities.bosses.WarriorOfChimericDarkness;
import net.sashakyotoz.common.items.ModItems;

import java.util.function.Consumer;

public class KeyHandlerStoneBlock extends BlockWithEntity {
    public static final BooleanProperty LOCKED = Properties.LOCKED;

    public static final EnumProperty<KeyHandlerType> HANDLER_TYPE = EnumProperty.of("handler_type", KeyHandlerType.class);

    private final VoxelShape KEY_HANDLER_STONE = VoxelShapes.union(
            KeyHandlerStoneBlock.createCuboidShape(0, 0, 0, 16, 4, 16),
            KeyHandlerStoneBlock.createCuboidShape(1, 4, 1, 15, 6, 15),
            KeyHandlerStoneBlock.createCuboidShape(3, 6, 3, 13, 14, 13),
            KeyHandlerStoneBlock.createCuboidShape(1, 14, 1, 15, 16, 15),
            KeyHandlerStoneBlock.createCuboidShape(2, 16, 2, 14, 18, 14));

    public KeyHandlerStoneBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(HANDLER_TYPE, KeyHandlerType.GLACIEMITE).with(LOCKED, true));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LOCKED).add(HANDLER_TYPE);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new KeyHandlerStoneBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return KEY_HANDLER_STONE;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof KeyHandlerStoneBlockEntity entity && entity.data.cooldown() <= 0) {
            if (!state.get(LOCKED) && player instanceof ServerPlayerEntity serverPlayer)
                serverPlayer.sendMessage(Text.translatable("gameplay.unseen_world.key_handler_cooldown", entity.data.cooldown()), true);
            Item requiredKey;
            Consumer<BlockPos> entitySpawner;
            switch (state.get(HANDLER_TYPE)) {
                case DARK_CURRANTSLATE -> {
                    requiredKey = ModItems.ABYSSAL_KEY;
                    entitySpawner = (targetPos) -> {
                        EclipseSentinel entityToSpawn = new EclipseSentinel(ModEntities.ECLIPSE_SENTINEL, world);
                        entityToSpawn.teleport(targetPos.getX(), targetPos.getY() + 0.5f, targetPos.getZ());
                        world.spawnEntity(entityToSpawn);
                    };
                }
                case GLACIEMITE -> {
                    requiredKey = ModItems.GRIPCRYSTAL_KEY;
                    entitySpawner = (targetPos) -> {
                        WarriorOfChimericDarkness entityToSpawn = new WarriorOfChimericDarkness(ModEntities.WARRIOR_OF_CHIMERIC_DARKNESS, world);
                        entityToSpawn.teleport(targetPos.getX(), targetPos.getY() + 0.5f, targetPos.getZ());
                        world.spawnEntity(entityToSpawn);
                    };
                }
                case GOLDEN -> {
                    requiredKey = ModItems.AURIC_KEY;
                    entitySpawner = (targetPos) -> {
                        WarriorOfChimericDarkness entityToSpawn = new WarriorOfChimericDarkness(ModEntities.WARRIOR_OF_CHIMERIC_DARKNESS, world);
                        entityToSpawn.teleport(targetPos.getX(), targetPos.getY() + 0.5f, targetPos.getZ());
                        world.spawnEntity(entityToSpawn);
                    };
                }
                default -> {
                    return super.onUse(state, world, pos, player, hand, hit);
                }
            }

            if (player.getMainHandStack().isOf(requiredKey)) {
                player.getItemCooldownManager().set(player.getMainHandStack().getItem(), 20);

                entity.data = entity.data.firstKeyIn() ? new KeyHandlerStoneData(true, entity.data.firstKeyOffset(), true, -1f, entity.data.cooldown())
                        : new KeyHandlerStoneData(true, 1, entity.data.secondKeyIn(), entity.data.secondKeyOffset(), entity.data.cooldown());

                player.getMainHandStack().decrement(1);
                world.playSound(player, pos, SoundEvents.BLOCK_METAL_HIT, SoundCategory.BLOCKS, 2.5f, 2);
            }

            if (entity.data.firstKeyIn() && entity.data.secondKeyIn() && !world.isClient()) {
                world.playSound(player, pos, SoundEvents.BLOCK_PORTAL_TRIGGER, SoundCategory.BLOCKS, 2, 2);
                world.setBlockState(pos, state.with(LOCKED, false));

                BlockPos targetPos = getPosOfTranslocatone(world, pos);
                world.getEntitiesByClass(PlayerEntity.class, new Box(pos.toCenterPos(), pos.toCenterPos()),
                                player1 -> player1.isAlive() && !player1.isCreative() && !player1.isSpectator())
                        .forEach(entity1 -> entity1.teleport(entity1.getX(), targetPos.getY() + 1, entity1.getZ()));
                setCooldown(entity);

                if (!pos.equals(targetPos))
                    entitySpawner.accept(targetPos);
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }


    private void setCooldown(KeyHandlerStoneBlockEntity entity) {
        entity.data = new KeyHandlerStoneData(
                entity.data.firstKeyIn(),
                entity.data.firstKeyOffset(),
                entity.data.secondKeyIn(),
                entity.data.secondKeyOffset(),
                ConfigEntries.keyHandlerBlockCooldown
        );
    }

    private BlockPos getPosOfTranslocatone(World world, BlockPos pos) {
        int radius = 3;
        int height = 31;
        for (int y = -height; y < height; y++) {
            for (int x = -radius; x < radius; x++) {
                for (int z = -radius; z < radius; z++) {
                    BlockPos pos1 = pos.add(x, y, z);
                    if (world.getBlockState(pos1).isOf(ModBlocks.GLACIEMITE_TRANSLOCATONE)) {
                        world.setBlockState(pos1, world.getBlockState(pos1).with(Properties.TRIGGERED, true));
                        return pos1;
                    }
                }
            }
        }
        return pos;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.KEY_HANDLER,
                world.isClient() ? KeyHandlerStoneBlockEntity::clientTick : KeyHandlerStoneBlockEntity::serverTick);
    }

    public enum KeyHandlerType implements StringIdentifiable {
        GLACIEMITE("glaciemite"),
        DARK_CURRANTSLATE("dark_currantslate"),
        GOLDEN("golden");
        public final String id;

        KeyHandlerType(String id) {
            this.id = id;
        }

        @Override
        public String asString() {
            return this.id;
        }
    }
}