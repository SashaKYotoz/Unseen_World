package net.sashakyotoz.common.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.custom.chests.entities.DarkCurranslateChestEntity;
import net.sashakyotoz.common.blocks.custom.chests.entities.GlaciemiteChestEntity;
import net.sashakyotoz.common.blocks.custom.entities.KeyHandlerStoneBlockEntity;
import net.sashakyotoz.common.blocks.custom.entities.SusBlockEntity;
import net.sashakyotoz.common.blocks.custom.entities.TranslocatoneBlockEntity;

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
    public static final BlockEntityType<SusBlockEntity> SUSBLOCK = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            UnseenWorld.makeID("sus_block"),
            FabricBlockEntityTypeBuilder.create(SusBlockEntity::new, ModBlocks.SUSPICIOUS_ASHEN_OOZE, ModBlocks.SUSPICIOUS_GLIMMERGRAIN_SAND).build());
    public static final BlockEntityType<DarkCurranslateChestEntity> DARK_CURRANTSLATE_CHEST = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            UnseenWorld.makeID("dark_currantslate_chest"),
            FabricBlockEntityTypeBuilder.create(DarkCurranslateChestEntity::new, ModBlocks.DARK_CURRANTSLATE_CHEST).build());
    public static final BlockEntityType<GlaciemiteChestEntity> GLACIEMITE_CHEST = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            UnseenWorld.makeID("glaciemite_chest"),
            FabricBlockEntityTypeBuilder.create(GlaciemiteChestEntity::new, ModBlocks.GLACIEMITE_CHEST).build());
}