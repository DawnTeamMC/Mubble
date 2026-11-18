package fr.hugman.mubble.splatoon.network.syncher;

import fr.hugman.mubble.splatoon.Splatoon;
import fr.hugman.mubble.splatoon.world.entity.projectile.ShooterInkBulletConfig;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityDataRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;

public class SplatoonEntityDataSerializers {
    public static final EntityDataSerializer<ShooterInkBulletConfig> SHOOTER_INK_BULLET_CONFIG = register("shooter_ink_bullet_config", ShooterInkBulletConfig.STREAM_CODEC);

    public static <T> EntityDataSerializer<T> register(String name, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        var serializer = EntityDataSerializer.forValueType(codec);
        FabricEntityDataRegistry.register(Splatoon.id(name), serializer);
        return serializer;
    }
}
