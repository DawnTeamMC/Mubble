package fr.hugman.mubble.core.component;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.world.item.component.PowerUpComponent;
import fr.hugman.mubble.world.entity.monster.goomba.GoombaVariant;
import java.util.function.UnaryOperator;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

public class MubbleDataComponents {
	public static final DataComponentType<PowerUpComponent> POWER_UP = register("power_up", builder -> builder.persistent(PowerUpComponent.CODEC).networkSynchronized(PowerUpComponent.PACKET_CODEC).cacheEncoding());
	public static final DataComponentType<Holder<GoombaVariant>> GOOMBA_VARIANT = register(
			"goomba/variant", builder -> builder.persistent(GoombaVariant.ENTRY_CODEC).networkSynchronized(GoombaVariant.ENTRY_PACKET_CODEC)
	);

	private static <T> DataComponentType<T> register(String path, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
		return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Mubble.id(path), builderOperator.apply(DataComponentType.builder()).build());
	}
}
