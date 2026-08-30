package fr.hugman.mubble.test.gametest.network;

import fr.hugman.mubble.network.protocol.common.custom.CollectCollectiblePayload;
import fr.hugman.mubble.network.protocol.common.custom.MubblePayloadTypes;
import fr.hugman.mubble.network.protocol.common.custom.PowerUpChangePayload;
import fr.hugman.mubble.network.protocol.common.custom.PowerUpTriggerPayload;
import fr.hugman.mubble.super_mario.references.SuperMarioPowerUpIds;
import fr.hugman.mubble.test.gametest.datapack.PowerUpFixtures;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Items;

import java.util.Optional;

/**
 * What the server sends to the client. These payloads carry power-ups, which are holders of a dynamic
 * registry, so unlike the rest of the codec tests they cannot run without a server.
 * <p>
 * A payload that encodes into more or fewer bytes than it decodes desynchronises everything sent
 * after it on the same connection, so each round trip also checks the buffer was drained.
 */
public class NetworkPayloadGameTest {
    @GameTest
    public void powerUpChangeRoundTrips(GameTestHelper helper) {
        var fire = PowerUpFixtures.registry(helper).getOrThrow(SuperMarioPowerUpIds.FIRE);
        var ice = PowerUpFixtures.registry(helper).getOrThrow(SuperMarioPowerUpIds.ICE);

        // Two different power-ups: swapping previous and next would survive a symmetric payload.
        var decoded = assertRoundTrip(helper, PowerUpChangePayload.STREAM_CODEC,
                new PowerUpChangePayload(Optional.of(fire), Optional.of(ice)));

        helper.assertTrue(decoded.previous().orElseThrow().is(SuperMarioPowerUpIds.FIRE), "the previous power-up changed on the way");
        helper.assertTrue(decoded.next().orElseThrow().is(SuperMarioPowerUpIds.ICE), "the next power-up changed on the way");

        helper.succeed();
    }

    @GameTest
    public void powerUpChangeCarriesTheAbsenceOfAPowerUp(GameTestHelper helper) {
        var fire = PowerUpFixtures.registry(helper).getOrThrow(SuperMarioPowerUpIds.FIRE);

        // Losing a power-up is sent as an empty "next", which is the shape that clears the client HUD.
        var lost = assertRoundTrip(helper, PowerUpChangePayload.STREAM_CODEC,
                new PowerUpChangePayload(Optional.of(fire), Optional.empty()));
        helper.assertTrue(lost.next().isEmpty(), "losing a power-up must arrive as an empty next");

        var gained = assertRoundTrip(helper, PowerUpChangePayload.STREAM_CODEC,
                new PowerUpChangePayload(Optional.empty(), Optional.of(fire)));
        helper.assertTrue(gained.previous().isEmpty(), "gaining a first power-up must arrive as an empty previous");

        helper.succeed();
    }

    /**
     * A data pack power-up is not on the client's copy of the registry by id alone, so this is the
     * case that breaks if the registry stops being synced.
     */
    @GameTest
    public void aDataPackPowerUpTravelsWhole(GameTestHelper helper) {
        var shooter = PowerUpFixtures.get(helper, PowerUpFixtures.SHOOTER);

        var decoded = assertRoundTrip(helper, PowerUpChangePayload.STREAM_CODEC,
                new PowerUpChangePayload(Optional.empty(), Optional.of(shooter)));

        var arrived = decoded.next().orElseThrow();
        helper.assertTrue(arrived.is(PowerUpFixtures.SHOOTER), "the power-up lost its identity on the way");
        helper.assertTrue(arrived.value().action().isPresent(), "the power-up arrived without its action");

        helper.succeed();
    }

    @GameTest
    public void collectCollectibleRoundTrips(GameTestHelper helper) {
        var payload = new CollectCollectiblePayload(
                BuiltInRegistries.ITEM.getId(Items.GOLD_INGOT),
                7,
                Optional.of(ParticleTypes.FLAME)
        );

        var decoded = assertRoundTrip(helper, CollectCollectiblePayload.STREAM_CODEC, payload);

        helper.assertValueEqual(decoded.itemId(), payload.itemId(), "the item id");
        helper.assertValueEqual(decoded.amount(), 7, "the amount collected");
        helper.assertTrue(decoded.particle().isPresent(), "the particle was dropped");

        helper.succeed();
    }

    @GameTest
    public void collectCollectibleCanTravelWithoutAParticle(GameTestHelper helper) {
        var decoded = assertRoundTrip(helper, CollectCollectiblePayload.STREAM_CODEC,
                new CollectCollectiblePayload(1, 1, Optional.empty()));

        helper.assertTrue(decoded.particle().isEmpty(), "an absent particle must stay absent");
        helper.succeed();
    }

    @GameTest
    public void powerUpTriggerRoundTrips(GameTestHelper helper) {
        var buf = buffer(helper);
        PowerUpTriggerPayload.STREAM_CODEC.encode(buf, PowerUpTriggerPayload.INSTANCE);

        helper.assertValueEqual(buf.readableBytes(), 0, "the trigger payload should carry no data at all");
        helper.assertTrue(PowerUpTriggerPayload.STREAM_CODEC.decode(buf) == PowerUpTriggerPayload.INSTANCE,
                "the trigger payload should always decode to its single instance");

        helper.succeed();
    }

    /** Each payload must be registered, or sending one throws at runtime rather than at build time. */
    @GameTest
    public void everyPayloadTypeIsDeclared(GameTestHelper helper) {
        helper.assertTrue(MubblePayloadTypes.COLLECT_COLLECTIBLE.equals(new CollectCollectiblePayload(0, 0, Optional.empty()).type()),
                "the collect payload does not report its own type");
        helper.assertTrue(MubblePayloadTypes.POWER_UP_CHANGE.equals(new PowerUpChangePayload(Optional.empty(), Optional.empty()).type()),
                "the power-up change payload does not report its own type");
        helper.assertTrue(MubblePayloadTypes.POWER_UP_TRIGGER.equals(PowerUpTriggerPayload.INSTANCE.type()),
                "the trigger payload does not report its own type");

        helper.succeed();
    }

    private static <T> T assertRoundTrip(GameTestHelper helper, StreamCodec<RegistryFriendlyByteBuf, T> codec, T value) {
        var buf = buffer(helper);

        codec.encode(buf, value);
        T decoded = codec.decode(buf);

        helper.assertValueEqual(buf.readableBytes(), 0, "bytes left unread after decoding " + value);
        return decoded;
    }

    private static RegistryFriendlyByteBuf buffer(GameTestHelper helper) {
        return new RegistryFriendlyByteBuf(Unpooled.buffer(), helper.getLevel().registryAccess());
    }
}
