package com.zitrone.lemonthings.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import com.zitrone.lemonthings.LemonThings;

public class StalactiteShieldModel<T extends Entity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(LemonThings.MODID, "stalactite_shield"), "main");

    private final ModelPart plate;
    private final ModelPart handle;

    public StalactiteShieldModel(ModelPart root) {
        this.plate = root.getChild("plate");
        this.handle = root.getChild("handle");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // plate: from [2,0,5] to [14,22,6] → size [12,22,1], pivot at origin
        root.addOrReplaceChild("plate",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-6.0F, -11.0F, -0.5F, 12.0F, 22.0F, 1.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        // handle: from [7,8,6] to [9,14,12] → size [2,6,6]
        root.addOrReplaceChild("handle",
                CubeListBuilder.create()
                        .texOffs(26, 0)
                        .addBox(-1.0F, -3.0F, 0.5F, 2.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer,
                               int packedLight, int packedOverlay,
                               int color) {
        plate.render(poseStack, buffer, packedLight, packedOverlay, color);
        handle.render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    public ModelPart plate() { return plate; }
    public ModelPart handle() { return handle; }
}
