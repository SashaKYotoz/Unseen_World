package net.sashakyotoz.unseenworld.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sashakyotoz.unseenworld.UnseenWorldMod;

@Mod.EventBusSubscriber(modid = UnseenWorldMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class UnseenWorldDataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        BlockTagsProvider blocktags = new UnseenWorldBlockTagProvider(event.getGenerator().getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper());
        event.getGenerator().addProvider(event.includeServer(), blocktags);
        event.getGenerator().addProvider(event.includeServer(), new UnseenWorldItemTagProvider(event.getGenerator().getPackOutput(),event.getLookupProvider(),blocktags,event.getExistingFileHelper()));
        event.getGenerator().addProvider(event.includeServer(), new UnseenWorldBiomeTagProvider(event.getGenerator().getPackOutput(),event.getLookupProvider(),event.getExistingFileHelper()));
//        generator.addProvider(true,new HumbledlessWorldLangProvider(packOutput,"en_us",false));
    }
}
