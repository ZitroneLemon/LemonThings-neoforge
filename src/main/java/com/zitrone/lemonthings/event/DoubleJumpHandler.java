package com.zitrone.lemonthings.event;

import com.zitrone.lemonthings.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber
public class DoubleJumpHandler {

    private static final Map<UUID, Boolean> hasDoubleJumped = new HashMap<>();
    private static final Map<UUID, Boolean> wasJumpPressed = new HashMap<>();
    private static final Map<UUID, Boolean> jumpedFromGround = new HashMap<>();
    private static final Map<UUID, Integer> airJumps = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        if (player.level().isClientSide) {
            UUID playerId = player.getUUID();

            // Проверяем, есть ли на поясе вихревой заряд
            boolean hasBottle = CuriosApi.getCuriosInventory(player).map(inv ->
                    inv.findFirstCurio(ModItems.WIND_CHARGE_IN_BOTTLE.get()).isPresent()
            ).orElse(false);

            if (!hasBottle) {
                hasDoubleJumped.put(playerId, false);
                wasJumpPressed.put(playerId, false);
                jumpedFromGround.put(playerId, false);
                airJumps.put(playerId, 0);
                return;
            }

            // На земле — сбрасываем всё
            if (player.onGround()) {
                hasDoubleJumped.put(playerId, false);
                wasJumpPressed.put(playerId, false);
                jumpedFromGround.put(playerId, false);
                airJumps.put(playerId, 0);
                return;
            }

            // Если уже использовали двойной прыжок — выходим
            if (hasDoubleJumped.getOrDefault(playerId, false)) return;

            // Проверяем нажатие пробела
            boolean isJumpPressed = Minecraft.getInstance().options.keyJump.isDown();
            boolean wasPressed = wasJumpPressed.getOrDefault(playerId, false);

            // Если пробел нажат только что
            if (isJumpPressed && !wasPressed) {
                int jumps = airJumps.getOrDefault(playerId, 0);

                if (jumps == 0) {
                    // Первый прыжок в воздухе — просто запоминаем
                    airJumps.put(playerId, 1);
                } else if (jumps == 1) {
                    // Второй прыжок в воздухе — двойной прыжок
                    performDoubleJump(player);
                    hasDoubleJumped.put(playerId, true);
                }
            }

            wasJumpPressed.put(playerId, isJumpPressed);
        }
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