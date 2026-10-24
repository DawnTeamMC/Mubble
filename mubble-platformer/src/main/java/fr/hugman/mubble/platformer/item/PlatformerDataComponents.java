package fr.hugman.mubble.platformer.item;

import fr.hugman.mubble.Mubble;
import java.util.function.UnaryOperator;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

public class PlatformerDataComponents {
	/** Makes an item a source of platformer movement while worn, see {@link PlatformerMovementComponent}. */
	public static final DataComponentType<PlatformerMovementComponent> PLATFORMER_MOVEMENT = register("platformer_movement", builder -> builder.persistent(PlatformerMovementComponent.CODEC).networkSynchronized(PlatformerMovementComponent.STREAM_CODEC).cacheEncoding());

	private static <T> DataComponentType<T> register(String path, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
		return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Mubble.id(path), builderOperator.apply(DataComponentType.builder()).build());
	}
}
