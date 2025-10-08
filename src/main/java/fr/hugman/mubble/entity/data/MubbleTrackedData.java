package fr.hugman.mubble.entity.data;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.entity.GoombaVariant;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.entry.RegistryEntry;

public class MubbleTrackedData {
    public static final TrackedDataHandler<RegistryEntry<GoombaVariant>> GOOMBA_VARIANT = of("goomba_variant", GoombaVariant.ENTRY_PACKET_CODEC);

	public static <T> TrackedDataHandler<T> of(String name, PacketCodec<? super RegistryByteBuf, T> codec) {
		var handler = TrackedDataHandler.create(codec);
		FabricTrackedDataRegistry.register(Mubble.id(name), handler);
		return handler;
	}
}
