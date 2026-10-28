package net.sashakyotoz;

import net.sashakyotoz.common.networking.ModMessages;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.lcc.sollib.api.common.SolRegistries;
import net.lcc.sollib.api.common.logger.SolLogger;
import net.lcc.sollib.api.common.registry.SolModContainer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
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
import net.sashakyotoz.common.config.ModMainConfig;
import net.sashakyotoz.common.effects.ModMobEffects;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.entities.bosses.AuricPatriarch;
import net.sashakyotoz.common.entities.bosses.EclipseSentinel;
import net.sashakyotoz.common.entities.bosses.WarriorOfChimericDarkness;
import net.sashakyotoz.common.items.ModItemGroups;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.world.ModDimensions;
import net.sashakyotoz.common.world.ModSpawnRules;
import net.sashakyotoz.common.world.ModWorldFeatures;
import net.sashakyotoz.common.world.features.trees.ModTreePlacerTypes;

import java.util.List;

public class UnseenWorld implements ModInitializer {
    public static final SolModContainer MOD = new SolModContainer("Unseen World", "unseen_world");
    public static final String MOD_ID = MOD.getNamespace();

    @Override
    public void onInitialize() {
        ModRegistry.register();
        ModMessages.register();
        CustomPortalBuilder.beginPortal()
                .frameBlock(ModBlocks.DARK_FROST_BRICKS)
                .lightWithItem(ModItems.ECLIPSE_KEYSTONE)
                .setPortalSearchYRange(52, 184)
                .setReturnPortalSearchYRange(52, 184)
                .destDimID(MOD.makeID("chimeric_darkness"))
                .tintColor(0x000)
                .registerPortal();
        MOD.createConfig("unseen_world", 1, ModMainConfig::build);
        ModFluids.register();
        ModItems.register();
        ModItemGroups.register();
        ModBlocks.register();
        ModBlockEntities.register();
        ModSoundEvents.register();

        EntityDataSerializers.registerSerializer(WarriorOfChimericDarkness.WARRIOR_STATE);
        EntityDataSerializers.registerSerializer(EclipseSentinel.SENTINEL_STATE);
        EntityDataSerializers.registerSerializer(AuricPatriarch.PATRIARCH_STATE);

        ModEntities.register();
        ModMobEffects.register();
        ModWorldFeatures.register();
        ModParticleTypes.register();
        ModScreenHandlers.register();

        ModTreePlacerTypes.register();
        ModSpawnRules.register();

        SolRegistries.WEATHER.register("grippfall",
                (source, duration) -> {
                    ChimericWeatherState.get(weatherLevel(source)).startGrippfall(duration);
                    source.sendSuccess(() -> Component.translatable("commands.unseen_world.weather.set.grippfall"), true);
                },
                (source, duration) -> ChimericWeatherState.get(weatherLevel(source)).clearGrippfall(duration));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(Commands.literal("set_gripping")
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
                                                IntegerArgumentType.getInteger(context, "amount")))))));

        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof MultipartEntity multipartEntity) {
                Int2ObjectMap<EntityPart> partMap = ((WorldMultipartHelper) world).unseen_World_Fabric$getPMEPartMap();
                for (EntityPart part : multipartEntity.getParts()) partMap.put(part.getId(), part);
            }
        });
        ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
            if (entity instanceof MultipartEntity multipartEntity) {
                Int2ObjectMap<EntityPart> partMap = ((WorldMultipartHelper) world).unseen_World_Fabric$getPMEPartMap();
                for (EntityPart part : multipartEntity.getParts()) partMap.remove(part.getId());
            }
        });

        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> ChimericWeatherState.get(destination).syncTo(player));
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> ChimericWeatherState.get(newPlayer.serverLevel()).syncTo(newPlayer));
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> ChimericWeatherState.get(handler.player.serverLevel()).syncTo(handler.player));
    }

    private static ServerLevel weatherLevel(CommandSourceStack source) {
        ServerLevel level = source.getServer().getLevel(ModDimensions.CHIMERIC_DARKNESS_LEVEL_KEY);
        return level == null ? source.getLevel() : level;
    }

    public static SolLogger log() {
        return MOD.getLogger();
    }
}