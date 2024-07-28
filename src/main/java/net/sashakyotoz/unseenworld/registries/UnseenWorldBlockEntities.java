package net.sashakyotoz.unseenworld.registries;

import net.sashakyotoz.unseenworld.block.entity.BeaconOfWeaponsBlockEntity;
import net.sashakyotoz.unseenworld.block.entity.GoldenChestBlockEntity;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.Block;

import net.sashakyotoz.unseenworld.UnseenWorldMod;

public class UnseenWorldBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, UnseenWorldMod.MODID);
	public static final RegistryObject<BlockEntityType<?>> GOLDENCHEST = register("goldenchest", UnseenWorldBlocks.GOLDEN_CHEST, GoldenChestBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BeaconOfWeaponsBlockEntity>> BEACON_OF_WEAPONS =
			REGISTRY.register("beacon_of_weapons", () ->
					BlockEntityType.Builder.of(BeaconOfWeaponsBlockEntity::new,
							UnseenWorldBlocks.BEACON_OF_WEAPONS.get()).build(null));

	private static RegistryObject<BlockEntityType<?>> register(String registryname, RegistryObject<Block> block, BlockEntityType.BlockEntitySupplier<?> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}
}
