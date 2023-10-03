
package net.sashakyotoz.unseenworld.item;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;

public class MusicdiscPianoItem extends RecordItem {
	public MusicdiscPianoItem() {
		super(2, () -> ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("unseen_world:chaotic")), new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 0);
	}
}
