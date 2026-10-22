package fr.hugman.mubble.test.unit.arcade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fr.hugman.mubble.arcade.ArcadeFraming;
import fr.hugman.mubble.test.unit.support.Registrations;
import net.minecraft.world.phys.Vec3;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The point the orbit camera looks at: the player moves freely within a margin around it, jumps leave
 * its height alone until the ground changes, and moving on brings the player back to the middle.
 */
public class ArcadeFramingTest {
    /** The run speed of the overworld profile, in blocks per tick. */
    private static final double RUN = 0.34D;

    @BeforeAll
    static void bootstrap() {
        Registrations.registerEverything();
    }

    private static ArcadeFraming settledAt(Vec3 position) {
        var framing = new ArcadeFraming();
        for (int i = 0; i < 20; i++) {
            framing.tick(position, true, 1.0D, 0.0D);
        }
        return framing;
    }

    private static double side(Vec3 target, Vec3 focus) {
        return Math.sqrt((target.x - focus.x) * (target.x - focus.x) + (target.z - focus.z) * (target.z - focus.z));
    }

    @Test
    @DisplayName("a step aside leaves the camera where it is")
    void aStepAsideLeavesTheCamera() {
        var framing = settledAt(Vec3.ZERO);
        var target = Vec3.ZERO;
        for (int i = 0; i < 3; i++) {
            target = target.add(0.15D, 0.0D, 0.0D);
            framing.tick(target, true, 1.0D, 0.0D);
        }
        for (int i = 0; i < 20; i++) {
            framing.tick(target, true, 1.0D, 0.0D);
        }
        assertEquals(Vec3.ZERO, framing.focus(), "half a block aside, the camera should not have moved");
    }

    @Test
    @DisplayName("a jump in place leaves the height of the camera alone")
    void aJumpLeavesTheHeight() {
        var framing = settledAt(Vec3.ZERO);
        for (double y : new double[]{0.6D, 1.2D, 1.7D, 2.0D, 1.7D, 1.2D, 0.6D}) {
            framing.tick(new Vec3(0.0D, y, 0.0D), false, 1.0D, 0.0D);
            assertEquals(0.0D, framing.focus().y, 1.0E-9D, "at " + y + " blocks up, the camera should not have risen");
        }
        framing.tick(Vec3.ZERO, true, 1.0D, 0.0D);
        assertEquals(Vec3.ZERO, framing.focus());
    }

    @Test
    @DisplayName("landing higher up, the camera eases up to the new ground")
    void landingHigherRaisesTheCamera() {
        var framing = settledAt(Vec3.ZERO);
        var ledge = new Vec3(0.0D, 1.5D, 0.0D);
        double last = 0.0D;
        for (int i = 0; i < 40; i++) {
            framing.tick(ledge, true, 1.0D, 0.0D);
            double y = framing.focus().y;
            assertTrue(y >= last - 1.0E-9D && y <= ledge.y + 1.0E-9D, "the camera should rise steadily, it went from " + last + " to " + y);
            last = y;
        }
        assertEquals(ledge.y, last, 1.0E-3D, "the camera should have reached the new ground");
    }

    @Test
    @DisplayName("running on brings the player back to the middle, never past the hard limit")
    void runningRecenters() {
        var framing = settledAt(Vec3.ZERO);
        var target = Vec3.ZERO;
        double widest = 0.0D;
        for (int i = 0; i < 80; i++) {
            target = target.add(RUN, 0.0D, 0.0D);
            framing.tick(target, true, 1.0D, 0.0D);
            widest = Math.max(widest, side(target, framing.focus()));
        }
        assertTrue(widest > 0.3D, "starting to run, the player should have left the middle a little, only got " + widest + " away");
        assertTrue(widest <= ArcadeFraming.SIDE_MARGIN + ArcadeFraming.HARD_LIMIT + 1.0E-9D, "the player went past the hard limit: " + widest);
        assertTrue(side(target, framing.focus()) < 0.01D, "after a run the player should be in the middle, still " + side(target, framing.focus()) + " away");
    }

    @Test
    @DisplayName("the camera trails a run rather than sticking to the player")
    void theCameraTrailsARun() {
        var framing = settledAt(Vec3.ZERO);
        var target = Vec3.ZERO;
        for (int i = 0; i < 20; i++) {
            target = target.add(RUN, 0.0D, 0.0D);
            framing.tick(target, true, 1.0D, 0.0D);
        }
        assertTrue(side(target, framing.focus()) > 0.5D, "a second into a run, the player should still be off the middle, only " + side(target, framing.focus()) + " away");
    }

    @Test
    @DisplayName("however fast the player goes, they stay within the hard limit")
    void theHardLimitHolds() {
        for (double speed : new double[]{0.5D, 1.2D, 3.0D}) {
            var framing = settledAt(Vec3.ZERO);
            var target = Vec3.ZERO;
            for (int i = 0; i < 30; i++) {
                target = target.add(speed, -speed, 0.0D);
                framing.tick(target, false, 1.0D, 0.0D);
                var focus = framing.focus();
                assertTrue(side(target, focus) <= ArcadeFraming.SIDE_MARGIN + ArcadeFraming.HARD_LIMIT + 1.0E-9D, "at " + speed + " b/t, sideways " + side(target, focus));
                assertTrue(focus.y - target.y <= ArcadeFraming.BELOW_MARGIN + ArcadeFraming.HARD_LIMIT + 1.0E-9D, "at " + speed + " b/t, falling " + (focus.y - target.y) + " below");
            }
        }
    }

    @Test
    @DisplayName("with no margin, the camera stays on the player")
    void noMarginNoRoom() {
        var framing = settledAt(Vec3.ZERO);
        var target = new Vec3(0.3D, 1.0D, -0.2D);
        framing.tick(target, false, 0.0D, 0.0D);
        assertEquals(target, framing.focus());
    }

    @Test
    @DisplayName("a teleport snaps the camera to the player")
    void aTeleportSnaps() {
        var framing = settledAt(Vec3.ZERO);
        var target = new Vec3(100.0D, 64.0D, 0.0D);
        framing.tick(target, false, 1.0D, 0.0D);
        assertEquals(target, framing.focus());
    }

    @Test
    @DisplayName("a recenter brings the player back to the middle, margins aside, then the margins hold again")
    void aRecenterBringsThePlayerBack() {
        var framing = settledAt(Vec3.ZERO);
        // standing half a block aside, mid-jump: within every margin, so the framing alone leaves it
        var target = new Vec3(0.5D, 2.0D, 0.0D);
        framing.tick(target, false, 1.0D, 0.0D);
        framing.tick(target, false, 1.0D, 0.0D);
        assertEquals(Vec3.ZERO, framing.focus(), "within the margins, the camera should not have moved");
        framing.recenter();
        int ticks = 0;
        while (framing.isRecentering() && ticks < 40) {
            framing.tick(target, false, 1.0D, 0.0D);
            ticks++;
        }
        assertTrue(!framing.isRecentering() && ticks <= 12, "the recenter should be over within 12 ticks, took " + ticks);
        assertEquals(target, framing.focus(), "after a recenter, the player should be in the middle");
        framing.tick(target.add(0.3D, 0.0D, 0.0D), true, 1.0D, 0.0D);
        assertEquals(target.x, framing.focus().x, 1.0E-9D, "once recentered, a step aside should leave the camera again");
    }

    @Test
    @DisplayName("an offset stays while nothing moves, and fades gently as the camera turns")
    void turningTheCameraLetsGoOfTheOffset() {
        var framing = settledAt(Vec3.ZERO);
        var target = new Vec3(0.6D, 0.0D, 0.0D);
        for (int i = 0; i < 20; i++) {
            framing.tick(target, true, 1.0D, 0.0D);
        }
        assertEquals(Vec3.ZERO, framing.focus(), "with neither the camera nor the player moving, the offset should stay");
        double last = side(target, framing.focus());
        for (int i = 0; i < 9; i++) {
            framing.tick(target, true, 1.0D, 10.0D);
            double now = side(target, framing.focus());
            assertTrue(now < last && last - now < 0.1D, "turning 10 degrees should take a little of the offset off, went from " + last + " to " + now);
            last = now;
        }
        assertEquals(0.6D * Math.exp(-1.0D), last, 1.0E-6D, "after a 90 degree turn, about a third of the offset should be left");
    }
}
