package fr.hugman.mubble.power_up.action;

import com.mojang.serialization.MapCodec;
import fr.hugman.mubble.registry.MubbleRegistries;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class PowerUpActionTypes {
    public static final PowerUpActionType<ShootProjectilePowerUpAction> SHOOT_PROJECTILE = of(PowerUpActionTypeKeys.SHOOT_PROJECTILE, ShootProjectilePowerUpAction.CODEC, ShootProjectilePowerUpAction.PACKET_CODEC);
    public static final PowerUpActionType<FloatPowerUpAction> FLOAT = of(PowerUpActionTypeKeys.FLOAT, FloatPowerUpAction.CODEC, FloatPowerUpAction.PACKET_CODEC);

    public static <T extends PowerUpAction> PowerUpActionType<T> of(RegistryKey<PowerUpActionType<?>> key, MapCodec<T> codec, PacketCodec<? super RegistryByteBuf, T> packetCodec) {
        return Registry.register(MubbleRegistries.POWER_UP_ACTION_TYPE, key, new PowerUpActionType<>(codec, packetCodec));
    }
}