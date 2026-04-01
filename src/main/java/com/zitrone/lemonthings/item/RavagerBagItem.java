package com.zitrone.lemonthings.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RavagerBagItem extends Item {

    private static final int MAX_STORAGE = 192;
    // Цвет полоски
    private static final int BAR_COLOR = 0x6666FF;

    public RavagerBagItem(Properties properties) {
        super(properties);
    }

    private int getItemWeight(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        int maxStack = stack.getMaxStackSize();
        return 64 / maxStack;
    }

    private int getStackWeight(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        return getItemWeight(stack) * stack.getCount();
    }

    private int getTotalWeight(BundleContents contents) {
        if (contents == null) return 0;
        int total = 0;
        for (ItemStack stack : contents.items()) {
            total += getStackWeight(stack);
        }
        return total;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        BundleContents contents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (contents == null || contents.isEmpty()) {
            return 0;
        }
        int totalWeight = getTotalWeight(contents);
        return Math.round(13.0F * totalWeight / MAX_STORAGE);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return BAR_COLOR;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        BundleContents contents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (contents == null || contents.isEmpty()) {
            return false;
        }
        return getTotalWeight(contents) > 0;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        if (action != ClickAction.SECONDARY) return false;

        ItemStack otherStack = slot.getItem();
        BundleContents contents = stack.get(DataComponents.BUNDLE_CONTENTS);

        if (otherStack.isEmpty()) {
            // Извлечение предмета
            if (contents != null && !contents.isEmpty()) {
                List<ItemStack> items = new ArrayList<>();
                contents.items().forEach(items::add);

                if (!items.isEmpty()) {
                    ItemStack topItem = items.get(0);
                    player.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + player.getRandom().nextFloat() * 0.4F);
                    items.remove(0);

                    stack.set(DataComponents.BUNDLE_CONTENTS, new BundleContents(items));
                    slot.safeInsert(topItem.copy());
                    return true;
                }
            }
        } else if (otherStack.getItem().canFitInsideContainerItems()) {
            // Добавление предмета
            if (contents == null) {
                contents = BundleContents.EMPTY;
            }

            int currentWeight = getTotalWeight(contents);
            int itemWeight = getItemWeight(otherStack);
            int availableWeight = MAX_STORAGE - currentWeight;

            if (availableWeight <= 0) {
                return false;
            }

            int maxAddCount = availableWeight / itemWeight;
            if (maxAddCount <= 0) {
                return false;
            }

            int addCount = Math.min(otherStack.getCount(), maxAddCount);
            if (addCount <= 0) {
                return false;
            }

            List<ItemStack> items = new ArrayList<>();
            contents.items().forEach(items::add);

            ItemStack targetStack = null;
            for (ItemStack existing : items) {
                if (ItemStack.isSameItemSameComponents(existing, otherStack)) {
                    targetStack = existing;
                    break;
                }
            }

            int remainingAddCount = addCount;

            if (targetStack != null) {
                int spaceInStack = targetStack.getMaxStackSize() - targetStack.getCount();
                int canAddToStack = Math.min(remainingAddCount, spaceInStack);
                if (canAddToStack > 0) {
                    targetStack.grow(canAddToStack);
                    remainingAddCount -= canAddToStack;
                }
            }

            if (remainingAddCount > 0) {
                items.add(otherStack.copyWithCount(remainingAddCount));
            }

            if (addCount > 0) {
                stack.set(DataComponents.BUNDLE_CONTENTS, new BundleContents(items));
                player.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + player.getRandom().nextFloat() * 0.4F);

                otherStack.shrink(addCount);
                if (otherStack.isEmpty()) {
                    slot.set(ItemStack.EMPTY);
                } else {
                    slot.set(otherStack);
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action != ClickAction.SECONDARY) return false;
        if (other.isEmpty()) return false;

        BundleContents contents = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);

        int currentWeight = getTotalWeight(contents);
        int itemWeight = getItemWeight(other);
        int availableWeight = MAX_STORAGE - currentWeight;

        if (availableWeight <= 0) {
            return false;
        }

        int maxAddCount = availableWeight / itemWeight;
        if (maxAddCount <= 0) {
            return false;
        }

        int addCount = Math.min(other.getCount(), maxAddCount);
        if (addCount <= 0) {
            return false;
        }

        List<ItemStack> items = new ArrayList<>();
        contents.items().forEach(items::add);

        ItemStack targetStack = null;
        for (ItemStack existing : items) {
            if (ItemStack.isSameItemSameComponents(existing, other)) {
                targetStack = existing;
                break;
            }
        }

        int remainingAddCount = addCount;

        if (targetStack != null) {
            int spaceInStack = targetStack.getMaxStackSize() - targetStack.getCount();
            int canAddToStack = Math.min(remainingAddCount, spaceInStack);
            if (canAddToStack > 0) {
                targetStack.grow(canAddToStack);
                remainingAddCount -= canAddToStack;
            }
        }

        if (remainingAddCount > 0) {
            items.add(other.copyWithCount(remainingAddCount));
        }

        if (addCount > 0) {
            stack.set(DataComponents.BUNDLE_CONTENTS, new BundleContents(items));
            player.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + player.getRandom().nextFloat() * 0.4F);

            ItemStack remaining = other.copy();
            remaining.shrink(addCount);
            access.set(remaining.isEmpty() ? ItemStack.EMPTY : remaining);
            return true;
        }

        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        BundleContents contents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (contents == null) {
            return Optional.empty();
        }
        return Optional.of(new BundleTooltip(contents));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipComponents, flag);

        BundleContents contents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (contents != null) {
            int currentWeight = getTotalWeight(contents);
            tooltipComponents.add(Component.literal(currentWeight + "/" + MAX_STORAGE).withStyle(ChatFormatting.GRAY));
        }
    }
}