package fr.hugman.mubble.tag;

import fr.hugman.mubble.Mubble;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class MubbleBlockTags {
    public static final TagKey<Block> MELTABLE_TO_AIR = of("meltable/air");
    public static final TagKey<Block> MELTABLE_TO_ICE = of("meltable/ice");
    public static final TagKey<Block> MELTABLE_TO_WATER = of("meltable/water");

	// SUPER MARIO
	public static final TagKey<Block> BRICK_BLOCKS = of("brick_blocks");
	public static final TagKey<Block> EXCLAMATION_BLOCKS = of("exclamation_blocks");
	public static final TagKey<Block> MARIMBA_BLOCKS = of("marimba_blocks");
	public static final TagKey<Block> SNAKE_BLOCKS = of("snake_blocks");
	public static final TagKey<Block> BEEP_BLOCKS = of("beep_blocks");

	// YOSHI'S ISLAND
	public static final TagKey<Block> EGG_BLOCKS = of("egg_blocks");

	public static TagKey<Block> of(String path) {
		return TagKey.of(RegistryKeys.BLOCK, Mubble.id(path));
	}
}
