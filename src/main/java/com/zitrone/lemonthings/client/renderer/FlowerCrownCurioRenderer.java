package com.zitrone.lemonthings.client.renderer;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.cache.object.GeoQuad;
import software.bernie.geckolib.cache.object.GeoVertex;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class FlowerCrownCurioRenderer implements ICurioRenderer {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("lemonthings", "textures/geo/flower_crown.png");
    private static final ResourceLocation MODEL_KEY =
            ResourceLocation.fromNamespaceAndPath("lemonthings", "geo/flower_crown.geo.json");

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack,
            SlotContext slotContext,
            PoseStack poseStack,
            RenderLayerParent<T, M> renderLayerParent,
            MultiBufferSource bufferSource,
            int light,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {

        M model = renderLayerParent.getModel();
        if (!(model instanceof HeadedModel headedModel)) return;

        ModelPart head = headedModel.getHead();

        BakedGeoModel bakedModel = GeckoLibCache.getBakedModels().get(MODEL_KEY);
        if (bakedModel == null) return;

        VertexConsumer consumer = bufferSource.getBuffer(
                RenderType.entityCutoutNoCull(TEXTURE));

        poseStack.pushPose();

        // Translate to the head's actual position in the model (handles sneaking offset)
        poseStack.translate(
                head.x / 16.0f,
                head.y / 16.0f,
                head.z / 16.0f
        );

        // Apply head rotations (yaw and pitch)
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(netHeadYaw));
        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(headPitch));

        // Flip to match Minecraft's coordinate system
        poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(180));

        for (GeoBone bone : bakedModel.topLevelBones()) {
            renderBone(poseStack, bone, consumer, light);
        }

        poseStack.popPose();
    }

    private void renderBone(PoseStack poseStack, GeoBone bone,
                            VertexConsumer consumer, int light) {
        if (bone.isHidden()) return;
        poseStack.pushPose();

        for (GeoCube cube : bone.getCubes()) {
            renderCube(poseStack, cube, consumer, light);
        }
        for (GeoBone child : bone.getChildBones()) {
            renderBone(poseStack, child, consumer, light);
        }

        poseStack.popPose();
    }

    private void renderCube(PoseStack poseStack, GeoCube cube,
                            VertexConsumer consumer, int light) {
        Matrix4f pose = poseStack.last().pose();
        for (GeoQuad quad : cube.quads()) {
            if (quad == null) continue;
            for (GeoVertex vertex : quad.vertices()) {
                consumer.addVertex(pose,
                                vertex.position().x(),
                                vertex.position().y(),
                                vertex.position().z())
                        .setColor(1f, 1f, 1f, 1f)
                        .setUv(vertex.texU(), vertex.texV())
                        .setOverlay(OverlayTexture.NO_OVERLAY)
                        .setLight(light)
                        .setNormal(poseStack.last(),
                                quad.normal().x(),
                                quad.normal().y(),
                                quad.normal().z());
            }
        }
    }
}
