package com.zitrone.lemonthings.event;

import com.zitrone.lemonthings.LemonThings;
import com.zitrone.lemonthings.ModPotions;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

@EventBusSubscriber(modid = LemonThings.MODID, bus = EventBusSubscriber.Bus.GAME)
public class BrewingRecipeHandler {

    @SubscribeEvent
    public static void onRegisterBrewingRecipes(RegisterBrewingRecipesEvent event) {
        var builder = event.getBuilder();

        // Получаем холдеры из реестра
        Holder<Potion> awkwardHolder = Potions.AWKWARD;
        Holder<Potion> witherHolder = BuiltInRegistries.POTION.getHolderOrThrow(ModPotions.WITHER_POTION.getKey());
        Holder<Potion> strongWitherHolder = BuiltInRegistries.POTION.getHolderOrThrow(ModPotions.STRONG_WITHER_POTION.getKey());
        Holder<Potion> longWitherHolder = BuiltInRegistries.POTION.getHolderOrThrow(ModPotions.LONG_WITHER_POTION.getKey());

        // 1. Неловкое зелье + Роза Визера = Зелье Иссушения
        builder.addMix(awkwardHolder, Items.WITHER_ROSE, witherHolder);

        // 2. Зелье Иссушения + Светокамень = Зелье Иссушения II
        builder.addMix(witherHolder, Items.GLOWSTONE_DUST, strongWitherHolder);

        // 3. Зелье Иссушения + Красная Пыль = Долгое Зелье Иссушения
        builder.addMix(witherHolder, Items.REDSTONE, longWitherHolder);

        LemonThings.LOGGER.info("Wither potion brewing recipes registered!");
    }
}