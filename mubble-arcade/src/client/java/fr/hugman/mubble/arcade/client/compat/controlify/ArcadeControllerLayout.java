package fr.hugman.mubble.arcade.client.compat.controlify;

import dev.isxander.controlify.bindings.BindContext;
import dev.isxander.controlify.bindings.input.EmptyInput;
import dev.isxander.controlify.bindings.input.Input;
import dev.isxander.controlify.controller.input.InputComponent;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.arcade.ArcadeController;
import java.util.LinkedHashSet;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

/**
 * The controller layout of the arcade layer: its own bindings, laid out by default as in Super Mario
 * Odyssey, in a context of their own that only applies while the layer drives and no screen is open.
 * <p>
 * By default: the stick moves, A and B jump, Y and X attack and use (Cappy's buttons: a roll when
 * crouching, a dive out of a ground pound), ZR crouches and ground pounds, ZL triggers the power-up,
 * pressing the left stick sprints, pressing the right stick recenters the camera, and the D-pad up
 * opens the inventory, which X takes. Buttons are named after their place, as Controlify names them:
 * {@code east} is A on a Switch controller, B on an Xbox one.
 * <p>
 * Controlify's own bindings keep their buttons and their defaults. While the layout is in use, the
 * ones on a button an arcade binding uses give way, see {@link #yields}: Controlify has no API to let a
 * mod's bindings take a button over for a while, so this is the one place reaching into it.
 */
@Environment(EnvType.CLIENT)
public final class ArcadeControllerLayout {
    /** The context of the arcade bindings: the layer drives, and no screen is open. */
    public static final BindContext CONTEXT = new BindContext(Mubble.id("arcade"), minecraft -> active());
    /** The bindings of the arcade layout. */
    static final Set<Identifier> BINDINGS = new LinkedHashSet<>();

    private ArcadeControllerLayout() {
    }

    /** Whether the arcade layout is the one in use: the arcade layer drives, and no screen is open. */
    public static boolean active() {
        var minecraft = Minecraft.getInstance();
        var player = minecraft.player;
        return player != null && minecraft.gui.screen() == null && ArcadeController.of(player).isDriving();
    }

    /**
     * Whether the binding {@code id} of a controller gives way to the arcade layout right now: the
     * layout is in use, the binding is not one of its own, and an arcade binding uses its input. It
     * then reads as unbound, which keeps it from acting, from pressing its key and from showing in the
     * button guide. Any screen, the controls menu and the radial menu among them, sees it as it is.
     */
    public static boolean yields(InputComponent component, Identifier id, Input bound) {
        if (BINDINGS.contains(id) || EmptyInput.equals(bound) || !active()) {
            return false;
        }
        for (var arcade : BINDINGS) {
            var binding = component.getBinding(arcade);
            if (binding != null && bound.equals(binding.boundInput())) {
                return true;
            }
        }
        return false;
    }
}
