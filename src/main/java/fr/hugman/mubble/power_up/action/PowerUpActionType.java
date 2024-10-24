package fr.hugman.mubble.power_up.action;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record PowerUpActionType<P extends PowerUpAction>(MapCodec<P> codec,
                                                         PacketCodec<? super RegistryByteBuf, P> packetCodec) {
}