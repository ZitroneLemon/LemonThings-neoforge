package com.zitrone.lemonthings.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;

public class CopperHammerItem extends PickaxeItem {

    public CopperHammerItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!level.isClientSide && miner instanceof Player player) {
            // Получаем сторону блока, по которой ударили
            Direction hitSide = getHitSide(player);

            // Сначала ломаем центральный блок
            boolean result = super.mineBlock(stack, level, state, pos, miner);

            // Затем ломаем соседние в зависимости от стороны удара
            mineArea(level, player, stack, pos, hitSide);

            return result;
        }
        return super.mineBlock(stack, level, state, pos, miner);
    }

    private Direction getHitSide(Player player) {
        // Получаем блок, на который смотрит игрок, и сторону удара
        BlockHitResult hitResult = (BlockHitResult) player.pick(5.0D, 0.0F, false);
        return hitResult.getDirection();
    }

    private void mineArea(Level level, Player player, ItemStack stack, BlockPos center, Direction hitSide) {
        List<BlockPos> positions = getBlocksInArea(center, hitSide);

        for (BlockPos pos : positions) {
            if (pos.equals(center)) continue;

            BlockState state = level.getBlockState(pos);

            // Проверяем, можно ли сломать этот блок нашим инструментом
            if (canBreak(state, stack, player)) {
                if (isCorrectToolForDrops(state, stack)) {
                    level.destroyBlock(pos, true, player);
                    stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
                }
            }
        }
    }

    private List<BlockPos> getBlocksInArea(BlockPos center, Direction hitSide) {
        List<BlockPos> positions = new ArrayList<>();

        int[][] offsets;

        // В зависимости от стороны удара определяем плоскость копания
        if (hitSide == Direction.UP || hitSide == Direction.DOWN) {
            // Ударили сверху или снизу — копаем по горизонтали (X и Z)
            offsets = new int[][]{
                    {-1, 0, -1}, {0, 0, -1}, {1, 0, -1},
                    {-1, 0, 0},  {0, 0, 0},  {1, 0, 0},
                    {-1, 0, 1},  {0, 0, 1},  {1, 0, 1}
            };
        } else if (hitSide == Direction.NORTH || hitSide == Direction.SOUTH) {
            // Ударили с севера или юга — копаем по X и Y (вертикально)
            offsets = new int[][]{
                    {-1, -1, 0}, {0, -1, 0}, {1, -1, 0},
                    {-1, 0, 0},  {0, 0, 0},  {1, 0, 0},
                    {-1, 1, 0},  {0, 1, 0},  {1, 1, 0}
            };
        } else { // EAST или WEST
            // Ударили с востока или запада — копаем по Z и Y (вертикально)
            offsets = new int[][]{
                    {0, -1, -1}, {0, -1, 0}, {0, -1, 1},
                    {0, 0, -1},  {0, 0, 0},  {0, 0, 1},
                    {0, 1, -1},  {0, 1, 0},  {0, 1, 1}
            };
        }

        for (int[] offset : offsets) {
            positions.add(center.offset(offset[0], offset[1], offset[2]));
        }

        return positions;
    }

    private boolean canBreak(BlockState state, ItemStack stack, Player player) {
        if (state.isAir()) return false;
        if (state.getDestroySpeed(player.level(), player.blockPosition()) < 0) return false;
        return true;
    }

    private boolean isCorrectToolForDrops(BlockState state, ItemStack stack) {
        return stack.isCorrectToolForDrops(state);
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 15;
    }
}