package fr.hugman.mubble.arcade.move;

import fr.hugman.mubble.arcade.sim.MoveContext;
import net.minecraft.world.entity.Pose;
import org.jspecify.annotations.Nullable;

/**
 * The vanilla crouch: slow, low, careful at edges. Crouching at speed slides to a stop first, which
 * is when a jump turns into a long jump.
 */
public class CrouchMove extends ArcadeMove {
    /** Ticks a crouch started at the run speed takes to slide to a stop. */
    public static final MoveParam SLIDE_TICKS = MoveParam.ticks("slide_ticks", 12.0D);

    public CrouchMove(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEnter(MoveContext ctx) {
        return ctx.state().grounded && ctx.crouchHeld();
    }

    @Override
    public void tick(MoveContext ctx) {
        double crawl = ctx.tuning().walkSpeed() * ctx.tuning().sneakingSpeed() * ctx.stickMagnitude();
        double speed = ctx.horizontalSpeed();
        if (speed > crawl) {
            double brake = ctx.tuning().effectiveRunSpeed() / Math.max(1.0D, ctx.param(this, SLIDE_TICKS)) * ctx.grip();
            ctx.setHorizontal(ctx.velocityYaw(), Math.max(crawl, speed - brake));
        } else {
            ctx.groundMotion(crawl);
        }
        ctx.gravity();
    }

    @Override
    @Nullable
    public ArcadeMove exit(MoveContext ctx) {
        return ctx.crouchHeld() ? null : (ctx.allowed(ArcadeMoves.RUN) ? ArcadeMoves.RUN : ArcadeMoves.WALK);
    }

    @Override
    public Pose pose(MoveContext ctx) {
        return Pose.CROUCHING;
    }

    /** Whether the crouch is slow enough for vanilla's edge guard to apply, which keeps a crawling player from falling off. */
    public static boolean guardsEdges(MoveContext ctx) {
        return ctx.horizontalSpeed() <= ctx.tuning().walkSpeed() * ctx.tuning().sneakingSpeed() + 1.0E-3D;
    }
}
