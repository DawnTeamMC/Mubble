package fr.hugman.mubble.power_up.action;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record PowerUpActionType<P extends PowerUpAction>(
		MapCodec<P> codec,
		StreamCodec<? super RegistryFriendlyByteBuf, P> packetCodec
) {
}