package fr.hugman.mubble.arcade.client.compat.controlify;

import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.api.bind.ControlifyBindApi;
import dev.isxander.controlify.api.bind.InputBindingSupplier;
import dev.isxander.controlify.api.entrypoint.ControlifyEntrypoint;
import dev.isxander.controlify.api.entrypoint.InitContext;
import dev.isxander.controlify.api.entrypoint.PreInitContext;
import dev.isxander.controlify.bindings.BindContext;
import dev.isxander.controlify.rumble.BasicRumbleEffect;
import dev.isxander.controlify.rumble.RumbleSource;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.arcade.client.ArcadeKeyMappings;
import fr.hugman.mubble.arcade.client.compat.ArcadeControllerBindings;
import fr.hugman.mubble.arcade.sim.ArcadeInputFrame;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

/**
 * The controller side of the arcade layer, when Controlify is installed, through its API only.
 * <p>
 * The layer plays on the vanilla actions, which Controlify already binds: jump, sneak (crouch and
 * ground pound), sprint, attack and use. Its own keys get a binding each, which presses the key, so
 * that Controlify does not add one of its own for them; the second jump button of Super Mario Odyssey
 * gets a binding too, as vanilla has a single jump key. Where each binding goes by default is data:
 * the layout of Super Mario Odyssey, in {@code assets/controlify/controllers/default_bind/default.json},
 * which Controlify layers over its own defaults. The button guide is data too, along with the facts
 * {@link ArcadeGuide} contributes, and cues rumble the controller.
 */
@Environment(EnvType.CLIENT)
public final class ArcadeControlifyEntrypoint implements ControlifyEntrypoint, ArcadeControllerBindings {
    @Nullable
    private InputBindingSupplier secondJump;

    @Override
    public void onControlifyPreInit(PreInitContext context) {
        var bindings = context.bindings();
        this.secondJump = bindings.registerBinding(builder -> builder
                .id(Mubble.id("arcade_jump"))
                .name(Component.translatable("key." + Mubble.MOD_ID + ".arcade_jump"))
                .category(ArcadeKeyMappings.CATEGORY.label())
                .allowedContexts(BindContext.IN_GAME));
        pressing(bindings, ArcadeKeyMappings.SPIN);
        pressing(bindings, ArcadeKeyMappings.RECENTER);
        pressing(bindings, ArcadeKeyMappings.DEBUG_HUD);
        context.contextualDomains().inGame().registerContributor(ArcadeGuide::contribute);
        ArcadeControllerBindings.Holder.instance = this;
    }

    /** A binding pressing {@code key}, named after it: {@code key.mubble.arcade_spin} binds as {@code mubble:arcade_spin}. */
    private static void pressing(ControlifyBindApi bindings, KeyMapping key) {
        var name = key.getName();
        bindings.registerBinding(builder -> builder
                .id(Mubble.id(name.substring(name.lastIndexOf('.') + 1)))
                .name(Component.translatable(name))
                .category(key.getCategory().label())
                .allowedContexts(BindContext.IN_GAME)
                .keyEmulation(key));
    }

    @Override
    public void onControlifyInit(InitContext context) {
    }

    @Override
    public void onControllersDiscovered(ControlifyApi controlify) {
    }

    @Override
    public int held() {
        var controller = ControlifyApi.get().getCurrentController();
        if (this.secondJump == null || controller.isEmpty()) {
            return 0;
        }
        var binding = this.secondJump.onOrNull(controller.get());
        return binding != null && binding.digitalNow() ? ArcadeInputFrame.JUMP : 0;
    }

    @Override
    public void rumble(float strong, float weak, int ticks) {
        var controlify = ControlifyApi.get();
        if (!controlify.currentInputMode().isKeyboardMouse()) {
            controlify.playRumbleEffect(RumbleSource.PLAYER, BasicRumbleEffect.constant(strong, weak, ticks));
        }
    }
}
