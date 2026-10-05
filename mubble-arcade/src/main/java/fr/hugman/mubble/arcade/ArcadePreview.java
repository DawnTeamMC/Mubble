package fr.hugman.mubble.arcade;

import fr.hugman.mubble.arcade.move.ArcadeMove;
import fr.hugman.mubble.arcade.sim.ArcadeInputFrame;
import fr.hugman.mubble.arcade.sim.ArcadeSimulation;
import org.jspecify.annotations.Nullable;

/**
 * What a press would do right now: the move it would start, found by planning the next tick on
 * copies of the state, once without the press and once with it. This is what the button guide of a
 * controller shows, so it never tells of a move the press would not start.
 */
public final class ArcadePreview {
    private ArcadePreview() {
    }

    /**
     * The move pressing {@code action} would start on the next tick, or {@code null} when the press
     * would change nothing.
     *
     * @param frame  the input of the player as it stands, with nothing pressed
     * @param action the {@link ArcadeInputFrame} bit of the button
     */
    @Nullable
    public static ArcadeMove ifPressed(ArcadeController controller, ArcadeInputFrame frame, int action) {
        if (!controller.isDriving()) {
            return null;
        }
        var without = plan(controller, frame);
        var pressed = new ArcadeInputFrame(frame.tick(), frame.stickX(), frame.stickZ(), frame.viewYaw(), frame.coupled(),
                (byte) (frame.held() | action), (byte) (frame.pressed() | action), (short) 0, (short) 0);
        var with = plan(controller, pressed);
        return with != without ? with : null;
    }

    private static ArcadeMove plan(ArcadeController controller, ArcadeInputFrame frame) {
        var state = controller.state().copy();
        ArcadeSimulation.plan(state, frame, controller.tuning(), controller.world(), controller.player().position());
        return state.move;
    }
}
