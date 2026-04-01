package com.zitrone.lemonthings.event;

import com.zitrone.lemonthings.LemonThings;
import com.zitrone.lemonthings.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = LemonThings.MODID)
public class WitheredBoneMealEvent {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        ItemStack stack = player.getItemInHand(hand);
        Level level = event.getLevel();
        BlockPos pos = event.getPos();

        // Проверяем, что в руке иссушённая костная мука
        if (!stack.is(ModItems.WITHERED_BONE_MEAL.get())) return;

        // Проверяем, что блок - песок душ или почва душ
        BlockState state = level.getBlockState(pos);
        boolean isSoulBlock = state.is(Blocks.SOUL_SAND) || state.is(Blocks.SOUL_SOIL);

        if (!isSoulBlock) return;

        // Проверяем, что над блоком есть место для розы визера
        BlockPos abovePos = pos.above();
        if (!level.getBlockState(abovePos).isAir()) return;

        // Если на сервере, выполняем изменения
        if (!level.isClientSide) {
            ServerLevel serverLevel = (ServerLevel) level;

            // Устанавливаем розу визера
            serverLevel.setBlock(abovePos, Blocks.WITHER_ROSE.defaultBlockState(), 3);

            // Уменьшаем количество предмета (если не креатив)
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            // Эффекты
            serverLevel.playSound(null, pos,
                    SoundEvents.BONE_MEAL_USE,
                    SoundSource.BLOCKS,
                    1.0f, 1.0f);

            serverLevel.sendParticles(
                    ParticleTypes.SOUL_FIRE_FLAME,
                    abovePos.getX() + 0.5, abovePos.getY() + 0.5, abovePos.getZ() + 0.5,
                    20, 0.3, 0.3, 0.3, 0.1
            );

            serverLevel.sendParticles(
                    ParticleTypes.SOUL,
                    abovePos.getX() + 0.5, abovePos.getY() + 0.5, abovePos.getZ() + 0.5,
                    30, 0.4, 0.4, 0.4, 0.05
            );
        }

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }
}