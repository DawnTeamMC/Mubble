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

/**
 * Samples the arcade actions every frame, and hands one input frame per tick to the local driver.
 * <p>
 * Jump, crouch and sprint are the vanilla jump, sneak and sprint: the keys, sampled every frame, and
 * the input of the player, read every tick, which is where a controller mod such as Controlify puts
 * them. Crouching is sneaking, then, whatever presses it, and the player shows it. Action comes from
 * attack and use, when {@link ArcadeHands} gives their press to the moves.
 */
@Environment(EnvType.CLIENT)
public final class ArcadeClientInput {
    private static final ArcadeInputCollector COLLECTOR = new ArcadeInputCollector();
    private static boolean wasActive;
    private static int down;
    /** The actions the input of the player held at the last tick. */
    private static int inputHeld;

    private ArcadeClientInput() {
    }

    /** Called at the very start of every frame, right after the window events were polled. */
    public static void onFrame(Minecraft minecraft) {
        var player = minecraft.player;
        if (player == null || !ArcadeController.of(player).isActive()) {
            if (wasActive) {
                COLLECTOR.reset();
                ArcadeHands.reset();
                down = 0;
                inputHeld = 0;
                wasActive = false;
            }
            return;
        }
        wasActive = true;
        int held = 0;
        int clicks = 0;
        if (minecraft.gui.screen() == null) {
            var options = minecraft.options;
            held |= bit(options.keyJump, ArcadeInputFrame.JUMP);
            held |= bit(options.keyShift, ArcadeInputFrame.CROUCH);
            held |= bit(options.keySprint, ArcadeInputFrame.SPRINT);
            held |= bit(ArcadeKeyMappings.SPIN, ArcadeInputFrame.SPIN);
            held |= inputHeld;
            held |= ArcadeHands.onFrame(minecraft);
            // presses shorter than a frame only show up as clicks
            clicks |= clicks(ArcadeKeyMappings.SPIN, ArcadeInputFrame.SPIN);
            var controller = ArcadeControllerBindings.Holder.instance;
            if (controller != null) {
                held |= controller.held();
            }
            while (ArcadeKeyMappings.RECENTER.consumeClick()) {
                ArcadeCamera.recenter();
            }
        }
        sample(held, clicks);
    }

    private static void sample(int held, int clicks) {
        down = held;
        COLLECTOR.sampleFrame(System.nanoTime(), held, clicks);
    }

    /** The actions the input of the player holds this tick: the keys, or whatever a controller mod pressed in their place. */
    private static int inputHeld(LocalPlayer player) {
        var keys = player.input.keyPresses;
        return (keys.jump() ? ArcadeInputFrame.JUMP : 0)
                | (keys.shift() ? ArcadeInputFrame.CROUCH : 0)
                | (keys.sprint() ? ArcadeInputFrame.SPRINT : 0);
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
        // the input of the player ticked right before: what it holds now stands for what it held before
        int input = inputHeld(player);
        sample((down & ~inputHeld) | input, 0);
        inputHeld = input;
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
        var move = player.input.getMoveVector();
        float radians = yaw * Mth.DEG_TO_RAD;
        float sin = Mth.sin(radians);
        float cos = Mth.cos(radians);
        return new float[]{move.x * cos - move.y * sin, move.y * cos + move.x * sin};
    }
}
