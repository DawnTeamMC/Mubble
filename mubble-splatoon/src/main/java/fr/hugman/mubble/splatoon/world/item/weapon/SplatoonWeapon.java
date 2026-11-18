package fr.hugman.mubble.splatoon.world.item.weapon;

import com.mojang.serialization.Codec;
import fr.hugman.mubble.splatoon.core.registries.SplatoonBuiltInRegistries;
import fr.hugman.mubble.splatoon.core.registries.SplatoonRegistries;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;

public interface SplatoonWeapon {
    Codec<SplatoonWeapon> DIRECT_CODEC = SplatoonBuiltInRegistries.SPLATOON_WEAPON_TYPE.byNameCodec().dispatch(SplatoonWeapon::getType, SplatoonWeaponType::codec);
    StreamCodec<RegistryFriendlyByteBuf, SplatoonWeapon> DIRECT_STREAM_CODEC = ByteBufCodecs.registry(SplatoonRegistries.SPLATOON_WEAPON_TYPE).dispatch(SplatoonWeapon::getType, SplatoonWeaponType::streamCodec);

    Codec<Holder<SplatoonWeapon>> CODEC = RegistryFileCodec.create(SplatoonRegistries.SPLATOON_WEAPON, DIRECT_CODEC);
    StreamCodec<RegistryFriendlyByteBuf, Holder<SplatoonWeapon>> STREAM_CODEC = ByteBufCodecs.holder(SplatoonRegistries.SPLATOON_WEAPON, DIRECT_STREAM_CODEC);

    SplatoonWeaponType<?> getType();
}