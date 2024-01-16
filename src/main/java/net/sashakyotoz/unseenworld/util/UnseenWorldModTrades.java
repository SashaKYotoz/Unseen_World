
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.sashakyotoz.unseenworld.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.common.BasicItemListing;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class UnseenWorldModTrades {
    @SubscribeEvent
    public static void registerTrades(VillagerTradesEvent event) {
        Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
        if (event.getType() == UnseenWorldModVillagerProfessions.SEEKER_WAYFARER.get()) {
            trades.get(1).add(new BasicItemListing(new ItemStack(UnseenWorldModItems.TANZANITE_SHARD.get()),

                    new ItemStack(Items.AMETHYST_SHARD), 12, 5, 0.05f));
            trades.get(1).add(new BasicItemListing(new ItemStack(UnseenWorldModBlocks.TANZASHROOM.get()),

                    new ItemStack(Blocks.BROWN_MUSHROOM), 10, 5, 0.05f));
            trades.get(2).add(new BasicItemListing(new ItemStack(UnseenWorldModItems.RAW_UNSEENIUM.get()), new ItemStack(UnseenWorldModItems.DEEP_GEM.get(), 4), new ItemStack(UnseenWorldModItems.UNSEEN_INGOT.get(), 2), 10, 5, 0.05f));
            trades.get(2).add(new BasicItemListing(new ItemStack(UnseenWorldModItems.BLUE_VOID.get()), new ItemStack(UnseenWorldModItems.DEEP_GEM.get(), 6), new ItemStack(UnseenWorldModItems.VOID_INGOT.get()), 10, 5, 0.05f));
            trades.get(2).add(new BasicItemListing(new ItemStack(UnseenWorldModItems.NATURERIUM_INGOT.get()),

                    new ItemStack(Items.NETHERITE_SCRAP, 2), 5, 5, 0.05f));
			trades.get(1).add(new BasicItemListing(new ItemStack(UnseenWorldModItems.DEEP_GEM.get(), 8), new ItemStack(Items.FLINT_AND_STEEL), new ItemStack(UnseenWorldModItems.THE_DARKNESS.get()), 10, 5, 0.1f));
            trades.get(3).add(new BasicItemListing(new ItemStack(Items.EMERALD, 3),

                    new ItemStack(UnseenWorldModItems.DEEP_GEM.get()), 8, 5, 0.05f));
            trades.get(4).add(new BasicItemListing(new ItemStack(UnseenWorldModBlocks.DARK_CRIMSON_FLOWING_AZALIA.get()), new ItemStack(UnseenWorldModItems.DEEP_GEM.get(), 2), new ItemStack(Blocks.FLOWERING_AZALEA), 10, 5, 0.05f));
            trades.get(5).add(new BasicItemListing(new ItemStack(UnseenWorldModItems.DARKPEARL.get()), new ItemStack(UnseenWorldModItems.DEEP_GEM.get(), 4), new ItemStack(Items.ENDER_PEARL), 10, 5, 0.05f));
            trades.get(1).add(new BasicItemListing(new ItemStack(Blocks.DEEPSLATE_BRICKS), new ItemStack(UnseenWorldModItems.DEEP_GEM.get(), 6), new ItemStack(UnseenWorldModBlocks.COLD_DARK_BRICKS.get()), 10, 5, 0.05f));
        }
        if (event.getType() == VillagerProfession.MASON) {
            trades.get(2).add(new BasicItemListing(new ItemStack(Blocks.DEEPSLATE_BRICKS), new ItemStack(Items.EMERALD, 12), new ItemStack(UnseenWorldModBlocks.COLD_DARK_BRICKS.get()), 12, 5, 0.1f));
        }
        if (event.getType() == VillagerProfession.FARMER) {
            trades.get(4).add(new BasicItemListing(new ItemStack(UnseenWorldModItems.PURPLE_BERRIES.get(), 8),

                    new ItemStack(Items.EMERALD), 10, 5, 0.05f));
        }
        if (event.getType() == VillagerProfession.FISHERMAN) {
            trades.get(5).add(new BasicItemListing(new ItemStack(UnseenWorldModItems.DUSTY_PINK_MAXOR_FISH_FOOD.get()), new ItemStack(Items.EMERALD, 2), new ItemStack(UnseenWorldModItems.COOKED_DUSTY_PINK_MAXOR_FISH.get()), 10, 5, 0.05f));
            trades.get(1).add(new BasicItemListing(new ItemStack(UnseenWorldModItems.MOON_FISH_FOOD.get(), 4),

                    new ItemStack(Items.EMERALD), 10, 5, 0.05f));
        }
        if (event.getType() == VillagerProfession.CARTOGRAPHER) {
            trades.get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 8), new ItemStack(Items.FLINT_AND_STEEL), new ItemStack(UnseenWorldModItems.THE_DARKNESS.get()), 10, 5, 0.05f));
        }
    }
}
