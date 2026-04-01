package com.zitrone.lemonthings;

import com.zitrone.lemonthings.entity.BoneArrowEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, LemonThings.MODID);

    public static final Supplier<EntityType<BoneArrowEntity>> BONE_ARROW =
            ENTITY_TYPES.register("bone_arrow",
                    () -> EntityType.Builder.<BoneArrowEntity>of(
                                    BoneArrowEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("bone_arrow"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}