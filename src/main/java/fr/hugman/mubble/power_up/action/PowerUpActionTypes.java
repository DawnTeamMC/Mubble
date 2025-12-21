package fr.hugman.mubble.power_up.action;

import com.mojang.serialization.MapCodec;
import fr.hugman.mubble.core.registries.MubbleBuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;

public class PowerUpActionTypes {
    public static final PowerUpActionType<ShootProjectilePowerUpAction> SHOOT_PROJECTILE = of(PowerUpActionTypeKeys.SHOOT_PROJECTILE, ShootProjectilePowerUpAction.CODEC, ShootProjectilePowerUpAction.PACKET_CODEC);

    public static <T extends PowerUpAction> PowerUpActionType<T> of(ResourceKey<PowerUpActionType<?>> key, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> packetCodec) {
        return Registry.register(MubbleBuiltInRegistries.POWER_UP_ACTION_TYPE, key, new PowerUpActionType<>(codec, packetCodec));
    }
}