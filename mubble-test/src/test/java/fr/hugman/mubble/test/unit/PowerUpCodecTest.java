package fr.hugman.mubble.test.unit;

import com.mojang.serialization.JsonOps;
import com.google.gson.JsonParser;
import fr.hugman.mubble.test.unit.support.CodecAssertions;
import fr.hugman.mubble.test.unit.support.TestBootstrap;
import fr.hugman.mubble.world.power_up.PowerUp;
import fr.hugman.mubble.world.power_up.PowerUpBuilder;
import fr.hugman.mubble.world.power_up.PowerUpCosmectics;
import fr.hugman.mubble.world.power_up.ability.ClingAbility;
import fr.hugman.mubble.world.power_up.ability.FlightAbility;
import fr.hugman.mubble.world.power_up.ability.FloatAbility;
import fr.hugman.mubble.world.power_up.ability.FlutterAbility;
import fr.hugman.mubble.world.power_up.ability.PowerUpAbilities;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.block.Blocks;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
    @DisplayName("both halves of a jump held on keep every one of their numbers, and their own field")
    void abilitiesRoundTrip() {
        var decoded = CodecAssertions.assertJsonRoundTrip(PowerUp.DIRECT_CODEC, fullyPopulated()).abilities();

        assertEquals(flutter(), decoded.flutter().orElseThrow(() -> new AssertionError("the flutter was dropped")), "the flutter ability");
        assertEquals(floating(), decoded.floating().orElseThrow(() -> new AssertionError("the float was dropped")), "the float ability");
        assertEquals(flight(), decoded.flight().orElseThrow(() -> new AssertionError("the flight was dropped")), "the flight ability");
        assertEquals(cling(), decoded.cling().orElseThrow(() -> new AssertionError("the cling was dropped")), "the cling ability");
    }

    @Test
    @DisplayName("a power-up without a flight or a cling writes neither of them")
    void absentAbilitiesAreNotWritten() {
        var ops = TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE);
        var powerUp = new PowerUpBuilder().flutter(flutter()).floating(floating()).build();

        var abilities = PowerUp.DIRECT_CODEC.encodeStart(ops, powerUp)
                .getOrThrow(error -> new AssertionError("could not encode a power-up: " + error))
                .getAsJsonObject().getAsJsonObject("abilities");

        assertEquals(Set.of("flutter", "float"), abilities.keySet(), "the abilities written");
    }

    /** The Tanooki form will grant the float on its own, so a power-up has to be able to carry just the one. */
    @Test
    @DisplayName("a power-up can grant the float without the flutter")
    void floatWithoutFlutterRoundTrips() {
        var powerUp = new PowerUpBuilder().floating(floating()).build();

        var decoded = CodecAssertions.assertJsonRoundTrip(PowerUp.DIRECT_CODEC, powerUp).abilities();

        assertTrue(decoded.flutter().isEmpty(), "a power-up granting only a float should have no flutter");
        assertEquals(floating(), decoded.floating().orElseThrow(() -> new AssertionError("the float was dropped")), "the float ability");
    }

    @Test
    @DisplayName("abilities written with nothing but their defaults read back as the defaults")
    void abilityDefaults() {
        var decoded = PowerUp.DIRECT_CODEC.parse(
                        TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE),
                        JsonParser.parseString("""
                                {"abilities": {"flutter": {}, "float": {}, "flight": {}, "cling": {"blocks": []}}}
                                """))
                .getOrThrow(error -> new AssertionError("could not read bare abilities: " + error))
                .abilities();

        assertEquals(
                FlutterAbility.of(FlutterAbility.DEFAULT_DURATION, FlutterAbility.DEFAULT_DROP, FlutterAbility.DEFAULT_ACCELERATION),
                decoded.flutter().orElseThrow(() -> new AssertionError("the flutter was dropped")),
                "a flutter with no field of its own"
        );
        assertEquals(
                FloatAbility.of(FloatAbility.DEFAULT_SPEED, FloatAbility.DEFAULT_FALL_DAMAGE),
                decoded.floating().orElseThrow(() -> new AssertionError("the float was dropped")),
                "a float with no field of its own"
        );
        assertEquals(
                FlightAbility.of(FlightAbility.DEFAULT_GAUGE, FlightAbility.DEFAULT_RESTART, Map.of()),
                decoded.flight().orElseThrow(() -> new AssertionError("the flight was dropped")),
                "a flight with no field of its own"
        );
        assertEquals(
                ClingAbility.of(HolderSet.direct()),
                decoded.cling().orElseThrow(() -> new AssertionError("the cling was dropped")),
                "a cling with no field but its blocks"
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
        return new PowerUp(Optional.empty(), List.of(), Optional.empty(), Optional.empty(), Optional.empty(), PowerUpAbilities.EMPTY, PowerUpCosmectics.EMPTY);
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
                .flutter(flutter())
                .floating(floating())
                .flight(flight())
                .cling(cling())
                .build();
    }

    /** Every number distinct, so that two of them swapped cannot round trip unnoticed. */
    static FlutterAbility flutter() {
        return new FlutterAbility(24, 0.07F, 0.003F, Optional.of(sound(SoundEvents.BAT_LOOP)), Optional.of(ParticleTypes.CHERRY_LEAVES));
    }

    /** Distinct from the flutter above in every field, for the same reason. */
    static FloatAbility floating() {
        return new FloatAbility(0.13F, 0.4F, Optional.of(sound(SoundEvents.BEACON_AMBIENT)), Optional.of(ParticleTypes.COMPOSTER));
    }

    static FlightAbility flight() {
        return new FlightAbility(140, 35, 0.31F, 0.07F, 1.8F, 0.45F, 9, Map.of(Identifier.parse("mubble:test_coin"), 15),
                Optional.of(sound(SoundEvents.BEE_LOOP)), Optional.of(ParticleTypes.FALLING_NECTAR), Optional.of(ParticleTypes.POOF),
                Optional.of(new FlightAbility.Bar(Identifier.parse("mubble:hud/test_background"), Identifier.parse("mubble:hud/test_progress"))));
    }

    static ClingAbility cling() {
        return new ClingAbility(HolderSet.direct(Blocks.HONEY_BLOCK.builtInRegistryHolder()), 0.12F,
                Optional.of(sound(SoundEvents.HONEY_BLOCK_SLIDE)), Optional.of(ParticleTypes.FALLING_HONEY));
    }

    /** Most {@code SoundEvents} constants are bare events, but the mod stores them as holders. */
    static Holder<SoundEvent> sound(SoundEvent event) {
        return BuiltInRegistries.SOUND_EVENT.wrapAsHolder(event);
    }
}
