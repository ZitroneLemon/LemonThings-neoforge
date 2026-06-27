package com.zitrone.lemonthings.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoItem;
import top.theillusivec4.curios.api.CuriosApi;
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
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            boolean equipped = CuriosApi.getCuriosInventory(player).map(inv -> {
                var slots = inv.getCurios();
                var headSlots = slots.get("head");
                if (headSlots == null) return false;
                var stacks = headSlots.getStacks();
                for (int i = 0; i < stacks.getSlots(); i++) {
                    if (stacks.getStackInSlot(i).isEmpty()) {
                        stacks.setStackInSlot(i, stack.copyWithCount(1));
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                        return true;
                    }
                }
                return false;
            }).orElse(false);

            if (equipped) {
                return InteractionResultHolder.success(stack);
            }
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.lemonthings.flower_crown.tooltip")
                .withStyle(ChatFormatting.GRAY));
    }
}
