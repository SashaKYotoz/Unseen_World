package net.sashakyotoz.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.GameEventTagsProvider;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.tags.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModGameEventTagProvider extends GameEventTagsProvider {

    public ModGameEventTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture, ExistingFileHelper existingFileHelper) {
        super(output, registriesFuture, UnseenWorld.MOD_ID, existingFileHelper);
    }

    @Override
    public void addTags(HolderLookup.Provider arg) {
        this.tag(ModTags.GameEvents.ELDRITCH_CAN_LISTEN)
                .add(GameEvent.BLOCK_PLACE.key())
                .add(GameEvent.BLOCK_ACTIVATE.key())
                .add(GameEvent.BLOCK_DESTROY.key())
                .add(GameEvent.PROJECTILE_LAND.key())
                .add(GameEvent.PROJECTILE_SHOOT.key())
                .add(GameEvent.ITEM_INTERACT_FINISH.key())
                .add(GameEvent.BLOCK_CHANGE.key());

        this.tag(ModTags.GameEvents.ENTITY_SHOOT)
                .add(GameEvent.EXPLODE.key())
                .add(GameEvent.PROJECTILE_SHOOT.key())
                .add(GameEvent.SHRIEK.key());

        this.tag(ModTags.GameEvents.ENTITY_MOVE)
                .add(GameEvent.STEP.key())
                .add(GameEvent.ELYTRA_GLIDE.key())
                .add(GameEvent.TELEPORT.key());
    }
}