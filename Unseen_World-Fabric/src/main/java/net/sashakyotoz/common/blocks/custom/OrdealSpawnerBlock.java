package net.sashakyotoz.common.blocks.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.ModBlockEntities;
import net.sashakyotoz.common.blocks.custom.entities.OrdealSpawnerBlockEntity;
import net.sashakyotoz.common.blocks.custom.states.OrdealSpawnerState;
import net.sashakyotoz.common.entities.ModEntities;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class OrdealSpawnerBlock extends BaseEntityBlock {
    public static final BooleanProperty GRIPPING = BooleanProperty.create("gripping");
    public static final EnumProperty<OrdealSpawnerState> STATE = EnumProperty.create("state", OrdealSpawnerState.class);
    public static final EnumProperty<Type> TYPE = EnumProperty.create("type", Type.class);

    public static final MapCodec<OrdealSpawnerBlock> CODEC = simpleCodec(OrdealSpawnerBlock::new);

    public OrdealSpawnerBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(GRIPPING, false)
                .setValue(STATE, OrdealSpawnerState.INACTIVE)
                .setValue(TYPE, Type.GRIPCRYSTAL));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(GRIPPING, STATE, TYPE);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OrdealSpawnerBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(stack.getItem() instanceof SpawnEggItem egg) || !player.getAbilities().instabuild) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof OrdealSpawnerBlockEntity spawner) {
            player.displayClientMessage(spawner.configure(egg.getType(stack)), true);
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock()) && level instanceof ServerLevel server
                && level.getBlockEntity(pos) instanceof OrdealSpawnerBlockEntity spawner) {
            spawner.dismissMobs(server);
        }
        super.onRemove(state, level, pos, newState, moved);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        OrdealSpawnerState phase = state.getValue(STATE);
        if (phase == OrdealSpawnerState.INACTIVE || random.nextInt(3) != 0) {
            return;
        }
        ParticleOptions particle = switch (phase) {
            case COOLDOWN -> ParticleTypes.SMOKE;
            case EJECTING_REWARD -> ParticleTypes.END_ROD;
            default -> state.getValue(GRIPPING) ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME;
        };
        double x = pos.getX() + random.nextDouble();
        double y = pos.getY() + random.nextDouble();
        double z = pos.getZ() + random.nextDouble();
        level.addParticle(particle, x, y, z, 0.0D, phase == OrdealSpawnerState.COOLDOWN ? 0.02D : 0.0D, 0.0D);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.ORDEAL_SPAWNER,
                level.isClientSide ? OrdealSpawnerBlockEntity::clientTick : OrdealSpawnerBlockEntity::serverTick);
    }

    public enum Type implements StringRepresentable {
        GRIPCRYSTAL("gripcrystal", () -> ModEntities.ESPYER, () -> ModEntities.ELDRITCH_WATCHER),
        ABYSSAL("abyssal", () -> ModEntities.ESPYER, () -> ModEntities.ELDRITCH_WATCHER),
        AURIC("auric", () -> ModEntities.ESPYER, () -> ModEntities.ELDRITCH_WATCHER);

        public final String name;
        private final Supplier<EntityType<?>> defaultPrimary;
        private final Supplier<EntityType<?>> defaultSecondary;

        Type(String name, Supplier<EntityType<?>> defaultPrimary, Supplier<EntityType<?>> defaultSecondary) {
            this.name = name;
            this.defaultPrimary = defaultPrimary;
            this.defaultSecondary = defaultSecondary;
        }

        public EntityType<?> defaultEntity(boolean secondary) {
            return secondary ? this.defaultSecondary.get() : this.defaultPrimary.get();
        }

        public ResourceKey<LootTable> lootTable(boolean gripping) {
            return ResourceKey.create(Registries.LOOT_TABLE, UnseenWorld.MOD.makeID("chests/ordeal_spawner_" + this.name + (gripping ? "_gripping" : "")));
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
