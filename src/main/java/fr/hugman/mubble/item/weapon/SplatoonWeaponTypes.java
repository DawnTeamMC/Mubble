package fr.hugman.mubble.item.weapon;

import com.mojang.serialization.MapCodec;
import fr.hugman.mubble.registry.MubbleRegistries;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class SplatoonWeaponTypes {
    public static final SplatoonWeaponType<AutomaticShooterConfig> AUTOMATIC_SHOOTER = of(SplatoonWeaponTypeKeys.AUTOMATIC_SHOOTER, AutomaticShooterConfig.CODEC, AutomaticShooterConfig.PACKET_CODEC);

    public static <T extends SplatoonWeapon> SplatoonWeaponType<T> of(RegistryKey<SplatoonWeaponType<?>> key, MapCodec<T> codec, PacketCodec<? super RegistryByteBuf, T> packetCodec) {
        return Registry.register(MubbleRegistries.SPLATOON_WEAPON_TYPE, key, new SplatoonWeaponType<>(codec, packetCodec));
    }
}
