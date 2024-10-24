package fr.hugman.mubble.network.payload.c2s;

import fr.hugman.mubble.network.payload.MubblePayloads;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public class PowerUpJumpTriggerPayload implements CustomPayload {
    public static final PowerUpJumpTriggerPayload INSTANCE = new PowerUpJumpTriggerPayload();
    public static final PacketCodec<RegistryByteBuf, PowerUpJumpTriggerPayload> PACKET_CODEC = PacketCodec.unit(INSTANCE);

    @Override
    public Id<? extends PowerUpJumpTriggerPayload> getId() {
        return MubblePayloads.POWER_UP_JUMP_TRIGGER;
    }
}
