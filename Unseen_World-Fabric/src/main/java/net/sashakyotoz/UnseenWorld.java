package net.sashakyotoz;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.sashakyotoz.api.entity_data.IEntityDataSaver;
import net.sashakyotoz.api.entity_data.data.GripcrystalManaData;
import net.sashakyotoz.api.multipart_entity.EntityPart;
import net.sashakyotoz.api.multipart_entity.MultipartEntity;
import net.sashakyotoz.api.multipart_entity.WorldMultipartHelper;
import net.sashakyotoz.client.gui.blocks.ModScreenHandlers;
import net.sashakyotoz.client.particles.ModParticleTypes;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.ModSoundEvents;
import net.sashakyotoz.common.UnseenWorldDataGenerator;
import net.sashakyotoz.common.blocks.ModBlockEntities;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.blocks.ModFluids;
import net.sashakyotoz.common.config.ConfigController;
import net.sashakyotoz.common.config.ConfigEntries;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.entities.bosses.EclipseSentinel;
import net.sashakyotoz.common.entities.bosses.WarriorOfChimericDarkness;
import net.sashakyotoz.common.items.ModItemGroups;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.networking.ModMessages;
import net.sashakyotoz.common.world.ModWorldFeatures;
import net.sashakyotoz.common.world.ModWorldGeneration;
import net.sashakyotoz.common.world.features.trees.ModTreePlacerTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnseenWorld implements ModInitializer {
    public static final String MOD_ID = "unseen_world";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        CustomPortalBuilder.beginPortal()
                .frameBlock(ModBlocks.DARK_FROST_BRICKS)
                .lightWithItem(ModItems.ECLIPSE_KEYSTONE)
                .setPortalSearchYRange(52, 184)
                .setReturnPortalSearchYRange(52, 184)
                .destDimID(makeID("chimeric_darkness"))
                .tintColor(0x000)
                .registerPortal();
        ConfigController.loadConfig();
        ModFluids.register();
        ModItems.register();
        ModItemGroups.register();
        ModBlocks.register();
        ModBlockEntities.register();

        EntityDataSerializers.registerSerializer(WarriorOfChimericDarkness.WARRIOR_POSE);
        EntityDataSerializers.registerSerializer(EclipseSentinel.SENTINEL_POSE);

        ModSoundEvents.registerSounds();
        ModEntities.register();
        ModWorldFeatures.register();
        ModParticleTypes.register();
        ModMessages.registerC2SPackets();
        ModScreenHandlers.register();

        UnseenWorldDataGenerator.registerBurnable();
        UnseenWorldDataGenerator.registerFuels();
        UnseenWorldDataGenerator.registerStripped();

        ModTreePlacerTypes.register();
        ModWorldGeneration.register();
        ServerWorldEvents.LOAD.register(new ConfigController());

        PotionBrewing.addMix(Potions.AWKWARD, ModItems.GLOW_APPLE, ModRegistry.GLOWING);
        PotionBrewing.addMix(Potions.AWKWARD, ModItems.WARPEDVEIL_VINE_FRUIT, ModRegistry.GLOWING);

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            if (environment.includeIntegrated) {
                dispatcher.register(Commands.literal("set_gripcrystal_mana")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands
                                .argument("amount", IntegerArgumentType.integer(0, 48))
                                .executes(context -> {
                                    if (context.getSource().getEntity() instanceof ServerPlayer player) {
                                        GripcrystalManaData.addMana((IEntityDataSaver) player, Math.max(0, IntegerArgumentType.getInteger(context, "amount")));
                                        return 1;
                                    } else {
                                        context.getSource()
                                                .sendSuccess(
                                                        () -> Component.literal("Called /set_gripcrystal_mana with no arguments."), false);
                                        return 0;
                                    }
                                })
                        ));
            }
        });
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof MultipartEntity multipartEntity) {
                Int2ObjectMap<EntityPart> partMap = ((WorldMultipartHelper) world).getPMEPartMap();
                for (EntityPart part : multipartEntity.getParts()) partMap.put(part.getId(), part);
            }
        });
        ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
            if (entity instanceof MultipartEntity multipartEntity) {
                Int2ObjectMap<EntityPart> partMap = ((WorldMultipartHelper) world).getPMEPartMap();
                for (EntityPart part : multipartEntity.getParts()) partMap.remove(part.getId());
            }
        });
        ServerTickEvents.END_WORLD_TICK.register(world -> world.players().stream()
                .filter(player -> !player.isSpectator()
                && GripcrystalManaData.getOpacity(((IEntityDataSaver) player)) > 0)
                .forEach(player -> GripcrystalManaData.removeOpacity((IEntityDataSaver) player, 0.02f)));
    }

    public static ResourceLocation makeID(String id) {
        return new ResourceLocation(MOD_ID, id);
    }

    public static <T> T log(T message) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment() || !ConfigEntries.doLoggingOnlyInDev)
            LOGGER.info(String.valueOf(message));
        return message;
    }
}