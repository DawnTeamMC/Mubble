package fr.hugman.mubble.item.consume;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

public class MubbleConsumeEffectTypes {
    public static final ConsumeEffect.Type<ChangePowerUpConsumeEffect> CHANGE_POWER_UP = of(MubbleConsumeEffectTypeKeys.CHANGE_POWER_UP, ChangePowerUpConsumeEffect.CODEC, ChangePowerUpConsumeEffect.STREAM_CODEC);

    private static <T extends ConsumeEffect> ConsumeEffect.Type<T> of(ResourceKey<ConsumeEffect.Type<?>> key, MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> packetCodec) {
        return Registry.register(BuiltInRegistries.CONSUME_EFFECT_TYPE, key, new ConsumeEffect.Type<>(codec, packetCodec));
    }
}
