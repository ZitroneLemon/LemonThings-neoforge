package com.zitrone.lemonthings.event;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.zitrone.lemonthings.item.ModItems;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import top.theillusivec4.curios.api.CuriosApi;

@EventBusSubscriber
public class DoubleJumpHandler {

    // Состояние двойного прыжка: false = можно прыгнуть ещё раз
    private static final Map<UUID, Boolean> hasDoubleJumped = new HashMap<>();
    private static final Map<UUID, Boolean> wasJumpPressed = new HashMap<>();
    private static final Map<UUID, Boolean> jumpedFromGround = new HashMap<>();
    private static final Map<UUID, Integer> airJumps = new HashMap<>();

    // Кэш: есть ли у игрока вихревой заряд в слоте Curios
    // Обновляется раз в INTERVAL тиков, а не каждый тик.
    // Двойной прыжок срабатывает по нажатию — для UX задержка незаметна.
    private static final Map<UUID, Boolean> bottleCache = new HashMap<>();
    private static final Map<UUID, Integer> bottleCacheTimer = new HashMap<>();
    private static final int BOTTLE_CHECK_INTERVAL = 40;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        // Двойной прыжок — только клиентская логика
        if (!player.level().isClientSide) return;

        UUID playerId = player.getUUID();

        // Проверяем кэш наличия бутылки раз в BOTTLE_CHECK_INTERVAL тиков
        int cacheAge = bottleCacheTimer.getOrDefault(playerId, BOTTLE_CHECK_INTERVAL);
        boolean hasBottle;
        if (cacheAge >= BOTTLE_CHECK_INTERVAL) {
            hasBottle = CuriosApi.getCuriosInventory(player).map(inv ->
                    inv.findFirstCurio(ModItems.WIND_CHARGE_IN_BOTTLE.get()).isPresent()
            ).orElse(false);
            bottleCache.put(playerId, hasBottle);
            bottleCacheTimer.put(playerId, 0);
        } else {
            hasBottle = bottleCache.getOrDefault(playerId, false);
            bottleCacheTimer.put(playerId, cacheAge + 1);
        }

        if (!hasBottle) {
            resetState(playerId);
            return;
        }

        // На земле — сбрасываем всё
        if (player.onGround()) {
            resetState(playerId);
            return;
        }

        // Если уже использовали двойной прыжок — выходим
        if (hasDoubleJumped.getOrDefault(playerId, false)) return;

        // Проверяем нажатие пробела
        boolean isJumpPressed = Minecraft.getInstance().options.keyJump.isDown();
        boolean wasPressed = wasJumpPressed.getOrDefault(playerId, false);

        // Если пробел нажат только что (новое нажатие)
        if (isJumpPressed && !wasPressed) {
            int jumps = airJumps.getOrDefault(playerId, 0);

            if (jumps == 0) {
                // Первый прыжок в воздухе — запоминаем
                airJumps.put(playerId, 1);
            } else if (jumps == 1) {
                // Второй прыжок в воздухе — выполняем двойной прыжок
                performDoubleJump(player);
                hasDoubleJumped.put(playerId, true);
            }
        }

        wasJumpPressed.put(playerId, isJumpPressed);
    }

    /**
     * Очищаем состояние игрока при выходе из мира,
     * чтобы карты не накапливали мёртвые UUID.
     */
    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        UUID playerId = event.getEntity().getUUID();
        hasDoubleJumped.remove(playerId);
        wasJumpPressed.remove(playerId);
        jumpedFromGround.remove(playerId);
        airJumps.remove(playerId);
        bottleCache.remove(playerId);
        bottleCacheTimer.remove(playerId);
    }

    private static void resetState(UUID playerId) {
        hasDoubleJumped.put(playerId, false);
        wasJumpPressed.put(playerId, false);
        jumpedFromGround.put(playerId, false);
        airJumps.put(playerId, 0);
    }

    private static void performDoubleJump(Player player) {
        double jumpVelocity = 0.75;
        player.setDeltaMovement(player.getDeltaMovement().x, jumpVelocity, player.getDeltaMovement().z);
        player.fallDistance = 0;

        player.playSound(SoundEvents.WIND_CHARGE_BURST.value(), 1.0F, 1.2F);

        for (int i = 0; i < 15; i++) {
            player.level().addParticle(
                    ParticleTypes.CLOUD,
                    player.getX(), player.getY() + 0.5, player.getZ(),
                    (player.getRandom().nextDouble() - 0.5) * 0.6,
                    0.2,
                    (player.getRandom().nextDouble() - 0.5) * 0.6
            );
        }

        for (int i = 0; i < 8; i++) {
            player.level().addParticle(
                    ParticleTypes.GUST,
                    player.getX(), player.getY(), player.getZ(),
                    (player.getRandom().nextDouble() - 0.5) * 0.4,
                    0.1,
                    (player.getRandom().nextDouble() - 0.5) * 0.4
            );
        }
    }
}
