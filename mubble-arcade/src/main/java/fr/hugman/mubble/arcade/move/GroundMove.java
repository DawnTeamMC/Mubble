package fr.hugman.mubble.arcade.move;

import fr.hugman.mubble.arcade.sim.MoveContext;
import org.jspecify.annotations.Nullable;

/**
 * Moving about on the ground: walking, running, and the landing that leads back to either.
 * <p>
 * Running is the move; walking is what the ground is without it, at the vanilla pace. Both share the
 * same locomotion, only the speed they aim for differs.
 */
public class GroundMove extends ArcadeMove {
    /** Food exhaustion per block travelled, when the profile charges for its moves. */
    public static final MoveParam EXHAUSTION_PER_BLOCK = MoveParam.factor("exhaustion_per_block", 0.0D);
    /** Speed gained per block of step-down hugged, see {@link MoveContext#hugGround}. */
    public static final MoveParam SLOPE_GAIN = MoveParam.factor("slope_gain", 0.02D);

    private final Variant variant;

    public GroundMove(Properties properties, Variant variant) {
        super(properties);
        this.variant = variant;
    }

    @Override
    public void tick(MoveContext ctx) {
        boolean canRun = this.variant == Variant.RUN || (this.variant == Variant.LAND && ctx.allowed(ArcadeMoves.RUN));
        ctx.groundMotion(ctx.stickTargetSpeed(canRun));
        ctx.gravity();
        if (this.variant == Variant.RUN) {
            ctx.hugGround(ctx.param(this, SLOPE_GAIN));
        }
    }

    @Override
    @Nullable
    public ArcadeMove exit(MoveContext ctx) {
        if (this.variant == Variant.LAND && ctx.state().moveTicks >= ctx.grace().landingLagTicks()) {
            return ctx.baseGroundMove();
        }
        return null;
    }

    @Override
    public double exhaustionPerBlock(MoveContext ctx) {
        return this.variant == Variant.RUN ? ctx.param(this, EXHAUSTION_PER_BLOCK) : 0.0D;
    }

    public enum Variant {
        WALK,
        RUN,
        LAND
    }
}
