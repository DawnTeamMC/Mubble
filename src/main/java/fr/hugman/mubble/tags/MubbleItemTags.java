package fr.hugman.mubble.tags;

import fr.hugman.mubble.Mubble;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class MubbleItemTags {
	// SUPER MARIO
	public static final TagKey<Item> COINS = bind("coins");
	public static final TagKey<Item> KOOPA_SHELLS = bind("koopa_shells");

	public static TagKey<Item> bind(String path) {
		return TagKey.create(Registries.ITEM, Mubble.id(path));
	}
}
