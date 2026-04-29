package com.zitrone.lemonthings.client.renderer.blockentity;

import com.zitrone.lemonthings.LemonThings;
import com.zitrone.lemonthings.block.entity.CopperBellBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class CopperBellRenderer extends GeoBlockRenderer<CopperBellBlockEntity> {

    public CopperBellRenderer(BlockEntityRendererProvider.Context context) {
        super(new GeoModel<CopperBellBlockEntity>() {
            @Override
            public ResourceLocation getModelResource(CopperBellBlockEntity animatable) {
                return ResourceLocation.fromNamespaceAndPath(LemonThings.MODID, "geo/copper_bell.geo.json");
            }

            @Override
            public ResourceLocation getTextureResource(CopperBellBlockEntity animatable) {
                return ResourceLocation.fromNamespaceAndPath(LemonThings.MODID, "textures/block/copper_bell.png");
            }

            @Override
            public ResourceLocation getAnimationResource(CopperBellBlockEntity animatable) {
                return ResourceLocation.fromNamespaceAndPath(LemonThings.MODID, "animations/copper_bell.animation.json");
            }
        });
    }
}