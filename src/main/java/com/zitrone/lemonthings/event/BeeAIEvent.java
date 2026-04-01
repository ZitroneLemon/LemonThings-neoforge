package com.zitrone.lemonthings.event;

import com.zitrone.lemonthings.entity.ai.BeeFollowCrownGoal;
import net.minecraft.world.entity.animal.Bee;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber
public class BeeAIEvent {

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;

        if (event.getEntity() instanceof Bee bee) {
            // Добавляем цель с приоритетом 3
            // Меньше число = выше приоритет
            // Ванильные цели пчелы начинаются с приоритета 1
            // 3 — достаточно высокий чтобы перебить обычное патрулирование
            bee.goalSelector.addGoal(3, new BeeFollowCrownGoal(bee));
        }
    }
}