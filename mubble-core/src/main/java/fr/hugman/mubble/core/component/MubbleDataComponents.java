package fr.hugman.mubble.core.component;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.world.item.component.PowerUpComponent;
import fr.hugman.mubble.world.voyage.session.VoyageControl;
import java.util.function.UnaryOperator;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

public class MubbleDataComponents {
	public static final DataComponentType<PowerUpComponent> POWER_UP = register("power_up", builder -> builder.persistent(PowerUpComponent.CODEC).networkSynchronized(PowerUpComponent.STREAM_CODEC).cacheEncoding());

	/**
	 * Marks one of a voyage's two control items.
	 *
	 * <p>Not network synchronised: the client needs the item's name, which is a vanilla custom name,
	 * and the marker is only ever read where the decision is made.
	 */
	public static final DataComponentType<VoyageControl> VOYAGE_CONTROL = register("voyage_control", builder -> builder.persistent(VoyageControl.CODEC));

	private static <T> DataComponentType<T> register(String path, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
		return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Mubble.id(path), builderOperator.apply(DataComponentType.builder()).build());
	}
}
