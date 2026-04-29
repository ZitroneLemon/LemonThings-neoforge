package com.zitrone.lemonthings;

import com.mojang.logging.LogUtils;
import com.zitrone.lemonthings.block.ModBlocks;
import com.zitrone.lemonthings.block.entity.ModBlockEntities;
import com.zitrone.lemonthings.entity.ModEntities;
import com.zitrone.lemonthings.event.*;
import com.zitrone.lemonthings.item.ModItems;
import com.zitrone.lemonthings.sound.ModSounds;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(LemonThings.MODID)
public class LemonThings {

    public static final String MODID = "lemonthings";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> LEMON_TAB =
            CREATIVE_MODE_TABS.register("lemon_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.lemonthings"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> ModItems.AMETHYST_SWORD.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.AMETHYST_SWORD.get());
                        output.accept(ModItems.SIMPLEST_BOW.get());
                        output.accept(ModItems.WITHERED_BONE.get());
                        output.accept(ModItems.WITHERED_BONE_MEAL.get());
                        output.accept(ModItems.BONE_ARROW.get());
                        output.accept(ModItems.BONE_BOW.get());
                        output.accept(ModItems.FEATHER_PAPER.get());
                        output.accept(ModItems.WANDERING_TRADER_BAG.get());
                        output.accept(ModItems.WITCH_BAG.get());
                        output.accept(ModItems.PIGLIN_BAG.get());
                        output.accept(ModItems.PILLAGER_BAG.get());
                        output.accept(ModItems.RAVAGER_BAG.get());
                        output.accept(ModItems.PLATINUM_INGOT.get());
                        output.accept(ModItems.PLATINUM_NUGGET.get());
                        output.accept(ModItems.RAVAGER_LEATHER.get());
                        output.accept(ModItems.PLATINUM_CHAIN_ITEM.get());
                        output.accept(ModItems.PLATINUM_BLOCK_ITEM.get());
                        output.accept(ModItems.TOME_OF_TRANSMISSION.get());
                        output.accept(ModItems.FLOWER_CROWN.get());
                        output.accept(ModItems.SCULK_SPYGLASS.get());
                        output.accept(ModItems.NEEDLE.get());
                        output.accept(ModItems.HANDMADE_TOTEM.get());
                        output.accept(ModItems.REPAIR_KIT.get());
                        output.accept(ModItems.WOODEN_BUCKET.get());
                        output.accept(ModItems.WOODEN_WATER_BUCKET.get());
                        output.accept(ModItems.COPPER_HAMMER.get());
                        output.accept(ModItems.MINERS_LOLLIPOP.get());
                        output.accept(ModItems.BERRY_ROLL.get());
                        output.accept(ModItems.COPPER_BELL.get());
                        output.accept(ModItems.WIND_CHARGE_IN_BOTTLE.get());
                    }).build());

    public LemonThings(IEventBus modEventBus, ModContainer modContainer) {
        // Регистрируем конфиг
        modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, ModConfig.COMMON_SPEC);

        ModSounds.register(modEventBus);
        ModItems.register(modEventBus);
        ModPotions.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntities.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        // Регистрируем клиентские события
        modEventBus.addListener(this::onClientSetup);

        // Регистрируем Forge события
        NeoForge.EVENT_BUS.register(AmethystEnchantEvent.class);
        NeoForge.EVENT_BUS.register(BrewingRecipeHandler.class);
        NeoForge.EVENT_BUS.register(FeatherPaperCraftEvent.class);
        NeoForge.EVENT_BUS.register(WitheredBoneMealEvent.class);
        NeoForge.EVENT_BUS.register(HandmadeTotemEvent.class);
        NeoForge.EVENT_BUS.register(GoatHornEffectHandler.class);
        NeoForge.EVENT_BUS.register(DoubleJumpHandler.class);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            registerBowProperties(ModItems.SIMPLEST_BOW.get(), 10.0F);
            registerBowProperties(ModItems.BONE_BOW.get(), 15.0F);

            net.minecraft.client.renderer.entity.EntityRenderers.register(
                    ModEntities.BONE_ARROW.get(),
                    context -> new com.zitrone.lemonthings.client.renderer.BoneArrowRenderer(context)
            );

            LemonThings.LOGGER.info("Client setup completed!");
        });
    }

    private void registerBowProperties(net.minecraft.world.item.Item bow, float chargeTime) {
        ItemProperties.register(bow,
                ResourceLocation.withDefaultNamespace("pull"),
                (stack, level, entity, seed) -> {
                    if (entity == null) return 0.0F;
                    return entity.getUseItem() != stack ? 0.0F :
                            (float)(stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / chargeTime;
                });

        ItemProperties.register(bow,
                ResourceLocation.withDefaultNamespace("pulling"),
                (stack, level, entity, seed) ->
                        entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
    }
}