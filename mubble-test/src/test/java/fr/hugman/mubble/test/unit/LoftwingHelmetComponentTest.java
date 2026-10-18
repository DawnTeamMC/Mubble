package fr.hugman.mubble.test.unit;

import fr.hugman.mubble.zelda.world.item.component.LoftwingHelmetComponent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The one piece of arithmetic behind a loftwing helmet: turning a time to come round ninety degrees
 * into how far the bird may turn in a single tick. No Minecraft needed.
 */
public class LoftwingHelmetComponentTest {
    private static final float TOLERANCE = 1.0E-4F;

    @Test
    @DisplayName("a helmet that takes one second to turn ninety degrees turns 4.5 degrees a tick")
    void oneSecondIsFourAndAHalfDegrees() {
        assertEquals(4.5F, new LoftwingHelmetComponent(2.0F, 1.0F).maxTurnPerTick(), TOLERANCE);
    }

    @Test
    @DisplayName("five seconds to turn ninety degrees is five times slower than one")
    void slowerHelmetsTurnProportionallyLess() {
        float quick = new LoftwingHelmetComponent(2.0F, 1.0F).maxTurnPerTick();
        float slow = new LoftwingHelmetComponent(Float.MAX_VALUE, 5.0F).maxTurnPerTick();

        assertEquals(quick / 5.0F, slow, TOLERANCE);
    }

    @Test
    @DisplayName("a bare head turns faster than anything it could be wearing")
    void bareheadedTurnsFastest() {
        assertTrue(LoftwingHelmetComponent.BAREHEADED.maxTurnPerTick() > new LoftwingHelmetComponent(2.0F, 1.0F).maxTurnPerTick());
        assertEquals(0.0F, LoftwingHelmetComponent.BAREHEADED.crashProtection(), TOLERANCE);
    }
}
