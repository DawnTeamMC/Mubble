package fr.hugman.mubble.item.consume;

import com.mojang.serialization.MapCodec;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class MubbleConsumeEffectTypes {
    public static final ConsumeEffect.Type<ChangePowerUpConsumeEffect> CHANGE_POWER_UP = of(MubbleConsumeEffectTypeKeys.CHANGE_POWER_UP, ChangePowerUpConsumeEffect.CODEC, ChangePowerUpConsumeEffect.PACKET_CODEC);

    private static <T extends ConsumeEffect> ConsumeEffect.Type<T> of(RegistryKey<ConsumeEffect.Type<?>> key, MapCodec<T> codec, PacketCodec<RegistryByteBuf, T> packetCodec) {
        return Registry.register(Registries.CONSUME_EFFECT_TYPE, key, new ConsumeEffect.Type<>(codec, packetCodec));
    }
}
