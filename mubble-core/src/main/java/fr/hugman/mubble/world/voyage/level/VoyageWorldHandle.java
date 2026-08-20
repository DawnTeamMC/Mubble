package fr.hugman.mubble.world.voyage.level;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

/**
 * A lease on a level that a voyage trial is running in.
 *
 * <p>Only the provider that opened a handle may close it. A handle is single-use: once closed, the
 * level it points at is gone (or has been returned to a pool) and the handle is dead. Callers must
 * not hold on to {@link #level()} across a close.
 */
public interface VoyageWorldHandle {
    /**
     * {@return the level this handle leases}
     *
     * @throws IllegalStateException if the handle has been closed
     */
    ServerLevel level();

    /** {@return the dimension key of the leased level} */
    ResourceKey<Level> dimension();

    /** {@return whether this handle is still open} */
    boolean isOpen();
}
