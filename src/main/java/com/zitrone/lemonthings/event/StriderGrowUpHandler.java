package com.zitrone.lemonthings.event;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.zitrone.lemonthings.item.ModItems;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class StriderGrowUpHandler {

    // UUID страйдеров которые были детёнышами на прошлой проверке
    private static final Set<UUID> wasBaby = new HashSet<>();

    // Не проверяем каждый тик: достаточно раз в секунду (20 тиков),
    // чтобы поймать момент вырастания, при этом экономим массу CPU.
    private static final int CHECK_INTERVAL = 20;

    @SubscribeEvent
    public static void onLivingTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof Strider strider)) return;
        if (strider.level().isClientSide()) return;

        // Пропускаем большинство тиков — проверяем только раз в CHECK_INTERVAL
        if (strider.tickCount % CHECK_INTERVAL != 0) return;

        UUID id = strider.getUUID();

        if (strider.isBaby()) {
            // Запоминаем что он был детёнышем
            wasBaby.add(id);
        } else {
            // Только что вырос — был детёнышем, а теперь нет
            if (wasBaby.remove(id)) {
                int count = 1 + strider.getRandom().nextInt(3); // 1..3
                ItemStack scales = new ItemStack(ModItems.STRIDER_SCALE.get(), count);
                ItemEntity drop = new ItemEntity(
                        strider.level(),
                        strider.getX(),
                        strider.getY(),
                        strider.getZ(),
                        scales
                );
                strider.level().addFreshEntity(drop);
            }
        }
    }
}
