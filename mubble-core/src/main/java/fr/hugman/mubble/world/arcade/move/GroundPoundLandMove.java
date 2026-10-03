package fr.hugman.mubble.world.arcade.move;

import fr.hugman.mubble.world.arcade.sim.MoveContext;
import org.jspecify.annotations.Nullable;

/**
 * The impact of a ground pound, which a jump turns into a ground pound jump.
 */
public class GroundPoundLandMove extends ArcadeMove {
    /** How long the impact lasts. */
    public static final MoveParam TICKS = MoveParam.ticks("ticks", 8.0D);
    /** Ticks after the impact during which a jump is a ground pound jump. */
    public static final MoveParam JUMP_WINDOW = MoveParam.ticks("jump_window_ticks", 5.0D);

    public GroundPoundLandMove(Properties properties) {
        super(properties);
    }

    @Override
    public void tick(MoveContext ctx) {
        ctx.setHorizontal(ctx.state().facing, 0.0D);
        ctx.gravity();
    }

    @Override
    @Nullable
    public ArcadeMove exit(MoveContext ctx) {
        return ctx.state().moveTicks >= ctx.settings(this).ticks(TICKS) ? ctx.baseGroundMove() : null;
    }

    @Override
    public boolean allowsInterruption(ArcadeMove next, MoveContext ctx) {
        return next == ArcadeMoves.GROUND_POUND_JUMP;
    }
}
