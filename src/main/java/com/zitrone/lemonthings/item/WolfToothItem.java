package com.zitrone.lemonthings.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * Зуб волка — выпадает, когда волк убивает скелета (шанс 70%).
 * Используется в крафте ножа охотника и ожерелья из волчьих зубов.
 */
public class WolfToothItem extends Item {

    public WolfToothItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.lemonthings.wolf_tooth.tooltip")
                .withStyle(ChatFormatting.GRAY));
    }
}
