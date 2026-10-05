package fr.hugman.mubble.arcade.move;

import fr.hugman.mubble.arcade.sim.ArcadeTuning;
import fr.hugman.mubble.arcade.sim.MoveContext;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.Nullable;

/**
 * Being on something to climb (a ladder, vines, scaffolding...), on the numbers of vanilla climbing:
 * the stick moves the player at walking pace, never faster than vanilla lets a climber go sideways;
 * pushing into a wall, or holding jump, climbs; otherwise the player slides down, no faster than
 * vanilla, and holding crouch holds on. The player lets go by walking off, or at the top.
 */
public class ClimbMove extends ArcadeMove {
    /** Upward speed climbing gives, as vanilla's. */
    public static final MoveParam CLIMB_SPEED = MoveParam.speed("climb_speed", 0.2D);
    /** The fastest the player goes sideways and slides down, on each axis, as vanilla's. */
    public static final MoveParam MAX_SPEED = MoveParam.speed("speed_cap", 0.15D);
    /** Speed the stick adds or takes away every tick, sideways. */
    public static final MoveParam ACCEL = MoveParam.speed("side_accel", 0.05D);
    /** How fast the body turns towards the stick, in degrees per tick. */
    public static final MoveParam TURN_SPEED = MoveParam.degrees("turn_speed", 20.0D);

    /** Share of the vertical speed kept every tick, as vanilla's. */
    private static final double VERTICAL_DRAG = 0.98D;

    public ClimbMove(Properties properties) {
        super(properties);
    }

    @Override
    public void enter(MoveContext ctx) {
        var state = ctx.state();
        state.chainIndex = 0;
        state.chainWindow = 0;
        state.arcGravity = 0.0D;
        state.jumpCut = true;
        state.divedThisAir = false;
        state.spunThisAir = false;
    }

    @Override
    public void tick(MoveContext ctx) {
        var state = ctx.state();
        if (ctx.jumpBuffered()) {
            // jump climbs here, it does not wait to jump once off
            ctx.consumeJump();
        }

        double max = ctx.param(this, MAX_SPEED);
        double accel = ctx.param(this, ACCEL);
        double walk = ctx.tuning().walkSpeed();
        state.vx = Mth.clamp(state.vx + Mth.clamp(ctx.stickX() * walk - state.vx, -accel, accel), -max, max);
        state.vz = Mth.clamp(state.vz + Mth.clamp(ctx.stickZ() * walk - state.vz, -accel, accel), -max, max);
        if (ctx.hasStick()) {
            ctx.turnFacing(ctx.stickYaw(), ctx.param(this, TURN_SPEED));
        }

        // as vanilla: the player moves with the speed of the tick, no faster down than a climber
        // slides, not at all while holding on; climbing sets the speed after the move, gravity follows
        double vy = Math.max(state.vy, -max);
        var climbable = ctx.climbable();
        if (vy < 0.0D && ctx.crouchHeld() && (climbable == null || !climbable.is(Blocks.SCAFFOLDING))) {
            vy = 0.0D;
        }
        ctx.setDisplacement(state.vx, vy, state.vz);
        if (ctx.jumpHeld() || ctx.stickPushesIntoWall()) {
            vy = ctx.param(this, CLIMB_SPEED);
        }
        state.vy = (vy - ArcadeTuning.VANILLA_GRAVITY * ctx.tuning().gravityScale()) * VERTICAL_DRAG;
    }

    /** Off what there is to climb, the player stands or falls. */
    @Override
    @Nullable
    public ArcadeMove exit(MoveContext ctx) {
        if (ctx.onClimbable()) {
            return null;
        }
        return ctx.state().grounded ? ctx.baseGroundMove() : ArcadeMoves.FALL;
    }

    /** Reaching the ground is not a landing: the player is still on what they climb. */
    @Override
    @Nullable
    public ArcadeMove onLand(MoveContext ctx) {
        return null;
    }

    /** Climbers take no fall damage, as in vanilla. */
    @Override
    public boolean negatesFallDamage(MoveContext ctx) {
        return true;
    }
}
