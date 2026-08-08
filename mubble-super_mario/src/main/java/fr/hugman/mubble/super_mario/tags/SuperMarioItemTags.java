package fr.hugman.mubble.super_mario.tags;

import fr.hugman.mubble.super_mario.SuperMario;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class SuperMarioItemTags {
	public static final TagKey<Item> COINS = bind("coins");
	public static final TagKey<Item> KOOPA_SHELLS = bind("koopa_shells");

	/** Items that come back to the world as a {@code mubble:collectible} rather than as a plain item entity. */
	public static final TagKey<Item> SPAWNS_AS_COLLECTIBLE = bind("spawns_as_collectible");

	public static TagKey<Item> bind(String path) {
		return TagKey.create(Registries.ITEM, SuperMario.id(path));
	}
}
