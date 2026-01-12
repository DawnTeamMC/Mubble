package fr.hugman.mubble.network.protocol.common.custom;

import fr.hugman.mubble.Mubble;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class MubblePayloadTypes {
    public static final CustomPacketPayload.Type<CollectCollectiblePayload> COLLECT_COLLECTIBLE = of("collectible/collect");
    public static final CustomPacketPayload.Type<PowerUpTriggerPayload> POWER_UP_TRIGGER = of("power_up/trigger");
    public static final CustomPacketPayload.Type<PowerUpChangePayload> POWER_UP_CHANGE = of("power_up/change");

    public static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> of(String path) {
        return new CustomPacketPayload.Type<>(Mubble.id(path));
    }

    public static void registerTypes() {
        PayloadTypeRegistry.clientboundPlay().register(MubblePayloadTypes.COLLECT_COLLECTIBLE, CollectCollectiblePayload.PACKET_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(MubblePayloadTypes.POWER_UP_TRIGGER, PowerUpTriggerPayload.PACKET_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(MubblePayloadTypes.POWER_UP_CHANGE, PowerUpChangePayload.PACKET_CODEC);
    }
}
