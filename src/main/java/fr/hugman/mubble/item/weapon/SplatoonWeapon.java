package fr.hugman.mubble.item.weapon;

import com.mojang.serialization.Codec;
import fr.hugman.mubble.registry.MubbleRegistries;
import fr.hugman.mubble.registry.MubbleRegistryKeys;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;

public abstract class SplatoonWeapon {
    public static final Codec<SplatoonWeapon> TYPE_CODEC = MubbleRegistries.SPLATOON_WEAPON_TYPE.getCodec().dispatch(SplatoonWeapon::getType, SplatoonWeaponType::codec);
    public static final Codec<RegistryEntry<SplatoonWeapon>> ENTRY_CODEC = RegistryElementCodec.of(MubbleRegistryKeys.SPLATOON_WEAPON, TYPE_CODEC);
    public static final PacketCodec<RegistryByteBuf, SplatoonWeapon> PACKET_CODEC = PacketCodecs.registryValue(MubbleRegistryKeys.SPLATOON_WEAPON_TYPE).dispatch(SplatoonWeapon::getType, SplatoonWeaponType::packetCodec);
    public static final PacketCodec<RegistryByteBuf, RegistryEntry<SplatoonWeapon>> ENTRY_PACKET_CODEC = PacketCodecs.registryEntry(MubbleRegistryKeys.SPLATOON_WEAPON, PACKET_CODEC);

    protected abstract SplatoonWeaponType<?> getType();
}
