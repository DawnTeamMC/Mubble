package fr.hugman.mubble.world.arcade.move;

import fr.hugman.mubble.world.arcade.sim.MoveContext;

/**
 * A move launching the player on an arc authored as a height and a number of ticks to its apex:
 * every jump, flip and somersault.
 */
public abstract class ArcMove extends ArcadeMove {
    public static final MoveParam HEIGHT = MoveParam.blocks("height", 2.2D);
    public static final MoveParam TICKS_TO_APEX = MoveParam.of("ticks_to_apex", 7.0D, 1.0D, 100.0D, "ticks");
    /** 1 when releasing jump early cuts the arc short, 0 when the arc always goes all the way. */
    public static final MoveParam VARIABLE = MoveParam.of("variable", 1.0D, 0.0D, 1.0D, "boolean");
    /** Scale of the air control during the arc. */
    public static final MoveParam AIR_CONTROL = MoveParam.factor("air_control", 1.0D);

    protected ArcMove(Properties properties) {
        super(properties);
    }

    @Override
    public void enter(MoveContext ctx) {
        ctx.launch(ctx.param(this, HEIGHT), ctx.param(this, TICKS_TO_APEX), ctx.param(this, VARIABLE) >= 0.5D);
        this.launchHorizontally(ctx);
    }

    /** What the arc does to the horizontal velocity when it starts; by default, nothing. */
    protected void launchHorizontally(MoveContext ctx) {
    }

    @Override
    public void tick(MoveContext ctx) {
        ctx.airMotion(ctx.param(this, AIR_CONTROL));
        ctx.gravity();
    }

    /** The parameters every arc has, followed by {@code extra}. */
    protected static MoveParam[] arcParams(MoveParam... extra) {
        var params = new MoveParam[5 + extra.length];
        params[0] = HEIGHT;
        params[1] = TICKS_TO_APEX;
        params[2] = VARIABLE;
        params[3] = AIR_CONTROL;
        params[4] = ArcadeMoves.CANCEL_WINDOW;
        System.arraycopy(extra, 0, params, 5, extra.length);
        return params;
    }
}
