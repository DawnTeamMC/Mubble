package fr.hugman.mubble.component;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.entity.GoombaVariant;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.dynamic.Codecs;

import java.util.function.UnaryOperator;

public class MubbleDataComponentTypes {
	public static final ComponentType<PowerUpComponent> POWER_UP = register("power_up", builder -> builder.codec(PowerUpComponent.CODEC).packetCodec(PowerUpComponent.PACKET_CODEC).cache());
	public static final ComponentType<RegistryEntry<GoombaVariant>> GOOMBA_VARIANT = register(
			"goomba/variant", builder -> builder.codec(GoombaVariant.ENTRY_CODEC).packetCodec(GoombaVariant.ENTRY_PACKET_CODEC)
	);

	private static <T> ComponentType<T> register(String path, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
		return Registry.register(Registries.DATA_COMPONENT_TYPE, Mubble.id(path), builderOperator.apply(ComponentType.builder()).build());
	}
}
