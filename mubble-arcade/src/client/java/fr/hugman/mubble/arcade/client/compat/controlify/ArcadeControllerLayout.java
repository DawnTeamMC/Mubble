package fr.hugman.mubble.arcade.client.compat.controlify;

import dev.isxander.controlify.bindings.input.EmptyInput;
import dev.isxander.controlify.bindings.input.Input;
import dev.isxander.controlify.controller.input.InputComponent;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.arcade.ArcadeController;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

/**
 * The controller layout of the arcade layer: Super Mario Odyssey's, for as long as the layer drives.
 * <p>
 * SMO puts jump on A and B, Cappy on Y and X, crouch and ground pound on ZL and ZR, and recentering
 * the camera on L. Here, Cappy's buttons are the hands: attack on Y and use on X, which roll when
 * crouch is held and dive out of a ground pound, as Cappy's do. Controlify gives those buttons to
 * other vanilla actions; while the layer drives, these move to buttons of their own: the inventory
 * and the hotbar to the D-pad, the radial menu to R. Any other vanilla binding sitting on a button of
 * the layout gives way, and the actions left without a button stay in the radial menu. Every binding
 * of the layout can be rebound in Controlify's menu, under the arcade movement category. Once the
 * layer stops, everything is Controlify's again.
 * <p>
 * Buttons are named after their place, as Controlify names them: {@code east} is A on a Switch
 * controller, B on an Xbox one.
 */
@Environment(EnvType.CLIENT)
public final class ArcadeControllerLayout {
    /** The vanilla actions the arcade layout moves, and the binding of the arcade layout each moves to. */
    static final Map<Identifier, String> MOVED = new LinkedHashMap<>();
    /** Every binding of the arcade layout, by name, with its default button or axis, or {@code null} for none. */
    static final Map<String, String> BINDINGS = new LinkedHashMap<>();

    static {
        // the moves, as in SMO; vanilla jump stays on south, so that both A and B jump
        BINDINGS.put("arcade_jump", "button/east");
        BINDINGS.put("arcade_crouch", "axis/left_trigger");
        BINDINGS.put("arcade_crouch_alt", "axis/right_trigger");
        BINDINGS.put("arcade_recenter", "button/left_shoulder");
        // turning the stick all the way around spins, as in SMO
        BINDINGS.put("arcade_spin", null);
        // the hands, on Cappy's buttons
        moved("attack", "arcade_attack", "button/west");
        moved("use", "arcade_use", "button/north");
        // where vanilla goes meanwhile
        moved("inventory", "arcade_inventory", "button/dpad_up");
        moved("drop", "arcade_drop", "button/dpad_down");
        moved("prev_slot", "arcade_prev_slot", "button/dpad_left");
        moved("next_slot", "arcade_next_slot", "button/dpad_right");
        moved("radial_menu", "arcade_radial_menu", "button/right_shoulder");
        moved("swap_hands", "arcade_swap_hands", null);
        moved("pick_block", "arcade_pick_block", null);
        moved("open_chat", "arcade_open_chat", null);
    }

    private ArcadeControllerLayout() {
    }

    private static void moved(String vanilla, String arcade, String input) {
        MOVED.put(Identifier.fromNamespaceAndPath("controlify", vanilla), arcade);
        BINDINGS.put(arcade, input);
    }

    /** The names of the bindings of the layout. */
    static List<String> names() {
        return List.copyOf(BINDINGS.keySet());
    }

    /** Whether the arcade layout is the one in use: the arcade layer drives, and no screen is open. */
    public static boolean active() {
        var minecraft = Minecraft.getInstance();
        var player = minecraft.player;
        return player != null && minecraft.gui.screen() == null && ArcadeController.of(player).isDriving();
    }

    /**
     * What the binding {@code id} of a controller reads this tick, given the input it is bound to:
     * that input when the arcade layout is not in use or the binding is part of it, the input of the
     * binding of the layout it moved to, or nothing when it sits on a button of the layout.
     */
    public static Input inputFor(InputComponent component, Identifier id, Input bound) {
        if (!active() || id.getNamespace().equals(Mubble.MOD_ID)) {
            return bound;
        }
        var moved = MOVED.get(id);
        if (moved != null) {
            var binding = component.getBinding(Mubble.id(moved));
            return binding == null ? bound : binding.boundInput();
        }
        if (EmptyInput.equals(bound)) {
            return bound;
        }
        for (var name : BINDINGS.keySet()) {
            var binding = component.getBinding(Mubble.id(name));
            if (binding != null && bound.equals(binding.boundInput())) {
                return EmptyInput.INSTANCE;
            }
        }
        return bound;
    }
}
