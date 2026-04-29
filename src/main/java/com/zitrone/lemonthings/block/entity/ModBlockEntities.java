package com.zitrone.lemonthings.block.entity;

import com.zitrone.lemonthings.LemonThings;
import com.zitrone.lemonthings.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, LemonThings.MODID);

    public static final Supplier<BlockEntityType<CopperBellBlockEntity>> COPPER_BELL =
            BLOCK_ENTITIES.register("copper_bell",
                    () -> BlockEntityType.Builder.of(CopperBellBlockEntity::new, ModBlocks.COPPER_BELL.get()).build(null));

    public static void register(net.neoforged.bus.api.IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

}