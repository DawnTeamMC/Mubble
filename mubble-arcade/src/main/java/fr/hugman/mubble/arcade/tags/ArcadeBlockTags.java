package fr.hugman.mubble.arcade.tags;

import fr.hugman.mubble.tags.MubbleBlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ArcadeBlockTags {
    /** Walls a player cannot slide down nor kick off from. */
    public static final TagKey<Block> NO_WALL_JUMP = MubbleBlockTags.bind("no_wall_jump");
    /** Edges a player cannot hang from. */
    public static final TagKey<Block> NO_LEDGE_GRAB = MubbleBlockTags.bind("no_ledge_grab");
    /** Blocks a landing bounces off, high. */
    public static final TagKey<Block> BOUNCE = MubbleBlockTags.bind("bounce");
    /** Blocks that let speed carry on instead of braking it: speed above the run speed does not decay on them, and releasing the stick does not stop the player. */
    public static final TagKey<Block> KEEPS_MOMENTUM = MubbleBlockTags.bind("keeps_momentum");
}
