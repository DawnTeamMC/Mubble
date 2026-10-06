package fr.hugman.mubble.arcade.client.compat.controlify;

import dev.isxander.controlify.api.contextual.ContextualStateSink;
import dev.isxander.controlify.api.contextual.InGameContext;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.arcade.ArcadeController;
import fr.hugman.mubble.arcade.ArcadePreview;
import fr.hugman.mubble.arcade.client.ArcadeClientInput;
import fr.hugman.mubble.arcade.move.ArcadeMove;
import fr.hugman.mubble.arcade.move.ArcadeMoves;
import fr.hugman.mubble.arcade.registries.ArcadeBuiltInRegistries;
import fr.hugman.mubble.arcade.sim.ArcadeInputFrame;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

/**
 * The facts the button guide of Controlify reads while the arcade layer drives.
 * <p>
 * The rules of the guide are data, in {@code assets/controlify/contextual/guide/in_game.json}: one
 * per arcade binding and move, translated, shown with the glyph of whatever the binding is bound to:
 * a button shows the move it would start, see {@link ArcadePreview}. Controlify's own entries of the
 * buttons the arcade layout takes leave the guide meanwhile, see {@link ArcadeControllerLayout#yields}.
 */
@Environment(EnvType.CLIENT)
public final class ArcadeGuide {
    /** The arcade layer drives. */
    public static final Identifier ARCADE = Mubble.id("arcade");
    /** The orbit camera is on. */
    public static final Identifier ORBITING = Mubble.id("arcade_orbiting");
    /** The player climbs, where crouch holds on. */
    public static final Identifier CLIMBING = Mubble.id("arcade_climbing");
    /** The player has a power-up to trigger. */
    public static final Identifier POWER_UP = Mubble.id("arcade_power_up");
    /** The buttons the guide previews, by the name their facts use. */
    public static final List<Button> BUTTONS = List.of(
            new Button("jump", ArcadeInputFrame.JUMP),
            new Button("crouch", ArcadeInputFrame.CROUCH),
            new Button("action", ArcadeInputFrame.ACTION),
            new Button("spin", ArcadeInputFrame.SPIN)
    );

    private ArcadeGuide() {
    }

    /** The fact of {@code button} starting {@code move} if pressed now. */
    public static Identifier fact(String button, ArcadeMove move) {
        var id = ArcadeBuiltInRegistries.ARCADE_MOVE.getKey(move);
        return Mubble.id("arcade/" + button + "/" + (id == null ? "unknown" : id.getPath()));
    }

    static void contribute(InGameContext context, ContextualStateSink sink) {
        var player = context.player();
        var controller = ArcadeController.of(player);
        if (!controller.isDriving() || context.client().gui.screen() != null) {
            return;
        }
        sink.contributeFact(ARCADE, true);
        sink.contributeFact(ORBITING, controller.orbiting());
        sink.contributeFact(CLIMBING, controller.state().move == ArcadeMoves.CLIMB);
        sink.contributeFact(POWER_UP, player.getPowerUp().isPresent());
        var frame = ArcadeClientInput.current(player);
        boolean actionToMoves = controller.handsGoToMoves(ArcadeClientInput.crouchHeld());
        for (var button : BUTTONS) {
            if (button.action() == ArcadeInputFrame.ACTION && !actionToMoves) {
                // attack and use are for the hands
                continue;
            }
            var move = ArcadePreview.ifPressed(controller, frame, button.action());
            if (move != null) {
                sink.contributeFact(fact(button.name(), move), true);
            }
        }
    }

    /** A button of the guide: the name its facts use, and its {@link ArcadeInputFrame} bit. */
    public record Button(String name, int action) {
    }
}
