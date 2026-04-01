package com.zitrone.lemonthings.item;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class WoodenBucketItem extends Item {

    public WoodenBucketItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = hitResult.getBlockPos();
            BlockState blockState = level.getBlockState(pos);

            // Проверяем, что это вода, а не лава
            if (blockState.getFluidState().getType() == Fluids.WATER) {
                if (blockState.getBlock() instanceof BucketPickup bucketPickup) {
                    ItemStack filledBucket = bucketPickup.pickupBlock(player, level, pos, blockState);

                    if (!filledBucket.isEmpty() && filledBucket.getItem() == Items.WATER_BUCKET) {
                        if (!level.isClientSide) {
                            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                                    SoundEvents.BUCKET_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
                        }

                        // Если игрок не в креативе, тратим ведро
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                            ItemStack waterBucket = new ItemStack(ModItems.WOODEN_WATER_BUCKET.get());
                            return InteractionResultHolder.success(waterBucket);
                        } else {
                            // В креативе - возвращаем тот же стек (не тратим)
                            return InteractionResultHolder.success(stack);
                        }
                    }
                }
            }
        }

        return InteractionResultHolder.pass(stack);
    }
}