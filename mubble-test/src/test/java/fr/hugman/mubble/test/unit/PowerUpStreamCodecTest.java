package fr.hugman.mubble.test.unit;

import fr.hugman.mubble.test.unit.support.CodecAssertions;
import fr.hugman.mubble.test.unit.support.TestBootstrap;
import fr.hugman.mubble.world.power_up.PowerUp;
import fr.hugman.mubble.world.power_up.PowerUpCosmectics;
import net.minecraft.sounds.SoundEvents;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static fr.hugman.mubble.test.unit.PowerUpCodecTest.cosmetics;
import static fr.hugman.mubble.test.unit.PowerUpCodecTest.empty;
import static fr.hugman.mubble.test.unit.PowerUpCodecTest.fullyPopulated;
import static fr.hugman.mubble.test.unit.PowerUpCodecTest.sound;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The other half of {@link PowerUpCodecTest}: what travels to the client. A stream codec is written
 * as two parallel lists, the fields and their getters, so a pair listed out of order swaps two values
 * without any compiler complaint — which is exactly what happened to the loose and refill sounds.
 * <p>
 * Every value below is therefore distinct: two fields holding the same thing would hide a swap.
 * <p>
 * Power-ups carrying an action are covered by the game tests instead: an action is a holder of a
 * dynamic registry, which only exists once a server is running.
 */
public class PowerUpStreamCodecTest {
    @BeforeAll
    static void bootstrapMinecraft() {
        TestBootstrap.bootstrap();
    }

    @Test
    @DisplayName("a power-up survives a network round trip")
    void powerUpRoundTrips() {
        CodecAssertions.assertStreamRoundTrip(PowerUp.DIRECT_STREAM_CODEC, fullyPopulated());
    }

    @Test
    @DisplayName("a power-up with every field left out survives a network round trip")
    void emptyPowerUpRoundTrips() {
        CodecAssertions.assertStreamRoundTrip(PowerUp.DIRECT_STREAM_CODEC, empty());
    }

    @Test
    @DisplayName("no two cosmetic sounds swap places on the way")
    void cosmeticSoundsDoNotSwap() {
        var decoded = CodecAssertions.assertStreamRoundTrip(PowerUpCosmectics.STREAM_CODEC, cosmetics());

        assertEquals(sound(SoundEvents.AMETHYST_BLOCK_CHIME), decoded.obtainSound().orElseThrow(), "obtain sound");
        assertEquals(sound(SoundEvents.BEACON_AMBIENT), decoded.emitSound().orElseThrow(), "emit sound");
        assertEquals(sound(SoundEvents.ANVIL_LAND), decoded.looseSound().orElseThrow(), "loose sound");
        assertEquals(sound(SoundEvents.BELL_BLOCK), decoded.refillSound().orElseThrow(), "refill sound");
    }

    @Test
    @DisplayName("the network form and the data pack form agree on what a power-up is")
    void bothFormsAgree() {
        var throughJson = CodecAssertions.assertJsonRoundTrip(PowerUp.DIRECT_CODEC, fullyPopulated());
        var throughNetwork = CodecAssertions.assertStreamRoundTrip(PowerUp.DIRECT_STREAM_CODEC, fullyPopulated());

        assertEquals(throughJson, throughNetwork, "a power-up read from a data pack differs from the same one read off the network");
    }
}
