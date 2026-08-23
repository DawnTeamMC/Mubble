package fr.hugman.mubble.world.voyage.trial;

import fr.hugman.mubble.world.voyage.VoyageSeeds;

import net.minecraft.resources.Identifier;

/**
 * One trial as it exists inside a running voyage: the definition, plus where in the voyage it sits.
 *
 * <p>The node seed is carried rather than recomputed. Every seed-dependent choice this trial makes —
 * which candidate a profile field resolves to today, and its layout, its loot and its modifiers
 * later — derives from this one number, so there is exactly one place to look when a run does not
 * reproduce, and no way for two call sites to disagree about what the seed was.
 *
 * @param id         the id of the definition this came from, used for logging and level naming
 * @param nodePath   this trial's address in the voyage tree
 * @param definition what the trial is
 * @param nodeSeed   {@link VoyageSeeds#node} of the voyage seed and the node path
 */
public record TrialInstance(Identifier id, String nodePath, TrialDefinition definition, long nodeSeed) {
    /** Derives the node seed rather than taking one, which is the only correct way to build these. */
    public static TrialInstance of(Identifier id, String nodePath, TrialDefinition definition, long voyageSeed) {
        return new TrialInstance(id, nodePath, definition, VoyageSeeds.node(voyageSeed, nodePath));
    }
}
