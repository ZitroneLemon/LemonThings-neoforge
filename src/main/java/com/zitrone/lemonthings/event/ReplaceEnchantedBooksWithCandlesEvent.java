package com.zitrone.lemonthings.event;

import com.zitrone.lemonthings.LemonThings;
import com.zitrone.lemonthings.ModConfig;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

@EventBusSubscriber(modid = LemonThings.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ReplaceEnchantedBooksWithCandlesEvent {

    private static final Random RANDOM = new Random();

    private static final ItemStack[] COLORED_CANDLES = {
            new ItemStack(Items.CANDLE),
            new ItemStack(Items.BLACK_CANDLE),
            new ItemStack(Items.BLUE_CANDLE),
            new ItemStack(Items.BROWN_CANDLE),
            new ItemStack(Items.CYAN_CANDLE),
            new ItemStack(Items.GRAY_CANDLE),
            new ItemStack(Items.GREEN_CANDLE),
            new ItemStack(Items.LIGHT_BLUE_CANDLE),
            new ItemStack(Items.LIGHT_GRAY_CANDLE),
            new ItemStack(Items.LIME_CANDLE),
            new ItemStack(Items.MAGENTA_CANDLE),
            new ItemStack(Items.ORANGE_CANDLE),
            new ItemStack(Items.PINK_CANDLE),
            new ItemStack(Items.PURPLE_CANDLE),
            new ItemStack(Items.RED_CANDLE),
            new ItemStack(Items.WHITE_CANDLE),
            new ItemStack(Items.YELLOW_CANDLE)
    };

    @SubscribeEvent
    public static void onVillagerTrades(VillagerTradesEvent event) {
        // Проверяем, что включено в конфиге
        if (!ModConfig.COMMON.replaceEnchantedBooksWithCandles.get()) return;

        // Проверяем, что это библиотекарь
        if (event.getType() == VillagerProfession.LIBRARIAN) {

            for (int level = 1; level <= 5; level++) {
                List<VillagerTrades.ItemListing> trades = event.getTrades().get(level);
                if (trades == null) continue;

                for (int i = 0; i < trades.size(); i++) {
                    VillagerTrades.ItemListing trade = trades.get(i);
                    if (trade == null) continue;

                    if (isEnchantedBookTrade(trade)) {
                        trades.set(i, createCandleTrade());
                        LemonThings.LOGGER.info("Replaced enchanted book trade with candles at level " + level);
                    }
                }
            }
        }
    }

    private static boolean isEnchantedBookTrade(VillagerTrades.ItemListing trade) {
        String tradeClassName = trade.getClass().getSimpleName();
        return tradeClassName.contains("EnchantBook") ||
                tradeClassName.contains("EnchantedBook") ||
                tradeClassName.contains("EnchantmentBook");
    }

    private static VillagerTrades.ItemListing createCandleTrade() {
        return new VillagerTrades.ItemListing() {
            @Override
            public @Nullable MerchantOffer getOffer(net.minecraft.world.entity.Entity trader, net.minecraft.util.RandomSource random) {
                ItemStack candles = COLORED_CANDLES[RANDOM.nextInt(COLORED_CANDLES.length)].copy();
                candles.setCount(4);

                return new MerchantOffer(
                        new ItemCost(Items.EMERALD, 1),
                        candles,
                        12,
                        2,
                        0.05f
                );
            }
        };
    }
}