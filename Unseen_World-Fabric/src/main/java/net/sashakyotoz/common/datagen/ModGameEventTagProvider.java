package net.sashakyotoz.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.GameEventTagsProvider;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sashakyotoz.common.tags.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModGameEventTagProvider extends GameEventTagsProvider {

    public ModGameEventTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void addTags(HolderLookup.Provider arg) {
        this.tag(ModTags.GameEvents.ELDRITCH_CAN_LISTEN)
                .add(GameEvent.BLOCK_PLACE)
                .add(GameEvent.BLOCK_ACTIVATE)
                .add(GameEvent.BLOCK_DESTROY)
                .add(GameEvent.PROJECTILE_LAND)
                .add(GameEvent.PROJECTILE_SHOOT)
                .add(GameEvent.ITEM_INTERACT_FINISH)
                .add(GameEvent.BLOCK_CHANGE);

        this.tag(ModTags.GameEvents.ENTITY_SHOOT)
                .add(GameEvent.EXPLODE)
                .add(GameEvent.PROJECTILE_SHOOT)
                .add(GameEvent.SHRIEK);

        this.tag(ModTags.GameEvents.ENTITY_MOVE)
                .add(GameEvent.STEP)
                .add(GameEvent.ELYTRA_GLIDE)
                .add(GameEvent.TELEPORT);
    }
}