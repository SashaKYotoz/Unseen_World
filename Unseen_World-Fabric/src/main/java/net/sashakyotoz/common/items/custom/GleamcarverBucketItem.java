package net.sashakyotoz.common.items.custom;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sashakyotoz.common.entities.ModEntities;
import net.sashakyotoz.common.entities.custom.GleamcarverEntity;

public class GleamcarverBucketItem extends Item {
    public GleamcarverBucketItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }
        ItemStack stack = context.getItemInHand();
        BlockPos clicked = context.getClickedPos();
        Direction face = context.getClickedFace();
        BlockState state = level.getBlockState(clicked);
        BlockPos target = state.getCollisionShape(level, clicked).isEmpty() ? clicked : clicked.relative(face);
        if (!level.getBlockState(target).getCollisionShape(level, target).isEmpty()) {
            return InteractionResult.FAIL;
        }
        Player player = context.getPlayer();
        GleamcarverEntity gleamcarver = ModEntities.GLEAMCARVER.spawn(serverLevel, stack, player, target, MobSpawnType.BUCKET, true,
                !clicked.equals(target) && face == Direction.UP);
        if (gleamcarver == null) {
            return InteractionResult.FAIL;
        }
        gleamcarver.loadFromBucketTag(stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY).copyTag());
        gleamcarver.setFromBucket(true);
        level.playSound(null, target, SoundEvents.BUCKET_EMPTY_POWDER_SNOW, SoundSource.NEUTRAL, 1.0F, 1.0F);
        level.gameEvent(player, GameEvent.ENTITY_PLACE, target);
        if (player != null) {
            player.setItemInHand(context.getHand(), BucketItem.getEmptySuccessItem(stack, player));
        }
        return InteractionResult.CONSUME;
    }
}