package com.zitrone.lemonthings.entity;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BoneArrowEntity extends Arrow {

    // Конструктор для создания стрелы из лука
    public BoneArrowEntity(Level level, LivingEntity shooter, ItemStack arrowStack, ItemStack weaponStack) {
        super(ModEntities.BONE_ARROW.get(), level);
        this.setOwner(shooter);
        this.setPos(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ());
        this.setPickupItemStack(arrowStack.copy());
    }

    // Конструктор для регистрации сущности (обязателен!)
    public BoneArrowEntity(EntityType<? extends Arrow> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void doPostHurtEffects(LivingEntity target) {
        super.doPostHurtEffects(target);
        // Иссушение 2 уровня от 2 до 6 секунд
        int duration = (2 + this.random.nextInt(5)) * 20;
        target.addEffect(new MobEffectInstance(MobEffects.WITHER, duration, 1));
    }
}