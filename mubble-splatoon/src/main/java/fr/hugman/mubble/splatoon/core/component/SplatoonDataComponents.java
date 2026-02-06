package fr.hugman.mubble.splatoon.core.component;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.splatoon.world.item.weapon.SplatoonWeapon;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.UnaryOperator;

public class SplatoonDataComponents {
	public static final DataComponentType<Holder<SplatoonWeapon>> SPLATOON_WEAPON = register("splatoon_weapon", builder -> builder.persistent(SplatoonWeapon.CODEC).networkSynchronized(SplatoonWeapon.STREAM_CODEC).cacheEncoding());

	private static <T> DataComponentType<T> register(String path, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
		return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Mubble.id(path), builderOperator.apply(DataComponentType.builder()).build());
	}
}
