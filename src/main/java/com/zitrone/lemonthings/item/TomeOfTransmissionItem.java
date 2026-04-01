package com.zitrone.lemonthings.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.List;

public class TomeOfTransmissionItem extends EnchantedBookItem {

    public TomeOfTransmissionItem(Properties properties) {
        super(properties);
    }

    // Проверяем — том уже содержит чары?
    public static boolean hasEnchantments(ItemStack stack) {
        return !EnchantmentHelper.getEnchantmentsForCrafting(stack).isEmpty();
    }

    public static ItemStack absorbEnchantments(ItemStack tome, ItemStack source) {
        ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(source);
        if (enchantments.isEmpty()) return source;

        // Переносим чары на том
        EnchantmentHelper.updateEnchantments(tome, mutable -> {
            enchantments.entrySet().forEach(entry ->
                    mutable.set(entry.getKey(), entry.getValue())
            );
        });

        // Очищаем чары с источника — заменяем на пустой список
        EnchantmentHelper.updateEnchantments(source, mutable -> {
            for (var entry : enchantments.entrySet()) {
                mutable.set(entry.getKey(), 0);
            }
        });

        return source;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(
                Component.translatable("item.lemonthings.tome_of_transmission.tooltip")
                        .withStyle(ChatFormatting.GRAY)
        );
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public boolean canGrindstoneRepair(ItemStack stack) {
        return true;
    }
}