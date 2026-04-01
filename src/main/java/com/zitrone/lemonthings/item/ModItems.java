package com.zitrone.lemonthings.item;

import com.zitrone.lemonthings.LemonThings;
import com.zitrone.lemonthings.ModBlocks;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BundleContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.zitrone.lemonthings.item.TomeOfTransmissionItem;


public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(LemonThings.MODID);

    public static final DeferredItem<AmethystSwordItem> AMETHYST_SWORD =
            ITEMS.register("amethyst_sword",
                    () -> new AmethystSwordItem(ModToolTiers.AMETHYST_TOOL, new Item.Properties()
                            .attributes(SwordItem.createAttributes(ModToolTiers.AMETHYST_TOOL, 3, -2.4f))
                    )
            );

    public static final DeferredItem<SimplestBowItem> SIMPLEST_BOW =
            ITEMS.register("simplest_bow",
                    () -> new SimplestBowItem(new Item.Properties()
                            .durability(182)
                            .stacksTo(1)));

    public static final DeferredItem<Item> WITHERED_BONE =
            ITEMS.register("withered_bone",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<BoneArrowItem> BONE_ARROW =
            ITEMS.register("bone_arrow",
                    () -> new BoneArrowItem(new Item.Properties().stacksTo(64)));

    public static final DeferredItem<BoneBowItem> BONE_BOW =
            ITEMS.register("bone_bow",
                    () -> new BoneBowItem(
                            new Item.Properties()
                                    .durability(384)
                                    .stacksTo(1),
                            () -> BONE_ARROW.get(),
                            () -> WITHERED_BONE.get()
                    ));

    public static final DeferredItem<FeatherPaperItem> FEATHER_PAPER =
            ITEMS.register("feather_paper",
                    () -> new FeatherPaperItem(new Item.Properties()));

    public static final DeferredItem<Item> WITHERED_BONE_MEAL =
            ITEMS.register("withered_bone_meal",
                    () -> new Item(new Item.Properties()));

    // Мешочек странствующего торговца
    public static final DeferredItem<LootBagItem> WANDERING_TRADER_BAG =
            ITEMS.register("wandering_trader_bag",
                    () -> new LootBagItem(new Item.Properties().stacksTo(16)));

    // Мешочек ведьмы
    public static final DeferredItem<LootBagItem> WITCH_BAG =
            ITEMS.register("witch_bag",
                    () -> new LootBagItem(new Item.Properties().stacksTo(16)));

    // Мешочек пиглина
    public static final DeferredItem<LootBagItem> PIGLIN_BAG =
            ITEMS.register("piglin_bag",
                    () -> new LootBagItem(new Item.Properties().stacksTo(16)));

    // Мешочек разбойника
    public static final DeferredItem<LootBagItem> PILLAGER_BAG =
            ITEMS.register("pillager_bag",
                    () -> new LootBagItem(new Item.Properties().stacksTo(16)));

    public static final DeferredItem<RavagerBagItem> RAVAGER_BAG =
            ITEMS.register("ravager_bag",
                    () -> new RavagerBagItem(new Item.Properties()
                            .stacksTo(1)
                            .component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)));

    // Платиновый слиток
    public static final DeferredItem<Item> PLATINUM_INGOT =
            ITEMS.register("platinum_ingot",
                    () -> new Item(new Item.Properties()));

    // Платиновый кусочек
    public static final DeferredItem<Item> PLATINUM_NUGGET =
            ITEMS.register("platinum_nugget",
                    () -> new Item(new Item.Properties()));

    // Кожа разорителя
    public static final DeferredItem<Item> RAVAGER_LEATHER =
            ITEMS.register("ravager_leather",
                    () -> new Item(new Item.Properties()));

    // Предмет для блока платиновой цепи (чтобы можно было поставить блок)
    public static final DeferredItem<BlockItem> PLATINUM_CHAIN_ITEM =
            ITEMS.register("platinum_chain",
                    () -> new BlockItem(ModBlocks.PLATINUM_CHAIN.get(), new Item.Properties()));

    // Предмет для платинового блока
    public static final DeferredItem<BlockItem> PLATINUM_BLOCK_ITEM =
            ITEMS.register("platinum_block",
                    () -> new BlockItem(ModBlocks.PLATINUM_BLOCK.get(), new Item.Properties()));

    public static final DeferredItem<TomeOfTransmissionItem> TOME_OF_TRANSMISSION =
            ITEMS.registerItem("tome_of_transmission",
                    props -> new TomeOfTransmissionItem(
                            new Item.Properties().stacksTo(1)
                    )
            );

    public static final DeferredItem<FlowerCrownItem> FLOWER_CROWN =
            ITEMS.registerItem("flower_crown",
                    props -> new FlowerCrownItem(
                            new Item.Properties()
                                    .stacksTo(1))
            );

    public static final DeferredItem<Item> SCULK_SPYGLASS = ITEMS.register("sculk_spyglass",
            () -> new SculkSpyglassItem(new Item.Properties()
                    .stacksTo(1)
            )
    );

    public static final DeferredItem<Item> NEEDLE = ITEMS.register("needle",
            () -> new NeedleItem(new Item.Properties()
                    .stacksTo(16)
            )
    );

    public static final DeferredItem<Item> HANDMADE_TOTEM = ITEMS.register("handmade_totem",
            () -> new HandmadeTotemItem(new Item.Properties()
                    .stacksTo(1)
            )
    );

    public static final DeferredItem<Item> REPAIR_KIT = ITEMS.register("repair_kit",
            () -> new RepairKitItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(32)
            )
    );

    public static final DeferredItem<Item> WOODEN_BUCKET = ITEMS.register("wooden_bucket",
            () -> new WoodenBucketItem(new Item.Properties()
                    .stacksTo(1)
            )
    );

    public static final DeferredItem<Item> WOODEN_WATER_BUCKET = ITEMS.register("wooden_water_bucket",
            () -> new WoodenWaterBucketItem(new Item.Properties()
                    .stacksTo(1)
            )
    );

    public static final DeferredItem<Item> COPPER_HAMMER = ITEMS.register("copper_hammer",
            () -> new CopperHammerItem(ModToolTiers.COPPER_HAMMER, new Item.Properties()
                    .attributes(PickaxeItem.createAttributes(ModToolTiers.COPPER_HAMMER, 6, -2.8F))
            )
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}