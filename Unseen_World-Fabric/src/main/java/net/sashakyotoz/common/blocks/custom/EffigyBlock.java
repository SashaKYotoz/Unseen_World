package net.sashakyotoz.common.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.ModBlockEntities;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.custom.entities.EffigyBlockEntity;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.items.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class EffigyBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Boss> BOSS = EnumProperty.create("boss", Boss.class);
    public static final EnumProperty<Phase> PHASE = EnumProperty.create("phase", Phase.class);
    public static final int STATUE_LAYERS = 5;
    public static final int SEAL_DISTANCE = 2;
    private static final int CELLS = 2 * 3 * (STATUE_LAYERS + 1) * 3;
    private static final VoxelShape PEDESTAL = Shapes.or(
            Block.box(-16, 0, -16, 32, 4, 32),
            Block.box(-14, 4, -14, 30, 6, 30),
            Block.box(-10, 6, -10, 26, 12, 26),
            Block.box(-13, 12, -13, 29, 16, 29));
    private static final Map<Boss, VoxelShape[]> SLICES = new ConcurrentHashMap<>();

    public EffigyBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(BOSS, Boss.WARRIOR)
                .setValue(PHASE, Phase.DORMANT));
    }

    public static VoxelShape slice(BlockState core, int dx, int dy, int dz) {
        if (dx < -1 || dx > 1 || dz < -1 || dz > 1 || dy < 0 || dy > STATUE_LAYERS) {
            return Shapes.empty();
        }
        boolean awakened = core.getValue(PHASE) == Phase.AWAKENED;
        return SLICES.computeIfAbsent(core.getValue(BOSS), EffigyBlock::buildSlices)[index(awakened, dx, dy, dz)];
    }

    private static int index(boolean awakened, int dx, int dy, int dz) {
        return (((awakened ? 1 : 0) * 3 + dx + 1) * (STATUE_LAYERS + 1) + dy) * 3 + dz + 1;
    }

    private static VoxelShape[] buildSlices(Boss boss) {
        VoxelShape[] slices = new VoxelShape[CELLS];
        VoxelShape withStatue = Shapes.or(PEDESTAL, boss.statueHull());
        for (int awake = 0; awake < 2; awake++) {
            VoxelShape full = awake == 1 ? PEDESTAL : withStatue;
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = 0; dy <= STATUE_LAYERS; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        VoxelShape cell = Shapes.box(dx, dy, dz, dx + 1, dy + 1, dz + 1);
                        slices[index(awake == 1, dx, dy, dz)] = Shapes.join(full, cell, BooleanOp.AND).move(-dx, -dy, -dz).optimize();
                    }
                }
            }
        }
        return slices;
    }

    public static boolean needsPart(Boss boss, int dx, int dy, int dz) {
        if (dx == 0 && dy == 0 && dz == 0) {
            return false;
        }
        if (dy == 0) {
            return true;
        }
        return !SLICES.computeIfAbsent(boss, EffigyBlock::buildSlices)[index(false, dx, dy, dz)].isEmpty();
    }

    public static void ensureParts(Level level, BlockPos core, BlockState coreState) {
        Boss boss = coreState.getValue(BOSS);
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = 0; dy <= STATUE_LAYERS; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) {
                        continue;
                    }
                    BlockPos target = core.offset(dx, dy, dz);
                    BlockState present = level.getBlockState(target);
                    boolean ours = EffigyPartBlock.belongsTo(present, target, core);
                    boolean wanted = needsPart(boss, dx, dy, dz);
                    if (wanted && !ours && present.canBeReplaced()) {
                        level.setBlock(target, EffigyPartBlock.forOffset(dx, dy, dz), Block.UPDATE_ALL);
                    } else if (!wanted && ours) {
                        level.setBlock(target, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                    }
                }
            }
        }
    }

    private static void removeParts(Level level, BlockPos core) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = 0; dy <= STATUE_LAYERS; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos target = core.offset(dx, dy, dz);
                    if (EffigyPartBlock.belongsTo(level.getBlockState(target), target, core)) {
                        level.setBlock(target, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                    }
                }
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, BOSS, PHASE);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
        Level level = context.getLevel();
        BlockPos core = context.getClickedPos();
        Boss boss = state.getValue(BOSS);
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = 0; dy <= STATUE_LAYERS; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (needsPart(boss, dx, dy, dz)) {
                        BlockPos target = core.offset(dx, dy, dz);
                        if (level.isOutsideBuildHeight(target) || !level.getBlockState(target).canBeReplaced(context)) {
                            return null;
                        }
                    }
                }
            }
        }
        return state;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moved) {
        if (!level.isClientSide && (!oldState.is(this) || oldState.getValue(BOSS) != state.getValue(BOSS))) {
            level.scheduleTick(pos, this, 1);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        ensureParts(level, pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            removeParts(level, pos);
        }
        super.onRemove(state, level, pos, newState, moved);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EffigyBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return slice(state, 0, 0, 0);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof EffigyBlockEntity effigy)) {
            return InteractionResult.PASS;
        }
        Boss boss = state.getValue(BOSS);
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(boss.key())) {
            if (!level.isClientSide) {
                effigy.insertKey(player, stack);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        if (hand == InteractionHand.MAIN_HAND && player.getOffhandItem().is(boss.key())) {
            return InteractionResult.PASS;
        }
        if (Boss.isAnyKey(stack)) {
            if (!level.isClientSide) {
                player.displayClientMessage(Component.translatable("block.unseen_world.effigy.wrong_key", boss.key().getDescription()), true);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        if (hand != InteractionHand.MAIN_HAND || !stack.isEmpty()) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            if (player.isSecondaryUseActive()) {
                effigy.retrieveKey(player);
            } else {
                player.displayClientMessage(effigy.status(), true);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return switch (state.getValue(PHASE)) {
            case AWAKENED -> 15;
            case AWAKENING -> 10;
            case DORMANT -> level.getBlockEntity(pos) instanceof EffigyBlockEntity effigy ? effigy.getKeys() * 3 : 0;
        };
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        Phase phase = state.getValue(PHASE);
        if (phase == Phase.AWAKENED || random.nextInt(phase == Phase.AWAKENING ? 1 : 6) != 0) {
            return;
        }
        double x = pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 2.4D;
        double y = pos.getY() + 1.0D + random.nextDouble() * 3.5D;
        double z = pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 2.4D;
        level.addParticle(phase == Phase.AWAKENING ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.WHITE_ASH, x, y, z, 0.0D, 0.02D, 0.0D);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, ModBlockEntities.EFFIGY, EffigyBlockEntity::serverTick);
    }

    public enum Phase implements StringRepresentable {
        DORMANT("dormant", 3),
        AWAKENING("awakening", 12),
        AWAKENED("awakened", 6);

        public final String id;
        public final int luminance;

        Phase(String id, int luminance) {
            this.id = id;
            this.luminance = luminance;
        }

        @Override
        public String getSerializedName() {
            return this.id;
        }
    }

    public enum Boss implements StringRepresentable {
        WARRIOR("warrior", () -> ModEntities.WARRIOR_OF_CHIMERIC_DARKNESS, () -> ModItems.GRIPCRYSTAL_KEY, () -> ModBlocks.GLACIEMITE_BRICKS, 2, 2, 0.0F, 0.0F),
        SENTINEL("sentinel", () -> ModEntities.ECLIPSE_SENTINEL, () -> ModItems.ABYSSAL_KEY, () -> ModBlocks.CORONA_STONE_BRICKS, 2, 3, 0.0F, 0.0F),
        PATRIARCH("patriarch", () -> ModEntities.AURIC_PATRIARCH, () -> ModItems.AURIC_KEY, () -> ModBlocks.GILDED_CURRANTSLATE, 2, 4, 0.0F, 0.0F);

        public final String id;
        private final Supplier<EntityType<?>> entity;
        private final Supplier<Item> key;
        private final Supplier<Block> material;
        public final int keysRequired;
        public final int sealsRequired;
        private final float statueWidth;
        private final float statueHeight;

        Boss(String id, Supplier<EntityType<?>> entity, Supplier<Item> key, Supplier<Block> material,
             int keysRequired, int sealsRequired, float statueWidth, float statueHeight) {
            this.id = id;
            this.entity = entity;
            this.key = key;
            this.material = material;
            this.keysRequired = keysRequired;
            this.sealsRequired = sealsRequired;
            this.statueWidth = statueWidth;
            this.statueHeight = statueHeight;
        }

        public EntityType<?> entity() {
            return this.entity.get();
        }

        public Item key() {
            return this.key.get();
        }

        public Block material() {
            return this.material.get();
        }

        public ResourceLocation statueTexture() {
            return UnseenWorld.MOD.makeID("textures/entity/statue/" + this.id + ".png");
        }

        public ResourceLocation pedestalTexture() {
            return UnseenWorld.MOD.makeID("textures/entity/effigy/" + this.id + ".png");
        }

        public VoxelShape statueHull() {
            EntityDimensions dimensions = this.entity().getDimensions();
            float width = Mth.clamp(this.statueWidth > 0.0F ? this.statueWidth : dimensions.width, 0.5F, 3.0F);
            float height = Mth.clamp(this.statueHeight > 0.0F ? this.statueHeight : dimensions.height, 1.0F, STATUE_LAYERS);
            double half = width / 2.0D;
            return Shapes.box(0.5D - half, 1.0D, 0.5D - half, 0.5D + half, 1.0D + height, 0.5D + half);
        }

        public static boolean isAnyKey(ItemStack stack) {
            return Arrays.stream(values()).anyMatch(boss -> stack.is(boss.key()));
        }

        @Override
        public String getSerializedName() {
            return this.id;
        }
    }
}
