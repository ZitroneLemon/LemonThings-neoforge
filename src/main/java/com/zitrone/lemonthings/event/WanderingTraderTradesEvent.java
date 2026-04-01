package com.zitrone.lemonthings.event;

import com.zitrone.lemonthings.LemonThings;
import com.zitrone.lemonthings.ModConfig;
import com.zitrone.lemonthings.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;

import javax.annotation.Nullable;
import java.util.List;

@EventBusSubscriber(modid = LemonThings.MODID, bus = EventBusSubscriber.Bus.GAME)
public class WanderingTraderTradesEvent {

    @SubscribeEvent
    public static void onWandererTrades(WandererTradesEvent event) {
        List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
        List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();

        // Починка (если включена)
        if (ModConfig.COMMON.enableMendingInWanderingTrader.get()) {
            rareTrades.add(createMendingTrade());
        }

        // Дополнительные трейды (если включены)
        if (ModConfig.COMMON.addWanderingTraderTrades.get()) {
            genericTrades.add(createEnderPearlTrade());
            genericTrades.add(createBlazeRodTrade());
            genericTrades.add(createBreezeRodTrade());
            genericTrades.add(createDiamondTrade());
            genericTrades.add(createTotemTrade());
        }

        LemonThings.LOGGER.info("Wandering trader trades registered (mending: {}, extra: {})",
                ModConfig.COMMON.enableMendingInWanderingTrader.get(),
                ModConfig.COMMON.addWanderingTraderTrades.get()
        );
    }

    private static VillagerTrades.ItemListing createMendingTrade() {
        return new VillagerTrades.ItemListing() {
            @Override
            public @Nullable MerchantOffer getOffer(net.minecraft.world.entity.Entity trader, RandomSource random) {
                ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
                Registry<net.minecraft.world.item.enchantment.Enchantment> registry =
                        trader.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT);
                Holder<net.minecraft.world.item.enchantment.Enchantment> mending =
                        registry.getHolderOrThrow(Enchantments.MENDING);
                EnchantmentHelper.updateEnchantments(enchantedBook, mutable -> mutable.set(mending, 1));

                int price = (int) Math.round(ModConfig.COMMON.mendingPrice.get());

                return new MerchantOffer(
                        new ItemCost(Items.EMERALD, price),
                        enchantedBook,
                        1,
                        5,
                        0.05f
                );
            }
        };
    }

    private static VillagerTrades.ItemListing createEnderPearlTrade() {
        return new VillagerTrades.ItemListing() {
            @Override
            public @Nullable MerchantOffer getOffer(net.minecraft.world.entity.Entity trader, RandomSource random) {
                return new MerchantOffer(
                        new ItemCost(Items.EMERALD, 5),
                        new ItemStack(Items.ENDER_PEARL, 1),
                        3,
                        3,
                        0.05f
                );
            }
        };
    }

    private static VillagerTrades.ItemListing createBlazeRodTrade() {
        return new VillagerTrades.ItemListing() {
            @Override
            public @Nullable MerchantOffer getOffer(net.minecraft.world.entity.Entity trader, RandomSource random) {
                return new MerchantOffer(
                        new ItemCost(Items.EMERALD, 8),
                        new ItemStack(Items.BLAZE_ROD, 1),
                        3,
                        4,
                        0.05f
                );
            }
        };
    }

    private static VillagerTrades.ItemListing createBreezeRodTrade() {
        return new VillagerTrades.ItemListing() {
            @Override
            public @Nullable MerchantOffer getOffer(net.minecraft.world.entity.Entity trader, RandomSource random) {
                return new MerchantOffer(
                        new ItemCost(Items.EMERALD, 10),
                        new ItemStack(Items.BREEZE_ROD, 1),
                        3,
                        4,
                        0.05f
                );
            }
        };
    }

    private static VillagerTrades.ItemListing createDiamondTrade() {
        return new VillagerTrades.ItemListing() {
            @Override
            public @Nullable MerchantOffer getOffer(net.minecraft.world.entity.Entity trader, RandomSource random) {
                return new MerchantOffer(
                        new ItemCost(Items.EMERALD, 12),
                        new ItemStack(Items.DIAMOND, 1),
                        2,
                        5,
                        0.05f
                );
            }
        };
    }

    private static VillagerTrades.ItemListing createTotemTrade() {
        return new VillagerTrades.ItemListing() {
            @Override
            public @Nullable MerchantOffer getOffer(net.minecraft.world.entity.Entity trader, RandomSource random) {
                return new MerchantOffer(
                        new ItemCost(Items.EMERALD, 32),
                        new ItemStack(Items.TOTEM_OF_UNDYING, 1),
                        1,
                        8,
                        0.05f
                );
            }
        };
    }
}