package fr.hugman.mubble.test.unit;

import fr.hugman.mubble.test.unit.support.TestBootstrap;
import fr.hugman.mubble.world.entity.projectile.Ball;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Whether a {@link Ball} trails anything at all depends on the ground it covers during the tick. The
 * particles themselves only exist on a client, but the count behind them is plain maths and belongs
 * here rather than in a game test.
 */
public class BallTrailTest {
    /** Roughly the distance a ball thrown by a player covers in one tick. */
    private static final double THROWN_BALL_DISTANCE = 1.5D;

    @BeforeAll
    static void bootstrapMinecraft() {
        TestBootstrap.bootstrap();
    }

    @Test
    @DisplayName("a ball that has come to a halt trails nothing")
    void motionlessBallTrailsNothing() {
        assertEquals(0, Ball.trailParticleCount(0.0D), "a ball standing still should not trail anything");
        // A ball resting on the ground still jitters by a fraction of a block, which is no movement to speak of.
        assertEquals(0, Ball.trailParticleCount(0.001D), "a barely moving ball should not trail anything");
    }

    @Test
    @DisplayName("a moving ball always trails at least one particle")
    void movingBallAlwaysTrailsSomething() {
        assertTrue(Ball.trailParticleCount(0.05D) >= 1, "a slowly moving ball should still trail something");
        assertTrue(Ball.trailParticleCount(THROWN_BALL_DISTANCE) >= 1, "a thrown ball should trail something");
    }

    @Test
    @DisplayName("going faster spaces the trail out rather than thickening it")
    void speedDoesNotThickenTheTrail() {
        int slow = Ball.trailParticleCount(0.05D);
        int fast = Ball.trailParticleCount(THROWN_BALL_DISTANCE);
        int absurd = Ball.trailParticleCount(1000.0D);

        assertEquals(slow, fast, "a faster ball should trail no more particles than a slow one");
        assertEquals(slow, absurd, "however fast a ball goes, a single tick should never spawn a screenful of particles");
    }
}
