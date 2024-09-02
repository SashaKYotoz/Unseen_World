package net.sashakyotoz.common.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.data.client.Models;
import net.minecraft.item.BucketItem;
import net.minecraft.item.HangingSignItem;
import net.minecraft.item.Item;
import net.minecraft.item.SignItem;
import net.minecraft.util.Rarity;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.blocks.ModBlocks;

public class ModItems {
    public static void register() {
        UnseenWorld.log("Registering Items for modid : " + UnseenWorld.MOD_ID);
    }
    public static final Item ECLIPSE_KEYSTONE = ModRegistry.ofItem("eclipse_keystone",
            new Item(new FabricItemSettings().rarity(Rarity.EPIC))).model(Models.GENERATED).build();
    //ore
    public static final Item RAW_ABYSSAL_ORE = ModRegistry.ofItem("raw_abyssal",
            new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON))).model(Models.GENERATED).build();
    public static final Item RAW_RED_TITANIUM_ORE = ModRegistry.ofItem("raw_red_titanium",
            new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON))).model(Models.GENERATED).build();
    public static final Item RAW_UNSEENIUM_ORE = ModRegistry.ofItem("raw_unseenium",
            new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON))).model(Models.GENERATED).build();
    public static final Item ABYSSAL_INGOT = ModRegistry.ofItem("abyssal_ingot",
            new Item(new FabricItemSettings().rarity(Rarity.RARE))).model(Models.GENERATED).build();
    public static final Item RED_TITANIUM_INGOT = ModRegistry.ofItem("red_titanium_ingot",
            new Item(new FabricItemSettings().rarity(Rarity.RARE))).model(Models.GENERATED).build();
    public static final Item UNSEENIUM_INGOT = ModRegistry.ofItem("unseenium_ingot",
            new Item(new FabricItemSettings().rarity(Rarity.RARE))).model(Models.GENERATED).build();
    //food
    public static final Item CRYSTIE_APPLE = ModRegistry.ofItem("crystie_apple",
            new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON).food(ModRegistry.Foods.CRYSTIE_APPLE))).model(Models.GENERATED).build();
    //wood items
    public static final Item AMETHYST_SIGN = ModRegistry.ofItem("amethyst_sign",
                    new SignItem(new FabricItemSettings(), ModBlocks.AMETHYST_SIGN, ModBlocks.AMETHYST_WALL_SIGN))
            .fuel(200).build();
    public static final Item AMETHYST_HANGING_SIGN = ModRegistry.ofItem("amethyst_hanging_sign",
                    new HangingSignItem(ModBlocks.AMETHYST_HANGING_SIGN, ModBlocks.AMETHYST_WALL_HANGING_SIGN, new FabricItemSettings()))
            .fuel(200).build();
    //buckets
    public static final Item DARK_WATER_BUCKET = ModRegistry.ofItem("dark_water_bucket",
            new BucketItem(ModBlocks.DARK_WATER,new FabricItemSettings())).model(Models.GENERATED).build();
}