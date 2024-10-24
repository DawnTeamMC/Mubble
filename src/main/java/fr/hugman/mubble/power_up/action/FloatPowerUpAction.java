package fr.hugman.mubble.power_up.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public record FloatPowerUpAction(
        int ignoreMe
) implements PowerUpAction {
    public static final MapCodec<FloatPowerUpAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.optionalFieldOf("ignoreMe", 0).forGetter(FloatPowerUpAction::ignoreMe)
    ).apply(instance, FloatPowerUpAction::new));

    public static final PacketCodec<RegistryByteBuf, FloatPowerUpAction> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, (FloatPowerUpAction::ignoreMe),
            FloatPowerUpAction::new
    );

    @Override
    public PowerUpActionType<?> getType() {
        return PowerUpActionTypes.FLOAT;
    }

    @Override
    public void onTrigger(MinecraftServer server, ServerPlayerEntity player) {
    }
}
