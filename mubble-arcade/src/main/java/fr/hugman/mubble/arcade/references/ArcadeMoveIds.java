package fr.hugman.mubble.arcade.references;

import fr.hugman.mubble.arcade.registries.ArcadeRegistries;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.arcade.move.ArcadeMove;
import net.minecraft.resources.ResourceKey;

public class ArcadeMoveIds {
    // the states that are not moves of their own
    public static final ResourceKey<ArcadeMove> WALK = createKey("walk");
    public static final ResourceKey<ArcadeMove> FALL = createKey("fall");
    public static final ResourceKey<ArcadeMove> LAND = createKey("land");

    // tier 1
    public static final ResourceKey<ArcadeMove> RUN = createKey("run");
    public static final ResourceKey<ArcadeMove> SKID = createKey("skid");
    public static final ResourceKey<ArcadeMove> JUMP = createKey("jump");
    public static final ResourceKey<ArcadeMove> DOUBLE_JUMP = createKey("double_jump");
    public static final ResourceKey<ArcadeMove> TRIPLE_JUMP = createKey("triple_jump");
    public static final ResourceKey<ArcadeMove> CROUCH = createKey("crouch");
    public static final ResourceKey<ArcadeMove> GROUND_POUND = createKey("ground_pound");
    public static final ResourceKey<ArcadeMove> GROUND_POUND_LAND = createKey("ground_pound_land");
    public static final ResourceKey<ArcadeMove> GROUND_POUND_JUMP = createKey("ground_pound_jump");
    public static final ResourceKey<ArcadeMove> LEDGE_GRAB = createKey("ledge_grab");
    public static final ResourceKey<ArcadeMove> LEDGE_CLIMB = createKey("ledge_climb");
    public static final ResourceKey<ArcadeMove> WALL_SLIDE = createKey("wall_slide");
    public static final ResourceKey<ArcadeMove> WALL_JUMP = createKey("wall_jump");

    // tier 2
    public static final ResourceKey<ArcadeMove> ROLL = createKey("roll");
    public static final ResourceKey<ArcadeMove> ROLL_JUMP = createKey("roll_jump");
    public static final ResourceKey<ArcadeMove> LONG_JUMP = createKey("long_jump");
    public static final ResourceKey<ArcadeMove> BACKFLIP = createKey("backflip");
    public static final ResourceKey<ArcadeMove> SIDE_SOMERSAULT = createKey("side_somersault");
    public static final ResourceKey<ArcadeMove> DIVE = createKey("dive");
    public static final ResourceKey<ArcadeMove> ROLLOUT = createKey("rollout");

    // tier 3
    public static final ResourceKey<ArcadeMove> SPIN = createKey("spin");
    public static final ResourceKey<ArcadeMove> VAULT = createKey("vault");
    public static final ResourceKey<ArcadeMove> SLIDE = createKey("slide");

    private static ResourceKey<ArcadeMove> createKey(String path) {
        return ResourceKey.create(ArcadeRegistries.ARCADE_MOVE, Mubble.id(path));
    }
}
