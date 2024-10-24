package fr.hugman.mubble.power_up.action;

import com.mojang.serialization.Codec;
import fr.hugman.mubble.registry.MubbleRegistries;
import fr.hugman.mubble.registry.MubbleRegistryKeys;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public interface PowerUpAction {
    Codec<PowerUpAction> TYPE_CODEC = MubbleRegistries.POWER_UP_ACTION_TYPE.getCodec().dispatch(PowerUpAction::getType, PowerUpActionType::codec);
    PacketCodec<RegistryByteBuf, PowerUpAction> TYPE_PACKET_CODEC = PacketCodecs.registryValue(MubbleRegistryKeys.POWER_UP_ACTION_TYPE).dispatch(PowerUpAction::getType, PowerUpActionType::packetCodec);

    Codec<RegistryEntry<PowerUpAction>> ENTRY_CODEC = RegistryElementCodec.of(MubbleRegistryKeys.POWER_UP_ACTION, TYPE_CODEC);
    PacketCodec<RegistryByteBuf, RegistryEntry<PowerUpAction>> ENTRY_PACKET_CODEC = PacketCodecs.registryEntry(MubbleRegistryKeys.POWER_UP_ACTION, TYPE_PACKET_CODEC);
    PacketCodec<RegistryByteBuf, Optional<RegistryEntry<PowerUpAction>>> OPTIONAL_ENTRY_PACKET_CODEC = ENTRY_PACKET_CODEC.collect(PacketCodecs::optional);

    PowerUpActionType<?> getType();

    void onTrigger(MinecraftServer server, ServerPlayerEntity player);
}
