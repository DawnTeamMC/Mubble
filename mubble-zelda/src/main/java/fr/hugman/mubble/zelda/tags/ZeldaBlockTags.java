package fr.hugman.mubble.zelda.tags;

import fr.hugman.mubble.zelda.Zelda;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ZeldaBlockTags {
	/** What a glass bottle can be filled with spores from. */
	public static final TagKey<Block> SPORE_SOURCES = bind("spore_sources");

	public static TagKey<Block> bind(String path) {
		return TagKey.create(Registries.BLOCK, Zelda.id(path));
	}
}
