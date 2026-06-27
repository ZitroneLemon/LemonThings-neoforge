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

    /**
     * Платиновый тир: характеристики между железом и алмазами.
     *  - Уровень добычи (level) как у железа = 2
     *  - Скорость добычи (speed) как у алмазов = 8
     *  - Урон (attackDamageBonus) = 2 (между железом 2 и алмазом 3)
     *  - Прочность (uses) = 905
     *  - Enchantability = 18 (между железом 14 и алмазом 10)
     */
    public static final Tier PLATINUM_TOOL = new SimpleTier(
            Tiers.IRON.getIncorrectBlocksForDrops(),
            905,
            8.0F,
            2.0F,
            18,
            () -> net.minecraft.world.item.crafting.Ingredient.of(
                    com.zitrone.lemonthings.item.ModItems.PLATINUM_INGOT.get())
    );

    /**
     * Тир ножа охотника — заточен на мясо: скорость копания низкая, прочность средняя.
     * Материал ремонта — кость (withered bone).
     */
    public static final Tier HUNTER_KNIFE = new SimpleTier(
            Tiers.STONE.getIncorrectBlocksForDrops(),
            200,
            4.0F,
            0.0F,
            14,
            () -> net.minecraft.world.item.crafting.Ingredient.of(
                    com.zitrone.lemonthings.item.ModItems.WITHERED_BONE.get())
    );
}
