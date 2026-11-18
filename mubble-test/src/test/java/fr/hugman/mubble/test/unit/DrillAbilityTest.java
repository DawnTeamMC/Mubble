package fr.hugman.mubble.test.unit;

import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import fr.hugman.mubble.test.unit.support.CodecAssertions;
import fr.hugman.mubble.test.unit.support.TestBootstrap;
import fr.hugman.mubble.world.power_up.ability.DrillAbility;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DrillAbilityTest {
    @BeforeAll
    static void bootstrapMinecraft() {
        TestBootstrap.bootstrap();
    }

    static DrillAbility drill() {
        return new DrillAbility(1.3F, 0.2F, 0.1F,
                Optional.of(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.MINECART_RIDING)),
                Optional.of(ParticleTypes.CRIT),
                Optional.of(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.ANVIL_LAND)));
    }

    @Test
    @DisplayName("a drill survives a JSON and a network round trip")
    void roundTrips() {
        CodecAssertions.assertJsonRoundTrip(DrillAbility.CODEC, drill());
        CodecAssertions.assertStreamRoundTrip(DrillAbility.STREAM_CODEC, drill());
    }

    @Test
    @DisplayName("a drill with no field of its own reads back as the defaults, with no fall damage")
    void defaults() {
        var ops = TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE);
        var decoded = DrillAbility.CODEC.parse(ops, JsonParser.parseString("{}")).getOrThrow(AssertionError::new);

        assertEquals(DrillAbility.of(DrillAbility.DEFAULT_SPEED, DrillAbility.DEFAULT_CONTROL, DrillAbility.DEFAULT_FALL_DAMAGE), decoded);
        assertEquals(0.0F, decoded.fallDamage(), "a drill takes no fall damage by default");
        assertEquals(decoded.speed(), decoded.fallForgivenPerTick(), 1.0E-6F, "every drilled tick is forgiven by default");
    }

    @Test
    @DisplayName("shares outside of zero to one and negative speeds are brought back")
    void clamps() {
        var drill = DrillAbility.of(-1.0F, 3.0F, -2.0F);
        assertEquals(0.0F, drill.speed());
        assertEquals(1.0F, drill.control());
        assertEquals(0.0F, drill.fallDamage());
    }
}
