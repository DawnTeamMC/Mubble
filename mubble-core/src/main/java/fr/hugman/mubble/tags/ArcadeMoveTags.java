package fr.hugman.mubble.tags;

import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.core.registries.MubbleRegistries;
import fr.hugman.mubble.world.arcade.move.ArcadeMove;
import net.minecraft.tags.TagKey;

public class ArcadeMoveTags {
    /** Every move that is a move of its own, which is what the Mario Boots enable. */
    public static final TagKey<ArcadeMove> ALL = bind("all");
    /** Moves happening in the air. */
    public static final TagKey<ArcadeMove> AERIAL = bind("aerial");
    /** Moves using walls. */
    public static final TagKey<ArcadeMove> WALL = bind("wall");
    /** Moves using ledges. */
    public static final TagKey<ArcadeMove> LEDGE = bind("ledge");
    /** Moves meant for speed, which like a vanilla sprint need enough food when the profile says so. */
    public static final TagKey<ArcadeMove> SPEED = bind("speed");
    /** Moves happening on the ground. */
    public static final TagKey<ArcadeMove> GROUND = bind("ground");

    public static TagKey<ArcadeMove> bind(String path) {
        return TagKey.create(MubbleRegistries.ARCADE_MOVE, Mubble.id(path));
    }
}
