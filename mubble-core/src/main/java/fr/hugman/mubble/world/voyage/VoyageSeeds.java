package fr.hugman.mubble.world.voyage;

import java.nio.charset.StandardCharsets;

/**
 * Derives every random-looking value a voyage needs from its one seed.
 *
 * <p>The rule from the design document (§6.9) is absolute: <strong>no live rolls anywhere in voyage
 * code</strong>. Every choice is a pure function of the voyage seed and where in the voyage tree the
 * choice is being made. The reason is run codes: a short shareable string only means anything if two
 * players entering the same code get the same voyage, and a live roll makes the stream depend on the
 * route walked, so taking the left branch would silently change what the right branch would have
 * contained.
 *
 * <p>Derivation is hierarchical. A node seed comes from the voyage seed and the node path; anything
 * that node needs several independent choices for derives further from its own seed with a salt. The
 * same input always gives the same output, on any machine and in any order.
 */
public final class VoyageSeeds {
    private static final long FNV_PRIME = 0x100000001B3L;

    private VoyageSeeds() {
    }

    /**
     * {@return the seed for one node of a voyage}
     *
     * <p>This is the {@code hash(voyage_seed + node_path)} the design document specifies. The node
     * path is the node's position in the tree, so sibling branches get unrelated seeds and a node
     * keeps its seed no matter which route the player took to reach it.
     */
    public static long node(long voyageSeed, String nodePath) {
        return derive(voyageSeed, nodePath);
    }

    /**
     * {@return a sub-seed of {@code seed}, independent for each distinct {@code salt}}
     *
     * <p>Anywhere a node makes more than one choice, each choice derives its own seed with a salt
     * naming what it is choosing. Without that, two choices made from the same seed over lists of
     * the same length would always land on the same index — a trial whose sky and fog each pick from
     * four candidates would only ever show four of the sixteen combinations.
     */
    public static long derive(long seed, String salt) {
        // FNV-1a over the salt, started from a scrambled seed and finished by the SplitMix64
        // finaliser. String.hashCode would be the obvious choice and is stable across JVMs, but it
        // barely avalanches: "voyage/1" and "voyage/2" differ by one in the hash, so adjacent nodes
        // would draw adjacent indices out of a small candidate list and the voyage would look
        // patterned rather than varied.
        long hash = mix(seed);
        for (byte b : salt.getBytes(StandardCharsets.UTF_8)) {
            hash = (hash ^ (b & 0xFFL)) * FNV_PRIME;
        }
        return mix(hash);
    }

    /**
     * {@return an index into a list of {@code size} entries}
     *
     * @throws IllegalArgumentException if {@code size} is not positive; an empty candidate list has
     *                                  no answer, and returning one anyway would hide the authoring
     *                                  mistake that produced it
     */
    public static int pick(long seed, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Cannot pick from " + size + " candidates");
        }
        // The modulo bias here is around one part in 2^58 for any list a human would write.
        return (int) Math.floorMod(seed, (long) size);
    }

    /** The SplitMix64 finaliser: a bijection, so distinct inputs stay distinct. */
    private static long mix(long value) {
        long z = value;
        z = (z ^ (z >>> 30)) * 0xBF58476D1CE4E5B9L;
        z = (z ^ (z >>> 27)) * 0x94D049BB133111EBL;
        return z ^ (z >>> 31);
    }
}
