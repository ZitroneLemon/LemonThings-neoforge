package com.zitrone.lemonthings.event;

import com.zitrone.lemonthings.LemonThings;
import com.zitrone.lemonthings.item.HuntersKnifeItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@EventBusSubscriber(modid = LemonThings.MODID)
public class HuntersKnifeEvent {

    /**
     * При убийстве животного ножом охотника — дропаемое сырое мясо удваивается.
     * Удваиваем все виды сырого мяса: beef, porkchop, chicken, mutton, rabbit.
     */
    private static final Set<String> MEAT_ITEMS = new HashSet<>();

    static {
        // Сырое мясо — удваиваем
        MEAT_ITEMS.add("minecraft:beef");
        MEAT_ITEMS.add("minecraft:porkchop");
        MEAT_ITEMS.add("minecraft:chicken");
        MEAT_ITEMS.add("minecraft:mutton");
        MEAT_ITEMS.add("minecraft:rabbit");
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        // Работаем только с животными
        if (!(entity instanceof Animal)) return;

        // Должен быть убит игроком ножом охотника
        if (!(event.getSource().getEntity() instanceof Player player)) return;

        ItemStack weapon = player.getMainHandItem();
        if (!(weapon.getItem() instanceof HuntersKnifeItem)) return;

        // Для каждого дропнутого предмета, если это сырое мясо — добавляем вторую копию
        Iterator<net.minecraft.world.entity.item.ItemEntity> it = event.getDrops().iterator();
        while (it.hasNext()) {
            net.minecraft.world.entity.item.ItemEntity itemEntity = it.next();
            ItemStack dropStack = itemEntity.getItem();
            Item dropItem = dropStack.getItem();
            String itemId = BuiltInRegistries.ITEM.getKey(dropItem).toString();
            if (MEAT_ITEMS.contains(itemId)) {
                // Удваиваем: спавним копию рядом
                net.minecraft.world.entity.item.ItemEntity second = new net.minecraft.world.entity.item.ItemEntity(
                        entity.level(),
                        itemEntity.getX(),
                        itemEntity.getY(),
                        itemEntity.getZ(),
                        dropStack.copy()
                );
                entity.level().addFreshEntity(second);
            }
        }
    }
}
