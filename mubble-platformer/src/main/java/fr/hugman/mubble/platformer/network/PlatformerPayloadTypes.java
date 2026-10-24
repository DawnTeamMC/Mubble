package fr.hugman.mubble.platformer.network;

import fr.hugman.mubble.network.protocol.common.custom.MubblePayloadTypes;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class PlatformerPayloadTypes {
    public static final CustomPacketPayload.Type<PlatformerInputPayload> PLATFORMER_INPUT = MubblePayloadTypes.of("platformer/input");
    public static final CustomPacketPayload.Type<PlatformerCorrectionPayload> PLATFORMER_CORRECTION = MubblePayloadTypes.of("platformer/correction");
    public static final CustomPacketPayload.Type<PlatformerProfilesPayload> PLATFORMER_PROFILES = MubblePayloadTypes.of("platformer/profiles");

    public static void registerTypes() {
        PayloadTypeRegistry.serverboundPlay().register(PLATFORMER_INPUT, PlatformerInputPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(PLATFORMER_CORRECTION, PlatformerCorrectionPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(PLATFORMER_PROFILES, PlatformerProfilesPayload.STREAM_CODEC);
    }
}
