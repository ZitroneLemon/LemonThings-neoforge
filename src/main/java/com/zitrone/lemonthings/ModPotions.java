package com.zitrone.lemonthings;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(BuiltInRegistries.POTION, LemonThings.MODID);

    public static final DeferredHolder<Potion, Potion> WITHER_POTION = POTIONS.register("wither",
            () -> new Potion(new MobEffectInstance(MobEffects.WITHER, 900, 0)));

    public static final DeferredHolder<Potion, Potion> LONG_WITHER_POTION = POTIONS.register("long_wither",
            () -> new Potion(new MobEffectInstance(MobEffects.WITHER, 2400, 0)));

    public static final DeferredHolder<Potion, Potion> STRONG_WITHER_POTION = POTIONS.register("strong_wither",
            () -> new Potion(new MobEffectInstance(MobEffects.WITHER, 440, 1)));

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}