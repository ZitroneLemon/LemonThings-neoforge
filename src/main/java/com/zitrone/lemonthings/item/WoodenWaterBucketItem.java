package com.zitrone.lemonthings.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class WoodenWaterBucketItem extends Item {

    public WoodenWaterBucketItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = hitResult.getBlockPos();
            Direction direction = hitResult.getDirection();
            BlockPos targetPos = pos.relative(direction);

            if (!level.isClientSide) {
                BlockState blockState = level.getBlockState(targetPos);
                if (blockState.isAir() || blockState.canBeReplaced()) {
                    // Ставим воду
                    level.setBlock(targetPos, Blocks.WATER.defaultBlockState(), 11);

                    // Звук выливания
                    level.playSound(null, targetPos, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F);

                    // Если игрок не в креативе, тратим ведро
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                        ItemStack emptyBucket = new ItemStack(ModItems.WOODEN_BUCKET.get());
                        return InteractionResultHolder.success(emptyBucket);
                    } else {
                        // В креативе - возвращаем тот же стек (не тратим)
                        return InteractionResultHolder.success(stack);
                    }
                }
            }
        }

        return InteractionResultHolder.pass(stack);
    }
}