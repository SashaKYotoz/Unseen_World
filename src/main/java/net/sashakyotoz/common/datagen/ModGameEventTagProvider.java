package net.sashakyotoz.common.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.server.tag.vanilla.VanillaGameEventTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.event.GameEvent;
import net.sashakyotoz.common.tags.ModTags;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModGameEventTagProvider extends VanillaGameEventTagProvider {
    private final List<GameEvent> BASIC_GAME_EVENTS = List.of(GameEvent.BLOCK_ATTACH,
            GameEvent.BLOCK_CHANGE,
            GameEvent.BLOCK_CLOSE,
            GameEvent.BLOCK_DESTROY,
            GameEvent.BLOCK_DETACH,
            GameEvent.BLOCK_OPEN,
            GameEvent.BLOCK_PLACE,
            GameEvent.BLOCK_ACTIVATE,
            GameEvent.BLOCK_DEACTIVATE,
            GameEvent.CONTAINER_CLOSE,
            GameEvent.CONTAINER_OPEN,
            GameEvent.DRINK,
            GameEvent.EAT,
            GameEvent.ENTITY_DAMAGE,
            GameEvent.ENTITY_PLACE,
            GameEvent.ENTITY_ROAR,
            GameEvent.ENTITY_SHAKE,
            GameEvent.EQUIP,
            GameEvent.EXPLODE,
            GameEvent.HIT_GROUND,
            GameEvent.INSTRUMENT_PLAY,
            GameEvent.ITEM_INTERACT_FINISH,
            GameEvent.LIGHTNING_STRIKE,
            GameEvent.NOTE_BLOCK_PLAY,
            GameEvent.PROJECTILE_LAND,
            GameEvent.PROJECTILE_SHOOT,
            GameEvent.SHEAR,
            GameEvent.SPLASH,
            GameEvent.STEP,
            GameEvent.SWIM,
            GameEvent.TELEPORT);

    public ModGameEventTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void configure(RegistryWrapper.WrapperLookup arg) {
        for (GameEvent event : BASIC_GAME_EVENTS)
            this.getOrCreateTagBuilder(ModTags.GameEvents.ELDRITCH_CAN_LISTEN).add(event);
    }
}