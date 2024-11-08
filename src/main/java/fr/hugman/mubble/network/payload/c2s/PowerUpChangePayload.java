package fr.hugman.mubble.network.payload.c2s;

import fr.hugman.mubble.network.payload.MubblePayloads;
import fr.hugman.mubble.power_up.PowerUp;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Optional;

public record PowerUpChangePayload(Optional<RegistryEntry<PowerUp>> previous,
                                   Optional<RegistryEntry<PowerUp>> next) implements CustomPayload {
    public static final PacketCodec<RegistryByteBuf, PowerUpChangePayload> PACKET_CODEC = PacketCodec.tuple(
            PowerUp.OPTIONAL_ENTRY_PACKET_CODEC, (powerUpChangePayload -> powerUpChangePayload.previous),
            PowerUp.OPTIONAL_ENTRY_PACKET_CODEC, (powerUpChangePayload -> powerUpChangePayload.next),
            PowerUpChangePayload::new
    );

    @Override
    public Id<? extends PowerUpChangePayload> getId() {
        return MubblePayloads.POWER_UP_CHANGE;
    }
}
