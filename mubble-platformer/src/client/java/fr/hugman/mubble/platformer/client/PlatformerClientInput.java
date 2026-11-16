package fr.hugman.mubble.platformer.client;

import fr.hugman.mubble.platformer.PlatformerController;
import fr.hugman.mubble.platformer.client.camera.PlatformerCamera;
import fr.hugman.mubble.platformer.client.compat.PlatformerControllerBindings;
import fr.hugman.mubble.platformer.sim.PlatformerInputCollector;
import fr.hugman.mubble.platformer.sim.PlatformerInputFrame;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;

/**
 * Samples the platformer actions every frame, and hands one input frame per tick to the local driver.
 * <p>
 * Everything comes from the keys of the layer (see {@link PlatformerKeyMappings}), which a controller mod
 * such as Controlify presses for its own bindings, and the movement stick from the movement keys or
 * the analog stick of the controller. While the layer drives, the vanilla keys they stand in for
 * stand aside: the hands answer to the platformer attack and use (see {@link PlatformerHands}), and the jump,
 * sneak and sprint of the player are the platformer ones (see {@link #standIn}), so that crouching shows
 * and the server knows of it whatever key it is on.
 */
@Environment(EnvType.CLIENT)
public final class PlatformerClientInput {
    private static final PlatformerInputCollector COLLECTOR = new PlatformerInputCollector();
    private static boolean wasActive;
    private static int down;

    private PlatformerClientInput() {
    }

    /** Called at the very start of every frame, right after the window events were polled. */
    public static void onFrame(Minecraft minecraft) {
        var player = minecraft.player;
        if (player == null || !PlatformerController.of(player).isDriving()) {
            if (wasActive) {
                COLLECTOR.reset();
                PlatformerHands.reset();
                down = 0;
                wasActive = false;
            }
            // the keys of the layer mean nothing meanwhile: their presses must not pile up for later
            for (var key : PlatformerKeyMappings.DRIVING) {
                while (key.consumeClick()) {
                    // dropped
                }
            }
            return;
        }
        wasActive = true;
        int held = 0;
        int clicks = 0;
        if (minecraft.gui.screen() == null) {
            held |= bit(PlatformerKeyMappings.JUMP, PlatformerInputFrame.JUMP);
            held |= bit(PlatformerKeyMappings.JUMP_ALT, PlatformerInputFrame.JUMP);
            held |= bit(PlatformerKeyMappings.CROUCH, PlatformerInputFrame.CROUCH);
            held |= bit(PlatformerKeyMappings.SPRINT, PlatformerInputFrame.SPRINT);
            held |= bit(PlatformerKeyMappings.SPIN, PlatformerInputFrame.SPIN);
            held |= PlatformerHands.onFrame();
            // presses shorter than a frame only show up as clicks
            clicks |= clicks(PlatformerKeyMappings.JUMP, PlatformerInputFrame.JUMP);
            clicks |= clicks(PlatformerKeyMappings.JUMP_ALT, PlatformerInputFrame.JUMP);
            clicks |= clicks(PlatformerKeyMappings.CROUCH, PlatformerInputFrame.CROUCH);
            clicks |= clicks(PlatformerKeyMappings.SPIN, PlatformerInputFrame.SPIN);
            while (PlatformerKeyMappings.RECENTER.consumeClick()) {
                PlatformerCamera.recenter();
            }
        }
        down = held;
        COLLECTOR.sampleFrame(System.nanoTime(), held, clicks);
    }

    /**
     * The input of the player while the layer drives, once vanilla read it from its keys: the jump,
     * sneak and sprint vanilla knows are the platformer ones, whatever keys they are on.
     */
    public static Input standIn(Input vanilla) {
        return new Input(vanilla.forward(), vanilla.backward(), vanilla.left(), vanilla.right(), jumpHeld(), crouchHeld(), false);
    }

    /** Whether jump was held at the last frame. */
    public static boolean jumpHeld() {
        return (down & PlatformerInputFrame.JUMP) != 0;
    }

    /** Whether crouch was held at the last frame. */
    public static boolean crouchHeld() {
        return (down & PlatformerInputFrame.CROUCH) != 0;
    }

    /** Presses action now: a press of attack or use went to the moves, see {@link PlatformerHands}. */
    public static void pressAction() {
        down |= PlatformerInputFrame.ACTION;
        COLLECTOR.sampleFrame(System.nanoTime(), down, PlatformerInputFrame.ACTION);
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
    public static PlatformerInputFrame frame(LocalPlayer player, int tick) {
        boolean coupled = PlatformerCamera.coupled();
        float yaw = yaw(player);
        float[] stick = stick(player, yaw);
        var frame = COLLECTOR.tick(tick, System.nanoTime(), stick[0], stick[1], yaw, coupled);
        return PlatformerCamera.facesCrosshair() ? frame.aiming() : frame;
    }

    /** The input of the player as it stands, with nothing pressed: what the previews of the button guide start from. */
    public static PlatformerInputFrame current(LocalPlayer player) {
        boolean coupled = PlatformerCamera.coupled();
        float yaw = yaw(player);
        float[] stick = stick(player, yaw);
        var frame = new PlatformerInputFrame(0, stick[0], stick[1], yaw, coupled, (byte) (down & ~PlatformerInputFrame.ACTION), (byte) 0, (short) 0, (short) 0).slowedFor(player);
        return PlatformerCamera.facesCrosshair() ? frame.aiming() : frame;
    }

    /**
     * The yaw of the view: the camera's while it orbits, aiming included, where the body looks at the
     * crosshair from beside the camera, a few degrees off it; the player's otherwise.
     */
    private static float yaw(LocalPlayer player) {
        return PlatformerCamera.isOrbiting() ? PlatformerCamera.yaw() : player.getYRot();
    }

    private static float[] stick(LocalPlayer player, float yaw) {
        var move = move();
        float radians = yaw * Mth.DEG_TO_RAD;
        float sin = Mth.sin(radians);
        float cos = Mth.cos(radians);
        return new float[]{move.x * cos - move.y * sin, move.y * cos + move.x * sin};
    }

    /** The movement stick, relative to the view: x to the left, y forward, as vanilla's. */
    private static Vec2 move() {
        if (Minecraft.getInstance().gui.screen() != null) {
            return Vec2.ZERO;
        }
        float forward = axis(PlatformerKeyMappings.FORWARD, PlatformerKeyMappings.BACKWARD);
        float left = axis(PlatformerKeyMappings.LEFT, PlatformerKeyMappings.RIGHT);
        var keys = new Vec2(left, forward).normalized();
        var controller = PlatformerControllerBindings.Holder.instance;
        var move = controller == null ? keys : keys.add(controller.stick());
        float length = move.length();
        return length > 1.0F ? move.scale(1.0F / length) : move;
    }

    private static float axis(KeyMapping positive, KeyMapping negative) {
        return (positive.isDown() ? 1.0F : 0.0F) - (negative.isDown() ? 1.0F : 0.0F);
    }
}
