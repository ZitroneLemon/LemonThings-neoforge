package com.zitrone.lemonthings.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class LootBagItem extends Item {

    public LootBagItem(Properties properties) {
        super(properties);
    }

    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag flag) {
        tooltipComponents.add(
                Component.translatable(this.getDescriptionId() + ".tooltip")
                        .withStyle(ChatFormatting.GRAY)
        );
        tooltipComponents.add(
                Component.translatable("item.lemonthings.loot_bag.tooltip")
                        .withStyle(ChatFormatting.GRAY)
        );
    }
}