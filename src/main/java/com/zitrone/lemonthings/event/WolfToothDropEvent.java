package com.zitrone.lemonthings.event;

import com.zitrone.lemonthings.LemonThings;
import com.zitrone.lemonthings.item.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

/**
 * Выпадает ли у скелета зуб волка?
 * Логика: когда скелет убит ВОЛКОМ (прямо или через снаряд, выпущенный волком),
 * с вероятностью 70% в дроп добавляется 1 Wolf Tooth.
 */
@EventBusSubscriber(modid = LemonThings.MODID)
public class WolfToothDropEvent {

    private static final float DROP_CHANCE = 0.70F; // 70%

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        LivingEntity victim = event.getEntity();
        if (!(victim instanceof Skeleton skeleton)) return;
        // Убит игроком/чем-то — не наш случай. Нужен именно волк.
        var damageSource = event.getSource();
        var directAttacker = damageSource.getEntity();

        boolean killedByWolf = false;

        // Прямая атака: волк нанёс урон
        if (directAttacker instanceof Wolf) {
            killedByWolf = true;
        }
        // Косвенно: снаряд/магия, инициированная волком
        else if (directAttacker instanceof Projectile projectile) {
            if (projectile.getOwner() instanceof Wolf) {
                killedByWolf = true;
            }
        }

        if (!killedByWolf) return;

        if (skeleton.level().random.nextFloat() <= DROP_CHANCE) {
            ItemStack stack = new ItemStack(ModItems.WOLF_TOOTH.get(), 1);
            // Спавним рядом со скелетом, чтобы сразу попало в дроп
            net.minecraft.world.entity.item.ItemEntity drop = new net.minecraft.world.entity.item.ItemEntity(
                    skeleton.level(),
                    skeleton.getX(),
                    skeleton.getY() + 0.5,
                    skeleton.getZ(),
                    stack
            );
            skeleton.level().addFreshEntity(drop);
        }
    }
}
