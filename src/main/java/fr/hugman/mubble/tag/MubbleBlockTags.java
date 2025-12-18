package fr.hugman.mubble.tag;

import fr.hugman.mubble.Mubble;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class MubbleBlockTags {
    public static final TagKey<Block> MELTABLE_TO_ICE = of("meltable/ice");
    public static final TagKey<Block> MELTABLE_TO_WATER = of("meltable/water");
    public static final TagKey<Block> FREEZABLE_TO_PACKED_ICE = of("freezable/to_packed_ice");

	// SUPER MARIO
	public static final TagKey<Block> BRICK_BLOCKS = of("brick_blocks");
	public static final TagKey<Block> EXCLAMATION_BLOCKS = of("exclamation_blocks");
	public static final TagKey<Block> MARIMBA_BLOCKS = of("marimba_blocks");
	public static final TagKey<Block> SNAKE_BLOCKS = of("snake_blocks");
	public static final TagKey<Block> BEEP_BLOCKS = of("beep_blocks");

	// YOSHI'S ISLAND
	public static final TagKey<Block> EGG_BLOCKS = of("egg_blocks");

	public static TagKey<Block> of(String path) {
		return TagKey.create(Registries.BLOCK, Mubble.id(path));
	}
}
