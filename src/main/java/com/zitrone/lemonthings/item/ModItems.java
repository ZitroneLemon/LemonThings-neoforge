package com.zitrone.lemonthings.item;

import java.util.List;
import java.util.Map;

import com.zitrone.lemonthings.LemonThings;
import com.zitrone.lemonthings.block.ModBlocks;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;


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

    // Мешочек пиглинa
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

    public static final DeferredItem<Item> BERRY_ROLL = ITEMS.register("berry_roll",
            () -> new BerryRollItem()
    );

    public static final DeferredItem<Item> MINERS_LOLLIPOP = ITEMS.register("miners_lollipop",
            () -> new MinersLollipopItem(new Item.Properties().stacksTo(16))
    );

    public static final DeferredItem<BlockItem> COPPER_BELL = ITEMS.register("copper_bell",
            () -> new BlockItem(ModBlocks.COPPER_BELL.get(), new Item.Properties())
    );

    public static final DeferredItem<Item> WIND_CHARGE_IN_BOTTLE = ITEMS.register("wind_charge_in_bottle",
            () -> new WindChargeInBottleItem(new Item.Properties().stacksTo(1).fireResistant())
    );

    // Сталактитовый щит
    public static final DeferredItem<StalactiteShieldItem> STALACTITE_SHIELD = ITEMS.register("stalactite_shield",
            () -> new StalactiteShieldItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(StalactiteShieldItem.DURABILITY)
            )
    );

    // Чешуйка страйдера
    public static final DeferredItem<StriderScaleItem> STRIDER_SCALE = ITEMS.register("strider_scale",
            () -> new StriderScaleItem(new Item.Properties().stacksTo(64))
    );

    // Материал брони страйдера
    private static Holder<ArmorMaterial> striderMaterial() {
        return Holder.direct(new ArmorMaterial(
                Map.of(
                        ArmorItem.Type.HELMET, 0,
                        ArmorItem.Type.CHESTPLATE, 0,
                        ArmorItem.Type.LEGGINGS, 0,
                        ArmorItem.Type.BOOTS, 2
                ),
                9,
                SoundEvents.ARMOR_EQUIP_IRON,
                () -> Ingredient.of(STRIDER_SCALE.get()),
                List.of(new ArmorMaterial.Layer(
                        ResourceLocation.fromNamespaceAndPath(LemonThings.MODID, "strider")
                )),
                0.0f,
                0.0f
        ));
    }

    // Ботинки страйдера
    public static final DeferredItem<StriderBootsItem> STRIDER_BOOTS = ITEMS.register("strider_boots",
            () -> new StriderBootsItem(striderMaterial(), new Item.Properties()
                    .stacksTo(1)
                    .durability(195)
                    .fireResistant()
            )
    );

    // ====================== ПЛАТИНОВЫЕ ПРЕДМЕТЫ ======================

    // Платиновый меч
    public static final DeferredItem<SwordItem> PLATINUM_SWORD = ITEMS.register("platinum_sword",
            () -> new SwordItem(ModToolTiers.PLATINUM_TOOL, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModToolTiers.PLATINUM_TOOL, 3, -2.4F))
            )
    );

    // Платиновая кирка
    public static final DeferredItem<PickaxeItem> PLATINUM_PICKAXE = ITEMS.register("platinum_pickaxe",
            () -> new PickaxeItem(ModToolTiers.PLATINUM_TOOL, new Item.Properties()
                    .attributes(PickaxeItem.createAttributes(ModToolTiers.PLATINUM_TOOL, 1, -2.8F))
            )
    );

    // Платиновый топор
    public static final DeferredItem<AxeItem> PLATINUM_AXE = ITEMS.register("platinum_axe",
            () -> new AxeItem(ModToolTiers.PLATINUM_TOOL, new Item.Properties()
                    .attributes(AxeItem.createAttributes(ModToolTiers.PLATINUM_TOOL, 5, -3.0F))
            )
    );

    // Платиновая лопата
    public static final DeferredItem<ShovelItem> PLATINUM_SHOVEL = ITEMS.register("platinum_shovel",
            () -> new ShovelItem(ModToolTiers.PLATINUM_TOOL, new Item.Properties()
                    .attributes(ShovelItem.createAttributes(ModToolTiers.PLATINUM_TOOL, 1.5F, -3.0F))
            )
    );

    // Платиновая мотыга
    public static final DeferredItem<HoeItem> PLATINUM_HOE = ITEMS.register("platinum_hoe",
            () -> new HoeItem(ModToolTiers.PLATINUM_TOOL, new Item.Properties()
                    .attributes(HoeItem.createAttributes(ModToolTiers.PLATINUM_TOOL, -1.0F, 0.0F))
            )
    );

    // Материал платиновой брони.
    // Защита (по слотам): шлем=2, нагрудник=7, поножи=5, ботинки=2
    // Твёрдость = 1.0 (как у алмазной брони)
    private static Holder<ArmorMaterial> platinumMaterial() {
        return Holder.direct(new ArmorMaterial(
                Map.of(
                        ArmorItem.Type.HELMET, 2,
                        ArmorItem.Type.CHESTPLATE, 7,
                        ArmorItem.Type.LEGGINGS, 5,
                        ArmorItem.Type.BOOTS, 2
                ),
                18,                                    // enchantability
                SoundEvents.ARMOR_EQUIP_IRON,
                () -> Ingredient.of(PLATINUM_INGOT.get()),
                List.of(new ArmorMaterial.Layer(
                        ResourceLocation.fromNamespaceAndPath(LemonThings.MODID, "platinumarmor")
                )),
                1.0f,                                  // toughness
                0.0f                                   // knockback resistance
        ));
    }

    // Платиновый шлем (прочность 264)
    public static final DeferredItem<ArmorItem> PLATINUM_HELMET = ITEMS.register("platinum_helmet",
            () -> new ArmorItem(platinumMaterial(), ArmorItem.Type.HELMET, new Item.Properties()
                    .durability(264)
            )
    );

    // Платиновый нагрудник (прочность 384)
    public static final DeferredItem<ArmorItem> PLATINUM_CHESTPLATE = ITEMS.register("platinum_chestplate",
            () -> new ArmorItem(platinumMaterial(), ArmorItem.Type.CHESTPLATE, new Item.Properties()
                    .durability(384)
            )
    );

    // Платиновые поножи (прочность 360)
    public static final DeferredItem<ArmorItem> PLATINUM_LEGGINGS = ITEMS.register("platinum_leggings",
            () -> new ArmorItem(platinumMaterial(), ArmorItem.Type.LEGGINGS, new Item.Properties()
                    .durability(360)
            )
    );

    // Платиновые ботинки (прочность 312)
    public static final DeferredItem<ArmorItem> PLATINUM_BOOTS = ITEMS.register("platinum_boots",
            () -> new ArmorItem(platinumMaterial(), ArmorItem.Type.BOOTS, new Item.Properties()
                    .durability(312)
            )
    );

    // ====================== ВОЛЧЬИ ЗУБЫ И ОХОТНИЧЬЕ СНАРЯЖЕНИЕ ======================

    // Зуб волка — выпадает со скелета-волка (Alex's Mobs: Skelewag)
    public static final DeferredItem<Item> WOLF_TOOTH =
            ITEMS.register("wolf_tooth",
                    () -> new WolfToothItem(new Item.Properties()));

    // Нож охотника — режет скот, добывая двойное мясо
    public static final DeferredItem<Item> HUNTERS_KNIFE =
            ITEMS.register("hunters_knife",
                    () -> new HuntersKnifeItem(ModToolTiers.HUNTER_KNIFE, new Item.Properties()
                            .attributes(SwordItem.createAttributes(ModToolTiers.HUNTER_KNIFE, 4, -2.0F))
                    ));

    // Ожерелье из волчьих зубов — Curios-слот "neck", усиливает прирученных волков
    public static final DeferredItem<Item> WOLF_TEETH_NECKLACE =
            ITEMS.register("wolf_teeth_necklace",
                    () -> new WolfTeethNecklaceItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
