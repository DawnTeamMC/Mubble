package fr.hugman.mubble.client.arcade.compat.controlify;

import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.api.bind.InputBindingSupplier;
import dev.isxander.controlify.api.entrypoint.ControlifyEntrypoint;
import dev.isxander.controlify.api.entrypoint.InitContext;
import dev.isxander.controlify.api.entrypoint.PreInitContext;
import dev.isxander.controlify.bindings.BindContext;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.client.arcade.compat.ArcadeControllerBindings;
import fr.hugman.mubble.world.arcade.sim.ArcadeInputFrame;
import java.util.LinkedHashMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

/**
 * The controller bindings of the arcade actions, registered with Controlify when it is installed.
 * <p>
 * Jump and crouch have no default button: left alone, they follow the vanilla jump and sneak of
 * Controlify, like their keys follow the vanilla keys. Action defaults to the east face button (B on
 * an Xbox layout), which Controlify leaves free in game; recenter has no default, every button being
 * taken already. The defaults live in {@code assets/mubble/controllers/default_bind/default.json}.
 */
@Environment(EnvType.CLIENT)
public final class ArcadeControlifyEntrypoint implements ControlifyEntrypoint, ArcadeControllerBindings {
    private final Map<String, InputBindingSupplier> bindings = new LinkedHashMap<>();
    private final Map<String, Integer> bits = Map.of(
            "arcade_jump", ArcadeInputFrame.JUMP,
            "arcade_crouch", ArcadeInputFrame.CROUCH,
            "arcade_action", ArcadeInputFrame.ACTION,
            "arcade_spin", ArcadeInputFrame.SPIN
    );
    private int previouslyHeld;
    private boolean previousRecenter;

    @Override
    public void onControlifyPreInit(PreInitContext context) {
        var category = Component.translatable("key.category." + Mubble.MOD_ID + ".arcade");
        for (var name : new String[]{"arcade_jump", "arcade_crouch", "arcade_action", "arcade_recenter", "arcade_spin"}) {
            this.bindings.put(name, context.bindings().registerBinding(builder -> builder
                    .id(Mubble.id(name))
                    .name(Component.translatable("key." + Mubble.MOD_ID + "." + name))
                    .category(category)
                    .allowedContexts(BindContext.IN_GAME)));
        }
        ArcadeControllerBindings.Holder.instance = this;
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
