package fr.hugman.mubble.test.unit;

import com.mojang.serialization.JsonOps;
import com.google.gson.JsonParser;
import fr.hugman.mubble.test.unit.support.CodecAssertions;
import fr.hugman.mubble.test.unit.support.TestBootstrap;
import fr.hugman.mubble.world.power_up.PowerUp;
import fr.hugman.mubble.world.power_up.PowerUpBuilder;
import fr.hugman.mubble.world.power_up.PowerUpCosmectics;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Serialisation of {@link PowerUp} to and from the data pack. Every power-up of the mod goes through
 * this codec, and a field dropped or swapped here stays invisible until someone notices in game.
 */
public class PowerUpCodecTest {
    @BeforeAll
    static void bootstrapMinecraft() {
        TestBootstrap.bootstrap();
    }

    @Test
    @DisplayName("a power-up with every field filled survives a JSON round trip")
    void fullPowerUpRoundTrips() {
        CodecAssertions.assertJsonRoundTrip(PowerUp.DIRECT_CODEC, fullyPopulated());
    }

    @Test
    @DisplayName("a power-up with every field left out survives a JSON round trip")
    void emptyPowerUpRoundTrips() {
        CodecAssertions.assertJsonRoundTrip(PowerUp.DIRECT_CODEC, empty());
    }

    @Test
    @DisplayName("an empty power-up writes nothing but an empty object")
    void emptyPowerUpWritesNothing() {
        var ops = TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE);

        var encoded = PowerUp.DIRECT_CODEC.encodeStart(ops, empty())
                .getOrThrow(error -> new AssertionError("could not encode an empty power-up: " + error));

        assertTrue(encoded.isJsonObject(), "a power-up should encode to an object");
        assertTrue(encoded.getAsJsonObject().isEmpty(), () -> "an empty power-up should write no field at all, wrote " + encoded);
    }

    @Test
    @DisplayName("every cosmetic sound keeps its own field")
    void cosmeticSoundsKeepTheirOwnField() {
        // Four distinct sounds: two of them swapped would still round trip if they shared a value.
        var decoded = CodecAssertions.assertJsonRoundTrip(PowerUpCosmectics.CODEC, cosmetics());

        assertEquals(sound(SoundEvents.AMETHYST_BLOCK_CHIME), decoded.obtainSound().orElseThrow(), "obtain sound");
        assertEquals(sound(SoundEvents.BEACON_AMBIENT), decoded.emitSound().orElseThrow(), "emit sound");
        assertEquals(sound(SoundEvents.ANVIL_LAND), decoded.looseSound().orElseThrow(), "loose sound");
        assertEquals(sound(SoundEvents.BELL_BLOCK), decoded.refillSound().orElseThrow(), "refill sound");
    }

    @Test
    @DisplayName("attribute modifiers keep their amount and their operation")
    void attributeModifiersRoundTrip() {
        var powerUp = new PowerUpBuilder()
                .attributesModifier(Attributes.MAX_HEALTH, 6.0D, AttributeModifier.Operation.ADD_VALUE)
                .attributesModifier(Attributes.MOVEMENT_SPEED, -0.25D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .build();

        var decoded = CodecAssertions.assertJsonRoundTrip(PowerUp.DIRECT_CODEC, powerUp);
        var modifiers = decoded.attributesModifiers().orElseThrow(() -> new AssertionError("the modifiers were dropped"));

        assertEquals(2, modifiers.size(), "modifier count");
        assertEquals(6.0D, modifiers.getFirst().modifier().amount(), "the amount of the first modifier");
        assertEquals(AttributeModifier.Operation.ADD_MULTIPLIED_BASE, modifiers.get(1).modifier().operation(), "the operation of the second modifier");
    }

    @Test
    @DisplayName("the description keeps its lines, in order")
    void descriptionRoundTrips() {
        var decoded = CodecAssertions.assertJsonRoundTrip(PowerUp.DIRECT_CODEC, fullyPopulated());

        assertEquals(
                List.of(
                        Component.translatable("power_up.mubble.test.description.first"),
                        Component.translatable("power_up.mubble.test.description.second")
                ),
                decoded.description(),
                "the description lines"
        );
    }

    @Test
    @DisplayName("an unknown action type is rejected instead of being ignored")
    void unknownActionTypeIsRejected() {
        CodecAssertions.assertRejects(PowerUp.DIRECT_CODEC, JsonParser.parseString("""
                {"action": {"type": "mubble:not_a_real_action"}}
                """));
    }

    @Test
    @DisplayName("an unknown attribute is rejected instead of being ignored")
    void unknownAttributeIsRejected() {
        CodecAssertions.assertRejects(PowerUp.DIRECT_CODEC, JsonParser.parseString("""
                {"attribute_modifiers": [{"type": "minecraft:not_a_real_attribute", "amount": 1.0, "id": "mubble:test", "operation": "add_value"}]}
                """));
    }

    static PowerUp empty() {
        return new PowerUp(Optional.empty(), List.of(), Optional.empty(), Optional.empty(), Optional.empty(), PowerUpCosmectics.EMPTY);
    }

    static PowerUpCosmectics cosmetics() {
        return new PowerUpCosmectics(
                Optional.of(ParticleTypes.FLAME),
                Optional.of(sound(SoundEvents.AMETHYST_BLOCK_CHIME)),
                Optional.of(sound(SoundEvents.BEACON_AMBIENT)),
                Optional.of(sound(SoundEvents.ANVIL_LAND)),
                Optional.of(sound(SoundEvents.BELL_BLOCK)),
                Optional.of(Identifier.parse("mubble:entity/power_up/humanoid/test")),
                true
        );
    }

    static PowerUp fullyPopulated() {
        return new PowerUpBuilder()
                .name(Component.translatable("power_up.mubble.test"))
                .description(Component.translatable("power_up.mubble.test.description.first"))
                .description(Component.translatable("power_up.mubble.test.description.second"))
                .spriteId(Identifier.parse("mubble:power_up/test"))
                .attributesModifier(Attributes.MAX_HEALTH, 4.0D, AttributeModifier.Operation.ADD_VALUE)
                .obtainSound(sound(SoundEvents.AMETHYST_BLOCK_CHIME))
                .emitSound(sound(SoundEvents.BEACON_AMBIENT))
                .looseSound(sound(SoundEvents.ANVIL_LAND))
                .refillSound(sound(SoundEvents.BELL_BLOCK))
                .particle(ParticleTypes.FLAME)
                .humanoidOverlay(Identifier.parse("mubble:entity/power_up/humanoid/test"))
                .emissiveOverlay()
                .build();
    }

    /** Most {@code SoundEvents} constants are bare events, but the mod stores them as holders. */
    static Holder<SoundEvent> sound(SoundEvent event) {
        return BuiltInRegistries.SOUND_EVENT.wrapAsHolder(event);
    }
}
