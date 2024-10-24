package fr.hugman.mubble.entity.data;

import fr.hugman.mubble.entity.GoombaVariant;
import fr.hugman.mubble.power_up.PowerUp;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Optional;

public class MubbleTrackedData {
    public static final TrackedDataHandler<RegistryEntry<GoombaVariant>> GOOMBA_VARIANT = of(GoombaVariant.ENTRY_PACKET_CODEC);
    public static final TrackedDataHandler<Optional<RegistryEntry<PowerUp>>> OPTIONAL_POWER_UP = of(PowerUp.ENTRY_PACKET_CODEC.collect(PacketCodecs::optional));

    public static <T> TrackedDataHandler<T> of(PacketCodec<? super RegistryByteBuf, T> codec) {
        var handler = TrackedDataHandler.create(codec);
        TrackedDataHandlerRegistry.register(handler);
        return handler;
    }
}
