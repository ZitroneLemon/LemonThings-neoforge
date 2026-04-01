package com.zitrone.lemonthings.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class SimplestBowItem extends BowItem {

    // Время полного натяжения в тиках — ванильный лук 20 тиков (1 сек), наш 10 тиков (0.5 сек)
    private static final int CHARGE_TIME = 10;
    // Максимальная скорость стрелы — ванильный лук 3.0f, наш 2.0f (меньше = меньше дальность)
    private static final float MAX_ARROW_VELOCITY = 2.0f;

    public SimplestBowItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        // Можно чинить палками
        return repairCandidate.is(Items.STICK);
    }

    // Переопределяем скорость стрелы исходя из времени натяжения
    public static float getPowerForTime(int charge) {
        float power = (float) charge / CHARGE_TIME;
        power = (power * power + power * 2.0f) / 3.0f;
        if (power > 1.0f) power = 1.0f;
        return power;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) return;

        boolean hasInfinity = EnchantmentHelper.getItemEnchantmentLevel(
                level.registryAccess().lookupOrThrow(
                        net.minecraft.core.registries.Registries.ENCHANTMENT
                ).getOrThrow(Enchantments.INFINITY),
                stack
        ) > 0;

        ItemStack arrowStack = player.getProjectile(stack);
        if (arrowStack.isEmpty() && !hasInfinity) return;

        int usedTime = this.getUseDuration(stack, entity) - timeLeft;
        float power = getPowerForTime(usedTime);
        if (power < 0.1f) return;

        // Получаем уровень зачарования Punch
        int punchLevel = EnchantmentHelper.getItemEnchantmentLevel(
                level.registryAccess().lookupOrThrow(
                        net.minecraft.core.registries.Registries.ENCHANTMENT
                ).getOrThrow(Enchantments.PUNCH),
                stack
        );

        if (!level.isClientSide) {
            ArrowItem arrowItem = arrowStack.getItem() instanceof ArrowItem a ? a
                    : (ArrowItem) Items.ARROW;

            // Создаем стрелу
            AbstractArrow arrow = arrowItem.createArrow(level, arrowStack, player, stack);


            // Применяем уменьшенную скорость
            arrow.shootFromRotation(player, player.getXRot(), player.getYRot(),
                    0.0f, power * MAX_ARROW_VELOCITY, 1.0f);

            if (power >= 1.0f) arrow.setCritArrow(true);

            // Урон от чар силы
            int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    level.registryAccess().lookupOrThrow(
                            net.minecraft.core.registries.Registries.ENCHANTMENT
                    ).getOrThrow(Enchantments.POWER),
                    stack
            );
            if (powerLevel > 0) {
                arrow.setBaseDamage(arrow.getBaseDamage() + powerLevel * 0.5 + 0.5);
            }

            // Поджигание от чара Flame
            int flameLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    level.registryAccess().lookupOrThrow(
                            net.minecraft.core.registries.Registries.ENCHANTMENT
                    ).getOrThrow(Enchantments.FLAME),
                    stack
            );
            if (flameLevel > 0) {
                arrow.igniteForSeconds(100);
            }

            if (hasInfinity || player.hasInfiniteMaterials()) {
                arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }

            level.addFreshEntity(arrow);
            stack.hurtAndBreak(1, player,
                    net.minecraft.world.entity.EquipmentSlot.MAINHAND);
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS,
                1.0f, 1.0f / (level.getRandom().nextFloat() * 0.4f + 1.2f) + power * 0.5f);

        if (!hasInfinity && !player.hasInfiniteMaterials()) {
            arrowStack.shrink(1);
            if (arrowStack.isEmpty()) player.getInventory().removeItem(arrowStack);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        boolean hasArrows = !player.getProjectile(stack).isEmpty();

        if (!player.hasInfiniteMaterials() && !hasArrows) {
            return InteractionResultHolder.fail(stack);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }
}