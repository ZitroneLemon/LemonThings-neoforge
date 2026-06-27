package com.zitrone.lemonthings.item;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;

public class StalactiteShieldItem extends ShieldItem {

    // Обычный щит: 336 прочности. +20% = ~403
    public static final int DURABILITY = 403;

    public StalactiteShieldItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag flag) {
        tooltipComponents.add(
                Component.translatable("item.lemonthings.stalactite_shield.tooltip1")
                        .withStyle(ChatFormatting.GRAY)
        );
    }
}
