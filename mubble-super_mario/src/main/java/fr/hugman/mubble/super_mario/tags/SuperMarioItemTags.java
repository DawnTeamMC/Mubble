package fr.hugman.mubble.super_mario.tags;

import fr.hugman.mubble.super_mario.SuperMario;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class SuperMarioItemTags {
	public static final TagKey<Item> COINS = bind("coins");
	public static final TagKey<Item> KOOPA_SHELLS = bind("koopa_shells");

	public static TagKey<Item> bind(String path) {
		return TagKey.create(Registries.ITEM, SuperMario.id(path));
	}
}
