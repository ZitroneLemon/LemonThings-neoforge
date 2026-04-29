package com.zitrone.lemonthings.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class MinersLollipopItem extends Item {

    public MinersLollipopItem(Properties properties) {
        super(new Item.Properties()
                .food(new FoodProperties.Builder()
                        .nutrition(2)
                        .saturationModifier(0.2F)
                        .alwaysEdible()
                        .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 60 * 20, 1), 1.0F)
                        .build()
                )
        );
    }
}