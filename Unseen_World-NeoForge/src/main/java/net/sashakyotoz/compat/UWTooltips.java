package net.sashakyotoz.compat;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.api.entity_data.IGrippingEntity;
import net.sashakyotoz.common.blocks.custom.BurlywoodHeartBlock;
import net.sashakyotoz.common.blocks.custom.EffigyBlock;
import net.sashakyotoz.common.blocks.custom.EffigyPartBlock;
import net.sashakyotoz.common.blocks.custom.OrdealSpawnerBlock;
import net.sashakyotoz.common.blocks.custom.entities.EffigyBlockEntity;
import net.sashakyotoz.common.blocks.custom.entities.OrdealSpawnerBlockEntity;
import net.sashakyotoz.common.blocks.custom.plants.HangingFruitBlock;
import net.sashakyotoz.common.blocks.custom.plants.TreeBushLikeBlock;
import net.sashakyotoz.common.blocks.custom.states.OrdealSpawnerState;
import net.sashakyotoz.common.tags.ModTags;

import java.util.Locale;
import java.util.function.Consumer;

public final class UWTooltips {
    public static final ResourceLocation EFFIGY = UnseenWorld.MOD.makeID("effigy");
    public static final ResourceLocation ORDEAL_SPAWNER = UnseenWorld.MOD.makeID("ordeal_spawner");
    public static final ResourceLocation FRUIT = UnseenWorld.MOD.makeID("fruit");
    public static final ResourceLocation BURLYWOOD_HEART = UnseenWorld.MOD.makeID("burlywood_heart");
    public static final ResourceLocation GRIPPING = UnseenWorld.MOD.makeID("gripping");
    private static final String ORDEAL_DATA = "unseen_world_ordeal";

    private UWTooltips() {
    }

    public static void effigy(Level level, BlockPos pos, BlockState state, Consumer<Component> lines) {
        BlockPos core = state.getBlock() instanceof EffigyPartBlock ? EffigyPartBlock.corePos(state, pos) : pos;
        BlockState coreState = level.getBlockState(core);
        if (!(coreState.getBlock() instanceof EffigyBlock)) {
            return;
        }
        EffigyBlock.Boss boss = coreState.getValue(EffigyBlock.BOSS);
        EffigyBlock.Phase phase = coreState.getValue(EffigyBlock.PHASE);
        lines.accept(Component.translatable("tooltip.unseen_world.effigy.boss", boss.entity().getDescription()).withStyle(ChatFormatting.GRAY));
        if (phase == EffigyBlock.Phase.DORMANT) {
            int keys = level.getBlockEntity(core) instanceof EffigyBlockEntity effigy ? effigy.getKeys() : 0;
            int seals = Math.min(EffigyBlockEntity.countSeals(level, core), boss.sealsRequired);
            lines.accept(Component.translatable("block.unseen_world.effigy.status", keys, boss.keysRequired, seals, boss.sealsRequired));
        }
        lines.accept(Component.translatable("tooltip.unseen_world.effigy.phase." + phase.getSerializedName())
                .withStyle(phase == EffigyBlock.Phase.DORMANT ? ChatFormatting.GRAY : ChatFormatting.LIGHT_PURPLE));
    }

    public static void writeOrdealData(BlockEntity blockEntity, CompoundTag target) {
        Level level = blockEntity.getLevel();
        if (!(blockEntity instanceof OrdealSpawnerBlockEntity) || level == null) {
            return;
        }
        CompoundTag saved = blockEntity.saveWithoutMetadata(level.registryAccess());
        CompoundTag data = new CompoundTag();
        if (saved.contains("primary", Tag.TAG_STRING)) {
            data.putString("primary", saved.getString("primary"));
        }
        if (saved.contains("secondary", Tag.TAG_STRING)) {
            data.putString("secondary", saved.getString("secondary"));
        }
        data.putInt("spawned", saved.getInt("spawned"));
        data.putInt("total", saved.getInt("total"));
        data.putLong("cooldown_left", Math.max(0L, saved.getLong("cooldown_ends_at") - level.getGameTime()));
        target.put(ORDEAL_DATA, data);
    }

    public static void ordealSpawner(BlockState state, CompoundTag serverData, Consumer<Component> lines) {
        if (!(state.getBlock() instanceof OrdealSpawnerBlock)) {
            return;
        }
        OrdealSpawnerState phase = state.getValue(OrdealSpawnerBlock.STATE);
        OrdealSpawnerBlock.Type type = state.getValue(OrdealSpawnerBlock.TYPE);
        lines.accept(Component.translatable("tooltip.unseen_world.ordeal_spawner.phase." + phase.name().toLowerCase(Locale.ROOT)));
        if (state.getValue(OrdealSpawnerBlock.GRIPPING)) {
            lines.accept(Component.translatable("tooltip.unseen_world.ordeal_spawner.gripping").withStyle(ChatFormatting.DARK_PURPLE));
        }
        if (!serverData.contains(ORDEAL_DATA, Tag.TAG_COMPOUND)) {
            return;
        }
        CompoundTag data = serverData.getCompound(ORDEAL_DATA);
        Component first = mobName(data, "primary", type, false);
        Component second = mobName(data, "secondary", type, true);
        int spawned = data.getInt("spawned");
        int total = data.getInt("total");
        switch (phase) {
            case ACTIVE -> {
                boolean secondWave = total > 0 && spawned >= (total + 1) / 2;
                lines.accept(Component.translatable("tooltip.unseen_world.ordeal_spawner.current_wave", secondWave ? 2 : 1, secondWave ? second : first));
                lines.accept(Component.translatable("tooltip.unseen_world.ordeal_spawner.progress", spawned, total).withStyle(ChatFormatting.GRAY));
            }
            case COOLDOWN -> lines.accept(Component.translatable("tooltip.unseen_world.ordeal_spawner.ready_in",
                    StringUtil.formatTickDuration((int) Math.min(Integer.MAX_VALUE, data.getLong("cooldown_left")), 20.0F)).withStyle(ChatFormatting.GRAY));
            case EJECTING_REWARD -> {
            }
            default -> {
                lines.accept(Component.translatable("tooltip.unseen_world.ordeal_spawner.wave", 1, first).withStyle(ChatFormatting.GRAY));
                lines.accept(Component.translatable("tooltip.unseen_world.ordeal_spawner.wave", 2, second).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    private static Component mobName(CompoundTag data, String key, OrdealSpawnerBlock.Type type, boolean secondary) {
        EntityType<?> fallback = type.defaultEntity(secondary);
        ResourceLocation id = data.contains(key, Tag.TAG_STRING) ? ResourceLocation.tryParse(data.getString(key)) : null;
        EntityType<?> entity = id == null ? fallback : BuiltInRegistries.ENTITY_TYPE.getOptional(id).orElse(fallback);
        return entity.getDescription();
    }

    public static void fruit(BlockState state, Consumer<Component> lines) {
        Boolean ripe = null;
        if (state.hasProperty(HangingFruitBlock.HAS_FRUIT)) {
            ripe = state.getValue(HangingFruitBlock.HAS_FRUIT);
        } else if (state.hasProperty(TreeBushLikeBlock.GROWN)) {
            ripe = state.getValue(TreeBushLikeBlock.GROWN);
        }
        if (ripe != null) {
            lines.accept(Component.translatable(ripe ? "tooltip.unseen_world.fruit.ripe" : "tooltip.unseen_world.fruit.growing")
                    .withStyle(ripe ? ChatFormatting.GREEN : ChatFormatting.GRAY));
        }
    }

    public static void burlywoodHeart(BlockState state, Consumer<Component> lines) {
        if (state.hasProperty(BurlywoodHeartBlock.PERSISTENT)) {
            lines.accept(Component.translatable(state.getValue(BurlywoodHeartBlock.PERSISTENT)
                    ? "tooltip.unseen_world.burlywood_heart.persistent"
                    : "tooltip.unseen_world.burlywood_heart.natural").withStyle(ChatFormatting.GRAY));
        }
    }

    public static void gripping(Entity entity, Consumer<Component> lines) {
        if (!(entity instanceof LivingEntity living) || !(entity instanceof IGrippingEntity gripping)) {
            return;
        }
        if (living.getType().is(ModTags.Entities.GRIPPING_IMMUNE_ENTITY_TYPES)) {
            lines.accept(Component.translatable("tooltip.unseen_world.gripping_immune").withStyle(ChatFormatting.GRAY));
            return;
        }
        int seconds = gripping.getGrippingData();
        if (seconds > 0) {
            lines.accept(Component.translatable("tooltip.unseen_world.gripping", seconds).withStyle(ChatFormatting.DARK_PURPLE));
        }
    }
}