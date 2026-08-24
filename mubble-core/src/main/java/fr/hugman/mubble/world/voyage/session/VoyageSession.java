package fr.hugman.mubble.world.voyage.session;

import fr.hugman.mubble.world.voyage.NodeInstance;
import fr.hugman.mubble.world.voyage.VoyageDefinition;
import fr.hugman.mubble.world.voyage.VoyageNode;
import fr.hugman.mubble.world.voyage.level.VoyageWorldHandle;
import java.util.List;
import java.util.UUID;

import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

/**
 * One player's voyage, in progress.
 *
 * <p>Purely in memory, and deliberately so. Node levels are temporary and do not survive a restart,
 * so there would be nothing for a saved session to point at; what has to survive is the
 * {@link PlayerStash}, which {@link VoyageSessionData} keeps. A restart ends voyages, it does not
 * resume them.
 *
 * <p>Position in the voyage is a node key, not an index. That is what lets routes branch and rejoin:
 * the key is the node's address, so it is the same whichever way the player got there, and so is the
 * seed derived from it.
 */
public final class VoyageSession {
    private final UUID playerId;
    private final Identifier voyageId;
    private final VoyageDefinition voyage;
    private final long seed;

    private @Nullable String nodeKey;
    private int trialsEntered;
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

    /** {@return the key of the node the player is in}, or {@code null} before the first one. */
    public @Nullable String nodeKey() {
        return this.nodeKey;
    }

    public @Nullable VoyageNode node() {
        return this.nodeKey == null ? null : this.voyage.node(this.nodeKey);
    }

    /** {@return how many trials the player has entered}, waystations not counted. */
    public int trialNumber() {
        return this.trialsEntered;
    }

    /** {@return how many trials the longest route runs} — see {@link VoyageDefinition#longestTrialCount}. */
    public int trialCount() {
        return this.voyage.longestTrialCount();
    }

    /** {@return where this node can lead}, empty when the voyage ends here. */
    public List<String> routes() {
        VoyageNode node = this.node();
        return node == null ? List.of() : node.next();
    }

    public @Nullable VoyageWorldHandle handle() {
        return this.handle;
    }

    /** {@return the instance for {@code key}}, having moved the session on to it. */
    NodeInstance moveTo(String key) {
        this.nodeKey = key;
        VoyageNode node = this.voyage.node(key);
        if (node.isTrial()) {
            this.trialsEntered++;
        }
        return NodeInstance.of(node.contentId(), key, node.content(), this.seed);
    }

    void setHandle(@Nullable VoyageWorldHandle handle) {
        this.handle = handle;
    }
}
