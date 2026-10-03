package fr.hugman.mubble.test.unit.arcade;

import com.google.gson.JsonParser;
import fr.hugman.mubble.Mubble;
import fr.hugman.mubble.references.ArcadeMoveIds;
import fr.hugman.mubble.references.ArcadeProfileIds;
import fr.hugman.mubble.tags.ArcadeMoveTags;
import fr.hugman.mubble.test.unit.support.CodecAssertions;
import fr.hugman.mubble.test.unit.support.Registrations;
import fr.hugman.mubble.world.arcade.ArcadeProfile;
import fr.hugman.mubble.world.arcade.ArcadeUnlocks;
import fr.hugman.mubble.world.arcade.access.AccessMode;
import fr.hugman.mubble.world.arcade.access.ArcadeSource;
import fr.hugman.mubble.world.arcade.access.MoveSelector;
import fr.hugman.mubble.world.arcade.move.ArcMove;
import fr.hugman.mubble.world.arcade.move.ArcadeMoves;
import fr.hugman.mubble.world.arcade.move.MoveSettings;
import fr.hugman.mubble.world.arcade.replay.ArcadeRecording;
import fr.hugman.mubble.world.arcade.sim.ArcadeInputFrame;
import fr.hugman.mubble.world.arcade.sim.ArcadeState;
import fr.hugman.mubble.world.arcade.sim.MoveResult;
import fr.hugman.mubble.world.item.component.ArcadeMovementComponent;
import fr.hugman.mubble.world.reward.ArcadeMoveReward;
import fr.hugman.mubble.world.reward.Reward;
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
 * The data of the arcade layer: what profiles, sources, components and rewards accept and refuse,
 * and the network shapes of the simulation.
 */
public class ArcadeCodecTest {
    @BeforeAll
    static void registerContent() {
        Registrations.registerEverything();
    }

    @Test
    @DisplayName("a move selector reads both moves and tags")
    void moveSelectorsReadMovesAndTags() {
        var tag = MoveSelector.parse("#mubble:aerial").getOrThrow();
        assertEquals(MoveSelector.tag(ArcadeMoveTags.AERIAL), tag);
        assertEquals("#mubble:aerial", tag.toString());
        var move = MoveSelector.parse("mubble:backflip").getOrThrow();
        assertEquals(MoveSelector.move(ArcadeMoveIds.BACKFLIP), move);
        assertEquals("mubble:backflip", move.toString());
    }

    @Test
    @DisplayName("items cannot deny a move")
    void itemsCannotDeny() {
        CodecAssertions.assertRejects(AccessMode.ITEM_CODEC, JsonParser.parseString("\"deny\""));
        CodecAssertions.assertRejects(ArcadeMovementComponent.CODEC, JsonParser.parseString("{\"slots\": \"feet\", \"moves\": {\"mubble:jump\": \"deny\"}}"));
    }

    @Test
    @DisplayName("the arcade movement component survives both round trips")
    void componentRoundTrips() {
        var component = new ArcadeMovementComponent(Optional.of(ArcadeProfileIds.OVERWORLD), EquipmentSlotGroup.FEET,
                Map.of(MoveSelector.tag(ArcadeMoveTags.ALL), AccessMode.ENABLE, MoveSelector.move(ArcadeMoveIds.DIVE), AccessMode.FORCE));
        CodecAssertions.assertJsonRoundTrip(ArcadeMovementComponent.CODEC, component);
        CodecAssertions.assertStreamRoundTrip(ArcadeMovementComponent.STREAM_CODEC, component);
    }

    @Test
    @DisplayName("a source survives both round trips")
    void sourceRoundTrips() {
        var source = new ArcadeSource(Mubble.id("ruleset/no_air"), Optional.of(ArcadeProfileIds.TRIAL),
                Map.of(MoveSelector.tag(ArcadeMoveTags.AERIAL), AccessMode.DENY), 10, Optional.of(1200L));
        CodecAssertions.assertJsonRoundTrip(ArcadeSource.CODEC, source);
        CodecAssertions.assertStreamRoundTrip(ArcadeSource.STREAM_CODEC, source);
    }

    @Test
    @DisplayName("unlocks survive a round trip")
    void unlocksRoundTrip() {
        var unlocks = new ArcadeUnlocks(Set.of(ArcadeMoveIds.JUMP, ArcadeMoveIds.WALL_JUMP));
        CodecAssertions.assertJsonRoundTrip(ArcadeUnlocks.CODEC, unlocks);
        CodecAssertions.assertStreamRoundTrip(ArcadeUnlocks.STREAM_CODEC, unlocks);
    }

    @Test
    @DisplayName("the arcade_move reward reads moves and tags")
    void arcadeMoveRewardReads() {
        var json = JsonParser.parseString("{\"type\": \"mubble:arcade_move\", \"moves\": [\"mubble:backflip\", \"#mubble:wall\"]}");
        var reward = CodecAssertions.assertJsonRoundTrip(Reward.CODEC, new ArcadeMoveReward(List.of(MoveSelector.move(ArcadeMoveIds.BACKFLIP), MoveSelector.tag(ArcadeMoveTags.WALL))));
        var parsed = Reward.CODEC.parse(com.mojang.serialization.JsonOps.INSTANCE, json).getOrThrow();
        assertEquals(reward, parsed);
        CodecAssertions.assertRejects(Reward.CODEC, JsonParser.parseString("{\"type\": \"mubble:arcade_move\", \"moves\": []}"));
    }

    @Test
    @DisplayName("a parameter a move does not have is an error, not a silent default")
    void unknownParametersAreRejected() {
        var codec = MoveSettings.codecFor(ArcadeMoves.JUMP);
        CodecAssertions.assertRejects(codec, JsonParser.parseString("{\"params\": {\"hieght\": 2.2}}"));
        CodecAssertions.assertRejects(codec, JsonParser.parseString("{\"params\": {\"height\": -1.0}}"));
        var settings = codec.parse(com.mojang.serialization.JsonOps.INSTANCE, JsonParser.parseString("{\"params\": {\"height\": 3.0}}")).getOrThrow();
        assertEquals(3.0D, settings.get(ArcMove.HEIGHT));
        assertEquals(ArcMove.TICKS_TO_APEX.defaultValue(), settings.get(ArcMove.TICKS_TO_APEX), "a parameter left out should fall back to its default");
    }

    @Test
    @DisplayName("a profile survives a round trip")
    void profileRoundTrips() {
        var profile = ArcadeTestProfiles.withMoves(Map.of(
                ArcadeMoves.JUMP, ArcadeTestProfiles.settings(Map.of("height", 2.2D, "ticks_to_apex", 7.0D)),
                ArcadeMoves.ROLL, ArcadeTestProfiles.settings(Map.of("speed", 0.6D))
        ));
        CodecAssertions.assertJsonRoundTrip(ArcadeProfile.DIRECT_CODEC, profile);
        CodecAssertions.assertStreamRoundTrip(ArcadeProfile.DIRECT_STREAM_CODEC, profile);
    }

    @Test
    @DisplayName("the simulation state keeps every field through its encoding")
    void stateRoundTrips() {
        var state = new ArcadeState();
        state.move = ArcadeMoves.LONG_JUMP;
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
        var decoded = ArcadeState.read(new FriendlyByteBuf(Unpooled.wrappedBuffer(bytes)));
        assertArrayEquals(bytes, decoded.toBytes());
        assertTrue(state.sameAs(decoded));
        assertEquals(state.fingerprint(), decoded.fingerprint());
        decoded.vy += 1.0E-12D;
        assertNotEquals(state.fingerprint(), decoded.fingerprint(), "the fingerprint should see the smallest difference");
    }

    @Test
    @DisplayName("an input frame from a hostile client is clamped back into range")
    void inputFramesAreSanitized() {
        var frame = new ArcadeInputFrame(5, Float.NaN, 30.0F, Float.POSITIVE_INFINITY, false, (byte) 0, (byte) 1, (short) 5000, (short) -3);
        var safe = frame.sanitized();
        assertEquals(0.0F, safe.stickX());
        assertEquals(1.0F, safe.stickZ(), 1.0E-6F);
        assertEquals(0.0F, safe.viewYaw());
        assertEquals(1000, safe.jumpAgeMs());
        assertEquals(0, safe.actionAgeMs());
        var normal = new ArcadeInputFrame(5, 0.3F, -0.4F, 12.0F, true, (byte) 3, (byte) 1, (short) 20, (short) 0);
        var buf = new FriendlyByteBuf(Unpooled.buffer());
        normal.write(buf);
        assertEquals(normal, ArcadeInputFrame.read(buf));
    }

    @Test
    @DisplayName("a recording survives a round trip")
    void recordingRoundTrips() {
        var frame = new ArcadeInputFrame(1, 0.0F, 1.0F, 0.0F, true, (byte) 1, (byte) 1, (short) 10, (short) 0);
        var recording = new ArcadeRecording(new Vec3(1.0D, 2.0D, 3.0D), List.of(new ArcadeRecording.Step(frame, Vec3.ZERO,
                Optional.of(new MoveResult(0.1D, 0.2D, 0.0D, false, false, false)), Optional.of(123L))));
        CodecAssertions.assertJsonRoundTrip(ArcadeRecording.CODEC, recording);
    }
}
