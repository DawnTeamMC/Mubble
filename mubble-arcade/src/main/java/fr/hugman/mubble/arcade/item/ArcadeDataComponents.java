package fr.hugman.mubble.arcade.item;

import fr.hugman.mubble.Mubble;
import java.util.function.UnaryOperator;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

public class ArcadeDataComponents {
	/** Makes an item a source of arcade movement while worn, see {@link ArcadeMovementComponent}. */
	public static final DataComponentType<ArcadeMovementComponent> ARCADE_MOVEMENT = register("arcade_movement", builder -> builder.persistent(ArcadeMovementComponent.CODEC).networkSynchronized(ArcadeMovementComponent.STREAM_CODEC).cacheEncoding());

	private static <T> DataComponentType<T> register(String path, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
		return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Mubble.id(path), builderOperator.apply(DataComponentType.builder()).build());
	}
}
