package fr.hugman.mubble.test.unit.arcade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fr.hugman.mubble.test.unit.support.Registrations;
import fr.hugman.mubble.world.arcade.ArcadeAim;
import net.minecraft.world.phys.Vec3;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The focus the orbit camera trails the player with. A camera that jitters at speed is one whose gap
 * to the player does not settle: these hold the gap to settling, at any speed.
 */
public class ArcadeCameraFollowTest {
    /** What a follow lag of 0.08 s keeps of the gap every tick. */
    private static final double KEEP = Math.exp(-0.05D / 0.08D);
    private static final double MAX_GAP = 1.5D;
    private static final double SNAP = 8.0D;

    @BeforeAll
    static void bootstrap() {
        Registrations.registerEverything();
    }

    /** The gaps after each tick of a player moving {@code speed} blocks per tick along x. */
    private static double[] gaps(double speed, int ticks) {
        var focus = Vec3.ZERO;
        var target = Vec3.ZERO;
        double[] gaps = new double[ticks];
        for (int i = 0; i < ticks; i++) {
            target = target.add(speed, 0.0D, 0.0D);
            focus = ArcadeAim.follow(focus, target, KEEP, MAX_GAP, SNAP);
            gaps[i] = target.distanceTo(focus);
        }
        return gaps;
    }

    @Test
    @DisplayName("at any speed the gap grows to a steady distance, without ever snapping back")
    void theGapSettlesAtAnySpeed() {
        for (double speed : new double[]{0.2D, 0.4D, 0.9D, 1.2D, 2.0D, 4.0D}) {
            var gaps = gaps(speed, 60);
            for (int i = 1; i < gaps.length; i++) {
                assertTrue(gaps[i] >= gaps[i - 1] - 1.0E-9D, "at " + speed + " b/t the gap shrank from " + gaps[i - 1] + " to " + gaps[i] + " on tick " + i + ": the camera would jump");
                assertTrue(gaps[i] <= MAX_GAP + 1.0E-9D, "at " + speed + " b/t the gap went past its cap: " + gaps[i]);
            }
            assertEquals(gaps[gaps.length - 2], gaps[gaps.length - 1], 1.0E-6D, "at " + speed + " b/t the gap should have settled");
        }
    }

    @Test
    @DisplayName("once the player stops, the camera catches up")
    void theCameraCatchesUp() {
        var focus = new Vec3(-1.2D, 0.0D, 0.0D);
        for (int i = 0; i < 40; i++) {
            focus = ArcadeAim.follow(focus, Vec3.ZERO, KEEP, MAX_GAP, SNAP);
        }
        assertTrue(focus.length() < 1.0E-6D, "the camera should have caught up, it is still " + focus.length() + " behind");
    }

    @Test
    @DisplayName("a teleport snaps the camera to the player")
    void aTeleportSnaps() {
        var target = new Vec3(100.0D, 64.0D, 0.0D);
        assertEquals(target, ArcadeAim.follow(Vec3.ZERO, target, KEEP, MAX_GAP, SNAP));
    }
}
