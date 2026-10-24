package fr.hugman.mubble.splatoon.network.protocol.common.custom;

import fr.hugman.mubble.splatoon.Splatoon;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class SplatoonPayloadTypes {
    public static final CustomPacketPayload.Type<InkSyncPayload> INK_SYNC = of("ink/sync");

    public static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> of(String path) {
        return new CustomPacketPayload.Type<>(Splatoon.id(path));
    }

    public static void registerTypes() {
        PayloadTypeRegistry.clientboundPlay().register(INK_SYNC, InkSyncPayload.STREAM_CODEC);
    }
}
