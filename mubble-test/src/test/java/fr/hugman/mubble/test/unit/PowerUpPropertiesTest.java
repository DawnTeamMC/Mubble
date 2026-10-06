package fr.hugman.mubble.test.unit;

import com.mojang.serialization.JsonOps;
import fr.hugman.mubble.test.unit.support.TestBootstrap;
import fr.hugman.mubble.world.power_up.PowerUpProperties;
import io.netty.buffer.Unpooled;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Charge bookkeeping of {@link PowerUpProperties}, the state a power-up holder carries between two
 * triggers. It is plain state handling, so it can be checked without a level.
 */
public class PowerUpPropertiesTest {
    @Test
    @DisplayName("a fresh power-up is fully charged")
    void freshPropertiesAreAtMax() {
        var properties = new PowerUpProperties(PowerUpProperties.ChargeCounting.ONLY_DECREASE, 2);

        assertEquals(2, properties.getChargeCount());
        assertTrue(properties.isAtMax());
    }

    @Test
    @DisplayName("spending a charge takes the power-up away from its maximum")
    void spendingAChargeLeavesTheMaximum() {
        var properties = new PowerUpProperties(PowerUpProperties.ChargeCounting.ONLY_DECREASE, 2);

        properties.useCharge();

        assertEquals(1, properties.getChargeCount());
        assertFalse(properties.isAtMax());
    }

    @Test
    @DisplayName("a running cooldown keeps the power-up away from its maximum")
    void runningCooldownLeavesTheMaximum() {
        var properties = new PowerUpProperties(
                PowerUpProperties.ChargeCounting.ONLY_DECREASE, 1, 0, 2, 1, List.of());

        assertFalse(properties.isAtMax());

        properties.tick();
        assertFalse(properties.isAtMax());

        properties.tick();
        assertTrue(properties.isAtMax());
    }

    @Test
    @DisplayName("COOLDOWN_RECHARGE gives a charge back once the cooldown runs out")
    void cooldownRechargeGivesAChargeBack() {
        var properties = new PowerUpProperties(PowerUpProperties.ChargeCounting.COOLDOWN_RECHARGE, 1, 2);
        // Spending the charge is what starts the countdown, so this is the whole trigger in one call.
        properties.useCharge();

        properties.tick();
        assertEquals(0, properties.getChargeCount());

        properties.tick();
        assertEquals(1, properties.getChargeCount());
        assertTrue(properties.isAtMax());
    }

    @Test
    @DisplayName("FROM_ACTIVE_ENTITIES counts the charges left from the entities still alive")
    void activeEntitiesDriveTheChargeCount() {
        var properties = new PowerUpProperties(PowerUpProperties.ChargeCounting.FROM_ACTIVE_ENTITIES, 2);
        var firstProjectile = UUID.randomUUID();

        // What a trigger does: spend the charge, then tie it to the entity that was sent out.
        properties.useCharge();
        properties.trackEntity(firstProjectile);
        properties.useCharge();
        properties.trackEntity(UUID.randomUUID());
        properties.tick();
        assertEquals(0, properties.getChargeCount());

        // The projectile died, so its charge comes back.
        properties.removeEntity(firstProjectile);
        properties.tick();
        assertEquals(1, properties.getChargeCount());
    }

    @Test
    @DisplayName("the dirty flag is raised on change and cleared by the first read")
    void dirtyFlagIsConsumedOnce() {
        var properties = new PowerUpProperties(PowerUpProperties.ChargeCounting.ONLY_DECREASE, 1);
        assertFalse(properties.checkDirty());

        properties.useCharge();

        assertTrue(properties.checkDirty());
        assertFalse(properties.checkDirty());
    }

    @Nested
    @DisplayName("codec")
    class Codec {
        @Test
        @DisplayName("keeps every field through a serialisation round trip")
        void roundTripsThroughJson() {
            var properties = new PowerUpProperties(
                    PowerUpProperties.ChargeCounting.FROM_ACTIVE_ENTITIES,
                    3,
                    5,
                    7,
                    2,
                    List.of(UUID.fromString("f7e5b26e-9e4d-4b6f-9f7c-6c1f0f8f2a11"))
            );

            var encoded = PowerUpProperties.CODEC.encodeStart(JsonOps.INSTANCE, properties).getOrThrow();
            var decoded = PowerUpProperties.CODEC.parse(JsonOps.INSTANCE, encoded).getOrThrow();

            // PowerUpProperties is mutable state without an equals(), so the encoded form is the reference.
            assertEquals(encoded, PowerUpProperties.CODEC.encodeStart(JsonOps.INSTANCE, decoded).getOrThrow());
            assertEquals(properties.getChargeCount(), decoded.getChargeCount());
            assertEquals(properties.chargeCounting, decoded.chargeCounting);
            assertEquals(properties.maxCharges, decoded.maxCharges);
            assertEquals(properties.interval, decoded.interval);
        }

        @Test
        @DisplayName("keeps every field through a network round trip")
        void roundTripsThroughTheNetwork() {
            // Every number is distinct: a cooldown and a charge count swapped would survive equal values.
            var properties = new PowerUpProperties(
                    PowerUpProperties.ChargeCounting.COOLDOWN_RECHARGE,
                    9,
                    6,
                    4,
                    2,
                    List.of(UUID.fromString("f7e5b26e-9e4d-4b6f-9f7c-6c1f0f8f2a11"))
            );

            var buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), TestBootstrap.registries());
            PowerUpProperties.STREAM_CODEC.encode(buf, properties);
            var decoded = PowerUpProperties.STREAM_CODEC.decode(buf);

            assertEquals(0, buf.readableBytes(), "the decoder left unread bytes behind");
            assertEquals(properties.chargeCounting, decoded.chargeCounting, "charge counting");
            assertEquals(properties.maxCharges, decoded.maxCharges, "max charges");
            assertEquals(properties.interval, decoded.interval, "interval");
            assertEquals(properties.getChargeCount(), decoded.getChargeCount(), "charge count");
            assertEquals(
                    PowerUpProperties.CODEC.encodeStart(JsonOps.INSTANCE, properties).getOrThrow(),
                    PowerUpProperties.CODEC.encodeStart(JsonOps.INSTANCE, decoded).getOrThrow(),
                    "the network form and the data form disagree"
            );
        }

        @Test
        @DisplayName("survives a round trip with no charge entity at all")
        void roundTripsWithoutEntities() {
            var properties = new PowerUpProperties(PowerUpProperties.ChargeCounting.ONLY_DECREASE, 1);

            var buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), TestBootstrap.registries());
            PowerUpProperties.STREAM_CODEC.encode(buf, properties);
            var decoded = PowerUpProperties.STREAM_CODEC.decode(buf);

            assertEquals(1, decoded.getChargeCount(), "charge count");
            assertTrue(decoded.isAtMax(), "a fresh set of properties should come back fully charged");
        }
    }
}
