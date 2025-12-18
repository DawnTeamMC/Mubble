package fr.hugman.mubble.tags;

import fr.hugman.mubble.Mubble;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class MubbleBlockTags {
    public static final TagKey<Block> MELTABLE_TO_ICE = bind("meltable/ice");
    public static final TagKey<Block> MELTABLE_TO_WATER = bind("meltable/water");
    public static final TagKey<Block> FREEZABLE_TO_PACKED_ICE = bind("freezable/to_packed_ice");

	// SUPER MARIO
	public static final TagKey<Block> BRICK_BLOCKS = bind("brick_blocks");
	public static final TagKey<Block> EXCLAMATION_BLOCKS = bind("exclamation_blocks");
	public static final TagKey<Block> MARIMBA_BLOCKS = bind("marimba_blocks");
	public static final TagKey<Block> SNAKE_BLOCKS = bind("snake_blocks");
	public static final TagKey<Block> BEEP_BLOCKS = bind("beep_blocks");

	// YOSHI'S ISLAND
	public static final TagKey<Block> EGG_BLOCKS = bind("egg_blocks");

	public static TagKey<Block> bind(String path) {
		return TagKey.create(Registries.BLOCK, Mubble.id(path));
	}
}
