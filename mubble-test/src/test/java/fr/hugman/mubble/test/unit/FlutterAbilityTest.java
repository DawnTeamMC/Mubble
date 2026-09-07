package fr.hugman.mubble.test.unit;

import fr.hugman.mubble.world.power_up.ability.FlutterAbility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The shape of the climb half of a jump held on: how much lift each of its ticks is worth.
 * <p>
 * A flutter builds rather than holding one speed, which is what tells it apart from a second jump, so the
 * curve behind it is worth pinning down on its own, away from a level and a player.
 */
public class FlutterAbilityTest {
    private static final int DURATION = 20;
    private static final float SPEED = 0.05F;
    private static final float ACCELERATION = 0.005F;

    private static final FlutterAbility FLUTTER = FlutterAbility.of(DURATION, SPEED, ACCELERATION);
    private static final float EPSILON = 1.0E-6F;

    @Test
    @DisplayName("the flutter opens on its own speed, so that the jump key is never ignored")
    void theFirstTickAlreadyLifts() {
        assertEquals(SPEED, FLUTTER.liftAt(0), EPSILON, "the very first tick of a flutter");
        assertTrue(FLUTTER.liftAt(0) > 0.0F, "the very first tick should already carry the player");
    }

    @Test
    @DisplayName("every tick lifts a little harder than the one before it")
    void theLiftKeepsBuilding() {
        for (int tick = 1; tick < DURATION; tick++) {
            assertEquals(ACCELERATION, FLUTTER.liftAt(tick) - FLUTTER.liftAt(tick - 1), EPSILON,
                    "the speed tick " + tick + " added to the one before it");
        }
    }

    /** A flutter that plateaued would carry its holder the same way a rising platform does. */
    @Test
    @DisplayName("the lift never settles on a top speed")
    void theLiftNeverPlateaus() {
        assertTrue(FLUTTER.liftAt(DURATION - 1) > FLUTTER.liftAt(DURATION - 2),
                "the last tick of a flutter should still be lifting harder than the one before it");
    }

    @Test
    @DisplayName("a flutter without acceleration holds one speed throughout")
    void noAccelerationMeansOneSpeed() {
        var steady = FlutterAbility.of(DURATION, SPEED, 0.0F);

        assertEquals(SPEED, steady.liftAt(0), EPSILON, "the first tick of a flutter with no acceleration");
        assertEquals(SPEED, steady.liftAt(DURATION - 1), EPSILON, "the last tick of a flutter with no acceleration");
        assertEquals(SPEED * DURATION, steady.totalLift(), EPSILON, "the whole of a flutter with no acceleration");
    }

    @Test
    @DisplayName("a whole flutter is worth its opening speed plus everything the acceleration added")
    void theAccelerationIsWorthTheClimb() {
        // The acceleration is added once on the second tick, twice on the third, and so on.
        float ticks = DURATION;
        float expected = SPEED * ticks + ACCELERATION * (ticks - 1.0F) * ticks / 2.0F;

        assertEquals(expected, FLUTTER.totalLift(), EPSILON, "the height a whole flutter is worth");
        assertTrue(FLUTTER.totalLift() > SPEED * DURATION, "the acceleration should be worth height of its own");
    }

    @Test
    @DisplayName("a flutter that lasts no time at all lifts nothing")
    void anEmptyFlutterLiftsNothing() {
        assertEquals(0.0F, FlutterAbility.of(0, SPEED, ACCELERATION).totalLift(), EPSILON, "a flutter of no duration");
    }

    @Test
    @DisplayName("numbers that would push the holder down are refused")
    void negativeNumbersAreRefused() {
        var backwards = FlutterAbility.of(-10, -0.5F, -0.1F);

        assertEquals(0, backwards.duration(), "a negative duration");
        assertEquals(0.0F, backwards.speed(), EPSILON, "a negative speed");
        assertEquals(0.0F, backwards.acceleration(), EPSILON, "a negative acceleration");
    }
}
