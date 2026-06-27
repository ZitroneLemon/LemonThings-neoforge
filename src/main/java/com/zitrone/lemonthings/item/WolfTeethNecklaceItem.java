package com.zitrone.lemonthings.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;

/**
 * Ожерелье из волчьих зубов. Надевается в Curios-слот "neck".
 * Усиливает прирученных волков игрока в радиусе 32 блоков:
 * Strength II, Speed II, Resistance II.
 */
public class WolfTeethNecklaceItem extends Item {

    public WolfTeethNecklaceItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            boolean equipped = CuriosApi.getCuriosInventory(player).map(inv -> {
                var slots = inv.getCurios();
                var neckSlots = slots.get("neck");
                if (neckSlots == null) return false;
                var stacks = neckSlots.getStacks();
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

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.lemonthings.wolf_teeth_necklace.tooltip")
                .withStyle(ChatFormatting.GRAY));
    }
}
