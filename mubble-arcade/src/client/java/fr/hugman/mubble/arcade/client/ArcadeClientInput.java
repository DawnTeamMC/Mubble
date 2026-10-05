package fr.hugman.mubble.arcade.client;

import fr.hugman.mubble.arcade.client.camera.ArcadeCamera;
import fr.hugman.mubble.arcade.client.compat.ArcadeControllerBindings;
import fr.hugman.mubble.arcade.ArcadeController;
import fr.hugman.mubble.arcade.sim.ArcadeInputCollector;
import fr.hugman.mubble.arcade.sim.ArcadeInputFrame;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;

/**
 * Samples the arcade actions every frame, and hands one input frame per tick to the local driver.
 */
@Environment(EnvType.CLIENT)
public final class ArcadeClientInput {
    private static final ArcadeInputCollector COLLECTOR = new ArcadeInputCollector();
    private static boolean wasActive;

    private ArcadeClientInput() {
    }

    /** Called at the very start of every frame, right after the window events were polled. */
    public static void onFrame(Minecraft minecraft) {
        var player = minecraft.player;
        if (player == null || !ArcadeController.of(player).isActive()) {
            if (wasActive) {
                COLLECTOR.reset();
                wasActive = false;
            }
            return;
        }
        wasActive = true;
        int down = 0;
        int clicks = 0;
        if (minecraft.gui.screen() == null) {
            down |= bit(ArcadeKeyMappings.jump(minecraft), ArcadeInputFrame.JUMP);
            down |= bit(ArcadeKeyMappings.crouch(minecraft), ArcadeInputFrame.CROUCH);
            down |= bit(ArcadeKeyMappings.ACTION, ArcadeInputFrame.ACTION);
            down |= bit(minecraft.options.keySprint, ArcadeInputFrame.SPRINT);
            down |= bit(ArcadeKeyMappings.SPIN, ArcadeInputFrame.SPIN);
            // presses shorter than a frame only show up as clicks
            clicks |= clicks(ArcadeKeyMappings.ACTION, ArcadeInputFrame.ACTION);
            clicks |= clicks(ArcadeKeyMappings.SPIN, ArcadeInputFrame.SPIN);
            if (!ArcadeKeyMappings.JUMP.isUnbound()) {
                clicks |= clicks(ArcadeKeyMappings.JUMP, ArcadeInputFrame.JUMP);
            }
            if (!ArcadeKeyMappings.CROUCH.isUnbound()) {
                clicks |= clicks(ArcadeKeyMappings.CROUCH, ArcadeInputFrame.CROUCH);
            }
            var controller = ArcadeControllerBindings.Holder.instance;
            if (controller != null) {
                down |= controller.held();
                clicks |= controller.pressed();
                if (controller.recenterPressed()) {
                    ArcadeCamera.recenter();
                }
            }
            while (ArcadeKeyMappings.RECENTER.consumeClick()) {
                ArcadeCamera.recenter();
            }
        }
        COLLECTOR.sampleFrame(System.nanoTime(), down, clicks);
    }

    private static int bit(KeyMapping key, int action) {
        return key.isDown() ? action : 0;
    }

    private static int clicks(KeyMapping key, int action) {
        int bits = 0;
        while (key.consumeClick()) {
            bits = action;
        }
        return bits;
    }

    /**
     * The frame of the tick about to be simulated: the movement stick turned into the world by the
     * camera, and every action sampled since the previous tick.
     */
    public static ArcadeInputFrame frame(LocalPlayer player, int tick) {
        var move = player.input.getMoveVector();
        boolean coupled = !ArcadeCamera.isOrbiting();
        float yaw = coupled ? player.getYRot() : ArcadeCamera.yaw();
        float radians = yaw * Mth.DEG_TO_RAD;
        float sin = Mth.sin(radians);
        float cos = Mth.cos(radians);
        float stickX = move.x * cos - move.y * sin;
        float stickZ = move.y * cos + move.x * sin;
        return COLLECTOR.tick(tick, System.nanoTime(), stickX, stickZ, yaw, coupled);
    }
}
