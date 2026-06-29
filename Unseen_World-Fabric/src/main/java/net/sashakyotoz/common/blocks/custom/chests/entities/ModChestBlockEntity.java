package net.sashakyotoz.common.blocks.custom.chests.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.client.gui.blocks.ChestScreenHandler;
import net.sashakyotoz.common.blocks.custom.chests.ModChestBlock;
import org.jetbrains.annotations.Nullable;

public class ModChestBlockEntity extends ChestBlockEntity {
    ModChestBlock.ChestTypes type;
    int viewerCount = 0;

    public ModChestBlockEntity(ModChestBlock.ChestTypes type, BlockPos pos, BlockState state) {
        super(type.getBlockEntityType(), pos, state);
        this.type = type;
        this.setItems(NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY));
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory inventory) {
        return new ChestScreenHandler(type.getScreenHandlerType(), type, syncId, inventory, ContainerLevelAccess.create(level, worldPosition));
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable(getBlockState().getBlock().getDescriptionId());
    }

    @Override
    public int getContainerSize() {
        return type.size;
    }

    public ModChestBlock.ChestTypes type() {
        return type;
    }

    @Environment(EnvType.CLIENT)
    public int countViewers() {
        return viewerCount;
    }

    public void startOpen(Player player) {
        if (!player.isSpectator()) {
            ++this.viewerCount;
            setChanged();
        }
    }

    public void stopOpen(Player player) {
        if (!player.isSpectator()) {
            --this.viewerCount;
            setChanged();
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.viewerCount = tag.getInt("viewers");
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("viewers", viewerCount);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.getLevel() != null && !this.getLevel().isClientSide() && this.getLevel() instanceof ServerLevel)
            ((ServerLevel) level).getChunkSource().blockChanged(getBlockPos());
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public float getOpenNess(float f) {
        return Mth.lerp(f, lastAnimationAngle, animationAngle);
    }

    private float animationAngle;
    private float lastAnimationAngle;

    @Environment(EnvType.CLIENT)
    public void clientTick() {
        if (level != null && level.isClientSide) {
            int viewerCount = countViewers();
            lastAnimationAngle = animationAngle;
            if (viewerCount > 0 && animationAngle == 0.0F) playSound(SoundEvents.CHEST_OPEN);
            if (viewerCount == 0 && animationAngle > 0.0F || viewerCount > 0 && animationAngle < 1.0F) {
                float float_2 = animationAngle;
                if (viewerCount > 0) animationAngle += 0.1F;
                else animationAngle -= 0.1F;
                animationAngle = Mth.clamp(animationAngle, 0, 1);
                if (animationAngle < 0.5F && float_2 >= 0.5F) playSound(SoundEvents.CHEST_CLOSE);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    private void playSound(SoundEvent soundEvent) {
        double d = (double) this.worldPosition.getX() + 0.5D;
        double e = (double) this.worldPosition.getY() + 0.5D;
        double f = (double) this.worldPosition.getZ() + 0.5D;
        if (this.level != null)
            this.level.playLocalSound(d, e, f, soundEvent, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F, false);
    }
}