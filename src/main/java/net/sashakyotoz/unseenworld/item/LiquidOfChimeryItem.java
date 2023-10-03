
package net.sashakyotoz.unseenworld.item;

import net.sashakyotoz.unseenworld.init.UnseenWorldModFluids;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BucketItem;

public class LiquidOfChimeryItem extends BucketItem {
	public LiquidOfChimeryItem() {
		super(UnseenWorldModFluids.LIQUID_OF_CHIMERY, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).rarity(Rarity.RARE));
	}
}
