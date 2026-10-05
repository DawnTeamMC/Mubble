package fr.hugman.mubble.arcade.client.compat.controlify;

import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.api.contextual.ContextualStateSink;
import dev.isxander.controlify.api.contextual.InGameContext;
import dev.isxander.controlify.bindings.input.EmptyInput;
import dev.isxander.controlify.contextual.GuideRule;
import dev.isxander.controlify.controller.ControllerEntity;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.arcade.ArcadeController;
import fr.hugman.mubble.arcade.ArcadePreview;
import fr.hugman.mubble.arcade.client.ArcadeClientInput;
import fr.hugman.mubble.arcade.move.ArcadeMove;
import fr.hugman.mubble.arcade.registries.ArcadeBuiltInRegistries;
import fr.hugman.mubble.arcade.sim.ArcadeInputFrame;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

/**
 * The button guide of Controlify while the arcade layer drives.
 * <p>
 * Its rules are data, in {@code assets/controlify/contextual/guide/in_game.json}: one per button and
 * move, translated, shown with the glyph of whatever the button is bound to. They read the facts this
 * class contributes every tick: whether the arcade layout is in use, and which move each button would
 * start, see {@link ArcadePreview}. Meanwhile the vanilla entries follow the layout: those of the
 * actions it moves show on their new button, and those of the buttons it takes, or of the actions the
 * arcade moves stand in for (jump, sneak, sprint), are left out.
 */
@Environment(EnvType.CLIENT)
public final class ArcadeGuide {
    /** The arcade layout is in use. */
    public static final Identifier ARCADE = Mubble.id("arcade");
    /** The orbit camera is on. */
    public static final Identifier ORBITING = Mubble.id("arcade_orbiting");
    /** The buttons the guide previews, by the name their facts use. */
    public static final List<Button> BUTTONS = List.of(
            new Button("jump", ArcadeInputFrame.JUMP),
            new Button("crouch", ArcadeInputFrame.CROUCH),
            new Button("action", ArcadeInputFrame.ACTION),
            new Button("spin", ArcadeInputFrame.SPIN)
    );
    /** The vanilla actions the arcade moves stand in for while the layer drives. */
    private static final Set<Identifier> STOOD_IN_FOR = Set.of(
            Identifier.fromNamespaceAndPath("controlify", "jump"),
            Identifier.fromNamespaceAndPath("controlify", "sneak"),
            Identifier.fromNamespaceAndPath("controlify", "sprint")
    );

    private ArcadeGuide() {
    }

    /** The fact of {@code button} starting {@code move} if pressed now. */
    public static Identifier fact(String button, ArcadeMove move) {
        var id = ArcadeBuiltInRegistries.ARCADE_MOVE.getKey(move);
        return Mubble.id("arcade/" + button + "/" + (id == null ? "unknown" : id.getPath()));
    }

    static void contribute(InGameContext context, ContextualStateSink sink) {
        if (!ArcadeControllerLayout.active()) {
            return;
        }
        var player = context.player();
        var controller = ArcadeController.of(player);
        sink.contributeFact(ARCADE, true);
        sink.contributeFact(ORBITING, controller.orbiting());
        var frame = ArcadeClientInput.current(player);
        boolean actionToMoves = controller.handsGoToMoves(ArcadeClientInput.crouchHeld());
        for (var button : BUTTONS) {
            if (button.action() == ArcadeInputFrame.ACTION && !actionToMoves) {
                // attack and use are for the hands: the vanilla entries tell what they do
                continue;
            }
            var move = ArcadePreview.ifPressed(controller, frame, button.action());
            if (move != null) {
                sink.contributeFact(fact(button.name(), move), true);
            }
        }
    }

    /**
     * The rules the guide shows, laid out for the arcade layout while it is in use: the arcade ones
     * first, as they win over the vanilla ones sharing their button, then the vanilla ones moved to
     * their new button or left out.
     */
    public static List<GuideRule> arrange(List<GuideRule> rules) {
        if (!ArcadeControllerLayout.active()) {
            return rules;
        }
        var controller = ControlifyApi.get().getCurrentController().flatMap(ControllerEntity::input).orElse(null);
        var arranged = new ArrayList<GuideRule>(rules.size());
        for (var rule : rules) {
            if (rule.binding().bindId().getNamespace().equals(Mubble.MOD_ID)) {
                arranged.add(rule);
            }
        }
        for (var rule : rules) {
            var id = rule.binding().bindId();
            if (id.getNamespace().equals(Mubble.MOD_ID) || STOOD_IN_FOR.contains(id)) {
                continue;
            }
            var moved = ArcadeControllerLayout.MOVED.get(id);
            if (moved != null) {
                var binding = ArcadeControlifyEntrypoint.binding(moved);
                if (binding != null) {
                    arranged.add(new GuideRule(binding, rule.location(), rule.predicate(), rule.text()));
                }
                continue;
            }
            if (controller != null) {
                var binding = controller.getBinding(id);
                if (binding != null && EmptyInput.equals(ArcadeControllerLayout.inputFor(controller, id, binding.boundInput()))) {
                    // its button belongs to the arcade layout now
                    continue;
                }
            }
            arranged.add(rule);
        }
        var shown = new HashSet<GuideRule.Key>();
        arranged.removeIf(rule -> !shown.add(new GuideRule.Key(rule.binding().bindId(), rule.location())));
        return arranged;
    }

    /** A button of the guide: the name its facts use, and its {@link ArcadeInputFrame} bit. */
    public record Button(String name, int action) {
    }
}
