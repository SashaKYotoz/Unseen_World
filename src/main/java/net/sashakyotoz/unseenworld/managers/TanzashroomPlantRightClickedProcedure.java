package net.sashakyotoz.unseenworld.managers;

import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;

public class TanzashroomPlantRightClickedProcedure {
    public static void execute(LevelAccessor world, double x,double y, double z, Player player) {
        if (player == null)
            return;
        if (player.getMainHandItem().is(Items.BONE_MEAL) || player.getOffhandItem().is(Items.BONE_MEAL)) {
            ItemStack stack = new ItemStack(Items.BONE_MEAL);
            player.getInventory().clearOrCountMatchingItems(p -> stack.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
            if (Math.random() < 0.25) {
                if (world instanceof ServerLevel level) {
                    StructureTemplate template = level.getStructureManager().getOrCreate(new ResourceLocation("unseen_world", "tanzashroom_big_mushroom"));
                    if (template != null) {
                        template.placeInWorld(level, BlockPos.containing(x - 3, y, z - 3), BlockPos.containing(x - 3, y, z - 3), new StructurePlaceSettings().setRotation(Rotation.NONE).setMirror(Mirror.NONE).setIgnoreEntities(false),
                                level.random, 3);
                    }
                }
            }
        } else if (player.getMainHandItem().is(Items.WARPED_FUNGUS_ON_A_STICK)) {
            player.getMainHandItem().getOrCreateTag().putDouble("CustomModelData", 1);
            player.getMainHandItem().setHoverName(Component.literal("Tanzashroom on a stick"));
            world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
        }
    }
}
