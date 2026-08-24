package fr.hugman.mubble.world.voyage.level;

import fr.hugman.mubble.world.voyage.NodeInstance;

/**
 * Supplies the levels that voyage trials run in.
 *
 * <p>Vanilla has no notion of a temporary world, so this is the seam that hides whichever trick we
 * use to get one. Today that is Fantasy (see
 * {@code fr.hugman.mubble.world.voyage.level.fantasy}); the alternative, if runtime dimensions ever
 * stop being viable, is leasing from a fixed pool of pre-declared levels cleared between uses.
 *
 * <p>Nothing outside the implementation package may name Fantasy or touch the server's level map, so
 * that swapping strategies stays a one-class change. See {@code design/voyages_poc/implementation.md}.
 */
public interface VoyageWorldProvider {
    /**
     * Opens a level for one node of a voyage.
     *
     * <p>The returned level is live and tickable by the time this returns, so the caller can
     * teleport into it immediately. The level is empty: building the platform in it is the caller's
     * job, because what a node contains is not this seam's business.
     *
     * @param node the voyage node that will run in the level; it carries its own node seed
     * @return an open handle the caller must eventually pass to {@link #close}
     */
    VoyageWorldHandle open(NodeInstance node);

    /**
     * Releases a handle.
     *
     * <p>Implementations must leave nothing behind: no entry in the server's level map, no
     * references that keep the level alive, and no files on disk. Any player still inside the
     * level must be moved out before calling this.
     *
     * <p>Closing an already-closed handle is a no-op.
     */
    void close(VoyageWorldHandle handle);
}
