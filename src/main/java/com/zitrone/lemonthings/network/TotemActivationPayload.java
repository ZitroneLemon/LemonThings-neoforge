package com.zitrone.lemonthings.network;

import com.zitrone.lemonthings.LemonThings;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.zitrone.lemonthings.item.ModItems;

public record TotemActivationPayload() implements CustomPacketPayload {
    public static final Type<TotemActivationPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(LemonThings.MODID, "totem_activation"));
    public static final StreamCodec<FriendlyByteBuf, TotemActivationPayload> CODEC =
            StreamCodec.unit(new TotemActivationPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(TotemActivationPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Minecraft.getInstance().gameRenderer.displayItemActivation(
                    new ItemStack(ModItems.HANDMADE_TOTEM.get())
            );
        });
    }
}