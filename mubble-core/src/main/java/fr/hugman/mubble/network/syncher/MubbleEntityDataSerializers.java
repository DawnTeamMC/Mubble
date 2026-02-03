package fr.hugman.mubble.network.syncher;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.world.power_up.PowerUp;
import fr.hugman.mubble.world.power_up.PowerUpProperties;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityDataRegistry;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import java.util.Optional;

public class MubbleEntityDataSerializers {
    public static final EntityDataSerializer<Optional<Holder<PowerUp>>> OPTIONAL_POWER_UP = register("optional_power_up", PowerUp.OPTIONAL_STREAM_CODEC);
    public static final EntityDataSerializer<PowerUpProperties> POWER_UP_PROPERTIES = register("power_up_properties", PowerUpProperties.PACKET_CODEC);

	public static <T> EntityDataSerializer<T> register(String name, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
		var handler = EntityDataSerializer.forValueType(codec);
		FabricEntityDataRegistry.register(Mubble.id(name), handler);
		return handler;
	}
}
