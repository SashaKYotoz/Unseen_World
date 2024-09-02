package net.sashakyotoz.common.items;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.FluidBlock;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.ModRegistry;
import net.sashakyotoz.common.blocks.ModBlocks;

public class ModItemGroups {
    public static final ItemGroup UNSEEN_WORLD_EQUIPMENT = register("equipment",
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.unseen_world.equipment"))
                    .icon(() -> new ItemStack(ModItems.ECLIPSE_KEYSTONE))
                    .entries((displayContext, entries) -> {
                        ModRegistry.ITEMS.forEach(item -> {
                            if (!(item instanceof BlockItem))
                                entries.add(item);
                        });
                    }).build());
    public static final ItemGroup UNSEEN_WORLD_BLOCKS = register("blocks",
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.unseen_world.blocks"))
                    .icon(() -> new ItemStack(ModBlocks.DARK_FROST_BRICKS))
                    .entries((displayContext, entries) -> ModRegistry.BLOCKS.forEach(block -> {
                        if (!block.getTranslationKey().contains("sign") && !(block instanceof FluidBlock))
                            entries.add(block);
                    })).build());

    public static ItemGroup register(String id, ItemGroup tab) {
        return Registry.register(Registries.ITEM_GROUP, UnseenWorld.makeID(id), tab);
    }

    public static void register() {
        UnseenWorld.log("Registering ItemGroups for modid : " + UnseenWorld.MOD_ID);
//        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
//            entries.addAfter(Items.WARPED_HANGING_SIGN, ModItems.AMETHYST_SIGN);
//            entries.addAfter(ModItems.AMETHYST_SIGN, ModItems.AMETHYST_HANGING_SIGN);
//        });
    }
}