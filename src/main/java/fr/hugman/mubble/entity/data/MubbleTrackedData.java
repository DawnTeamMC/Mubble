package fr.hugman.mubble.entity.data;

import fr.hugman.mubble.entity.GoombaVariant;
import fr.hugman.mubble.registry.MubbleRegistryKeys;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryEntry;

public class MubbleTrackedData {
    public static final TrackedDataHandler<RegistryEntry<GoombaVariant>> GOOMBA_VARIANT = of(PacketCodecs.registryEntry(MubbleRegistryKeys.GOOMBA_VARIANT));

    public static <T> TrackedDataHandler<T> of(PacketCodec<? super RegistryByteBuf, T> codec) {
        var handler = TrackedDataHandler.create(codec);
        TrackedDataHandlerRegistry.register(handler);
        return handler;
    }
}
