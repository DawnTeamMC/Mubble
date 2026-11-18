package fr.hugman.mubble.platformer.tags;

import fr.hugman.mubble.platformer.registries.PlatformerRegistries;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.platformer.move.PlatformerMove;
import net.minecraft.tags.TagKey;

public class PlatformerMoveTags {
    /** Every move that is a move of its own, which is what the Mario Boots enable. */
    public static final TagKey<PlatformerMove> ALL = bind("all");
    /** Moves happening in the air. */
    public static final TagKey<PlatformerMove> AERIAL = bind("aerial");
    /** Moves using walls. */
    public static final TagKey<PlatformerMove> WALL = bind("wall");
    /** Moves using ledges. */
    public static final TagKey<PlatformerMove> LEDGE = bind("ledge");
    /** Moves meant for speed, which like a vanilla sprint need enough food when the profile says so. */
    public static final TagKey<PlatformerMove> SPEED = bind("speed");
    /** Moves happening on the ground. */
    public static final TagKey<PlatformerMove> GROUND = bind("ground");

    public static TagKey<PlatformerMove> bind(String path) {
        return TagKey.create(PlatformerRegistries.PLATFORMER_MOVE, Mubble.id(path));
    }
}
