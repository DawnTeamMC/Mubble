package fr.hugman.mubble.splatoon.world.item.weapon;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record SplatoonWeaponType<P extends SplatoonWeapon>(
        MapCodec<P> codec,
        StreamCodec<? super RegistryFriendlyByteBuf, P> streamCodec
) {
}