package net.sashakyotoz.common.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.blocks.custom.chests.ModChestBlock;
import net.sashakyotoz.common.blocks.custom.chests.entities.ModChestBlockEntity;
import net.sashakyotoz.common.blocks.custom.entities.EffigyBlockEntity;
import net.sashakyotoz.common.blocks.custom.entities.OrdealSpawnerBlockEntity;
import net.sashakyotoz.common.blocks.custom.entities.SusBlockEntity;

public class ModBlockEntities {
    public static void register() {
        UnseenWorld.log().debug("Registering BlockEntities for modid : " + UnseenWorld.MOD_ID);
        OrdealSpawnerBlockEntity.registerEvents();
        EffigyBlockEntity.registerEvents();
    }

    public static final BlockEntityType<EffigyBlockEntity> EFFIGY =
            register("effigy", EffigyBlockEntity::new, ModBlocks.EFFIGY);
    public static final BlockEntityType<SusBlockEntity> SUSBLOCK =
            register("sus_block", SusBlockEntity::new, ModBlocks.SUSPICIOUS_ASHEN_OOZE, ModBlocks.SUSPICIOUS_GLIMMERGRAIN_SAND);
    public static final BlockEntityType<ModChestBlockEntity> DARK_CURRANTSLATE_CHEST =
            register("dark_currantslate_chest", (pos, state) -> new ModChestBlockEntity(ModChestBlock.ChestTypes.DARK_CURRANTSLATE, pos, state), ModBlocks.DARK_CURRANTSLATE_CHEST);
    public static final BlockEntityType<ModChestBlockEntity> GLACIEMITE_CHEST =
            register("glaciemite_chest", (pos, state) -> new ModChestBlockEntity(ModChestBlock.ChestTypes.GLACIEMITE, pos, state), ModBlocks.GLACIEMITE_CHEST);
    public static final BlockEntityType<ModChestBlockEntity> CORONA_STONE_CHEST =
            register("corona_stone_chest", (pos, state) -> new ModChestBlockEntity(ModChestBlock.ChestTypes.CORONA_STONE, pos, state), ModBlocks.CORONA_STONE_CHEST);
    public static final BlockEntityType<OrdealSpawnerBlockEntity> ORDEAL_SPAWNER =
            register("ordeal_spawner", OrdealSpawnerBlockEntity::new, ModBlocks.ORDEAL_SPAWNER);

    private static <T extends BlockEntity> BlockEntityType<T> register(String id, FabricBlockEntityTypeBuilder.Factory<T> factory, Block... blocks) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, UnseenWorld.MOD.makeID(id),
                FabricBlockEntityTypeBuilder.create(factory, blocks).build());
    }
}
