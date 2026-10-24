package fr.hugman.mubble.test.unit.platformer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fr.hugman.mubble.platformer.PlatformerCameraFollow;
import fr.hugman.mubble.test.unit.support.Registrations;
import net.minecraft.util.Mth;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The camera swinging round behind the player on its own: hardly at all walking, slowly once they go
 * a long way, plainly when they roll, never right after the hand turned it, never round when they run
 * at it, and always without a jolt.
 */
public class PlatformerCameraFollowTest {
    /** The walk and run speeds of the overworld profile, in blocks per tick, and a roll. */
    private static final double WALK = 0.216D;
    private static final double RUN = 0.34D;
    private static final double ROLL = 0.6D;
    /** The default top rate, in degrees per second. */
    private static final double TOP = 90.0D;
    /** One step a tick. */
    private static final double TICK = 0.05D;

    @BeforeAll
    static void bootstrap() {
        Registrations.registerEverything();
    }

    /** The camera, looking north, following a player going east at a speed for some seconds: how far it turned, in degrees. */
    private static float sideways(PlatformerCameraFollow follow, float yaw, double speed, double seconds) {
        for (int i = 0; i < Math.round(seconds / TICK); i++) {
            yaw += follow.step(yaw, 90.0F, speed, RUN, TOP, TICK);
        }
        return yaw;
    }

    @Test
    @DisplayName("walking sideways for two seconds hardly turns the camera")
    void walkingBarelyTurns() {
        float yaw = sideways(new PlatformerCameraFollow(), 0.0F, WALK, 2.0D);
        assertTrue(Math.abs(yaw) < 1.0F, "two seconds of walking turned the camera " + yaw + " degrees");
    }

    @Test
    @DisplayName("running sideways for two seconds barely turns it")
    void runningBarelyTurns() {
        float yaw = sideways(new PlatformerCameraFollow(), 0.0F, RUN, 2.0D);
        assertTrue(Math.abs(yaw) < 8.0F, "two seconds of running turned the camera " + yaw + " degrees");
    }

    @Test
    @DisplayName("walking a long way, the camera slowly comes round")
    void walkingFarFollows() {
        var follow = new PlatformerCameraFollow();
        float yaw = 0.0F;
        double fastest = 0.0D;
        for (int i = 0; i < 30 * 20; i++) {
            yaw += follow.step(yaw, 90.0F, WALK, RUN, TOP, TICK);
            fastest = Math.max(fastest, Math.abs(follow.rate()));
        }
        assertTrue(yaw > 60.0F, "after half a minute of walking east, the camera should have come round, only turned " + yaw);
        assertTrue(fastest < 25.0D, "walking, the camera should turn slowly, went " + fastest + " degrees per second");
    }

    @Test
    @DisplayName("rolling fast, the camera follows plainly, more than running")
    void rollingFollows() {
        float rolled = sideways(new PlatformerCameraFollow(), 0.0F, ROLL, 2.0D);
        float ran = sideways(new PlatformerCameraFollow(), 0.0F, RUN, 2.0D);
        assertTrue(rolled > 30.0F, "two seconds of rolling should have turned the camera, only " + rolled);
        assertTrue(rolled > 4.0F * ran, "rolling should follow far more than running: " + rolled + " against " + ran);
    }

    @Test
    @DisplayName("turning the camera by hand stops following a while, then it comes back gently")
    void touchingPauses() {
        var follow = new PlatformerCameraFollow();
        sideways(follow, 0.0F, ROLL, 2.0D);
        // the hand turns the camera back north
        follow.touch();
        float yaw = sideways(follow, 0.0F, ROLL, PlatformerCameraFollow.TOUCH_DELAY - TICK);
        assertEquals(0.0F, yaw, 1.0E-6F, "within the delay, the camera should stay where the hand left it");
        double last = 0.0D;
        for (int i = 0; i < 10; i++) {
            yaw += follow.step(yaw, 90.0F, ROLL, RUN, TOP, TICK);
            assertTrue(Math.abs(follow.rate()) >= last - 1.0E-9D, "waking up, the camera should speed up steadily");
            last = Math.abs(follow.rate());
        }
        assertTrue(last > 0.0D && last < 25.0D, "half a second after the delay, the camera should only just turn, at " + last + " degrees per second");
    }

    @Test
    @DisplayName("running towards the camera never turns it round")
    void runningAtTheCameraNeverTurns() {
        for (float heading : new float[]{180.0F, 160.0F, -150.0F, 140.0F}) {
            var follow = new PlatformerCameraFollow();
            float yaw = 0.0F;
            for (int i = 0; i < 10 * 20; i++) {
                yaw += follow.step(yaw, heading, ROLL, RUN, TOP, TICK);
            }
            assertEquals(0.0F, yaw, 1.0E-6F, "rolling at the camera, " + heading + " degrees off, turned it");
        }
    }

    @Test
    @DisplayName("standing still never turns the camera")
    void standingNeverTurns() {
        var follow = new PlatformerCameraFollow();
        float yaw = 0.0F;
        for (int i = 0; i < 10 * 20; i++) {
            yaw += follow.step(yaw, 90.0F, 0.0D, RUN, TOP, TICK);
        }
        assertEquals(0.0F, yaw, 1.0E-6F);
    }

    @Test
    @DisplayName("the camera never jolts: its angular speed changes a little at a time, and it stops on the player")
    void theRateIsContinuous() {
        var follow = new PlatformerCameraFollow();
        float yaw = 0.0F;
        double last = 0.0D;
        double limit = PlatformerCameraFollow.ACCELERATION * TICK + 1.0E-9D;
        // rolling east, then west, then standing, then a long run north
        float[] headings = {90.0F, -90.0F, -90.0F, 0.0F};
        double[] speeds = {ROLL, ROLL, 0.0D, RUN};
        for (int phase = 0; phase < headings.length; phase++) {
            for (int i = 0; i < 8 * 20; i++) {
                yaw = Mth.wrapDegrees(yaw + follow.step(yaw, headings[phase], speeds[phase], RUN, TOP, TICK));
                assertTrue(Math.abs(follow.rate() - last) <= limit, "the angular speed jumped from " + last + " to " + follow.rate());
                last = follow.rate();
            }
        }
        assertTrue(Math.abs(Mth.degreesDifference(yaw, 0.0F)) < 30.0F, "after a long run, the camera should be nearly behind the player, still " + yaw);
    }

    @Test
    @DisplayName("a sharp turn starts the distance afresh, a gentle curve does not")
    void aSharpTurnStartsAfresh() {
        var follow = new PlatformerCameraFollow();
        float heading = 0.0F;
        for (int i = 0; i < 5 * 20; i++) {
            heading += 1.0F;
            follow.step(heading, heading, WALK, RUN, TOP, TICK);
        }
        assertTrue(follow.travel() > 20.0D, "a gentle curve should keep counting, only " + follow.travel());
        follow.step(heading, heading + 90.0F, WALK, RUN, TOP, TICK);
        assertTrue(follow.travel() < 1.0D, "a sharp turn should start afresh, still " + follow.travel());
    }
}
