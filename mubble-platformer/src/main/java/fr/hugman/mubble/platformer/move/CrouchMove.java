package fr.hugman.mubble.platformer.move;

import fr.hugman.mubble.platformer.sim.MoveContext;
import net.minecraft.world.entity.Pose;
import org.jspecify.annotations.Nullable;

/**
 * The vanilla crouch: slow, low, careful at edges. Crouching at speed slides to a stop first, which
 * is when a jump turns into a long jump.
 */
public class CrouchMove extends PlatformerMove {
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
    public PlatformerMove exit(MoveContext ctx) {
        return ctx.crouchHeld() ? null : ctx.baseGroundMove();
    }

    @Override
    public Pose pose(MoveContext ctx) {
        return Pose.CROUCHING;
    }
}
