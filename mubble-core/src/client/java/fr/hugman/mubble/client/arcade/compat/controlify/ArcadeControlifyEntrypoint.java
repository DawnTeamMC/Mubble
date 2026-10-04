package fr.hugman.mubble.client.arcade.compat.controlify;

import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.api.bind.InputBindingSupplier;
import dev.isxander.controlify.api.entrypoint.ControlifyEntrypoint;
import dev.isxander.controlify.api.entrypoint.InitContext;
import dev.isxander.controlify.api.entrypoint.PreInitContext;
import dev.isxander.controlify.bindings.BindContext;
import dev.isxander.controlify.bindings.input.AxisInput;
import dev.isxander.controlify.bindings.input.ButtonInput;
import dev.isxander.controlify.bindings.input.EmptyInput;
import dev.isxander.controlify.bindings.input.Input;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.client.arcade.compat.ArcadeControllerBindings;
import fr.hugman.mubble.world.arcade.sim.ArcadeInputFrame;
import java.util.LinkedHashMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

/**
 * The controller bindings of the arcade layer, registered with Controlify when it is installed, laid
 * out as in Super Mario Odyssey: see {@link ArcadeControllerLayout}. They belong to a context of their
 * own, which only applies while the layer drives, so that they neither act nor show as conflicts with
 * Controlify's bindings the rest of the time.
 */
@Environment(EnvType.CLIENT)
public final class ArcadeControlifyEntrypoint implements ControlifyEntrypoint, ArcadeControllerBindings {
    /** The context of the arcade layout: the layer drives and no screen is open. */
    public static final BindContext ARCADE = new BindContext(Mubble.id("arcade"), minecraft -> ArcadeControllerLayout.active());

    private final Map<String, InputBindingSupplier> bindings = new LinkedHashMap<>();
    private final Map<String, Integer> bits = Map.of(
            "arcade_jump", ArcadeInputFrame.JUMP,
            "arcade_crouch", ArcadeInputFrame.CROUCH,
            "arcade_crouch_alt", ArcadeInputFrame.CROUCH,
            "arcade_action", ArcadeInputFrame.ACTION,
            "arcade_action_alt", ArcadeInputFrame.ACTION,
            "arcade_spin", ArcadeInputFrame.SPIN
    );
    private int previouslyHeld;
    private boolean previousRecenter;

    @Override
    public void onControlifyPreInit(PreInitContext context) {
        context.bindings().registerBindContext(ARCADE);
        var category = Component.translatable("key.category." + Mubble.MOD_ID + ".arcade");
        ArcadeControllerLayout.BINDINGS.forEach((name, input) -> this.bindings.put(name, context.bindings().registerBinding(builder -> builder
                .id(Mubble.id(name))
                .name(Component.translatable("key." + Mubble.MOD_ID + "." + name))
                .category(category)
                .defaultInput(defaultInput(input))
                .allowedContexts(ARCADE))));
        ArcadeControllerBindings.Holder.instance = this;
    }

    private static Input defaultInput(@Nullable String input) {
        if (input == null) {
            return EmptyInput.INSTANCE;
        }
        var id = Identifier.fromNamespaceAndPath("controlify", input);
        return input.startsWith("axis/") ? new AxisInput(id) : new ButtonInput(id);
    }

    @Override
    public void onControlifyInit(InitContext context) {
    }

    @Override
    public void onControllersDiscovered(ControlifyApi controlify) {
    }

    private boolean isDown(String name) {
        var controller = ControlifyApi.get().getCurrentController();
        if (controller.isEmpty()) {
            return false;
        }
        var binding = this.bindings.get(name).onOrNull(controller.get());
        return binding != null && binding.digitalNow();
    }

    @Override
    public int held() {
        int held = 0;
        for (var entry : this.bits.entrySet()) {
            if (this.isDown(entry.getKey())) {
                held |= entry.getValue();
            }
        }
        return held;
    }

    @Override
    public int pressed() {
        int held = this.held();
        int pressed = held & ~this.previouslyHeld;
        this.previouslyHeld = held;
        return pressed;
    }

    @Override
    public boolean recenterPressed() {
        boolean down = this.isDown("arcade_recenter");
        boolean pressed = down && !this.previousRecenter;
        this.previousRecenter = down;
        return pressed;
    }

    @Override
    @Nullable
    public Component glyph(String action) {
        var supplier = this.bindings.get(action);
        return supplier == null ? null : supplier.inputGlyph();
    }
}
