package net.sashakyotoz.unseenworld.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.sashakyotoz.unseenworld.UnseenWorldConfigs;
import net.sashakyotoz.unseenworld.registries.UnseenWorldEnchantments;

import java.util.Objects;
import java.util.function.Supplier;

public class MiningBootsAbilityMessage {
    int type, pressedms;

    public MiningBootsAbilityMessage(int type, int pressedms) {
        this.type = type;
        this.pressedms = pressedms;
    }

    public MiningBootsAbilityMessage(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
        this.pressedms = buffer.readInt();
    }

    public static void buffer(MiningBootsAbilityMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
        buffer.writeInt(message.pressedms);
    }

    public static void handler(MiningBootsAbilityMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> pressAction(Objects.requireNonNull(context.getSender()), message.type, message.pressedms));
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player, int type, int pressedms) {
        if (type == 0)
            execute(player);
    }

    private static void execute(Entity entity) {
        if (entity == null)
            return;
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        double sx, sy, sz;
        Level level = entity.level();
        if (!UnseenWorldConfigs.DEACTIVATE_MINING_BOOTS.get()) {
            if (entity instanceof LivingEntity livingEntity && livingEntity.getItemBySlot(EquipmentSlot.FEET).getEnchantmentLevel(UnseenWorldEnchantments.MININGBOOTS.get()) > 0) {
                sx = -1;
                for (int i = 0; i < 3; i++) {
                    sy = -1;
                    for (int j = 0; j < 3; j++) {
                        sz = -1;
                        for (int k = 0; k < 3; k++) {
                            if (new Object() {
                                public int getHarvestLevel(BlockState _bs) {
                                    return TierSortingRegistry.getSortedTiers().stream().filter(t -> t.getTag() != null && _bs.is(t.getTag())).map(Tier::getLevel).findFirst().orElse(0);
                                }
                            }.getHarvestLevel(level.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz))) <= 4
                                    && !(level.getBlockState(BlockPos.containing(x + sx, y + sy, z + sz)).getBlock().defaultDestroyTime() <= -1)) {
                                BlockPos pos = BlockPos.containing(x + sx, y + sy, z + sz);
                                Block.dropResources(level.getBlockState(pos), level, BlockPos.containing(x, y, z), null);
                                level.destroyBlock(pos, false);
                            }
                            sz = sz + 1;
                        }
                        sy = sy - 1;
                    }
                    sx = sx + 1;
                }
                ItemStack stack = livingEntity.getItemBySlot(EquipmentSlot.FEET);
                if (stack.hurt((int) (Mth.nextDouble(RandomSource.create(), 1, 6) - stack.getEnchantmentLevel(Enchantments.UNBREAKING)),
                        RandomSource.create(), null)) {
                    stack.shrink(1);
                    stack.setDamageValue(0);
                }
            }
        }
    }
}
