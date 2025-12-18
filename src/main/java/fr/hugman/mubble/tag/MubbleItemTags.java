package fr.hugman.mubble.tag;

import fr.hugman.mubble.Mubble;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class MubbleItemTags {
	// SUPER MARIO
	public static final TagKey<Item> KOOPA_SHELLS = of("koopa_shells");

	public static TagKey<Item> of(String path) {
		return TagKey.create(Registries.ITEM, Mubble.id(path));
	}
}
