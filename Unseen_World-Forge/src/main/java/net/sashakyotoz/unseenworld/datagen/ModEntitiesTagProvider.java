package net.sashakyotoz.unseenworld.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.sashakyotoz.unseenworld.UnseenWorldMod;
import net.sashakyotoz.unseenworld.registries.UnseenWorldEntities;
import net.sashakyotoz.unseenworld.registries.UnseenWorldTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModEntitiesTagProvider extends EntityTypeTagsProvider {
    public ModEntitiesTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, future, UnseenWorldMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(UnseenWorldTags.Entities.ON_DARK_GRASS_SPAWN_WHITELIST).add(
                UnseenWorldEntities.AMETHYST_GOLEM.get(),
                UnseenWorldEntities.DARK_HOGLIN.get(),
                UnseenWorldEntities.DARK_SKELETON.get(),
                UnseenWorldEntities.DARK_SPIRIT_WOLF.get(),
                UnseenWorldEntities.SNOWDRIFTER.get(),
                UnseenWorldEntities.CAVERN_SCARECROW.get(),
                UnseenWorldEntities.CHIMERIC_REDMARER.get(),
                UnseenWorldEntities.CHIMERIC_PURPLEMARER.get()
        );
    }
}
