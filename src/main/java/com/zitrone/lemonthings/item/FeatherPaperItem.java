package com.zitrone.lemonthings.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class FeatherPaperItem extends Item {

    public FeatherPaperItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag flag) {
        tooltipComponents.add(
                Component.translatable("item.lemonthings.feather_paper.tooltip")
                        .withStyle(ChatFormatting.GRAY)
        );
        super.appendHoverText(stack, context, tooltipComponents, flag);
    }
}