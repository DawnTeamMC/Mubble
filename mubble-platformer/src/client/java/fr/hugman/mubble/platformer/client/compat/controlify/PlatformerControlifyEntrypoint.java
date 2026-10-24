package fr.hugman.mubble.platformer.client.compat.controlify;

import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.api.bind.ControlifyBindApi;
import dev.isxander.controlify.api.bind.InputBindingSupplier;
import dev.isxander.controlify.api.entrypoint.ControlifyEntrypoint;
import dev.isxander.controlify.api.entrypoint.InitContext;
import dev.isxander.controlify.api.entrypoint.PreInitContext;
import dev.isxander.controlify.bindings.BindContext;
import dev.isxander.controlify.bindings.input.AxisInput;
import dev.isxander.controlify.bindings.input.ButtonInput;
import dev.isxander.controlify.bindings.input.Input;
import dev.isxander.controlify.rumble.BasicRumbleEffect;
import dev.isxander.controlify.rumble.RumbleSource;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.platformer.client.PlatformerClientConfig;
import fr.hugman.mubble.platformer.client.PlatformerKeyMappings;
import fr.hugman.mubble.platformer.client.compat.PlatformerControllerBindings;
import fr.hugman.mubble.client.keybind.PowerUpKeybindsHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec2;
import org.jspecify.annotations.Nullable;

/**
 * The controller side of the platformer layer, when Controlify is installed.
 * <p>
 * Every platformer action has a binding of its own, in the <em>Platformer Movement</em> category of
 * Controlify's controls, laid out by default as in Super Mario Odyssey (see
 * {@link PlatformerControllerLayout}): Controlify's own bindings and defaults stay as they are. The
 * buttons press the platformer keys, so that the layer reads one set of keys whatever plays it; the stick
 * is read analog. The bindings belong to a context of their own, which only applies while the layer
 * drives, outside of any screen. Meanwhile, a binding of Controlify's on a button the platformer layout
 * uses gives way, see {@link PlatformerControllerLayout#yields}. The button guide follows (see
 * {@link PlatformerGuide}), and cues rumble the controller.
 */
@Environment(EnvType.CLIENT)
public final class PlatformerControlifyEntrypoint implements ControlifyEntrypoint, PlatformerControllerBindings {
    @Nullable
    private InputBindingSupplier forward, backward, left, right, powerUp, inventory, autoCamera;

    @Override
    public void onControlifyPreInit(PreInitContext context) {
        var bindings = context.bindings();
        bindings.registerBindContext(PlatformerControllerLayout.CONTEXT);
        // the stick, read analog
        this.forward = stick(bindings, PlatformerKeyMappings.FORWARD, "left_stick_up");
        this.backward = stick(bindings, PlatformerKeyMappings.BACKWARD, "left_stick_down");
        this.left = stick(bindings, PlatformerKeyMappings.LEFT, "left_stick_left");
        this.right = stick(bindings, PlatformerKeyMappings.RIGHT, "left_stick_right");
        // the buttons, pressing the platformer keys
        pressing(bindings, PlatformerKeyMappings.JUMP, button("south"));
        pressing(bindings, PlatformerKeyMappings.JUMP_ALT, button("east"));
        pressing(bindings, PlatformerKeyMappings.CROUCH, axis("right_trigger"));
        pressing(bindings, PlatformerKeyMappings.SPRINT, button("left_stick"));
        pressing(bindings, PlatformerKeyMappings.ATTACK, button("west"));
        pressing(bindings, PlatformerKeyMappings.USE, button("north"));
        pressing(bindings, PlatformerKeyMappings.SPIN, null);
        pressing(bindings, PlatformerKeyMappings.RECENTER, button("right_stick"));
        pressing(bindings, PlatformerKeyMappings.DEBUG_HUD, null);
        // what the layout moves aside comes back on buttons of its own
        this.powerUp = layout(bindings, "platformer_power_up", axis("left_trigger"));
        this.inventory = layout(bindings, "platformer_inventory", button("dpad_up"));
        // the camera following the player round, the one setting of the layer about controllers, at hand
        // in Controlify's controls and radial menu, which gives it an icon
        this.autoCamera = bindings.registerBinding(builder -> builder
                .id(Mubble.id("platformer_auto_camera"))
                .name(Component.translatable("key." + Mubble.MOD_ID + ".platformer_auto_camera"))
                .description(Component.translatable("key." + Mubble.MOD_ID + ".platformer_auto_camera.description"))
                .category(PlatformerKeyMappings.CATEGORY.label())
                .allowedContexts(BindContext.IN_GAME));
        context.contextualDomains().inGame().registerContributor(PlatformerGuide::contribute);
        PlatformerControllerBindings.Holder.instance = this;
    }

    private static InputBindingSupplier stick(ControlifyBindApi bindings, KeyMapping key, String axis) {
        return register(bindings, key.getName(), axis(axis), builder -> builder.addKeyCorrelation(key));
    }

    private static void pressing(ControlifyBindApi bindings, KeyMapping key, @Nullable Input input) {
        register(bindings, key.getName(), input, builder -> builder.keyEmulation(key));
    }

    private static InputBindingSupplier layout(ControlifyBindApi bindings, String name, Input input) {
        return register(bindings, "key." + Mubble.MOD_ID + "." + name, input, builder -> builder);
    }

    /** A binding of the platformer layout, named after the translation key {@code key.mubble.<name>}, as {@code mubble:<name>}. */
    private static InputBindingSupplier register(ControlifyBindApi bindings, String translation, @Nullable Input input, ControlifyBindApi.RegistryCallback more) {
        var id = Mubble.id(translation.substring(translation.lastIndexOf('.') + 1));
        PlatformerControllerLayout.BINDINGS.add(id);
        return bindings.registerBinding(builder -> {
            builder.id(id)
                    .name(Component.translatable(translation))
                    .category(PlatformerKeyMappings.CATEGORY.label())
                    .allowedContexts(PlatformerControllerLayout.CONTEXT);
            if (input != null) {
                builder.defaultInput(input);
            }
            return more.apply(builder);
        });
    }

    private static Input button(String name) {
        return new ButtonInput(Identifier.fromNamespaceAndPath("controlify", "button/" + name));
    }

    private static Input axis(String name) {
        return new AxisInput(Identifier.fromNamespaceAndPath("controlify", "axis/" + name));
    }

    @Override
    public void onControlifyInit(InitContext context) {
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }

    @Override
    public void onControllersDiscovered(ControlifyApi controlify) {
    }

    /** The bindings the layout brings back what it moves aside with: they act themselves, the context keeping them to the layout. */
    private void tick(Minecraft minecraft) {
        var player = minecraft.player;
        if (player == null || minecraft.gui.screen() != null) {
            return;
        }
        if (this.justPressed(this.autoCamera)) {
            boolean on = !PlatformerClientConfig.get().autoCameraController();
            PlatformerClientConfig.set(PlatformerClientConfig.get().withAutoCameraController(on));
            player.sendOverlayMessage(Component.translatable("platformer." + Mubble.MOD_ID + ".settings.auto_camera_controller." + (on ? "on" : "off")));
        }
        if (this.justPressed(this.powerUp)) {
            PowerUpKeybindsHandler.trigger(player);
        }
        if (this.justPressed(this.inventory)) {
            // as the inventory key does
            if (minecraft.gameMode != null && minecraft.gameMode.isServerControlledInventory()) {
                player.sendOpenInventory();
            } else {
                minecraft.getTutorial().onOpenInventory();
                minecraft.gui.setScreen(new InventoryScreen(player));
            }
        }
    }

    private boolean justPressed(@Nullable InputBindingSupplier supplier) {
        var controller = ControlifyApi.get().getCurrentController();
        if (supplier == null || controller.isEmpty()) {
            return false;
        }
        var binding = supplier.onOrNull(controller.get());
        return binding != null && binding.justPressed();
    }

    private float analogue(@Nullable InputBindingSupplier supplier) {
        var controller = ControlifyApi.get().getCurrentController();
        if (supplier == null || controller.isEmpty()) {
            return 0.0F;
        }
        var binding = supplier.onOrNull(controller.get());
        return binding == null ? 0.0F : binding.analogueNow();
    }

    @Override
    public Vec2 stick() {
        return new Vec2(this.analogue(this.left) - this.analogue(this.right), this.analogue(this.forward) - this.analogue(this.backward));
    }

    @Override
    public boolean usingController() {
        return ControlifyApi.get().currentInputMode().isController();
    }

    @Override
    public void rumble(float strong, float weak, int ticks) {
        if (this.usingController()) {
            ControlifyApi.get().playRumbleEffect(RumbleSource.PLAYER, BasicRumbleEffect.constant(strong, weak, ticks));
        }
    }
}
