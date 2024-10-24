package fr.hugman.mubble.network.payload;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.network.payload.c2s.PowerUpChangePayload;
import fr.hugman.mubble.network.payload.c2s.PowerUpJumpTriggerPayload;
import fr.hugman.mubble.network.payload.c2s.PowerUpTriggerPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.packet.CustomPayload;

public class MubblePayloads {
    public static final CustomPayload.Id<PowerUpTriggerPayload> POWER_UP_TRIGGER = of("power_up/trigger");
    public static final CustomPayload.Id<PowerUpJumpTriggerPayload> POWER_UP_JUMP_TRIGGER = of("power_up/trigger_jump");
    public static final CustomPayload.Id<PowerUpChangePayload> POWER_UP_CHANGE = of("power_up/change");

    public static <T extends CustomPayload> CustomPayload.Id<T> of(String path) {
        return new CustomPayload.Id<>(Mubble.id(path));
    }

    public static void registerTypes() {
        PayloadTypeRegistry.playC2S().register(MubblePayloads.POWER_UP_TRIGGER, PowerUpTriggerPayload.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(MubblePayloads.POWER_UP_JUMP_TRIGGER, PowerUpJumpTriggerPayload.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(MubblePayloads.POWER_UP_CHANGE, PowerUpChangePayload.PACKET_CODEC);
    }
}
