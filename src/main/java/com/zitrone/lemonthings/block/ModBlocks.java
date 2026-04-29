package com.zitrone.lemonthings.block;

import com.zitrone.lemonthings.LemonThings;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import static net.minecraft.world.item.Items.registerBlock;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(LemonThings.MODID);

    public static final DeferredBlock<PlatinumChainBlock> PLATINUM_CHAIN =
            BLOCKS.register("platinum_chain",
                    () -> new PlatinumChainBlock(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .sound(SoundType.CHAIN)
                            .strength(5.0F, 6.0F)  // Как у обычной цепи
                            .noOcclusion()
                            .requiresCorrectToolForDrops()));  // Требует правильный инструмент

    // Платиновый блок
    public static final DeferredBlock<Block> PLATINUM_BLOCK =
            BLOCKS.register("platinum_block",
                    () -> new Block(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .sound(SoundType.METAL)
                            .strength(5.0F, 6.0F)
                            .requiresCorrectToolForDrops()));

    public static final DeferredBlock<CopperBellBlock> COPPER_BELL = BLOCKS.register("copper_bell",
            () -> new CopperBellBlock(BlockBehaviour.Properties.of()
                    .strength(5.0F)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()
            )
    );

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}