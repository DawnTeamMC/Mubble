package fr.hugman.mubble.item.weapon;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record SplatoonWeaponType<P extends SplatoonWeapon>(MapCodec<P> codec,
                                                           PacketCodec<? super RegistryByteBuf, P> packetCodec) {
}
