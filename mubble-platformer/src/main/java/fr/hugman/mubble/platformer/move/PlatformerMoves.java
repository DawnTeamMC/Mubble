package fr.hugman.mubble.platformer.move;

import fr.hugman.mubble.platformer.registries.PlatformerBuiltInRegistries;
import fr.hugman.mubble.platformer.references.PlatformerMoveIds;
import fr.hugman.mubble.platformer.move.PlatformerMove.Kind;
import fr.hugman.mubble.platformer.move.PlatformerMove.Properties;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

/**
 * Every move of the platformer movement layer.
 * <p>
 * The entry priorities decide which move wins when several could start on the same tick: the more
 * specific a move is, the higher it ranks, so that crouching and jumping is a backflip before it is a
 * jump, and jumping right after a ground pound is a ground pound jump before anything else.
 */
public final class PlatformerMoves {
    /** Ticks an airborne move lasts before the aerial follow-ups may cut it short. */
    public static final MoveParam CANCEL_WINDOW = MoveParam.ticks("cancel_window_ticks", 3.0D);

    // the states that are not moves of their own
    public static final PlatformerMove WALK = register(PlatformerMoveIds.WALK, new GroundMove(Properties.of(Kind.GROUND).base(), GroundMove.Variant.WALK));
    public static final PlatformerMove FALL = register(PlatformerMoveIds.FALL, new FallMove(Properties.of(Kind.AIR).base()));
    public static final PlatformerMove LAND = register(PlatformerMoveIds.LAND, new GroundMove(Properties.of(Kind.GROUND).base(), GroundMove.Variant.LAND));

    // tier 1
    public static final PlatformerMove RUN = register(PlatformerMoveIds.RUN, new GroundMove(Properties.of(Kind.GROUND)
            .params(GroundMove.EXHAUSTION_PER_BLOCK, GroundMove.SLOPE_GAIN), GroundMove.Variant.RUN));
    public static final PlatformerMove SKID = register(PlatformerMoveIds.SKID, new SkidMove(Properties.of(Kind.GROUND).facesItsOwnWay().entryPriority(15)
            .params(SkidMove.MIN_SPEED, SkidMove.REVERSE_DOT, SkidMove.TICKS, SkidMove.BRAKE, SkidMove.EXIT_SPEED)));
    public static final PlatformerMove JUMP = register(PlatformerMoveIds.JUMP, new JumpMove(Properties.of(Kind.AIR).entryPriority(55)
            .params(ArcMove.arcParams()), 0));
    public static final PlatformerMove DOUBLE_JUMP = register(PlatformerMoveIds.DOUBLE_JUMP, new JumpMove(Properties.of(Kind.AIR).entryPriority(65)
            .params(ArcMove.arcParams(JumpMove.MIN_SPEED)), 1));
    public static final PlatformerMove TRIPLE_JUMP = register(PlatformerMoveIds.TRIPLE_JUMP, new JumpMove(Properties.of(Kind.AIR).entryPriority(70).handsBusy()
            .params(ArcMove.arcParams(JumpMove.MIN_SPEED)), 2));
    public static final PlatformerMove CROUCH = register(PlatformerMoveIds.CROUCH, new CrouchMove(Properties.of(Kind.GROUND).entryPriority(10)
            .params(CrouchMove.SLIDE_TICKS)));
    public static final PlatformerMove GROUND_POUND = register(PlatformerMoveIds.GROUND_POUND, new GroundPoundMove(Properties.of(Kind.AIR).entryPriority(45).handsBusy()
            .params(GroundPoundMove.HANG_TICKS, GroundPoundMove.DROP_SPEED, GroundPoundMove.MIN_HEIGHT, GroundPoundMove.WATER_DROP)));
    public static final PlatformerMove GROUND_POUND_LAND = register(PlatformerMoveIds.GROUND_POUND_LAND, new GroundPoundLandMove(Properties.of(Kind.GROUND).handsBusy()
            .accessParent(() -> PlatformerMoves.GROUND_POUND)
            .params(GroundPoundLandMove.TICKS, GroundPoundLandMove.JUMP_WINDOW, GroundPoundLandMove.ROLL_WINDOW, GroundPoundLandMove.ROLL_SPEED)));
    public static final PlatformerMove GROUND_POUND_JUMP = register(PlatformerMoveIds.GROUND_POUND_JUMP, new GroundPoundJumpMove(Properties.of(Kind.AIR).entryPriority(100)
            .params(ArcMove.arcParams())));
    public static final PlatformerMove LEDGE_GRAB = register(PlatformerMoveIds.LEDGE_GRAB, new LedgeGrabMove(Properties.of(Kind.ATTACHED).facesItsOwnWay().entryPriority(50).handsBusy()
            .params(LedgeGrabMove.HANG_DEPTH, LedgeGrabMove.GRAB_BAND, LedgeGrabMove.MIN_HEIGHT, LedgeGrabMove.SHIMMY_SPEED)));
    public static final PlatformerMove LEDGE_CLIMB = register(PlatformerMoveIds.LEDGE_CLIMB, new LedgeClimbMove(Properties.of(Kind.ATTACHED).facesItsOwnWay().handsBusy()
            .accessParent(() -> PlatformerMoves.LEDGE_GRAB)
            .params(LedgeClimbMove.TICKS, LedgeClimbMove.FORWARD)));
    public static final PlatformerMove WALL_SLIDE = register(PlatformerMoveIds.WALL_SLIDE, new WallSlideMove(Properties.of(Kind.ATTACHED).facesItsOwnWay().entryPriority(35).handsBusy()
            .params(WallSlideMove.MAX_FALL_SPEED, WallSlideMove.MIN_HEIGHT, WallSlideMove.STICK_DOT, WallSlideMove.REACH)));
    public static final PlatformerMove WALL_JUMP = register(PlatformerMoveIds.WALL_JUMP, new WallJumpMove(Properties.of(Kind.AIR).facesItsOwnWay().entryPriority(95)
            .params(ArcMove.arcParams(WallJumpMove.PUSH, WallJumpMove.LOCK_TICKS))));

    // tier 2
    public static final PlatformerMove ROLL = register(PlatformerMoveIds.ROLL, new RollMove(Properties.of(Kind.GROUND).facesItsOwnWay().entryPriority(30).handsBusy()
            .params(RollMove.SPEED, RollMove.BOOST, RollMove.MAX_BOOSTS, RollMove.BOOST_COOLDOWN, RollMove.DECEL, RollMove.MIN_SPEED, RollMove.TURN_SPEED, RollMove.SLOPE_GAIN, RollMove.CLIMB_HEIGHT)));
    public static final PlatformerMove ROLL_JUMP = register(PlatformerMoveIds.ROLL_JUMP, new RollJumpMove(Properties.of(Kind.AIR).facesItsOwnWay().entryPriority(85)
            .params(ArcMove.arcParams())));
    public static final PlatformerMove LONG_JUMP = register(PlatformerMoveIds.LONG_JUMP, new LongJumpMove(Properties.of(Kind.AIR).facesItsOwnWay().entryPriority(80)
            .params(ArcMove.arcParams(LongJumpMove.SPEED, LongJumpMove.MIN_SPEED))));
    public static final PlatformerMove BACKFLIP = register(PlatformerMoveIds.BACKFLIP, new BackflipMove(Properties.of(Kind.AIR).entryPriority(75).handsBusy()
            .params(ArcMove.arcParams(BackflipMove.BACK_SPEED, BackflipMove.MAX_SPEED))));
    public static final PlatformerMove SIDE_SOMERSAULT = register(PlatformerMoveIds.SIDE_SOMERSAULT, new SideSomersaultMove(Properties.of(Kind.AIR).facesItsOwnWay().entryPriority(90).handsBusy()
            .params(ArcMove.arcParams(SideSomersaultMove.SIDE_SPEED))));
    public static final PlatformerMove DIVE = register(PlatformerMoveIds.DIVE, new DiveMove(Properties.of(Kind.AIR).facesItsOwnWay().entryPriority(40).handsBusy()
            .params(DiveMove.SPEED, DiveMove.LIFT, DiveMove.AIR_CONTROL)));
    public static final PlatformerMove ROLLOUT = register(PlatformerMoveIds.ROLLOUT, new RolloutMove(Properties.of(Kind.GROUND).facesItsOwnWay().handsBusy()
            .accessParent(() -> PlatformerMoves.DIVE)
            .params(RolloutMove.TICKS, RolloutMove.DECEL)));

    // tier 3
    public static final PlatformerMove SPIN = register(PlatformerMoveIds.SPIN, new SpinMove(Properties.of(Kind.AIR).entryPriority(60)
            .params(SpinMove.FALL_SPEED, SpinMove.TICKS, SpinMove.LIFT_HEIGHT, SpinMove.LIFT_TICKS, SpinMove.JUMP_HEIGHT, SpinMove.JUMP_TICKS, SpinMove.AIR_CONTROL, CANCEL_WINDOW)));
    public static final PlatformerMove VAULT = register(PlatformerMoveIds.VAULT, new VaultMove(Properties.of(Kind.GROUND).entryPriority(20)
            .params(VaultMove.MAX_HEIGHT, VaultMove.MIN_SPEED, VaultMove.STICK_DOT, VaultMove.REACH, VaultMove.TICKS)));
    public static final PlatformerMove SLIDE = register(PlatformerMoveIds.SLIDE, new SlideMove(Properties.of(Kind.GROUND).facesItsOwnWay().entryPriority(25)
            .params(SlideMove.MIN_SPEED, SlideMove.MIN_SLOPE, SlideMove.GAIN, SlideMove.FLAT_DECEL, SlideMove.EXIT_SPEED, SlideMove.TURN_SPEED, SlideMove.SLOPE_GAIN)));

    // water
    public static final PlatformerMove SWIM = register(PlatformerMoveIds.SWIM, new SwimMove(Properties.of(Kind.WATER).base()
            .params(SwimMove.ACCEL, SwimMove.DRAG, SwimMove.SPRINT_DRAG, SwimMove.RISE, SwimMove.SINK, SwimMove.STROKE, SwimMove.SURFACE_DEPTH, SwimMove.TURN_SPEED)));
    public static final PlatformerMove SWIM_DASH = register(PlatformerMoveIds.SWIM_DASH, new SwimDashMove(Properties.of(Kind.WATER).facesItsOwnWay().entryPriority(42).handsBusy()
            .accessParent(() -> PlatformerMoves.DIVE)
            .params(SwimDashMove.SPEED, SwimDashMove.TICKS, SwimDashMove.DRAG, SwimDashMove.TURN_SPEED)));

    // climbing
    public static final PlatformerMove CLIMB = register(PlatformerMoveIds.CLIMB, new ClimbMove(Properties.of(Kind.ATTACHED).base()
            .params(ClimbMove.CLIMB_SPEED, ClimbMove.MAX_SPEED, ClimbMove.ACCEL, ClimbMove.TURN_SPEED)));

    /** The moves that may cut an airborne move short once its cancel window is over. */
    private static final Set<PlatformerMove> AERIAL_FOLLOW_UPS = Set.of(GROUND_POUND, DIVE, SPIN, LEDGE_GRAB, WALL_SLIDE, WALL_JUMP);

    private static List<PlatformerMove> entryOrder;

    private PlatformerMoves() {
    }

    private static PlatformerMove register(ResourceKey<PlatformerMove> key, PlatformerMove move) {
        return Registry.register(PlatformerBuiltInRegistries.PLATFORMER_MOVE, key, move);
    }

    /** The plain move of the ground when crouch is not held: running when {@code allowed} lets it, walking otherwise. */
    public static PlatformerMove runOrWalk(Predicate<PlatformerMove> allowed) {
        return allowed.test(RUN) ? RUN : WALK;
    }

    public static boolean isAerialFollowUp(PlatformerMove move) {
        return AERIAL_FOLLOW_UPS.contains(move);
    }

    /** The moves the generic entry loop offers to start, highest priority first. Ties go by id, so that the order never depends on registration. */
    public static List<PlatformerMove> entryOrder() {
        if (entryOrder == null) {
            entryOrder = PlatformerBuiltInRegistries.PLATFORMER_MOVE.stream()
                    .filter(PlatformerMove::isEnteredByItself)
                    .sorted(Comparator.comparingInt(PlatformerMove::entryPriority).reversed().thenComparing(move -> move.id().toString()))
                    .toList();
        }
        return entryOrder;
    }
}
