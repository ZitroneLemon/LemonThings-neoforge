package com.zitrone.lemonthings.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class FlowerCrownItem extends Item implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public FlowerCrownItem(Properties properties) {
        super(properties);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // Статичный предмет, анимаций нет
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    // Этот метод говорит GeckoLib какой рендерер использовать для предмета в руке/инвентаре
    // Мы возвращаем null — используем стандартную 2D иконку из models/item/flower_crown.json
    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        // Намеренно пусто — не используем GeckoLib рендерер для иконки
        // 2D иконка берётся из models/item/flower_crown.json как обычно
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.lemonthings.flower_crown.tooltip")
                .withStyle(ChatFormatting.GRAY));
    }
}