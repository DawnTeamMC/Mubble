package fr.hugman.mubble.super_mario.network.protocol.common.custom;

import fr.hugman.mubble.super_mario.SuperMario;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class SuperMarioPayloadTypes {
    public static final CustomPacketPayload.Type<StruggleFreePayload> STRUGGLE_FREE = of("freeze/struggle");

    public static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> of(String path) {
        return new CustomPacketPayload.Type<>(SuperMario.id(path));
    }

    public static void registerTypes() {
        PayloadTypeRegistry.serverboundPlay().register(SuperMarioPayloadTypes.STRUGGLE_FREE, StruggleFreePayload.STREAM_CODEC);
    }
}
