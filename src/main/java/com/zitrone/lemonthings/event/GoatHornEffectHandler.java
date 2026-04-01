package com.zitrone.lemonthings.event;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber
public class GoatHornEffectHandler {

    private static final long COOLDOWN_TIME = 2 * 60 * 1000; // 2 минуты

    private static final Map<ResourceLocation, MobEffectInstance> HORN_EFFECTS = new HashMap<>();

    static {
        HORN_EFFECTS.put(ResourceLocation.withDefaultNamespace("ponder_goat_horn"),
                new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 60, 1));
        HORN_EFFECTS.put(ResourceLocation.withDefaultNamespace("sing_goat_horn"),
                new MobEffectInstance(MobEffects.NIGHT_VISION, 20 * 60, 0));
        HORN_EFFECTS.put(ResourceLocation.withDefaultNamespace("seek_goat_horn"),
                new MobEffectInstance(MobEffects.REGENERATION, 20 * 15, 1));
        HORN_EFFECTS.put(ResourceLocation.withDefaultNamespace("feel_goat_horn"),
                new MobEffectInstance(MobEffects.ABSORPTION, 20 * 120, 3));
        HORN_EFFECTS.put(ResourceLocation.withDefaultNamespace("admire_goat_horn"),
                new MobEffectInstance(MobEffects.WATER_BREATHING, 20 * 60, 0));
        HORN_EFFECTS.put(ResourceLocation.withDefaultNamespace("call_goat_horn"),
                new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 60, 1));
        HORN_EFFECTS.put(ResourceLocation.withDefaultNamespace("yearn_goat_horn"),
                new MobEffectInstance(MobEffects.JUMP, 20 * 60, 1));
        HORN_EFFECTS.put(ResourceLocation.withDefaultNamespace("dream_goat_horn"),
                new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 60, 1));
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        ItemStack stack = event.getItemStack();

        if (stack.getItem() != Items.GOAT_HORN) {
            return;
        }

        Player player = event.getEntity();

        Holder<Instrument> instrumentHolder = stack.get(DataComponents.INSTRUMENT);
        if (instrumentHolder == null) {
            return;
        }

        ResourceLocation hornId = instrumentHolder.unwrapKey().orElse(null).location();
        if (hornId == null || !HORN_EFFECTS.containsKey(hornId)) {
            return;
        }

        // Получаем время окончания перезарядки из данных игрока
        long cooldownEndTime = player.getPersistentData().getLong("HornCooldown");
        boolean onCooldown = System.currentTimeMillis() < cooldownEndTime;

        if (!onCooldown) {
            // Применяем эффект
            player.addEffect(HORN_EFFECTS.get(hornId));
            // Сохраняем время окончания перезарядки
            player.getPersistentData().putLong("HornCooldown", System.currentTimeMillis() + COOLDOWN_TIME);
        }

        // Всегда отменяем ванильное действие рога
        event.setCanceled(true);

        // Вручную запускаем анимацию использования предмета
        player.startUsingItem(event.getHand());

        // Вручную проигрываем звук рога, следующий за игроком
        player.playSound(instrumentHolder.value().soundEvent().value(), 1.0F, 1.0F);

        // Устанавливаем ванильную перезарядку, чтобы отображалась серая полоска
        player.getCooldowns().addCooldown(stack.getItem(), (int)(COOLDOWN_TIME / 50));
    }
}