package com.zitrone.lemonthings.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.SimpleTier;

public class ModToolTiers {

    public static final Tier COPPER_HAMMER = new SimpleTier(
            Tiers.STONE.getIncorrectBlocksForDrops(),
            250,
            6.0F,
            1.0F,
            10,
            () -> Ingredient.of(Blocks.COPPER_BLOCK.asItem())
    );

    public static final Tier AMETHYST_TOOL = new SimpleTier(
            Tiers.STONE.getIncorrectBlocksForDrops(),
            250,
            6.0F,
            1.0F,
            10,
            () -> Ingredient.of(Blocks.AMETHYST_BLOCK.asItem())
    );
}