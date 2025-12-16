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
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
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
import net.sashakyotoz.common.entities.bosses.EclipseSentinelEntity;
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

        TrackedDataHandlerRegistry.register(WarriorOfChimericDarkness.WARRIOR_POSE);
        TrackedDataHandlerRegistry.register(EclipseSentinelEntity.SENTINEL_POSE);
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

        BrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, ModItems.GLOW_APPLE, ModRegistry.GLOWING);
        BrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, ModItems.WARPEDVEIL_VINE_FRUIT, ModRegistry.GLOWING);

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            if (environment.integrated) {
                dispatcher.register(CommandManager.literal("set_gripcrystal_mana")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager
                                .argument("amount", IntegerArgumentType.integer(0, 48))
                                .executes(context -> {
                                    if (context.getSource().getEntity() instanceof ServerPlayerEntity player) {
                                        GripcrystalManaData.addMana((IEntityDataSaver) player, Math.max(0, IntegerArgumentType.getInteger(context, "amount")));
                                        return 1;
                                    } else {
                                        context.getSource()
                                                .sendFeedback(
                                                        () -> Text.literal("Called /set_gripcrystal_mana with no arguments."), false);
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
        ServerTickEvents.END_WORLD_TICK.register(world -> world.getPlayers().stream()
                .filter(player -> !player.isSpectator()
                && GripcrystalManaData.getOpacity(((IEntityDataSaver) player)) > 0)
                .forEach(player -> GripcrystalManaData.removeOpacity((IEntityDataSaver) player, 0.02f)));
    }

    public static Identifier makeID(String id) {
        return new Identifier(MOD_ID, id);
    }

    public static <T> T log(T message) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment() || !ConfigEntries.doLoggingOnlyInDev)
            LOGGER.info(String.valueOf(message));
        return message;
    }
}