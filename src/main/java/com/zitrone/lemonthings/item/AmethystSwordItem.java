package com.zitrone.lemonthings.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class AmethystSwordItem extends SwordItem {

    public AmethystSwordItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return repairCandidate.is(Blocks.AMETHYST_BLOCK.asItem());
    }

    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents, TooltipFlag flag) {
        tooltipComponents.add(
                Component.translatable("item.lemonthings.amethyst_sword.tooltip")
                        .withStyle(ChatFormatting.GRAY)
        );
    }
}