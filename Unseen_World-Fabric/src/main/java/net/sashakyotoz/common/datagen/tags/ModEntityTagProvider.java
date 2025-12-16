package net.sashakyotoz.common.datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.tags.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModEntityTagProvider extends FabricTagProvider.EntityTypeTagProvider {
    public ModEntityTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        this.getOrCreateTagBuilder(ModTags.Entities.GRIPPING_IMMUNE_ENTITY_TYPES).add(
                ModEntities.WARRIOR_OF_CHIMERIC_DARKNESS,
                ModEntities.ECLIPSE_SENTINEL,
                ModEntities.VIOLEGER,
                ModEntities.SABERPARD,
                ModEntities.ELDRITCH_WATCHER,
                ModEntities.HARMONY_WATCHER,
                ModEntities.GLOOMWHALE,
                ModEntities.TUSKHOG,
                ModEntities.ESPYER,
                ModEntities.GLEAMCARVER
        );
    }
}