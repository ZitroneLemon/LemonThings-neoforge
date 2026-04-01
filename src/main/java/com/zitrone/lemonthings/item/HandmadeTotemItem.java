package com.zitrone.lemonthings.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class HandmadeTotemItem extends Item {

    public HandmadeTotemItem(Properties properties) {
        super(properties);
    }

    public boolean canBeDepleted() {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.lemonthings.handmade_totem.tooltip")
                .withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltip, flag);
    }

    public static boolean canSave(LivingEntity entity, ItemStack stack) {
        return entity instanceof Player && !entity.level().isClientSide;
    }

    public static void save(LivingEntity entity, ItemStack stack) {
        if (entity instanceof Player player) {
            // Восстанавливаем 1 сердце
            player.setHealth(1.0F);
            // Расходуем тотем
            stack.shrink(1);
            // Снимаем огонь
            player.setSharedFlagOnFire(false);
        }
    }
}