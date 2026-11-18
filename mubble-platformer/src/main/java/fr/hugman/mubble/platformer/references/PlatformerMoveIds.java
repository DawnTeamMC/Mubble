package fr.hugman.mubble.platformer.references;

import fr.hugman.mubble.platformer.registries.PlatformerRegistries;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.platformer.move.PlatformerMove;
import net.minecraft.resources.ResourceKey;

public class PlatformerMoveIds {
    // the states that are not moves of their own
    public static final ResourceKey<PlatformerMove> WALK = createKey("walk");
    public static final ResourceKey<PlatformerMove> FALL = createKey("fall");
    public static final ResourceKey<PlatformerMove> LAND = createKey("land");

    // tier 1
    public static final ResourceKey<PlatformerMove> RUN = createKey("run");
    public static final ResourceKey<PlatformerMove> SKID = createKey("skid");
    public static final ResourceKey<PlatformerMove> JUMP = createKey("jump");
    public static final ResourceKey<PlatformerMove> DOUBLE_JUMP = createKey("double_jump");
    public static final ResourceKey<PlatformerMove> TRIPLE_JUMP = createKey("triple_jump");
    public static final ResourceKey<PlatformerMove> CROUCH = createKey("crouch");
    public static final ResourceKey<PlatformerMove> GROUND_POUND = createKey("ground_pound");
    public static final ResourceKey<PlatformerMove> GROUND_POUND_LAND = createKey("ground_pound_land");
    public static final ResourceKey<PlatformerMove> GROUND_POUND_JUMP = createKey("ground_pound_jump");
    public static final ResourceKey<PlatformerMove> LEDGE_GRAB = createKey("ledge_grab");
    public static final ResourceKey<PlatformerMove> LEDGE_CLIMB = createKey("ledge_climb");
    public static final ResourceKey<PlatformerMove> WALL_SLIDE = createKey("wall_slide");
    public static final ResourceKey<PlatformerMove> WALL_JUMP = createKey("wall_jump");

    // tier 2
    public static final ResourceKey<PlatformerMove> ROLL = createKey("roll");
    public static final ResourceKey<PlatformerMove> ROLL_JUMP = createKey("roll_jump");
    public static final ResourceKey<PlatformerMove> LONG_JUMP = createKey("long_jump");
    public static final ResourceKey<PlatformerMove> BACKFLIP = createKey("backflip");
    public static final ResourceKey<PlatformerMove> SIDE_SOMERSAULT = createKey("side_somersault");
    public static final ResourceKey<PlatformerMove> DIVE = createKey("dive");
    public static final ResourceKey<PlatformerMove> ROLLOUT = createKey("rollout");

    // tier 3
    public static final ResourceKey<PlatformerMove> SPIN = createKey("spin");
    public static final ResourceKey<PlatformerMove> VAULT = createKey("vault");
    public static final ResourceKey<PlatformerMove> SLIDE = createKey("slide");

    // water
    public static final ResourceKey<PlatformerMove> SWIM = createKey("swim");
    public static final ResourceKey<PlatformerMove> SWIM_DASH = createKey("swim_dash");

    // climbing
    public static final ResourceKey<PlatformerMove> CLIMB = createKey("climb");

    private static ResourceKey<PlatformerMove> createKey(String path) {
        return ResourceKey.create(PlatformerRegistries.PLATFORMER_MOVE, Mubble.id(path));
    }
}
