package fr.hugman.mubble.world.power_up.action;

import com.mojang.serialization.MapCodec;
import fr.hugman.mubble.core.registries.MubbleBuiltInRegistries;
import fr.hugman.mubble.references.PowerUpActionTypesKeys;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;

public class PowerUpActionTypes {
    public static final PowerUpActionType<ShootProjectilePowerUpAction> SHOOT_PROJECTILE = register(PowerUpActionTypesKeys.SHOOT_PROJECTILE, ShootProjectilePowerUpAction.CODEC, ShootProjectilePowerUpAction.STREAM_CODEC);
    public static final PowerUpActionType<SummonEntityAtPlayerPowerUpAction> SUMMON_ENTITY_AT_PLAYER = register(PowerUpActionTypesKeys.SUMMON_ENTITY_AT_PLAYER, SummonEntityAtPlayerPowerUpAction.CODEC, SummonEntityAtPlayerPowerUpAction.STREAM_CODEC);

    public static <T extends PowerUpAction> PowerUpActionType<T> register(ResourceKey<PowerUpActionType<?>> key, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return Registry.register(MubbleBuiltInRegistries.POWER_UP_ACTION_TYPE, key, new PowerUpActionType<>(codec, streamCodec));
    }
}