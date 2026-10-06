package fr.hugman.mubble.splatoon.core.component;

import fr.hugman.mubble.splatoon.Splatoon;
import fr.hugman.mubble.splatoon.world.item.weapon.SplatoonWeapon;
import fr.hugman.mubble.splatoon.world.level.ink.InkStyle;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.UnaryOperator;

public class SplatoonDataComponents {
	public static final DataComponentType<Holder<SplatoonWeapon>> SPLATOON_WEAPON = register("splatoon_weapon", builder -> builder.persistent(SplatoonWeapon.CODEC).networkSynchronized(SplatoonWeapon.STREAM_CODEC).cacheEncoding());
	/**
	 * The ink an item shoots, which is all a weapon needs to paint in any color: {@code splatoon:ink={color:"#FF00FF"}}.
	 */
	public static final DataComponentType<InkStyle> INK = register("ink", builder -> builder.persistent(InkStyle.CODEC).networkSynchronized(InkStyle.STREAM_CODEC).cacheEncoding());

	private static <T> DataComponentType<T> register(String path, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
		return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Splatoon.id(path), builderOperator.apply(DataComponentType.builder()).build());
	}
}
