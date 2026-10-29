package net.sashakyotoz;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.logger.SolLogger;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.sashakyotoz.api.entity_data.data.GrippingData;
import net.sashakyotoz.api.multipart_entity.EntityPart;
import net.sashakyotoz.api.multipart_entity.MultipartEntity;
import net.sashakyotoz.api.multipart_entity.WorldMultipartHelper;
import net.sashakyotoz.client.environment.weather.ChimericWeatherState;
import net.sashakyotoz.client.gui.blocks.ModScreenHandlers;
import net.sashakyotoz.client.particles.ModParticleTypes;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.ModSoundEvents;
import net.sashakyotoz.common.blocks.ModBlockEntities;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.ModFluids;
import net.sashakyotoz.common.blocks.custom.SusBlock;
import net.sashakyotoz.common.config.ModMainConfig;
import net.sashakyotoz.common.datagen.UnseenWorldDataGenerator;
import net.sashakyotoz.common.effects.ModMobEffects;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.entities.bosses.AuricPatriarch;
import net.sashakyotoz.common.entities.bosses.EclipseSentinel;
import net.sashakyotoz.common.entities.bosses.WarriorOfChimericDarkness;
import net.sashakyotoz.common.items.ModItemGroups;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.networking.ModMessages;
import net.sashakyotoz.common.registry.ModRegistrar;
import net.sashakyotoz.common.registry.OrderedSolModContainer;
import net.sashakyotoz.common.world.ModDimensions;
import net.sashakyotoz.common.world.ModSpawnRules;
import net.sashakyotoz.common.world.ModWorldFeatures;
import net.sashakyotoz.common.world.features.trees.ModTreePlacerTypes;
import net.sashakyotoz.common.world.portal.ChimericPortals;

import java.util.List;

@Mod(UnseenWorld.MOD_ID)
public class UnseenWorld {
    public static final SolModContainer MOD = new OrderedSolModContainer("Unseen World", "unseen_world");
    public static final String MOD_ID = "unseen_world";

    private static boolean contentBootstrapped;

    public UnseenWorld(IEventBus modBus, ModContainer container) {
        MOD.createConfig("unseen_world", 1, ModMainConfig::build);

        SolRegistries.WEATHER.register("grippfall",
                (source, duration) -> {
                    ChimericWeatherState.get(weatherLevel(source)).startGrippfall(duration);
                    source.sendSuccess(() -> Component.translatable("commands.unseen_world.weather.set.grippfall"), true);
                },
                (source, duration) -> ChimericWeatherState.get(weatherLevel(source)).clearGrippfall(duration));

        modBus.addListener(EventPriority.HIGHEST, (RegisterEvent event) -> {
            bootstrapContent();
            ModRegistrar.onRegister(event);
        });
        modBus.addListener((RegisterPayloadHandlersEvent event) -> ModMessages.register(event));
        modBus.addListener((BuildCreativeModeTabContentsEvent event) -> ModItemGroups.addSigns(event));
        modBus.addListener((RegisterSpawnPlacementsEvent event) -> ModSpawnRules.register(event));
        modBus.addListener(UnseenWorld::addBlockEntityBlocks);
        modBus.addListener((GatherDataEvent event) -> UnseenWorldDataGenerator.gatherData(event));

        IEventBus gameBus = NeoForge.EVENT_BUS;
        gameBus.addListener(UnseenWorld::registerCommands);
        gameBus.addListener(UnseenWorld::onEntityJoin);
        gameBus.addListener(UnseenWorld::onEntityLeave);
        gameBus.addListener((PlayerInteractEvent.RightClickBlock event) -> ChimericPortals.onRightClickBlock(event));
        gameBus.addListener((PlayerEvent.PlayerChangedDimensionEvent event) -> syncWeather(event.getEntity()));
        gameBus.addListener((PlayerEvent.PlayerRespawnEvent event) -> syncWeather(event.getEntity()));
        gameBus.addListener((PlayerEvent.PlayerLoggedInEvent event) -> syncWeather(event.getEntity()));
    }

    public static boolean isContentBootstrapped() {
        return contentBootstrapped;
    }

    public static synchronized void bootstrapContent() {
        if (contentBootstrapped) {
            return;
        }
        contentBootstrapped = true;
        ModRegistry.register();
        ModFluids.register();
        ModItems.register();
        ModItemGroups.register();
        ModBlocks.register();
        ModBlockEntities.register();
        ModSoundEvents.register();

        ModRegistrar.register(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, MOD.makeID("warrior_state"), WarriorOfChimericDarkness.WARRIOR_STATE);
        ModRegistrar.register(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, MOD.makeID("sentinel_state"), EclipseSentinel.SENTINEL_STATE);
        ModRegistrar.register(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, MOD.makeID("patriarch_state"), AuricPatriarch.PATRIARCH_STATE);

        ModEntities.register();
        ModMobEffects.register();
        ModWorldFeatures.register();
        ModParticleTypes.register();
        ModScreenHandlers.register();
        ModTreePlacerTypes.register();
        ChimericPortals.register();
    }

    private static void addBlockEntityBlocks(BlockEntityTypeAddBlocksEvent event) {
        for (Block block : BuiltInRegistries.BLOCK) {
            if (!BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(MOD_ID)) {
                continue;
            }
            if (block instanceof SusBlock) {
                event.modify(BlockEntityType.BRUSHABLE_BLOCK, block);
            } else if (block instanceof StandingSignBlock || block instanceof WallSignBlock) {
                event.modify(BlockEntityType.SIGN, block);
            } else if (block instanceof CeilingHangingSignBlock || block instanceof WallHangingSignBlock) {
                event.modify(BlockEntityType.HANGING_SIGN, block);
            }
        }
    }

    private static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("set_gripping")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("amount", IntegerArgumentType.integer(0, 98))
                        .executes(context -> {
                            Entity entity = context.getSource().getEntity();
                            if (entity == null) {
                                context.getSource().sendFailure(Component.literal("set_gripping needs an entity as its source or a target selector"));
                                return 0;
                            }
                            return GrippingData.applyGripping(context.getSource(), List.of(entity), IntegerArgumentType.getInteger(context, "amount"));
                        }))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .then(Commands.argument("amount", IntegerArgumentType.integer(0, 98))
                                .executes(context -> GrippingData.applyGripping(
                                        context.getSource(),
                                        EntityArgument.getEntities(context, "targets"),
                                        IntegerArgumentType.getInteger(context, "amount"))))));
    }

    private static void onEntityJoin(EntityJoinLevelEvent event) {
        trackParts(event.getEntity(), event.getLevel(), true);
    }

    private static void onEntityLeave(EntityLeaveLevelEvent event) {
        trackParts(event.getEntity(), event.getLevel(), false);
    }

    public static void trackParts(Entity entity, Level level, boolean add) {
        if (entity instanceof MultipartEntity multipartEntity && level instanceof WorldMultipartHelper helper) {
            Int2ObjectMap<EntityPart> partMap = helper.unseen_world$getPMEPartMap();
            for (EntityPart part : multipartEntity.getMultipartParts()) {
                if (add) {
                    partMap.put(part.getId(), part);
                } else {
                    partMap.remove(part.getId());
                }
            }
        }
    }

    private static void syncWeather(net.minecraft.world.entity.player.Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            ChimericWeatherState.get(serverPlayer.serverLevel()).syncTo(serverPlayer);
        }
    }

    private static ServerLevel weatherLevel(CommandSourceStack source) {
        ServerLevel level = source.getServer().getLevel(ModDimensions.CHIMERIC_DARKNESS_LEVEL_KEY);
        return level == null ? source.getLevel() : level;
    }

    public static SolLogger log() {
        return MOD.getLogger();
    }
}
