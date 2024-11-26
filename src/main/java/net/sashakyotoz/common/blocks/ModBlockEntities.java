package net.sashakyotoz.common.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.custom.entities.TranslocatoneBlockEntity;
import net.sashakyotoz.common.blocks.custom.entities.KeyHandlerStoneBlockEntity;

public class ModBlockEntities {
    public static void register() {
        UnseenWorld.log("Registering BlockEntities for modid : " + UnseenWorld.MOD_ID);
    }

    public static final BlockEntityType<KeyHandlerStoneBlockEntity> KEY_HANDLER = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            UnseenWorld.makeID("key_handler_stone"),
            FabricBlockEntityTypeBuilder.create(KeyHandlerStoneBlockEntity::new, ModBlocks.KEY_HANDLER_STONE).build());
    public static final BlockEntityType<TranslocatoneBlockEntity> TRANSLOCATONE = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            UnseenWorld.makeID("translocatone"),
            FabricBlockEntityTypeBuilder.create(TranslocatoneBlockEntity::new, ModBlocks.GLACIEMITE_TRANSLOCATONE).build());
}