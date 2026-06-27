package com.zitrone.lemonthings.event;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber
public class GoatHornEffectHandler {

    private static final int COOLDOWN_TICKS = 900;
    private static final int EFFECT_DURATION = 600;

    private static final Map<UUID, Long> cooldownUntilTick = new HashMap<>();

    // Хранит для каждого игрока и эффекта момент (gameTime) когда он должен закончиться.
    // Если в эффекте storage.time == -1 — эффект будет удалён в этом тике.
    // Хранится потому что ванильный счётчик может застрять на 0.
    private static final Map<UUID, Map<Holder<MobEffect>, Long>> effectEndTick = new HashMap<>();

    private static final Map<ResourceLocation, HornEffect> HORN_EFFECTS = new HashMap<>();

    private record HornEffect(Holder<MobEffect> effect, int amplifier) {}

    static {
        HORN_EFFECTS.put(ResourceLocation.withDefaultNamespace("ponder_goat_horn"),
                new HornEffect(MobEffects.DAMAGE_RESISTANCE, 1));
        HORN_EFFECTS.put(ResourceLocation.withDefaultNamespace("sing_goat_horn"),
                new HornEffect(MobEffects.NIGHT_VISION, 0));
        HORN_EFFECTS.put(ResourceLocation.withDefaultNamespace("seek_goat_horn"),
                new HornEffect(MobEffects.REGENERATION, 1));
        HORN_EFFECTS.put(ResourceLocation.withDefaultNamespace("feel_goat_horn"),
                new HornEffect(MobEffects.ABSORPTION, 3));
        HORN_EFFECTS.put(ResourceLocation.withDefaultNamespace("admire_goat_horn"),
                new HornEffect(MobEffects.WATER_BREATHING, 0));
        HORN_EFFECTS.put(ResourceLocation.withDefaultNamespace("call_goat_horn"),
                new HornEffect(MobEffects.DAMAGE_BOOST, 1));
        HORN_EFFECTS.put(ResourceLocation.withDefaultNamespace("yearn_goat_horn"),
                new HornEffect(MobEffects.JUMP, 1));
        HORN_EFFECTS.put(ResourceLocation.withDefaultNamespace("dream_goat_horn"),
                new HornEffect(MobEffects.MOVEMENT_SPEED, 1));
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        // Работаем только на сервере — клиент не должен напрямую выдавать эффекты,
        // иначе они не синхронизируются обратно и тики эффектов (хилинг регенерации и т.п.)
        // не будут работать. Клиент всё равно отправит пакет использования предмета — мы его тут перехватим.
        if (event.getLevel().isClientSide()) return;

        ItemStack stack = event.getItemStack();
        if (stack.getItem() != Items.GOAT_HORN) return;
        Player player = event.getEntity();
        Holder<Instrument> instrumentHolder = stack.get(DataComponents.INSTRUMENT);
        if (instrumentHolder == null) return;
        ResourceLocation hornId = instrumentHolder.unwrapKey().orElse(null).location();
        if (hornId == null) return;
        HornEffect horn = HORN_EFFECTS.get(hornId);
        if (horn == null) return;

        long currentTick = player.level().getGameTime();
        long cooldownEnd = cooldownUntilTick.getOrDefault(player.getUUID(), 0L);
        boolean onCooldown = currentTick < cooldownEnd;

        if (!onCooldown) {
            // Перед выдачей убираем старый эффект — чистый старт без подвисшего состояния
            player.removeEffect(horn.effect());

            // Каждый раз создаём новый MobEffectInstance с явными параметрами
            MobEffectInstance instance = new MobEffectInstance(
                    horn.effect(),
                    EFFECT_DURATION,
                    horn.amplifier(),
                    true,   // ambient
                    true,   // visible (иконка в инвентаре)
                    true    // showParticles
            );
            player.addEffect(instance);

            // Запоминаем когда этот эффект должен закончиться
            effectEndTick
                    .computeIfAbsent(player.getUUID(), k -> new HashMap<>())
                    .put(horn.effect(), currentTick + EFFECT_DURATION);

            cooldownUntilTick.put(player.getUUID(), currentTick + COOLDOWN_TICKS);
        }

        event.setCanceled(true);
        player.startUsingItem(event.getHand());
        player.playSound(instrumentHolder.value().soundEvent().value(), 1.0F, 1.0F);
        player.getCooldowns().addCooldown(stack.getItem(), COOLDOWN_TICKS);
    }

    /**
     * Каждый тик проверяем — не пора ли снимать наш эффект.
     * Если в мапе записан момент окончания и он <= текущего тика —
     * принудительно снимаем эффект у этого игрока.
     */
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        UUID id = player.getUUID();
        Map<Holder<MobEffect>, Long> endMap = effectEndTick.get(id);
        if (endMap == null || endMap.isEmpty()) return;

        long currentTick = player.level().getGameTime();
        endMap.entrySet().removeIf(entry -> {
            Holder<MobEffect> effect = entry.getKey();
            long endTick = entry.getValue();

            if (currentTick >= endTick) {
                // Время вышло — принудительно снимаем эффект
                MobEffectInstance active = player.getEffect(effect);
                if (active != null) {
                    player.removeEffect(effect);
                }
                return true; // убрать из мапы
            }

            // Если эффект уже пропал сам (например, игрок умер и эффект снялся)
            // но в мапе всё ещё записан — ничего не делаем
            return false;
        });
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        UUID id = event.getEntity().getUUID();
        cooldownUntilTick.remove(id);
        effectEndTick.remove(id);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        // При респавне эффекты и так сбрасываются, чистим и наши данные
        UUID id = event.getEntity().getUUID();
        effectEndTick.remove(id);
    }
}
