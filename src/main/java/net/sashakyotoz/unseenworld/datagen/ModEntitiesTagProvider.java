package net.sashakyotoz.unseenworld.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModEntities;
import net.sashakyotoz.unseenworld.registries.UnseenWorldModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModEntitiesTagProvider extends EntityTypeTagsProvider {
    public ModEntitiesTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, future, UnseenWorldMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(UnseenWorldModTags.Entities.ON_DARK_GRASS_SPAWN_WHITELIST).add(
                UnseenWorldModEntities.AMETHYST_GOLEM.get(),
                UnseenWorldModEntities.DARK_HOGLIN.get(),
                UnseenWorldModEntities.DARK_SKELETON.get(),
                UnseenWorldModEntities.DARK_SPIRIT_WOLF.get(),
                UnseenWorldModEntities.SNOWDRIFTER.get(),
                UnseenWorldModEntities.CAVERN_SCARECROW.get(),
                UnseenWorldModEntities.CHIMERIC_REDMARER.get(),
                UnseenWorldModEntities.CHIMERIC_PURPLEMARER.get()
        );
    }
}
