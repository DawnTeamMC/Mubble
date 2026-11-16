package fr.hugman.mubble.test.unit;

import fr.hugman.mubble.test.unit.support.CodecAssertions;
import fr.hugman.mubble.test.unit.support.TestBootstrap;
import fr.hugman.mubble.world.power_up.ability.FloatAbility;
import fr.hugman.mubble.world.power_up.ability.LaunchAbility;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LaunchAbilityTest {
    private static final double DEFAULT_GRAVITY = 0.08D;

    @BeforeAll
    static void bootstrapMinecraft() {
        TestBootstrap.bootstrap();
    }

    static LaunchAbility launch() {
        return new LaunchAbility(9, 5.5F,
                Optional.of(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.BREEZE_JUMP)),
                Optional.of(ParticleTypes.CLOUD),
                Optional.of(FloatAbility.of(0.12F, 0.3F)));
    }

    @Test
    @DisplayName("a launch survives a JSON and a network round trip")
    void roundTrips() {
        CodecAssertions.assertJsonRoundTrip(LaunchAbility.CODEC, launch());
        CodecAssertions.assertStreamRoundTrip(LaunchAbility.STREAM_CODEC, launch());
    }

    @Test
    @DisplayName("a launch with no field of its own reads back as the defaults")
    void defaults() {
        var ops = TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE);
        var decoded = LaunchAbility.CODEC.parse(ops, JsonParser.parseString("{}")).getOrThrow(AssertionError::new);

        assertEquals(LaunchAbility.of(LaunchAbility.DEFAULT_DOUBLE_TAP_WINDOW, LaunchAbility.DEFAULT_HEIGHT, Optional.empty()), decoded);
        assertEquals(7, decoded.doubleTapWindow(), "vanilla's own double tap window");
        assertEquals(7.0F, decoded.height(), "the launch height of the issue");
    }

    @Test
    @DisplayName("the take-off speed reaches the launch height under default gravity")
    void reachesItsHeight() {
        var launch = LaunchAbility.of(7, 7.0F, Optional.empty());
        double apex = LaunchAbility.apex(launch.takeOffSpeed(DEFAULT_GRAVITY), DEFAULT_GRAVITY);

        assertEquals(7.0D, apex, 1.0E-3D, "the height a default launch reaches");
    }

    @Test
    @DisplayName("a heavier holder needs a faster take-off to reach the same height")
    void scalesWithGravity() {
        var launch = LaunchAbility.of(7, 7.0F, Optional.empty());
        double light = launch.takeOffSpeed(DEFAULT_GRAVITY * 0.5D);
        double heavy = launch.takeOffSpeed(DEFAULT_GRAVITY * 2.0D);

        assertTrue(heavy > launch.takeOffSpeed(DEFAULT_GRAVITY), "more gravity should take a faster take-off");
        assertTrue(light < launch.takeOffSpeed(DEFAULT_GRAVITY), "less gravity should take a slower take-off");
        assertEquals(7.0D, LaunchAbility.apex(heavy, DEFAULT_GRAVITY * 2.0D), 1.0E-3D, "the height under heavy gravity");
        assertEquals(7.0D, LaunchAbility.apex(light, DEFAULT_GRAVITY * 0.5D), 1.0E-3D, "the height under light gravity");
    }

    @Test
    @DisplayName("a launch with no height does not move the holder")
    void noHeightNoSpeed() {
        assertEquals(0.0D, LaunchAbility.of(7, -3.0F, Optional.empty()).takeOffSpeed(DEFAULT_GRAVITY), 1.0E-6D);
    }
}
