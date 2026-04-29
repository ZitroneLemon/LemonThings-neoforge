package com.zitrone.lemonthings.block;

import com.zitrone.lemonthings.block.entity.CopperBellBlockEntity;
import com.zitrone.lemonthings.block.entity.ModBlockEntities;
import com.zitrone.lemonthings.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.Nullable;

public class CopperBellBlock extends Block implements EntityBlock {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    protected static final VoxelShape SHAPE = Shapes.or(
            Block.box(7.0, 14.0, 7.0, 9.0, 16.0, 9.0),
            Block.box(2.0, 3.0, 2.0, 14.0, 14.0, 14.0),
            Block.box(1.0, 1.0, 1.0, 15.0, 3.0, 15.0)
    );

    public CopperBellBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public boolean canSurvive(BlockState state, Level level, BlockPos pos) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        return aboveState.isSolid() && !aboveState.isAir();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        if (canSurvive(defaultBlockState(), level, pos)) {
            return defaultBlockState();
        }
        return null;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.UP && !canSurvive(state, (Level) level, pos)) {
            if (level instanceof Level serverLevel && !serverLevel.isClientSide) {
                ItemStack dropStack = new ItemStack(this.asItem());
                ItemEntity itemEntity = new ItemEntity(serverLevel,
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        dropStack);
                serverLevel.addFreshEntity(itemEntity);
            }
            return Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);

        if (!level.isClientSide) {
            boolean powered = level.hasNeighborSignal(pos);

            if (powered && !state.getValue(POWERED)) {
                // Получили сигнал редстоуна
                level.setBlock(pos, state.setValue(POWERED, true), 3);

                // Проигрываем звук
                level.playSound(null, pos, ModSounds.COPPER_BELL_USE.get(),
                        SoundSource.BLOCKS, 1.0F, 1.0F);

                // Запускаем анимацию
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof CopperBellBlockEntity bellEntity) {
                    bellEntity.ring();
                }

                // Планируем сброс состояния через 20 тиков
                level.scheduleTick(pos, this, 20);
            } else if (!powered && state.getValue(POWERED)) {
                level.setBlock(pos, state.setValue(POWERED, false), 3);
            }
        }
    }

    public void tick(BlockState state, Level level, BlockPos pos, java.util.Random random) {
        if (!level.isClientSide && state.getValue(POWERED)) {
            level.setBlock(pos, state.setValue(POWERED, false), 3);
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CopperBellBlockEntity(pos, state);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide) {
            level.playSound(null, pos, ModSounds.COPPER_BELL_USE.get(),
                    SoundSource.BLOCKS, 1.0F, 1.0F);

            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CopperBellBlockEntity bellEntity) {
                bellEntity.ring();
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return null;
    }
}