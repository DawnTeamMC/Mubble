package fr.hugman.mubble.arcade.client;

import fr.hugman.mubble.arcade.ArcadeController;
import fr.hugman.mubble.arcade.client.camera.ArcadeCamera;
import fr.hugman.mubble.arcade.client.compat.ArcadeControllerBindings;
import fr.hugman.mubble.arcade.sim.ArcadeInputCollector;
import fr.hugman.mubble.arcade.sim.ArcadeInputFrame;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;

/**
 * Samples the arcade actions every frame, and hands one input frame per tick to the local driver.
 * <p>
 * Everything comes from the keys of the layer (see {@link ArcadeKeyMappings}), which a controller mod
 * such as Controlify presses for its own bindings, and the movement stick from the movement keys or
 * the analog stick of the controller. While the layer drives, the vanilla keys they stand in for
 * stand aside: the hands answer to the arcade attack and use (see {@link ArcadeHands}), and the jump,
 * sneak and sprint of the player are the arcade ones (see {@link #standIn}), so that crouching shows
 * and the server knows of it whatever key it is on.
 */
@Environment(EnvType.CLIENT)
public final class ArcadeClientInput {
    private static final ArcadeInputCollector COLLECTOR = new ArcadeInputCollector();
    private static boolean wasActive;
    private static int down;

    private ArcadeClientInput() {
    }

    /** Called at the very start of every frame, right after the window events were polled. */
    public static void onFrame(Minecraft minecraft) {
        var player = minecraft.player;
        if (player == null || !ArcadeController.of(player).isDriving()) {
            if (wasActive) {
                COLLECTOR.reset();
                ArcadeHands.reset();
                down = 0;
                wasActive = false;
            }
            // the keys of the layer mean nothing meanwhile: their presses must not pile up for later
            for (var key : ArcadeKeyMappings.DRIVING) {
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
            held |= bit(ArcadeKeyMappings.JUMP, ArcadeInputFrame.JUMP);
            held |= bit(ArcadeKeyMappings.JUMP_ALT, ArcadeInputFrame.JUMP);
            held |= bit(ArcadeKeyMappings.CROUCH, ArcadeInputFrame.CROUCH);
            held |= bit(ArcadeKeyMappings.SPRINT, ArcadeInputFrame.SPRINT);
            held |= bit(ArcadeKeyMappings.SPIN, ArcadeInputFrame.SPIN);
            held |= ArcadeHands.onFrame();
            // presses shorter than a frame only show up as clicks
            clicks |= clicks(ArcadeKeyMappings.JUMP, ArcadeInputFrame.JUMP);
            clicks |= clicks(ArcadeKeyMappings.JUMP_ALT, ArcadeInputFrame.JUMP);
            clicks |= clicks(ArcadeKeyMappings.CROUCH, ArcadeInputFrame.CROUCH);
            clicks |= clicks(ArcadeKeyMappings.SPIN, ArcadeInputFrame.SPIN);
            while (ArcadeKeyMappings.RECENTER.consumeClick()) {
                ArcadeCamera.recenter();
            }
        }
        down = held;
        COLLECTOR.sampleFrame(System.nanoTime(), held, clicks);
    }

    /**
     * The input of the player while the layer drives, once vanilla read it from its keys: the jump,
     * sneak and sprint vanilla knows are the arcade ones, whatever keys they are on.
     */
    public static Input standIn(Input vanilla) {
        return new Input(vanilla.forward(), vanilla.backward(), vanilla.left(), vanilla.right(), jumpHeld(), crouchHeld(), false);
    }

    /** Whether jump was held at the last frame. */
    public static boolean jumpHeld() {
        return (down & ArcadeInputFrame.JUMP) != 0;
    }

    /** Whether crouch was held at the last frame. */
    public static boolean crouchHeld() {
        return (down & ArcadeInputFrame.CROUCH) != 0;
    }

    /** Presses action now: a press of attack or use went to the moves, see {@link ArcadeHands}. */
    public static void pressAction() {
        down |= ArcadeInputFrame.ACTION;
        COLLECTOR.sampleFrame(System.nanoTime(), down, ArcadeInputFrame.ACTION);
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
        boolean coupled = !ArcadeCamera.isOrbiting();
        float yaw = yaw(player, coupled);
        float[] stick = stick(player, yaw);
        return COLLECTOR.tick(tick, System.nanoTime(), stick[0], stick[1], yaw, coupled);
    }

    /** The input of the player as it stands, with nothing pressed: what the previews of the button guide start from. */
    public static ArcadeInputFrame current(LocalPlayer player) {
        boolean coupled = !ArcadeCamera.isOrbiting();
        float yaw = yaw(player, coupled);
        float[] stick = stick(player, yaw);
        return new ArcadeInputFrame(0, stick[0], stick[1], yaw, coupled, (byte) (down & ~ArcadeInputFrame.ACTION), (byte) 0, (short) 0, (short) 0).slowedFor(player);
    }

    private static float yaw(LocalPlayer player, boolean coupled) {
        return coupled ? player.getYRot() : ArcadeCamera.yaw();
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
        float forward = axis(ArcadeKeyMappings.FORWARD, ArcadeKeyMappings.BACKWARD);
        float left = axis(ArcadeKeyMappings.LEFT, ArcadeKeyMappings.RIGHT);
        var keys = new Vec2(left, forward).normalized();
        var controller = ArcadeControllerBindings.Holder.instance;
        var move = controller == null ? keys : keys.add(controller.stick());
        float length = move.length();
        return length > 1.0F ? move.scale(1.0F / length) : move;
    }

    private static float axis(KeyMapping positive, KeyMapping negative) {
        return (positive.isDown() ? 1.0F : 0.0F) - (negative.isDown() ? 1.0F : 0.0F);
    }
}
