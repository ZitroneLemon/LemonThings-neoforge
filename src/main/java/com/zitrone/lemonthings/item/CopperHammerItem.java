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

import java.util.ArrayList;
import java.util.List;

public class CopperHammerItem extends PickaxeItem {

    public CopperHammerItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!level.isClientSide && miner instanceof Player player) {
            // Сначала ломаем центральный блок
            boolean result = super.mineBlock(stack, level, state, pos, miner);

            // Затем ломаем соседние
            mineArea(level, player, stack, pos, state);

            return result;
        }
        return super.mineBlock(stack, level, state, pos, miner);
    }

    private void mineArea(Level level, Player player, ItemStack stack, BlockPos center, BlockState centerState) {
        Direction facing = player.getDirection();
        List<BlockPos> positions = getBlocksInArea(center, facing);

        for (BlockPos pos : positions) {
            if (pos.equals(center)) continue;

            BlockState state = level.getBlockState(pos);

            // Проверяем, можно ли сломать этот блок нашим инструментом
            if (canBreak(state, stack, player)) {
                // Проверяем, что инструмент подходит для добычи этого блока (по уровню)
                if (isCorrectToolForDrops(state, stack)) {
                    level.destroyBlock(pos, true, player);
                    stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
                }
            }
        }
    }

    private List<BlockPos> getBlocksInArea(BlockPos center, Direction facing) {
        List<BlockPos> positions = new ArrayList<>();

        int[][] offsets;

        switch (facing) {
            case NORTH:
            case SOUTH:
                offsets = new int[][]{
                        {-1, -1, 0}, {0, -1, 0}, {1, -1, 0},
                        {-1, 0, 0},  {0, 0, 0},  {1, 0, 0},
                        {-1, 1, 0},  {0, 1, 0},  {1, 1, 0}
                };
                break;
            case EAST:
            case WEST:
                offsets = new int[][]{
                        {0, -1, -1}, {0, -1, 0}, {0, -1, 1},
                        {0, 0, -1},  {0, 0, 0},  {0, 0, 1},
                        {0, 1, -1},  {0, 1, 0},  {0, 1, 1}
                };
                break;
            default:
                return positions;
        }

        for (int[] offset : offsets) {
            positions.add(center.offset(offset[0], offset[1], offset[2]));
        }

        return positions;
    }

    private boolean canBreak(BlockState state, ItemStack stack, Player player) {
        // Блок не должен быть воздухом
        if (state.isAir()) return false;

        // Проверяем, что блок не бесконечный (например, вода)
        if (state.getDestroySpeed(player.level(), player.blockPosition()) < 0) return false;

        return true;
    }

    private boolean isCorrectToolForDrops(BlockState state, ItemStack stack) {
        // Проверяем, подходит ли инструмент для добычи этого блока
        return stack.isCorrectToolForDrops(state);
    }
}