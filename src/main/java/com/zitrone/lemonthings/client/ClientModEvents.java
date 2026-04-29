package com.zitrone.lemonthings.client;

import com.zitrone.lemonthings.LemonThings;
import com.zitrone.lemonthings.block.entity.ModBlockEntities;
import com.zitrone.lemonthings.client.renderer.BoneArrowRenderer;
import com.zitrone.lemonthings.client.renderer.FlowerCrownCurioRenderer;
import com.zitrone.lemonthings.client.renderer.blockentity.CopperBellRenderer;
import com.zitrone.lemonthings.entity.ModEntities;
import com.zitrone.lemonthings.item.ModItems;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.BundleContents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@EventBusSubscriber(modid = LemonThings.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            EntityRenderers.register(ModEntities.BONE_ARROW.get(), BoneArrowRenderer::new);

            ItemProperties.register(ModItems.RAVAGER_BAG.get(),
                    ResourceLocation.withDefaultNamespace("filled"),
                    (stack, level, entity, seed) -> {
                        BundleContents contents = stack.get(DataComponents.BUNDLE_CONTENTS);
                        if (contents == null || contents.isEmpty()) return 0.0F;
                        for (net.minecraft.world.item.ItemStack item : contents.items()) {
                            if (!item.isEmpty()) return 1.0F;
                        }
                        return 0.0F;
                    });

            CuriosRendererRegistry.register(ModItems.FLOWER_CROWN.get(), FlowerCrownCurioRenderer::new);

            BlockEntityRenderers.register(ModBlockEntities.COPPER_BELL.get(), CopperBellRenderer::new);

            LemonThings.LOGGER.info("Client setup completed!");
        });
    }


}