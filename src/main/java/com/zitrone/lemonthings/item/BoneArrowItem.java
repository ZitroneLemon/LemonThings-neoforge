package com.zitrone.lemonthings.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import com.zitrone.lemonthings.entity.BoneArrowEntity;

public class BoneArrowItem extends ArrowItem {

    public BoneArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity shooter, ItemStack weaponStack) {
        return new BoneArrowEntity(level, shooter, stack.copyWithCount(1), weaponStack);
    }
}