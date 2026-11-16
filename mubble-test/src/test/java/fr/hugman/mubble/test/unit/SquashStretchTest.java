package fr.hugman.mubble.test.unit;

import fr.hugman.mubble.util.SquashStretch;
import org.joml.Vector3f;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SquashStretchTest {
    private static final float EPSILON = 1.0E-4F;

    private static float volume(Vector3f scale) {
        return scale.x * scale.y * scale.z;
    }

    @Test
    @DisplayName("a deformed shape keeps its volume")
    void keepsVolume() {
        for (float amount : new float[]{-0.6F, -0.25F, 0.0F, 0.3F, 1.5F}) {
            assertEquals(1.0F, volume(SquashStretch.alongX(amount)), EPSILON);
            assertEquals(1.0F, volume(SquashStretch.alongY(amount)), EPSILON);
            assertEquals(1.0F, volume(SquashStretch.alongZ(amount)), EPSILON);
        }
    }

    @Test
    @DisplayName("no amount leaves the shape alone")
    void zeroIsIdentity() {
        assertEquals(new Vector3f(1.0F, 1.0F, 1.0F), SquashStretch.alongY(0.0F));
    }

    @Test
    @DisplayName("a stretch grows its axis and thins the others, a squash does the opposite")
    void stretchesAndSquashes() {
        Vector3f stretched = SquashStretch.alongY(0.5F);
        assertTrue(stretched.y > 1.0F && stretched.x < 1.0F && stretched.z < 1.0F);
        Vector3f squashed = SquashStretch.alongY(-0.5F);
        assertTrue(squashed.y < 1.0F && squashed.x > 1.0F && squashed.z > 1.0F);
    }

    @Test
    @DisplayName("a scale never collapses or flips, however hard the squash")
    void neverCollapses() {
        Vector3f crushed = SquashStretch.alongY(-5.0F);
        assertTrue(crushed.y > 0.0F);
        assertEquals(1.0F, volume(crushed), EPSILON);
    }

    @Test
    @DisplayName("a pulse rises and comes back")
    void pulse() {
        assertEquals(0.0F, SquashStretch.pulse(0.0F), EPSILON);
        assertEquals(1.0F, SquashStretch.pulse(0.5F), EPSILON);
        assertEquals(0.0F, SquashStretch.pulse(1.0F), EPSILON);
        assertEquals(0.0F, SquashStretch.pulse(3.0F), EPSILON);
    }

    @Test
    @DisplayName("a wobble starts at full and has died off by the end")
    void wobble() {
        assertEquals(1.0F, SquashStretch.wobble(0.0F, 3.0F), EPSILON);
        assertEquals(0.0F, SquashStretch.wobble(1.0F, 3.0F), EPSILON);
        assertTrue(Math.abs(SquashStretch.wobble(0.9F, 3.0F)) < Math.abs(SquashStretch.wobble(0.1F, 3.0F)));
    }

    @Test
    @DisplayName("a speed is worth its share of the maximum, capped")
    void fromSpeed() {
        assertEquals(0.0F, SquashStretch.fromSpeed(0.0D, 1.0D, 0.4F), EPSILON);
        assertEquals(0.2F, SquashStretch.fromSpeed(0.5D, 1.0D, 0.4F), EPSILON);
        assertEquals(0.4F, SquashStretch.fromSpeed(3.0D, 1.0D, 0.4F), EPSILON);
        assertEquals(0.0F, SquashStretch.fromSpeed(1.0D, 0.0D, 0.4F), EPSILON);
    }
}
