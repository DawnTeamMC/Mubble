package fr.hugman.mubble.super_mario.world.power_up.action;

import com.mojang.serialization.MapCodec;
import fr.hugman.mubble.core.registries.MubbleBuiltInRegistries;
import fr.hugman.mubble.super_mario.references.SuperMarioPowerUpActionTypesKeys;
import fr.hugman.mubble.world.power_up.action.PowerUpAction;
import fr.hugman.mubble.world.power_up.action.PowerUpActionType;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;

public class SuperMarioPowerUpActionTypes {
    public static final PowerUpActionType<SpawnCloudPlatformPowerUpAction> SPAWN_CLOUD_PLATFORM = register(SuperMarioPowerUpActionTypesKeys.SPAWN_CLOUD_PLATFORM, SpawnCloudPlatformPowerUpAction.CODEC, SpawnCloudPlatformPowerUpAction.STREAM_CODEC);
    public static final PowerUpActionType<ShootBubblePowerUpAction> SHOOT_BUBBLE = register(SuperMarioPowerUpActionTypesKeys.SHOOT_BUBBLE, ShootBubblePowerUpAction.CODEC, ShootBubblePowerUpAction.STREAM_CODEC);

    public static <T extends PowerUpAction> PowerUpActionType<T> register(ResourceKey<PowerUpActionType<?>> key, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return Registry.register(MubbleBuiltInRegistries.POWER_UP_ACTION_TYPE, key, new PowerUpActionType<>(codec, streamCodec));
    }
}