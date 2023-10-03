
package net.sashakyotoz.unseenworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.unseenworld.entity.DustyPinkMaxorFishEntity;
import net.sashakyotoz.unseenworld.entity.MoonfishEntity;
import net.sashakyotoz.unseenworld.entity.StrederEntity;
import net.sashakyotoz.unseenworld.init.UnseenWorldModFluids;
import net.sashakyotoz.unseenworld.init.UnseenWorldModItems;
import net.sashakyotoz.unseenworld.init.UnseenWorldModMobEffects;

public class DarkWaterBlock extends LiquidBlock {
    public DarkWaterBlock() {
        super(UnseenWorldModFluids.DARK_WATER,
                BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(100f).lightLevel(s -> 8).noCollission().noLootTable().liquid().pushReaction(PushReaction.DESTROY).sound(SoundType.EMPTY).replaceable());
    }

    @Override
    public void entityInside(BlockState p_60495_, Level p_60496_, BlockPos p_60497_, Entity entity) {
        if (!(entity instanceof MoonfishEntity || entity instanceof DustyPinkMaxorFishEntity || entity instanceof StrederEntity
                || (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == UnseenWorldModItems.NATURERIUM_ARMOR_HELMET.get()
                || (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == UnseenWorldModItems.DEEP_GEM_ARMOR_HELMET.get()
                || (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == UnseenWorldModItems.RED_TITANIUM_ARMOR_HELMET.get()
                || (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == UnseenWorldModItems.VOIDINGOT_ARMOR_HELMET.get()
                || entity instanceof LivingEntity _livEnt11 && _livEnt11.hasEffect(UnseenWorldModMobEffects.DARK_IMMUNITE.get()))) {
            entity.setDeltaMovement(new Vec3(0, 0.05, 0));
            if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
                _entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 1));
        }
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 8;
    }
}
