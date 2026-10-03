package fr.hugman.mubble.test.unit.arcade;

import fr.hugman.mubble.world.arcade.sim.ArcadeInputCollector;
import fr.hugman.mubble.world.arcade.sim.ArcadeInputFrame;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The frame-sampled input buffer: what happens between two ticks has to reach the tick.
 */
public class ArcadeInputCollectorTest {
    private static final long MS = 1_000_000L;

    @Test
    @DisplayName("a jump pressed and released between two ticks still reaches the next one")
    void aPressShorterThanATickIsCaught() {
        var collector = new ArcadeInputCollector();
        collector.sampleFrame(0L, 0, 0);
        // a single frame, one frame before the tick: down and already up again by the next poll
        collector.sampleFrame(33 * MS, 0, ArcadeInputFrame.JUMP);
        var frame = collector.tick(1, 50 * MS, 0.0F, 0.0F, 0.0F, true);
        assertTrue(frame.wasPressed(ArcadeInputFrame.JUMP), "the press should be in the frame");
        assertFalse(frame.isHeld(ArcadeInputFrame.JUMP), "the key was already released");
        assertEquals(17, frame.jumpAgeMs(), "the age should be counted from the press itself");
    }

    @Test
    @DisplayName("a held key is pressed once, then only held")
    void aHeldKeyIsPressedOnce() {
        var collector = new ArcadeInputCollector();
        collector.sampleFrame(0L, ArcadeInputFrame.ACTION, 0);
        collector.sampleFrame(16 * MS, ArcadeInputFrame.ACTION, 0);
        var first = collector.tick(1, 20 * MS, 0.0F, 0.0F, 0.0F, true);
        collector.sampleFrame(40 * MS, ArcadeInputFrame.ACTION, 0);
        var second = collector.tick(2, 70 * MS, 0.0F, 0.0F, 0.0F, true);
        assertTrue(first.wasPressed(ArcadeInputFrame.ACTION));
        assertEquals(20, first.actionAgeMs());
        assertFalse(second.wasPressed(ArcadeInputFrame.ACTION), "holding a key down is not pressing it again");
        assertTrue(second.isHeld(ArcadeInputFrame.ACTION));
    }

    @Test
    @DisplayName("several frames before a tick keep the time of the first press")
    void theFirstPressOfATickCounts() {
        var collector = new ArcadeInputCollector();
        collector.sampleFrame(5 * MS, ArcadeInputFrame.JUMP, 0);
        collector.sampleFrame(10 * MS, ArcadeInputFrame.JUMP, 0);
        var frame = collector.tick(1, 50 * MS, 0.0F, 0.0F, 0.0F, true);
        assertEquals(45, frame.jumpAgeMs());
    }

    @Test
    @DisplayName("nothing pressed, no age")
    void noPressNoAge() {
        var collector = new ArcadeInputCollector();
        collector.sampleFrame(0L, 0, 0);
        var frame = collector.tick(1, 50 * MS, 0.0F, 0.0F, 0.0F, true);
        assertEquals(0, frame.pressed());
        assertEquals(0, frame.jumpAgeMs());
    }
}
