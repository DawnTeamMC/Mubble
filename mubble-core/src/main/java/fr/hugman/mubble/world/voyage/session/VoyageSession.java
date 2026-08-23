package fr.hugman.mubble.world.voyage.session;

import fr.hugman.mubble.world.voyage.VoyageDefinition;
import fr.hugman.mubble.world.voyage.level.VoyageWorldHandle;
import fr.hugman.mubble.world.voyage.trial.TrialDefinition;
import fr.hugman.mubble.world.voyage.trial.TrialInstance;
import java.util.UUID;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

/**
 * One player's voyage, in progress.
 *
 * <p>Purely in memory, and deliberately so. Trial levels are temporary and do not survive a restart,
 * so there would be nothing for a saved session to point at; what has to survive is the
 * {@link PlayerStash}, which {@link VoyageSessionData} keeps. A restart ends voyages, it does not
 * resume them.
 */
public final class VoyageSession {
    private final UUID playerId;
    private final Identifier voyageId;
    private final VoyageDefinition voyage;
    private final long seed;

    private int trialIndex = -1;
    private @Nullable VoyageWorldHandle handle;

    VoyageSession(UUID playerId, Identifier voyageId, VoyageDefinition voyage, long seed) {
        this.playerId = playerId;
        this.voyageId = voyageId;
        this.voyage = voyage;
        this.seed = seed;
    }

    public UUID playerId() {
        return this.playerId;
    }

    public Identifier voyageId() {
        return this.voyageId;
    }

    public VoyageDefinition voyage() {
        return this.voyage;
    }

    /** {@return the voyage seed} — everything else about this run derives from it. */
    public long seed() {
        return this.seed;
    }

    /** {@return which trial the player is in, counting from zero}, or {@code -1} before the first. */
    public int trialIndex() {
        return this.trialIndex;
    }

    /** {@return the trial number a player would recognise}, counting from one. */
    public int trialNumber() {
        return this.trialIndex + 1;
    }

    public int trialCount() {
        return this.voyage.trials().size();
    }

    public boolean isOnLastTrial() {
        return this.trialIndex >= this.trialCount() - 1;
    }

    public @Nullable VoyageWorldHandle handle() {
        return this.handle;
    }

    /** {@return the instance for the next trial}, having moved the session on to it. */
    TrialInstance advanceToNextTrial() {
        this.trialIndex++;
        Holder<TrialDefinition> holder = this.voyage.trials().get(this.trialIndex);
        return TrialInstance.of(
                holder.unwrapKey().orElseThrow().identifier(),
                VoyageDefinition.nodePath(this.trialIndex),
                holder.value(),
                this.seed);
    }

    void setHandle(@Nullable VoyageWorldHandle handle) {
        this.handle = handle;
    }
}
