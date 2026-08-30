package fr.hugman.mubble.test.unit;

import fr.hugman.mubble.super_mario.world.level.block.BumpableDropMode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * The drop mode of a bumpable block is cycled through by the player, stored as an index in the block
 * entity and written as a string in NBT, so the three views of it have to agree.
 */
public class BumpableDropModeTest {
    @Test
    @DisplayName("cycling through the modes comes back round")
    void nextCyclesThroughEveryMode() {
        var mode = BumpableDropMode.ALL;

        for (int i = 0; i < BumpableDropMode.values().length; i++) {
            mode = mode.next();
        }

        assertSame(BumpableDropMode.ALL, mode, "cycling through every mode should come back to the first");
        assertSame(BumpableDropMode.ONE, BumpableDropMode.ALL.next(), "all should be followed by one");
    }

    @Test
    @DisplayName("every mode is reachable by its index and by its name")
    void everyModeIsReachable() {
        for (BumpableDropMode mode : BumpableDropMode.values()) {
            assertSame(mode, BumpableDropMode.get(mode.getIndex()), () -> mode + " is not reachable by index");
            assertSame(mode, BumpableDropMode.get(mode.getSerializedName()), () -> mode + " is not reachable by name");
        }
    }

    @Test
    @DisplayName("indices are unique and start at zero")
    void indicesAreContiguous() {
        var indices = Arrays.stream(BumpableDropMode.values()).mapToInt(BumpableDropMode::getIndex).sorted().toArray();

        for (int i = 0; i < indices.length; i++) {
            assertEquals(i, indices[i], "the indices must stay contiguous from zero, or next() skips a mode");
        }
    }

    @Test
    @DisplayName("an unknown index or name falls back on dropping everything")
    void unknownValuesFallBackOnAll() {
        assertSame(BumpableDropMode.ALL, BumpableDropMode.get(-1), "a negative index");
        assertSame(BumpableDropMode.ALL, BumpableDropMode.get(BumpableDropMode.values().length), "an index past the end");
        assertSame(BumpableDropMode.ALL, BumpableDropMode.get("not_a_mode"), "an unknown name");
    }

    @Test
    @DisplayName("the codec reads back what the enum writes")
    void codecAgreesWithTheSerializedNames() {
        for (BumpableDropMode mode : BumpableDropMode.values()) {
            var encoded = BumpableDropMode.CODEC.encodeStart(com.mojang.serialization.JsonOps.INSTANCE, mode)
                    .getOrThrow(error -> new AssertionError("could not encode " + mode + ": " + error));
            var decoded = BumpableDropMode.CODEC.parse(com.mojang.serialization.JsonOps.INSTANCE, encoded)
                    .getOrThrow(error -> new AssertionError("could not decode " + encoded + ": " + error));

            assertSame(mode, decoded, "the codec lost the mode");
            assertEquals(mode.getSerializedName(), encoded.getAsString(), "the codec and getSerializedName disagree");
        }
    }
}
