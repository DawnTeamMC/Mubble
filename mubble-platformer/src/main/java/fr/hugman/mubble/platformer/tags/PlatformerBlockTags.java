package fr.hugman.mubble.platformer.tags;

import fr.hugman.mubble.tags.MubbleBlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class PlatformerBlockTags {
    /** Walls a player cannot slide down nor kick off from, on top of the slippery ones. */
    public static final TagKey<Block> NO_WALL_JUMP = MubbleBlockTags.bind("no_wall_jump");
    /** Edges a player cannot hang from. */
    public static final TagKey<Block> NO_LEDGE_GRAB = MubbleBlockTags.bind("no_ledge_grab");
    /** Blocks that let speed carry on instead of braking it, on top of the slippery ones: speed above the run speed does not decay on them, and releasing the stick does not stop the player. */
    public static final TagKey<Block> KEEPS_MOMENTUM = MubbleBlockTags.bind("keeps_momentum");
    /**
     * Blocks never taken as slippery, whatever their friction: they neither keep momentum nor refuse
     * wall jumps, unless they are also tagged for it.
     */
    public static final TagKey<Block> NOT_SLIPPERY = MubbleBlockTags.bind("not_slippery");
}
