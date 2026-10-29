package net.sashakyotoz.common.world.portal;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.sashakyotoz.UnseenWorld;
import net.sashakyotoz.common.items.ModItems;
import net.sashakyotoz.common.registry.ModRegistrar;

public final class ChimericPortals {
    public static final ResourceLocation PORTAL_ID = UnseenWorld.MOD.makeID("chimeric_portal");
    public static final ResourceKey<PoiType> PORTAL_POI_KEY = ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, PORTAL_ID);
    public static final int TINT = 0x000000;

    public static final Block PORTAL = ModRegistrar.register(Registries.BLOCK, PORTAL_ID, new ChimericPortalBlock(BlockBehaviour.Properties.of()
            .noCollission()
            .strength(-1.0F)
            .sound(SoundType.GLASS)
            .lightLevel(state -> 11)
            .pushReaction(PushReaction.BLOCK)
            .noLootTable()));

    public static final PoiType PORTAL_POI = ModRegistrar.register(Registries.POINT_OF_INTEREST_TYPE, PORTAL_ID,
            new PoiType(ImmutableSet.copyOf(PORTAL.getStateDefinition().getPossibleStates()), 0, 1));

    private ChimericPortals() {
    }

    public static void register() {
        UnseenWorld.log().debug("Registering Chimeric Darkness portal for modid : " + UnseenWorld.MOD_ID);
    }

    public static boolean tryLight(Level level, BlockPos pos) {
        if (!level.getBlockState(pos).isAir() && !level.getBlockState(pos).canBeReplaced()) {
            return false;
        }
        return ChimericPortalShape.findEmptyPortalShape(level, pos, Direction.Axis.X).map(shape -> {
            shape.createPortalBlocks();
            return true;
        }).orElse(false);
    }

    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        if (!stack.is(ModItems.ECLIPSE_KEYSTONE)) {
            return;
        }
        Level level = event.getLevel();
        BlockPos clicked = event.getPos();
        if (!ChimericPortalShape.isFrame(level.getBlockState(clicked))) {
            return;
        }
        Direction face = event.getFace() == null ? Direction.UP : event.getFace();
        BlockPos target = clicked.relative(face);
        if (level.isClientSide) {
            if (ChimericPortalShape.findEmptyPortalShape(level, target, Direction.Axis.X).isPresent()) {
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
            return;
        }
        if (tryLight(level, target)) {
            Player player = event.getEntity();
            level.playSound(null, target, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1.0F, 0.6F);
            level.gameEvent(player, GameEvent.BLOCK_PLACE, target);
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }
}
