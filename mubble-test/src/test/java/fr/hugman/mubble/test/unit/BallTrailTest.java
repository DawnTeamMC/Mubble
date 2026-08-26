package fr.hugman.mubble.test.unit;

import fr.hugman.mubble.test.unit.support.TestBootstrap;
import fr.hugman.mubble.world.entity.projectile.Ball;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * How thick the trail of a {@link Ball} is depends on the ground it covered during the tick. The
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
    @DisplayName("the farther a ball travelled, the thicker its trail")
    void longerPathsTrailMoreParticles() {
        int slow = Ball.trailParticleCount(0.1D);
        int fast = Ball.trailParticleCount(THROWN_BALL_DISTANCE);

        assertTrue(fast > slow, "a ball covering more ground should trail more particles, got " + fast + " against " + slow);
    }

    @Test
    @DisplayName("the trail of a single tick stays capped, however fast the ball goes")
    void veryFastBallsStayCapped() {
        int fast = Ball.trailParticleCount(THROWN_BALL_DISTANCE * 10.0D);
        int absurd = Ball.trailParticleCount(1000.0D);

        assertEquals(fast, absurd, "past a point, going faster should not add any more particles");
        assertTrue(absurd <= 16, "a single tick should never spawn a screenful of particles, got " + absurd);
    }
}
