package fr.hugman.mubble.super_mario.tags;

import fr.hugman.mubble.super_mario.SuperMario;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class SuperMarioBlockTags {
	public static final TagKey<Block> BRICK_BLOCKS = bind("brick_blocks");
	public static final TagKey<Block> EXCLAMATION_BLOCKS = bind("exclamation_blocks");
	public static final TagKey<Block> MARIMBA_BLOCKS = bind("marimba_blocks");
	public static final TagKey<Block> SNAKE_BLOCKS = bind("snake_blocks");
	public static final TagKey<Block> BEEP_BLOCKS = bind("beep_blocks");

	public static final TagKey<Block> GOLD_EXPLOSION_SENSITIVE = bind("gold_explosion_sensitive");

	// YOSHI'S ISLAND
	public static final TagKey<Block> EGG_BLOCKS = bind("egg_blocks");

	public static TagKey<Block> bind(String path) {
		return TagKey.create(Registries.BLOCK, SuperMario.id(path));
	}
}
