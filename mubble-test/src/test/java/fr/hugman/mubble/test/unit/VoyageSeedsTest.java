package fr.hugman.mubble.test.unit;

import fr.hugman.mubble.world.voyage.VoyageSeeds;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Seed derivation for voyages.
 *
 * <p>This is the machinery behind shareable run codes, so the properties worth testing are not
 * "is it random" but "is it the same every time" and "are unrelated things unrelated".
 */
public class VoyageSeedsTest {
    @Test
    @DisplayName("the same voyage seed and node path always give the same node seed")
    void derivationIsStable() {
        assertEquals(VoyageSeeds.node(1234L, "0/left/2"), VoyageSeeds.node(1234L, "0/left/2"));
    }

    @Test
    @DisplayName("the derivation is pinned, because changing it invalidates every shared run code")
    void derivationIsPinned() {
        // If this fails, the algorithm changed. That is allowed, but it means a run code handed out
        // before the change produces a different voyage after it, so it is a decision and not a
        // refactor. Update the constants deliberately.
        assertEquals(3994824556335993965L, VoyageSeeds.node(0L, "0"));
        assertEquals(2232977016502454331L, VoyageSeeds.node(0L, "1"));
        assertEquals(4274635680365594508L, VoyageSeeds.node(1L, "0"));
    }

    @Test
    @DisplayName("adjacent node paths give unrelated seeds")
    void adjacentPathsDoNotGiveAdjacentSeeds() {
        // The reason this matters: pick() is a modulo, so seeds that differ by one would draw
        // consecutive indices out of a candidate list and every voyage would march through its
        // options in order. String.hashCode fails exactly here.
        long first = VoyageSeeds.node(0L, "0");
        long second = VoyageSeeds.node(0L, "1");

        assertNotEquals(first, second);
        assertTrue(Math.abs(first - second) > 1_000_000L,
                "node seeds for '0' and '1' came out " + Math.abs(first - second) + " apart");
    }

    @Test
    @DisplayName("the same node path under different voyage seeds gives different seeds")
    void voyageSeedMatters() {
        assertNotEquals(VoyageSeeds.node(0L, "0"), VoyageSeeds.node(1L, "0"));
    }

    @Test
    @DisplayName("salts split one seed into independent ones")
    void saltsAreIndependent() {
        long node = VoyageSeeds.node(42L, "0");

        assertNotEquals(VoyageSeeds.derive(node, "visual/sky_color"), VoyageSeeds.derive(node, "visual/fog_color"));
        assertEquals(VoyageSeeds.derive(node, "visual/sky_color"), VoyageSeeds.derive(node, "visual/sky_color"));
    }

    @Test
    @DisplayName("a pick is always a valid index")
    void picksAreInRange() {
        for (long seed = -50; seed <= 50; seed++) {
            int picked = VoyageSeeds.pick(seed, 4);
            assertTrue(picked >= 0 && picked < 4, seed + " picked index " + picked + " out of 4");
        }
        // Negative seeds are the interesting half: the remainder operator would return a negative
        // index here and blow up at the call site rather than in anything a test would look at.
        assertTrue(VoyageSeeds.pick(Long.MIN_VALUE, 3) >= 0);
    }

    @Test
    @DisplayName("picking from an empty list is refused rather than answered")
    void emptyCandidateListIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> VoyageSeeds.pick(0L, 0));
    }

    @Test
    @DisplayName("every candidate in a list is reachable")
    void picksCoverTheWholeList() {
        Set<Integer> seen = new HashSet<>();
        for (int i = 0; i < 200; i++) {
            seen.add(VoyageSeeds.pick(VoyageSeeds.node(i, "0"), 4));
        }
        assertEquals(Set.of(0, 1, 2, 3), seen, "some candidates could never be drawn");
    }
}
