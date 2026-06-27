package com.zitrone.lemonthings.event;

import com.zitrone.lemonthings.item.ModItems;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

@EventBusSubscriber
public class StalactiteShieldEvent {

    /**
     * Максимальный урон, который щит может отразить назад атакующему.
     * Без этого лимита 50% от боссовского удара делало бы щит слишком OP.
     */
    private static final float MAX_REFLECT_DAMAGE = 10.0f;

    /**
     * Минимальный урон ОДНОГО удара, при котором щит «ломается» и уходит в перезарядку.
     * Срабатывает только если щит уже поднят (>= 10 тиков держания).
     */
    private static final float SHIELD_BREAK_DAMAGE = 20.0f;

    /**
     * Длительность перезарядки в тиках: 100 тиков = 5 секунд.
     * Совпадает с ванильным cooldown щита после успешного блока.
     */
    private static final int BREAK_COOLDOWN_TICKS = 100;

    /**
     * Замедленный подъём щита + поломка от сильного удара:
     * 1) Блокирует только если держится >= 10 тиков.
     * 2) Если заблокированный одиночный удар >= 20 — щит уходит в перезарядку 5 секунд.
     */
    @SubscribeEvent
    public static void onShieldBlock(LivingShieldBlockEvent event) {
        LivingEntity defender = event.getEntity();
        ItemStack activeItem = defender.getUseItem();

        if (!activeItem.is(ModItems.STALACTITE_SHIELD.get())) return;

        // Тиков держания = useDuration - remainingTicks
        int ticksHeld = activeItem.getUseDuration(defender) - defender.getUseItemRemainingTicks();

        // Блокируем только если держим >= 10 тиков (в 2 раза дольше обычного)
        if (ticksHeld < 10) {
            event.setCanceled(true);
            return;
        }

        // Урон, который щит реально собирается заблокировать
        float blockedDamage = event.getBlockedDamage();
        if (blockedDamage <= 0.0f) return;

        // Если заблокированный одиночный удар >= 20 — ломаем щит и уходим в перезарядку.
        if (blockedDamage >= SHIELD_BREAK_DAMAGE) {
            // Кулдаун действует только на игроков.
            if (defender instanceof Player player) {
                player.getCooldowns().addCooldown(activeItem.getItem(), BREAK_COOLDOWN_TICKS);
            }
            // Опускаем щит: прекращаем использование.
            defender.stopUsingItem();
        }
    }

    /**
     * Шипы: если атакующий (ближняя атака) ударил игрока с этим щитом —
     * атакующий получает 50% от заблокированного урона.
     * Работает только на ближние атаки (не дальнобойные, не магические, не взрывы).
     */
    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity defender = event.getEntity();

        // Проверяем, что защитник держит наш щит и активно блокирует
        ItemStack activeItem = defender.getUseItem();
        if (!activeItem.is(ModItems.STALACTITE_SHIELD.get())) return;

        // Проверяем, что щит уже поднят (10+ тиков)
        int ticksHeld = activeItem.getUseDuration(defender) - defender.getUseItemRemainingTicks();
        if (ticksHeld < 10) return;

        DamageSource source = event.getSource();

        // Только ближние атаки — исключаем дальнобойные, магические, взрывы, огонь и т.д.
        if (isMeleeAttack(source)) {
            LivingEntity attacker = getAttackerLiving(source);
            if (attacker != null) {
                // Анти-босс лимит: отражаем не больше 10 урона, иначе щит дисбалансирует в ПВЕ.
                float reflectDamage = Math.min(event.getAmount() * 0.5f, MAX_REFLECT_DAMAGE);
                if (reflectDamage > 0) {
                    attacker.hurt(
                            defender.level().damageSources().thorns(defender),
                            reflectDamage
                    );
                }
            }
        }
    }

    /**
     * Определяет, является ли источник урона ближней атакой.
     * Снаряд любого вида (стрела, трезубец, огненный шар и т.д.) — не ближняя атака.
     * Магия, взрывы, огонь и прочее — тоже не ближняя атака.
     * Только прямой удар существа без снаряда считается ближним.
     */
    private static boolean isMeleeAttack(DamageSource source) {
        // Любой снаряд — не ближняя атака (стрелы, огненные шары, трезубцы, ветровые заряды и т.д.)
        if (source.getDirectEntity() instanceof Projectile) return false;

        // Фильтруем урон без прямой сущности (магия, взрывы, огонь и пр.)
        if (source.is(DamageTypes.MAGIC)) return false;
        if (source.is(DamageTypes.INDIRECT_MAGIC)) return false;
        if (source.is(DamageTypes.EXPLOSION)) return false;
        if (source.is(DamageTypes.PLAYER_EXPLOSION)) return false;
        if (source.is(DamageTypes.ON_FIRE)) return false;
        if (source.is(DamageTypes.IN_FIRE)) return false;
        if (source.is(DamageTypes.LAVA)) return false;
        if (source.is(DamageTypes.LIGHTNING_BOLT)) return false;
        if (source.is(DamageTypes.FALLING_BLOCK)) return false;
        if (source.is(DamageTypes.WITHER)) return false;
        if (source.is(DamageTypes.SONIC_BOOM)) return false;

        // Прямой удар существа — ближняя атака
        return source.getDirectEntity() instanceof LivingEntity;
    }

    private static LivingEntity getAttackerLiving(DamageSource source) {
        if (source.getDirectEntity() instanceof LivingEntity living) {
            return living;
        }
        if (source.getEntity() instanceof LivingEntity living) {
            return living;
        }
        return null;
    }
}
