package fr.hugman.mubble.test.unit;

import fr.hugman.mubble.world.power_up.ability.FlutterAbility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The shape of a flutter: how much lift each of its ticks is worth.
 * <p>
 * It is the ramp that tells a flutter apart from a second jump, so the curve behind it is worth
 * pinning down on its own, away from a level and a player.
 */
public class FlutterAbilityTest {
    private static final int DURATION = 20;
    private static final int RAMP = 5;
    private static final float STRENGTH = 0.12F;

    private static final FlutterAbility FLUTTER = FlutterAbility.of(DURATION, RAMP, STRENGTH);
    private static final float EPSILON = 1.0E-6F;

    @Test
    @DisplayName("the first tick already lifts, so that the jump key is never ignored")
    void theFirstTickAlreadyLifts() {
        assertTrue(FLUTTER.liftAt(0) > 0.0F, "the very first tick of a flutter should already carry the player");
    }

    @Test
    @DisplayName("the lift climbs over the ramp instead of snapping to full strength")
    void theLiftClimbsOverTheRamp() {
        float previous = 0.0F;
        for (int tick = 0; tick < RAMP; tick++) {
            float lift = FLUTTER.liftAt(tick);
            assertTrue(lift > previous, "tick " + tick + " should lift more than the one before it");
            assertTrue(lift <= STRENGTH + EPSILON, "no tick of the ramp should lift more than the full strength");
            previous = lift;
        }
    }

    @Test
    @DisplayName("the lift reaches its full strength at the end of the ramp, and stays there")
    void theLiftPlateausAfterTheRamp() {
        assertEquals(STRENGTH, FLUTTER.liftAt(RAMP - 1), EPSILON, "the last tick of the ramp");
        assertEquals(STRENGTH, FLUTTER.liftAt(RAMP), EPSILON, "the tick right after the ramp");
        assertEquals(STRENGTH, FLUTTER.liftAt(DURATION - 1), EPSILON, "the last tick of the flutter");
    }

    @Test
    @DisplayName("a flutter without a ramp lifts at full strength from the start")
    void noRampMeansNoClimb() {
        var abrupt = FlutterAbility.of(DURATION, 0, STRENGTH);

        assertEquals(STRENGTH, abrupt.liftAt(0), EPSILON, "the first tick of a flutter with no ramp");
        assertEquals(STRENGTH, abrupt.liftAt(DURATION - 1), EPSILON, "the last tick of a flutter with no ramp");
    }

    @Test
    @DisplayName("a whole flutter is worth less than its strength held for its whole duration")
    void theRampCostsSomeHeight() {
        float held = STRENGTH * DURATION;

        assertTrue(FLUTTER.totalLift() < held, "the ramp should cost the flutter some of its height");
        assertTrue(FLUTTER.totalLift() > held * 0.5F, "the ramp should not cost the flutter most of its height either");
        assertEquals(held, FlutterAbility.of(DURATION, 0, STRENGTH).totalLift(), EPSILON,
                "a flutter with no ramp should be worth its strength on every one of its ticks");
    }

    @Test
    @DisplayName("a flutter that lasts no time at all lifts nothing")
    void anEmptyFlutterLiftsNothing() {
        assertEquals(0.0F, FlutterAbility.of(0, RAMP, STRENGTH).totalLift(), EPSILON, "a flutter of no duration");
    }

    @Test
    @DisplayName("numbers that would push the holder down are refused")
    void negativeNumbersAreRefused() {
        var backwards = FlutterAbility.of(-10, -3, -0.5F);

        assertEquals(0, backwards.duration(), "a negative duration");
        assertEquals(0, backwards.ramp(), "a negative ramp");
        assertEquals(0.0F, backwards.strength(), EPSILON, "a negative strength");
    }
}
