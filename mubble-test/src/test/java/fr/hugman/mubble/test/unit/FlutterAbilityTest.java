package fr.hugman.mubble.test.unit;

import fr.hugman.mubble.world.power_up.ability.FlutterAbility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The shape of the climb half of a jump held on: what each of its ticks is worth.
 * <p>
 * A flutter opens on a drop and only turns that around a few ticks in, which is what makes the lift land as
 * a snap rather than as a balloon. The curve behind that is worth pinning down on its own, away from a level
 * and a player.
 */
public class FlutterAbilityTest {
    private static final int DURATION = 8;
    private static final float DROP = 0.2F;
    private static final float ACCELERATION = 0.1F;

    private static final FlutterAbility FLUTTER = FlutterAbility.of(DURATION, DROP, ACCELERATION);
    private static final float EPSILON = 1.0E-6F;

    @Test
    @DisplayName("the flutter opens on a drop rather than on a lift")
    void theFlutterOpensOnADrop() {
        assertEquals(-DROP, FLUTTER.liftAt(0), EPSILON, "the very first tick of a flutter");
        assertTrue(FLUTTER.liftAt(0) < 0.0F, "the first tick of a flutter should still be sinking");
    }

    @Test
    @DisplayName("every tick takes the same bite out of the drop, and then out of the sky")
    void everyTickIsWorthTheAcceleration() {
        for (int tick = 1; tick < DURATION; tick++) {
            assertEquals(ACCELERATION, FLUTTER.liftAt(tick) - FLUTTER.liftAt(tick - 1), EPSILON,
                    "the speed tick " + tick + " added to the one before it");
        }
    }

    @Test
    @DisplayName("the hang lasts until the acceleration has eaten the drop, and no longer")
    void theHangGivesWayToLift() {
        int hang = FLUTTER.hangTicks();

        assertTrue(hang > 0, "a flutter should hang for at least a tick before lifting");
        assertTrue(hang < DURATION, "a flutter that hung for its whole duration would never lift at all");
        assertTrue(FLUTTER.liftAt(hang - 1) < 0.0F, "the last tick of the hang should still be sinking");
        assertTrue(FLUTTER.liftAt(hang) >= 0.0F, "the tick after the hang should no longer be sinking");
    }

    /** A couple of ticks and no more: the hang is an anticipation, not a fall. */
    @Test
    @DisplayName("the hang is over in a couple of ticks")
    void theHangIsShort() {
        assertEquals(2, FLUTTER.hangTicks(), "the ticks a flutter hangs for");
    }

    /** A flutter that plateaued would carry its holder the way a rising platform does. */
    @Test
    @DisplayName("the lift never settles on a top speed")
    void theLiftNeverPlateaus() {
        assertTrue(FLUTTER.liftAt(DURATION - 1) > FLUTTER.liftAt(DURATION - 2),
                "the last tick of a flutter should still be lifting harder than the one before it");
    }

    @Test
    @DisplayName("a flutter is worth height overall, drop and all")
    void theFlutterIsWorthHeightOverall() {
        // The acceleration is added once on the second tick, twice on the third, and so on.
        float ticks = DURATION;
        float expected = ACCELERATION * (ticks - 1.0F) * ticks / 2.0F - DROP * ticks;

        assertEquals(expected, FLUTTER.totalLift(), EPSILON, "the height a whole flutter is worth");
        assertTrue(FLUTTER.totalLift() > 0.0F, "a flutter should be worth more than the drop it opens on");
    }

    @Test
    @DisplayName("a flutter that never accelerates never stops sinking")
    void noAccelerationMeansNoLift() {
        var stalled = FlutterAbility.of(DURATION, DROP, 0.0F);

        assertEquals(-DROP, stalled.liftAt(DURATION - 1), EPSILON, "the last tick of a flutter with no acceleration");
        assertEquals(DURATION, stalled.hangTicks(), "a flutter with no acceleration hangs for its whole duration");
        assertTrue(stalled.totalLift() < 0.0F, "a flutter that never lifts should not be worth any height");
    }

    @Test
    @DisplayName("a flutter that lasts no time at all is worth nothing")
    void anEmptyFlutterIsWorthNothing() {
        assertEquals(0.0F, FlutterAbility.of(0, DROP, ACCELERATION).totalLift(), EPSILON, "a flutter of no duration");
    }

    @Test
    @DisplayName("numbers written the wrong way round are refused")
    void negativeNumbersAreRefused() {
        var backwards = FlutterAbility.of(-10, -0.5F, -0.1F);

        assertEquals(0, backwards.duration(), "a negative duration");
        assertEquals(0.0F, backwards.drop(), EPSILON, "a negative drop");
        assertEquals(0.0F, backwards.acceleration(), EPSILON, "a negative acceleration");
    }
}
