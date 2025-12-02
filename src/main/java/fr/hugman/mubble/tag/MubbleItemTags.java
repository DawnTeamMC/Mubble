package fr.hugman.mubble.tag;

import fr.hugman.mubble.Mubble;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class MubbleItemTags {
	// SUPER MARIO
	public static final TagKey<Item> KOOPA_SHELLS = of("koopa_shells");

	public static TagKey<Item> of(String path) {
		return TagKey.of(RegistryKeys.ITEM, Mubble.id(path));
	}
}
