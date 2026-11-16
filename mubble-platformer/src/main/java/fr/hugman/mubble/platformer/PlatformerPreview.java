package fr.hugman.mubble.platformer;

import fr.hugman.mubble.platformer.move.PlatformerMove;
import fr.hugman.mubble.platformer.sim.PlatformerInputFrame;
import fr.hugman.mubble.platformer.sim.PlatformerSimulation;
import org.jspecify.annotations.Nullable;

/**
 * What a press would do right now: the move it would start, found by planning the next tick on
 * copies of the state, once without the press and once with it. This is what the button guide of a
 * controller shows, so it never tells of a move the press would not start.
 */
public final class PlatformerPreview {
    private PlatformerPreview() {
    }

    /**
     * The move pressing {@code action} would start on the next tick, or {@code null} when the press
     * would change nothing.
     *
     * @param frame  the input of the player as it stands, with nothing pressed
     * @param action the {@link PlatformerInputFrame} bit of the button
     */
    @Nullable
    public static PlatformerMove ifPressed(PlatformerController controller, PlatformerInputFrame frame, int action) {
        if (!controller.isDriving()) {
            return null;
        }
        var without = plan(controller, frame);
        var pressed = new PlatformerInputFrame(frame.tick(), frame.stickX(), frame.stickZ(), frame.viewYaw(), frame.coupled(),
                (byte) (frame.held() | action), (byte) (frame.pressed() | action), (short) 0, (short) 0);
        var with = plan(controller, pressed);
        return with != without ? with : null;
    }

    private static PlatformerMove plan(PlatformerController controller, PlatformerInputFrame frame) {
        var state = controller.state().copy();
        PlatformerSimulation.plan(state, frame, controller.tuning(), controller.world(), controller.player().position());
        return state.move;
    }
}
