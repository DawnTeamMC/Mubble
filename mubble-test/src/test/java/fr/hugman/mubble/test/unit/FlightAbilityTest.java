package fr.hugman.mubble.test.unit;

import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import fr.hugman.mubble.test.unit.support.CodecAssertions;
import fr.hugman.mubble.test.unit.support.TestBootstrap;
import fr.hugman.mubble.world.power_up.ability.FlightAbility;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static fr.hugman.mubble.test.unit.PowerUpCodecTest.sound;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Flying on a held jump key: the numbers of a flight, what a tick of it is worth, and how an ability written
 * by a data pack is read and sent.
 */
public class FlightAbilityTest {
    private static final float EPSILON = 1.0E-6F;
    private static final Identifier COIN = Identifier.parse("mubble:test_coin");
    private static final Identifier STOMP = Identifier.parse("mubble:test_stomp");

    @BeforeAll
    static void bootstrapMinecraft() {
        TestBootstrap.bootstrap();
    }

    @Test
    @DisplayName("the speed moves towards the lift by the acceleration, from below and from above")
    void theSpeedApproachesTheLift() {
        var flight = flight();

        assertEquals(-0.4F, flight.speedAfter(-0.5F), EPSILON, "a fall slowed by the acceleration");
        assertEquals(0.4F, flight.speedAfter(0.5F), EPSILON, "a jump slowed down to the lift");
    }

    @Test
    @DisplayName("the speed never overshoots the lift")
    void theSpeedStopsAtTheLift() {
        var flight = flight();

        assertEquals(flight.lift(), flight.speedAfter(flight.lift() - 0.05F), EPSILON, "a speed closer than one step from below");
        assertEquals(flight.lift(), flight.speedAfter(flight.lift() + 0.05F), EPSILON, "a speed closer than one step from above");
        assertEquals(flight.lift(), flight.speedAfter(flight.lift()), EPSILON, "a speed already at the lift");
    }

    @Test
    @DisplayName("a lift reached over a few ticks is held once it is reached")
    void theLiftIsReachedAndHeld() {
        var flight = flight();
        float speed = -0.8F;

        for (int tick = 0; tick < 20; tick++) {
            speed = flight.speedAfter(speed);
        }

        assertEquals(flight.lift(), speed, EPSILON, "the speed after twenty ticks");
    }

    @Test
    @DisplayName("the lift only stutters at the end of the gauge")
    void theSputterIsTheEndOfTheGauge() {
        var flight = flight();
        int sputter = flight.sputter();

        assertFalse(flight.isSputtering(sputter), "the first tick that is not part of the sputter");
        assertTrue(flight.isSputtering(sputter - 1), "the first tick of the sputter");
        assertTrue(flight.isSputtering(0), "the last tick of the gauge");
    }

    @Test
    @DisplayName("a sputtering flight stops lifting every other tick, and never pushes down")
    void theSputterHangsEveryOtherTick() {
        var flight = flight();
        int sputter = flight.sputter();

        assertEquals(flight.lift(), flight.liftFor(flight.lift(), sputter), EPSILON, "a tick outside the sputter");
        assertEquals(0.0F, flight.liftFor(flight.lift(), sputter - 1), EPSILON, "a tick of the sputter that hangs");
        assertEquals(flight.lift(), flight.liftFor(flight.lift(), sputter - 2), EPSILON, "a tick of the sputter that lifts");
        assertEquals(-0.2F, flight.liftFor(-0.2F, sputter - 1), EPSILON, "a hang that would have been a fall stays a fall");
    }

    @Test
    @DisplayName("a flight that never sputters lifts on every tick")
    void noSputterNeverHangs() {
        var flight = new FlightAbility(100, 20, 0.25F, 0.1F, 1.5F, 0.0F, 0, Map.of(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

        for (int left = 0; left < 100; left++) {
            assertEquals(0.25F, flight.liftFor(0.25F, left), EPSILON, "the lift with " + left + " ticks of gauge left");
        }
    }

    @Test
    @DisplayName("a source nobody priced refills nothing, and a negative price is not a drain")
    void refillsAreNeverNegative() {
        var flight = FlightAbility.of(100, 20, Map.of(COIN, 15, STOMP, -40));

        assertEquals(15, flight.refillFor(COIN), "a priced source");
        assertEquals(0, flight.refillFor(STOMP), "a source priced negatively");
        assertEquals(0, flight.refillFor(Identifier.parse("mubble:unknown")), "a source nobody priced");
    }

    @Test
    @DisplayName("numbers written the wrong way round are brought back into range")
    void nonsenseIsClamped() {
        var flight = new FlightAbility(-5, 500, 0.25F, -1.0F, -2.0F, 4.0F, -3, Map.of(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

        assertEquals(1, flight.gauge(), "a gauge that holds nothing");
        assertEquals(1, flight.restart(), "a threshold above the gauge");
        assertEquals(0.0F, flight.acceleration(), EPSILON, "a negative acceleration");
        assertEquals(0.0F, flight.airControl(), EPSILON, "a negative air control");
        assertEquals(1.0F, flight.fallDamage(), EPSILON, "a share of the fall above the whole");
        assertEquals(0, flight.sputter(), "a negative sputter");
    }

    @Test
    @DisplayName("an ability with every field filled survives a JSON round trip")
    void roundTripsThroughJson() {
        CodecAssertions.assertJsonRoundTrip(FlightAbility.CODEC, full());
    }

    @Test
    @DisplayName("an ability with every field filled survives a network round trip")
    void roundTripsThroughTheNetwork() {
        CodecAssertions.assertStreamRoundTrip(FlightAbility.STREAM_CODEC, full());
    }

    @Test
    @DisplayName("the bar sprites and the refills keep their own fields")
    void theBarAndTheRefillsSurvive() {
        var decoded = CodecAssertions.assertStreamRoundTrip(FlightAbility.STREAM_CODEC, full());

        assertEquals(Optional.of(new FlightAbility.Bar(Identifier.parse("mubble:hud/test_background"), Identifier.parse("mubble:hud/test_progress"))), decoded.bar(), "the bar");
        assertEquals(Map.of(COIN, 15, STOMP, 60), decoded.refills(), "the refills");
    }

    @Test
    @DisplayName("an ability written as an empty object reads as the defaults")
    void anEmptyObjectIsTheDefaults() {
        var decoded = FlightAbility.CODEC.parse(
                        TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE),
                        JsonParser.parseString("{}"))
                .getOrThrow(error -> new AssertionError("could not read a bare flight: " + error));

        assertEquals(FlightAbility.of(FlightAbility.DEFAULT_GAUGE, FlightAbility.DEFAULT_RESTART, Map.of()), decoded, "a flight with no field of its own");
        assertEquals(100, decoded.gauge(), "the gauge of five seconds");
    }

    @Test
    @DisplayName("an unknown refill source is accepted, since other mods price their own")
    void anyRefillSourceCanBePriced() {
        var decoded = FlightAbility.CODEC.parse(
                        TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE),
                        JsonParser.parseString("""
                                {"refills": {"another_mod:honey_pot": 7}}
                                """))
                .getOrThrow(error -> new AssertionError("could not read a refill from another mod: " + error));

        assertEquals(7, decoded.refillFor(Identifier.parse("another_mod:honey_pot")), "the price of a source of another mod");
    }

    @Test
    @DisplayName("a refill that is not a whole number is rejected")
    void aRefillMustBeAWholeNumber() {
        CodecAssertions.assertRejects(FlightAbility.CODEC, JsonParser.parseString("""
                {"refills": {"mubble:ground": "a lot"}}
                """));
    }

    private static FlightAbility flight() {
        return FlightAbility.of(FlightAbility.DEFAULT_GAUGE, FlightAbility.DEFAULT_RESTART, Map.of());
    }

    /** Every number distinct from its default and from the others, so that a swap cannot round trip unnoticed. */
    private static FlightAbility full() {
        return new FlightAbility(
                140,
                35,
                0.31F,
                0.07F,
                1.8F,
                0.45F,
                9,
                Map.of(COIN, 15, STOMP, 60),
                Optional.of(sound(SoundEvents.BEE_LOOP)),
                Optional.of(ParticleTypes.FALLING_NECTAR),
                Optional.of(ParticleTypes.POOF),
                Optional.of(new FlightAbility.Bar(Identifier.parse("mubble:hud/test_background"), Identifier.parse("mubble:hud/test_progress")))
        );
    }
}
