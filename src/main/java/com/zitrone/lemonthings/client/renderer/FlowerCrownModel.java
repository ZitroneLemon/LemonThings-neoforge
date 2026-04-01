package com.zitrone.lemonthings.client.renderer;

import com.zitrone.lemonthings.item.FlowerCrownItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FlowerCrownModel extends GeoModel<FlowerCrownItem> {

    @Override
    public ResourceLocation getModelResource(FlowerCrownItem item) {
        return ResourceLocation.fromNamespaceAndPath("lemonthings", "geo/flower_crown.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FlowerCrownItem item) {
        // Текстура для 3D модели на игроке
        return ResourceLocation.fromNamespaceAndPath("lemonthings", "textures/geo/flower_crown.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FlowerCrownItem item) {
        return null; // анимаций нет
    }
}