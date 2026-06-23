package net.sashakyotoz.common.blocks.custom.entities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
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
    public void readNbt(NbtCompound nbt) {
        this.ticks = nbt.getInt("ticks");
        super.readNbt(nbt);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("ticks", ticks);
        super.writeNbt(nbt);
    }

    public static void tick(World world, BlockPos pos, BlockState state, OrdealSpawnerBlockEntity entity) {
        if (state.contains(OrdealSpawnerBlock.STATE) && !state.get(OrdealSpawnerBlock.STATE).id.equals("inactive")) {
            switch (state.get(OrdealSpawnerBlock.STATE)) {
                case COOLDOWN -> {
                    if (entity.ticks > 24000)
                        entity.ticks++;
                    else {
                        entity.ticks = 0;
                        world.setBlockState(pos, state.with(OrdealSpawnerBlock.STATE, OrdealSpawnerState.INACTIVE), 3);
                        world.playSound(null, pos, ModSoundEvents.ORDEAL_SPAWNER_REWARDING, SoundCategory.BLOCKS, 0.75F, 1.75F);
                    }
                }
                case EJECTING_REWARD -> {
                    if (entity.ticks > 100)
                        entity.ticks++;
                    else {
                        entity.ticks = 0;
                        var lootTableId = UnseenWorld.makeID("chests/ordeal_spawner_%s"
                                .formatted(state.get(OrdealSpawnerBlock.TYPE).name));
                        if (world instanceof ServerWorld serverWorld) {
                            var lootTable = serverWorld.getServer().getLootManager()
                                    .getLootTable(lootTableId);

                            var lootParams = new LootContextParameterSet.Builder(serverWorld)
                                    .add(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos))
                                    .add(LootContextParameters.BLOCK_STATE, state)
                                    .add(LootContextParameters.BLOCK_ENTITY, entity)
                                    .build(LootContextTypes.CHEST);
                            List<ItemStack> generatedLoot = lootTable.generateLoot(lootParams);

                            for (ItemStack stack : generatedLoot)
                                Block.dropStack(world, pos, stack);
                            world.setBlockState(pos, state.with(OrdealSpawnerBlock.STATE, OrdealSpawnerState.COOLDOWN), 3);
                            world.playSound(null, pos, ModSoundEvents.ORDEAL_SPAWNER_REWARDING, SoundCategory.BLOCKS, 1.0F, 0.75F);
                        }
                    }
                }
                case WAITING_FOR_PLAYERS -> {
                    if (world.getPlayers().stream().anyMatch(player ->
                            !player.isSpectator()
                                    && !player.isDead()
                                    && !player.isCreative()
                                    && player.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ()) <= 9
                    )) {
                        if (entity.ticks < 300) {
                            entity.ticks++;
                            if (entity.ticks % 20 == 0)
                                world.playSound(null, pos, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.BLOCKS, 0.75F, 1.75F);
                        } else {
                            world.setBlockState(pos, state.with(OrdealSpawnerBlock.STATE, OrdealSpawnerState.ACTIVE), 3);
                            world.playSound(null, pos, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.BLOCKS, 1.25F, 0.9F);
                        }
                    }
                }
            }
        }
    }

    private static void createEntity(World world, BlockPos pos, BlockState state) {
        if (!world.isClient() && world instanceof ServerWorld serverWorld) {
            int amount = serverWorld.random.nextInt(4) + 2;
            for (int i = 0; i < amount; i++) {
                int randomE = serverWorld.getRandom().nextInt(3);
                PathAwareEntity entity;
                switch (state.get(OrdealSpawnerBlock.TYPE)) {
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