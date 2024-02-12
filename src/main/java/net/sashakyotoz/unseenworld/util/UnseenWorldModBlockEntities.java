package net.sashakyotoz.unseenworld.util;

import net.sashakyotoz.unseenworld.block.entity.BeaconOfWeaponsBlockEntity;
import net.sashakyotoz.unseenworld.block.entity.GoldenchestBlockEntity;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.Block;

import net.sashakyotoz.unseenworld.UnseenWorldMod;

public class UnseenWorldModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, UnseenWorldMod.MODID);
	public static final RegistryObject<BlockEntityType<?>> GOLDENCHEST = register("goldenchest", UnseenWorldModBlocks.GOLDENCHEST, GoldenchestBlockEntity::new);
	public static final RegistryObject<BlockEntityType<BeaconOfWeaponsBlockEntity>> BEACON_OF_WEAPONS =
			REGISTRY.register("beacon_of_weapons", () ->
					BlockEntityType.Builder.of(BeaconOfWeaponsBlockEntity::new,
							UnseenWorldModBlocks.BEACON_OF_WEAPONS.get()).build(null));

	private static RegistryObject<BlockEntityType<?>> register(String registryname, RegistryObject<Block> block, BlockEntityType.BlockEntitySupplier<?> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}
}
