package com.zitrone.lemonthings.event;

import com.zitrone.lemonthings.item.HandmadeTotemItem;
import com.zitrone.lemonthings.network.TotemActivationPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber
public class HandmadeTotemEvent {

    private static final int TOTEM_EVENT_ID = 60;

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        ItemStack totem = findTotem(entity);

        if (!totem.isEmpty() && totem.getItem() instanceof HandmadeTotemItem) {
            event.setCanceled(true);

            entity.setHealth(1.0F);
            entity.clearFire();

            entity.getActiveEffects().forEach(effect -> {
                if (!effect.getEffect().value().isBeneficial()) {
                    entity.removeEffect(effect.getEffect());
                }
            });

            totem.shrink(1);

            if (!entity.level().isClientSide) {
                // Звук тотема
                entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                        SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.0F);

                // Частицы тотема
                if (entity.level() instanceof ServerLevel serverLevel) {
                    for (int i = 0; i < 60; i++) {
                        double speedX = (serverLevel.getRandom().nextDouble() - 0.5) * 2.0;
                        double speedY = (serverLevel.getRandom().nextDouble() - 0.5) * 2.0;
                        double speedZ = (serverLevel.getRandom().nextDouble() - 0.5) * 2.0;

                        serverLevel.sendParticles(
                                ParticleTypes.TOTEM_OF_UNDYING,
                                entity.getX(), entity.getY() + 0.5, entity.getZ(),
                                1, speedX, speedY, speedZ, 0.0
                        );
                    }
                }

                // Эффект уровня
                if (entity instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(new ClientboundLevelEventPacket(
                            TOTEM_EVENT_ID,
                            entity.blockPosition(),
                            0,
                            true
                    ));
                }

                // Отправляем пакет на клиент для анимации тотема
                if (entity instanceof ServerPlayer serverPlayer) {
                    PacketDistributor.sendToPlayer(serverPlayer, new TotemActivationPayload());
                }
            } else {
                // На клиенте показываем анимацию использования предмета
                Minecraft.getInstance().gameRenderer.displayItemActivation(totem);
            }
        }
    }

    private static ItemStack findTotem(LivingEntity entity) {
        ItemStack mainHand = entity.getMainHandItem();
        if (!mainHand.isEmpty() && mainHand.getItem() instanceof HandmadeTotemItem) {
            return mainHand;
        }
        ItemStack offHand = entity.getOffhandItem();
        if (!offHand.isEmpty() && offHand.getItem() instanceof HandmadeTotemItem) {
            return offHand;
        }
        return ItemStack.EMPTY;
    }
}