package fr.hugman.mubble.world.voyage.level;

import net.minecraft.resources.Identifier;

/**
 * One trial as it exists inside a running voyage.
 *
 * <p>Phase 0 placeholder. The level provider only needs enough to name and size the level it opens;
 * everything a trial actually <em>is</em> (environment, objective, ruleset) arrives in phase 2 and
 * should be added here rather than passed alongside.
 *
 * @param id        the id of the trial definition this instance came from, used for logging and level naming
 * @param nodePath  this trial's position in the voyage, used as the node path for seed derivation
 */
public record TrialInstance(Identifier id, String nodePath) {
}
