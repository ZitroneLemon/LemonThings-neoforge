package com.zitrone.lemonthings.client.renderer;

import com.zitrone.lemonthings.LemonThings;
import com.zitrone.lemonthings.entity.BoneArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class BoneArrowRenderer extends ArrowRenderer<BoneArrowEntity> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(LemonThings.MODID, "textures/entity/bone_arrow_model_.png");

    public BoneArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(BoneArrowEntity entity) {
        return TEXTURE;
    }
}