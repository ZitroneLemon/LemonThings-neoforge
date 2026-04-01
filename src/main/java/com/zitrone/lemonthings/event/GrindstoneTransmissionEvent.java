package com.zitrone.lemonthings.event;

import com.zitrone.lemonthings.item.TomeOfTransmissionItem;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.GrindstoneEvent;

@EventBusSubscriber
public class GrindstoneTransmissionEvent {

    // Создаёт ванильную зачарованную книгу с чарами из enchanted
    private static ItemStack createEnchantedBook(ItemStack enchanted) {
        ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.updateEnchantments(book, mutable -> {
            EnchantmentHelper.getEnchantmentsForCrafting(enchanted)
                    .entrySet()
                    .forEach(e -> mutable.set(e.getKey(), e.getValue()));
        });
        return book;
    }

    // Создаёт очищенную копию предмета
    private static ItemStack createCleanItem(ItemStack enchanted) {
        ItemStack clean = enchanted.copy();
        EnchantmentHelper.updateEnchantments(clean, mutable -> {
            EnchantmentHelper.getEnchantmentsForCrafting(enchanted)
                    .entrySet()
                    .forEach(e -> mutable.set(e.getKey(), 0));
        });
        return clean;
    }

    private static boolean isTomeSlot(ItemStack top, ItemStack bottom) {
        return (top.getItem() instanceof TomeOfTransmissionItem && !bottom.isEmpty())
                || (bottom.getItem() instanceof TomeOfTransmissionItem && !top.isEmpty());
    }

    @SubscribeEvent
    public static void onGrindstonePlace(GrindstoneEvent.OnPlaceItem event) {
        ItemStack top = event.getTopItem();
        ItemStack bottom = event.getBottomItem();

        if (!isTomeSlot(top, bottom)) return;

        ItemStack enchanted = top.getItem() instanceof TomeOfTransmissionItem ? bottom : top;

        if (EnchantmentHelper.getEnchantmentsForCrafting(enchanted).isEmpty()) return;

        // В выходном слоте показываем ванильную зачарованную книгу
        event.setOutput(createEnchantedBook(enchanted));
        event.setXp(0);
    }

    @SubscribeEvent
    public static void onGrindstoneTake(GrindstoneEvent.OnTakeItem event) {
        ItemStack top = event.getTopItem();
        ItemStack bottom = event.getBottomItem();

        if (!isTomeSlot(top, bottom)) return;

        boolean tomeIsTop = top.getItem() instanceof TomeOfTransmissionItem;
        ItemStack enchanted = tomeIsTop ? bottom : top;

        if (EnchantmentHelper.getEnchantmentsForCrafting(enchanted).isEmpty()) return;

        // Возвращаем очищенный предмет обратно в его слот
        // Том переноса исчезает (он был расходником)
        if (tomeIsTop) {
            event.setNewBottomItem(createCleanItem(enchanted));
            event.setNewTopItem(ItemStack.EMPTY); // том потрачен
        } else {
            event.setNewTopItem(createCleanItem(enchanted));
            event.setNewBottomItem(ItemStack.EMPTY); // том потрачен
        }
    }
}