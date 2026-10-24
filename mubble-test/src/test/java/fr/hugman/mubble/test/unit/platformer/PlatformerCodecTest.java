package fr.hugman.mubble.test.unit.platformer;

import com.google.gson.JsonParser;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.platformer.references.PlatformerMoveIds;
import fr.hugman.mubble.platformer.references.PlatformerProfileIds;
import fr.hugman.mubble.platformer.tags.PlatformerMoveTags;
import fr.hugman.mubble.test.unit.support.CodecAssertions;
import fr.hugman.mubble.test.unit.support.Registrations;
import fr.hugman.mubble.platformer.PlatformerProfile;
import fr.hugman.mubble.platformer.PlatformerUnlocks;
import fr.hugman.mubble.platformer.access.AccessMode;
import fr.hugman.mubble.platformer.access.PlatformerSource;
import fr.hugman.mubble.platformer.access.MoveSelector;
import fr.hugman.mubble.platformer.move.ArcMove;
import fr.hugman.mubble.platformer.move.PlatformerMoves;
import fr.hugman.mubble.platformer.move.MoveSettings;
import fr.hugman.mubble.platformer.replay.PlatformerRecording;
import fr.hugman.mubble.platformer.sim.PlatformerInputFrame;
import fr.hugman.mubble.platformer.sim.PlatformerState;
import fr.hugman.mubble.platformer.sim.MoveResult;
import fr.hugman.mubble.platformer.item.PlatformerMovementComponent;
import fr.hugman.mubble.platformer.reward.PlatformerMoveReward;
import fr.hugman.mubble.platformer.reward.Reward;
import io.netty.buffer.Unpooled;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.phys.Vec3;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The data of the platformer layer: what profiles, sources, components and rewards accept and refuse,
 * and the network shapes of the simulation.
 */
public class PlatformerCodecTest {
    @BeforeAll
    static void registerContent() {
        Registrations.registerEverything();
    }

    @Test
    @DisplayName("a move selector reads both moves and tags")
    void moveSelectorsReadMovesAndTags() {
        var tag = MoveSelector.parse("#mubble:aerial").getOrThrow();
        assertEquals(MoveSelector.tag(PlatformerMoveTags.AERIAL), tag);
        assertEquals("#mubble:aerial", tag.toString());
        var move = MoveSelector.parse("mubble:backflip").getOrThrow();
        assertEquals(MoveSelector.move(PlatformerMoveIds.BACKFLIP), move);
        assertEquals("mubble:backflip", move.toString());
    }

    @Test
    @DisplayName("items cannot deny a move")
    void itemsCannotDeny() {
        CodecAssertions.assertRejects(AccessMode.ITEM_CODEC, JsonParser.parseString("\"deny\""));
        CodecAssertions.assertRejects(PlatformerMovementComponent.CODEC, JsonParser.parseString("{\"slots\": \"feet\", \"moves\": {\"mubble:jump\": \"deny\"}}"));
    }

    @Test
    @DisplayName("the platformer movement component survives both round trips")
    void componentRoundTrips() {
        var component = new PlatformerMovementComponent(Optional.of(PlatformerProfileIds.OVERWORLD), EquipmentSlotGroup.FEET,
                Map.of(MoveSelector.tag(PlatformerMoveTags.ALL), AccessMode.ENABLE, MoveSelector.move(PlatformerMoveIds.DIVE), AccessMode.FORCE));
        CodecAssertions.assertJsonRoundTrip(PlatformerMovementComponent.CODEC, component);
        CodecAssertions.assertStreamRoundTrip(PlatformerMovementComponent.STREAM_CODEC, component);
    }

    @Test
    @DisplayName("a source survives both round trips")
    void sourceRoundTrips() {
        var source = new PlatformerSource(Mubble.id("ruleset/no_air"), Optional.of(PlatformerProfileIds.TRIAL),
                Map.of(MoveSelector.tag(PlatformerMoveTags.AERIAL), AccessMode.DENY), 10, Optional.of(1200L));
        CodecAssertions.assertJsonRoundTrip(PlatformerSource.CODEC, source);
        CodecAssertions.assertStreamRoundTrip(PlatformerSource.STREAM_CODEC, source);
    }

    @Test
    @DisplayName("unlocks survive a round trip")
    void unlocksRoundTrip() {
        var unlocks = new PlatformerUnlocks(Set.of(PlatformerMoveIds.JUMP, PlatformerMoveIds.WALL_JUMP));
        CodecAssertions.assertJsonRoundTrip(PlatformerUnlocks.CODEC, unlocks);
        CodecAssertions.assertStreamRoundTrip(PlatformerUnlocks.STREAM_CODEC, unlocks);
    }

    @Test
    @DisplayName("the platformer_move reward reads moves and tags")
    void platformerMoveRewardReads() {
        var json = JsonParser.parseString("{\"type\": \"mubble:platformer_move\", \"moves\": [\"mubble:backflip\", \"#mubble:wall\"]}");
        var reward = CodecAssertions.assertJsonRoundTrip(Reward.CODEC, new PlatformerMoveReward(List.of(MoveSelector.move(PlatformerMoveIds.BACKFLIP), MoveSelector.tag(PlatformerMoveTags.WALL))));
        var parsed = Reward.CODEC.parse(com.mojang.serialization.JsonOps.INSTANCE, json).getOrThrow();
        assertEquals(reward, parsed);
        CodecAssertions.assertRejects(Reward.CODEC, JsonParser.parseString("{\"type\": \"mubble:platformer_move\", \"moves\": []}"));
    }

    @Test
    @DisplayName("a parameter a move does not have is an error, not a silent default")
    void unknownParametersAreRejected() {
        var codec = MoveSettings.codecFor(PlatformerMoves.JUMP);
        CodecAssertions.assertRejects(codec, JsonParser.parseString("{\"params\": {\"hieght\": 2.2}}"));
        CodecAssertions.assertRejects(codec, JsonParser.parseString("{\"params\": {\"height\": -1.0}}"));
        var settings = codec.parse(com.mojang.serialization.JsonOps.INSTANCE, JsonParser.parseString("{\"params\": {\"height\": 3.0}}")).getOrThrow();
        assertEquals(3.0D, settings.get(ArcMove.HEIGHT));
        assertEquals(ArcMove.TICKS_TO_APEX.defaultValue(), settings.get(ArcMove.TICKS_TO_APEX), "a parameter left out should fall back to its default");
    }

    @Test
    @DisplayName("a profile survives a round trip")
    void profileRoundTrips() {
        var profile = PlatformerTestProfiles.withMoves(Map.of(
                PlatformerMoves.JUMP, PlatformerTestProfiles.settings(Map.of("height", 2.2D, "ticks_to_apex", 7.0D)),
                PlatformerMoves.ROLL, PlatformerTestProfiles.settings(Map.of("speed", 0.6D))
        ));
        CodecAssertions.assertJsonRoundTrip(PlatformerProfile.DIRECT_CODEC, profile);
        CodecAssertions.assertStreamRoundTrip(PlatformerProfile.DIRECT_STREAM_CODEC, profile);
    }

    @Test
    @DisplayName("the simulation state keeps every field through its encoding")
    void stateRoundTrips() {
        var state = new PlatformerState();
        state.move = PlatformerMoves.LONG_JUMP;
        state.moveTicks = 7;
        state.moveSeq = 42;
        state.vx = 0.55D;
        state.vy = -0.123456789D;
        state.facing = 91.5F;
        state.jumpBufferMs = 95;
        state.chainIndex = 2;
        state.ledgeFace = 3;
        state.recordSlope(-0.5D, 0.6D, 4);
        var bytes = state.toBytes();
        var decoded = PlatformerState.read(new FriendlyByteBuf(Unpooled.wrappedBuffer(bytes)));
        assertArrayEquals(bytes, decoded.toBytes());
        assertTrue(state.sameAs(decoded));
        assertEquals(state.fingerprint(), decoded.fingerprint());
        decoded.vy += 1.0E-12D;
        assertNotEquals(state.fingerprint(), decoded.fingerprint(), "the fingerprint should see the smallest difference");
    }

    @Test
    @DisplayName("an input frame from a hostile client is clamped back into range")
    void inputFramesAreSanitized() {
        var frame = new PlatformerInputFrame(5, Float.NaN, 30.0F, Float.POSITIVE_INFINITY, false, (byte) 0, (byte) 1, (short) 5000, (short) -3);
        var safe = frame.sanitized();
        assertEquals(0.0F, safe.stickX());
        assertEquals(1.0F, safe.stickZ(), 1.0E-6F);
        assertEquals(0.0F, safe.viewYaw());
        assertEquals(1000, safe.jumpAgeMs());
        assertEquals(0, safe.actionAgeMs());
        var normal = new PlatformerInputFrame(5, 0.3F, -0.4F, 12.0F, true, (byte) 3, (byte) 1, (short) 20, (short) 0);
        var buf = new FriendlyByteBuf(Unpooled.buffer());
        normal.write(buf);
        assertEquals(normal, PlatformerInputFrame.read(buf));
    }

    @Test
    @DisplayName("a recording survives a round trip")
    void recordingRoundTrips() {
        var frame = new PlatformerInputFrame(1, 0.0F, 1.0F, 0.0F, true, (byte) 1, (byte) 1, (short) 10, (short) 0);
        var recording = new PlatformerRecording(new Vec3(1.0D, 2.0D, 3.0D), List.of(new PlatformerRecording.Step(frame, Vec3.ZERO,
                Optional.of(new MoveResult(0.1D, 0.2D, 0.0D, false, false, false)), Optional.of(123L))));
        CodecAssertions.assertJsonRoundTrip(PlatformerRecording.CODEC, recording);
    }
}
