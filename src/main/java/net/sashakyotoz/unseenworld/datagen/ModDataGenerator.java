package net.sashakyotoz.unseenworld.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sashakyotoz.unseenworld.UnseenWorldMod;

@Mod.EventBusSubscriber(modid = UnseenWorldMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
//        BlockTagsProvider blocktags = new ModBlockTagProvider(event.getGenerator().getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper());
//        event.getGenerator().addProvider(event.includeServer(), blocktags);
//        generator.addProvider(true, ModLootTableProvider.create(packOutput));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, existingFileHelper));
//        event.getGenerator().addProvider(event.includeServer(), new ModItemTagProvider(event.getGenerator().getPackOutput(),event.getLookupProvider(),blocktags,event.getExistingFileHelper()));
        event.getGenerator().addProvider(event.includeServer(), new ModBiomeTagProvider(event.getGenerator().getPackOutput(),event.getLookupProvider(),event.getExistingFileHelper()));
        event.getGenerator().addProvider(event.includeServer(), new ModEntitiesTagProvider(event.getGenerator().getPackOutput(),event.getLookupProvider(),event.getExistingFileHelper()));
    }
}
