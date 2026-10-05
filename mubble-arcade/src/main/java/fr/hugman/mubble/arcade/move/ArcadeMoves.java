package fr.hugman.mubble.arcade.move;

import fr.hugman.mubble.arcade.registries.ArcadeBuiltInRegistries;
import fr.hugman.mubble.arcade.references.ArcadeMoveIds;
import fr.hugman.mubble.arcade.move.ArcadeMove.Kind;
import fr.hugman.mubble.arcade.move.ArcadeMove.Properties;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

/**
 * Every move of the arcade movement layer.
 * <p>
 * The entry priorities decide which move wins when several could start on the same tick: the more
 * specific a move is, the higher it ranks, so that crouching and jumping is a backflip before it is a
 * jump, and jumping right after a ground pound is a ground pound jump before anything else.
 */
public final class ArcadeMoves {
    /** Ticks an airborne move lasts before the aerial follow-ups may cut it short. */
    public static final MoveParam CANCEL_WINDOW = MoveParam.ticks("cancel_window_ticks", 3.0D);

    // the states that are not moves of their own
    public static final ArcadeMove WALK = register(ArcadeMoveIds.WALK, new GroundMove(Properties.of(Kind.GROUND).base(), GroundMove.Variant.WALK));
    public static final ArcadeMove FALL = register(ArcadeMoveIds.FALL, new FallMove(Properties.of(Kind.AIR).base()));
    public static final ArcadeMove LAND = register(ArcadeMoveIds.LAND, new GroundMove(Properties.of(Kind.GROUND).base(), GroundMove.Variant.LAND));

    // tier 1
    public static final ArcadeMove RUN = register(ArcadeMoveIds.RUN, new GroundMove(Properties.of(Kind.GROUND)
            .params(GroundMove.EXHAUSTION_PER_BLOCK, GroundMove.SLOPE_GAIN), GroundMove.Variant.RUN));
    public static final ArcadeMove SKID = register(ArcadeMoveIds.SKID, new SkidMove(Properties.of(Kind.GROUND).entryPriority(15)
            .params(SkidMove.MIN_SPEED, SkidMove.REVERSE_DOT, SkidMove.TICKS, SkidMove.BRAKE, SkidMove.EXIT_SPEED)));
    public static final ArcadeMove JUMP = register(ArcadeMoveIds.JUMP, new JumpMove(Properties.of(Kind.AIR).entryPriority(55)
            .params(ArcMove.arcParams()), 0));
    public static final ArcadeMove DOUBLE_JUMP = register(ArcadeMoveIds.DOUBLE_JUMP, new JumpMove(Properties.of(Kind.AIR).entryPriority(65)
            .params(ArcMove.arcParams(JumpMove.MIN_SPEED)), 1));
    public static final ArcadeMove TRIPLE_JUMP = register(ArcadeMoveIds.TRIPLE_JUMP, new JumpMove(Properties.of(Kind.AIR).entryPriority(70).handsBusy()
            .params(ArcMove.arcParams(JumpMove.MIN_SPEED)), 2));
    public static final ArcadeMove CROUCH = register(ArcadeMoveIds.CROUCH, new CrouchMove(Properties.of(Kind.GROUND).entryPriority(10)
            .params(CrouchMove.SLIDE_TICKS)));
    public static final ArcadeMove GROUND_POUND = register(ArcadeMoveIds.GROUND_POUND, new GroundPoundMove(Properties.of(Kind.AIR).entryPriority(45).handsBusy()
            .params(GroundPoundMove.HANG_TICKS, GroundPoundMove.DROP_SPEED, GroundPoundMove.MIN_HEIGHT, GroundPoundMove.WATER_DROP)));
    public static final ArcadeMove GROUND_POUND_LAND = register(ArcadeMoveIds.GROUND_POUND_LAND, new GroundPoundLandMove(Properties.of(Kind.GROUND).handsBusy()
            .accessParent(() -> ArcadeMoves.GROUND_POUND)
            .params(GroundPoundLandMove.TICKS, GroundPoundLandMove.JUMP_WINDOW, GroundPoundLandMove.ROLL_WINDOW, GroundPoundLandMove.ROLL_SPEED)));
    public static final ArcadeMove GROUND_POUND_JUMP = register(ArcadeMoveIds.GROUND_POUND_JUMP, new GroundPoundJumpMove(Properties.of(Kind.AIR).entryPriority(100)
            .params(ArcMove.arcParams())));
    public static final ArcadeMove LEDGE_GRAB = register(ArcadeMoveIds.LEDGE_GRAB, new LedgeGrabMove(Properties.of(Kind.ATTACHED).entryPriority(50).handsBusy()
            .params(LedgeGrabMove.HANG_DEPTH, LedgeGrabMove.GRAB_BAND, LedgeGrabMove.MIN_HEIGHT, LedgeGrabMove.SHIMMY_SPEED)));
    public static final ArcadeMove LEDGE_CLIMB = register(ArcadeMoveIds.LEDGE_CLIMB, new LedgeClimbMove(Properties.of(Kind.ATTACHED).handsBusy()
            .accessParent(() -> ArcadeMoves.LEDGE_GRAB)
            .params(LedgeClimbMove.TICKS, LedgeClimbMove.FORWARD)));
    public static final ArcadeMove WALL_SLIDE = register(ArcadeMoveIds.WALL_SLIDE, new WallSlideMove(Properties.of(Kind.ATTACHED).entryPriority(35).handsBusy()
            .params(WallSlideMove.MAX_FALL_SPEED, WallSlideMove.MIN_HEIGHT, WallSlideMove.STICK_DOT, WallSlideMove.REACH)));
    public static final ArcadeMove WALL_JUMP = register(ArcadeMoveIds.WALL_JUMP, new WallJumpMove(Properties.of(Kind.AIR).entryPriority(95)
            .params(ArcMove.arcParams(WallJumpMove.PUSH, WallJumpMove.LOCK_TICKS))));

    // tier 2
    public static final ArcadeMove ROLL = register(ArcadeMoveIds.ROLL, new RollMove(Properties.of(Kind.GROUND).entryPriority(30).handsBusy()
            .params(RollMove.SPEED, RollMove.BOOST, RollMove.MAX_BOOSTS, RollMove.BOOST_COOLDOWN, RollMove.DECEL, RollMove.MIN_SPEED, RollMove.TURN_SPEED, RollMove.SLOPE_GAIN, RollMove.CLIMB_HEIGHT)));
    public static final ArcadeMove ROLL_JUMP = register(ArcadeMoveIds.ROLL_JUMP, new RollJumpMove(Properties.of(Kind.AIR).entryPriority(85)
            .params(ArcMove.arcParams())));
    public static final ArcadeMove LONG_JUMP = register(ArcadeMoveIds.LONG_JUMP, new LongJumpMove(Properties.of(Kind.AIR).entryPriority(80)
            .params(ArcMove.arcParams(LongJumpMove.SPEED, LongJumpMove.MIN_SPEED))));
    public static final ArcadeMove BACKFLIP = register(ArcadeMoveIds.BACKFLIP, new BackflipMove(Properties.of(Kind.AIR).entryPriority(75).handsBusy()
            .params(ArcMove.arcParams(BackflipMove.BACK_SPEED, BackflipMove.MAX_SPEED))));
    public static final ArcadeMove SIDE_SOMERSAULT = register(ArcadeMoveIds.SIDE_SOMERSAULT, new SideSomersaultMove(Properties.of(Kind.AIR).entryPriority(90).handsBusy()
            .params(ArcMove.arcParams(SideSomersaultMove.SIDE_SPEED))));
    public static final ArcadeMove DIVE = register(ArcadeMoveIds.DIVE, new DiveMove(Properties.of(Kind.AIR).entryPriority(40).handsBusy()
            .params(DiveMove.SPEED, DiveMove.LIFT, DiveMove.AIR_CONTROL)));
    public static final ArcadeMove ROLLOUT = register(ArcadeMoveIds.ROLLOUT, new RolloutMove(Properties.of(Kind.GROUND).handsBusy()
            .accessParent(() -> ArcadeMoves.DIVE)
            .params(RolloutMove.TICKS, RolloutMove.DECEL)));

    // tier 3
    public static final ArcadeMove SPIN = register(ArcadeMoveIds.SPIN, new SpinMove(Properties.of(Kind.AIR).entryPriority(60)
            .params(SpinMove.FALL_SPEED, SpinMove.TICKS, SpinMove.LIFT_HEIGHT, SpinMove.LIFT_TICKS, SpinMove.JUMP_HEIGHT, SpinMove.JUMP_TICKS, SpinMove.AIR_CONTROL, CANCEL_WINDOW)));
    public static final ArcadeMove VAULT = register(ArcadeMoveIds.VAULT, new VaultMove(Properties.of(Kind.GROUND).entryPriority(20)
            .params(VaultMove.MAX_HEIGHT, VaultMove.MIN_SPEED, VaultMove.TICKS)));
    public static final ArcadeMove SLIDE = register(ArcadeMoveIds.SLIDE, new SlideMove(Properties.of(Kind.GROUND).entryPriority(25)
            .params(SlideMove.MIN_SPEED, SlideMove.MIN_SLOPE, SlideMove.GAIN, SlideMove.FLAT_DECEL, SlideMove.EXIT_SPEED, SlideMove.TURN_SPEED, SlideMove.SLOPE_GAIN)));

    // water
    public static final ArcadeMove SWIM = register(ArcadeMoveIds.SWIM, new SwimMove(Properties.of(Kind.WATER).base()
            .params(SwimMove.ACCEL, SwimMove.DRAG, SwimMove.SPRINT_DRAG, SwimMove.RISE, SwimMove.SINK, SwimMove.STROKE, SwimMove.SURFACE_DEPTH, SwimMove.TURN_SPEED)));
    public static final ArcadeMove SWIM_DASH = register(ArcadeMoveIds.SWIM_DASH, new SwimDashMove(Properties.of(Kind.WATER).entryPriority(42).handsBusy()
            .accessParent(() -> ArcadeMoves.DIVE)
            .params(SwimDashMove.SPEED, SwimDashMove.TICKS, SwimDashMove.DRAG, SwimDashMove.TURN_SPEED)));

    /** The moves that may cut an airborne move short once its cancel window is over. */
    private static final Set<ArcadeMove> AERIAL_FOLLOW_UPS = Set.of(GROUND_POUND, DIVE, SPIN, LEDGE_GRAB, WALL_SLIDE, WALL_JUMP);

    private static List<ArcadeMove> entryOrder;

    private ArcadeMoves() {
    }

    private static ArcadeMove register(ResourceKey<ArcadeMove> key, ArcadeMove move) {
        return Registry.register(ArcadeBuiltInRegistries.ARCADE_MOVE, key, move);
    }

    public static boolean isAerialFollowUp(ArcadeMove move) {
        return AERIAL_FOLLOW_UPS.contains(move);
    }

    /** The moves the generic entry loop offers to start, highest priority first. Ties go by id, so that the order never depends on registration. */
    public static List<ArcadeMove> entryOrder() {
        if (entryOrder == null) {
            entryOrder = ArcadeBuiltInRegistries.ARCADE_MOVE.stream()
                    .filter(ArcadeMove::isEnteredByItself)
                    .sorted(Comparator.comparingInt(ArcadeMove::entryPriority).reversed().thenComparing(move -> move.id().toString()))
                    .toList();
        }
        return entryOrder;
    }
}
