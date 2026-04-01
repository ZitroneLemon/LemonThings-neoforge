package com.zitrone.lemonthings.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
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

import java.util.List;
import java.util.function.Supplier;

public class BoneBowItem extends BowItem {

    private final Supplier<Item> boneArrowSupplier;
    private final Supplier<Item> witheredBoneSupplier;
    private static final int CHARGE_TIME = 15;
    private static final float MAX_ARROW_VELOCITY = 2.5f;

    public BoneBowItem(Properties properties,
                       Supplier<Item> boneArrowSupplier,
                       Supplier<Item> witheredBoneSupplier) {
        super(properties);
        this.boneArrowSupplier = boneArrowSupplier;
        this.witheredBoneSupplier = witheredBoneSupplier;
    }

    public ItemStack getDefaultProjectile() {
        return new ItemStack(boneArrowSupplier.get());
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        // Можно чинить иссушёнными костями ИЛИ обычными костями
        return repairCandidate.is(witheredBoneSupplier.get()) || repairCandidate.is(Items.BONE);
    }

    public static float getPowerForTime(int charge) {
        float power = (float) charge / CHARGE_TIME;
        power = (power * power + power * 2.0f) / 3.0f;
        if (power > 1.0f) power = 1.0f;
        return power;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) return;

        boolean isCreative = player.getAbilities().instabuild;
        boolean hasInfinity = EnchantmentHelper.getItemEnchantmentLevel(
                level.registryAccess().lookupOrThrow(
                        net.minecraft.core.registries.Registries.ENCHANTMENT
                ).getOrThrow(Enchantments.INFINITY),
                stack
        ) > 0;

        // Ищем костяные стрелы в инвентаре игрока
        ItemStack boneArrows = ItemStack.EMPTY;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item.getItem() == boneArrowSupplier.get() && !item.isEmpty()) {
                boneArrows = item;
                break;
            }
        }

        boolean hasBoneArrows = !boneArrows.isEmpty();

        // В креативе или с бесконечностью всегда стреляем
        if (!isCreative && !hasInfinity && !hasBoneArrows) {
            return;
        }

        int usedTime = this.getUseDuration(stack, entity) - timeLeft;
        float power = getPowerForTime(usedTime);
        if (power < 0.1f) return;

        // Определяем какую стрелу использовать
        ItemStack finalArrowStack;
        boolean consumeArrow = false;

        if (isCreative) {
            finalArrowStack = getDefaultProjectile();
            consumeArrow = false;
        } else if (hasInfinity) {
            finalArrowStack = getDefaultProjectile();
            consumeArrow = false;
        } else if (hasBoneArrows) {
            finalArrowStack = boneArrows;
            consumeArrow = true;
        } else {
            return;
        }

        if (!level.isClientSide) {
            ArrowItem arrowItem = (ArrowItem) boneArrowSupplier.get();
            AbstractArrow arrow = arrowItem.createArrow(level, finalArrowStack, player, stack);
            arrow.shootFromRotation(player, player.getXRot(), player.getYRot(),
                    0.0f, power * MAX_ARROW_VELOCITY, 1.0f);

            if (power >= 1.0f) arrow.setCritArrow(true);

            int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    level.registryAccess().lookupOrThrow(
                            net.minecraft.core.registries.Registries.ENCHANTMENT
                    ).getOrThrow(Enchantments.POWER),
                    stack
            );
            if (powerLevel > 0) {
                arrow.setBaseDamage(arrow.getBaseDamage() + powerLevel * 0.5 + 0.5);
            }

            int flameLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    level.registryAccess().lookupOrThrow(
                            net.minecraft.core.registries.Registries.ENCHANTMENT
                    ).getOrThrow(Enchantments.FLAME),
                    stack
            );
            if (flameLevel > 0) {
                arrow.igniteForSeconds(100);
            }

            if (isCreative || hasInfinity) {
                arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }

            level.addFreshEntity(arrow);
            stack.hurtAndBreak(1, player,
                    net.minecraft.world.entity.EquipmentSlot.MAINHAND);
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS,
                1.0f, 1.0f / (level.getRandom().nextFloat() * 0.4f + 1.2f) + power * 0.5f);

        // Тратим стрелы только если нужно
        if (consumeArrow && !level.isClientSide && hasBoneArrows) {
            boneArrows.shrink(1);
            if (boneArrows.isEmpty()) {
                player.getInventory().removeItem(boneArrows);
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // В креативе всегда можно стрелять
        if (player.getAbilities().instabuild) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        }

        // Проверяем наличие костяных стрел
        boolean hasBoneArrows = false;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item.getItem() == boneArrowSupplier.get() && !item.isEmpty()) {
                hasBoneArrows = true;
                break;
            }
        }

        boolean hasInfinity = EnchantmentHelper.getItemEnchantmentLevel(
                level.registryAccess().lookupOrThrow(
                        net.minecraft.core.registries.Registries.ENCHANTMENT
                ).getOrThrow(Enchantments.INFINITY),
                stack
        ) > 0;

        if (!hasBoneArrows && !hasInfinity) {
            return InteractionResultHolder.fail(stack);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("item.lemonthings.bone_bow.tooltip")
                .withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}