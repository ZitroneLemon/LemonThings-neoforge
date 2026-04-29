package com.zitrone.lemonthings.sound;

import com.zitrone.lemonthings.LemonThings;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, LemonThings.MODID);

    public static final Supplier<SoundEvent> COPPER_BELL_USE =
            SOUND_EVENTS.register("copper_bell_use",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(LemonThings.MODID, "copper_bell_use")
                    )
            );

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}