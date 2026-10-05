package fr.hugman.mubble.arcade.client.compat.controlify;

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
import dev.isxander.controlify.rumble.BasicRumbleEffect;
import dev.isxander.controlify.rumble.RumbleSource;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.arcade.client.compat.ArcadeControllerBindings;
import fr.hugman.mubble.arcade.sim.ArcadeInputFrame;
import java.util.LinkedHashMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

/**
 * The controller side of the arcade layer, when Controlify is installed: bindings laid out as in Super
 * Mario Odyssey (see {@link ArcadeControllerLayout}), a button guide that follows them (see
 * {@link ArcadeGuide}), and rumbles. The bindings belong to a context of their own, which only applies
 * while the layer drives, so that they neither act nor show as conflicts with Controlify's bindings
 * the rest of the time; all of them can be rebound in Controlify's controls menu.
 */
@Environment(EnvType.CLIENT)
public final class ArcadeControlifyEntrypoint implements ControlifyEntrypoint, ArcadeControllerBindings {
    /** The context of the arcade layout: the layer drives and no screen is open. */
    public static final BindContext ARCADE = new BindContext(Mubble.id("arcade"), minecraft -> ArcadeControllerLayout.active());

    private static final Map<String, InputBindingSupplier> BINDINGS = new LinkedHashMap<>();
    private final Map<String, Integer> bits = Map.of(
            "arcade_jump", ArcadeInputFrame.JUMP,
            "arcade_crouch", ArcadeInputFrame.CROUCH,
            "arcade_crouch_alt", ArcadeInputFrame.CROUCH,
            "arcade_spin", ArcadeInputFrame.SPIN
    );
    private int previouslyHeld;
    private boolean previousRecenter;

    @Override
    public void onControlifyPreInit(PreInitContext context) {
        context.bindings().registerBindContext(ARCADE);
        var category = Component.translatable("key.category." + Mubble.MOD_ID + ".arcade");
        ArcadeControllerLayout.BINDINGS.forEach((name, input) -> BINDINGS.put(name, context.bindings().registerBinding(builder -> builder
                .id(Mubble.id(name))
                .name(Component.translatable("key." + Mubble.MOD_ID + "." + name))
                .category(category)
                .defaultInput(defaultInput(input))
                .allowedContexts(ARCADE))));
        context.contextualDomains().inGame().registerContributor(ArcadeGuide::contribute);
        ArcadeControllerBindings.Holder.instance = this;
    }

    /** The binding of the arcade layout named {@code name}, if Controlify registered it. */
    @Nullable
    static InputBindingSupplier binding(String name) {
        return BINDINGS.get(name);
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
        var binding = BINDINGS.get(name).onOrNull(controller.get());
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
    public void rumble(float strong, float weak, int ticks) {
        var controlify = ControlifyApi.get();
        if (!controlify.currentInputMode().isKeyboardMouse()) {
            controlify.playRumbleEffect(RumbleSource.PLAYER, BasicRumbleEffect.constant(strong, weak, ticks));
        }
    }
}
