package fr.hugman.mubble.tags;

import fr.hugman.mubble.Mubble;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class MubbleBlockTags {
    public static final TagKey<Block> MELTABLE_TO_ICE = bind("meltable/ice");
    public static final TagKey<Block> MELTABLE_TO_WATER = bind("meltable/water");
    public static final TagKey<Block> FREEZABLE_TO_PACKED_ICE = bind("freezable/to_packed_ice");

    /** Walls a player cannot slide down nor kick off from. */
    public static final TagKey<Block> NO_WALL_JUMP = bind("no_wall_jump");
    /** Edges a player cannot hang from. */
    public static final TagKey<Block> NO_LEDGE_GRAB = bind("no_ledge_grab");
    /** Blocks a landing bounces off, high. */
    public static final TagKey<Block> BOUNCE = bind("bounce");
    /** Blocks that let speed carry on instead of braking it: speed above the run speed does not decay on them, and releasing the stick does not stop the player. */
    public static final TagKey<Block> KEEPS_MOMENTUM = bind("keeps_momentum");

	public static TagKey<Block> bind(String path) {
		return TagKey.create(Registries.BLOCK, Mubble.id(path));
	}
}
