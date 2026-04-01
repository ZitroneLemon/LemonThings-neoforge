package com.zitrone.lemonthings.event;

import com.zitrone.lemonthings.LemonThings;
import com.zitrone.lemonthings.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = LemonThings.MODID, bus = EventBusSubscriber.Bus.GAME)
public class FeatherPaperCraftEvent {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        ItemStack handStack = player.getItemInHand(event.getHand());

        // Проверяем, что блок - стол лучника
        if (!level.getBlockState(pos).is(Blocks.FLETCHING_TABLE)) return;

        // Проверяем, что в руке бумага
        if (!handStack.is(Items.PAPER)) return;

        if (!level.isClientSide) {
            // Уменьшаем бумагу на 1
            if (!player.getAbilities().instabuild) {
                handStack.shrink(1);
            }

            // Добавляем бумажное оперение
            player.addItem(new ItemStack(ModItems.FEATHER_PAPER.get(), 1));

            // Звук
            level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
        }

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }
}