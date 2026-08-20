package fr.hugman.mubble.world.voyage.level;

/**
 * Supplies the levels that voyage trials run in.
 *
 * <p>Vanilla has no notion of a temporary world, so this is the seam that hides whichever trick we
 * use to get one. Today that is Fantasy (see
 * {@code fr.hugman.mubble.world.voyage.level.fantasy}); the alternative, if runtime dimensions ever
 * stop being viable, is leasing from a fixed pool of pre-declared levels cleared between uses.
 *
 * <p>Nothing outside the implementation package may name Fantasy or touch the server's level map, so
 * that swapping strategies stays a one-class change. See {@code docs/runtime-worlds.md}.
 */
public interface VoyageWorldProvider {
    /**
     * Opens a level for a trial.
     *
     * <p>The returned level is live and tickable by the time this returns, so the caller can
     * teleport into it immediately.
     *
     * @param trial the trial that will run in the level
     * @param seed  the node seed for this trial, derived from the voyage seed
     * @return an open handle the caller must eventually pass to {@link #close}
     */
    VoyageWorldHandle open(TrialInstance trial, long seed);

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
