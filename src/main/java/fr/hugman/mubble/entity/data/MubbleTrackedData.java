package fr.hugman.mubble.entity.data;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.entity.GoombaVariant;
import fr.hugman.mubble.power_up.PowerUp;
import fr.hugman.mubble.power_up.PowerUpProperties;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import java.util.Optional;

public class MubbleTrackedData {
    public static final EntityDataSerializer<Holder<GoombaVariant>> GOOMBA_VARIANT = of("goomba_variant", GoombaVariant.ENTRY_PACKET_CODEC);
    public static final EntityDataSerializer<Optional<Holder<PowerUp>>> OPTIONAL_POWER_UP = of("optional_power_up", PowerUp.OPTIONAL_ENTRY_PACKET_CODEC);
    public static final EntityDataSerializer<PowerUpProperties> POWER_UP_PROPERTIES = of("power_up_properties", PowerUpProperties.PACKET_CODEC);

	public static <T> EntityDataSerializer<T> of(String name, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
		var handler = EntityDataSerializer.forValueType(codec);
		FabricTrackedDataRegistry.register(Mubble.id(name), handler);
		return handler;
	}
}
