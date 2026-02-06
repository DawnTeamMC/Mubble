package fr.hugman.mubble.splatoon.world.item.weapon;

import com.mojang.serialization.MapCodec;
import fr.hugman.mubble.splatoon.core.registries.SplatoonBuiltInRegistries;
import fr.hugman.mubble.splatoon.references.SplatoonWeaponTypeKeys;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;

public class SplatoonWeaponTypes {
    public static final SplatoonWeaponType<AutomaticShooterConfig> AUTOMATIC_SHOOTER = register(SplatoonWeaponTypeKeys.AUTOMATIC_SHOOTER, AutomaticShooterConfig.CODEC, AutomaticShooterConfig.STREAM_CODEC);

    public static <T extends SplatoonWeapon> SplatoonWeaponType<T> register(ResourceKey<SplatoonWeaponType<?>> key, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return Registry.register(SplatoonBuiltInRegistries.SPLATOON_WEAPON_TYPE, key, new SplatoonWeaponType<>(codec, streamCodec));
    }
}
