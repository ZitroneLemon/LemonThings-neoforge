package com.zitrone.lemonthings.event;

import com.zitrone.lemonthings.item.StriderBootsItem;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class LavaWalkerHandler {

    private static boolean wearingBoots(Player player) {
        return player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof StriderBootsItem;
    }

    /**
     * Длительность эффекта огнестойкости — 200 тиков (10 секунд).
     * Это много, благодаря чему мы добавляем эффект значительно реже.
     */
    private static final int FIRE_RES_DURATION = 200;

    /**
     * Минимальный остаток длительности, при котором мы обновляем эффект.
     * Если осталось больше этого значения — пропускаем addEffect.
     * Так обновление происходит раз в ~5 секунд, а не каждый тик.
     */
    private static final int REFRESH_THRESHOLD = 100;

    /**
     * Каждый тик пока игрок в лаве и на нём ботинки:
     * - гасим огонь
     * - убираем скорость погружения (игрок "плавает" по поверхности лавы)
     * - выдаём огнестойкость РЕДКО (раз в ~5 сек), а не каждый тик
     */
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        if (!wearingBoots(player)) return;

        boolean eyeInLava = player.isEyeInFluid(FluidTags.LAVA);

        if (player.isInLava()) {
            // Гасим огонь только если не полностью погружён
            if (!eyeInLava) {
                player.clearFire();
            }
            // Толкаем игрока вверх чтобы он не тонул — как поплавок
            if (player.getDeltaMovement().y < 0.05) {
                player.setDeltaMovement(
                        player.getDeltaMovement().x,
                        0.05,
                        player.getDeltaMovement().z
                );
            }
        }

        // Огнестойкость только если НЕ полностью погружён в лаву
        if (!eyeInLava) {
            BlockPos feetPos = player.blockPosition().below();
            Block blockBelow = player.level().getBlockState(feetPos).getBlock();
            boolean onHotSurface = player.isInLava()
                    || blockBelow == Blocks.LAVA
                    || blockBelow == Blocks.MAGMA_BLOCK;

            if (onHotSurface) {
                // Не дёргаем addEffect каждый тик — добавим только если эффект
                // отсутствует или вот-вот закончится. Длительность 200 тиков
                // покрывает разрыв между обновлениями.
                MobEffectInstance current = player.getEffect(MobEffects.FIRE_RESISTANCE);
                if (current == null || current.getDuration() <= REFRESH_THRESHOLD) {
                    player.addEffect(new MobEffectInstance(
                            MobEffects.FIRE_RESISTANCE,
                            FIRE_RES_DURATION,
                            0,
                            false,
                            false
                    ));
                }
            }
        } else {
            // Полностью погружён — убираем огнестойкость если она была от ботинок
            player.removeEffect(MobEffects.FIRE_RESISTANCE);
        }
    }

    /**
     * Снижаем урон от лавы и огня только если игрок НЕ полностью погружён —
     * то есть глаза не в лаве. Если игрок ушёл под лаву — урон идёт как обычно.
     */
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!wearingBoots(player)) return;

        // Если глаза в лаве — игрок полностью погружён, не защищаем
        if (player.isEyeInFluid(FluidTags.LAVA)) return;

        DamageSource source = event.getSource();
        if (source.is(DamageTypes.IN_FIRE)
                || source.is(DamageTypes.ON_FIRE)
                || source.is(DamageTypes.LAVA)
                || source.is(DamageTypes.HOT_FLOOR)) {
            event.setNewDamage(0f);
        }
    }
}
