package fr.hugman.mubble.test.unit;

import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import fr.hugman.mubble.test.unit.support.CodecAssertions;
import fr.hugman.mubble.test.unit.support.TestBootstrap;
import fr.hugman.mubble.world.power_up.ability.FloatAbility;
import net.minecraft.core.particles.ParticleTypes;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The descent half of a jump held on, and the only half the Tanooki form will take: what a tick spent
 * coming down slowly is worth, and how much of the fall it writes off.
 */
public class FloatAbilityTest {
    @org.junit.jupiter.api.BeforeAll
    static void bootstrapMinecraft() {
        TestBootstrap.bootstrap();
    }

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

    @Test
    @DisplayName("the air control survives a JSON and a network round trip")
    void airControlRoundTrips() {
        var floating = new FloatAbility(SPEED, FALL_DAMAGE, Optional.empty(), Optional.of(ParticleTypes.CLOUD), Optional.of(new FloatAbility.AirControl(0.07F, 0.4F)));

        CodecAssertions.assertJsonRoundTrip(FloatAbility.CODEC, floating);
        CodecAssertions.assertStreamRoundTrip(FloatAbility.STREAM_CODEC, floating);
    }

    @Test
    @DisplayName("a float written before the air control existed reads back without one, and writes back the same")
    void existingJsonIsUntouched() {
        var ops = TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE);
        var json = JsonParser.parseString("{\"speed\": 0.1, \"fall_damage\": 0.25}");
        var decoded = FloatAbility.CODEC.parse(ops, json).getOrThrow(AssertionError::new);

        assertEquals(FloatAbility.of(0.1F, 0.25F), decoded);
        assertEquals(json.toString(), FloatAbility.CODEC.encodeStart(ops, decoded).getOrThrow(AssertionError::new).toString(), "no air control field is written for a float without one");
    }

    @Test
    @DisplayName("an air control with no field of its own reads back as the defaults")
    void airControlDefaults() {
        var ops = TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE);
        var decoded = FloatAbility.CODEC.parse(ops, JsonParser.parseString("{\"air_control\": {}}")).getOrThrow(AssertionError::new);

        assertEquals(new FloatAbility.AirControl(FloatAbility.AirControl.DEFAULT_ACCELERATION, FloatAbility.AirControl.DEFAULT_TOP_SPEED), decoded.airControl().orElseThrow());
    }
}
