package fr.hugman.mubble.test.unit;

import fr.hugman.mubble.world.power_up.ability.FloatAbility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The descent half of a jump held on, and the only half the Tanooki form will take: what a tick spent
 * coming down slowly is worth, and how much of the fall it writes off.
 */
public class FloatAbilityTest {
    private static final float SPEED = 0.1F;
    private static final float FALL_DAMAGE = 0.25F;
    private static final float EPSILON = 1.0E-6F;

    @Test
    @DisplayName("a floated tick only counts for its share of the fall")
    void aFloatedTickIsMostlyForgiven() {
        var floating = FloatAbility.of(SPEED, FALL_DAMAGE);

        assertEquals(SPEED * (1.0F - FALL_DAMAGE), floating.fallForgivenPerTick(), EPSILON,
                "the blocks a tick of floating writes off");
        assertTrue(floating.fallForgivenPerTick() < SPEED,
                "a float that forgave the whole tick would be free of fall damage, not reduced");
    }

    @Test
    @DisplayName("a float that counts for nothing forgives the whole descent")
    void noFallDamageForgivesEverything() {
        assertEquals(SPEED, FloatAbility.of(SPEED, 0.0F).fallForgivenPerTick(), EPSILON, "a float taking no damage at all");
    }

    @Test
    @DisplayName("a float that counts in full forgives nothing")
    void fullFallDamageForgivesNothing() {
        assertEquals(0.0F, FloatAbility.of(SPEED, 1.0F).fallForgivenPerTick(), EPSILON, "a float taking the whole fall");
    }

    @Test
    @DisplayName("a share of the fall outside of zero to one is brought back into it")
    void theShareOfTheFallIsAShare() {
        assertEquals(0.0F, FloatAbility.of(SPEED, -2.0F).fallDamage(), EPSILON, "a negative share");
        assertEquals(1.0F, FloatAbility.of(SPEED, 4.0F).fallDamage(), EPSILON, "a share above the whole fall");
    }

    /** A negative speed would send a floating holder upwards, which is the flutter's job and not this one's. */
    @Test
    @DisplayName("a speed that would lift the holder is refused")
    void negativeSpeedIsRefused() {
        assertEquals(0.0F, FloatAbility.of(-0.5F, FALL_DAMAGE).speed(), EPSILON, "a negative speed");
    }
}
