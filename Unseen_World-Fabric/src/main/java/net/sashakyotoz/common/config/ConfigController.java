package net.sashakyotoz.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.items.custom.ChimericRockbreakerHammerItem;
import net.sashakyotoz.common.items.custom.EclipsebaneItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigController implements ServerWorldEvents.Load {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static List<DataItem> DATA_LIST;
    @SuppressWarnings("rawtypes")
    public static Map CONFIG = new TreeMap<>();

    @Override
    public void onWorldLoad(MinecraftServer server, ServerWorld world) {
        WorldConfigController.data.put(0, WorldConfigController.loadData(world));
        DATA_LIST = loadGrippingManaConfig();
    }

    public static List<DataItem> getDataList() {
        return Objects.requireNonNullElseGet(DATA_LIST, List::of);
    }

    public record DataItem(Item item, String compatibilityType, Identifier spellIcon, int manaCost,
                           @Nullable Identifier effectToRemove, @Nullable List<String> actionToPerform) {
    }

    public record ItemEntry(String itemId, String compatibilityType, String spellIcon, int manaCost,
                            @Nullable String effectToRemove, @Nullable List<String> actionToPerform) {
    }

    public record ModEntry(String modId, List<ItemEntry> items) {
    }

    public record ModConfig(List<ModEntry> entries) {
    }

    public static List<DataItem> loadGrippingManaConfig() {
        try {
            Path configPath = FabricLoader.getInstance().getConfigDir().resolve("unseen_world/uw-compatibility-config.json");
            if (!Files.exists(configPath)) {
                Files.createDirectories(configPath.getParent());
                Files.writeString(configPath, getCompatibilityConfig());
            }
            String json = Files.readString(configPath);
            ModConfig config = gson.fromJson(json, ModConfig.class);

            List<DataItem> spellItems = new ArrayList<>();
            for (ModEntry modEntry : config.entries()) {
                for (ItemEntry itemEntry : modEntry.items()) {
                    Identifier itemIdentifier = new Identifier(modEntry.modId(), itemEntry.itemId());
                    Item item = Registries.ITEM.get(itemIdentifier);
                    DataItem spellItem = getDataItem(modEntry, itemEntry, item);
                    spellItems.add(spellItem);
                }
            }
            UnseenWorld.log("Loaded %s compatibility-config".formatted(UnseenWorld.MOD_ID));
            return spellItems;
        } catch (Exception e) {
            return List.of();
        }
    }

    public static void loadConfig() {
        try {
            Path configPath = FabricLoader.getInstance().getConfigDir().resolve("unseen_world/uw-config.json");
            if (!Files.exists(configPath)) {
                Files.createDirectories(configPath.getParent());
                Files.writeString(configPath, getEntriesConfig()); 
            }
            String json = Files.readString(configPath);
            CONFIG = gson.fromJson(json, Map.class);
            ConfigEntries.reload();
        } catch (Exception e) {
            UnseenWorld.log("Failed to load %s config".formatted(UnseenWorld.MOD_ID));
        }
    }

    private static @NotNull DataItem getDataItem(ModEntry modEntry, ItemEntry itemEntry, Item item) {
        Identifier spellIconIdentifier = new Identifier(itemEntry.spellIcon());
        DataItem dataItem;
        if (itemEntry.compatibilityType.equals(Type.EFFECT_REMOVER.type) && itemEntry.effectToRemove() != null)
            dataItem = new DataItem(item, itemEntry.compatibilityType(), spellIconIdentifier, itemEntry.manaCost(), new Identifier(modEntry.modId(), itemEntry.effectToRemove()), null);
        else
            dataItem = new DataItem(item, itemEntry.compatibilityType(), spellIconIdentifier, itemEntry.manaCost(), null, itemEntry.actionToPerform());
        return dataItem;
    }

    public static DataItem getDataToStack(ItemStack stack) {
        if (stack.isOf(Items.AIR))
            return null;
        if (getDataList().stream().anyMatch(data -> data.item().equals(stack.getItem())))
            return getDataList().stream().filter(data -> data.item().equals(stack.getItem())).findAny().get();
        else
            return null;
    }

    private static String getCompatibilityConfig() {
        return """
                {
                   "entries": [
                     {
                       "modId": "sortilege",
                       "items": [
                         {
                           "itemId": "gripcrystal_staff",
                           "compatibilityType": "spell",
                           "spellIcon": "unseen_world:textures/gui/gripcrystal_abilities/staff_aura.png",
                           "manaCost": 6,
                           "actionToPerform": [
                             "/execute at @s run summon area_effect_cloud ~ ~ ~ {Particle:\\"unseen_world:gripping_crystal\\",Color:2333625,Radius:3,RadiusPerTick:-0.2,Duration:60,Effects:[{Duration:80,ShowParticles:1b,ShowIcon:1}]}",
                             "/execute at @s run effect give @e[distance=..9,type=!minecraft:player] minecraft:weakness 5 2 true",
                             "/execute at @s run effect give @e[distance=..6,type=!minecraft:player] minecraft:levitation 4 2 true",
                             "/execute at @s run tp @e[distance=..6,type=!minecraft:player] ^ ^ ^5",
                             "damageAll _8_5",
                             "/playsound minecraft:block.amethyst_block.break player @p ~ ~ ~ 2 1.5"
                           ]
                         },
                         {
                           "itemId": "netherite_staff",
                           "compatibilityType": "spell",
                           "spellIcon": "unseen_world:textures/gui/gripcrystal_abilities/staff_aura.png",
                           "manaCost": 2,
                           "actionToPerform": [
                             "/execute at @s run summon area_effect_cloud ~ ~ ~ {Particle:\\"unseen_world:gripping_crystal\\",Color:2333625,Radius:3,RadiusPerTick:-0.2,Duration:60,Effects:[{Duration:80,ShowParticles:1b,ShowIcon:1}]}",
                             "/execute at @s run effect give @e[distance=..9,type=!minecraft:player] minecraft:weakness 5 2 true",
                             "/execute at @s run tp @e[distance=..6,type=!minecraft:player] ^ ^ ^3",
                             "damageAll _7_4",
                             "/playsound minecraft:block.amethyst_block.break player @p ~ ~ ~ 2 1.25"
                           ]
                         }
                       ]
                     },
                     {
                       "modId": "minecells",
                       "items": [
                         {
                           "itemId": "cursed_sword",
                           "compatibilityType": "effect_remover",
                           "spellIcon": "unseen_world:textures/gui/gripcrystal_abilities/gripcrystal_shield.png",
                           "manaCost": 1,
                           "effectToRemove": "cursed"
                         }
                       ]
                     },
                     {
                       "modId": "deeperdarker",
                       "items": [
                         {
                           "itemId": "sonorous_staff",
                           "compatibilityType": "spell",
                           "spellIcon": "unseen_world:textures/gui/gripcrystal_abilities/sonorous_whirlwind.png",
                           "manaCost": 4,
                           "actionToPerform": [
                             "/execute at @s anchored eyes run particle minecraft:sonic_boom ^ ^ ^5",
                             "/execute at @s anchored eyes run particle minecraft:sonic_boom ^5 ^ ^",
                             "/execute at @s anchored eyes run particle minecraft:sonic_boom ^-5 ^ ^",
                             "/execute at @s anchored eyes run particle minecraft:sonic_boom ^ ^ ^-5",
                             "damageAll _8_5",
                             "/playsound minecraft:block.sculk_shrieker.break player @p ~ ~ ~ 2 1.5"
                           ]
                         }
                       ]
                     }
                   ]
                 }
                """;
    }

    private static String getEntriesConfig() {
        return """
                {
                    "mobs":{
                        "animate_death_for_mobs": true,
                        "render_gripping_on_mobs": true,
                        "spawn_gripping_particles": true,
                        "remove_gripping_naturally": true,
                        "violeger_patrol_spawns_in": 9000
                    },
                    "gameplay":{
                        "do_abyssal_armor_save_from_void": true,
                        "key_handler_block_cooldown": 24000,
                        "x_gripping_mana_texture_offset": 0,
                        "y_gripping_mana_texture_offset": 0
                    },
                    "technic":{
                        "do_logging_only_in_dev": true
                    }
                }
                """;
    }

    public static boolean canHandleGripcrystalAbility(ItemStack stack) {
        return (stack.getItem() instanceof EclipsebaneItem eclipsebaneItem && eclipsebaneItem.getItemPhase(stack).equals("light_ray"))
                || (stack.getItem() instanceof ChimericRockbreakerHammerItem hammerItem && hammerItem.getItemPhase(stack).equals("heavy_winding"))
                || ConfigController.getDataToStack(stack) != null;
    }

    public static List<Integer> getIntegers(String s) {
        List<Integer> ints = new ArrayList<>();
        Pattern pattern = Pattern.compile("_(\\d+)");
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            ints.add(Integer.valueOf(matcher.group(1)));
            if (ints.size() >= 2)
                break;
        }
        return ints;
    }

    public enum Type {
        SPELL("spell"),
        EFFECT_REMOVER("effect_remover");

        public final String type;

        Type(String type) {
            this.type = type;
        }
    }
}