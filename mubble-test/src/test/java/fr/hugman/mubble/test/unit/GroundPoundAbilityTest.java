package fr.hugman.mubble.test.unit;

import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import fr.hugman.mubble.test.unit.support.CodecAssertions;
import fr.hugman.mubble.test.unit.support.TestBootstrap;
import fr.hugman.mubble.world.power_up.ability.GroundPoundAbility;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EntityTypes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GroundPoundAbilityTest {
    @BeforeAll
    static void bootstrapMinecraft() {
        TestBootstrap.bootstrap();
    }

    static GroundPoundAbility ability() {
        return new GroundPoundAbility(4, 0.9F, 2.0F, 2.5F,
                Optional.of(HolderSet.direct(EntityTypes.PIG.builtInRegistryHolder(), EntityTypes.COW.builtInRegistryHolder())),
                3.0F, 25.0F,
                Optional.of(ParticleTypes.CLOUD),
                Optional.of(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.ANVIL_LAND)),
                Optional.of(new GroundPoundAbility.Statue(100, false, DamageTypeTags.IS_FIRE,
                        Optional.of(ParticleTypes.POOF), Optional.of(ParticleTypes.CRIT),
                        Optional.of(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.STONE_PLACE)),
                        Optional.of(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.STONE_BREAK)))));
    }

    @Test
    @DisplayName("a ground pound survives a JSON and a network round trip")
    void roundTrips() {
        CodecAssertions.assertJsonRoundTrip(GroundPoundAbility.CODEC, ability());
        CodecAssertions.assertStreamRoundTrip(GroundPoundAbility.STREAM_CODEC, ability());
    }

    @Test
    @DisplayName("a ground pound with no field of its own reads back as the defaults, with no statue")
    void defaults() {
        var ops = TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE);
        var decoded = GroundPoundAbility.CODEC.parse(ops, JsonParser.parseString("{}")).getOrThrow(AssertionError::new);

        assertEquals(GroundPoundAbility.of(Optional.empty()), decoded);
        assertTrue(decoded.kills().isEmpty(), "a pound defeats nothing outright unless told to");
    }

    @Test
    @DisplayName("a statue with no field of its own lasts twelve seconds, is invulnerable and lets through what bypasses invulnerability")
    void statueDefaults() {
        var ops = TestBootstrap.registries().createSerializationContext(JsonOps.INSTANCE);
        var decoded = GroundPoundAbility.CODEC.parse(ops, JsonParser.parseString("{\"statue\": {}}")).getOrThrow(AssertionError::new);

        assertEquals(GroundPoundAbility.of(Optional.of(GroundPoundAbility.Statue.of(240))), decoded);
        assertEquals(DamageTypeTags.BYPASSES_INVULNERABILITY, decoded.statue().orElseThrow().vulnerableTo());
    }

    @Test
    @DisplayName("the damage is so much per block fallen, up to a cap")
    void damageIsCapped() {
        var pound = GroundPoundAbility.of(Optional.empty());

        assertEquals(0.0F, pound.damageFor(0.0D), "no fall, no damage");
        assertEquals(6.0F, pound.damageFor(3.4D), "whole points per block");
        assertEquals(pound.maxDamage(), pound.damageFor(1000.0D), "the cap");
    }

    @Test
    @DisplayName("negative numbers are brought back to zero")
    void clamps() {
        var pound = new GroundPoundAbility(-1, -1.0F, -1.0F, -1.0F, Optional.empty(), -1.0F, -1.0F, Optional.empty(), Optional.empty(), Optional.empty());

        assertEquals(0, pound.hangTicks());
        assertEquals(0.0F, pound.dropSpeed());
        assertEquals(0.0F, pound.reach());
        assertEquals(0.0F, pound.maxDamage());
    }
}
