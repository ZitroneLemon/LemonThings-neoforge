package com.zitrone.lemonthings.event;

import com.zitrone.lemonthings.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@EventBusSubscriber
public class AmethystEnchantEvent {

    private static final List<ResourceKey<Enchantment>> ALL_ENCHANTS = List.of(
            Enchantments.SHARPNESS,
            Enchantments.SMITE,
            Enchantments.BANE_OF_ARTHROPODS,
            Enchantments.KNOCKBACK,
            Enchantments.FIRE_ASPECT,
            Enchantments.LOOTING,
            Enchantments.SWEEPING_EDGE,
            Enchantments.UNBREAKING
    );

    // Событие для добавления тултипа аметистовому мечу
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        // Проверяем, что это аметистовый меч
        if (stack.getItem() == ModItems.AMETHYST_SWORD.get()) {
            // Добавляем наш тултип на вторую позицию (сразу под названием)
            // index 0 - название предмета, index 1 - наш тултип
            event.getToolTip().add(1,
                    Component.translatable("item.lemonthings.amethyst_sword.tooltip")
                            .withStyle(ChatFormatting.GRAY)
            );
        }
    }

    // Существующее событие зачарования
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        BlockPos pos = event.getPos();
        var level = event.getLevel();
        InteractionHand hand = event.getHand();

        ItemStack stack = player.getItemInHand(hand);
        if (!stack.is(ModItems.AMETHYST_SWORD.get())) return;
        if (!level.getBlockState(pos).is(Blocks.BUDDING_AMETHYST)) return;

        if (level instanceof ServerLevel serverLevel) {
            var registry = serverLevel.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            Random random = new Random();

            // Собираем только те чары, которые ещё не достигли максимума
            // И не конфликтуют с уже существующими на мече
            List<ResourceKey<Enchantment>> available = new ArrayList<>();

            for (ResourceKey<Enchantment> key : ALL_ENCHANTS) {
                var holderOpt = registry.get(key);
                if (holderOpt.isEmpty()) continue;

                Holder<Enchantment> holder = holderOpt.get();
                int currentLevel = EnchantmentHelper.getItemEnchantmentLevel(holder, stack);
                int maxLevel = holder.value().getMaxLevel();

                // Пропускаем если уже максимальный уровень
                if (currentLevel >= maxLevel) continue;

                // Пропускаем если конфликтует с уже существующими чарами на мече
                boolean conflicts = false;
                for (ResourceKey<Enchantment> otherKey : ALL_ENCHANTS) {
                    if (otherKey == key) continue;
                    var otherOpt = registry.get(otherKey);
                    if (otherOpt.isEmpty()) continue;
                    Holder<Enchantment> otherHolder = otherOpt.get();
                    // Если другой чар уже есть на мече И конфликтует с текущим
                    if (EnchantmentHelper.getItemEnchantmentLevel(otherHolder, stack) > 0
                            && !Enchantment.areCompatible(holder, otherHolder)) {
                        conflicts = true;
                        break;
                    }
                }
                if (conflicts) continue;

                available.add(key);
            }

            if (available.isEmpty()) {
                player.displayClientMessage(
                        Component.translatable("message.lemonthings.amethyst_sword_full")
                                .withStyle(ChatFormatting.DARK_PURPLE),
                        true
                );
                event.setCanceled(true);
                return;
            }

            // Выбираем один случайный чар из доступных
            ResourceKey<Enchantment> chosenKey = available.get(random.nextInt(available.size()));
            registry.get(chosenKey).ifPresent(holder -> {
                int currentLevel = EnchantmentHelper.getItemEnchantmentLevel(holder, stack);
                EnchantmentHelper.updateEnchantments(stack, mutable ->
                        mutable.set(holder, currentLevel + 1)
                );
            });

            // Заменяем блок, звук, частицы
            serverLevel.setBlock(pos, Blocks.AMETHYST_BLOCK.defaultBlockState(), 3);
            serverLevel.playSound(null, pos,
                    SoundEvents.AMETHYST_BLOCK_PLACE,
                    SoundSource.BLOCKS, 1.0f, 1.0f);
            serverLevel.sendParticles(
                    ParticleTypes.ENCHANT,
                    pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                    30, 0.5, 0.5, 0.5, 0.1
            );
        }

        event.setCanceled(true);
    }
}