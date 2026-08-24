package fr.hugman.mubble.world.voyage;

import net.minecraft.resources.Identifier;

/**
 * One node of a voyage as it exists in a running one: what it is, where it sits, and its seed.
 *
 * <p>The node seed is carried rather than recomputed. Every seed-dependent choice this node makes —
 * which candidate a profile field resolves to today, and its layout, its loot and its modifiers
 * later — derives from this one number, so there is exactly one place to look when a run does not
 * reproduce, and no way for two call sites to disagree about what the seed was.
 *
 * @param id       the id of the definition this came from, used for logging and advancement matching
 * @param nodePath this node's key in the voyage graph, which is also its address for seeding
 * @param content  what the node puts the player in
 * @param nodeSeed {@link VoyageSeeds#node} of the voyage seed and the node path
 */
public record NodeInstance(Identifier id, String nodePath, VoyageNodeContent content, long nodeSeed) {
    /** Derives the node seed rather than taking one, which is the only correct way to build these. */
    public static NodeInstance of(Identifier id, String nodePath, VoyageNodeContent content, long voyageSeed) {
        return new NodeInstance(id, nodePath, content, VoyageSeeds.node(voyageSeed, nodePath));
    }
}
