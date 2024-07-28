package net.sashakyotoz.unseenworld.registries;

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
public class UnseenWorldTrades {
    @SubscribeEvent
    public static void registerTrades(VillagerTradesEvent event) {
        Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
        if (event.getType() == UnseenWorldVillagerProfessions.SEEKER_WAYFARER.get()) {
            trades.get(1).add(new BasicItemListing(new ItemStack(UnseenWorldItems.TANZANITE_SHARD.get()),

                    new ItemStack(Items.AMETHYST_SHARD), 12, 5, 0.05f));
            trades.get(1).add(new BasicItemListing(new ItemStack(UnseenWorldBlocks.TANZASHROOM.get()),

                    new ItemStack(Blocks.BROWN_MUSHROOM), 10, 5, 0.05f));
            trades.get(2).add(new BasicItemListing(new ItemStack(UnseenWorldItems.RAW_UNSEENIUM.get()), new ItemStack(UnseenWorldItems.DEEP_GEM.get(), 4), new ItemStack(UnseenWorldItems.UNSEEN_INGOT.get(), 2), 10, 5, 0.05f));
            trades.get(2).add(new BasicItemListing(new ItemStack(UnseenWorldItems.BLUE_VOID.get()), new ItemStack(UnseenWorldItems.DEEP_GEM.get(), 6), new ItemStack(UnseenWorldItems.VOID_INGOT.get()), 10, 5, 0.05f));
            trades.get(2).add(new BasicItemListing(new ItemStack(UnseenWorldItems.NATURERIUM_INGOT.get()),

                    new ItemStack(Items.NETHERITE_SCRAP, 2), 5, 5, 0.05f));
			trades.get(1).add(new BasicItemListing(new ItemStack(UnseenWorldItems.DEEP_GEM.get(), 8), new ItemStack(Items.FLINT_AND_STEEL), new ItemStack(UnseenWorldItems.THE_DARKNESS.get()), 10, 5, 0.1f));
            trades.get(3).add(new BasicItemListing(new ItemStack(Items.EMERALD, 3),

                    new ItemStack(UnseenWorldItems.DEEP_GEM.get()), 8, 5, 0.05f));
            trades.get(4).add(new BasicItemListing(new ItemStack(UnseenWorldBlocks.DARK_CRIMSON_FLOWING_AZALEA.get()), new ItemStack(UnseenWorldItems.DEEP_GEM.get(), 2), new ItemStack(Blocks.FLOWERING_AZALEA), 10, 5, 0.05f));
            trades.get(5).add(new BasicItemListing(new ItemStack(UnseenWorldItems.DARK_PEARL.get()), new ItemStack(UnseenWorldItems.DEEP_GEM.get(), 4), new ItemStack(Items.ENDER_PEARL), 10, 5, 0.05f));
            trades.get(1).add(new BasicItemListing(new ItemStack(Blocks.DEEPSLATE_BRICKS), new ItemStack(UnseenWorldItems.DEEP_GEM.get(), 6), new ItemStack(UnseenWorldBlocks.COLD_DARK_BRICKS.get()), 10, 5, 0.05f));
        }
        if (event.getType() == VillagerProfession.MASON) {
            trades.get(2).add(new BasicItemListing(new ItemStack(Blocks.DEEPSLATE_BRICKS), new ItemStack(Items.EMERALD, 12), new ItemStack(UnseenWorldBlocks.COLD_DARK_BRICKS.get()), 12, 5, 0.1f));
        }
        if (event.getType() == VillagerProfession.FARMER) {
            trades.get(4).add(new BasicItemListing(new ItemStack(UnseenWorldItems.PURPLE_BERRIES.get(), 8),

                    new ItemStack(Items.EMERALD), 10, 5, 0.05f));
        }
        if (event.getType() == VillagerProfession.FISHERMAN) {
            trades.get(5).add(new BasicItemListing(new ItemStack(UnseenWorldItems.DUSTY_PINK_MAXOR_FISH_FOOD.get()), new ItemStack(Items.EMERALD, 2), new ItemStack(UnseenWorldItems.COOKED_DUSTY_PINK_MAXOR_FISH.get()), 10, 5, 0.05f));
            trades.get(1).add(new BasicItemListing(new ItemStack(UnseenWorldItems.MOON_FISH_FOOD.get(), 4),

                    new ItemStack(Items.EMERALD), 10, 5, 0.05f));
        }
        if (event.getType() == VillagerProfession.CARTOGRAPHER) {
            trades.get(2).add(new BasicItemListing(new ItemStack(Items.EMERALD, 8), new ItemStack(Items.FLINT_AND_STEEL), new ItemStack(UnseenWorldItems.THE_DARKNESS.get()), 10, 5, 0.05f));
        }
    }
}
