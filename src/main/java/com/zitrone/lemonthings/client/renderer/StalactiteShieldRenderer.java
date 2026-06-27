package com.zitrone.lemonthings.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.zitrone.lemonthings.LemonThings;
import com.zitrone.lemonthings.client.model.StalactiteShieldModel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StalactiteShieldRenderer extends BlockEntityWithoutLevelRenderer {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(LemonThings.MODID, "textures/item/stalactite_shield.png");

    private StalactiteShieldModel<?> model;

    public StalactiteShieldRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels());
    }

    // Модель создаётся лениво, чтобы гарантировать что EntityModels уже baked
    private StalactiteShieldModel<?> getModel() {
        if (model == null) {
            model = new StalactiteShieldModel<>(
                    Minecraft.getInstance().getEntityModels()
                            .bakeLayer(StalactiteShieldModel.LAYER_LOCATION)
            );
        }
        return model;
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext,
                             PoseStack poseStack, MultiBufferSource bufferSource,
                             int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.scale(1.0F, -1.0F, -1.0F);

        VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(
                bufferSource,
                RenderType.entitySolid(TEXTURE),
                false,
                stack.hasFoil()
        );

        StalactiteShieldModel<?> m = getModel();
        m.plate().render(poseStack, vertexConsumer, packedLight, packedOverlay);
        m.handle().render(poseStack, vertexConsumer, packedLight, packedOverlay);

        poseStack.popPose();
    }
}
