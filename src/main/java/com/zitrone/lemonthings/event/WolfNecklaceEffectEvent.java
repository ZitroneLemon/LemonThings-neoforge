package com.zitrone.lemonthings.event;

import com.zitrone.lemonthings.LemonThings;
import com.zitrone.lemonthings.item.WolfTeethNecklaceItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;
import java.util.UUID;

@EventBusSubscriber(modid = LemonThings.MODID)
public class WolfNecklaceEffectEvent {

    private static final int EFFECT_DURATION = 100; // 5 секунд — обновляется каждые 40 тиков

    /**
     * Каждый тик проверяем, носит ли игрок ожерелье из волчьих зубов.
     * Если да — ищем всех прирученных волков этого игрока в радиусе 32 блоков
     * и накладываем на них эффекты: Strength II, Speed II, Resistance II.
     */
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;
        if (player.tickCount % 40 != 0) return; // проверяем каждые 40 тиков

        if (!hasWolfTeethNecklace(player)) return;

        applyEffectsToTamedWolves(player);
    }

    private static boolean hasWolfTeethNecklace(Player player) {
        var curiosInventory = CuriosApi.getCuriosInventory(player).orElse(null);
        if (curiosInventory == null) return false;

        var neckSlot = curiosInventory.getCurios().get("neck");
        if (neckSlot == null) return false;

        var stacks = neckSlot.getStacks();
        for (int i = 0; i < stacks.getSlots(); i++) {
            ItemStack stack = stacks.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof WolfTeethNecklaceItem) {
                return true;
            }
        }
        return false;
    }

    private static void applyEffectsToTamedWolves(Player player) {
        UUID playerUuid = player.getUUID();
        // Ищем всех прирученных игроком волков в радиусе 32 блока
        List<Wolf> nearbyWolves = player.level().getEntitiesOfClass(
                Wolf.class,
                player.getBoundingBox().inflate(32.0D),
                wolf -> {
                    if (!wolf.isTame()) return false;
                    UUID ownerUuid = wolf.getOwnerUUID();
                    return ownerUuid != null && ownerUuid.equals(playerUuid);
                }
        );

        for (Wolf wolf : nearbyWolves) {
            wolf.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, EFFECT_DURATION, 1, true, false));
            wolf.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, EFFECT_DURATION, 1, true, false, true));
            wolf.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, EFFECT_DURATION, 1, true, false, true));
        }
    }
}
