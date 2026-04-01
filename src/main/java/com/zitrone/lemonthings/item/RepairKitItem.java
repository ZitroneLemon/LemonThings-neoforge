package com.zitrone.lemonthings.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Random;

public class RepairKitItem extends Item {

    private static final Random RANDOM = new Random();

    public RepairKitItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack repairKit = player.getItemInHand(hand);

        // Проверяем, что ремонтный набор в правой руке
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResultHolder.pass(repairKit);
        }

        // Получаем предмет в левой руке
        ItemStack leftHandItem = player.getOffhandItem();

        // Проверяем, есть ли предмет в левой руке
        if (leftHandItem.isEmpty()) {
            return InteractionResultHolder.pass(repairKit);
        }

        // Проверяем, что предмет можно чинить
        if (!leftHandItem.isDamageableItem()) {
            return InteractionResultHolder.pass(repairKit);
        }

        // Проверяем, что предмет повреждён
        if (leftHandItem.getDamageValue() == 0) {
            return InteractionResultHolder.pass(repairKit);
        }

        // Проверяем, что ремонтный набор ещё не сломан
        if (repairKit.getDamageValue() >= repairKit.getMaxDamage()) {
            return InteractionResultHolder.pass(repairKit);
        }

        // Ремонтируем предмет (от 0 до 4)
        int repairAmount = RANDOM.nextInt(5); // 0-4
        int currentDamage = leftHandItem.getDamageValue();
        int newDamage = Math.max(0, currentDamage - repairAmount);

        if (newDamage == currentDamage) {
            if (!level.isClientSide) {
                player.displayClientMessage(
                        Component.translatable("message.lemonthings.repair_kit.failed")
                                .withStyle(ChatFormatting.RED),
                        true
                );
            }
            return InteractionResultHolder.pass(repairKit);
        }

        // Применяем ремонт
        leftHandItem.setDamageValue(newDamage);

        // Повреждаем ремонтный набор на 1
        EquipmentSlot slot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
        repairKit.hurtAndBreak(1, player, slot);

        // Сообщение о результате
        if (!level.isClientSide) {
            player.displayClientMessage(
                    Component.translatable("message.lemonthings.repair_kit.success", repairAmount)
                            .withStyle(ChatFormatting.GREEN),
                    true
            );

            // Звук ремонта
            player.playSound(net.minecraft.sounds.SoundEvents.ANVIL_USE, 0.5F, 1.0F);
        }

        return InteractionResultHolder.success(repairKit);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.lemonthings.repair_kit.tooltip")
                .withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltip, flag);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }
}