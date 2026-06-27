package com.zitrone.lemonthings.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;

public class WindChargeInBottleItem extends Item {

    public WindChargeInBottleItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            boolean equipped = CuriosApi.getCuriosInventory(player).map(inv -> {
                var slots = inv.getCurios();
                var beltSlots = slots.get("belt");
                if (beltSlots == null) return false;
                var stacks = beltSlots.getStacks();
                for (int i = 0; i < stacks.getSlots(); i++) {
                    if (stacks.getStackInSlot(i).isEmpty()) {
                        stacks.setStackInSlot(i, stack.copyWithCount(1));
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                        return true;
                    }
                }
                return false;
            }).orElse(false);

            if (equipped) {
                return InteractionResultHolder.success(stack);
            }
        }
        return InteractionResultHolder.pass(stack);
    }
}
