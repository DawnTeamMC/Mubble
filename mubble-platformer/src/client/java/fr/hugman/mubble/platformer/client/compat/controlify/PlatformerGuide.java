package fr.hugman.mubble.platformer.client.compat.controlify;

import dev.isxander.controlify.api.contextual.ContextualStateSink;
import dev.isxander.controlify.api.contextual.InGameContext;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.platformer.PlatformerController;
import fr.hugman.mubble.platformer.PlatformerPreview;
import fr.hugman.mubble.platformer.client.PlatformerClientInput;
import fr.hugman.mubble.platformer.move.PlatformerMove;
import fr.hugman.mubble.platformer.move.PlatformerMoves;
import fr.hugman.mubble.platformer.registries.PlatformerBuiltInRegistries;
import fr.hugman.mubble.platformer.sim.PlatformerInputFrame;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

/**
 * The facts the button guide of Controlify reads while the platformer layer drives.
 * <p>
 * The rules of the guide are data, in {@code assets/controlify/contextual/guide/in_game.json}: one
 * per platformer binding and move, translated, shown with the glyph of whatever the binding is bound to:
 * a button shows the move it would start, see {@link PlatformerPreview}. Controlify's own entries of the
 * buttons the platformer layout takes leave the guide meanwhile, see {@link PlatformerControllerLayout#yields}.
 */
@Environment(EnvType.CLIENT)
public final class PlatformerGuide {
    /** The platformer layer drives. */
    public static final Identifier PLATFORMER = Mubble.id("platformer");
    /** The orbit camera is on. */
    public static final Identifier ORBITING = Mubble.id("platformer_orbiting");
    /** The player climbs, where crouch holds on. */
    public static final Identifier CLIMBING = Mubble.id("platformer_climbing");
    /** The player has a power-up to trigger. */
    public static final Identifier POWER_UP = Mubble.id("platformer_power_up");
    /** The buttons the guide previews, by the name their facts use. */
    public static final List<Button> BUTTONS = List.of(
            new Button("jump", PlatformerInputFrame.JUMP),
            new Button("crouch", PlatformerInputFrame.CROUCH),
            new Button("action", PlatformerInputFrame.ACTION),
            new Button("spin", PlatformerInputFrame.SPIN)
    );

    private PlatformerGuide() {
    }

    /** The fact of {@code button} starting {@code move} if pressed now. */
    public static Identifier fact(String button, PlatformerMove move) {
        var id = PlatformerBuiltInRegistries.PLATFORMER_MOVE.getKey(move);
        return Mubble.id("platformer/" + button + "/" + (id == null ? "unknown" : id.getPath()));
    }

    static void contribute(InGameContext context, ContextualStateSink sink) {
        var player = context.player();
        var controller = PlatformerController.of(player);
        if (!controller.isDriving() || context.client().gui.screen() != null) {
            return;
        }
        sink.contributeFact(PLATFORMER, true);
        sink.contributeFact(ORBITING, controller.orbiting());
        sink.contributeFact(CLIMBING, controller.state().move == PlatformerMoves.CLIMB);
        sink.contributeFact(POWER_UP, player.getPowerUp().isPresent());
        var frame = PlatformerClientInput.current(player);
        boolean actionToMoves = controller.handsGoToMoves(PlatformerClientInput.crouchHeld());
        for (var button : BUTTONS) {
            if (button.action() == PlatformerInputFrame.ACTION && !actionToMoves) {
                // attack and use are for the hands
                continue;
            }
            var move = PlatformerPreview.ifPressed(controller, frame, button.action());
            if (move != null) {
                sink.contributeFact(fact(button.name(), move), true);
            }
        }
    }

    /** A button of the guide: the name its facts use, and its {@link PlatformerInputFrame} bit. */
    public record Button(String name, int action) {
    }
}
