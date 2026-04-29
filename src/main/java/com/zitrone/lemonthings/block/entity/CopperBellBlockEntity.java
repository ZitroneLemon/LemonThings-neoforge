package com.zitrone.lemonthings.block.entity;

import com.zitrone.lemonthings.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CopperBellBlockEntity extends BlockEntity implements GeoBlockEntity {

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation RING_ANIMATION = RawAnimation.begin().thenPlay("ring");

    public CopperBellBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COPPER_BELL.get(), pos, state);
    }

    public void ring() {
        // Запускаем анимацию через триггер
        this.triggerAnim("ring_controller", "ring");
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<CopperBellBlockEntity> controller = new AnimationController<>(this, "ring_controller", 0, state -> {
            // По умолчанию — STOP, анимация не проигрывается
            return PlayState.STOP;
        });

        // Регистрируем анимацию, которая будет запускаться по триггеру
        controller.triggerableAnim("ring", RING_ANIMATION);

        controllers.add(controller);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}