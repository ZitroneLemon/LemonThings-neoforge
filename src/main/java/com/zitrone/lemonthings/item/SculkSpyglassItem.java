package com.zitrone.lemonthings.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class SculkSpyglassItem extends SpyglassItem {

    private static final double HIGHLIGHT_RADIUS = 128.0;
    private static final int GLOW_DURATION = 20;

    public SculkSpyglassItem(Properties properties) {
        super(properties);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity,
                          ItemStack stack, int remainingUseDuration) {
        super.onUseTick(level, entity, stack, remainingUseDuration);

        if (level.isClientSide()) return;
        if (!(entity instanceof Player player)) return;
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (remainingUseDuration % 10 != 0) return;

        serverLevel.getEntitiesOfClass(LivingEntity.class,
                player.getBoundingBox().inflate(HIGHLIGHT_RADIUS)
        ).forEach(target -> {
            if (target == player) return;

            target.addEffect(new MobEffectInstance(
                    MobEffects.GLOWING,
                    GLOW_DURATION,
                    0,
                    false,
                    false
            ));
        });
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.lemonthings.sculk_spyglass.tooltip")
                .withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltip, flag);
    }
}