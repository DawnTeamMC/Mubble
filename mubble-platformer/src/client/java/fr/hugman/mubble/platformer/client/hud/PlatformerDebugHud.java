package fr.hugman.mubble.platformer.client.hud;

import fr.hugman.mubble.platformer.client.PlatformerClientConfig;
import fr.hugman.mubble.platformer.client.PlatformerClientInput;
import fr.hugman.mubble.platformer.client.PlatformerKeyMappings;
import fr.hugman.mubble.platformer.client.PlatformerRecorder;
import fr.hugman.mubble.platformer.client.animation.PlatformerPlayerAnimator;
import fr.hugman.mubble.platformer.client.camera.PlatformerCamera;
import fr.hugman.mubble.platformer.PlatformerController;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

/**
 * The debug HUD of the platformer layer: the state of the simulation, its timers, and what the sources
 * of the player resolve to. Toggled with its key, or on from the start through the client config.
 */
@Environment(EnvType.CLIENT)
public final class PlatformerDebugHud {
    private static final int TEXT = 0xFFFFFFFF;
    private static final int BACKGROUND = 0x90000000;
    private static boolean shown;
    private static boolean initialized;

    private PlatformerDebugHud() {
    }

    public static void tick(Minecraft minecraft) {
        if (!initialized) {
            shown = PlatformerClientConfig.get().debugHud();
            initialized = true;
        }
        while (PlatformerKeyMappings.DEBUG_HUD.consumeClick()) {
            shown = !shown;
        }
    }

    public static void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        var minecraft = Minecraft.getInstance();
        var player = minecraft.player;
        if (!shown || player == null) {
            return;
        }
        var lines = lines(PlatformerController.of(player));
        var font = minecraft.font;
        int y = 2;
        for (var line : lines) {
            int width = font.width(line);
            graphics.fill(1, y - 1, 3 + width, y + font.lineHeight, BACKGROUND);
            graphics.text(font, line, 2, y, TEXT, false);
            y += font.lineHeight + 1;
        }
    }

    public static List<Component> lines(PlatformerController controller) {
        var lines = new ArrayList<Component>();
        var access = controller.access();
        var profile = controller.isActive() ? access.profile().map(key -> key.identifier().toString()).orElse("?") : "off";
        var recorder = PlatformerRecorder.isRecording() ? "  [recording]" : PlatformerRecorder.isReplaying() ? "  [replaying]" : "";
        lines.add(Component.literal("Platformer: " + profile + (controller.isSuspended() ? " (suspended)" : "") + recorder));
        if (controller.isDriving()) {
            var state = controller.state();
            lines.add(Component.literal("Move: " + state.move + " #" + state.moveSeq + " t=" + state.moveTicks + " " + state.pose.name().toLowerCase(Locale.ROOT)));
            lines.add(Component.literal("Animation: " + PlatformerPlayerAnimator.describe(controller.player())));
            lines.add(Component.literal(String.format(Locale.ROOT, "Speed: %.3f b/t  vy=%.3f  %s", state.horizontalSpeed(), state.vy, state.grounded ? "grounded" : "airborne " + state.airTicks)));
            lines.add(Component.literal(String.format(Locale.ROOT, "Slope: %.2f", state.slope())));
            lines.add(Component.literal("Coyote: " + state.coyote + "  Buffer: jump " + state.jumpBufferMs + " ms, action " + state.actionBufferMs + " ms"));
            lines.add(Component.literal("Chain: " + state.chainIndex + " (" + state.chainWindow + ")  Wall: " + state.wallLeniency + "  Regrab: " + state.ledgeRegrab));
        }
        lines.add(Component.literal("Sources: " + access.sources().stream().map(source -> source.id().toString()).toList()));
        lines.add(Component.literal("Modes: " + access.modes().entrySet().stream().map(entry -> entry.getKey().id().getPath() + "=" + entry.getValue().getSerializedName()).sorted().toList()));
        lines.add(Component.literal("Owned: " + access.unlocks().moves().stream().map(key -> key.identifier().getPath()).sorted().toList()));
        if (controller.isDriving()) {
            var hands = controller.handsGoToMoves(PlatformerClientInput.crouchHeld()) ? "moves" : "item";
            lines.add(Component.literal("Hands: " + hands + (controller.orbiting() ? ", ahead" : ", crosshair")));
            var camera = !PlatformerCamera.isOrbiting() ? "vanilla" : PlatformerCamera.facesCrosshair() ? "shoulder, facing the crosshair" : PlatformerCamera.isAiming() ? "shoulder" : "orbit";
            lines.add(Component.literal(String.format(Locale.ROOT, "Camera: %s  aim=%.2f", camera, PlatformerCamera.aim())));
        }
        return lines;
    }
}
