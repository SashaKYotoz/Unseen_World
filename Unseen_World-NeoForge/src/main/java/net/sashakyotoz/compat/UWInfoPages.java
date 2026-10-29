package net.sashakyotoz.compat;

import net.minecraft.world.level.ItemLike;
import net.sashakyotoz.common.blocks.ModBlocks;
import net.sashakyotoz.common.items.ModItems;

import java.util.List;
import java.util.function.Supplier;

public final class UWInfoPages {
    public static final List<Page> PAGES = List.of(
            new Page("gripping_spread",
                    () -> List.of(ModBlocks.GRIPCRYSTAL_WART, ModBlocks.GRIPPING_DARK_CURRANTSLATE, ModBlocks.GRIPPING_GLACIEMITE,
                            ModBlocks.GRIPPING_CORONA_STONE, ModItems.GRIPCRYSTAL),
                    List.of(
                            "Gripping is a parasite crystal. Creatures in the gripping-immune tag never catch it.",
                            "Gripcrystal warts and gripping stones are where it starts: stay close to them and it takes hold.",
                            "Gripping Gloomwhales, Eclipsed Acolytes and the bosses of the Chimeric Darkness add Gripping with some of their attacks.",
                            "Mobs from a gripping ordeal add 4 seconds with every hit. A raised shield stops it.")),
            new Page("gripping_cure",
                    () -> List.of(ModBlocks.GRIPTONITE_LANTERN, ModItems.GRIPTONITE, ModBlocks.ORDEAL_SPAWNER),
                    List.of(
                            "A placed griptonite lantern removes Gripping from players near it.",
                            "Walk up to an ordeal spawner with 10 or more seconds of Gripping and the spawner absorbs all of it, starting a gripping ordeal.",
                            "Use griptonite on a gripping creature to free it. The griptonite loses durability.",
                            "If the server allows it, Gripping also fades by itself over time.")),
            new Page("effigy_warrior",
                    () -> List.of(ModItems.GRIPCRYSTAL_KEY, ModBlocks.EFFIGY),
                    List.of(
                            "The Effigy of the Warrior of Chimeric Darkness takes 2 Gripcrystal Keys and needs 2 lit seals.",
                            "Gripcrystal Keys are the reward of gripping ordeals at gripcrystal ordeal spawners.")),
            new Page("effigy_sentinel",
                    () -> List.of(ModItems.ABYSSAL_KEY, ModBlocks.EFFIGY),
                    List.of(
                            "The Effigy of the Eclipse Sentinel takes 2 Abyssal Keys and needs 3 lit seals.",
                            "Abyssal Keys are the reward of gripping ordeals at abyssal ordeal spawners.")),
            new Page("effigy_patriarch",
                    () -> List.of(ModItems.AURIC_KEY, ModBlocks.EFFIGY),
                    List.of(
                            "The Effigy of the Auric Patriarch takes 2 Auric Keys and needs 4 lit seals.",
                            "Auric Keys are the reward of gripping ordeals at auric ordeal spawners.")),
            new Page("effigy_seals",
                    () -> List.of(ModBlocks.EFFIGY, ModBlocks.ENGRAVED_GLACIEMITE_BRICKS, ModBlocks.ENGRAVED_CORONA_STONE_BRICKS, ModBlocks.GRIPPING_GLACIEMITE),
                    List.of(
                            "A seal is a lit engraved brick 2 blocks from the effigy core, at the middle of a pedestal side, with its arrow pointing into the pedestal.",
                            "A brick takes power from a gripping stone behind its arrow and passes it on along the arrow. Land on a brick to turn it clockwise; sneak while landing to turn it back.",
                            "With every key inserted and enough seals lit, the statue rumbles for 5 seconds and the boss steps out. Click with an empty hand to see progress; sneak-click to take a key back.",
                            "When the boss dies, the statue re-forms and the effigy accepts keys again.")),
            new Page("ordeal_spawner",
                    () -> List.of(ModBlocks.ORDEAL_SPAWNER),
                    List.of(
                            "An ordeal starts 3 seconds after a survival player comes within 14 blocks in line of sight.",
                            "Normal ordeal: 6 mobs plus 2 per extra player (up to 4 players), 2 alive at once plus 1 per extra player, one every 2 seconds. Cooldown: 20 minutes.",
                            "The first half of the mobs is the wave 1 creature, the second half the wave 2 creature. Every player who took part gets their own reward roll.",
                            "Gripping ordeal: 10 mobs plus 3 per extra player, 3 alive at once plus 1 per extra player, one every second, and each hit adds 4 seconds of Gripping. Its reward includes the spawner's boss key. Cooldown: about 27 minutes, then the spawner turns back to normal.",
                            "Leave for 30 seconds and the remaining mobs vanish and the ordeal resets."))
    );

    private UWInfoPages() {
    }

    public static String lineKey(Page page, int line) {
        return "emi.info.unseen_world." + page.id() + "." + line;
    }

    public record Page(String id, Supplier<List<ItemLike>> items, List<String> lines) {
    }
}