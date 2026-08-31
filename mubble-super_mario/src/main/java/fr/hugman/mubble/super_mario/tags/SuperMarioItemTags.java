package fr.hugman.mubble.super_mario.tags;

import fr.hugman.mubble.super_mario.SuperMario;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class SuperMarioItemTags {
	public static final TagKey<Item> COINS = bind("coins");
	public static final TagKey<Item> KOOPA_SHELLS = bind("koopa_shells");

	public static final TagKey<Item> BUBBLE_CATCH_AS_COLLECTIBLE = bind("bubble/catch_as_collectible");

	public static final TagKey<Item> BRICK_BLOCKS = bind("brick_blocks");
	public static final TagKey<Item> EXCLAMATION_BLOCKS = bind("exclamation_blocks");
	public static final TagKey<Item> MARIMBA_BLOCKS = bind("marimba_blocks");
	public static final TagKey<Item> SNAKE_BLOCKS = bind("snake_blocks");
	public static final TagKey<Item> BEEP_BLOCKS = bind("beep_blocks");

	public static final TagKey<Item> EGG_BLOCKS = bind("egg_blocks");

	public static TagKey<Item> bind(String path) {
		return TagKey.create(Registries.ITEM, SuperMario.id(path));
	}
}
