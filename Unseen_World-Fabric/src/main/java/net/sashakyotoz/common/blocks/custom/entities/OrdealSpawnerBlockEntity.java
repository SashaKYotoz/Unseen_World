package net.sashakyotoz.common.blocks.custom.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.ModSoundEvents;
import net.sashakyotoz.common.blocks.ModBlockEntities;
import net.sashakyotoz.common.blocks.custom.OrdealSpawnerBlock;
import net.sashakyotoz.common.blocks.custom.states.OrdealSpawnerState;
import net.sashakyotoz.common.entities.ModEntities;

import java.util.List;

public class OrdealSpawnerBlockEntity extends BlockEntity {
    public int ticks = 0;

    public OrdealSpawnerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ORDEAL_SPAWNER, pos, state);
    }

    @Override
    public void load(CompoundTag nbt) {
        this.ticks = nbt.getInt("ticks");
        super.load(nbt);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putInt("ticks", ticks);
        super.saveAdditional(nbt);
    }

    public static void tick(Level world, BlockPos pos, BlockState state, OrdealSpawnerBlockEntity entity) {
        if (state.hasProperty(OrdealSpawnerBlock.STATE) && !state.getValue(OrdealSpawnerBlock.STATE).id.equals("inactive")) {
            switch (state.getValue(OrdealSpawnerBlock.STATE)) {
                case COOLDOWN -> {
                    if (entity.ticks > 24000)
                        entity.ticks++;
                    else {
                        entity.ticks = 0;
                        world.setBlock(pos, state.setValue(OrdealSpawnerBlock.STATE, OrdealSpawnerState.INACTIVE), 3);
                        world.playSound(null, pos, ModSoundEvents.ORDEAL_SPAWNER_REWARDING, SoundSource.BLOCKS, 0.75F, 1.75F);
                    }
                }
                case EJECTING_REWARD -> {
                    if (entity.ticks > 100)
                        entity.ticks++;
                    else {
                        entity.ticks = 0;
                        var lootTableId = UnseenWorld.makeID("chests/ordeal_spawner_%s"
                                .formatted(state.getValue(OrdealSpawnerBlock.TYPE).name));
                        if (world instanceof ServerLevel serverWorld) {
                            var lootTable = serverWorld.getServer().getLootData()
                                    .getLootTable(lootTableId);

                            var lootParams = new LootParams.Builder(serverWorld)
                                    .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                                    .withParameter(LootContextParams.BLOCK_STATE, state)
                                    .withParameter(LootContextParams.BLOCK_ENTITY, entity)
                                    .create(LootContextParamSets.CHEST);
                            List<ItemStack> generatedLoot = lootTable.getRandomItems(lootParams);

                            for (ItemStack stack : generatedLoot)
                                Block.popResource(world, pos, stack);
                            world.setBlock(pos, state.setValue(OrdealSpawnerBlock.STATE, OrdealSpawnerState.COOLDOWN), 3);
                            world.playSound(null, pos, ModSoundEvents.ORDEAL_SPAWNER_REWARDING, SoundSource.BLOCKS, 1.0F, 0.75F);
                        }
                    }
                }
                case WAITING_FOR_PLAYERS -> {
                    if (world.players().stream().anyMatch(player ->
                            !player.isSpectator()
                                    && !player.isDeadOrDying()
                                    && !player.isCreative()
                                    && player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) <= 9
                    )) {
                        if (entity.ticks < 300) {
                            entity.ticks++;
                            if (entity.ticks % 20 == 0)
                                world.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 0.75F, 1.75F);
                        } else {
                            world.setBlock(pos, state.setValue(OrdealSpawnerBlock.STATE, OrdealSpawnerState.ACTIVE), 3);
                            world.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.25F, 0.9F);
                        }
                    }
                }
            }
        }
    }

    private static void createEntity(Level world, BlockPos pos, BlockState state) {
        if (!world.isClientSide() && world instanceof ServerLevel serverWorld) {
            int amount = serverWorld.random.nextInt(4) + 2;
            for (int i = 0; i < amount; i++) {
                int randomE = serverWorld.getRandom().nextInt(3);
                PathfinderMob entity;
                switch (state.getValue(OrdealSpawnerBlock.TYPE)) {
                    case GRIPCRYSTAL -> {
                        switch (randomE) {
                            case 0 -> entity = ModEntities.ESPYER.create(world);
                            case 1 -> entity = ModEntities.ELDRITCH_WATCHER.create(world);
                        }
                    }
                }
            }
        }
    }
}