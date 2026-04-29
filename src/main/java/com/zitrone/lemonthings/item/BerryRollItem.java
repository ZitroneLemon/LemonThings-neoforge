package com.zitrone.lemonthings.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class BerryRollItem extends Item {

    public BerryRollItem() {
        super(new Item.Properties()
                .food(new FoodProperties.Builder()
                        .nutrition(5)
                        .saturationModifier(0.6F)
                        .build()
                )
        );
    }
}