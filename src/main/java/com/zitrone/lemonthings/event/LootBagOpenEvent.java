package com.zitrone.lemonthings.event;

import com.zitrone.lemonthings.LemonThings;
import com.zitrone.lemonthings.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.List;

@EventBusSubscriber(modid = LemonThings.MODID, bus = EventBusSubscriber.Bus.GAME)
public class LootBagOpenEvent {

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();

        boolean isBag = stack.getItem() == ModItems.WANDERING_TRADER_BAG.get() ||
                stack.getItem() == ModItems.WITCH_BAG.get() ||
                stack.getItem() == ModItems.PIGLIN_BAG.get() ||
                stack.getItem() == ModItems.PILLAGER_BAG.get();

        if (!isBag) return;

        ResourceLocation lootTableId = null;
        if (stack.getItem() == ModItems.WANDERING_TRADER_BAG.get()) {
            lootTableId = ResourceLocation.fromNamespaceAndPath(LemonThings.MODID, "bags/wandering_trader_bag");
        } else if (stack.getItem() == ModItems.WITCH_BAG.get()) {
            lootTableId = ResourceLocation.fromNamespaceAndPath(LemonThings.MODID, "bags/witch_bag");
        } else if (stack.getItem() == ModItems.PIGLIN_BAG.get()) {
            lootTableId = ResourceLocation.fromNamespaceAndPath(LemonThings.MODID, "bags/piglin_bag");
        } else if (stack.getItem() == ModItems.PILLAGER_BAG.get()) {
            lootTableId = ResourceLocation.fromNamespaceAndPath(LemonThings.MODID, "bags/pillager_bag");
        }

        if (lootTableId == null) return;

        Level level = event.getLevel();

        if (!level.isClientSide) {
            ServerLevel serverLevel = (ServerLevel) level;

            ResourceKey<LootTable> lootTableKey = ResourceKey.create(
                    net.minecraft.core.registries.Registries.LOOT_TABLE,
                    lootTableId
            );

            LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(lootTableKey);

            if (lootTable != null && lootTable != LootTable.EMPTY) {
                LootParams params = new LootParams.Builder(serverLevel)
                        .withParameter(LootContextParams.ORIGIN, player.position())
                        .withLuck(player.getLuck())
                        .create(LootContextParamSets.CHEST);

                List<ItemStack> drops = lootTable.getRandomItems(params);

                for (ItemStack drop : drops) {
                    ItemEntity itemEntity = new ItemEntity(
                            level,
                            player.getX(),
                            player.getY() + 0.5,
                            player.getZ(),
                            drop
                    );
                    level.addFreshEntity(itemEntity);
                }

                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }

                player.displayClientMessage(
                        Component.translatable("message.lemonthings.bag_opened")
                                .withStyle(ChatFormatting.GREEN),
                        true
                );
            } else {
                player.displayClientMessage(
                        Component.literal("§cLoot table not found: " + lootTableId),
                        true
                );
            }
        }

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }
}