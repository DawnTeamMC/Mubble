package fr.hugman.mubble.arcade.network;

import fr.hugman.mubble.network.protocol.common.custom.MubblePayloadTypes;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class ArcadePayloadTypes {
    public static final CustomPacketPayload.Type<ArcadeInputPayload> ARCADE_INPUT = MubblePayloadTypes.of("arcade/input");
    public static final CustomPacketPayload.Type<ArcadeCorrectionPayload> ARCADE_CORRECTION = MubblePayloadTypes.of("arcade/correction");
    public static final CustomPacketPayload.Type<ArcadeProfilesPayload> ARCADE_PROFILES = MubblePayloadTypes.of("arcade/profiles");

    public static void registerTypes() {
        PayloadTypeRegistry.serverboundPlay().register(ARCADE_INPUT, ArcadeInputPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ARCADE_CORRECTION, ArcadeCorrectionPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ARCADE_PROFILES, ArcadeProfilesPayload.STREAM_CODEC);
    }
}
